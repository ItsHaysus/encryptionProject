package EncryptionClient;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EncryptionMain extends Application {

    BorderPane border = new BorderPane();
    GridPane root = new GridPane();
    EncryptionUtil encript = new EncryptionUtil();
    Button LoadImage = new Button("Load Image");
    Button showEncryptionOptions = new Button("Encrypt Image");
    Button BeginEncryption = new Button("Encrypt");

    @Override
    public void start(Stage primaryStage) {

        ImageView im = new ImageView();
        border.setTop(root);
        border.setCenter(im);
        root.setVgap(5);
        root.setHgap(5);
        BorderPane.setAlignment(im, Pos.CENTER);

        root.add(LoadImage, 0, 0);
        root.add(showEncryptionOptions, 1, 0);

        LoadImage.setOnAction((event) -> {
            encript.loadImage(im);
        });

        showEncryptionOptions.setOnAction((event) -> {
            encript.showEncryptButtons(BeginEncryption, border);
        });

        BeginEncryption.setOnAction((event) -> {
            encript.encryptFinal();
        });

        Scene scene = new Scene(border, 300, 250);

        primaryStage.setTitle("Encryption Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
