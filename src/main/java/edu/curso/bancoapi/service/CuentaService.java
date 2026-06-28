package edu.curso.bancoapi.service;

import edu.curso.bancoapi.dto.MovimientoResponse;
import edu.curso.bancoapi.dto.MovimientosResponse;
import edu.curso.bancoapi.dto.SaldoResponse;
import edu.curso.bancoapi.exception.CuentaInvalidaException;
import edu.curso.bancoapi.exception.CuentaNoEncontradaException;
import edu.curso.bancoapi.model.Cuenta;
import edu.curso.bancoapi.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de negocio de cuentas: validacion, consulta de saldo y de movimientos.
 *
 * <p>El controlador delega aqui toda la logica; el servicio no sabe nada de
 * HTTP. Recibe el repositorio por inyeccion de dependencias en el constructor.</p>
 */
@Service
public class CuentaService {

    private static final String MONEDA = "GTQ";

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Consulta el saldo de una cuenta.
     *
     * @throws CuentaInvalidaException     si el numero de cuenta es nulo o vacio
     * @throws CuentaNoEncontradaException si la cuenta no existe
     */
    public SaldoResponse consultarSaldo(String numeroCuenta) {
        Cuenta cuenta = obtenerCuenta(numeroCuenta);
        return new SaldoResponse(
                cuenta.numero(),
                cuenta.titular(),
                cuenta.saldo(),
                MONEDA,
                cuenta.estado());
    }

    /**
     * Consulta los movimientos de una cuenta.
     *
     * @throws CuentaInvalidaException     si el numero de cuenta es nulo o vacio
     * @throws CuentaNoEncontradaException si la cuenta no existe
     */
    public MovimientosResponse consultarMovimientos(String numeroCuenta) {
        Cuenta cuenta = obtenerCuenta(numeroCuenta);

        List<MovimientoResponse> movimientos = cuentaRepository
                .buscarMovimientos(cuenta.numero())
                .stream()
                .map(m -> new MovimientoResponse(
                        m.id(), m.tipo(), m.monto(), m.fecha(), m.descripcion()))
                .toList();

        return new MovimientosResponse(cuenta.numero(), movimientos.size(), movimientos);
    }

    /**
     * Valida el numero y recupera la cuenta. Centraliza ambas reglas para que
     * los dos endpoints las compartan sin duplicar codigo.
     */
    private Cuenta obtenerCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new CuentaInvalidaException("El numero de cuenta es obligatorio");
        }
        return cuentaRepository.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException(numeroCuenta));
    }
}
