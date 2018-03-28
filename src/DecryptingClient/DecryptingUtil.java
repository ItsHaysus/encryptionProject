package DecryptingClient;

import EncryptionClient.EncryptionMain;
import EncryptionClient.EncryptionUtil;
import Utilities.IOUtilities;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecryptingUtil {
    byte[] EncryptedImageBytes = null;
    Text text = new Text("Enter password");
    PasswordField pass = new PasswordField();

    public void loadEncryptedImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image files", "*.JPG", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG);
            File file = fileChooser.showOpenDialog(null);
            EncryptedImageBytes = IOUtilities.getImageRawBytes(file.getAbsolutePath());
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

    public byte[] returnSHA256fromPasswordBytesAndTrimIt(PasswordField password) {
        byte[] trimmedHash = null;
        try {
            byte[] PasswordBytes = password.toString().getBytes();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(PasswordBytes);

            for (int i = 0; i < 16+1; i++) {
                trimmedHash = Arrays.copyOf(hash, i);
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trimmedHash;
    }
    public byte[] ConvertSHA256toAES(PasswordField password) throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] finalHash = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(returnSHA256fromPasswordBytesAndTrimIt(password), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            finalHash = cipher.doFinal(EncryptedImageBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return finalHash;
    }
    public void DecryptFinal() {
        try {
            IOUtilities.writeImageRawBytes(ConvertSHA256toAES(pass), "Decrypted.jpg");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
