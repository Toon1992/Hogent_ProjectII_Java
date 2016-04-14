/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import gui.LoaderSchermen;
import gui.ReservatieSchermController;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import repository.HulpMethode;
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
    @FXML
    private CheckBox checkOverruul;

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
        dtpOphaal.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(dtpTerugbreng.getValue() == null ){
                    LocalDate date = dtpOphaal.getValue();
                    Date maandag = HulpMethode.geefEersteDagVanDeWeek(date);
                    LocalDate vrijdag = maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4);
                    dtpTerugbreng.setValue(vrijdag);
                }
            }
        });
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
        String controle = LoaderSchermen.getInstance().reservatieInvoerControle(aantal, startDate, endDate, status, materiaal, gebruiker);
        if(!controle.isEmpty()){
            flag = false;
            lblOnvolledigheid.setText(controle);
        }

        berekenBeschikbaarheden(startDate, endDate, materiaal, gebruiker, aantal, status, flag);


    }
    private void berekenBeschikbaarheden(Date startDate, Date endDate, Materiaal materiaal, Gebruiker gebruiker, int aantal, ReservatieStateEnum status, boolean flag){
        int[] beschikbaarheden = rc.berekenAantalBeschikbaar(gebruiker, startDate, endDate, materiaal, aantal, 0);
        int aantalBeschikbaar = beschikbaarheden[0];
        int aantalOverruled = beschikbaarheden[1];
        boolean automatischOverrulen = checkOverruul.isSelected();

        //Kijken of alle geselecteerde materialen beschikbaar zijn
        if(aantalBeschikbaar < aantal){
            lblOnvolledigheid.setText("");
            lblOnbeschikbaarheid.setText(String.format("Slechts %d stuks beschikbaar van materiaal %s van %s tot %s ", aantalBeschikbaar, materiaal.getNaam(), df.format(startDate), df.format(endDate)));
            flag = false;
        }

        //Indien lector wordt er gekeken of hij een student zal overrulen
        if(gebruiker.getType().equals("LE") && flag){
            if(aantalOverruled > 0){
                lblOnbeschikbaarheid.setText("");
                lblOnvolledigheid.setText("");
                String nietAutomatisch = String.format("OPGELET: Blokkering mogelijk maar er zullen %d stuk(s) van student(en) manueel moeten overruled worden, wilt u doorgaan?", aantalOverruled);
                String automatisch = String.format("OPGELET: Blokkering mogelijk maar er zullen automatisch %d stuk(s) van student(en) overruled worden, wilt u doorgaan?", aantalOverruled);
                boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Blokkering maken",automatischOverrulen? automatisch : nietAutomatisch , "Nee", "Ja");
                flag = false;
                if (isOk)
                {

                    rc.maakReservatie(aantal,0, startDate, endDate, status, gebruiker, materiaal);
                    if(automatischOverrulen){
                        rc.overruleStudent(aantalOverruled, materiaal);
                    }
                    terug(null);
                }
            }
        }
        if(flag){
            lblOnvolledigheid.setText("");
            lblOnbeschikbaarheid.setText("");
            boolean isNotOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie maken", "Ben je zeker dat je de reservatie wilt opslaan", "Ja", "Nee");
            if (!isNotOk)
            {
                rc.maakReservatie(aantal,0, startDate, endDate, status, gebruiker, materiaal);
                terug(null);
            }

        }
    }
    
}
