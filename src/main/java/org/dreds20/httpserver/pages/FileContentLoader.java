package org.dreds20.httpserver.pages;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for retrieving the body of a response object from a given URI and parsing it to a list of Strings
 */
public class FileContentLoader implements ContentLoader {
    /**
     * Retrieves a file from a given URI location, parses it to a list of Strings, and returns it
     *
     * @param uri The URI pointing to the file to be returned as a list of Strings
     *
     * @return A list of Strings read from the file located at the given URI
     *
     * @throws IOException throws an exception if the file cannot be read
     */
    public List<String> getContent(URI uri) throws Exception {
        Path path = Paths.get(uri);
        List<String> data;
        try (Stream<String> lines = Files.lines(path)) {
            data = lines.collect(Collectors.toList());
        }
        return data;
    }
}
