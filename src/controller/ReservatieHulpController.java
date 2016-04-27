package controller;

import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import gui.LoaderSchermen;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
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
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public static <E> boolean wijzigReservatie(Map<String, E> parameters){
        boolean flag = true;
        gebruiker = (Gebruiker) parameters.get("gebruiker");
        materiaal = (Materiaal) parameters.get("materiaal");
        status = (ReservatieStateEnum) parameters.get("status");
        reservatie = parameters.containsKey("reservatie") ? (Reservatie) parameters.get("reservatie") : null;
        startDate = (Date) parameters.get("startDate");
        endDate = (Date) parameters.get("endDate");
        lblMelding = (Label) parameters.get("lblMelding");
        txfAantalGereserveerd = (TextField) parameters.get("txfAantalGereserveerd");
        txfAantalUit = (TextField) parameters.get("txfAantalUit");
        txfAantalTerug = (TextField) parameters.get("txfAantalTerug");
        type = (OperatieType) parameters.get("operatieType");
        checkOverruul = (CheckBox) parameters.get("checkOverruul");
        rc = (ReservatieController) parameters.get("reservatieController");
        int aantalGereserveerd = 0, aantalUit = 0, aantalTerug =0;
        try{
            aantalGereserveerd = Integer.parseInt(txfAantalGereserveerd.getText());
        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal gereserveerde stuks moet een nummer zijn groter dan 0");
            flag = false;
        }
        try{
            if(!txfAantalUit.getText().isEmpty()){
                aantalUit = Integer.parseInt(txfAantalUit.getText());
            }

        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal uitgeleende stuks moet een nummer zijn groter dan 0");
            flag = false;
        }
        try{
            if(!txfAantalTerug.getText().isEmpty()){
                aantalTerug = Integer.parseInt(txfAantalTerug.getText());
            }
        }
        catch (NumberFormatException e){
            lblMelding.setText("Het aantal teruggebrachte stuks moet een nummer zijn groter dan 0");
            flag = false;
        }
        if(flag){
            String controle = LoaderSchermen.getInstance().reservatieInvoerControle(aantalGereserveerd,aantalUit, aantalTerug, startDate, endDate, status, materiaal, gebruiker);
            if(!controle.isEmpty()){
                flag = false;
                lblMelding.setText(controle);
            }
            else{
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
                    if(aantalUit > aantalTerug){
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
                if(aantalUit > aantalTerug){
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
        materiaal.setAantalOnbeschikbaar(verschil);
        mc.wijzigMateriaal(materiaal);
    }
    public enum OperatieType{
        NIEUW, WIJZIG
    }
}

