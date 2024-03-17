package org.dreds20.httpserver.pages;

import org.immutables.value.Value;
import org.dreds20.httpserver.model.HttpVerb;
import org.dreds20.utils.ImmutableDefault;

import java.net.URI;
import java.util.List;
import java.util.function.UnaryOperator;

@Value.Immutable
@ImmutableDefault
public interface Page {
    static PageImpl.Builder builder() {
        return PageImpl.builder();
    }

    static PageImpl.Builder builder(UnaryOperator<PageImpl.Builder> spec) {
        return spec.apply(builder());
    }

    static Page from(UnaryOperator<PageImpl.Builder> spec) {
        return builder(spec).build();
    }

    String pageName();
    List<HttpVerb> verbs();
    String contentType();
    URI uri();
}
