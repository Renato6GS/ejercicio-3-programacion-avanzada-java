package edu.curso.bancoapi.repository;

import edu.curso.bancoapi.model.Cuenta;
import edu.curso.bancoapi.model.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas del repositorio en memoria. Verifican los datos semilla, la busqueda
 * por numero y que los movimientos se devuelvan ordenados por fecha descendente.
 */
class CuentaRepositoryTest {

    private CuentaRepository repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new CuentaRepository();
    }

    @Test
    void buscarPorNumero_devuelveLaCuentaCuandoExiste() {
        Optional<Cuenta> cuenta = repositorio.buscarPorNumero("12345");

        assertThat(cuenta).isPresent();
        assertThat(cuenta.get().titular()).isEqualTo("Renato Granados");
        assertThat(cuenta.get().saldo()).isEqualByComparingTo("1525.75");
    }

    @Test
    void buscarPorNumero_devuelveVacioCuandoNoExiste() {
        assertThat(repositorio.buscarPorNumero("00000")).isEmpty();
    }

    @Test
    void buscarMovimientos_devuelveSoloLosDeLaCuentaOrdenadosPorFechaDesc() {
        List<Movimiento> movimientos = repositorio.buscarMovimientos("12345");

        assertThat(movimientos).hasSize(2);
        assertThat(movimientos).allMatch(m -> m.numeroCuenta().equals("12345"));
        // El mas reciente (10 de junio) debe ir primero.
        assertThat(movimientos.get(0).fecha())
                .isAfter(movimientos.get(1).fecha());
    }

    @Test
    void buscarMovimientos_devuelveListaVaciaCuandoLaCuentaNoTieneMovimientos() {
        assertThat(repositorio.buscarMovimientos("11111")).isEmpty();
    }
}
