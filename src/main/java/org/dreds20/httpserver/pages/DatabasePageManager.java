package org.dreds20.httpserver.pages;

import org.dreds20.db.DataBase;
import org.dreds20.httpserver.model.HttpVerb;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String URI = "uri";
    public DatabasePageManager() {
    }

    private Page buildPage(ResultSet resultSet) {
        return Page.from(builder ->
        {
            try {
                return builder.pageName(resultSet.getString(PAGE))
                        .verbs(Stream.of(resultSet.getString(VERBS).split(",")).map(HttpVerb::valueOf).collect(Collectors.toList()))
                        .contentType(resultSet.getString(CONTENT_TYPE))
                        .uri(new URI(resultSet.getString(URI)));
            } catch (SQLException | URISyntaxException e) {
                throw new RuntimeException(e);
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
    public List<Page> pages() {
        List<Page> pages = new ArrayList<>();
        try (DataBase dataBase = new DataBase()){
            Connection connection = dataBase.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM pages;");
            while(resultSet.next()) {
                pages.add(buildPage(resultSet));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pages;
    }
}
