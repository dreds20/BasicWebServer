package org.dreds20.httpserver.pages;

import java.util.List;

public interface PageManager {
    List<Page> pages() throws Exception;
    default Page getPage(String pageName) throws Exception {
        return pages().stream().filter(p -> p.pageName().equals(pageName)).findFirst().orElse(null);
    }
}
