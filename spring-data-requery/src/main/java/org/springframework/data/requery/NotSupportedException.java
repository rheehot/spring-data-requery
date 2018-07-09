package org.springframework.data.requery;

/**
 * org.springframework.data.requery.NotSupportedException
 *
 * @author debop
 * @since 18. 6. 22
 */
public class NotSupportedException extends RuntimeException {

    public NotSupportedException() {}

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -4471049196596338256L;
}
