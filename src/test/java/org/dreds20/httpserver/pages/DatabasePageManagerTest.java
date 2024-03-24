package org.dreds20.httpserver.pages;

import org.assertj.core.api.Assertions;
import org.dreds20.db.DataBase;
import org.dreds20.httpserver.model.HttpVerb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DatabasePageManagerTest {
    public static final String PUT = "PUT";
    private static final String PAGE = "page";
    private static final String VERBS = "verbs";
    private static final String CONTENT_TYPE = "content_type";
    private static final String PATH = "path";
    public static final String PAGE_NAME = "pageName";
    public static final String GET = "GET";
    public static final String CONTENT_TYPE_VALUE = "text/html";
    public static final String PATH_VALUE = "/";
    @Mock
    DataBase databaseMock;
    @Mock
    Connection connectionMock;
    @Mock
    PreparedStatement preparedStatementMock;
    @Mock
    ResultSet resultSetMock;
    DatabasePageManager databasePageManager;
    Page page;

    @BeforeEach
    void beforeEach() throws SQLException {
        when(databaseMock.connect()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, false);
        databasePageManager = new DatabasePageManager(databaseMock);
        page = Page.from(builder -> builder
                .pageName(PAGE_NAME)
                .verbs(List.of(HttpVerb.GET, HttpVerb.PUT))
                .path(PATH_VALUE)
                .contentType(CONTENT_TYPE_VALUE));
    }

    private void mockResultSet() throws SQLException {
        when(resultSetMock.getString(eq(PAGE))).thenReturn(PAGE_NAME);
        when(resultSetMock.getString(eq(VERBS))).thenReturn(GET + ", " + PUT);
        when(resultSetMock.getString(eq(CONTENT_TYPE))).thenReturn(CONTENT_TYPE_VALUE);
        when(resultSetMock.getString(eq(PATH))).thenReturn(PATH_VALUE);
    }
    
    @Test
    void pageCreationFailureHandled() throws SQLException {
        when(resultSetMock.getString(eq(PAGE))).thenThrow(new SQLException());
        assertThatThrownBy(() -> databasePageManager.pages()).isInstanceOf(Exception.class);
    }

    @Test
    void getPagesReturnsAPage() throws Exception {
        mockResultSet();
        assertThat(databasePageManager.pages().getFirst()).isEqualTo(page);
    }

    @Test
    void getPageByPageName() throws Exception {
        mockResultSet();
        assertThat(databasePageManager.getPage(PAGE_NAME)).isEqualTo(page);
    }
    
}
