package org.dreds20.httpserver.pages;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentLoader {
    public List<String> getContent(URI uri) throws IOException {
        Path path = Paths.get(uri);
        List<String> data;
        try (Stream<String> lines = Files.lines(path)) {
            data = lines.collect(Collectors.toList());
        }
        return data;
    }
}
