/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EncryptionClient;

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
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jesus
 */
public class EncryptionUtil {

    private PasswordField pass = new PasswordField();
    private Text text = new Text("Please type password for encryption");
    private static byte[] imageBytes = null;

     static void loadImage(ImageView im) throws Exception {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Image files", "*.JPG", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG);
        File file = fileChooser.showOpenDialog(null);

        BufferedImage bufferedImage = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        im.setImage(image);

        imageBytes = IOUtilities.getImageRawBytes(file.getAbsolutePath());
        //printBytes(imageBytes, "Image bytes");
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

    public byte[] returnSHA256fromPasswordBytesAndTrimIt(PasswordField password) {
        byte[] trimmedHash = null;
        try {
            byte[] PasswordBytes = password.toString().getBytes();
            printBytes(PasswordBytes, "password bytes");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(PasswordBytes);

            for (int i = 0; i < 16 + 1; i++) {
                trimmedHash = Arrays.copyOf(hash, i);
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        printBytes(trimmedHash, "Trimmed bytes");
        return trimmedHash;
    }

    private byte[] ConvertSHA256toAES(PasswordField password) throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] finalHash = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            printBytes(returnSHA256fromPasswordBytesAndTrimIt(password), "Trimmed key bytes");
            SecretKeySpec key = new SecretKeySpec(returnSHA256fromPasswordBytesAndTrimIt(password), "AES");
            System.out.println("Key2:" +key);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            finalHash = cipher.doFinal(imageBytes);
            //printBytes(finalHash, "final hash");
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalHash;
    }

    void encryptFinal() {
        try {
            IOUtilities.writeImageRawBytes(ConvertSHA256toAES(pass),"Encrypted.jpg");
          //  saveToTXT(ConvertSHA256toAES(pass));
            System.out.println("Encrypted!");
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

    private void saveToTXT(byte[] hash) throws IOException {
        PrintWriter print = new PrintWriter("hash.txt");
        for (byte i = 0; i < hash.length; i++) {
            print. format("%02x",i);    }
    }
}
