/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.*;
import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
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
    private TableColumn<Reservatie, Number> aantalBeschikbaarColumn;
    @FXML
    private TableColumn<Reservatie, String> BeginDatumColumn;
    @FXML
    private TableColumn<Reservatie, String> EindDatumColumn;
    @FXML
    private TableColumn<Reservatie, String> naamColumn;
    @FXML
    private TableColumn<Reservatie, String> statusColumn;
    @FXML
    private TableColumn<Reservatie, String> typeColumn;
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
    private TextField txfAantalGereserveerd;
    @FXML
    private TextField txfAantalUitgeleend;
    @FXML
    private TextField txfAantalTerug;
    @FXML
    private Label lblMelding;
    @FXML
    private CheckBox checkOverruul;

    private ReservatieController rc;
    private MateriaalController mc;
    private GebruikerController gc;
    private SortedList<Reservatie> sortedReservatie;
    private Reservatie currentReservatie;
    private Reservatie reservatie;

    public ReservatieSchermController()
    {
        LoaderSchermen.getInstance().setLocation("ReservatieScherm.fxml", this);
        this.rc = ControllerSingelton.getReservatieControllerInstance();
        this.mc = ControllerSingelton.getMateriaalControllerInstance();
        this.gc = ControllerSingelton.getGebruikerControllerInstance();

        invullenTable();
        vulComboBoxStatus();
        vulComboBoxMateriaal();
        vulComboBoxGebruiker();

        rc.setFormatDatepicker(dtpOphaal);
        rc.setFormatDatepicker(dtpTerugbreng);
        rc.setFormatDatepicker(datePickerBegin);
        rc.setFormatDatepicker(datePickerEind);

        if (reservatie == null)
        {
            dtpOphaal.setDisable(true);
            dtpTerugbreng.setDisable(true);
            cmbNaam.setDisable(true);
            cmbMateriaal.setDisable(true);
            cmbStatus.setDisable(true);
            txfAantalGereserveerd.setDisable(true);
            txfAantalUitgeleend.setDisable(true);
            txfAantalTerug.setDisable(true);
            checkOverruul.setDisable(true);
        }
    }

    private void invullenTable()
    {
        sortedReservatie = rc.getReservaties();
        reservatieTable.setItems(sortedReservatie.sorted(Comparator.comparing(Reservatie::getBeginDatum)));
        sortedReservatie.comparatorProperty().bind(reservatieTable.comparatorProperty());

        this.materiaalColumn.setCellValueFactory(reservatie -> reservatie.getValue().naamMateriaalProperty());
        this.aantalColumn.setCellValueFactory(reservatie -> reservatie.getValue().aantalProperty());
        this.aantalBeschikbaarColumn.setCellValueFactory(reservatie -> reservatie.getValue().getMateriaal().aantalProperty());
        this.BeginDatumColumn.setCellValueFactory(reservatie -> reservatie.getValue().beginDatumProperty());
        this.EindDatumColumn.setCellValueFactory(reservatie -> reservatie.getValue().eindDatumProperty());
        this.naamColumn.setCellValueFactory(reservatie -> reservatie.getValue().naamGebruikerProperty());
        this.statusColumn.setCellValueFactory(reservatie -> reservatie.getValue().statusProperty());
        this.typeColumn.setCellValueFactory(reservatie -> reservatie.getValue().getGebruiker().typeProperty());

        reservatieTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> 
                {
                    if (newValue != null)
                    {
                        if (oldValue == null || !oldValue.equals(newValue))
                        {
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

    private void vulComboBoxGebruiker()
    {
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

        if (dateEind != null)
        {
            rc.zoekOpEindDatum(dateEind);
        }

        rc.zoekOpBeginDatum(dateBegin);
    }

    @FXML
    private void zoekOpEindDatum(ActionEvent event)
    {
        LocalDate dateBegin = datePickerBegin.getValue();
        LocalDate dateEind = datePickerEind.getValue();
        rc.zoekOpEindDatum(dateEind);
        if (dateBegin != null)
        {
            rc.zoekOpBeginDatum(dateBegin);
        }
        
    }

    @FXML
    private void verwijderReservatie(ActionEvent event)
    {
        if (reservatie == null)
        {
            LoaderSchermen.getInstance().popupMessageOneButton("Reservatie verwijderen", "Je moet eerst een reservatie selecteren", "Ok");
        } else
        {
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie Verwijderen", "Ben je zeker dat je de reservatie wilt verwijderen", "Ja", "Nee");
            if (isOk)
            {
                rc.verwijderReservatie(reservatie);
            }
        }
    }

    public void setCurrenReservatie(Reservatie reservatie)
    {
        this.reservatie = reservatie;
        dtpOphaal.setDisable(false);
        dtpTerugbreng.setDisable(false);
        cmbNaam.setDisable(false);
        cmbMateriaal.setDisable(false);
        cmbStatus.setDisable(false);
        txfAantalGereserveerd.setDisable(false);
        txfAantalUitgeleend.setDisable(false);
        txfAantalTerug.setDisable(false);
        checkOverruul.setDisable(false);
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
        txfAantalGereserveerd.setText(String.format("%d", reservatie.getAantalGereserveerd()));
        txfAantalUitgeleend.setText(String.format("%d", reservatie.getAantalUitgeleend()));
        txfAantalTerug.setText(String.format("%d", reservatie.getAantalTeruggebracht()));
    }

    @FXML
    private <E> void wijzigReservatie(ActionEvent event)
    {
        Map<String, E> parameters = new HashMap<>();
        parameters.put("gebruiker", (E) cmbNaam.getSelectionModel().getSelectedItem());
        parameters.put("materiaal", (E) cmbMateriaal.getSelectionModel().getSelectedItem());
        parameters.put("status", (E) cmbStatus.getSelectionModel().getSelectedItem());
        parameters.put("reservatie", (E) reservatie);
        parameters.put("startDate", (E) dtpOphaal.getValue() == null ? null : (E) Date.from(dtpOphaal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("endDate", (E) dtpTerugbreng.getValue() == null ? null : (E) Date.from(dtpTerugbreng.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("txfAantalGereserveerd", (E) txfAantalGereserveerd);
        parameters.put("txfAantalUit", (E) txfAantalUitgeleend);
        parameters.put("txfAantalTerug", (E) txfAantalTerug);
        parameters.put("lblMelding", (E) lblMelding);
        parameters.put("lblMelding", (E) lblMelding);
        parameters.put("checkOverruul", (E) checkOverruul);
        parameters.put("reservatieController", (E) rc);
        parameters.put("operatieType", (E) ReservatieHulpController.OperatieType.WIJZIG);
        boolean succes = ReservatieHulpController.wijzigReservatie(parameters);
        if (succes)
        {
            lblMelding.setText("");
            invullenTable();
        }
    }

    @FXML
    private void nieuweReservatie(ActionEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new ReservatieNieuwSchermController());
    }
}
