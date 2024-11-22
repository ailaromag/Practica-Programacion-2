/*
 * CLASE FicheroObjetosPalabraOut
 * AGLUTINA LAS DECLARACIONES Y FUNCIONALIDADES PARA LLEVAR A CABO LA GESTIÓN 
 * DE FICHEROS DE OBJETOS de tipo Partida A NIVEL DE ESCRITURA
 */
package practicafinal2023;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FicheroPartidaOut {

    /*Atributos
     * Declaración del Objeto ObjectOutputStream para posibilitar la escritura 
     * en ficheros a nivel de Partida.
     */
    private ObjectOutputStream fichero1 = null;
    String nombreFichero;
    
    
    public FicheroPartidaOut(String nombreFichero) {
        this.nombreFichero = nombreFichero;
        try {
            File f = new File(nombreFichero);
                fichero1 = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nombreFichero)));
        } catch (IOException ex) {
            System.out.println("Error al abrir el fichero");
        }
    }

    /*
    * Método de escritura de objetos partida en el fichero enlazado con el
    * objeto FicheroPartidaOut, que lleva a cabo la escritura de un objeto partida 
    * pasado por parámetro.
    */
     public void escrituraPartidaFichero(Partida partida) {
        try {
                fichero1.writeObject(partida);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
     
     
      
    /*
     * Método que lleva a cabo el CIERRE DEL ENLACE DEL FICHERO, se añade 
     * el CENTINELA al final.
     */
      
    public void cierre() {
        try {
            fichero1.writeObject(Partida.CENTINELA);
            fichero1.close();
        } catch (IOException ex) {
            System.out.println("Error en cierre: "+ ex);
        }
    }
    
    public void vaciar(){
        try {
            fichero1.reset();
        } catch (IOException ex) {
            Logger.getLogger(FicheroPartidaOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

}
