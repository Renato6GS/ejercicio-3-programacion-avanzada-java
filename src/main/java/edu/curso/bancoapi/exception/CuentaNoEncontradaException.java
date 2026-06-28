package edu.curso.bancoapi.exception;

/**
 * Se lanza cuando se consulta una cuenta que no existe en el sistema.
 * El manejador global la traduce a un HTTP 404.
 */
public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException(String numeroCuenta) {
        super("No existe la cuenta con numero: " + numeroCuenta);
    }
}
