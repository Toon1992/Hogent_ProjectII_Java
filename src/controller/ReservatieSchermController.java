/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import controller.ReservatieController;
import domein.Reservatie;
import java.time.LocalDate;
import java.util.Date;

import gui.LoaderSchermen;
import gui.ReservatieDetailsController;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class ReservatieSchermController extends HBox
{

    @FXML
    private TextField txfZoek;
    @FXML
    private DatePicker datePickerBegin;
    @FXML
    private DatePicker datePickerEind;
    @FXML
    private TableView<Reservatie> reservatieTable;
    @FXML
    private TableColumn<Reservatie, String> materiaalColumn;
    @FXML
    private TableColumn<Reservatie, Number> aantalColumn;
    @FXML
    private TableColumn<Reservatie, String> BeginDatumColumn;
    @FXML
    private TableColumn<Reservatie, String> EindDatumColumn;
    @FXML
    private TableColumn<Reservatie, String> naamColumn;
    @FXML
    private TableColumn<Reservatie, String> statusColumn;

    private ReservatieController rc;
    private SortedList<Reservatie> sortedReservatie;
    private ReservatieDetailsController reservatieDetailController;
    private Reservatie currentReservatie;

    public ReservatieSchermController(ReservatieController rc)
    {
        LoaderSchermen.getInstance().setLocation("ReservatieScherm.fxml",this);
        this.rc = rc;
        
        reservatieDetailController = new ReservatieDetailsController(rc);
        this.getChildren().add(reservatieDetailController);
             
        invullenTable();
    }
    
    private void invullenTable()
    {
        sortedReservatie = rc.getReservaties();
        reservatieTable.setItems(sortedReservatie);
        sortedReservatie.comparatorProperty().bind(reservatieTable.comparatorProperty());
        
        this.materiaalColumn.setCellValueFactory(reservatie->reservatie.getValue().naamMateriaalProperty());    
        this.aantalColumn.setCellValueFactory(reservatie->reservatie.getValue().aantalProperty());
        this.BeginDatumColumn.setCellValueFactory(reservatie->reservatie.getValue().beginDatumProperty());
        this.EindDatumColumn.setCellValueFactory(reservatie->reservatie.getValue().eindDatumProperty());
        this.naamColumn.setCellValueFactory(reservatie->reservatie.getValue().naamGebruikerProperty());
        this.statusColumn.setCellValueFactory(reservatie->reservatie.getValue().statusProperty());
        
        reservatieTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    currentReservatie = newValue;
                    rc.setCurrentReservatie(currentReservatie);
                    reservatieDetailController.setCurrenReservatie(currentReservatie);
                }
            }

        });
    }

    @FXML
    private void zoeken(KeyEvent event)
    {
        String zoekterm = txfZoek.getText() + event.getCharacter().trim();
        rc.zoek(zoekterm);
    }

    @FXML
    private void zoekOpBeginDatum(ActionEvent event)
    {
        LocalDate date = datePickerBegin.getValue();
        rc.zoekOpEindDatum(date);
    }

    @FXML
    private void zoekOpEindDatum(ActionEvent event)
    {
        LocalDate date = datePickerEind.getValue();
        rc.zoekOpEindDatum(date);
    }
}
