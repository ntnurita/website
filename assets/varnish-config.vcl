
# TODO: Figure out a good way of invalidating things. We DO NOT want to invalidate, have Tomcat crash, and not have a backup. Stale is great then. Notes about that:
#     see https://www.varnish-software.com/static/book/Cache_invalidation.html for invalidation strategies (do we want to use req.hash_always_miss = true)?
#     is there a way to skip ban checks if the server is down? or do we have to invalidate every single page recorded?
#     evidence to the contrary: https://www.varnish-cache.org/lists/pipermail/varnish-misc/2012-April/021942.html
#     investigate https://www.varnish-cache.org/vmod/soft-purge, or come up with a custom vmod to make this happen? See https://github.com/lkarsten/libvmod-softpurge
#     note: we could customize the vmod to hit the URLs we need, like a ban?
#     investigate softban patch: https://www.varnish-cache.org/patchwork/patch/30/
#     and softban commit from https://github.com/mbgrydeland/varnish-cache/commit/59b92073daf812ff816b4da4433e8b6c5b0fa431, did it make it into a production version?
#     and RFC https://www.varnish-cache.org/lists/pipermail/varnish-dev/2012-October/007297.html
#     Drupal notes for softban: https://drupal.org/node/2138291, with https://www.varnish-cache.org/patchwork/patch/30/ and
#                               https://github.com/mbgrydeland/varnish-cache/commit/59b92073daf812ff816b4da4433e8b6c5b0fa431
#     Better to set up a second "backup" backend that properly stores safe snapshots, and serves them correctly?
#     How to fail-over nicely here? Do we always do a health-check in vcl_recv?

# TODO: Vary on Accept-Encoding for .jnlp files only (we can serve it with pack200, presumably still through Apache)

# QUESTION: How can we use Varnish for HTTPS content? Do we need to put something in front of Varnish for HTTPS, or do we just not cache any of that content?

# QUESTION: We have a few DNS aliases, like phet-data1.colorado.edu. Should we trigger more aggressive caching for these? If we only want to cache one copy for each 1,2,3,4,
#           should we not put that in vcl_hash?

# QUESTION: If we can use Varnish with HTTPS, it would be nice to use ESI to construct portions of the page. Will this work with compression, HTTPS, etc.? Would allow less
#           memory in Tomcat.

# QUESTION: If vcl_hash has added data, is that like implicitly varying on that?

# TODO: The following are Apache-served directories, how should we handle them?
#     /sims:
#       We're currently setting Cache-control: max-age=0, must-revalidate
#       Serving pack200s
#     /publications:
#       Very cacheable
#     /workshops:
#       Very cacheable
#     /files:
#       Very cacheable, but needs to support "added" files instantly (e.g. activities)
#     /installer:
#       Very cacheable, but invalidate manually once a new installer is uploaded
#       Probably doesn't benefit from caching
#     /newsletters:
#       Very cacheable, but need ability to manually invalidate while working on the newsletter
#       (consider 304s)
#     /statistics:
#       Never cache
#     /staging:
#       Never cache (deployment testing needs to work well)
#     /dev:
#       This gets redirected, no?
#     /blog:
#       Presumably only bypass cache if the req.url is wp-login or wp-admin (see below)
#         Otherwise cache somewhat aggressively?
#       See https://www.varnish-cache.org/trac/wiki/VarnishAndWordpress
#         # Drop any cookies sent to Wordpress.
#         sub vcl_recv {
#           if (!(req.url ~ "wp-(login|admin)")) {
#             unset req.http.cookie;
#           }
#         }
#         
#         # Drop any cookies Wordpress tries to send back to the client.
#         sub vcl_fetch {
#           if (!(req.url ~ "wp-(login|admin)")) {
#             unset beresp.http.set-cookie;
#           }
#         }


# TODO: Invalidation types:
#   Invalidate the HTML world (all HTML pages) on:
#     Publicly-visible translation change
#     Project / Simulation / LocalizedSimulation changes
#   also Invalidate category pages on:
#     Detailed category changes
#   also Invalidate sim pages on:
#     Detailed project changes
#     Detailed teacher guide changes
#   also Invalidate home pages every X hours, so no-JS sponsor rotates?
#   also Invalidate contribution pages on contribution changes, along with project/simulation changes, etc.
#   Other dependencies:
#     NewsChangelogPanel (wherever that is used) invalidate on project/sim/lsim
#     TranslationLinksPanel: on translation changes (Translation change invalidator)
#     FeaturedSponsorPanel: strings
#     SponsorsPanel: strings

# TODO: How to handle the longer-term caching headers that Tomcat is sending right now:
#     PostFilter is explicitly allowing caching for:
#       /images/
#       /js/ (we rename here anyways)
#       /css/ (we rename here anyways)
#       /favicon.ico
#       /crossdomain.xml (for flash sims sending stats)
#     Cache is 30 days, with:
#       Cache-control: public
#       Expires: <30 days from now>
#       NOTES: says Vary: Accept-Encoding is already added by Tomcat or Apache, we NEED to keep this in presumably


# TODO: Should we be caching /oai requests? Are those expensive? (They hit a different servlet)


backend default {
  # simian.colorado.edu. will need to change for production (figaro)
  .host = "128.138.133.42";
  .port = "81";
  .probe = {
    .url = "/services/varnish-health-check";
  }
}

# these should be localhost and the IPs that we will have for production and testing
acl local {
  "localhost";
  "128.138.133.42"; # simian.colorado.edu
  "128.138.128.88"; # figaro.colorado.edu
  "128.138.128.89"; # phet.colorado.edu
}

# Pass through everything for now
sub vcl_recv {
  # entry points for purges
  if ( req.request == "PURGE" ) {
    if ( !client.ip ~ local ) {
      error 405 "Not allowed.";
    }
    return (lookup);
  }
  
  # entry point for bans. accepts regex
  # TODO: should we hard-code in some specific bans?
  # TODO: if we issue a ton of bans, will they accumulate and cause CPU issues? check with CLI ban.list command
  if ( req.request == "BAN" ) {
    if ( !client.ip ~ local ) {
      error 405 "Not allowed.";
    }
    
    ban( "obj.http.x-url ~ " + req.url ); # assumes req.url is a PCRE
    
    error 200 "Ban added";
  }
  
  if ( req.restarts == 0 ) {
      if ( req.http.x-forwarded-for ) {
          set req.http.X-Forwarded-For = req.http.X-Forwarded-For + ", " + client.ip;
      } else {
          set req.http.X-Forwarded-For = client.ip;
      }
  }
  
  # check for non-RFC2616 or CONNECT
  if ( req.request != "GET" &&
       req.request != "HEAD" &&
       req.request != "PUT" &&
       req.request != "POST" &&
       req.request != "TRACE" &&
       req.request != "OPTIONS" &&
       req.request != "DELETE" ) {
    return (pipe);
  }
  
  if ( req.request != "GET" && req.request != "HEAD" ) {
    # We only deal with GET and HEAD by default
    return (pass);
  }
  
  # HTTP Auth on login
  if ( req.http.Authorization ) {
    return (pass);
  }
  
  # using example from https://www.varnish-cache.org/docs/3.0/tutorial/cookies.html for stripping out undesired cookies
  # everything that isn't a JSESSIONID or sign-in-panel.sign-in-form.username is cut (the latter is used for remembering the login name)
  if ( req.http.Cookie ) {
    set req.http.Cookie = ";" + req.http.Cookie;
    set req.http.Cookie = regsuball(req.http.Cookie, "; +", ";");
    set req.http.Cookie = regsuball(req.http.Cookie, ";(JSESSIONID|sign-in-panel\.sign-in-form.username)=", "; \1=");
    set req.http.Cookie = regsuball(req.http.Cookie, ";[^ ][^;]*", "");
    set req.http.Cookie = regsuball(req.http.Cookie, "^[; ]+|[; ]+$", "");
    
    
    if ( req.http.Cookie == "" ) {
      remove req.http.Cookie;
    } else {
      # if it has a JSESSIONID, we'll need to pass it through to Tomcat for proper processing for now.
      # TODO: use ESI for this purpose, so we can still cache most of a page?
      return (pass);
    }
  }
  
  # force the installer ripper to bypass the cache, since we change behavior for rips (and want to avoid polluting the cache with installer versions)
  if ( req.http.User-Agent ~ "^httrack" ) {
    return (pass);
  }
  
  # force another user-agent customization to bypass the cache, to avoid polluting it
  if ( req.http.User-Agent ~ "^hide-translations$" ) {
    return (pass);
  }
  
  # cache pages that aren't relative to a locale
  if ( req.url == "/" ) { return (lookup); }
  if ( req.url ~ "^/autocomplete" ) { return (lookup); } # TODO: should we not include this?
  if ( req.url ~ "^/sims/.+\.(png|jpg)" ) { return (lookup); } # thumbnails and screenshots
  
  # TODO: /publications, /workshops, /files, /installer, /newsletters, /blog?
  
  # check to see if we are relative to a locale (e.g. starts with "/en/" or "/pt_BR", etc.)
  if ( req.url ~ "^/\w\w(_\w\w)/" ) { # matches only ascii characters, but we have no locales with UTF-8 in the description
    set req.http.minus-locale = regsub( req.url, "^/\w\w(_\w\w)", "" ); # strip off the "/en" or "/pt_BR", but leave the next slash
    
    if ( req.http.minus-locale ~ "^/simulation/([^/]+)" ||
         req.http.minus-locale ~ "^/simulation/([^/])+/changelog" ||
         req.http.minus-locale ~ "^/simulations" ||
         req.http.minus-locale ~ "^/simulations/index" ||
         req.http.minus-locale ~ "^/simulations/category/(.+)" ||
         req.http.minus-locale ~ "^/simulations/category/(.+)/index" ||
         req.http.minus-locale ~ "^/simulations/translated" ||
         req.http.minus-locale ~ "^/simulations/translated/([^/]+)" ||
         req.http.minus-locale ~ "^/simulations/category/html" ||
         req.http.minus-locale ~ "^/simulations/category/by-level" ||
         req.http.minus-locale ~ "^/about" ||
         req.http.minus-locale ~ "^/about/contact" ||
         req.http.minus-locale ~ "^/about/licensing" ||
         req.http.minus-locale ~ "^/about/news" ||
         req.http.minus-locale ~ "^/about/source-code" ||
         req.http.minus-locale ~ "^/about/sponsors" ||
         req.http.minus-locale ~ "^/change-password-success" ||
         req.http.minus-locale ~ "^/donate" ||
         req.http.minus-locale ~ "^/faqs" ||
         req.http.minus-locale ~ "^/for-teachers" ||
         req.http.minus-locale ~ "^/for-teachers/activity-guide" ||
         req.http.minus-locale ~ "^/for-teachers/classroom-use" ||
         req.http.minus-locale ~ "^/for-teachers/legend" ||
         req.http.minus-locale ~ "^/for-teachers/workshops" ||
         req.http.minus-locale ~ "^/for-teachers/workshops/uganda" ||
         req.http.minus-locale ~ "^/for-teachers/workshops/uganda-photos" ||
         req.http.minus-locale ~ "^/for-translators" ||
         req.http.minus-locale ~ "^/for-translators/translation-utility" ||
         req.http.minus-locale ~ "^/get-phet" ||
         req.http.minus-locale ~ "^/get-phet/full-install" ||
         req.http.minus-locale ~ "^/get-phet/one-at-a-time" ||
         req.http.minus-locale ~ "^/research" ||
         req.http.minus-locale ~ "^/sim-faq-test" ||
         req.http.minus-locale ~ "^/stay-connected" ||
         req.http.minus-locale ~ "^/troubleshooting" ||
         req.http.minus-locale ~ "^/troubleshooting/java" ||
         req.http.minus-locale ~ "^/troubleshooting/flash" ||
         req.http.minus-locale ~ "^/troubleshooting/javascript" ||
         req.http.minus-locale ~ "^/troubleshooting/javaSecurity" ||
         req.http.minus-locale ~ "^/simulations/keyword/([^/]+)"" ||
         req.http.minus-locale ~ "^/search" || # TODO: decide on whether this si a good idea
         req.http.minus-locale ~ "^/for-teachers/browse-activities" || # TODO: decide on whether this si a good idea
         req.http.minus-locale ~ "^/media/tech-award-2011" ||
         req.http.minus-locale ~ "^/media/photos" ||
         req.http.minus-locale ~ "^/media/images" ||
         req.http.minus-locale ~ "^/teachwithphet" ||
         req.http.minus-locale ~ "^/contributions/view/([^/]+)" # TODO: need to invalidate this on any contribution changes!
      ) {
      
      unset req.http.minus-locale;
      
      return (lookup);
    }
    
    unset req.http.minus-locale;
  }
  
  return (pass);
}

sub vcl_hash {
  hash_data( req.url );
  hash_data( req.proto ); # we handle HTTP and HTTPS slightly differently in some cases
  if ( req.http.host ) {
    hash_data( req.http.host );
  } else {
    hash_data( server.ip );
  }
  return (hash);
}

sub vcl_pipe {
  return (pipe);
}

sub vcl_pass {
  return (pass);
}

sub vcl_hit {
  if ( req.request == "PURGE" ) {
    purge;
    error 200 "Purged.";
  }
  
  return (deliver);
}

sub vcl_miss {
  if ( req.request == "PURGE" ) {
    purge;
    error 200 "Purged.";
  }
  
  return (fetch);
}

sub vcl_fetch {
  if ( beresp.ttl <= 0s || beresp.http.Set-Cookie || beresp.http.Vary == "*" ) {
      # Mark as "Hit-For-Pass" for the next 2 minutes
      set beresp.ttl = 120 s;
      return (hit_for_pass);
  }
  
  set beresp.http.x-url = req.url; # store the URL for future bans that are lurker-friendly
  
  return (deliver);
}

sub vcl_deliver {
  unset resp.http.x-url; # strip off so they don't get sent to the client
  
  return (deliver);
}

sub vcl_error {
  # allow a false 800-status-code to trigger a restart instead
  if ( obj.status == 800 ) {
    return (restart);
  }
  
  set obj.http.Content-Type = "text/html; charset=utf-8";
  set obj.http.Retry-After = "5";
  synthetic {"
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <title>"} + obj.status + " " + obj.response + {"</title>
  </head>
  <body>
    <h1>Error "} + obj.status + " " + obj.response + {"</h1>
    <p>"} + obj.response + {"</p>
    <h3>Guru Meditation:</h3>
    <p>XID: "} + req.xid + {"</p>
    <hr>
    <p>Varnish cache server</p>
  </body>
</html>
"};
  return (deliver);
}

sub vcl_init {
  return (ok);
}

sub vcl_fini {
  return (ok);
}
