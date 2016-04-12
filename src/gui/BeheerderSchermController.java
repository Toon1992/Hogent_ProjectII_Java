/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.BeheerderController;
import domein.Beheerder;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class BeheerderSchermController extends GridPane
{

    @FXML
    private ListView<String> lvBeheerder;
    
    private BeheerderController controller;
    
    public BeheerderSchermController()
    {
        controller = new BeheerderController();
        LoaderSchermen.getInstance().setLocation("BeheerderScherm.fxml", this);
        
        vulListViewIn();
    }
    
    private void vulListViewIn()
    {
        lvBeheerder.setItems(FXCollections.observableArrayList(controller.getBeheerders().stream().map(Beheerder::getNaam).collect(Collectors.toList())));
    }
    
}
