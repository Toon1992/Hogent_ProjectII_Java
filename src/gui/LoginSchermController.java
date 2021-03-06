/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.GebruikerController;
import exceptions.EmailException;
import exceptions.WachtwoordException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.persistence.EntityNotFoundException;

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
    private GebruikerController gc;
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
    private Label lblUitloggen;

    public LoginSchermController()
    {
        LoaderSchermen.getInstance().setLocation("LoginScherm.fxml", this);
        this.gc = ControllerSingelton.getGebruikerControllerInstance();
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
            gc.login(email, wachtwoord);
            BorderPane bp = (BorderPane) this.getParent();
            GridPane gp = (GridPane) bp.getTop();
            lblUitloggen = (Label) gp.getChildren().get(gp.getChildren().size() -1 );
            lblUitloggen.setText("Afmelden");
            LoaderSchermen.getInstance().setLoggedIn(true);
            LoaderSchermen.getInstance().setWidthAndHeight(bp);
            LoaderSchermen.getInstance().setOverzichtScherm(bp,  new StartSchermController());



            //aLoaderSchermen.getInstance().load("start", new StartSchermController(gc), 1300, 600, this);
        } catch (EmailException e) {
            lblEmail.setText(e.getLocalizedMessage());
        } catch (WachtwoordException e)
        {
            lblWachtwoord.setText(e.getLocalizedMessage());
        } catch (EntityNotFoundException e)
        {
            lblLogin.setText("Email of wachtwoord ongeldig");
        }
    }

    @FXML
    private void annuleer(ActionEvent event)
    {
        boolean ok = LoaderSchermen.getInstance().popupMessageTwoButtons("Afsluiten", "Weet u zeker dat het programma mag afgesloten worden?", "Ok","Annuleer");
        if(ok){
            System.exit(0);
        }
    }

}
