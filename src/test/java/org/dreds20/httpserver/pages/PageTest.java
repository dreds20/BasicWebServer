package org.dreds20.httpserver.pages;


import org.dreds20.httpserver.model.HttpVerb;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PageTest {
    @Test
    void exceptionWhenBuildingPageAndPageNameIsNull() {
        assertThatThrownBy(() -> Page.builder()
                .pageName(null)
                .addVerb(HttpVerb.GET)
                .contentType("text/html")
                .path("/")
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void exceptionWhenBuildingPageWithNullVerbs() {
        assertThatThrownBy(() -> Page.builder()
                .pageName("pageName")
                .verbs(null)
                .contentType("text/html")
                .path("/")
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void exceptionWhenBuildingPageWithNullContentType() {
        assertThatThrownBy(() -> Page.builder()
                .pageName("pageName")
                .addVerb(HttpVerb.GET)
                .contentType(null)
                .path("/")
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void exceptionWhenBuildingPageWithNullPath() {
        assertThatThrownBy(() -> Page.builder()
                .pageName("pageName")
                .addVerb(HttpVerb.GET)
                .contentType("text/html")
                .path(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    private PageImpl.Builder getCompletePageBuilder() {
        return Page.builder()
                .pageName("pageName")
                .addVerb(HttpVerb.GET)
                .contentType("text/html")
                .path("/");
    }

    private Page getCompletePage() {
        return Page.from(b -> getCompletePageBuilder());
    }

    @Test
    void pageNameCanBeSet() {
        assertThat(getCompletePage().pageName()).isEqualTo("pageName");
    }

    @Test
    void verbCanBeSet() {
        assertThat(getCompletePage().verbs().getFirst()).isEqualTo(HttpVerb.GET);
    }

    @Test
    void multipleVerbsCanBeSet() {
        Page completePage = getCompletePageBuilder().addVerb(HttpVerb.POST).build();
        assertThat(completePage.verbs()).contains(HttpVerb.GET, HttpVerb.POST);
    }

    @Test
    void contentTypeCanBeSet() {
        assertThat(getCompletePage().contentType()).isEqualTo("text/html");
    }

    @Test
    void pathCanBeSet() {
        assertThat(getCompletePage().path()).isEqualTo("/");
    }
}
