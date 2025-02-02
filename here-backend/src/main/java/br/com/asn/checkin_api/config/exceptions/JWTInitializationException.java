package br.com.asn.checkin_api.config.exceptions;

public class JWTInitializationException extends RuntimeException {
    public JWTInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

