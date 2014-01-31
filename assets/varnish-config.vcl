# Varnish configuration for in front of Apache HTTPD and Tomcat

# TODO: ESI Invalidation types:
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

backend default {
  # simian.colorado.edu. will need to change for production (figaro)
  .host = "128.138.133.42";
  .port = "81";
  .probe = {
    .request = "GET /services/varnish-health-check HTTP/1.1"
               "Host: phet.colorado.edu"
               "Connection: close";
    .timeout = 0.5s;
    .window = 10;
    .threshold = 8;
    .interval = 1s;
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
  if ( req.backend.healthy ) {
    set req.grace = 30s;
  } else {
    set req.grace = 1h;
  }
  
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
  
  # static files for Tomcat
  if ( req.url ~ "^/images/" ||
       req.url ~ "^/js/" ||
       req.url ~ "^/css/" ||
       req.url ~ "^/favicon.ico$" ||
       req.url ~ "^/google00b23c7fe7eabc63.html$" ||
       req.url ~ "^/crossdomain.xml$" ) {
    return (lookup);
  }
  
  # TODO: /publications, /workshops, /files, /installer, /newsletters, /blog?, /oai?
  
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
         req.http.minus-locale ~ "^/simulations/keyword/([^/]+)" ||
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
    if ( req.http.host ~ "phet-data.\.colorado\.edu" ) { # like phet-data1.colorado.edu
      hash_data( "data-host" );
    } else {
      hash_data( req.http.host );
    }
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
  set beresp.grace = 1h;
  
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
