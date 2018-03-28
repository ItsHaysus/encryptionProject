package DecryptingClient;

import EncryptionClient.EncryptionUtil;
import Utilities.IOUtilities;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecryptingUtil {
    private Text text = new Text("Enter password");
    private PasswordField pass = new PasswordField();
    private byte[] EncryptedImageBytes = null;

    void loadEncryptedImage() throws Exception {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image files", "*.JPG", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG);
        File file = fileChooser.showOpenDialog(null);
        EncryptedImageBytes = IOUtilities.getImageRawBytes(file.getAbsolutePath());
    }

    void showEncryptButtons(Button but, BorderPane bor) {

        GridPane grid2 = new GridPane();
        grid2.setVgap(5);
        grid2.setHgap(5);

        grid2.add(text, 0, 0);
        grid2.add(pass, 0, 1);
        grid2.add(but, 1, 1);
        bor.setBottom(grid2);
    }

    private byte[] returnSHA256fromPasswordBytesAndTrimIt(PasswordField password) {
        byte[] trimmedHash = null;
        try {
            byte[] PasswordBytes = password.toString().getBytes();
            printBytes(PasswordBytes,"password bytes");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(PasswordBytes);

            for (int i = 0; i < 16 + 1; i++) {
                trimmedHash = Arrays.copyOf(hash, i);
            }
            printBytes(trimmedHash, "Trimmed bytes");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trimmedHash;
    }

    private byte[] ConvertSHA256toAES(PasswordField password) throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] finalHash = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            printBytes(returnSHA256fromPasswordBytesAndTrimIt(password), "Trimmed key bytes");
            SecretKeySpec key = new SecretKeySpec(returnSHA256fromPasswordBytesAndTrimIt(password), "AES");
            System.out.println("Your Key"+key);
            cipher.init(Cipher.DECRYPT_MODE, key);
            finalHash = cipher.doFinal(EncryptedImageBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalHash;
    }

    void DecryptFinal() {
        try {
            IOUtilities.writeImageRawBytes(ConvertSHA256toAES(pass), "Decrypted.jpg");
        } catch (Exception ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void printBytes(byte[] b, String s){
        System.out.println(s+": ");
        for (byte i = 0; i < b.length; i++) {
            System.out.format("%02x",i);
        }
        System.out.println();
    }
}
