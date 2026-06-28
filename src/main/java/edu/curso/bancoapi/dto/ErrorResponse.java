package edu.curso.bancoapi.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cuerpo estandar de las respuestas de error de la API.
 *
 * <p>Tener un formato unico de error hace que el cliente siempre sepa que
 * esperar cuando algo falla, sin importar que parte del sistema lo produjo.</p>
 *
 * @param timestamp momento en que se genero el error
 * @param status    codigo HTTP (404, 400, ...)
 * @param error     descripcion corta del codigo HTTP
 * @param mensaje   mensaje principal y legible del error
 * @param ruta      endpoint que se estaba invocando
 * @param detalles  lista opcional de errores de validacion campo a campo
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String ruta,
        List<String> detalles) {
}
