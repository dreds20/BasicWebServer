package org.dreds20.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dreds20.config.Config;
import org.dreds20.httpserver.model.HttpRequest;
import org.dreds20.httpserver.model.HttpResponse;
import org.dreds20.httpserver.model.HttpStatus;
import org.dreds20.httpserver.pages.ContentLoader;
import org.dreds20.httpserver.pages.Page;
import org.dreds20.httpserver.pages.PageManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ConnectionManager {
    private static final Logger log = LogManager.getLogger(ConnectionManager.class);
    private final PageManager pageManager;
    private final ContentLoader contentLoader;
    private static final String HTML_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    public ConnectionManager(PageManager pageManager, ContentLoader contentLoader) {
        this.pageManager = pageManager;
        this.contentLoader = contentLoader;
    }

    public String getResponse(HttpRequest request) {
        Page page = null;
        try {
            page = pageManager.getPage(request.path().getRawPath());
        } catch (Exception e) {
            return HttpResponse.create(builder -> builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR)).toString();
        }

        if (page == null) {
            // A null response means that the page is not listed in the database, so we should return a 404
            return HttpResponse.create(builder -> builder
                    .statusCode(HttpStatus.NOT_FOUND)).toString();
        }
        if (!page.verbs().contains(request.verb())) {
            // Page exists but the method is not listed
            return HttpResponse.create(builder -> builder.statusCode(HttpStatus.METHOD_NOT_ALLOWED)).toString();
        }
        List<String> body;
        try {
            String basePath = Config.get().getString("pages.basePath");
            if (basePath.isEmpty()) {
                body = contentLoader.getContent(Paths.get(page.path()).toAbsolutePath().toUri());
            } else {
                body = contentLoader.getContent(Paths.get(basePath, page.path()).toUri());
            }

        } catch (IOException e) {
            log.error("Exception thrown while loading body");
            return HttpResponse.create(builder -> builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR)).toString();
        }
        String contentType = page.contentType();
        return HttpResponse.create(builder -> builder
                .statusCode(HttpStatus.OK)
//                .putHeader("Date", LocalDateTime.now()
//                        .format(DateTimeFormatter.ofPattern(HTML_DATE_PATTERN, Locale.ENGLISH).withZone(ZoneId.of("GMT"))).toString())
                .putHeader("Content-Type", contentType)
                .body(body)).toString();
    }

}
