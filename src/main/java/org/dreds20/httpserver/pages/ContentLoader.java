package org.dreds20.httpserver.pages;

import java.net.URI;
import java.util.List;

public interface ContentLoader {
    List<String> getContent(URI uri) throws Exception;
}
