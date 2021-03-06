/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import controller.ReservatieHulpController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import domein.Gebruiker;
import domein.Materiaal;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import domein.HulpMethode;
import stateMachine.ReservatieStateEnum;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class ReservatieNieuwSchermController extends GridPane {

    @FXML
    private DatePicker dtpOphaal;
    @FXML
    private DatePicker dtpTerugbreng;
    @FXML
    private Button btnTerug;
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
    private TextField txfAantalTeruggebracht;
    @FXML
    private TextField txfMaxAantal;
    @FXML
    private Label lblOnvolledigheid;
    @FXML
    private Label lblOnbeschikbaarheid;
    @FXML
    private CheckBox checkOverruul;
    private Tooltip tooltip;

    private ReservatieController rc;
    private MateriaalController mc;
    private GebruikerController gc;
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public ReservatieNieuwSchermController() {
        LoaderSchermen.getInstance().setLocation("ReservatieNieuwScherm.fxml", this);
        this.rc = ControllerSingelton.getReservatieControllerInstance();
        this.mc = ControllerSingelton.getMateriaalControllerInstance();
        this.gc = ControllerSingelton.getGebruikerControllerInstance();
        initializeData();
    }

    private void initializeData() {
        cmbStatus.setItems(FXCollections.observableArrayList(ReservatieStateEnum.Gereserveerd, ReservatieStateEnum.Geblokkeerd, ReservatieStateEnum.Opgehaald, ReservatieStateEnum.Overruled, ReservatieStateEnum.TeLaat));
        cmbMateriaal.setItems(mc.getMateriaalFilterList());
        cmbNaam.setItems(gc.getGebruikers());
        rc.setFormatDatepicker(dtpOphaal);
        rc.setFormatDatepicker(dtpTerugbreng);
        txfMaxAantal.setText(String.format("%d", 0));
        txfAantalGereserveerd.setText(String.format("%d", 0));
        txfAantalTeruggebracht.setText(String.format("%d", 0));
        txfAantalUitgeleend.setText(String.format("%d", 0));
        dtpOphaal.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                LocalDate date = dtpOphaal.getValue();
                Date maandag = HulpMethode.geefEersteDagVanDeWeek(date);
                LocalDate vrijdag = maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4);
                dtpTerugbreng.setValue(vrijdag);
            }
        });
        tooltip = new Tooltip();
        tooltip.setText("Wanneer u deze optie aanvinkt, zal automatisch de laatst toegevoegde reservatie voor dit materiaal overschreven worden.");
        Tooltip.install(checkOverruul, tooltip);
    }

    @FXML
    private void terug(ActionEvent event) {
        boolean result = LoaderSchermen.getInstance().popupMessageTwoButtons("Terug naar overzicht", "Wilt u terug gaan naar het overzicht?", "Ja", "Nee");
        if (result) {
            BorderPane bp = (BorderPane) this.getParent();
            LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new ReservatieSchermController());
        }

    }

    @FXML
    private void changeMateriaal(ActionEvent event) {
        Materiaal materiaal = cmbMateriaal.getSelectionModel().getSelectedItem();
        Date startDate = Date.from(dtpOphaal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(dtpTerugbreng.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Gebruiker gebruiker = cmbNaam.getValue();
        int aantalGerserveerd;
        try{
            aantalGerserveerd = Integer.parseInt(txfAantalGereserveerd.getText());
        }
        catch(Exception e){
            aantalGerserveerd = -1;
        }

        if(startDate != null && endDate != null && gebruiker != null && materiaal != null && aantalGerserveerd != -1){
            int[] beschikbaarheden =  rc.berekenAantalBeschikbaar(gebruiker, startDate, endDate, materiaal, 0, aantalGerserveerd);
            txfMaxAantal.setText(String.format("%d", beschikbaarheden[0]<0?0:beschikbaarheden[0]));
        }
        else{
            int aantal = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar();
            
            if(aantal < 0)
                aantal = 0;
            txfMaxAantal.setText(String.format("%d", aantal));
        }

    }

    @FXML
    private void wijzigGebruiker(ActionEvent event) {
        Gebruiker gebruiker = cmbNaam.getSelectionModel().getSelectedItem();
        if (gebruiker.getType().equals("LE")) {
            cmbStatus.getSelectionModel().select(ReservatieStateEnum.Geblokkeerd);
        } else {
            cmbStatus.getSelectionModel().select(ReservatieStateEnum.Gereserveerd);
        }
    }

    @FXML
    private <E> void saveReservatie(ActionEvent event) {
        boolean flag = true;
        Map<String, E> parameters = new HashMap<>();
        //parameters.put("gebruiker", (E) cmbNaam.getSelectionModel().getSelectedItem());
        //parameters.put("materiaal", (E) cmbMateriaal.getSelectionModel().getSelectedItem());
        //parameters.put("status", (E) cmbStatus.getSelectionModel().getSelectedItem());
        //parameters.put("startDate", (E) dtpOphaal.getValue() == null ? null :(E)Date.from(dtpOphaal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        //parameters.put("endDate", (E) dtpTerugbreng.getValue() == null ? null : (E) Date.from(dtpTerugbreng.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("txfAantalGereserveerd", (E) txfAantalGereserveerd);
        parameters.put("txfAantalUit", (E) txfAantalUitgeleend);
        parameters.put("txfAantalTerug", (E) txfAantalTeruggebracht);
        parameters.put("lblMelding", (E) lblOnvolledigheid);
        parameters.put("checkOverruul", (E) checkOverruul);
        parameters.put("reservatieController", (E) rc);
        parameters.put("operatieType", (E) ReservatieHulpController.OperatieType.NIEUW);
        parameters.put("datePickerBegin", (E) dtpOphaal);
        parameters.put("datePickerEind", (E) dtpTerugbreng);
        parameters.put("comboStatus", (E) cmbStatus);
        parameters.put("comboMateriaal", (E) cmbMateriaal);
        parameters.put("comboGebruiker", (E) cmbNaam);
        boolean succes = ReservatieHulpController.wijzigReservatie(parameters);
        if (succes) {
            lblOnvolledigheid.setText("");
        }
    }
}
