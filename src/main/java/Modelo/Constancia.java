/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author alfre
 */

import java.util.Date;

/**
 * Clase de entidad que pertenece a la capa del Modelo en la arquitectura MVC.
 * Representa el documento final (constancia de estudios) que se genera en el sistema.
 * Su función es estructurar los datos que la Vista mostrará al usuario al final del proceso.
 */
public class Constancia {
    
    // Atributos privados (encapsulamiento) que conforman los datos del documento
    private String folio;
    private Date fechaEmision;
    private String contenido;

    /**
     * Constructor de la clase.
     * Es invocado por el Controlador (ControladorConstancia) únicamente después de 
     * haber validado que el alumno existe y está activo.
     */
    public Constancia(String folio, Date fechaEmision, String contenido) {
        this.folio = folio;
        this.fechaEmision = fechaEmision;
        this.contenido = contenido;
    }

    /**
     * Retorna el identificador único generado para el documento.
     * La Vista lo utiliza para imprimir el número de folio oficial.
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Retorna la fecha y hora exactas en las que se autorizó y generó la constancia.
     */
    public Date getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Retorna el texto completo y redactado con los datos del alumno.
     * La Vista extrae este valor para plasmar el cuerpo de la constancia en pantalla.
     */
    public String getContenido() {
        return contenido;
    }
}