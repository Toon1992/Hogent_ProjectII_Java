/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.BeheerderController;
import controller.ControllerSingelton;
import domein.Beheerder;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import repository.BeheerderRepository;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class BeheerderSchermController extends GridPane
{

    @FXML
    private ListView<String> lvBeheerder;

    private BeheerderController controller;
    @FXML
    private Button btnNieuw;
    @FXML
    private Button btnVerwijderen;

    @FXML
    private TextField txfEmail;

    private ObservableList<String> beheerdernamen;
    private List<Beheerder> beheerderNamenList;
    private Beheerder loginBeheerder;
    private Beheerder currentBeheerder;
    @FXML
    private Label lblEmail;

    public BeheerderSchermController()
    {
        LoaderSchermen.getInstance().setLocation("BeheerderScherm.fxml", this);

        controller = ControllerSingelton.getBeheerderControllerInstance();
        beheerderNamenList = controller.getBeheerders();
        loginBeheerder = controller.GetLoggedInBeheerder();

        vulListViewIn();

        if (isHoofdBeheerder())
        {
            selectListViewListener();
        } else
        {
            btnNieuw.setVisible(false);
            btnVerwijderen.setVisible(false);
            txfEmail.setVisible(false);
            lblEmail.setVisible(false);
        }
    }

    protected void vulListViewIn()
    {
        beheerdernamen = FXCollections.observableArrayList(beheerderNamenList.stream().map(Beheerder::getEmail).collect(Collectors.toList()));
        lvBeheerder.setItems(beheerdernamen);
    }

    private void selectListViewListener()
    {
        lvBeheerder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue != null)
                {
                    if (oldValue == null || !oldValue.equals(newValue))
                    {
                        currentBeheerder = beheerderNamenList.stream().filter(beheerder -> beheerder.getEmail().equals(newValue)).findFirst().get();
                    }
                }
            }
        });
    }

    private String filterNaam(String value)
    {
        int index = value.indexOf("(");
        return value.substring(0, index).trim();
    }

    @FXML
    private void nieuwBeheerder(ActionEvent event)
    {    
        String tekst = txfEmail.getText();

        if (tekst == null || tekst.isEmpty())
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Waarschuwing", "Email moet ingevuld zijn", "OK");
        } else if (komtEmailVoor(tekst))
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Waarschuwing", "Email staat al bij de beheerders", "OK");
        } else if (!validateEmail(tekst))
        {
           LoaderSchermen.getInstance().popupMessageOneButton("Waarschuwing", "Foute Email", "OK"); 
        }
        else{

            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Confirm", "Bent u zeker dat je de gebruiker wilt toevoegen?", "ja", "nee");

            if (isOk)
            {
                voegNieuweBeheerderToe(new Beheerder(tekst, false));
                vulListViewIn();
                txfEmail.clear();
            }
        }

       

    }

    @FXML
    private void VerwijderBeheerder(ActionEvent event)
    {
        if (currentBeheerder != null)
        {
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Confirm", "Bent u zeker dat je de beheerder wilt verwijderen?", "ja", "nee");

            if (isOk)
            {
                controller.verwijderBeheerder(currentBeheerder);
                txfEmail.clear();

                beheerderNamenList.remove(currentBeheerder);
                vulListViewIn();
                currentBeheerder = null;
            }
        } else
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen beheerder geselecteerd", "OK");
        }
    }

    protected void voegNieuweBeheerderToe(Beheerder beheerder)
    {
        beheerderNamenList.add(beheerder);
        controller.voegBeheerderToe(beheerder);
        beheerdernamen.add(beheerder.getEmail());
    }

    protected boolean validateEmail(String email)
    {
        //return beheerderNamenList.stream().noneMatch(beheerder -> beheerder.getEmail().equals(email));    
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }

    protected boolean komtEmailVoor(String email)
    {
        return !beheerderNamenList.stream().noneMatch(beheerder -> beheerder.getEmail().equals(email));
    }

    private boolean isHoofdBeheerder()
    {
        return loginBeheerder.isHoofd();
    }

}
