package org.dreds20.db;

import org.dreds20.utils.ImmutableDefault;
import org.immutables.value.Value;

import java.util.function.UnaryOperator;

@Value.Immutable
@ImmutableDefault
public interface DataBaseConfig {
    String url();
    String username();
    String password();

    static DataBaseConfigImpl.Builder builder() {
        return DataBaseConfigImpl.builder();
    }

    static DataBaseConfigImpl.Builder builder(UnaryOperator<DataBaseConfigImpl.Builder> spec) {
        return spec.apply(builder());
    }

    static DataBaseConfig create(UnaryOperator<DataBaseConfigImpl.Builder> spec) {
        return builder(spec).build();
    }
}
