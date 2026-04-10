package hexlet.code.exception;

public final class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() {
        super();
    }

    public UrlNotFoundException(final String message) {
        super(message);
    }
}
