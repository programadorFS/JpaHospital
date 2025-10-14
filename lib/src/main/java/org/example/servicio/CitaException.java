package org.example.servicio;

public class CitaException extends RuntimeException {
    
    public CitaException(String mensaje) {
        super(mensaje);
    }
    
    public CitaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}