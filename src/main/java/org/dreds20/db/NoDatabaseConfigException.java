package org.dreds20.db;

public class NoDatabaseConfigException extends RuntimeException {
    public NoDatabaseConfigException() {
    }

    public NoDatabaseConfigException(String message) {
        super(message);
    }

    public NoDatabaseConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDatabaseConfigException(Throwable cause) {
        super(cause);
    }
}
