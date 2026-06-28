package edu.curso.bancoapi.service;

import edu.curso.bancoapi.dto.MovimientosResponse;
import edu.curso.bancoapi.dto.SaldoResponse;
import edu.curso.bancoapi.exception.CuentaInvalidaException;
import edu.curso.bancoapi.exception.CuentaNoEncontradaException;
import edu.curso.bancoapi.model.Cuenta;
import edu.curso.bancoapi.model.EstadoCuenta;
import edu.curso.bancoapi.model.Movimiento;
import edu.curso.bancoapi.model.TipoMovimiento;
import edu.curso.bancoapi.repository.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias del servicio. Se aisla la logica de negocio del resto del
 * sistema usando un {@code @Mock} del repositorio: las pruebas no tocan datos
 * reales, solo verifican el comportamiento de {@link CuentaService}.
 */
@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private static final Cuenta CUENTA = new Cuenta(
            "12345", "Renato Granados", new BigDecimal("1525.75"), EstadoCuenta.ACTIVA);

    @Test
    void consultarSaldo_devuelveSaldoCuandoLaCuentaExiste() {
        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(CUENTA));

        SaldoResponse respuesta = cuentaService.consultarSaldo("12345");

        assertThat(respuesta.numeroCuenta()).isEqualTo("12345");
        assertThat(respuesta.titular()).isEqualTo("Renato Granados");
        assertThat(respuesta.saldo()).isEqualByComparingTo("1525.75");
        assertThat(respuesta.moneda()).isEqualTo("GTQ");
        assertThat(respuesta.estado()).isEqualTo(EstadoCuenta.ACTIVA);
    }

    @Test
    void consultarSaldo_lanzaExcepcionCuandoLaCuentaNoExiste() {
        when(cuentaRepository.buscarPorNumero("99999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cuentaService.consultarSaldo("99999"))
                .isInstanceOf(CuentaNoEncontradaException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void consultarSaldo_lanzaExcepcionCuandoElNumeroEsNulo() {
        assertThatThrownBy(() -> cuentaService.consultarSaldo(null))
                .isInstanceOf(CuentaInvalidaException.class);
    }

    @Test
    void consultarSaldo_lanzaExcepcionCuandoElNumeroEsVacio() {
        assertThatThrownBy(() -> cuentaService.consultarSaldo("   "))
                .isInstanceOf(CuentaInvalidaException.class);
    }

    @Test
    void consultarMovimientos_devuelveListaCuandoLaCuentaExiste() {
        Movimiento m1 = new Movimiento(1L, "12345", TipoMovimiento.DEPOSITO,
                new BigDecimal("2000.00"), LocalDateTime.of(2026, 6, 1, 9, 30), "Nomina");
        Movimiento m2 = new Movimiento(2L, "12345", TipoMovimiento.RETIRO,
                new BigDecimal("474.25"), LocalDateTime.of(2026, 6, 10, 14, 5), "Cajero");
        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(CUENTA));
        when(cuentaRepository.buscarMovimientos("12345")).thenReturn(List.of(m1, m2));

        MovimientosResponse respuesta = cuentaService.consultarMovimientos("12345");

        assertThat(respuesta.numeroCuenta()).isEqualTo("12345");
        assertThat(respuesta.total()).isEqualTo(2);
        assertThat(respuesta.movimientos()).hasSize(2);
        assertThat(respuesta.movimientos().get(0).tipo()).isEqualTo(TipoMovimiento.DEPOSITO);
    }

    @Test
    void consultarMovimientos_lanzaExcepcionCuandoLaCuentaNoExiste() {
        when(cuentaRepository.buscarPorNumero("99999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cuentaService.consultarMovimientos("99999"))
                .isInstanceOf(CuentaNoEncontradaException.class);
    }
}
