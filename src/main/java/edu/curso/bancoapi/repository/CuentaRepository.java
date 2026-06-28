package edu.curso.bancoapi.repository;

import edu.curso.bancoapi.model.Cuenta;
import edu.curso.bancoapi.model.EstadoCuenta;
import edu.curso.bancoapi.model.Movimiento;
import edu.curso.bancoapi.model.TipoMovimiento;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria. Simula el acceso a datos sin base de datos
 * (la guia de la semana indica concentrarse en Spring Core/Boot, no en JPA).
 *
 * <p>{@code @Repository} lo marca como bean de acceso a datos para que Spring
 * lo administre e inyecte en el servicio.</p>
 */
@Repository
public class CuentaRepository {

    private final Map<String, Cuenta> cuentas;
    private final List<Movimiento> movimientos;

    public CuentaRepository() {
        this.cuentas = Map.of(
                "12345", new Cuenta("12345", "Renato Granados",
                        new BigDecimal("1525.75"), EstadoCuenta.ACTIVA),
                "67890", new Cuenta("67890", "Ana Lopez",
                        new BigDecimal("8400.00"), EstadoCuenta.ACTIVA),
                "11111", new Cuenta("11111", "Carlos Mejia",
                        new BigDecimal("0.00"), EstadoCuenta.INACTIVA));

        this.movimientos = List.of(
                new Movimiento(1L, "12345", TipoMovimiento.DEPOSITO,
                        new BigDecimal("2000.00"),
                        LocalDateTime.of(2026, 6, 1, 9, 30), "Deposito de nomina"),
                new Movimiento(2L, "12345", TipoMovimiento.RETIRO,
                        new BigDecimal("474.25"),
                        LocalDateTime.of(2026, 6, 10, 14, 5), "Retiro en cajero"),
                new Movimiento(3L, "67890", TipoMovimiento.DEPOSITO,
                        new BigDecimal("8400.00"),
                        LocalDateTime.of(2026, 6, 15, 11, 0), "Transferencia recibida"));
    }

    /** Busca una cuenta por su numero. {@code Optional} evita devolver null. */
    public Optional<Cuenta> buscarPorNumero(String numeroCuenta) {
        return Optional.ofNullable(cuentas.get(numeroCuenta));
    }

    /** Devuelve los movimientos de una cuenta, ordenados por fecha descendente. */
    public List<Movimiento> buscarMovimientos(String numeroCuenta) {
        return movimientos.stream()
                .filter(m -> m.numeroCuenta().equals(numeroCuenta))
                .sorted((a, b) -> b.fecha().compareTo(a.fecha()))
                .collect(Collectors.toList());
    }
}
