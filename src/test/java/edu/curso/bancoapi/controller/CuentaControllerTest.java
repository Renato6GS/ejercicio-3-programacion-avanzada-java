package edu.curso.bancoapi.controller;

import edu.curso.bancoapi.dto.MovimientoResponse;
import edu.curso.bancoapi.dto.MovimientosResponse;
import edu.curso.bancoapi.dto.SaldoResponse;
import edu.curso.bancoapi.exception.CuentaNoEncontradaException;
import edu.curso.bancoapi.model.EstadoCuenta;
import edu.curso.bancoapi.model.TipoMovimiento;
import edu.curso.bancoapi.service.CuentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas del controlador con MockMvc. {@code @WebMvcTest} levanta solo la capa
 * web (controlador + manejador de errores + validacion), sin servidor real y
 * con el servicio simulado mediante {@code @MockitoBean}. Asi se prueban las
 * rutas, los codigos HTTP y el formato JSON de la respuesta.
 */
@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CuentaService cuentaService;

    @Test
    void getSaldo_devuelve200ConElSaldo() throws Exception {
        SaldoResponse respuesta = new SaldoResponse(
                "12345", "Renato Granados", new BigDecimal("1525.75"),
                "GTQ", EstadoCuenta.ACTIVA);
        when(cuentaService.consultarSaldo("12345")).thenReturn(respuesta);

        mockMvc.perform(get("/api/cuentas/12345/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("12345"))
                .andExpect(jsonPath("$.saldo").value(1525.75))
                .andExpect(jsonPath("$.moneda").value("GTQ"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }

    @Test
    void getSaldo_devuelve404CuandoLaCuentaNoExiste() throws Exception {
        when(cuentaService.consultarSaldo("99999"))
                .thenThrow(new CuentaNoEncontradaException("99999"));

        mockMvc.perform(get("/api/cuentas/99999/saldo"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("No existe la cuenta con numero: 99999"))
                .andExpect(jsonPath("$.ruta").value("/api/cuentas/99999/saldo"));
    }

    @Test
    void getSaldo_devuelve400CuandoElFormatoEsInvalido() throws Exception {
        mockMvc.perform(get("/api/cuentas/abc/saldo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalles").isArray());
    }

    @Test
    void getSaldo_devuelve500CuandoOcurreUnErrorInesperado() throws Exception {
        when(cuentaService.consultarSaldo("12345"))
                .thenThrow(new RuntimeException("fallo inesperado"));

        mockMvc.perform(get("/api/cuentas/12345/saldo"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void getMovimientos_devuelve200ConLaLista() throws Exception {
        MovimientoResponse mov = new MovimientoResponse(
                1L, TipoMovimiento.DEPOSITO, new BigDecimal("2000.00"),
                LocalDateTime.of(2026, 6, 1, 9, 30), "Nomina");
        MovimientosResponse respuesta =
                new MovimientosResponse("12345", 1, List.of(mov));
        when(cuentaService.consultarMovimientos("12345")).thenReturn(respuesta);

        mockMvc.perform(get("/api/cuentas/12345/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("12345"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.movimientos[0].tipo").value("DEPOSITO"))
                .andExpect(jsonPath("$.movimientos[0].descripcion").value("Nomina"));
    }

    @Test
    void getMovimientos_devuelve404CuandoLaCuentaNoExiste() throws Exception {
        when(cuentaService.consultarMovimientos(anyString()))
                .thenThrow(new CuentaNoEncontradaException("99999"));

        mockMvc.perform(get("/api/cuentas/99999/movimientos"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
