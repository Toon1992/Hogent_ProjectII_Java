/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import controller.ReservatieNieuwSchermController;
import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import stateMachine.ReservatieStateEnum;

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
    @FXML
    private DatePicker dtpOphaal;
    @FXML
    private DatePicker dtpTerugbreng;
    @FXML
    private ComboBox<ReservatieStateEnum> cmbStatus;
    @FXML
    private ComboBox<Materiaal> cmbMateriaal;
    @FXML
    private ComboBox<Gebruiker> cmbNaam;
    @FXML
    private TextField txfAantal;

    private ReservatieController rc;
    private MateriaalController mc;
    private GebruikerController gc;
    private SortedList<Reservatie> sortedReservatie;
    private Reservatie currentReservatie;
    private Reservatie reservatie;

    public ReservatieSchermController(ReservatieController rc, MateriaalController mc)
    {
        LoaderSchermen.getInstance().setLocation("ReservatieScherm.fxml",this);
        this.rc = rc;
        this.mc = mc;
        this.gc = new GebruikerController();
        invullenTable();
        vulComboBoxStatus();
        vulComboBoxMateriaal();
        vulComboBoxGebruiker();
        rc.setFormatDatepicker(dtpOphaal);
        rc.setFormatDatepicker(dtpTerugbreng);
        rc.setFormatDatepicker(datePickerBegin);
        rc.setFormatDatepicker(datePickerEind);
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
                    setCurrenReservatie(currentReservatie);
                }
            }

        });
    }
    private void vulComboBoxStatus()
    {
        cmbStatus.setItems(FXCollections.observableArrayList(ReservatieStateEnum.Gereserveerd, ReservatieStateEnum.Geblokkeerd, ReservatieStateEnum.Opgehaald, ReservatieStateEnum.Overruled, ReservatieStateEnum.TeLaat));
    }

    private void vulComboBoxMateriaal()
    {
        cmbMateriaal.setItems(mc.getMateriaalFilterList());
    }
    private void vulComboBoxGebruiker(){
        cmbNaam.setItems(gc.getGebruikers());;
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
        LocalDate dateBegin = datePickerBegin.getValue();
        LocalDate dateEind = datePickerEind.getValue();
        rc.zoekOpBeginDatum(dateBegin);
        if(dateEind != null){
            rc.zoekOpEindDatum(dateEind);
        }


    }

    @FXML
    private void zoekOpEindDatum(ActionEvent event)
    {
        LocalDate dateBegin = datePickerBegin.getValue();
        LocalDate dateEind = datePickerEind.getValue();
        if(dateBegin != null){
            rc.zoekOpBeginDatum(dateBegin);
        }
        rc.zoekOpEindDatum(dateEind);
    }

    @FXML
    private void verwijderReservatie(ActionEvent event) {
        if (reservatie == null)
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Reservatie verwijderen", "Je moet eerst een reservatie selecteren", "Ok");
        } else
        {
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie Verwijderen", "Ben je zeker dat je de reservatie wilt verwijderen", "Ja", "Nee");
            if (!isOk)
            {
                rc.verwijderReservatie(reservatie);
            }
        }
    }
    public void setCurrenReservatie(Reservatie reservatie)
    {
        this.reservatie = reservatie;
        update();
    }
    public void update()
    {
        if (reservatie == null)
        {
            return;
        }

        dtpOphaal.setValue(reservatie.getBeginDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dtpTerugbreng.setValue(reservatie.getEindDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        cmbStatus.setValue(reservatie.getReservatieStateEnum());
        cmbMateriaal.setValue(reservatie.getMateriaal());
        cmbNaam.setValue(reservatie.getGebruiker());
        txfAantal.setText(String.format("%d", reservatie.getAantal()));
    }

    @FXML
    private void wijzigReservatie(ActionEvent event) {
    }

    @FXML
    private void nieuweReservatie(ActionEvent event) {
        BorderPane bp = (BorderPane)this.getParent();
        bp.setCenter(new ReservatieNieuwSchermController(rc, mc));
    }
}
