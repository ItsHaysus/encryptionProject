package EncryptionClient;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EncryptionMain extends Application {

    private BorderPane border = new BorderPane();
    private GridPane root = new GridPane();
    private EncryptionUtil encript = new EncryptionUtil();
    private Button LoadImage = new Button("Load Image");
    private Button showEncryptionOptions = new Button("Encrypt Image");
    private Button BeginEncryption = new Button("Encrypt");

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
            try {
                EncryptionUtil.loadImage(im);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        showEncryptionOptions.setOnAction((event) -> {
            encript.showEncryptButtons(BeginEncryption, border);
        });

        BeginEncryption.setOnAction((event) -> {
            encript.encryptFinal();
        });

        Scene scene = new Scene(border, 500, 900);

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
