package controller;

import domein.Gebruiker;
import domein.HulpMethode;
import domein.Materiaal;
import domein.Reservatie;
import gui.LoaderSchermen;
import javafx.scene.control.*;
import stateMachine.ReservatieStateEnum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * Created by donovandesmedt on 17/04/16.
 */
public class ReservatieHulpController {
    private static ReservatieController rc;
    private static MateriaalController mc;
    private static Gebruiker gebruiker;
    private static Materiaal materiaal;
    private static Reservatie reservatie;
    private static ReservatieStateEnum status;
    private static Date startDate;
    private static Date endDate;
    private static Label lblMelding;
    private static TextField txfAantalGereserveerd;
    private static TextField txfAantalUit;
    private static TextField txfAantalTerug;
    private static CheckBox checkOverruul;
    private static OperatieType type;
    private static DatePicker datePickerBegin;
    private static DatePicker datePickerEind;
    private static ComboBox comboStatus;
    private static ComboBox comboGebruiker;
    private static ComboBox comboMateriaal;
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public static <E> boolean wijzigReservatie(Map<String, E> parameters){
        boolean flag = true;
        gebruiker = ((ComboBox<Gebruiker>) parameters.get("comboGebruiker")).getSelectionModel().getSelectedItem();
        materiaal = ((ComboBox<Materiaal>) parameters.get("comboMateriaal")).getSelectionModel().getSelectedItem();
        status = ((ComboBox<ReservatieStateEnum>) parameters.get("comboStatus")).getSelectionModel().getSelectedItem();
        reservatie = parameters.containsKey("reservatie") ? (Reservatie) parameters.get("reservatie") : null;
        startDate =  ((DatePicker) parameters.get("datePickerBegin")).getValue() == null ? null : HulpMethode.convertLocalDateToDate(((DatePicker) parameters.get("datePickerBegin")).getValue());
        endDate = ((DatePicker) parameters.get("datePickerEind")).getValue() == null ? null : HulpMethode.convertLocalDateToDate(((DatePicker) parameters.get("datePickerEind")).getValue());
        lblMelding = (Label) parameters.get("lblMelding");
        txfAantalGereserveerd = (TextField) parameters.get("txfAantalGereserveerd");
        txfAantalUit = (TextField) parameters.get("txfAantalUit");
        txfAantalTerug = (TextField) parameters.get("txfAantalTerug");
        type = (OperatieType) parameters.get("operatieType");
        checkOverruul = (CheckBox) parameters.get("checkOverruul");
        datePickerBegin = (DatePicker) parameters.get("datePickerBegin");
        datePickerEind = (DatePicker) parameters.get("datePickerEind");
        rc = (ReservatieController) parameters.get("reservatieController");
        comboStatus = (ComboBox) parameters.get("comboStatus");
        comboMateriaal = (ComboBox) parameters.get("comboMateriaal");
        comboGebruiker = (ComboBox) parameters.get("comboGebruiker");
        cleanInputFields();
        int aantalGereserveerd = 0, aantalUit = 0, aantalTerug =0;
        try{
            aantalGereserveerd = Integer.parseInt(txfAantalGereserveerd.getText());
        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal gereserveerde stuks moet een nummer zijn groter dan 0");
            txfAantalGereserveerd.getStyleClass().add("errorField");
            flag = false;
        }
        try{
            if(!txfAantalUit.getText().isEmpty()){
                aantalUit = Integer.parseInt(txfAantalUit.getText());
            }
        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal uitgeleende stuks moet een nummer zijn groter dan 0");
            txfAantalUit.getStyleClass().add("errorField");
            flag = false;
        }
        try{
            if(!txfAantalTerug.getText().isEmpty()){
                aantalTerug = Integer.parseInt(txfAantalTerug.getText());
            }
        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal teruggebrachte stuks moet een nummer zijn groter dan 0");
            txfAantalTerug.getStyleClass().add("errorField");
            flag = false;
        }
        if(flag){
            String controle = reservatieInvoerControle(aantalGereserveerd,aantalUit, aantalTerug, startDate, endDate, status, materiaal, gebruiker);
            if(!controle.isEmpty()){
                flag = false;
                lblMelding.setText(controle);
            }
            else{
                if(txfAantalUit.getText().isEmpty()){
                    aantalUit = -1;
                }
                if(txfAantalTerug.getText().isEmpty()){
                    aantalTerug = -1;
                }
                return berekenBeschikbaarheden(startDate, endDate, materiaal, gebruiker, aantalGereserveerd,aantalUit, aantalTerug, status, flag);
            }
        }
        return flag;
    }
    private static boolean berekenBeschikbaarheden(Date startDate, Date endDate, Materiaal materiaal, Gebruiker gebruiker, int aantal,int aantalUit, int aantalTerug, ReservatieStateEnum status, boolean flag){
        int aantalGerserveerd = reservatie == null? 0 : reservatie.getAantalGereserveerd();
        int[] beschikbaarheden = rc.berekenAantalBeschikbaar(gebruiker, startDate, endDate, materiaal, aantal, aantalGerserveerd);
        int aantalBeschikbaar = beschikbaarheden[0];
        int aantalOverruled = beschikbaarheden[1];

        boolean automatischOverrulen = checkOverruul.isSelected();

        //Kijken of alle geselecteerde materialen beschikbaar zijn
        if(aantalBeschikbaar < aantal){
            lblMelding.setText(String.format("Slechts %d stuks beschikbaar van materiaal %s van %s tot %s ", aantalBeschikbaar, materiaal.getNaam(), df.format(startDate), df.format(endDate)));
            flag = false;
        }

        //Indien lector wordt er gekeken of hij een student zal overrulen
        if(gebruiker.getType().equals("LE") && flag){
            if(aantalOverruled > 0){
                lblMelding.setText("");
                String nietAutomatisch = String.format("OPGELET: Blokkering mogelijk maar er zullen %d stuk(s) van student(en) manueel moeten overruled worden, wilt u doorgaan?", aantalOverruled);
                String automatisch = String.format("OPGELET: Blokkering mogelijk maar er zullen automatisch %d stuk(s) van student(en) overruled worden, wilt u doorgaan?", aantalOverruled);
                boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Blokkering maken",automatischOverrulen? automatisch : nietAutomatisch, "Ja", "Nee");
                flag = false;
                if (isOk)
                {
                    switch (type){
                        case NIEUW: rc.maakReservatie(aantal,aantalUit, aantalTerug, startDate, endDate, status, gebruiker, materiaal);break;
                        case WIJZIG: rc.wijzigReservatie(reservatie, aantal,aantalUit, aantalTerug, gebruiker, startDate, endDate, materiaal, status); break;
                    }
                    if(checkOverruul.isSelected()){
                        rc.overruleStudent(aantalOverruled, materiaal);
                    }
                    //Indien niet alle stuks van een materiaal teruggebracht worden moet het aantalonbeschikbaar van het materiaal aangepast worden.
                    if(aantalUit > aantalTerug && aantalTerug != -1){
                        updateAantalOnbeschikbaar(materiaal, aantalUit - aantalTerug);
                    }
                    return true;
                }
            }
        }
        if(flag){
            lblMelding.setText("");
            lblMelding.setText("");
            boolean isOk = LoaderSchermen.getInstance().popupMessageTwoButtons("Reservatie maken", "Ben je zeker dat je de reservatie wilt opslaan", "Ja", "Nee");
            if (isOk)
            {
                switch (type){
                    case NIEUW: {
                        rc.maakReservatie(aantal,aantalUit, aantalTerug, startDate, endDate, status, gebruiker, materiaal);
                        LoaderSchermen.getInstance().popupMessageOneButton("Reservatie succesvol gemaakt",String.format("Er werden %d stuk(s) van materiaal %s gereserveerd door %s van %s tot %s",aantal, materiaal.getNaam(), gebruiker.getNaam(), df.format(startDate), df.format(endDate)), "Ok");
                        break;
                    }
                    case WIJZIG: {
                        rc.wijzigReservatie(reservatie, aantal,aantalUit, aantalTerug, gebruiker, startDate, endDate, materiaal, status);
                        LoaderSchermen.getInstance().popupMessageOneButton("Reservatie succesvol gewijzigd",String.format("Er werden %d stuk(s) van materiaal %s gereserveerd door %s van %s tot %s",aantal, materiaal.getNaam(), gebruiker.getNaam(), df.format(startDate), df.format(endDate)), "Ok");
                    } break;
                }
                if(aantalUit > aantalTerug && aantalTerug != -1){
                    updateAantalOnbeschikbaar(materiaal, aantalUit - aantalTerug);
                }
            }
        }

        return flag;
    }
    private static void updateAantalOnbeschikbaar(Materiaal materiaal, int verschil){
        if(mc == null){
            mc = new MateriaalController();
        }
        verschil += materiaal.getAantalOnbeschikbaar();
        materiaal.setAantalOnbeschikbaar(verschil);
        mc.wijzigMateriaal(materiaal);
    }
    private static void cleanInputFields(){
        txfAantalGereserveerd.getStyleClass().remove("errorField");
        txfAantalUit.getStyleClass().remove("errorField");
        txfAantalTerug.getStyleClass().remove("errorField");
        datePickerBegin.getStyleClass().remove("errorField");
        datePickerEind.getStyleClass().remove("errorField");
        comboStatus.getStyleClass().remove("errorField");
        comboGebruiker.getStyleClass().remove("errorField");
        comboMateriaal.getStyleClass().remove("errorField");
    }
    private static String reservatieInvoerControle(int aantal,int aantalUit, int aantalTerug, Date startDatum, Date eindDatum, ReservatieStateEnum status, Materiaal materiaal, Gebruiker gebruiker){
        if (aantal <= 0)
        {
            txfAantalGereserveerd.getStyleClass().add("errorField");
            return "Het aantal gereserveerde stuks moet groter dan 0 zijn";
        }
        if(aantalUit < 0){
            txfAantalUit.getStyleClass().add("errorField");
            return "Het aantal uitgeleende stuks kan niet negatief zijn";
        }
        if(aantalTerug < 0){
            txfAantalTerug.getStyleClass().add("errorField");
            return "Het aantal teruggebrachte stuks kan niet negatief zijn";
        }
        if(aantalUit > aantal){
            txfAantalUit.getStyleClass().add("errorField");
            return "Het aantal uitgeleende stuks kan niet groter dan het aantal gereserveerde stuks zijn";
        }
        if(aantalTerug > aantalUit){
            txfAantalTerug.getStyleClass().add("errorField");
            return "Het aantal teruggebrachte stuks kan niet groter zijn dan het aantal uitgeleende stuks";
        }
        if (eindDatum == null)
        {
            datePickerEind.getStyleClass().add("errorField");
            return "Selecteer een terugbrengdatum";
        }
        if (startDatum == null)
        {
            datePickerBegin.getStyleClass().add("errorField");
            return "Selecteer een ophaaldatum";
        }
        if (eindDatum != null && startDatum != null && eindDatum.before(startDatum))
        {
            datePickerEind.getStyleClass().add("errorField");
            return "Tergubrengdatum moet groter zijn dat ophaaldatum";
        }
        if(eindDatum.before(HulpMethode.convertLocalDateToDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1)))){
            datePickerBegin.getStyleClass().add("errorField");
            datePickerEind.getStyleClass().add("errorField");
            return "Je kan niet in het verleden reserveren";
        }
        if (status == null)
        {
            comboStatus.getStyleClass().add("errorField");
            return "Selecteer een status";
        }
        if (materiaal == null)
        {
            comboMateriaal.getStyleClass().add("errorField");
            return "Selecteer een materiaal";
        }
        if (gebruiker == null)
        {
            comboGebruiker.getStyleClass().add("errorField");
            return "Selecteer een gebruiker";
        }
        return "";
    }
    public enum OperatieType{
        NIEUW, WIJZIG
    }
}

