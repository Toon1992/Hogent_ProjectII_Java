/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import gui.ReservatieSchermController;
import gui.LoaderSchermen;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ToonDT
 */
public class StartSchermController extends GridPane
{
    private GebruikerController gc;
    private MateriaalController mc;
    private ReservatieController rc;
    
    @FXML
    private ImageView imgViewMateriaal;
    
    public StartSchermController(GebruikerController gc)
    {
        LoaderSchermen.getInstance().setLocation("StartScherm.fxml", this);
        
        this.gc = gc;
        this.mc = new MateriaalController();
        this.rc = new ReservatieController();
    }

    @FXML
    private void clickedMateriaal(MouseEvent event)
    {
         LoaderSchermen.getInstance().load("Materialen", new MateriaalSchermController(mc), 1300, 740, this);
    }

    @FXML
    private void clickedReservaties(MouseEvent event)
    {
         LoaderSchermen.getInstance().load("Reservaties", new ReservatieSchermController(rc), 1300, 740, this);
    }

    @FXML
    private void clickedBeheerder(MouseEvent event)
    {
    }

    @FXML
    private void hoverMateriaal(MouseEvent event)
    {
        imgViewMateriaal.setOpacity(50.0);
    }
    
}
