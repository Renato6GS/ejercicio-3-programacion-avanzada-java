package edu.curso.bancoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * <p>{@code @SpringBootApplication} combina configuracion, auto-configuracion y
 * el escaneo de componentes a partir de este paquete raiz hacia abajo.</p>
 */
@SpringBootApplication
public class BancoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoApiApplication.class, args);
    }
}
