package edu.curso.bancoapi.controller;

import edu.curso.bancoapi.dto.MovimientosResponse;
import edu.curso.bancoapi.dto.SaldoResponse;
import edu.curso.bancoapi.service.CuentaService;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de cuentas. Expone los endpoints HTTP y delega la logica
 * en {@link CuentaService} (inyectado por constructor).
 *
 * <p>{@code @Validated} junto con {@code @Pattern} en el {@code @PathVariable}
 * hace que Spring valide el formato del numero de cuenta ANTES de entrar al
 * metodo. Si no cumple, se lanza una excepcion que captura el manejador global.</p>
 */
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    /** El numero de cuenta debe tener entre 4 y 10 digitos. */
    private static final String PATRON_CUENTA = "\\d{4,10}";

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * GET /api/cuentas/{numeroCuenta}/saldo
     * Devuelve el saldo actual de la cuenta.
     */
    @GetMapping("/{numeroCuenta}/saldo")
    public SaldoResponse consultarSaldo(
            @PathVariable
            @Pattern(regexp = PATRON_CUENTA,
                    message = "El numero de cuenta debe tener entre 4 y 10 digitos")
            String numeroCuenta) {
        return cuentaService.consultarSaldo(numeroCuenta);
    }

    /**
     * GET /api/cuentas/{numeroCuenta}/movimientos
     * Devuelve la lista de movimientos de la cuenta.
     */
    @GetMapping("/{numeroCuenta}/movimientos")
    public MovimientosResponse consultarMovimientos(
            @PathVariable
            @Pattern(regexp = PATRON_CUENTA,
                    message = "El numero de cuenta debe tener entre 4 y 10 digitos")
            String numeroCuenta) {
        return cuentaService.consultarMovimientos(numeroCuenta);
    }
}
