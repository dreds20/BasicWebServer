package org.dreds20.httpserver.pages;

import java.util.List;

public interface PageManager {
    List<Page> pages();
    default Page getPage(String pageName) {
        return pages().stream().filter(p -> p.pageName().equals(pageName)).findFirst().orElse(null);
    }
}
