package com.example.demo.UI;

import com.example.demo.src.Key;
import com.example.demo.src.TripleDES;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class AppWindowContoller {


    @FXML
    public TextArea txtJawnyArea;
    public TextArea szyfrogramArea;
    public TextField key1txtField;
    public TextField key2txtField;
    public TextField key3txtField;
    public Button szyfrujButton;
    public Button deszyfrujButton;
    public Button otworzJawnyButton;
    public TextField otworzJawnyTxtField;
    public TextField otworzSzyfrogramTxtField;
    public Button otworzSzyfrogramButton;
    public Button zapiszJawnyButton;
    public Button zapiszSzyfrogramButton;
    public TextField zapiszJawnyTxtField;
    public TextField zapiszSzyfrogramTxtField;
    public Button generujKluczeButton;
    public ComboBox choiceComboBox;
    //---------------------------------> fxml

    private String[] _keys = new String[3];

    private TripleDES des;

    private static String _valuesToGenerate = "0123456789abcdef";

    private byte[] txtAreaBytes;
    private byte[] cypherAreaBytes;

    private byte[] _encryptBuff;
    private byte[] _decryptBuff;


    public void GetKeysStringValue() //We are getting strings to an array, to quickly initialize DES
    {
        _keys[0] = key1txtField.getText();
        _keys[1] = key2txtField.getText();
        _keys[2] = key3txtField.getText();
   }

    public void GenerateKeys()
    {
        _keys[0] = GenerateKey();
        _keys[1] = GenerateKey();
        _keys[2] = GenerateKey();

        key1txtField.setText(_keys[0]);
        key2txtField.setText(_keys[1]);
        key3txtField.setText(_keys[2]);

        des = new TripleDES(_keys[0],_keys[1],_keys[2]);
    }


    public void Decrypt() {

        if(des == null){
            GetKeysStringValue();

            if(!Key.ValidateKey(_keys[0]) || !Key.ValidateKey(_keys[1]) || !Key.ValidateKey(_keys[2]))
                return;

            des = new TripleDES(_keys[0],_keys[1],_keys[2]);
        }

        if(_encryptBuff!= null  && !Objects.equals(szyfrogramArea.getText(), bytesToHex(_encryptBuff)))
            _encryptBuff = null;


        String choiceList = (String) choiceComboBox.getValue();
        if(Objects.equals(choiceList, "Okno")){
            txtAreaBytes = des.decrypt3DES(_encryptBuff == null ? szyfrogramArea.getText().getBytes() : _encryptBuff);
            txtJawnyArea.setText(bytesToChars(txtAreaBytes));
        } else if(Objects.equals(choiceList, "Plik")) {
            try{
                Path path = Paths.get(otworzSzyfrogramTxtField.getText());
                byte[] arr = Files.readAllBytes(path);
                szyfrogramArea.setText(bytesToChars(arr));
                txtAreaBytes = des.decrypt3DES(arr);
                txtJawnyArea.setText(bytesToChars(txtAreaBytes));

            } catch (IOException o) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("No chosen file.");
                alert.showAndWait();
            }
        }


    }

    public void Encrypt() {
        String choiceList = (String) choiceComboBox.getValue();
        String output = "";

        if(des == null){
            GetKeysStringValue();

            if(!Key.ValidateKey(_keys[0]) || !Key.ValidateKey(_keys[1]) || !Key.ValidateKey(_keys[2]))
                return;

            des = new TripleDES(_keys[0],_keys[1],_keys[2]);
        }


        if(Objects.equals(choiceList, "Okno")){
            GetKeysStringValue();
            byte[] outputB = des.encrypt3DES(txtJawnyArea.getText().getBytes());
            _encryptBuff = outputB.clone();
            output = bytesToHex(outputB);

        } else if(Objects.equals(choiceList, "Plik")) {
            try{
                Path path = Paths.get(otworzJawnyTxtField.getText());
                byte[] arr = Files.readAllBytes(path);
                byte[] outputB = des.encrypt3DES(arr);
                _encryptBuff = outputB.clone();
                output = bytesToHex(outputB);
                txtJawnyArea.setText(bytesToChars(arr));

            } catch (IOException o) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("No chosen file.");
                alert.showAndWait();
            }
        }

        szyfrogramArea.setText(output);
    }

    private String GenerateKey()
    {
        String key = new String();

        Random r = new Random();

        for(int i = 0; i < 16; i++)
        {
            key += _valuesToGenerate.toCharArray()[r.nextInt(15)];
        }
        return key;
    }

    public void chooseFileTxt() {
        choiceComboBox.getSelectionModel().select(0);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getClassLoader().getResource("AppWindow.fxml"));

        Stage scene = new Stage();

        FileChooser fil_chooser = new FileChooser();
        File file = fil_chooser.showOpenDialog(scene);

        if (file != null) {

            otworzJawnyTxtField.setText(file.getAbsolutePath());

        }
    }

    public void chooseFileCypher() {
        choiceComboBox.getSelectionModel().select(0);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getClassLoader().getResource("AppWindow.fxml"));

        Stage scene = new Stage();

        FileChooser fil_chooser = new FileChooser();
        File file = fil_chooser.showOpenDialog(scene);

        if (file != null) {

            otworzSzyfrogramTxtField.setText(file.getAbsolutePath());
        }
    }

    public void SaveFileTxt() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getClassLoader().getResource("AppWindow.fxml"));

        Stage scene = new Stage();

        FileChooser fil_chooser = new FileChooser();
        File file = fil_chooser.showSaveDialog(scene);

        if (file != null && txtJawnyArea.getText() != null && txtAreaBytes != null) {

            zapiszJawnyTxtField.setText(file.getAbsolutePath());

            OutputStream out = new FileOutputStream(file.getAbsolutePath());
            out.write(txtAreaBytes);
            out.close();

        }
        if(txtAreaBytes == null && file != null){

            zapiszJawnyTxtField.setText(file.getAbsolutePath());

            saveTextToFile(txtJawnyArea.getText(), file);
        }

    }

    public void SaveFileCypher() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getClassLoader().getResource("AppWindow.fxml"));

        Stage scene = new Stage();

        FileChooser fil_chooser = new FileChooser();
        File file = fil_chooser.showSaveDialog(scene);

        if (file != null) {

            zapiszSzyfrogramTxtField.setText(file.getAbsolutePath());
            OutputStream out = new FileOutputStream(file.getAbsolutePath());
            out.write(_encryptBuff);
            out.close();
        }
    }

    private void saveTextToFile(String content, File file) throws FileNotFoundException {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
    }

    public static String BytesToString(byte[] bytes)
    {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        StringBuilder s = new StringBuilder();

        for (byte b: bytes) {
            b += 128;

            if (encoder.canEncode((char) b)) {
                s.append((char) b);
            }

        }

        return s.toString();
    }

    public static String bytesToChars(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            char c = (char) (b & 0xff);
            sb.append(c);
        }
        return sb.toString();
    }
    public static String bytesToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes){
            sb.append(_valuesToGenerate.charAt((b & 0xF0) >> 4));
            sb.append(_valuesToGenerate.charAt((b & 0x0F)));
        }
        return sb.toString();
    }

}
