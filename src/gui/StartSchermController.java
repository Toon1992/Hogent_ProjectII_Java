/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class StartSchermController extends GridPane
{
    private DomeinController dc;
    
    public StartSchermController(DomeinController dc)
    {
       LoaderSchermen.getInstance().setLocation("StartScherm.fxml", this);
       this.dc = dc;
    }

    @FXML
    private void clickedMateriaal(MouseEvent event)
    {
         LoaderSchermen.getInstance().load("Materialen", new MateriaalSchermController(dc), 1300, 640, this);
    }

    @FXML
    private void clickedReservaties(MouseEvent event)
    {
    }

    @FXML
    private void clickedBeheerder(MouseEvent event)
    {
    }
    
}
