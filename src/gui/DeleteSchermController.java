/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.FirmaController;
import controller.GebiedenController;
import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private FirmaController firmaController;
    private MateriaalNieuwSchermController mc;

    public DeleteSchermController(MateriaalNieuwSchermController mc, List<String> items, String lblName, char deleteCode)
    {
        LoaderSchermen.getInstance().setLocation("DeleteScherm.fxml", this);
        this.items = items;
        this.deleteCode = deleteCode;
        lblHeader.setText(lblName);
        gebiedencontroller = ControllerSingelton.getGebiedenControllerInstance();
        firmaController = ControllerSingelton.getFirmaControllerInstance();
        this.mc = mc;

        vulListView();
        selectListViewListener();
    }

    private void vulListView()
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
                leergebied = (Leergebied) gebiedencontroller.geefGebied(new Leergebied("l"), value);
                break;
            case 'f':
                //List<String> firmas = firmaController.geefAlleFirmas();
                String firmaNaam = items.stream().filter(f -> f.equals(value)).findFirst().get();
                firma = firmaController.geefFirma(firmaNaam);
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

        verwijder();
        mc.vulGridPaneOp();

    }

    private boolean isBevestigd()
    {
        return LoaderSchermen.getInstance().popupMessageTwoButtons("warning", "Bent u zeker?", "ja", "nee");
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
                try
                {
                    if (isBevestigd())
                    {
                        updateListAndComboBox("d");
                    }
                } catch (Exception ex)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Deze doelgroep kan niet verwijderd worden omdat het nog bij een materiaal hoort", "Ok");
                }
                break;
            case 'l':
                if (leergebied == null)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen leergebied geselecteerd", "ok");
                    break;
                }
                try
                {
                    if (isBevestigd())
                    {
                        updateListAndComboBox("l");
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Dit leergebied kan niet verwijderd worden omdat het nog bij een materiaal hoort", "Ok");
                }
                break;
            case 'f':
                if (firma == null)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Er is geen firma geselecteerd", "ok");
                    break;
                }
                try
                {
                    if (isBevestigd())
                    {
                        updateListAndComboBox("f");
                    }
                } catch (Exception e)
                {
                    LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Deze firma kan niet verwijderd worden omdat het nog bij een materiaal hoort", "Ok");
                }
                break;
        }
    }

    private void updateListAndComboBox(String type)
    {
        switch (type)
        {
            case "d":
            {
                gebiedencontroller.deleteGebied(doelgroep);
                items.remove(doelgroep.getNaam());
                vulListView();

                List<String> checkedGebieden = mc.getCheckedGebieden("d");
                int index = checkedGebieden.indexOf(doelgroep.getNaam());
                if (index > -1)
                {
                    checkedGebieden.remove(index);
                }
                mc.vulDoelgroepenLijstIn();
                mc.checkItems(checkedGebieden, "d");
                break;
            }
            case "l":
            {
                gebiedencontroller.deleteGebied(leergebied);
                items.remove(leergebied.getNaam());
                vulListView();

                List<String> checkedGebieden = mc.getCheckedGebieden("l");
                int index = checkedGebieden.indexOf(leergebied.getNaam());
                if (index > -1)
                {
                    checkedGebieden.remove(index);
                }
                mc.vulLeergebiedenLijstIn();
                mc.checkItems(checkedGebieden, "l");
                break;
            }
            case "f":
            {
                firmaController.deleteFirma(firma);
                items.remove(firma.getNaam());
                vulListView();
                mc.vulFirmaLijstenIn();
                break;
            }
        }
    }

}
