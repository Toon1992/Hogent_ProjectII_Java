/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author donovandesmedt
 */
public class LoaderSchermen
{

    private static LoaderSchermen instance = null;
    private boolean loggedIn;

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

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

}
