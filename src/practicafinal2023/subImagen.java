/*
 * Esta clase define el objeto de subImagen como un JLabel, con un atributo de 
 * tipo Image, y dos atributos int que indican su “posición correcta” y su “posición actual”.
 */
package practicafinal2023;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/**
 *
 */
public class subImagen extends JLabel {

    int posicionCorrecta; //indica la posición correcta de la subImagen en la imagen ganadora
    int posicionActual; //indica la posición en que se encuentra
    private Image imagen;

    public subImagen(Image subImg, int posicionCorrecta) {
        this.posicionCorrecta = posicionCorrecta;
        imagen = subImg;
    }

    public subImagen() {

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(imagen, 0, 0, this);
    }

    public Image getImagen() {
        return imagen;
    }

    public void setPosicionActual(int posicionActual) {
        this.posicionActual = posicionActual;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public int getPosicionCorrecta() {
        return posicionCorrecta;
    }

    public boolean subImagenBienColocada() {
        return (posicionActual == posicionCorrecta);
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

}
