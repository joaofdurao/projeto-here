package br.com.asn.checkin_api.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGlobalException(Exception ex) {
        return new ExceptionResponse("ERROR", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ExceptionResponse("VALIDATION ERROR", message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ExceptionResponse handleBadRequest(HttpClientErrorException.BadRequest ex) {
        return new ExceptionResponse("BAD REQUEST", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ExceptionResponse handleUnauthorized(HttpClientErrorException.Unauthorized ex) {
        return new ExceptionResponse("UNAUTHORIZED", "Acesso não autorizado.");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ExceptionResponse handleForbidden(HttpClientErrorException.Forbidden ex) {
        return new ExceptionResponse("FORBIDDEN", "Acesso proibido.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse handleNotFound(EntityNotFoundException ex) {
        return new ExceptionResponse("NOT FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ExceptionResponse handleInternalServerError(HttpServerErrorException.InternalServerError ex) {
        return new ExceptionResponse("INTERNAL SERVER ERROR", "Erro interno do servidor.");
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public ExceptionResponse handleBadGateway(HttpServerErrorException.BadGateway ex) {
        return new ExceptionResponse("BAD GATEWAY", "Erro no gateway.");
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
    public ExceptionResponse handleServiceUnavailable(HttpServerErrorException.ServiceUnavailable ex) {
        return new ExceptionResponse("SERVICE UNAVAILABLE", "Serviço indisponível.");
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public ExceptionResponse handleGatewayTimeout(HttpServerErrorException.GatewayTimeout ex) {
        return new ExceptionResponse("GATEWAY TIMEOUT", "Tempo de resposta do gateway excedido.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ExceptionResponse handleUnauthorizedException(UnauthorizedException ex) {
        return new ExceptionResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ExceptionResponse("BAD REQUEST", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ExceptionResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ExceptionResponse("NOT FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(JWTInitializationException.class)
    public ExceptionResponse handleJWTInitializationException(JWTInitializationException ex) {
        return new ExceptionResponse("JWT INITIALIZATION ERROR", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(QRCodeGenerationException.class)
    public ExceptionResponse handleQRCodeGenerationException(QRCodeGenerationException ex) {
        return new ExceptionResponse("QR CODE ERROR", ex.getMessage());
    }
}
