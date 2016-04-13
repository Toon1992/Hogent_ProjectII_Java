/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.BeheerderController;
import domein.Beheerder;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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
    private Button btnWijzig;
    @FXML
    private Button btnVerwijderen;
    @FXML
    private TextField txfNaam;
    @FXML
    private TextField txfEmail;
    @FXML
    private TextField txfPaswoord;

    private ObservableList<String> beheerdernamen;
    private List<Beheerder> beheerderNamenList;
    private Beheerder currentBeheerder;

    public BeheerderSchermController()
    {
        controller = new BeheerderController();
        beheerderNamenList = controller.getBeheerders();

        LoaderSchermen.getInstance().setLocation("BeheerderScherm.fxml", this);

        vulListViewIn();
        selectListViewListener();
    }

    protected void vulListViewIn()
    {
        beheerdernamen = FXCollections.observableArrayList(beheerderNamenList.stream().filter(beheerder->!(beheerder.getNaam().equals("admin"))).map(b -> b.getNaam() + " (" + b.getEmail() + ")").collect(Collectors.toList()));
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
                        currentBeheerder = beheerderNamenList.stream().filter(beheerder -> beheerder.getNaam().equals(filterNaam(newValue))).findFirst().get();
                        txfNaam.setText(currentBeheerder.getNaam());
                        txfEmail.setText(currentBeheerder.getEmail());
                        txfPaswoord.setText(currentBeheerder.getWachtwoord());
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
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new NieuwBeheerderSchermController(this, controller));
    }

    @FXML
    private void WijzigBeheerder(ActionEvent event)
    {
        if(currentBeheerder == null)
        {
             LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen beheerder geselecteerd", "OK");
             return;
        }
        
        if (txfNaam.getText() != null && txfEmail.getText() != null && txfPaswoord != null)
        {
            if(!validateEmail(txfEmail.getText()))
            {
                LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Email adres is incorrect", "OK");
                return;
            }
                
            currentBeheerder.setEmail(txfEmail.getText());
            currentBeheerder.setNaam(txfNaam.getText());
            currentBeheerder.setWachtwoord(txfPaswoord.getText());
            controller.wijzigBeheerder(currentBeheerder);
            vulListViewIn();
        } else
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Text velden moeten ingevuld zijn", "OK");
        }

    }

    @FXML
    private void VerwijderBeheerder(ActionEvent event)
    {
        if (currentBeheerder != null)
        {
            controller.verwijderBeheerder(currentBeheerder);

            txfNaam.clear();
            txfEmail.clear();
            txfPaswoord.clear();

            beheerderNamenList.remove(currentBeheerder);
            vulListViewIn();
            currentBeheerder = null;
        } else
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen beheerder geselecteerd", "OK");
        }
    }

    protected void voegNieuweBeheerderToe(Beheerder beheerder)
    {
        beheerderNamenList.add(beheerder);
    }

    protected boolean validateEmail(String email)
    {
        if (email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"))
        {
            return beheerderNamenList.stream().noneMatch(beheerder -> beheerder.getEmail().equals(email));
        }
        
        return false;
    }

}
