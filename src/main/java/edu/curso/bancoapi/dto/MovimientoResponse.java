package edu.curso.bancoapi.dto;

import edu.curso.bancoapi.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representacion de un movimiento tal como se expone en la API.
 */
public record MovimientoResponse(
        Long id,
        TipoMovimiento tipo,
        BigDecimal monto,
        LocalDateTime fecha,
        String descripcion) {
}
