package edu.curso.bancoapi.exception;

/**
 * Se lanza cuando el numero de cuenta no cumple las reglas de negocio
 * (por ejemplo, formato invalido). El manejador global la traduce a un HTTP 400.
 */
public class CuentaInvalidaException extends RuntimeException {

    public CuentaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
