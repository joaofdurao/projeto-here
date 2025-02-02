package br.com.asn.checkin_api.config.exceptions;

public class QRCodeGenerationException extends RuntimeException {
    public QRCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
