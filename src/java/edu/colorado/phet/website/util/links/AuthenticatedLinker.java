package edu.colorado.phet.website.util.links;

/**
 * AbstractLinker for pages that require login
 */
public abstract class AuthenticatedLinker extends AbstractLinker implements RawLinkable {

    @Override
    public boolean requireHttpsIfAvailable() {
        return true;
    }

}
