/*
* Clase que utilizaremos para crear las subimágenes sobre las que trabajará el 
* juego. Se define como un JPanel cuyas divisiones irán directamente relacionadas 
* con los datos que el usuario haya introducido por parámetro.
*
 */
package practicafinal2023;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 */
public class PanelSubImagenes extends JPanel {

    private Image imagenGanadora;
    private subImagen subImagenes[];              //Array de subImagenes ordenadas
    private subImagen subImagenesDesordenadas[];  //Array de subImagenes desordenadas
    private int subImagenesCogidas[];
    private int filas;
    private int columnas;
    private int numSubImagenes;
    private MouseListener gestEventos;

    private int anchura, altura;

    private int dim_x = 1200;
    private int dim_y = 745;

    private Random rnd = new Random();

    public PanelSubImagenes() {

    }

    //Constructor del panel de SubImagenes con los datos de filas y columnas pasados por parámetro
    public PanelSubImagenes(int filas, int columnas, MouseListener gestEventos, Image imagenGanadora) throws IOException {
        this.gestEventos = gestEventos;
        this.filas = filas;
        this.columnas = columnas;

        calcularNumSubImagenes(); //Se calculan el número de subImagenes

        subImagenes = new subImagen[numSubImagenes];//Se crea el array de subImagenes
        subImagenesDesordenadas = new subImagen[numSubImagenes]; //Se crea un array donde se almacenaran las imagenes desordenadas

        //Se llama al método que se encarga de dividir la imagenGanadora en tantas filas y columnas
        // como las pasadas por parámetro
        cargarImagen(imagenGanadora);

        //Método que desordena las subImagenes y inicializa el array de subImagenesDesordenadas
        desordenarSubImagenes();
        while (imagenesPosicionGanadora()) {//Mientras salgan las imagenes en posición ganadora, se
            desordenarSubImagenes();//deberán de desordenar 
        }

        //Finalmente se crea el layout
        setLayout(new GridLayout(filas, columnas, 2, 2));
        setPreferredSize(new Dimension(anchura, altura));

        for (int i = 0; i < numSubImagenes; i++) {
            add(subImagenesDesordenadas[i]);
        }
    }


    /*
    * Método que actualiza el panel de subImagenes con las imagenes Desordenadas
     */
    public void actualizarPanelSubImagenes() {
        setLayout(new GridLayout(filas, columnas, 2, 2));
        for (int i = 0; i < numSubImagenes; i++) {
            add(subImagenesDesordenadas[i]);
        }
    }

    public subImagen subImagenGanadora() {
        subImagen IGanadora = new subImagen(imagenGanadora, -1);
        return IGanadora;
    }

    //Método que resetea el array que indica las subImagenes cogidas para desordenar
    public void crearArraySubImagenesCogidas(int valorMax) {
        subImagenesCogidas = new int[valorMax];
        for (int i = 0; i < valorMax; i++) {
            subImagenesCogidas[i] = 0;
        }
    }

    //Método que devuelve la posición correcta de la subImagen seleccionada
    public int getSubImagenSeleccionada(MouseEvent evento) {
        subImagen aux = (subImagen) evento.getSource();
        return aux.getPosicionCorrecta();
    }

    public void moverSubImagenes(subImagen im1, subImagen im2) {
        int posActual1 = im1.getPosicionActual();
        int posActual2 = im2.getPosicionActual();

        subImagenesDesordenadas[posActual1] = im2; //Colocamos en la posición de la subImagen 1, la subImagen 2
        im2.setPosicionActual(posActual1);
        subImagenesDesordenadas[posActual2] = im1; //Colocamos en la posición de la subImagen 2, la subImagen 1
        im1.setPosicionActual(posActual2);

    }

    public boolean imagenesPosicionGanadora() {
        for (int i = 0; i < numSubImagenes; i++) {
            if (!subImagenesDesordenadas[i].subImagenBienColocada()) { //Si hay alguna subImagen que NO esté bien colocada
                return false;                                        //entonces no ha acabado el juego
            }
        }
        return true; //En cambio si todas estan bien colocadas, el juego termina
    }

    public subImagen[] getSubImagenes() {
        return subImagenes;
    }

    public Image getImagenGanadora() {
        return imagenGanadora;
    }

    public subImagen[] getSubImagenesDesordenadas() {
        return subImagenesDesordenadas;
    }

    public void printImagenes() {
        for (int i = 0; i < subImagenes.length; i++) {
            System.out.println(subImagenes[i].getPosicionActual());
        }
    }

    public void calcularNumSubImagenes() {
        numSubImagenes = filas * columnas;

    }

    public void recortarImagenes() {

    }

    public int getAltura() {
        return altura;
    }

    public int getAnchura() {
        return anchura;
    }


    public void desordenarSubImagenes() {
        crearArraySubImagenesCogidas(numSubImagenes); //Se crea el array que indicará las subImagenes cogidas, esto sirve para desordenar ls imagenes
        //For que asigna las nuevas posiciones de las subImagenes
        int valorRand = 0;
        for (int i = 0; i < numSubImagenes; i++) {
            valorRand = numeroAleatorio(numSubImagenes); //se genera un valor aleatorio y se 
            //asigna a una posicion del int array sequenciaPartida
            while (subImagenesCogidas[valorRand] == 1) {//Si esa imagen ya ha sido cogida, se genera otro numero al azar
                valorRand = numeroAleatorio(numSubImagenes);
            }
            subImagenesCogidas[valorRand] = 1; //Indicamos que hemos cogido la subImagen de esa posición
            subImagenesDesordenadas[i] = subImagenes[valorRand]; //Se coloca la subImagen de la posicion "random" en la 
            subImagenes[valorRand].setPosicionActual(i); //Actualizamos la posición en la que se encuentra la subImagen
            //primera posición de las subImagenes desordenadas
            //System.out.println("He colocado la imagen " + valorRand + " en la posicion " + i);
        }
    }

    /*
    * Genera un número aleatorio en un rango determinado, entre 0
    * y el valorMax - 1 siendo valorMax el número de subImagenes
     */
    public int numeroAleatorio(int valorMax) {
        return rnd.nextInt(valorMax);
    }

    /*
    * Método que se encarga de dividir la imagen ganadora pasada por parámetro en tantas filas y 
    * columnas como le haya indicado el usuario.
    * Estas subImágenes se almacenan en el array ordenado de subImagenes
     */
    public void cargarImagen(Image imagenGanadora) {
        ImageIcon imagenTemporal;
        int posicion = 0;

        //imagenTemporal = redimensionarImagen(new ImageIcon(imagenGanadora), 600, 700); //Redimensionamos la imagen para que todas las imagenes tengan el mismo tamaño
        //Obtenemos la altura y la anchura de la imagen Ganadora después de redimensionarla a la ventana
        Dimension dimImagenG = obtenerDimensionProporcional(new Dimension(imagenGanadora.getWidth(this), imagenGanadora.getHeight(this)));

        //Asignamos los valores de la dimensión obtenida a las variables:
        anchura = dimImagenG.width;
        altura = dimImagenG.height;

        //A continuación tenemos que redimensionar la imagenGanadora 
        imagenTemporal = redimensionarImagen(new ImageIcon(imagenGanadora), anchura, altura);
        imagenGanadora = imagenTemporal.getImage();

        //Se redimensiona la imagen con el nuevo tamaño
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Image imagen = createImage(new FilteredImageSource(imagenGanadora.getSource(),
                        new CropImageFilter(j * (anchura / columnas), i * (altura / filas), anchura / columnas, altura / filas)));
                subImagen subImagenAux = new subImagen(imagen, posicion);
                subImagenes[posicion] = subImagenAux;
                subImagenes[posicion].addMouseListener(gestEventos);
                posicion++;

            }
        }

    }

    /*
     * Redimensionamiento de una imagen en base a las dimensiones de unaetiqueta
     */
    public ImageIcon redimensionarImagen(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage(); // cogemos la imagen de imageIcon
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, w, h, null);
        g2.dispose();

        imageIcon = new ImageIcon(resizedImg);
        return imageIcon;
    }
    
    /*
    * Método que a partir de una dimensión pasada por parámetro devuelve una 
    * nueva dimensión que se ajusta al tamaño del JFrame y que no deforma la imagen.
    */

    public Dimension obtenerDimensionProporcional(Dimension imgTam) {
        int x_original = imgTam.width;
        int y_original = imgTam.height;
        int x_nueva = x_original;
        int y_nueva = y_original;

        if (x_original > y_original) {//Si la anchura es mayor que la altura
            //Hay que redimensionar la anchura
            x_nueva = dim_x;
            //Y la altura en proporcion de la anchura
            y_nueva = (x_nueva * y_original) / x_original;
        }
        if ((y_original > x_original) || (y_nueva > dim_y)) {//Si la altura es mayor que la anchura
           //Hay que redimensionar la altura
            y_nueva = dim_y;
            //Y la anchura en proporcion de la altura
            x_nueva = (y_nueva * x_original) / y_original;
        }
        return new Dimension(x_nueva, y_nueva);
    }
}
