package DecryptingClient;

import EncryptionClient.EncryptionMain;
import EncryptionClient.EncryptionUtil;
import Utilities.IOUtilities;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecryptingUtil {
    public void loadEncryptedImage(ImageView im) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image files", "*.JPG", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG);
            File file = fileChooser.showOpenDialog(null);

            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            im.setImage(image);
            ImageBytes = IOUtilities.getImageRawBytes(file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(EncryptionMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showEncryptButtons(Button but, BorderPane bor) {

        GridPane grid2 = new GridPane();
        grid2.setVgap(5);
        grid2.setHgap(5);

        grid2.add(text, 0, 0);
        grid2.add(pass, 0, 1);
        grid2.add(but, 1, 1);
        bor.setBottom(grid2);
    }
}
