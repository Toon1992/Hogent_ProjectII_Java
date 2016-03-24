/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import exceptions.EmailException;
import exceptions.WachtwoordException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javax.persistence.NoResultException;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class LoginSchermController extends GridPane
{

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnAnnuleer;
    private DomeinController dc;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblWachtwoord;
    @FXML
    private TextField txfEmail;
    @FXML
    private PasswordField txfWachtwoord;
    @FXML
    private Label lblLogin;

    public LoginSchermController(DomeinController dc)
    {
        LoaderSchermen.getInstance().setLocation("LoginScherm.fxml", this);
        this.dc = dc;
    }

    @FXML
    private void login(ActionEvent event) throws Exception
    {
        lblEmail.setText("");
        lblLogin.setText("");
        lblWachtwoord.setText("");
        String email = txfEmail.getText();
        String wachtwoord = txfWachtwoord.getText();
        Boolean success = false;
        try
        {
            dc.login(email, wachtwoord);
            MateriaalSchermController msc = new MateriaalSchermController(dc);
            dc.addObserver(msc);
            LoaderSchermen.getInstance().load("start", new StartSchermController(dc), 1024, 600, this);
        } catch (EmailException e) {
            lblEmail.setText(e.getLocalizedMessage());
        } catch (WachtwoordException e)
        {
            lblWachtwoord.setText(e.getLocalizedMessage());
        } catch (NoResultException e)
        {
            lblLogin.setText("Email of wachtwoord ongelidg");
        }
    }

    @FXML
    private void annuleer(ActionEvent event)
    {
        Alert boodschap = new Alert(Alert.AlertType.CONFIRMATION);
        boodschap.setTitle("Annuleer");
        boodschap.setHeaderText("Weet u zeker dat het programma mag afgesloten worden?");

        ButtonType Annuleer = new ButtonType("Annuleer");
        ButtonType Ok = new ButtonType("Ok");
        boodschap.getButtonTypes().setAll(Annuleer, Ok);
        Optional<ButtonType> result = boodschap.showAndWait();

        if (result.get() == Ok)
        {
            System.exit(0);
        }
    }

}
