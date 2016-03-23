/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    private TextField txfWachtwoord2;
    @FXML
    private TextField txfWachtwoord;
    @FXML
    private Button btnRegistreer;
    @FXML
    private Button btnAnnuleer;
    private DomeinController dc;
    @FXML
    private TextField txfNaam;
    @FXML
    private PasswordField txfWachtwoord1;
    @FXML
    private Label lblNaam;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblWachtwoord;
    @FXML
    private Label lblWachtwoord1;
    public RegistreerSchermController(DomeinController dc){
        LoaderSchermen.getInstance().setLocation("RegistreerScherm.fxml", this);
        this.dc = dc;
        
    }  

    @FXML
    private void registreer(ActionEvent event) {
        String email = txfEmail.getText();
        String wachtwoord = txfWachtwoord.getText();
        String confirmWachtwoord = txfWachtwoord2.getText();
        String naam = txfNaam.getText();
        Boolean geldig = true;
        if(naam.isEmpty()){
            lblNaam.setText("Naam is verplicht");
            geldig = false;
        }
        if(email.isEmpty()){
            lblEmail.setText("Email is verplicht");
            geldig = false;
        }
        if(wachtwoord.isEmpty()){
            lblWachtwoord.setText("Wachtwoord is verplicht");
            geldig = false;
        }
        if(!wachtwoord.equals(confirmWachtwoord)){
            lblWachtwoord1.setText("Wachtwoorden komen niet overeen");
            geldig = false;
        }
        if(geldig)
            dc.registreer(email, wachtwoord, naam);
    }

    @FXML
    private void annuleer(ActionEvent event) {
    }
    
}
