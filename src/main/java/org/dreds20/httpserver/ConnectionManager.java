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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConnectionManager {
    private static final Logger log = LogManager.getLogger(ConnectionManager.class);
    private final PageManager pageManager;
    private final ContentLoader contentLoader;
    private static final String HTML_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final int TIMEOUT_SECONDS = 10;

    public ConnectionManager(PageManager pageManager, ContentLoader contentLoader) {
        this.pageManager = pageManager;
        this.contentLoader = contentLoader;
    }

    private URI getFullPath(String path) {
        String basePath = Config.get().getString("pages.basePath");
        if (basePath.isEmpty()) {
            return Paths.get(path).toAbsolutePath().toUri();
        } else {
            return Paths.get(basePath, path).toUri();
        }
    }

    private HttpResponseImpl.Builder addBody(HttpResponseImpl.Builder builder, String path) {
        try {
            return builder.body(contentLoader.getContent(getFullPath(path)));
        } catch (IOException e) {
            log.error("Error retrieving content for path: {}", path);
            return getFailureBuilder();
        }
    }

    public String getResponse(HttpRequest request) {
        Page page = null;
        try {
            page = pageManager.getPage(request.path().getRawPath());
        } catch (Exception e) {
            return getFailureResponse();
        }

        if (page == null) {
            // A null response means that the page is not listed in the database, so we should return a 404
            return HttpResponse.create(builder -> addBody(builder
                    .statusCode(HttpStatus.NOT_FOUND), "pages/not_found.html")).toString();
        }
        if (!page.verbs().contains(request.verb())) {
            // Page exists but the method is not listed
            return HttpResponse.create(builder -> builder.statusCode(HttpStatus.METHOD_NOT_ALLOWED)).toString();
        }

        String path = page.path();
        String contentType = page.contentType();

        return HttpResponse.create(builder -> addBody(builder
                .statusCode(HttpStatus.OK)
                .putHeader("Date", LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(HTML_DATE_PATTERN, Locale.ENGLISH).withZone(ZoneId.of("GMT"))))
                .putHeader("Content-Type", contentType), path)).toString();
    }

    private HttpResponseImpl.Builder getFailureBuilder() {
        return HttpResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String getTimeoutResponse() {
        return HttpResponse.builder().statusCode(HttpStatus.REQUEST_TIMEOUT).build().toString();
    }

    public String getFailureResponse() {
        return getFailureBuilder().build().toString();
    }

}
