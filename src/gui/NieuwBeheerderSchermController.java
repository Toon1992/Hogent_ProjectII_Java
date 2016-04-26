///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package gui;
//
//import controller.BeheerderController;
//import controller.ControllerSingelton;
//import domein.Beheerder;
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
///**
// * FXML Controller class
// *
// * @author ToonDT
// */
//public class NieuwBeheerderSchermController extends GridPane
//{
//
//    @FXML
//    private Button btnNieuw;
//    @FXML
//    private Button btnTerug;
//    @FXML
//    private TextField txfNaam;
//    @FXML
//    private TextField txfEmail;
//    @FXML
//    private TextField txfPaswoord;
//
//    private BeheerderSchermController schermController;
//    private BeheerderController controller;
//
//    public NieuwBeheerderSchermController(BeheerderSchermController schermController)
//    {
//        LoaderSchermen.getInstance().setLocation("NieuwBeheerderScherm.fxml", this);
//
//        this.schermController = schermController;
//        this.controller = ControllerSingelton.getBeheerderControllerInstance();
//    }
//
//    @FXML
//    private void nieuwBeheerder(ActionEvent event)
//    {
//        if (txfNaam.getText() != null || txfEmail.getText() != null || txfPaswoord.getText() != null)
//        {
//            String email = txfEmail.getText();
//            if (!schermController.validateEmail(email))
//            {
//                LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Email adres is incorrect", "OK");
//                return;
//            }
//            
//            if(schermController.komtEmailVoor(email))
//            {
//                  LoaderSchermen.getInstance().popupMessageOneButton("Warning", "Email adres bestaat al", "OK");
//                    return;
//            }
//
//            Beheerder beheerder = new Beheerder(email, txfNaam.getText(),false);
//            controller.voegBeheerderToe(beheerder);
//
//            schermController.voegNieuweBeheerderToe(beheerder);
//            schermController.vulListViewIn();
//
//            terugNaarVorigScherm(btnNieuw);
//        } else
//        {
//            LoaderSchermen.getInstance().popupMessageOneButton("warning", "Alle textvelden moeten ingevuld zijn!", "OK");
//        }
//    }
//
//    @FXML
//    private void terug(ActionEvent event)
//    {
//        terugNaarVorigScherm(btnTerug);
//    }
//
//    private void terugNaarVorigScherm(Button button)
//    {
//      Stage stage = (Stage) button.getScene().getWindow();
//      stage.close();
//    }
//
//}
