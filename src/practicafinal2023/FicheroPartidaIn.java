/*
* Clase en la que encontramos los métodos pertinentes a la gestión de fichero 
* concerniente a objetos partida a nivel de lectura.
*/

package practicafinal2023;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FicheroPartidaIn {

    private FileInputStream fis = null;
    private ObjectInputStream ois = null;

    
    /*
    *Constructor donde se indicará el nombre del fichero a leer.
    */
    public FicheroPartidaIn() {
        try {
            fis = new FileInputStream("resultados.dat");
            ois = new ObjectInputStream(new BufferedInputStream(fis));
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.toString());
        } catch (IOException ex) {
            System.out.println("ERROR: NO S'HA POGUT ESTABLIR L'ENLLAÇ DE LECTURA AMB EL FITXER");
            System.out.println(ex);
        }
    }

    /*
    Cerrar el fichero
     */
    public void cerrarFichero() {
        try {
            ois.close();
        } catch (IOException ex) {
            System.out.println("ERROR: ERROR EN EL TANCAMENT DEL FITXER");
        }
    }

    /*
    * Método de lectura que devuelve el objeto partida leído del fichero abierto 
    * previamente indicado en el constructor. Si no se lee nada devuelve null. 
    */
    public Partida leerPartida(){
        try {
            return (Partida) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(FicheroPartidaIn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FicheroPartidaIn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    

}
