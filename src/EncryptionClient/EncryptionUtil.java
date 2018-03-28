/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EncryptionClient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import Utilities.IOUtilities;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jesus
 */
public class EncryptionUtil {

    byte[] ImageBytes = null;
    PasswordField pass = new PasswordField();
    Text text = new Text("Please type password for encryption");

    public void loadImage(ImageView im) {
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
            cipher.init(Cipher.ENCRYPT_MODE, key);
            finalHash = cipher.doFinal(ImageBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return finalHash;
    }

    public void encryptFinal() {
        try {
            IOUtilities.writeImageRawBytes(ConvertSHA256toAES(pass), "Encrypted.jpg");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
