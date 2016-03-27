/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import controller.ReservatieController;
import domein.Reservatie;
import javafx.collections.transformation.SortedList;
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
    private TableColumn<Reservatie, Object> BeginDatumColumn;
    @FXML
    private TableColumn<Reservatie, Object> EindDatumColumn;
    @FXML
    private TableColumn<Reservatie, String> naamColumn;

    private ReservatieController rc;
    private SortedList<Reservatie> sortedReservatie;

    public ReservatieSchermController(ReservatieController rc)
    {
        LoaderSchermen.getInstance().setLocation("ReservatieScherm.fxml",this);
        this.rc = rc;
        
        
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

        reservatieTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    Reservatie reservatie = newValue;
                    rc.setCurrentReservatie(reservatie);
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
}
