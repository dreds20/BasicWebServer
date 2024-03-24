package org.dreds20.httpserver.pages;

import java.util.List;

/**
 * This class is responsible for looking up a given page object.  A page object provides the HttpVerbs that are
 * acceptable for a page, the Content-Type that will be provided on the header of an HttpResponse generated, and
 * a path to a file which should contain the body content.
 */
public interface PageManager {
    /**
     * Returns all available pages
     *
     * @return A list of all pages
     *
     * @throws Exception Exception can be thrown when connecting to the server hosting the page configuration
     */
    List<Page> pages() throws Exception;

    /**
     * Gets a specific page from pages in page directory by the pageName field
     *
     * @param pageName the name of the page we are performing a lookup for
     *
     * @return Returns a matching page
     *
     * @throws Exception Can throw an exception if an error occurs connecting to the page server
     */
    default Page getPage(String pageName) throws Exception {
        return pages().stream().filter(p -> p.pageName().equals(pageName)).findFirst().orElse(null);
    }
}
