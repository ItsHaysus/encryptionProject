package DecryptingClient;

import EncryptionClient.EncryptionUtil;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DecryptingMain extends Application {

    BorderPane border = new BorderPane();
    GridPane root = new GridPane();
    DecryptingUtil decrypt = new DecryptingUtil();
    Button LoadEncryptedImage = new Button("Load Encrypted Image");
    Button showDecryptionOptions = new Button("Image");
    Button BeginDecryption = new Button("Encrypt");
    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageView im = new ImageView();
        border.setTop(root);
        border.setCenter(im);
        root.setVgap(5);
        root.setHgap(5);
        BorderPane.setAlignment(im, Pos.CENTER);

        root.add(LoadEncryptedImage, 0, 0);
        root.add(showDecryptionOptions, 1, 0);

        LoadEncryptedImage.setOnAction((event) -> {
            decrypt.loadEncryptedImage();
        });

        showDecryptionOptions.setOnAction((event) -> {
        });

        BeginDecryption.setOnAction((event) -> {
        });

        Scene scene = new Scene(border, 300, 250);

        primaryStage.setTitle("Decryption Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    }

