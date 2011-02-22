/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.util.wicket;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.*;
import org.apache.wicket.behavior.AbstractHeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.protocol.http.*;
import org.apache.wicket.protocol.http.request.WebErrorCodeResponseTarget;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.response.StringResponse;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.util.ClassAppender;

public class WicketUtils {

    /**
     * Highlight every other row of a list view. Call within the populateItem( ListItem item ) loop.
     *
     * @param item The list item to possibly highlight
     */
    public static void highlightListItem( ListItem item ) {
        if ( item.getIndex() % 2 == 0 ) {
            item.add( new ClassAppender( "list-highlight-background" ) );
        }
    }

    /**
     * Display the specified component from the factory if visible is true, otherwise don't show anything.
     * <p/>
     * Had this been a language like Scala or Clojure, we could make this into a much nicer-to-read version.
     *
     * @param visible Whether to show the factory component (true) or invisible component (false)
     * @param id      String id to give the component. Will be passed to the factory
     * @param factory Factory to create a component
     * @return A component
     */
    public static Component componentIf( boolean visible, String id, IComponentFactory<? extends Component> factory ) {
        return ( visible ? factory.create( id ) : new InvisibleComponent( id ) );
    }

    public static Component componentIf( boolean visible, String id, Component component ) {
        return ( visible ? component : new InvisibleComponent( id ) );
    }

    public static String renderPage( Class<? extends Page> pageClass, PageParameters pageParameters ) {
        //get the servlet context
        WebApplication application = WebApplication.get();

        ServletContext context = application.getServletContext();

        //fake a request/response cycle
        MockHttpSession servletSession = new MockHttpSession( context );
        servletSession.setTemporary( true );

        MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                application, servletSession, context );
        MockHttpServletResponse servletResponse = new MockHttpServletResponse(
                servletRequest );

        //initialize request and response
        servletRequest.initialize();
        servletResponse.initialize();

        WebRequest webRequest = new ServletWebRequest( servletRequest );

        BufferedWebResponse webResponse = new BufferedWebResponse( servletResponse );
        webResponse.setAjax( true );

        WebRequestCycle requestCycle = new WebRequestCycle(
                application, webRequest, webResponse );

        requestCycle.setRequestTarget( new BookmarkablePageRequestTarget( pageClass, pageParameters ) );

        try {
            //requestCycle.request();
            requestCycle.getProcessor().respond( requestCycle );

//			log.warn("Response after request: "+webResponse.toString());

            if ( !requestCycle.wasHandled() ) {
                requestCycle.setRequestTarget( new WebErrorCodeResponseTarget(
                        HttpServletResponse.SC_NOT_FOUND ) );
            }
            requestCycle.detach();

        }
        finally {
            requestCycle.getResponse().close();
        }

        return webResponse.toString();
    }

    /**
     * Render a component into a string by replacing the donor component with it temporarily and using a fake response
     * TODO: analyze the consequences of rendering these within a cachable panel render. possible bad things!
     *
     * @param parentDonorComponent The component to (temporarily) replace
     * @param component            The component to render in to a string
     * @return A rendered form of the component
     */
    public static String renderToString( final Component parentDonorComponent, final Component component ) {
        if ( !component.getId().equals( parentDonorComponent.getId() ) ) {
            throw new IllegalStateException( "Component will try to substitute parentDonorComponent to render. Donor and string render Component id's must be equal." );
        }

        final Response originalResponse = RequestCycle.get().getResponse();
        StringResponse stringResponse = new StringResponse();
        RequestCycle.get().setResponse( stringResponse );
        MarkupContainer parentComponent = parentDonorComponent.getParent();
        parentComponent.remove( parentDonorComponent );

        try {
            parentComponent.add( component );

            try {
                component.prepareForRender();
                component.renderComponent();
            }
            catch( RuntimeException e ) {
                component.afterRender();
                throw e;
            }
        }
        finally {
            // Restore original component
            parentComponent.replace( parentDonorComponent );
            // Restore original response
            RequestCycle.get().setResponse( originalResponse );
        }

        return stringResponse.toString();
    }

    /**
     * Adds a raw string into the header
     *
     * @param str String to add
     * @return HeaderContributor to add
     */
    public static AbstractHeaderContributor createStringHeaderContributor( final String str ) {
        return new AbstractHeaderContributor() {
            @Override
            public IHeaderContributor[] getHeaderContributors() {
                return new IHeaderContributor[]{
                        new IHeaderContributor() {
                            public void renderHead( IHeaderResponse response ) {
                                response.renderString( str );
                            }
                        }
                };
            }
        };
    }

}
