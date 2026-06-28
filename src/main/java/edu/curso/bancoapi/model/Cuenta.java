package edu.curso.bancoapi.model;

import java.math.BigDecimal;

/**
 * Modelo de dominio de una cuenta bancaria.
 *
 * <p>Se usa un {@code record} (Java moderno) porque la cuenta es un dato
 * inmutable de solo lectura para este ejercicio. {@code BigDecimal} se usa
 * para el saldo: nunca se debe representar dinero con {@code double} por los
 * errores de redondeo del punto flotante.</p>
 *
 * @param numero     numero identificador de la cuenta
 * @param titular    nombre del titular
 * @param saldo      saldo disponible
 * @param estado     estado de la cuenta (ACTIVA, INACTIVA, ...)
 */
public record Cuenta(String numero, String titular, BigDecimal saldo, EstadoCuenta estado) {
}
