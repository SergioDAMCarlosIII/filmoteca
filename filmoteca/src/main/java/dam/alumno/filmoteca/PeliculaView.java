package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PeliculaView{

    @FXML
    public Text textoTitulo;
    @FXML
    public TextField fieldTitulo;
    @FXML
    public TextField fieldAno;
    @FXML
    public TextArea fieldDescripcion;
    //@FXML
    //public TextField fieldPoster;
    @FXML
    public Slider sliderRating;
    @FXML
    public ImageView imgPoster;
    @FXML
    public TextField fieldPosterUrl;

    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas = datosFilmoteca.getListaPeliculas();
    // Añadimos este atributo para saber si estamos editando o creando
    private Pelicula peliculaEditando;

   /* @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cada vez que se modifique el contenido del TextField, se actualiza la imagen en el ImageView.
        fieldPoster.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Image image = new Image(newValue, true);
                imgPoster.setImage(image);
            } catch (Exception e) {
                // En caso de error se limpia la imagen
                imgPoster.setImage(null);
            }
        });
    }*/

    public void handlerAceptar(ActionEvent actionEvent) {
        // Si peliculaEditando es null, significa que el usuario ha pulsado "Nuevo" y no estamos editando
        if (peliculaEditando == null) {
            // Creamos una nueva pelicula
            int nuevoId = listaPeliculas.stream()
                    .mapToInt(Pelicula::getId)
                    .max()
                    .orElse(0) + 1;

            Pelicula pelicula = new Pelicula();
            pelicula.setId(nuevoId);
            pelicula.setTitle(fieldTitulo.getText());
            pelicula.setYear(Integer.parseInt(fieldAno.getText()));
            pelicula.setDescription(fieldDescripcion.getText());

            // Rendondeo el valor del Slider a un solo decimal
            float ratingRedondeado = (float) Math.round(sliderRating.getValue() * 10) / 10;
            pelicula.setRating(ratingRedondeado);
            // Guardamos la URL esscrita en el TextField
            pelicula.setPoster(fieldPosterUrl.getText());

            // Añadimos a la lista
            listaPeliculas.add(pelicula);
        } else {
            // Si NO es null, actualizamos la que estamos editando
            peliculaEditando.setTitle(fieldTitulo.getText());
            peliculaEditando.setYear(Integer.parseInt(fieldAno.getText()));
            peliculaEditando.setDescription(fieldDescripcion.getText());

            float ratingRedondeado = (float) Math.round(sliderRating.getValue() * 10) / 10;
            peliculaEditando.setRating(ratingRedondeado);
            // Actualizamos la URL
            peliculaEditando.setPoster(fieldPosterUrl.getText());

            // No hace falta añadir a la lista, porque la película ya está en la lista
            // (Al ser un objeto que ya se encontraba allí, se modificará en la tabla)
        }

        // Cerramos la ventana
        Stage stage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
        stage.close();

    }

    public void handlerEliminar(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
        stage.close();
    }


    public void setPelicula(Pelicula pelicula) {
        this.peliculaEditando = pelicula; //guardamos la referencia a la pelicula que estamos editando
        if (pelicula != null) {
            textoTitulo.setText("Editar pelicula");
            fieldTitulo.setText(pelicula.getTitle());
            fieldAno.setText(pelicula.getYear() + "");
            fieldDescripcion.setText(pelicula.getDescription());
            sliderRating.setValue(pelicula.getRating());
            // Rellenar con la URL que ya exista
            fieldPosterUrl.setText(pelicula.getPoster());
            // Actualizamos la imagen del ImageView al cargar la película
            try {
                if(pelicula.getPoster() != null && !pelicula.getPoster().isBlank()) {
                    imgPoster.setImage(new Image(pelicula.getPoster(), true));
                } else {
                    imgPoster.setImage(null);
                }
            } catch (Exception e) {
                imgPoster.setImage(null);
            }
        }else {
            // Podriamos dejarlo en blanco para cuando sea nuevo
            textoTitulo.setText("Crea la Pelicula");
            fieldTitulo.setText("");
            fieldAno.setText("");
            fieldDescripcion.setText("");
            sliderRating.setValue(0);
            //fieldPoster.setText("");
        }
    }
}
