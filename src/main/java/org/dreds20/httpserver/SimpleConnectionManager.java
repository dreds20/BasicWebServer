package org.dreds20.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dreds20.config.Config;
import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpResponseImpl;
import org.dreds20.httpserver.model.HttpStatus;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.Page;
import org.dreds20.httpserver.pages.PageManager;

import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class is a simple implementtion of the ConnectionManager, and should be provided to the HttpServer
 * via its configuration. It is responsible for interpreting connections made to the HttpServer, looking up
 * matching pages to return from the
 * provided PageManager object.
 * <p/>
 * If no matching pages are found then a 404 is returned, if a
 * returned page does not contain matching HttpVerb's it will return a 405.
 * <p/>
 * If a valid page is returned, the ConnectionManager will use a provided ContentLoader to
 * retrieve a body, populate this into a valid response object, and return it.
 * <p/>
 * Any unexpected failures will produce a 500 error.
 */
public class SimpleConnectionManager implements ConnectionManager {
    private static final Logger log = LogManager.getLogger(SimpleConnectionManager.class);
    private final PageManager pageManager;
    private final ContentLoader contentLoader;
    private static final String HTML_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    /**
     * Constructor for the ConnectionManager, requires a PageManager to handle matching look up for requests
     * and ContentLoader to retrieve the body for to populate for matching responses
     *
     * @param pageManager a PageManager object is responsible for looking up requests to find matching Pages
     * @param contentLoader a ContentLoader object is responsible for loading the body from the resource specified
     *                      in the returned page from the PageManager look up
     */
    public SimpleConnectionManager(PageManager pageManager, ContentLoader contentLoader) {
        if (pageManager == null || contentLoader == null) {
            throw new NullPointerException("PageManager or ContentLoader object not provided");
        }
        this.pageManager = pageManager;
        this.contentLoader = contentLoader;
    }

    private URI getFullPath(String path) {
        String basePath = Config.getString("pages.basePath");
        if (basePath.isEmpty()) {
            return Paths.get(path).toAbsolutePath().toUri();
        } else {
            return Paths.get(basePath, path).toUri();
        }
    }

    private HttpResponseImpl.Builder addBody(HttpResponseImpl.Builder builder, String path) {
        try {
            return builder.body(contentLoader.getContent(getFullPath(path)));
        } catch (Exception e) {
            log.error("Error retrieving content for path: {}", path);
            return ConnectionManager.getFailureBuilder();
        }
    }

    /**
     * Using the provided request object, this method will extract the path and use this to query the PageManager
     * for a matching Page, and then use the path provided in the page to retrieve the body content via the
     * ContentManager, and return a response as a String.
     *
     * @param request an HttpRequest object that has been sent to the HttpServer
     *
     * @return an HttpResponse object
     */
    public HttpResponse getResponse(HttpRequest request) {
        Page page = null;
        try {
            page = pageManager.getPage(request.path().getRawPath());
        } catch (Exception e) {
            return ConnectionManager.getFailureResponse();
        }

        if (page == null) {
            // A null response means that the page is not listed in the database, so we should return a 404
            return HttpResponse.create(builder -> addBody(builder
                    .statusCode(HttpStatus.NOT_FOUND).putHeader("Content-Type", "text/html"), "pages/not_found.html"));
        }
        if (!page.verbs().contains(request.verb())) {
            // Page exists but the method is not listed
            return HttpResponse.create(builder -> builder.statusCode(HttpStatus.METHOD_NOT_ALLOWED));
        }

        String path = page.path();
        String contentType = page.contentType();

        return HttpResponse.create(builder -> addBody(builder
                .statusCode(HttpStatus.OK)
                .putHeader("Date", LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(HTML_DATE_PATTERN, Locale.ENGLISH).withZone(ZoneId.of("GMT"))))
                .putHeader("Content-Type", contentType), path));
    }

    @Override
    public String getResponseString(HttpRequest request) {
        return getResponse(request).toString();
    }
}
