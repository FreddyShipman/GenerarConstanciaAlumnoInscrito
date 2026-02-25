/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author alfre
 */

/**
 * Clase de entidad que pertenece a la capa del Modelo en el patrón MVC.
 * Representa los detalles del estado académico y la inscripción de un alumno.
 * Su única responsabilidad es almacenar y proveer esta información.
 */
public class Inscripcion {
    
    // Atributos privados para aplicar el principio de encapsulamiento
    private String semestre;
    private Integer cantidadMaterias;
    private Boolean estatusActivo;

    /**
     * Constructor de la clase.
     * Se utiliza para instanciar (crear) los datos de inscripción cuando 
     * se carga la información simulada en el DAO.
     */
    public Inscripcion(String semestre, Integer cantidadMaterias, Boolean estatusActivo) {
        this.semestre = semestre;
        this.cantidadMaterias = cantidadMaterias;
        this.estatusActivo = estatusActivo;
    }

    /**
     * Retorna el estatus del alumno.
     * El ControladorConstancia utiliza este método para validar 
     * si el alumno es apto para generar el documento.
     */
    public Boolean getEstatusActivo() {
        return estatusActivo;
    }

    /**
     * Retorna el semestre que cursa actualmente el alumno.
     */
    public String getSemestre() {
        return semestre;
    }

    /**
     * Retorna el número de materias que el alumno tiene registradas.
     */
    public Integer getCantidadMaterias() {
        return cantidadMaterias;
    }
}