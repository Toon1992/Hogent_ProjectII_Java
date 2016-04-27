/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import gui.BeheerderSchermController;
import gui.LoaderSchermen;
import gui.ReservatieSchermController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    @FXML
    private ImageView btnMails;
    
    public StartSchermController()
    {
        LoaderSchermen.getInstance().setLocation("StartScherm.fxml", this);
        this.gc = ControllerSingelton.getGebruikerControllerInstance();
        this.mc = ControllerSingelton.getMateriaalControllerInstance();
        this.rc = ControllerSingelton.getReservatieControllerInstance();
    }

    @FXML
    private void clickedMateriaal(MouseEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp,  new MateriaalOverzichtSchermController());
    }

    @FXML
    private void clickedReservaties(MouseEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter( new ReservatieSchermController());
    }

    @FXML
    private void clickedBeheerder(MouseEvent event)
    {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new BeheerderSchermController());
    }

    @FXML
    private void hoverMateriaal(MouseEvent event)
    {
        imgViewMateriaal.setOpacity(50.0);
    }

    @FXML
    private void btnMailsOnClicked(MouseEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new MailSchermController());
    }
    
}
