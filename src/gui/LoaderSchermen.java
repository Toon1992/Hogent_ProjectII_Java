/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import domein.Gebruiker;
import domein.Materiaal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import stateMachine.ReservatieStateEnum;

/**
 *
 * @author donovandesmedt
 */
public class LoaderSchermen
{

    private static LoaderSchermen instance = null;
    private boolean loggedIn;
    private double screenWidth;
    private double screenHeight;
    private Node node;

    protected LoaderSchermen()
    {
        
    }

    public static LoaderSchermen getInstance()
    {
        if (instance == null)
        {
            instance = new LoaderSchermen();
        }
        return instance;
    }

    public double getScreenWidth()
    {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth)
    {
        this.screenWidth = screenWidth;
    }

    public double getScreenHeight()
    {
        return screenHeight;
    }

    public void setScreenHeigth(double screenHeight)
    {
        this.screenHeight = screenHeight;
    }

    public void load(String titel, Parent scherm, int width, int height, Node node)
    {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle(titel);

        Scene scene = new Scene(scherm, width, height);
        stage.setScene(scene);
    }

    public void setLocation(String fxmlBestand, Node node)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlBestand));
        loader.setRoot(node);
        loader.setController(node);

        try
        {
            loader.load();
        } catch (IOException ex)
        {
            throw new RuntimeException();
        }
    }

    public boolean popupMessageTwoButtons(String title, String message, String button1, String button2)
    {
        Alert boodschap = new Alert(Alert.AlertType.CONFIRMATION);
        boodschap.setTitle(title);
        boodschap.setHeaderText(message);

        ButtonType Annuleer = new ButtonType(button1);
        ButtonType Ok = new ButtonType(button2);
        boodschap.getButtonTypes().setAll(Annuleer, Ok);
        Optional<ButtonType> result = boodschap.showAndWait();

        return result.get() == Ok;
    }

    public boolean popupMessageOneButton(String title, String message, String button1)
    {
        Alert boodschap = new Alert(Alert.AlertType.CONFIRMATION);
        boodschap.setTitle(title);
        boodschap.setHeaderText(message);

        ButtonType Ok = new ButtonType(button1);

        boodschap.getButtonTypes().setAll(Ok);
        Optional<ButtonType> result = boodschap.showAndWait();

        return result.get() == Ok;
    }

    public void setMateriaalOvezichtScherm(BorderPane bp, HBox mco)
    {

        setWidthAndHeight(bp);
        mco.setPrefWidth(getScreenWidth());
        mco.setPrefHeight(getScreenHeight() * 0.85);
        bp.setCenter(mco);
    }

    public String reservatieInvoerControle(int aantal,int aantalUit, int aantalTerug, Date startDatum, Date eindDatum, ReservatieStateEnum status, Materiaal materiaal, Gebruiker gebruiker)
    {
        if (aantal == 0)
        {
            return "Het aantal gereserveerde stuks moet groter dan 0 zijn";
        }
        if(aantalUit < 0){
            return "Het aantal uitgeleende stuks kan niet negatief zijn";
        }
        if(aantalTerug < 0){
            return "Het aantal teruggebrachte stuks kan niet negatief zijn";
        }
        if(aantalUit > aantal){
            return "Het aantal uitgeleende stuks kan niet groter dan het aantal gereserveerde stuks zijn";
        }
        if(aantalTerug > aantalUit){
            return "Het aantal teruggebrachte stuks kan niet groter zijn dan het aantal uitgeleende stuks";
        }
        if (eindDatum == null)
        {
            return "Selecteer een terugbrengdatum";
        }
        if (startDatum == null)
        {
            return "Selecteer een ophaaldatum";
        }
        if (eindDatum != null && startDatum != null && eindDatum.before(startDatum))
        {
            return "Tergubrengdatum moet groter zijn dat ophaaldatum";
        }
        if (status == null)
        {
            return "Selecteer een status";
        }
        if (materiaal == null)
        {
            return "Selecteer een materiaal";
        }
        if (gebruiker == null)
        {
            return "Selecteer een gebruiker";
        }
        return "";
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public void setWidthAndHeight(Node node)
    {
        setScreenHeigth(node.getScene().getHeight());
        setScreenWidth(node.getScene().getWidth());
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Node getNode()
    {
        return node;
    }
}
