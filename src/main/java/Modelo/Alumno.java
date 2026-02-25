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
 * Clase de entidad principal de la capa del Modelo en el patrón MVC.
 * Representa a un estudiante dentro del sistema de control escolar.
 */
public class Alumno {
    
    // Atributos privados para mantener el encapsulamiento de los datos.
    private String idAlumno;
    private String nombreCompleto;
    
    /** * Relación de Composición: Un alumno "tiene una" inscripción.
     * Esto significa que los datos académicos del semestre dependen 
     * directamente de la existencia de este alumno.
     */
    private Inscripcion inscripcion; 

    /**
     * Constructor de la clase.
     * Se utiliza para crear el objeto completo del estudiante, 
     * vinculándolo inmediatamente con su objeto Inscripcion correspondiente.
     */
    public Alumno(String idAlumno, String nombreCompleto, Inscripcion inscripcion) {
        this.idAlumno = idAlumno;
        this.nombreCompleto = nombreCompleto;
        this.inscripcion = inscripcion;
    }

    /**
     * Retorna la matrícula o identificador único del estudiante.
     * Se usa principalmente para las búsquedas en el DAO y para la tabla de la Vista.
     */
    public String getIdAlumno() {
        return idAlumno;
    }

    /**
     * Retorna el nombre completo del estudiante.
     * Utilizado para mostrar los resultados en pantalla y para redactar la constancia.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Retorna el objeto Inscripcion asociado a este alumno.
     * El Controlador llama a este método para poder acceder a los datos 
     * del semestre y validar si el estatus es activo.
     */
    public Inscripcion getInscripcion() {
        return inscripcion;
    }
}