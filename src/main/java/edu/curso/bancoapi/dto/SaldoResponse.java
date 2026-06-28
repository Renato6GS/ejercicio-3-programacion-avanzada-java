package edu.curso.bancoapi.dto;

import edu.curso.bancoapi.model.EstadoCuenta;

import java.math.BigDecimal;

/**
 * Respuesta del endpoint de consulta de saldo.
 *
 * <p>Se usa un DTO (Data Transfer Object) en lugar de devolver el modelo
 * {@code Cuenta} directamente. Asi la API controla exactamente que datos
 * expone y puede evolucionar el modelo interno sin romper el contrato HTTP.</p>
 */
public record SaldoResponse(
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        String moneda,
        EstadoCuenta estado) {
}
