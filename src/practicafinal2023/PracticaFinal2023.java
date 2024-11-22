/*
 * Clase principal que conforma la ejecución del programa. Aquí se encuentran 
 * todos los componentes que se añadirán al JFrame, y los métodos que conforman 
 * la estructura del juego.
 */
package practicafinal2023;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import static java.awt.FlowLayout.LEFT;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;

/**
 *
 */
public class PracticaFinal2023 {

    private JFrame ventana;
    private Container panelContenidos;
    private static final int DIM_X = 850;
    private static final int DIM_Y = 800;

    private final PanelSubImagenes pAux = new PanelSubImagenes();

    Partida partida;
    PanelSubImagenes pSubImagenesPartida;
    Image ImagenGanadora;

    //COMPONENTES MENÚ DESPLEGABLE
    JMenuBar barramenu;
    JMenu menu;
    JMenuItem JMnuevaPartidaBoton, JMdirectorioBoton, JMhistorialBoton, JMsalirBoton;

    //COMPONENTES MENÚ JTOOLBAR
    JToolBar iconosMenu;
    JButton JTnuevaPartidaIcono, JThistorialGeneralIcono, JThistorialSelectivoIcono, JTcambiarDirectorioIcono, JTsalirIcono;

    //COMPONENTES PANEL BOTONES LADO ESTE
    JPanel panelBotones;
    JButton JBnuevaPartidaBoton, JBhistorialGeneralBoton, JBhistorialSelectivoBoton, JBsalirBoton;

    //COMPONENTES PANEL PDE VISUALIZACIONES
    JPanel panelVisualizaciones; //panel que engloba los subpaneles de la partida con CardLayout
    JPanel panelPartida; //panel que conforma el puzzle con las subimagenes
    PanelSubImagenes pSubImagenes;

    //COMPONENTES BARRA TEMPORAL
    BarraTemporal barraTemporal;
    Timer cronometro;
    JButton botonContinuar;

    JPanel panelStandby; //logo grande de la UIB
    JLabel imagenUIB;
    JPanel panelHistorial;

    //PANEL CONTENEDOR MENÚS NORTE
    JPanel contenedorMenus;

    //SEPARADORES
    JSplitPane separador1, separador2, separador3;

    String directorio = "Imagenes";
    String carpeta = "iconos/";

    //CARD LAYOUTS
    private CardLayout cl1;

    //Atributos para el juego
    boolean partidaIniciada = false; //Boolean que indica si hay una partida en curso
    boolean continuarHS = false;
    FicheroPartidaOut fout = new FicheroPartidaOut("resultados.dat"); //Abrimos el enlace de escritura con el archivo 

    boolean streamFOutCerrado = false;

    private boolean primeraApretada = false;

    boolean añadidaPartida = false;

    ArrayList<Partida> contenidos = new ArrayList<Partida>();


    public static void main(String[] args) throws IOException {
        new PracticaFinal2023().inicio();
    }

    /*
    * En este método se inicializan los contenidos del JFrame, se añaden los 
    * Layouts, separadores y se definen las dimensiones.
    * Se llaman a los métodos: crearMenu(), crearIconosMenu() y crearPanelBotones()
    * los cuales inicializan los distintos componentes de cada uno de los paneles.
     */
    public void inicio() throws IOException {

        /* DECLARACIÓN DEL PANEL DE CONTENIDOS DEL JFRAME */
        ventana = new JFrame("Puzzle Practica Final 2023");
        panelContenidos = ventana.getContentPane();
        panelContenidos.setLayout(new BorderLayout());

        crearMenu();
        barramenu = new JMenuBar();
        barramenu.setBackground(Color.black);
        barramenu.add(menu);

        crearIconosMenu();

        contenedorMenus = new JPanel();
        contenedorMenus.setBackground(Color.BLACK);
        contenedorMenus.setForeground(Color.WHITE);
        contenedorMenus.setLayout(new BorderLayout());
        contenedorMenus.add(barramenu, NORTH);
        contenedorMenus.add(iconosMenu, SOUTH);

        crearPanelBotones();
        panelVisualizaciones = new JPanel();
        // panelVisualizaciones.setSize(700, 650);
        cl1 = new CardLayout();
        panelVisualizaciones.setLayout(cl1);

        panelStandby = new JPanel();

        ImageIcon uib = new ImageIcon("UIB.jpg");
        imagenUIB = new JLabel(uib);

        panelStandby.add(imagenUIB);
        panelVisualizaciones.add(panelStandby, "1");

        //Añadimos los componentes a los separadores, que posteriormente son añadidos al panel de contenidos
        separador1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBotones, panelVisualizaciones);
        separador2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contenedorMenus, separador1);
        separador3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, separador2, null);
        panelContenidos.add(separador1, CENTER);
        panelContenidos.add(separador2, NORTH);
        panelContenidos.add(separador3, SOUTH);


        /* AÑADIMOS LOS COMPONENTES AL PANEL DE CONTENIDOS */
        //DIMENSIONAMIENTO DEL CONTENEDOR JFrame // 
        ventana.setSize(1300, 870);
        ventana.setResizable(false); //NO se podrá cambiar la dimensión

        //CENTRADO DEL CONTENEDOR ventana EN EL CENTRO DE LA PANTALLA
        ventana.setLocationRelativeTo(null);
        //ACTIVACIÓN DEL CIERRE INTERACTIVO VENTANA DE WINDOWS EN EL CONTENEDOR 
        //JFrame ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //VISUALIZACIÓN CONTENEDOR JFrame ventana
        ventana.setVisible(true);

    }

    /*
    * Método que inicializa el menú desplegable
     */
    public void crearMenu() {
        menu = new JMenu("MENU");
        menu.setSize(DIM_X, 20);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Courier New", Font.BOLD, 14));

        JMnuevaPartidaBoton = new JMenuItem("NUEVA PARTIDA");
        JMnuevaPartidaBoton.addActionListener(new gestorEvento());
        JMnuevaPartidaBoton.setBackground(Color.BLACK);
        JMnuevaPartidaBoton.setForeground(Color.WHITE);
        JMnuevaPartidaBoton.setFont(new Font("Courier New", Font.BOLD, 11));

        JMdirectorioBoton = new JMenuItem("CAMBIAR DIRECTORIO");
        JMdirectorioBoton.addActionListener(new gestorEvento());
        JMdirectorioBoton.setBackground(Color.BLACK);
        JMdirectorioBoton.setForeground(Color.WHITE);
        JMdirectorioBoton.setFont(new Font("Courier New", Font.BOLD, 11));

        JMhistorialBoton = new JMenuItem("HISTORIAL");
        JMhistorialBoton.addActionListener(new gestorEvento());
        JMhistorialBoton.setBackground(Color.BLACK);
        JMhistorialBoton.setForeground(Color.WHITE);
        JMhistorialBoton.setFont(new Font("Courier New", Font.BOLD, 11));

        JMsalirBoton = new JMenuItem("SALIR");
        JMsalirBoton.addActionListener(new gestorEvento());
        JMsalirBoton.setBackground(Color.BLACK);
        JMsalirBoton.setForeground(Color.WHITE);
        JMsalirBoton.setFont(new Font("Courier New", Font.BOLD, 11));

        menu.add(JMnuevaPartidaBoton);
        menu.add(JMdirectorioBoton);
        menu.add(JMhistorialBoton);
        menu.add(JMhistorialBoton);
    }

    /*
    * Método que inicializa el menú de iconos
     */
    public void crearIconosMenu() {
        iconosMenu = new JToolBar("iconos menu");
        iconosMenu.setBackground(Color.black);

        iconosMenu.setLayout(new FlowLayout(LEFT));

        JTnuevaPartidaIcono = new JButton();
        JTnuevaPartidaIcono.setBackground(Color.black);

        ImageIcon icono1 = new ImageIcon(carpeta + "iconoNuevaPartida.jpg");
        JTnuevaPartidaIcono.setIcon(icono1);
        JTnuevaPartidaIcono.addActionListener(new gestorEvento());
        iconosMenu.add(JTnuevaPartidaIcono);

        JThistorialGeneralIcono = new JButton();
        JThistorialGeneralIcono.setBackground(Color.black);
        ImageIcon icono2 = new ImageIcon(carpeta + "iconoHistorialGeneral.jpg");
        JThistorialGeneralIcono.setIcon(icono2);
        JThistorialGeneralIcono.addActionListener(new gestorEvento());
        iconosMenu.add(JThistorialGeneralIcono);

        JThistorialSelectivoIcono = new JButton();
        JThistorialSelectivoIcono.setBackground(Color.black);

        ImageIcon icono3 = new ImageIcon(carpeta + "iconoHistorialSelectivo.jpg");
        JThistorialSelectivoIcono.setIcon(icono3);
        JThistorialSelectivoIcono.addActionListener(new gestorEvento());
        iconosMenu.add(JThistorialSelectivoIcono);

        JTcambiarDirectorioIcono = new JButton();
        JTcambiarDirectorioIcono.setBackground(Color.black);

        ImageIcon icono4 = new ImageIcon(carpeta + "iconoCambiarDirectorio.jpg");
        JTcambiarDirectorioIcono.setIcon(icono4);
        JTcambiarDirectorioIcono.addActionListener(new gestorEvento());
        iconosMenu.add(JTcambiarDirectorioIcono);

        JTsalirIcono = new JButton();
        JTsalirIcono.setBackground(Color.black);
        ImageIcon icono5 = new ImageIcon(carpeta + "iconoSalir.jpg");
        JTsalirIcono.setIcon(icono5);
        JTsalirIcono.addActionListener(new gestorEvento());
        iconosMenu.add(JTsalirIcono);
    }

    /*
    * Método que inicializa el panel de botones
     */
    public void crearPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 2, 2));

        JBnuevaPartidaBoton = new JButton("NUEVA PARTIDA");
        JBnuevaPartidaBoton.setBackground(Color.BLACK);
        JBnuevaPartidaBoton.setForeground(Color.WHITE);
        JBnuevaPartidaBoton.setFont(new Font("Courier New", Font.BOLD, 12));
        JBnuevaPartidaBoton.addActionListener(new gestorEvento());
        panelBotones.add(JBnuevaPartidaBoton);

        //button.setFont(new Font("Arial", Font.PLAIN, 40));
        JBhistorialGeneralBoton = new JButton("HISTORIAL GENERAL");
        JBhistorialGeneralBoton.setBackground(Color.BLACK);
        JBhistorialGeneralBoton.setForeground(Color.WHITE);
        JBhistorialGeneralBoton.setFont(new Font("Courier New", Font.BOLD, 12));
        JBhistorialGeneralBoton.addActionListener(new gestorEvento());
        panelBotones.add(JBhistorialGeneralBoton);

        JBhistorialSelectivoBoton = new JButton("HISTORIAL SELECTIVO");
        JBhistorialSelectivoBoton.setBackground(Color.BLACK);
        JBhistorialSelectivoBoton.setForeground(Color.WHITE);
        JBhistorialSelectivoBoton.setFont(new Font("Courier New", Font.BOLD, 12));
        JBhistorialSelectivoBoton.addActionListener(new gestorEvento());
        panelBotones.add(JBhistorialSelectivoBoton);

        JBsalirBoton = new JButton("SALIR");
        JBsalirBoton.setBackground(Color.BLACK);
        JBsalirBoton.setForeground(Color.WHITE);
        JBsalirBoton.setFont(new Font("Courier New", Font.BOLD, 12));
        JBsalirBoton.addActionListener(new gestorEvento());

        panelBotones.add(JBsalirBoton);

    }


    /*
    * Método que conforma el inicio de una nueva partida
     */
    public void nuevaPartida() {

        añadidaPartida = true;
        //actualizamosHistorial();
        //Creamos una nueva partida
        partida = new Partida(directorio);
        partidaIniciada = true;

        ImagenGanadora = partida.elegirImagenAleatoria();
        if (ImagenGanadora == null) {// Si NO hemos conseguido obtener una imagen ganadora del directorio, entonces NO se inicia la partida
            partida.continuarP = false;

        }

        if (partida.continuarP) {
            try {
                //Se crea el Panel de SubImagenes a partir de los datos de la partida
                pSubImagenesPartida = new PanelSubImagenes(partida.filasP, partida.columnasP, new manipuladorEventosPanelJuego(), ImagenGanadora);
            } catch (IOException ex) {
                Logger.getLogger(PracticaFinal2023.class.getName()).log(Level.SEVERE, null, ex);
            }

            panelPartida = new JPanel();
            // panelPartida.setLayout(new BorderLayout());

            BoxLayout caja = new BoxLayout(panelPartida, BoxLayout.Y_AXIS);
            panelPartida.setLayout(caja);
            panelPartida.add(Box.createVerticalGlue());

            //Inicializamos la BARRA TEMPORAL
            barraTemporal = new BarraTemporal(new gestorEvento(), partida.getFilasP(), partida.getColumnasP());
            cronometro = new Timer(100, new gestorEvento());
            cronometro.start();

            //Creamos el panel de subImagenes de la partida
            pSubImagenes = pSubImagenesPartida;

            pSubImagenes.setMaximumSize(new Dimension(pSubImagenesPartida.getAnchura(), pSubImagenesPartida.getAltura()));

            //partida.getSubImagenesPartida();
            //Añadimos los componentes al panelPartida en las posiciones correspondientes
            panelPartida.add(pSubImagenes);
            panelPartida.add(barraTemporal);

            //Añadimos el panelPartida al panel de Visualizaciones con CardLayout()
            panelVisualizaciones.add(panelPartida, "2");
            //  panelVisualizaciones.setSize(pSubImagenesPartida.getAnchura(), pSubImagenesPartida.getAltura());

            //Visualizamos el panel partida
            cl1.show(panelVisualizaciones, "2");

        } else {
            //Si la partida no es iniciada por algún error, entonces
            //volvemos a la pantalla principal

            partidaIniciada = false;
            cl1.show(panelVisualizaciones, "1");
        }

    }


    /*
    *Método que cambia el directorio mediante un objeto del tipo FileChooser.
     */
    public void cambiarDirectorio() {
        //Inicializamos un objeto del tipo FileChooser
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(panelVisualizaciones);

        if (result != JFileChooser.CANCEL_OPTION) {
            File fileName = fileChooser.getSelectedFile(); //obtenemos el archivo (de tipo carpeta) seleccionado
            if ((fileName == null) || (fileName.getName().equals(""))) {
                directorio = null;
            } else {
                directorio = fileName.getAbsolutePath();
            }
        }
    }

    /*
    * En este método se establece un enlace con el archivo resultados.dat y se 
    * leen los datos de las partidas guardadas para posteriormente ser añadidas 
    * a un objeto de tipo JTextArea.
     */
    public void historialGeneral() {

        //Inicializamos el panel del historial
        panelHistorial = new JPanel();
        panelHistorial.setBackground(Color.WHITE);
        String contenidosS = "                   HISTORIAL                " + '\n' + '\n';

        //Llamamos al método que lee los contenidos del fichero
        contenidosS = contenidosS + lecturaContenidosFichero();

        //Se añade el String de Partidas al objeto JTextArea, y este
        //se introduce al panel del Historial.
        JTextArea texto = new JTextArea(contenidosS);
        Font font = new Font("Monospaced", Font.BOLD, 16);
        texto.setFont(font);

        panelHistorial.add(texto);

    }

    /*
    * MÉTODO de LECTURA de los CONTENIDOS DEL FICHERO.
    * Si se ha jugado una nueva Partida, se actualizan los contenidos del historial, y 
    * a continuación se leen las Partidas del fichero "resultados.dat".
    *
    * Si no se ha jugado ninguna Partida más, se visualizan por pantalla los contenidos
    * del fichero anteriormente leídos.
    */
    public String lecturaContenidosFichero() {

        String s = "";

        //Importante para cuando no se añadan Partidas,
        //siga habiendo partidas en el historial guardadas
        if (añadidaPartida) {
            actualizamosHistorial(); //reescribimos las partidas anteriores y escribimos a continuación las nuevas

            fout.cierre();
            int y = 0;

            //ESTABLECEMOS el enlace de LECTURA con el fichero  
            FicheroPartidaIn fIn = new FicheroPartidaIn();
            Partida pAux = fIn.leerPartida(); //Variable que nos servirá para leer las partidas
            while (!pAux.isCentinela()) {
                s = s + '\n' + pAux.toString();//Se van añadiendo las partidas en modo de String
                pAux = fIn.leerPartida(); //Se lee la siguiente Partida 
            }
            fIn.cerrarFichero();

        } else {
            for (int t = 0; t < contenidos.size(); t++) {
                s = s + '\n' + contenidos.get(t).toString();
            }
        }
        añadidaPartida = false;

        return s;

    }
    
     /*
    * Método que permite visualizar el historial General en
    * eñ panel de visualizaciones
    */
    public void showHistorialGeneral() {

        //Volvemos a ABRIR el STREAM de ESCRITURA
        fout = new FicheroPartidaOut("resultados.dat");

        //Añadimos en el CardLayout y visualizamos el panel del historial en 
        //el panelVisualizaciones
        panelVisualizaciones.add(panelHistorial, "3");
        cl1.show(panelVisualizaciones, "3");
    }


    /*
    *Método que establece un enlace con el archivo resultados.dat y se leen los
    *datos de la partida que corresponde al nombre del jugador pasado por parámetro.
     */
    public void historialSelectivo() {
        //Leemos los contenidos del fichero, aunque retorna un String, NO lo usamos en esta ocasion
        //Primeramente debemos de preguntar el nombre del Jugador a buscar
        String nombre = "";
        JTextField nombreJugador = new JTextField();
        Object[] mensaje = {"Nombre :", nombreJugador};

        int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Historial Selectivo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/person.png"));
        if (opcion == JOptionPane.OK_OPTION) {
            nombre = nombreJugador.getText(); //Guardamos el nombre obtenido en el JOptionPane en la variable tipo String nombre
            continuarHS = true;
        }
        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            continuarHS = false;
        }

        if (continuarHS) {
            String s = "               HISTORIAL SELECTIVO              " + '\n' + '\n';

            s = s + lecturaContenidosFicheroSelectivo(nombre);

            panelHistorial = new JPanel();
            panelHistorial.setBackground(Color.WHITE);

            //Finalmente se añade el objeto JTextArea al panel del Historial,
            //y este es visualizado en el panelVisualizaciones.
            JTextArea texto = new JTextArea(s);
            Font font = new Font("Monospaced", Font.BOLD, 16);
            texto.setFont(font);

            panelHistorial.add(texto);

            fout = new FicheroPartidaOut("resultados.dat");
            streamFOutCerrado = false;

            panelVisualizaciones.add(panelHistorial, "3");
            cl1.show(panelVisualizaciones, "3");
        } else {
            cl1.show(panelVisualizaciones, "1");
        }
    }
    
    
    /*
    * MÉTODO de LECTURA de los CONTENIDOS SELECTIVO.
    * Si se ha jugado una nueva Partida, se actualizan los contenidos del historial, y 
    * a continuación se leen las Partidas del fichero "resultados.dat" donde el nombre coincida 
    * con el String pasado por parámetro.
    *
    * Si no se ha jugado ninguna Partida más, se visualizan por pantalla los contenidos
    * del fichero donde el nombre coincida con el String pasado por parámetro, posteriormente leídos.
    */

    public String lecturaContenidosFicheroSelectivo(String nombre) {
        String s = "";

        if (añadidaPartida) {
            actualizamosHistorial();

            fout.cierre();

            //ESTABLECEMOS el enlace de LECTURA con el fichero  
            FicheroPartidaIn fIn = new FicheroPartidaIn();
            Partida pAux = fIn.leerPartida(); //Variable que nos servirá para leer las partidas
            while (!pAux.isCentinela()) {
                if (pAux.getNombreJugador().equals(nombre)) {
                    s = s + '\n' + pAux.toString();//Se van añadiendo las partidas en modo de String
                }
                pAux = fIn.leerPartida(); //Se lee la siguiente Partida
            }
            fIn.cerrarFichero();
        } else {
            for (int t = 0; t < contenidos.size(); t++) {
                if (contenidos.get(t).getNombreJugador().equals(nombre)) {
                    s = s + '\n' + contenidos.get(t).toString();
                }
            }
        }
        añadidaPartida = false;
        return s;

    }

   /*
    * Método que finaliza el programa
    */
    public void salir() {
        JOptionPane.showMessageDialog(null, "¡Hasta otra!", "Salir Juego", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/ghost.png"));
        if (!streamFOutCerrado) {
            fout.cierre();//Cerramos el Stream de escritura
            streamFOutCerrado = true;
        }
        System.exit(0);
    }

    /*
    * Método que actualiza los contenidos del historial, reescribiendo las partidas anteriores
    * y escribiendo a continuación las nuevas
     */
    public void actualizamosHistorial() {
        //escritura de los contenidos
        if (!contenidos.isEmpty()) {
            fout.vaciar();
            for (int j = 0; j < contenidos.size(); j++) {
                fout.escrituraPartidaFichero(contenidos.get(j));
            }
        }
    }

    /*
    * CLASE GESTOR DE EVENTOS gestorEvento 
    * Clase que implementa un ActionListener para responder ante los eventos
    * que tienen lugar en los elementos de los menús.
     */
    private class gestorEvento implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evento) {
            if (partidaIniciada) { //Si hay una partida iniciada en transcurso.
                //Gestion del evento de la BARRA TEMPORAL
                if (evento.getSource() == cronometro) {
                    //VERIFICACIÓN SI EL VALOR ACTUAL DE LA JProgressBar barraTemporal
                    //ha llegado al valor Máximo
                    if (barraTemporal.getValorBarraTemporal() < barraTemporal.getValorMaximo()) {
                        //Asignamos a la barraTemporal su valor incrementando en una unidad determinada
                        barraTemporal.setValorBarraTemporal(barraTemporal.getValorBarraTemporal() + barraTemporal.getVELOCIDAD());
                    } else {
                        //partidaIniciada = false;
                        //Cuando se llega al final, detenemos el cronómetro
                        cronometro.stop();

                        //Se escriben los datos de la partida en el historial
                        contenidos.add(partida); //Se van añadiendo los contenidos en el arrayList de contenidos
                        //que posteriormente se escribiran en el fichero "resultados.dat"

                        //INDICAMOS AL JUGADOR QUE HA PERDIDO, ya que se le ha
                        // acabado el tiempo y mostrar la IMAGEN GANADORA
                        pantallaFinalPartida();
                        JOptionPane.showMessageDialog(null, "¡TIEMPO AGOTADO!, NO LO HAS CONSEGUIDO  \n   Nos vemos a la próxima ;) ", "Fin partida", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/gameOver.png"));

                    }
                } //Gestión de la opción de salir durante el transcurso de una partida
                else if (evento.getSource() == JTsalirIcono || evento.getActionCommand() == "SALIR") {
                    salir();
                } //Cualquier otra funcionalidad está bloqueada hasta que se termine la partida
                else if ("CONTINUAR".equals(evento.getActionCommand())) {
                    partidaIniciada = false; //Unicamente se termina la partida cuando el usuario le da al botón de continuar
                    cl1.show(panelVisualizaciones, "1");
                } else {
                    JOptionPane.showMessageDialog(null, "ANTES DEBE DE TERMINAR LA PARTIDA EN CURSO ");
                }

            } else {
                switch (evento.getActionCommand()) {
                    case "NUEVA PARTIDA":
                        nuevaPartida();

                        break;
                    case "CAMBIAR DIRECTORIO":
                        cambiarDirectorio();
                        break;
                    case "HISTORIAL":
                        historialGeneral();
                        showHistorialGeneral();
                        break;
                    case "HISTORIAL GENERAL":
                        historialGeneral();
                        showHistorialGeneral();
                        break;
                    case "HISTORIAL SELECTIVO":
                        historialSelectivo();

                        break;
                    case "SALIR":
                        salir();
                        break;
                }

                if (evento.getSource() == JTnuevaPartidaIcono) {
                    nuevaPartida();
                }
                if (evento.getSource() == JThistorialGeneralIcono) {
                    historialGeneral();
                    showHistorialGeneral();

                }
                if (evento.getSource() == JThistorialSelectivoIcono) {
                    historialSelectivo();
                }
                if (evento.getSource() == JTcambiarDirectorioIcono) {
                    cambiarDirectorio();
                }
                if (evento.getSource() == JTsalirIcono) {
                    salir();
                }
            }
        }
    }

    /*
    * CLASE MANIPULADOR DE EVENTOS manipuladorEventosPanelJuego 
    *Clase que implementa un MouseListener y responde a los eventos que tienen 
    *lugar en el panelVisualizaciones y que intervienen en el transcurso de la 
    *partida.
     */
    private class manipuladorEventosPanelJuego implements MouseListener {

        subImagen subImagenSeleccionada1 = new subImagen(); //inicializamos dos subImagenes vacias
        subImagen subImagenSeleccionada2 = new subImagen();

        @Override
        public void mousePressed(MouseEvent evento) {

            //Primera SubImagen Pulsada
            if (!primeraApretada) {//Miramos si es el primer "click", es decir, la primera subImagen seleccionada
                subImagenSeleccionada1 = (subImagen) evento.getSource();
                primeraApretada = true;

                //Segunda SubImagen Pulsada    
            } else {//Si no es la primera, entonces será la segunda, y determinara el intercambio  
                subImagenSeleccionada2 = (subImagen) evento.getSource();

                //Llamamos al método que intercambia las posiciones de las subImagenes pulsadas
                pSubImagenes.moverSubImagenes(subImagenSeleccionada1, subImagenSeleccionada2);

                //Se actualiza el panel de subImagenes con las nuevas posiciones
                pSubImagenesPartida.actualizarPanelSubImagenes();

                //Se muestra en el panelVisualizaciones el panel de subImagenes actualizado
                panelPartida.add(pSubImagenes, 1);
                panelVisualizaciones.add(panelPartida, "2");
                cl1.show(panelVisualizaciones, "2");

                primeraApretada = false;

            }

            //Si las subImagenes se encuentran en las posiciones "GANADORAS", entonces finaliza el juego
            if (pSubImagenesPartida.imagenesPosicionGanadora()) {
                //  partidaIniciada = false;
                cronometro.stop();

                //Se actualizan los puntos del jugador
                partida.puntuarGanador();

                contenidos.add(partida); //Se van añadiendo los contenidos en el arrayList de contenidos
                //que posteriormente se escribiran en el fichero "resultados.dat"

                pantallaFinalPartida();

                JOptionPane.showMessageDialog(null, "¡ENHORABUENA, has ganado! \n  Has obtenido " + partida.getPuntos() + " puntos.", "Ganador Partida", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/champion.png"));
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    public void pantallaFinalPartida() {
        JPanel imagenGanadora = new JPanel();
        botonContinuar = new JButton("CONTINUAR");
        botonContinuar.setBackground(Color.BLACK);
        botonContinuar.setForeground(Color.WHITE);
        botonContinuar.setFont(new Font("Courier New", Font.BOLD, 12));
        botonContinuar.setPreferredSize(new Dimension(DIM_X, 40));

        botonContinuar.addActionListener(new gestorEvento());

        imagenGanadora.setLayout(new BorderLayout());
        JLabel imG = new JLabel(pAux.redimensionarImagen(new ImageIcon(ImagenGanadora), pSubImagenes.getAnchura(), pSubImagenes.getAltura()));

        imagenGanadora.add(imG, BorderLayout.CENTER);
        imagenGanadora.add(botonContinuar, BorderLayout.SOUTH);

        panelVisualizaciones.add(imagenGanadora, "2");
        cl1.show(panelVisualizaciones, "2");
    }
}
