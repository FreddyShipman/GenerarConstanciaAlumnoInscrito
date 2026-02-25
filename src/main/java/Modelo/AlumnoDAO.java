/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author alfre
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) que pertenece a la capa del Modelo en MVC.
 * En este proyecto funciona como un "Mock" (simulador), reemplazando una conexión real
 * a base de datos por una lista en memoria para almacenar y gestionar los registros.
 */
public class AlumnoDAO {
    
    // Lista privada que actúa como nuestra base de datos simulada
    private List<Alumno> bdSimulada;

    /**
     * Constructor de la clase.
     * Inicializa la lista y la prueba con datos estáticos.
     * Esto permite probar toda la lógica del sistema sin requerir un servidor SQL.
     */
    public AlumnoDAO() {
        bdSimulada = new ArrayList<>();
        // Creación de datos mockeados
        bdSimulada.add(new Alumno("252530", "Manuel Romo", new Inscripcion("6to", 5, true)));
        bdSimulada.add(new Alumno("252531", "Santiago Rivera", new Inscripcion("8vo", 4, true)));
        bdSimulada.add(new Alumno("252529", "Valeria Urias", new Inscripcion("8vo", 8, true)));
        bdSimulada.add(new Alumno("252432", "Bruno Torres", new Inscripcion("2do", 6, false)));
    }

    /**
     * Realiza una búsqueda dinámica de alumnos.
     * El Controlador invoca este método cada vez que el usuario teclea en la Vista,
     * devolviendo todos los registros cuyo ID comience con los caracteres ingresados.
     */
    public List<Alumno> buscarPorIdParcial(String id) {
        List<Alumno> coincidencias = new ArrayList<>();
        for (Alumno a : bdSimulada) {
            // Filtra si el ID ingresado coincide con el inicio del ID del alumno
            if (a.getIdAlumno().startsWith(id)) {
                coincidencias.add(a);
            }
        }
        return coincidencias;
    }

    /**
     * Busca un alumno específico mediante su identificador único.
     * Se ejecuta cuando el usuario selecciona una fila de la tabla en la interfaz,
     * devolviendo el objeto Alumno completo para mostrarlo en el panel de confirmación.
     */
    public Alumno buscarPorIdExacto(String id) {
        for (Alumno a : bdSimulada) {
            if (a.getIdAlumno().equals(id)) {
                return a; // Retorna el objeto
            }
        }
        return null; // Retorna nulo si no hay coincidencias
    }
}