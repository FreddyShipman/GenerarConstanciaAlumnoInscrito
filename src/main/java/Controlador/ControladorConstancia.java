/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author alfre
 */

import Modelo.Alumno;
import Modelo.AlumnoDAO;
import Modelo.Constancia;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Clase que representa el Controlador en el patrón de arquitectura MVC.
 * Actúa como el intermediario central (el cerebro) entre la Vista (lo que ve el usuario) 
 * y el Modelo (los datos). Se encarga de procesar las peticiones, aplicar las reglas 
 * de negocio y devolver respuestas.
 */
public class ControladorConstancia {
    
    // Conexión privada con el objeto de acceso a datos (Modelo)
    private AlumnoDAO dao;

    /**
     * Constructor de la clase.
     * Al instanciar el controlador (cuando se abre la pantalla), inicializamos 
     * nuestra "base de datos" simulada para que esté lista para operar.
     */
    public ControladorConstancia() {
        this.dao = new AlumnoDAO();
    }

    /**
     * Atiende la petición de la Vista cuando el usuario teclea en el buscador.
     * Su función es pasar el texto parcial al DAO y retornar la lista de alumnos 
     * encontrados para que la Vista actualice la tabla.
     */
    public List<Alumno> buscarCoincidencias(String idParcial) {
        return dao.buscarPorIdParcial(idParcial);
    }

    /**
     * Atiende la petición de la Vista cuando el usuario hace clic en un alumno de la tabla.
     * Extrae al estudiante exacto desde el DAO para enviar toda su información a 
     * los campos de confirmación en la interfaz.
     */
    public Alumno obtenerDatosAlumno(String idExacto) {
        return dao.buscarPorIdExacto(idExacto);
    }

    /**
     * Aplica la principal Regla de Negocio del sistema.
     * Protege el proceso asegurándose de que el trámite solo proceda si el objeto 
     * alumno es válido, si realmente tiene una inscripción, y si su estatus es "activo".
     */
    public Boolean validarInscripcion(Alumno alumno) {
        if (alumno != null && alumno.getInscripcion() != null) {
            return alumno.getInscripcion().getEstatusActivo();
        }
        return false; // Retorna falso si falla alguna validación, bloqueando el botón
    }

    /**
     * Ejecuta el caso de uso principal: generar el documento físico/digital.
     * Toma al alumno validado, genera información automática (un folio rastreable único 
     * y la fecha actual del sistema), redacta la plantilla oficial y crea la entidad 
     * Constancia para devolvérsela a la Vista.
     */
    public Constancia crearConstancia(Alumno alumno) {
        // Generamos un folio aleatorio corto para simular un registro real del sistema
        String folio = "CONSTANCIA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Date fechaEmision = new Date();
        
        // Redactamos el texto del documento uniendo la plantilla con los datos del modelo
        String contenido = "A QUIEN CORRESPONDA:\n\n" +
                           "Por medio de la presente se hace constar que el(la) C. " + 
                           alumno.getNombreCompleto() + " con número de control " + 
                           alumno.getIdAlumno() + ",\n" +
                           "se encuentra debidamente inscrito(a) en el " + 
                           alumno.getInscripcion().getSemestre() + " semestre, " +
                           "cursando un total de " + alumno.getInscripcion().getCantidadMaterias() + 
                           " materias en el periodo actual.\n\n" +
                           "Se extiende la presente para los fines que al interesado convengan.";
                           
        return new Constancia(folio, fechaEmision, contenido);
    }
}