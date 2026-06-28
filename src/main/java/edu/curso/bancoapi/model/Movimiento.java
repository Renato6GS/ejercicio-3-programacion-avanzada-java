package edu.curso.bancoapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Movimiento (transaccion) asociado a una cuenta.
 *
 * @param id          identificador del movimiento
 * @param numeroCuenta cuenta a la que pertenece el movimiento
 * @param tipo        tipo de movimiento (DEPOSITO o RETIRO)
 * @param monto       monto del movimiento
 * @param fecha       fecha y hora en que ocurrio
 * @param descripcion descripcion legible del movimiento
 */
public record Movimiento(
        Long id,
        String numeroCuenta,
        TipoMovimiento tipo,
        BigDecimal monto,
        LocalDateTime fecha,
        String descripcion) {
}
