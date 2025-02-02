package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainController {


    public ImageView imagenPosterDetalle;
    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas;

    @FXML
    public HBox root;


    @FXML
    private TableColumn<Pelicula, Integer> columnaAno;

    @FXML
    private TableColumn<Pelicula, String> columnaDescripcion;

    @FXML
    private TableColumn<Pelicula, String> columnaId;

    @FXML
    private TableColumn<Pelicula, String> columnaPoster;

    @FXML
    private TableColumn<Pelicula, Number> columnaPuntuacion;

    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private Text textoId;

    @FXML
    private Text textoTitulo;

    @FXML
    private Text textoAno;

    @FXML
    private TextArea textoDescripcion;

    @FXML
    private Text textoPoster;

    @FXML
    private Text textoPuntuacion;

    @FXML
    public Button btnNuevo;

    @FXML
    public Button btnEditar;

    @FXML
    public Button btnBorrar;

    public void initialize() {

        listaPeliculas = datosFilmoteca.getListaPeliculas();



        // Vinculo las columnas con las propiedades del objeto Pelicula
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnaAno.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnaPuntuacion.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnaPoster.setCellValueFactory(new PropertyValueFactory<>("poster"));

        //Aquí añado el cellFactory para mostrar la imagen de la pelicula
        columnaPoster.setCellFactory(col -> new TableCell<Pelicula, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String posterUrl, boolean empty) {
                super.updateItem(posterUrl, empty);

                if (empty || posterUrl == null || posterUrl.isBlank()) {
                    // Si no hay URL o la celda está vacía
                    imageView.setImage(null);
                    setGraphic(null);
                } else {
                    // Ajustamos el tamaño en miniatura para el póster
                    imageView.setFitHeight(80);
                    imageView.setFitWidth(55);
                    imageView.setPreserveRatio(true);

                    try {
                        // Carga asíncrona de la imagen
                        Image imagen = new Image(posterUrl, true);
                        imageView.setImage(imagen);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        imageView.setImage(null);
                        setGraphic(null);
                    }
                }
            }
        });

        // Cargamos la lista de peliculas en la tabla
        tablaPeliculas.setItems(listaPeliculas);

        HBox.setHgrow(tablaPeliculas, Priority.ALWAYS);

        // Ajuste automático de ancho (todas las columnas se reparten el espacio)
        tablaPeliculas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Listener para mostrar los datos de la película seleccionada en los Text (lado derecho)
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textoId.setText(String.valueOf(newValue.getId()));
                textoTitulo.setText(newValue.getTitle());
                textoAno.setText(String.valueOf(newValue.getYear()));
                textoDescripcion.setText(newValue.getDescription());
                textoPuntuacion.setText(String.valueOf(newValue.getRating()));
                //textoPoster.setText(newValue.getPoster());
                // Asignamos la imagen al ImageView
                try {
                    if (newValue.getPoster() != null && !newValue.getPoster().isBlank()) {
                        Image img = new Image(newValue.getPoster(), true);
                        imagenPosterDetalle.setImage(img);
                    } else {
                        imagenPosterDetalle.setImage(null);
                    }
                } catch (Exception e) {
                    // En caso de que la URL falle, poner a null o imagen por defecto
                    imagenPosterDetalle.setImage(null);
                }
            }
        });
    }

    public void handlerEliminar(ActionEvent actionEvent){
        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();

        if (indice >= 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Atencion");
            alert.setHeaderText("Eliminar Pelicula");
            alert.setContentText("¿Desea eliminar la pelicula?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                tablaPeliculas.getItems().remove(indice);
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Debe seleccionar una pelicula");
            alerta.showAndWait();
        }
    }

    public void handlerNuevo(ActionEvent actionEvent) {
        Scene escena = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("PeliculaView.fxml"));
        try {
            escena = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Error al intentar crear un nuevo usuario");
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Nueva Pelicula");
        stage.setScene(escena);
        stage.showAndWait();
    }

    public void handlerEditar(ActionEvent actionEvent) {

        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();

        if(indice < 0){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se ha seleccionado ninguna persona");
            alerta.setContentText("Por favor seleccione una persona de la tabla para poder editarla");
            alerta.showAndWait();
        } else {
            Scene escena = null;
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("PeliculaView.fxml"));
            try {
                escena = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Error al intentar crear un nuevo usuario");
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("Edita la Pelicula");
            stage.setScene(escena);

            // Obtenemos el controlador de PeliculaView
            PeliculaView controlador = fxmlLoader.getController();
            // Pasamos la película seleccionada al controlador para su edición
            controlador.setPelicula(tablaPeliculas.getSelectionModel().getSelectedItem());

            stage.showAndWait();
        }
    }

    public void handlerSalir(ActionEvent actionEvent) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar cerrar aplicacion");
        alerta.setHeaderText("Estas seguro que quieres cerrar la aplicación sin guardar los cambios?");
        alerta.setContentText("Elige una opción para continuar");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK){
            System.exit(0);
        }
    }
}
