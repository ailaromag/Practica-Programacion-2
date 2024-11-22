/*
* Clase extends de JProgressBar sobre la que declaramos todos aquellos 
* métodos y atributos que nos servirán para llevar a cabo la implementación de 
* un cronómetro cuya velocidad irá en función de la dificultad del puzzle seleccionado.
 */
package practicafinal2023;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JProgressBar;

/**
 *
 */
public class BarraTemporal extends JProgressBar {

    //DECLARACIÓN ATRIBUTOS
    private int valorMinimo = 0;
    private int valorMaximo = 10000;
    int ANCHO_BARRA = 40;
    int INDICE_VELOCIDAD = 100; //Indice de velocidad máximo
    int VELOCIDAD;

    //Constructor de la BarraTemporal
    public BarraTemporal(ActionListener gestorEvento, int filas, int columnas) {
        //Asignamos la velocidad con la siguiente fórmula dependiente de las filas y columnas
        VELOCIDAD = INDICE_VELOCIDAD - (filas*columnas + (int)Math.pow(1.05, Math.sqrt(filas*columnas)) -2);
        
        //Asignamos los valores MÁXIMO y MÍNIMO
        setMinimum(valorMinimo);
        setMaximum(valorMaximo);

        //Asignamos el valor INICIAL
        setValue(0);
        //Activamos la visualizacion del valor de la progresión en la barraTemporal
        setStringPainted(true);
        //Dimensionamos la barra
        setPreferredSize(new Dimension(500, ANCHO_BARRA));
        //Asignamos los COLORES de FONDO y TRAZADO de la barra
        setForeground(Color.RED);
        setBackground(Color.YELLOW);

    }

    public int getVELOCIDAD() {
        return VELOCIDAD;
    }

    public int getValorBarraTemporal() {
        return getValue();
    }

    public int getValorMaximo() {
        return valorMaximo;
    }

    public void setValorBarraTemporal(int valor) {
        setValue(valor);
    }
}
