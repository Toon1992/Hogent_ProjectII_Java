/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.DomeinController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class RegistreerSchermController extends GridPane {

    @FXML
    private TextField txfEmail;
    @FXML
    private TextField txfWachtwoord2;
    @FXML
    private TextField txfWachtwoord;
    @FXML
    private Button btnRegistreer;
    @FXML
    private Button btnAnnuleer;
    @FXML
    private Label lblWachtwoord;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblWachtwoord2;
    private DomeinController dc;
    @FXML
    private TextField txfNaam;
    public RegistreerSchermController(DomeinController dc){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistreerScherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.dc = dc;
        //LoaderSchermen.getInstance().setLocation("RegistreerScherm.fxml", this);
    }  

    @FXML
    private void registreer(ActionEvent event) {
        String email = txfEmail.getText();
        String wachtwoord = txfWachtwoord.getText();
        String confirmWachtwoord = txfWachtwoord2.getText();
        String naam = txfNaam.getText();
        Boolean flag = true;
        if(email.isEmpty()){
            lblEmail.setText("Email is verplicht in te vullen");
            flag = false;
        }
        if(wachtwoord.isEmpty()){
            lblEmail.setText("Wachtwoord is verplicht in te vullen");
            flag = false;
        }
        if(wachtwoord != confirmWachtwoord){
            lblEmail.setText("Wachtwoorden komen niet overeen");
            flag = false;
        }
        if(flag)
            dc.registreer(email, wachtwoord, naam);
    }

    @FXML
    private void annuleer(ActionEvent event) {
    }
    
}
