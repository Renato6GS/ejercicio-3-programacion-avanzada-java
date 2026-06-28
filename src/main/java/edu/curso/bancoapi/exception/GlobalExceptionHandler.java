package edu.curso.bancoapi.exception;

import edu.curso.bancoapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Manejador centralizado de errores.
 *
 * <p>{@code @RestControllerAdvice} intercepta las excepciones lanzadas por
 * cualquier controlador y las convierte en una respuesta HTTP consistente
 * (un {@link ErrorResponse}). Asi los controladores quedan limpios: solo se
 * ocupan del caso exitoso y delegan el manejo de errores aqui.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Cuenta inexistente -> 404 Not Found. */
    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejarCuentaNoEncontrada(
            CuentaNoEncontradaException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /** Numero de cuenta con formato invalido -> 400 Bad Request. */
    @ExceptionHandler(CuentaInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarCuentaInvalida(
            CuentaInvalidaException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    /**
     * Falla de validacion de Jakarta Bean Validation (@Pattern sobre el
     * {@code @PathVariable}) -> 400 Bad Request, con el detalle de cada regla.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            HandlerMethodValidationException ex, HttpServletRequest request) {
        List<String> detalles = ex.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return construir(HttpStatus.BAD_REQUEST,
                "Uno o mas parametros no son validos", request, detalles);
    }

    /** Cualquier error no contemplado -> 500 Internal Server Error. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGenerico(
            Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado", request, null);
    }

    private ResponseEntity<ErrorResponse> construir(
            HttpStatus status, String mensaje, HttpServletRequest request, List<String> detalles) {
        ErrorResponse cuerpo = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                detalles);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
