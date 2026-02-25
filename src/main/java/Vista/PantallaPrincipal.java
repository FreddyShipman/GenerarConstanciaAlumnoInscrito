/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

/**
 *
 * @author alfre
 */

import Controlador.ControladorConstancia;
import Modelo.Alumno;
import Modelo.Constancia;


import javax.swing.*;
import java.awt.*;
import javax.swing.text.AbstractDocument;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Clase que representa la Vista en el patrón de arquitectura MVC.
 * Es la Interfaz Gráfica de Usuario (GUI) construida con Java Swing.
 * Su única responsabilidad es dibujar la pantalla, capturar las interacciones 
 * del usuario (clics, teclas) y delegar todo el procesamiento al Controlador.
 */
public class PantallaPrincipal extends JFrame {
    
    // --- CONEXIÓN CON EL CONTROLADOR ---
    // La Vista conoce al Controlador para poder enviarle las peticiones del usuario.
    private ControladorConstancia controlador;
    
    // Objeto temporal para guardar el estudiante que el usuario seleccionó en la tabla
    private Alumno alumnoSeleccionado;

    // --- COMPONENTES DE LA INTERFAZ VISUAL ---
    private JTextField txtBusqueda;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    
    // CardLayout nos permite intercambiar vistas en el mismo panel (Vacío -> Confirmación -> Constancia)
    private JPanel panelDinamicoIzquierdo;
    private CardLayout cardLayout;

    // Componentes para mostrar los datos de confirmación
    private JLabel lblNombre, lblSemestre, lblMaterias, lblEstatus;
    private JButton btnGenerar;
    
    // Área de texto donde se dibujará la constancia final
    private JTextArea txtVistaConstancia;

    // Paleta de Colores centralizada
    private Color azulOscuro = new Color(10, 65, 115);
    private Color azulBotones = new Color(25, 118, 210); 
    private Color grisFondo = new Color(245, 247, 250);
    private Color grisFila = new Color(240, 240, 240);

    /**
     * Constructor de la Vista.
     * Al crearse la ventana, se instancia el Controlador y se construyen los componentes visuales.
     */
    public PantallaPrincipal() {
        controlador = new ControladorConstancia();
        configurarVentana();
        inicializarComponentes();
        agregarEventos();
    }

    /**
     * Configuración básica del JFrame (ventana principal).
     */
    private void configurarVentana() {
        setTitle("Sistema de Control Escolar - Generación de Constancias");
        setSize(950, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        
        JPanel contentPane = new JPanel(new BorderLayout(20, 20));
        contentPane.setBackground(grisFondo);
        contentPane.setBorder(new EmptyBorder(15, 20, 20, 20)); 
        setContentPane(contentPane);
    }

    /**
     * Método que construye, diseña y acomoda todos los botones, tablas y paneles.
     * Es estrictamente visual, aquí no hay lógica de negocio.
     */
    private void inicializarComponentes() {
        // =====================================================================================
        // ENCABEZADO SUPERIOR
        // =====================================================================================
        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEncabezado.setBackground(azulOscuro);
        panelEncabezado.setBorder(new EmptyBorder(12, 0, 12, 0)); 
        
        JLabel lblTituloGeneral = new JLabel("GENERACIÓN DE CONSTANCIAS");
        lblTituloGeneral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloGeneral.setForeground(Color.WHITE);
        panelEncabezado.add(lblTituloGeneral);
        
        add(panelEncabezado, BorderLayout.NORTH);

        // =====================================================================================
        // SECCIÓN IZQUIERDA (Buscador + Panel Dinámico de Detalles)
        // =====================================================================================
        JPanel panelIzquierdoContainer = new JPanel(new BorderLayout(0, 15));
        panelIzquierdoContainer.setOpaque(false);
        panelIzquierdoContainer.setPreferredSize(new Dimension(380, 0)); 

        // 1. Buscador
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        
        JLabel lblBuscar = new JLabel("ID Alumno:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(azulOscuro);
        
        txtBusqueda = new JTextField(8); 
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtBusqueda.setHorizontalAlignment(JTextField.CENTER);
        // Filtro para evitar que el usuario escriba más de 6 caracteres
        ((AbstractDocument) txtBusqueda.getDocument()).setDocumentFilter(new LimitadorCaracteres(6));
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBusqueda);
        
        panelIzquierdoContainer.add(panelBusqueda, BorderLayout.NORTH);

        // 2. Panel Dinámico (CardLayout)
        // Permite tener 3 "pantallas" apiladas en el mismo espacio y decidir cuál mostrar
        cardLayout = new CardLayout();
        panelDinamicoIzquierdo = new JPanel(cardLayout);
        panelDinamicoIzquierdo.setOpaque(false);

        // --- ESTADO 1: VACÍO (Instrucciones) ---
        JPanel panelVacio = new JPanel(new GridBagLayout());
        panelVacio.setBackground(Color.WHITE);
        panelVacio.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        JLabel lblInstruccion = new JLabel("<html><center>Ingrese un ID para buscar<br>y seleccione un alumno.</center></html>");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setForeground(Color.GRAY);
        panelVacio.add(lblInstruccion);

        // --- ESTADO 2: CONFIRMACIÓN (Datos y botón) ---
        JPanel panelConfirmacion = new JPanel(new GridLayout(6, 1, 5, 5));
        panelConfirmacion.setBackground(Color.WHITE);
        panelConfirmacion.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(azulOscuro, 2, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblTituloConf = new JLabel("DATOS DE CONFIRMACIÓN");
        lblTituloConf.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloConf.setForeground(azulOscuro);
        lblTituloConf.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblNombre = new JLabel("Nombre: ");
        lblSemestre = new JLabel("Semestre: ");
        lblMaterias = new JLabel("Materias: ");
        lblEstatus = new JLabel("Estatus: ");
        
        Font fuenteDatos = new Font("Segoe UI", Font.PLAIN, 14);
        lblNombre.setFont(fuenteDatos);
        lblSemestre.setFont(fuenteDatos);
        lblMaterias.setFont(fuenteDatos);
        lblEstatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnGenerar = new JButton("GENERAR CONSTANCIA");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerar.setBackground(azulBotones);
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setOpaque(true);
        btnGenerar.setContentAreaFilled(true);
        btnGenerar.setBorderPainted(false);

        panelConfirmacion.add(lblTituloConf);
        panelConfirmacion.add(lblNombre);
        panelConfirmacion.add(lblSemestre);
        panelConfirmacion.add(lblMaterias);
        panelConfirmacion.add(lblEstatus);
        panelConfirmacion.add(btnGenerar);

        // --- ESTADO 3: CONSTANCIA (Visualización del documento final) ---
        JPanel panelConstanciaContenedor = new JPanel(new BorderLayout());
        panelConstanciaContenedor.setBackground(Color.WHITE);
        panelConstanciaContenedor.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(azulBotones, 12), 
                new EmptyBorder(10, 10, 10, 10)
        ));

        txtVistaConstancia = new JTextArea();
        txtVistaConstancia.setEditable(false);
        txtVistaConstancia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtVistaConstancia.setBackground(Color.WHITE);
        txtVistaConstancia.setLineWrap(true);
        txtVistaConstancia.setWrapStyleWord(true);

        JScrollPane scrollConstancia = new JScrollPane(txtVistaConstancia);
        scrollConstancia.setBorder(BorderFactory.createEmptyBorder());
        panelConstanciaContenedor.add(scrollConstancia, BorderLayout.CENTER);

        // Se agregan los 3 estados al layout general
        panelDinamicoIzquierdo.add(panelVacio, "VACIO");
        panelDinamicoIzquierdo.add(panelConfirmacion, "CONFIRMACION");
        panelDinamicoIzquierdo.add(panelConstanciaContenedor, "CONSTANCIA");

        panelIzquierdoContainer.add(panelDinamicoIzquierdo, BorderLayout.CENTER);
        add(panelIzquierdoContainer, BorderLayout.WEST);


        // =====================================================================================
        // SECCIÓN DERECHA (Tabla de alumnos)
        // =====================================================================================
        modeloTabla = new DefaultTableModel(new String[]{"ID Alumno", "Nombre Completo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setRowHeight(35);
        tablaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tablaResultados.setShowGrid(true);
        tablaResultados.setGridColor(new Color(220, 220, 220));
        tablaResultados.setIntercellSpacing(new Dimension(1, 1));

        tablaResultados.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaResultados.getColumnModel().getColumn(0).setMaxWidth(120);
        tablaResultados.getColumnModel().getColumn(1).setPreferredWidth(300);

        JTableHeader header = tablaResultados.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(azulOscuro);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        header.setDefaultRenderer(headerRenderer);

        tablaResultados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : grisFila);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(173, 216, 230));
                    c.setForeground(Color.BLACK);
                }
                if (column == 0) setHorizontalAlignment(SwingConstants.CENTER);
                else setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        
        add(scrollTabla, BorderLayout.CENTER);
    }

    /**
     * Define qué sucede cuando el usuario interactúa con la interfaz.
     * En MVC, aquí es donde la Vista captura los eventos y llama al Controlador.
     */
    private void agregarEventos() {
        
        // EVENTO 1: Cuando el usuario teclea en el buscador
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String idParcial = txtBusqueda.getText().trim();
                
                // DELEGACIÓN AL CONTROLADOR: Se pide buscar coincidencias
                List<Alumno> coincidencias = controlador.buscarCoincidencias(idParcial);
                
                // La Vista solo actualiza la tabla con lo que le devolvió el Controlador
                modeloTabla.setRowCount(0);
                if (!idParcial.isEmpty()) {
                    for (Alumno a : coincidencias) {
                        modeloTabla.addRow(new Object[]{a.getIdAlumno(), a.getNombreCompleto()});
                    }
                }
                // Resetea el panel izquierdo por si estaba mostrando a un alumno
                cardLayout.show(panelDinamicoIzquierdo, "VACIO");
            }
        });

        // EVENTO 2: Cuando el usuario hace clic en un renglón de la tabla
        tablaResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaResultados.getSelectedRow();
                if (fila >= 0) {
                    String idExacto = modeloTabla.getValueAt(fila, 0).toString();
                    
                    // DELEGACIÓN AL CONTROLADOR: Obtiene el estudiante completo
                    alumnoSeleccionado = controlador.obtenerDatosAlumno(idExacto);
                    
                    if (alumnoSeleccionado != null) {
                        // DELEGACIÓN AL CONTROLADOR: Verifica la regla de negocio (Estatus activo)
                        boolean valido = controlador.validarInscripcion(alumnoSeleccionado);
                        
                        // La Vista se actualiza según las respuestas del Controlador
                        lblNombre.setText("Nombre: " + alumnoSeleccionado.getNombreCompleto());
                        lblSemestre.setText("Semestre: " + alumnoSeleccionado.getInscripcion().getSemestre());
                        lblMaterias.setText("Materias: " + alumnoSeleccionado.getInscripcion().getCantidadMaterias());
                        lblEstatus.setText("Estatus: " + (valido ? "ACTIVO" : "INACTIVO"));
                        lblEstatus.setForeground(valido ? new Color(0, 150, 0) : Color.RED);
                        btnGenerar.setEnabled(valido); // Bloquea o desbloquea el botón
                        
                        // Cambia la vista para mostrar la confirmación
                        cardLayout.show(panelDinamicoIzquierdo, "CONFIRMACION");
                    }
                }
            }
        });

        // EVENTO 3: Cuando el usuario hace clic en el botón "Generar Constancia"
        btnGenerar.addActionListener(e -> {
            
            // Manda a crear el documento oficial
            Constancia constancia = controlador.crearConstancia(alumnoSeleccionado);
            
            // La Vista toma el objeto Constancia generado y lo arma para presentarlo
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String textoDocumento = 
                "\n  FOLIO: " + constancia.getFolio() + "\n" +
                "  FECHA: " + formato.format(constancia.getFechaEmision()) + "\n\n" +
                "  A QUIEN CORRESPONDA:\n\n" +
                "  Por medio de la presente se hace constar que el(la) alumno(a):\n\n" +
                "  " + alumnoSeleccionado.getNombreCompleto().toUpperCase() + "\n" +
                "  con número de control escolar " + alumnoSeleccionado.getIdAlumno() + "\n\n" +
                "  Se encuentra inscrito(a) formalmente en esta institución en el \n" + 
                "  " + alumnoSeleccionado.getInscripcion().getSemestre() + " semestre, cursando un total de " + 
                alumnoSeleccionado.getInscripcion().getCantidadMaterias() + " materias.\n\n" +
                "  Se extiende la presente para los fines legales correspondientes.\n\n\n" +
                "  ___________________________\n" +
                "  Departamento Escolar";

            txtVistaConstancia.setText(textoDocumento);
            txtVistaConstancia.setCaretPosition(0); // Sube el scroll al inicio del texto
            
            // Muestra la constancia en el panel izquierdo
            cardLayout.show(panelDinamicoIzquierdo, "CONSTANCIA");
            
            // Limpia la pantalla para un trámite nuevo
            txtBusqueda.setText("");
            modeloTabla.setRowCount(0);
        });
    }

    /**
     * Punto de entrada principal para arrancar la aplicación gráfica.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PantallaPrincipal().setVisible(true));
    }
    
    /**
     * Clase auxiliar interna (Filtro).
     * Su única tarea es evitar que el usuario escriba más de 6 números en el buscador.
     */
    class LimitadorCaracteres extends javax.swing.text.DocumentFilter {
        private int maxChars;
        public LimitadorCaracteres(int maxChars) { this.maxChars = maxChars; }
        public void insertString(FilterBypass fb, int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if ((fb.getDocument().getLength() + str.length()) <= maxChars) super.insertString(fb, offs, str, a);
        }
        public void replace(FilterBypass fb, int offs, int len, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
            if ((fb.getDocument().getLength() - len + str.length()) <= maxChars) super.replace(fb, offs, len, str, a);
        }
    }
}