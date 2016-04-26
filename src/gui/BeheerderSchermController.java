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
    private Beheerder loginBeheerder;

    public BeheerderSchermController()
    {
        LoaderSchermen.getInstance().setLocation("BeheerderScherm.fxml", this);

        controller = ControllerSingelton.getBeheerderControllerInstance();
        beheerderNamenList = controller.getBeheerders();
        loginBeheerder = controller.GetLoggedInBeheerder();

        txfEmail.setDisable(true);
        txfNaam.setDisable(true);
        txfPaswoord.setDisable(true);

        vulListViewIn();

        if (isHoofdBeheerder())
        {
            selectListViewListener();
        } else
        {
            btnNieuw.setVisible(false);
            btnWijzig.setVisible(false);
            btnVerwijderen.setVisible(false);
        }
    }

    protected void vulListViewIn()
    {
        beheerdernamen = FXCollections.observableArrayList(beheerderNamenList.stream().filter(beheerder -> !(beheerder.getNaam().equals("admin"))).map(b -> b.getNaam() + " (" + b.getEmail() + ")").collect(Collectors.toList()));
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
                        txfEmail.setDisable(false);
                        txfNaam.setDisable(false);
                        txfPaswoord.setDisable(false);
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
//        BorderPane bp = (BorderPane) this.getParent();
//        bp.setCenter(new NieuwBeheerderSchermController(this, controller));

        Stage stage = new Stage();
        Scene scene = new Scene(new NieuwBeheerderSchermController(this));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void WijzigBeheerder(ActionEvent event)
    {
        if (currentBeheerder == null)
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen beheerder geselecteerd", "OK");
            return;
        }

        if (txfNaam.getText() != null && txfEmail.getText() != null && txfPaswoord != null)
        {
            String email = txfEmail.getText();

            if (!validateEmail(email))
            {
                LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Email adres is incorrect", "OK");
                return;
            }

            if (!(email.equals(currentBeheerder.getEmail())))
            {
                if (komtEmailVoor(email))
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Email adres bestaat al", "OK");
                    return;
                }
            }

            currentBeheerder.setEmail(email);
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
