package org.dreds20.httpserver.pages;

import org.dreds20.db.DataBase;
import org.dreds20.httpserver.model.HttpVerb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class DatabasePageManager implements PageManager {
    private static final String PAGE = "page";
    private static final String VERBS = "verbs";
    private static final String CONTENT_TYPE = "content_type";
    private static final String PATH = "path";
    private final DataBase dataBase;
    public DatabasePageManager(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    private Page buildPage(ResultSet resultSet) {
        return Page.from(builder ->
        {
            try {
                return builder.pageName(resultSet.getString(PAGE))
                        .verbs(Stream.of(resultSet.getString(VERBS).split(","))
                                .map(String::trim)
                                .map(String::toUpperCase)
                                .map(HttpVerb::valueOf).collect(Collectors.toList()))
                        .contentType(resultSet.getString(CONTENT_TYPE))
                        .path(resultSet.getString(PATH));
            } catch (SQLException e) {
                throw new RuntimeException("Exception thrown while attempting to create page from sql query", e);
            }
        });
    }
    /**
     * Establishes a connection to the server database and retrieves a list
     * of page objects
     *
     * @return A list of page objects retrieved from the server database
     */
    @Override
    public List<Page> pages() throws Exception {
        List<Page> pages = new ArrayList<>();
        try (Connection connection = dataBase.connect()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM pages;");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                pages.add(buildPage(resultSet));
            }
        }
        return pages;
    }
}
