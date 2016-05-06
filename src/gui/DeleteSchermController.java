/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.GebiedenController;
import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import repository.FirmaRepository;
import repository.GebiedenRepository;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class DeleteSchermController extends GridPane
{

    @FXML
    private Label lblHeader;
    @FXML
    private ListView<String> lstvDeleteItems;

    private List<String> items;

    private Doelgroep doelgroep;
    private Leergebied leergebied;
    private Firma firma;
    private char deleteCode;
    private GebiedenController gebiedencontroller;
    private FirmaRepository firmaRepo;

    public DeleteSchermController(List<String> items, String lblName, char deleteCode)
    {
        LoaderSchermen.getInstance().setLocation("DeleteScherm.fxml", this);
        this.items = items;
        this.deleteCode = deleteCode;
        lblHeader.setText(lblName);
        gebiedencontroller = ControllerSingelton.getGebiedenControllerInstance();
        firmaRepo = new FirmaRepository();

        vulListView(items);
        selectListViewListener();
    }

    private void vulListView(List<String> items)
    {
        lstvDeleteItems.setItems(FXCollections.observableArrayList(items));
    }

    private void vulCurrentObjectIn(String value)
    {
        switch (deleteCode)
        {
            case 'd':               
                doelgroep = (Doelgroep) gebiedencontroller.geefGebied(new Doelgroep("d"), value);
                break;
            case 'l':
                ObservableList<Leergebied> leergebieden = gebiedencontroller.geefAlleGebieden(new Leergebied("l"));
                leergebied = leergebieden.stream().filter(d -> d.getNaam().equals(value)).findFirst().get();
                break;
            case 'f':
                List<String> firmas = firmaRepo.geefAlleFirmas();
                String firmaNaam = firmas.stream().filter(f -> f.equals(value)).findFirst().get();
                firma = firmaRepo.geefFirma(firmaNaam);
                break;
        }
    }

    private void selectListViewListener()
    {
        lstvDeleteItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue != null)
                {
                    if (oldValue == null || !oldValue.equals(newValue))
                    {
                        vulCurrentObjectIn(newValue);
                    }
                }
            }
        });
    }

    @FXML
    private void terug(ActionEvent event)
    {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void verwijderen(ActionEvent event)
    {
        boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("warning", "Bent u zeker?", "ja", "nee");
        
        if(isOk)
        {
            verwijder();
        }
    }

    private void verwijder()
    {
        switch (deleteCode)
        {
            case 'd':
                if (doelgroep == null)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen doelgroep geselecteerd", "ok");
                    break;
                }
                gebiedencontroller.deleteGebied(doelgroep);
                break;
            case 'l':
                if (leergebied == null)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen leergebied geselecteerd", "ok");
                    break;
                }
                gebiedencontroller.deleteGebied(leergebied);
                break;
            case 'f':
                if (firma == null)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen firma geselecteerd", "ok");
                    break;
                }
                break;
        }
    }

}
