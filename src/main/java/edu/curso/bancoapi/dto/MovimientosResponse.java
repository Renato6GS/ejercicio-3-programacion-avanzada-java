package edu.curso.bancoapi.dto;

import java.util.List;

/**
 * Respuesta del endpoint de movimientos: envuelve la lista junto con metadatos
 * utiles para el cliente (numero de cuenta y total de movimientos).
 */
public record MovimientosResponse(
        String numeroCuenta,
        int total,
        List<MovimientoResponse> movimientos) {
}
