package edu.colorado.phet.website.admin;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.website.components.LocalizedText;
import edu.colorado.phet.website.data.Category;
import edu.colorado.phet.website.menu.NavLocation;
import edu.colorado.phet.website.util.HibernateTask;
import edu.colorado.phet.website.util.HibernateUtils;

public class AdminCategoriesPage extends AdminPage {
    public AdminCategoriesPage( PageParameters parameters ) {
        super( parameters );

        final List<Category> categories = new LinkedList<Category>();

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                Category root = Category.getRootCategory( session );
                addCategory( root );
                return true;
            }

            private void addCategory( Category category ) {
                if ( !category.isRoot() ) {
                    categories.add( category );
                }
                for ( Object o : category.getSubcategories() ) {
                    addCategory( (Category) o );
                }
            }
        } );

        add( new ListView( "categories", categories ) {
            protected void populateItem( ListItem item ) {
                final Category category = (Category) item.getModel().getObject();

                Component titleComponent;
                titleComponent = new LocalizedText( "title", category.getNavLocation( getNavMenu() ).getLocalizationKey() );
                titleComponent.add( new AttributeAppender( "style", new Model( "margin-left: " + String.valueOf( category.getDepth() * 20 ) + "px;" ), "" ) );

                Link categoryLink = new Link( "category-link" ) {
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.put( "categoryId", category.getId() );
                        setResponsePage( AdminCategoryPage.class, params );
                    }
                };

                categoryLink.add( titleComponent );
                item.add( categoryLink );
            }
        } );
    }
}
