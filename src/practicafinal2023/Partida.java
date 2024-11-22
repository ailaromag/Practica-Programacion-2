/*
* Clase donde tratamos con el concepto partida, definido como un objeto 
* constituido por los atributos puntos, columnas y filas.
* Bajo esta clase encontramos los métodos a emplear durante el transcurso de un 
* nuevo juego.
 */
package practicafinal2023;

import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 */
public class Partida implements Serializable {

    String nombreJugador;
    String fecha;
    int puntos;
    int filasP;
    int columnasP;
    boolean continuarP;

    String directorio;  //Nombre de la carpeta predeterminada de imagenes

    public static Partida CENTINELA = new Partida(-1, -1, -1);

    public Partida() {
        this.puntos = 0;
    }

    public Partida(int puntos, int columnas, int filas) {
        this.puntos = puntos;
        columnasP = columnas;
        filasP = filas;
        nombreJugador = "";
        continuarP = true;
    }

    public Partida(String directorio) {
        this.puntos = 0;
        this.continuarP = true;
        getFechaActual();
        obtenerDatosJ();
        this.directorio = directorio;

    }

    /*
    * Método que elige un elemento de manera aleatoria de los archivos que se encuentran
    * en el directorio asignado, seguidamente, se asignará el elemento seleccionado 
    * como resultado del random como la imagen ganadora.
     */
    public Image elegirImagenAleatoria() {
        //Primeramente se realiza la selección aleatoria del directorio predeterminado
        Random rand = new Random();
        File[] files = new File(directorio).listFiles();

        if (comprobarDirectorio(files)) {
            File file = files[rand.nextInt(files.length)];
            Image ImagenGanadora = null;

            try { //Se escoge la imagen ganadora a partir del directorio
                ImagenGanadora = ImageIO.read(file); //actualizamos la imagen ganadpra

            } catch (IOException ex) {
                Logger.getLogger(PanelSubImagenes.class.getName()).log(Level.SEVERE, null, ex);
            }
            return ImagenGanadora;
        }
        return null;
    }

    public boolean comprobarDirectorio(File[] files) {
        if (files.length == 0) {
            JOptionPane.showMessageDialog(null, "No hay imagenes en el directorio seleccionado, por favor elija un directorio con imágenes.", "Directorio erroneo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
            return false;
        }
        if (!sonImagenes(files)) {
            JOptionPane.showMessageDialog(null, "Los archivos del directorio seleccionado no son imágenes, por favor elija un directorio con imágenes.", "Directorio erroneo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
            return false;
        }
        return true;
    }

    /*
    * Método que comprueba que todos los archivos sean imagenes
     */
    public boolean sonImagenes(File[] files) {
        Image aux;
        for (File file : files) {
            try {
                aux = ImageIO.read(file);
                if (aux == null) {
                    return false;
                }               
            } catch (IOException ex) { //Si no se consigue establecer el enlace para la lectura de ImageIO 
               return false;           //es porque no hay imágenes en este directorio
            }
        }
        return true;
    }

    /*
    * Método que mediante un JOptionPane obtiene los datos necesarios
    * para inicializar el panel de SubImágenes de la partida
     */
    public void obtenerDatosJ() {
        // JOptionPane optionPane;

        this.filasP = 0;
        this.columnasP = 0;

        JTextField nombre = new JTextField();
        JTextField filas = new JTextField();
        JTextField columnas = new JTextField();

        Object[] message = {
            "Nombre:", nombre,
            "Filas:", filas,
            "Columnas:", columnas
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Nueva Partida", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/juego.png"));

        if (option == JOptionPane.OK_OPTION) {
            try {

                nombreJugador = nombre.getText();
                filasP = Integer.parseInt(filas.getText());
                columnasP = Integer.parseInt(columnas.getText());
                if (filasP * columnasP < 2) { //Se hace una multiplicación para que se puedan hacer puzzles de una fila o de una columna
                    JOptionPane.showMessageDialog(null, "¡Parámetros demasiado pequeños!, introduzca valores MÁS grandes.  \nLas entrada más pequeñas posibles són 2 - 1 o 1 - 2.", "Error", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
                    obtenerDatosJ();
                }
                if("".equals(nombreJugador)){
                    JOptionPane.showMessageDialog(null, "¡Debe introducir un nombre! ", "Error", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
                    obtenerDatosJ();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "¡Se ha olvidado de introducir los valores! ", "Error", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
                continuarP = false;
                obtenerDatosJ();
            }

        } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            continuarP = false;
        }

        if (filasP > 10 || columnasP > 10) {
            JOptionPane.showMessageDialog(null, "¡Parámetros demasiado grandes!, introduzca valores menores o iguales a 10.", "Error", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("iconos/error.png"));
            obtenerDatosJ();
        }

    }

    public void elegirImagen(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    //Método que retorna true si es centinela
    public boolean isCentinela() {
        return puntos == -1;
    }

    public Partida getCentinela() {
        return CENTINELA;
    }

    public void getFechaActual() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.fecha = dateFormat.format(date);
    }

    public boolean isContinuarP() {
        return continuarP;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getFilasP() {
        return filasP;
    }

    public int getColumnasP() {
        return columnasP;
    }

    public String getFecha() {
        return fecha;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void puntuarGanador() {
        puntos = filasP * columnasP;
    }

    @Override
    public String toString() {
        return " Jugador: " + getNombreJugador() + "      Fecha:  " + getFecha() + "      Puntos: " + getPuntos();
    }

}
