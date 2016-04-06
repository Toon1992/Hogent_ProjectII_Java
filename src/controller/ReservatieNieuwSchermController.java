/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import gui.LoaderSchermen;
import gui.ReservatieSchermController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import stateMachine.ReservatieStateEnum;

import javax.xml.bind.SchemaOutputResolver;

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
    private TextField txfAantal;
    @FXML
    private Label lblOnvolledigheid;
    @FXML
    private Label lblOnbeschikbaarheid;
    private ReservatieController rc;
    private MateriaalController mc;
    private GebruikerController gc;
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    public ReservatieNieuwSchermController(ReservatieController rc, MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("ReservatieNieuwScherm.fxml", this);
        this.rc = rc;
        this.mc = mc;
        this.gc = new GebruikerController();
        initializeData();
    }
    private void initializeData(){
        cmbStatus.setItems(FXCollections.observableArrayList(ReservatieStateEnum.Gereserveerd, ReservatieStateEnum.Geblokkeerd, ReservatieStateEnum.Opgehaald, ReservatieStateEnum.Overruled, ReservatieStateEnum.TeLaat));
        cmbMateriaal.setItems(mc.getMateriaalFilterList());
        cmbNaam.setItems(gc.getGebruikers());
        rc.setFormatDatepicker(dtpOphaal);
        rc.setFormatDatepicker(dtpTerugbreng);
    }

    @FXML
    private void terug(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new ReservatieSchermController(rc, mc));
    }

    @FXML
    private void saveReservatie(ActionEvent event) {
        boolean flag = true;

        Gebruiker gebruiker = cmbNaam.getSelectionModel().getSelectedItem();
        Materiaal materiaal = cmbMateriaal.getSelectionModel().getSelectedItem();
        ReservatieStateEnum status = cmbStatus.getSelectionModel().getSelectedItem();
        Date startDate = dtpOphaal.getValue() == null ? null :Date.from(dtpOphaal.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = dtpTerugbreng.getValue() == null ? null :Date.from(dtpTerugbreng.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        int aantal;
        try{
            aantal = Integer.parseInt(txfAantal.getText());
        }
        catch (NumberFormatException e){
            aantal = 0;
        }

        if(aantal == 0){
            lblOnvolledigheid.setText("Voer een positief aantal in groter dan 0");
            flag = false;
        }
        if(endDate == null){
            lblOnvolledigheid.setText("Selecteer een terugbrengdatum");
            flag = false;
        }
        if(startDate == null){
            lblOnvolledigheid.setText("Selecteer een ophaaldatum");
            flag = false;
        }
        if(endDate != null && startDate != null && endDate.before(startDate)){
            lblOnvolledigheid.setText("Tergubrengdatum moet groter zijn dat ophaaldatum");
            flag = false;
        }
        if(status == null){
            lblOnvolledigheid.setText("Selecteer een status");
            flag = false;
        }
        if(materiaal == null){
            lblOnvolledigheid.setText("Selecteer een materiaal");
            flag = false;
        }
        if(gebruiker == null){
            lblOnvolledigheid.setText("Selecteer een gebruiker");
            flag = false;
        }
        berekenBeschikbaarheden(startDate, endDate, materiaal, gebruiker, aantal, status, flag);


    }
    private void berekenBeschikbaarheden(Date startDate, Date endDate, Materiaal materiaal, Gebruiker gebruiker, int aantal, ReservatieStateEnum status, boolean flag){
        List<Reservatie> overschrijvendeReservaties = rc.getReservatiesByDatum(startDate, endDate, materiaal);
        int aantalStudent = 0;
        //Indien lector enkel de reservaties van lector opvragen
        if(gebruiker.getType().equals("LE")){
            aantalStudent = overschrijvendeReservaties.stream().mapToInt(r -> r.getAantal()).sum();
            overschrijvendeReservaties = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("LE")).collect(Collectors.toList());
        }
        //Aantal stuks dat reeds onbeschikbaar zijn voor de gebruiker (lector of student)
        int aantalGereserveerdeStuks = overschrijvendeReservaties.stream().mapToInt(r -> r.getAantal()).sum();
        int aantalBeschikbaar = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar() - aantalGereserveerdeStuks;

        //Kijken of alle geselecteerde materialen beschikbaar zijn
        if(aantalBeschikbaar < aantal){
            lblOnvolledigheid.setText("");
            lblOnbeschikbaarheid.setText(String.format("Slechts %d stuks beschikbaar van materiaal %s van %s tot %s ", aantalBeschikbaar, materiaal.getNaam(), df.format(startDate), df.format(endDate)));
            flag = false;
        }

        //Indien lector wordt er gekeken of hij een student zal overrulen
        if(gebruiker.getType().equals("LE") && flag){
            int aantalOverruled = aantalStudent+ aantal - materiaal.getAantal() - materiaal.getAantalOnbeschikbaar();
            if(aantalOverruled > 0){
                lblOnbeschikbaarheid.setText("");
                lblOnvolledigheid.setText("");
                boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Blokkering maken", String.format("Blokkering mogelijk maar er zullen %d stuk(s) van student(en) moeten overruled worden, wilt u doorgaan?", aantalOverruled), "Nee", "Ja");
                flag = false;
                if (isOk)
                {
                    rc.maakReservatie(new Reservatie(aantal, startDate, endDate, new Date(), status, gebruiker, materiaal));
                    terug(null);
                }
            }
        }

        if(flag){
            lblOnvolledigheid.setText("");
            lblOnbeschikbaarheid.setText("");
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie maken", "Ben je zeker dat je de reservatie wilt opslaan", "Nee", "Ja");
            if (isOk)
            {
                rc.maakReservatie(new Reservatie(aantal, startDate, endDate, new Date(), status, gebruiker, materiaal));
                terug(null);
            }

        }
    }
    
}
