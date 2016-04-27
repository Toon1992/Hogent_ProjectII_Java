/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.GebruikerController;
import controller.MateriaalController;
import controller.ReservatieController;
import gui.BeheerderSchermController;
import gui.LoaderSchermen;
import gui.ReservatieSchermController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class LayoutFrameController extends BorderPane {

    @FXML
    private GridPane layoutTop;
    @FXML
    private GridPane layoutBottom;
    @FXML
    private ImageView btnHome;
    @FXML
    private ImageView btnCatalogus;
    @FXML
    private ImageView btnReserveren;
    @FXML
    private ImageView btnBeheerder;
    @FXML
    private ImageView btnMails;
    @FXML
    private Label lblUitloggen;

    public LayoutFrameController(){
        LoaderSchermen.getInstance().setLocation("LayoutFrame.fxml", this);
        this.setCenter(new LoginSchermController());
    }
    @FXML
    private void gaNaarStartScherm(MouseEvent event) {
        if(LoaderSchermen.getInstance().isLoggedIn()){
            LoaderSchermen.getInstance().setWidthAndHeight(this);
            LoaderSchermen.getInstance().setOverzichtScherm(this,  new StartSchermController());
        }
    }

    @FXML
    private void gaNaarCatalogus(MouseEvent event) {
        if(LoaderSchermen.getInstance().isLoggedIn()) {
            LoaderSchermen.getInstance().setWidthAndHeight(this);
            LoaderSchermen.getInstance().setMateriaalOvezichtScherm(this, new MateriaalOverzichtSchermController());
        }
    }

    @FXML
    private void gaNaarReservaties(MouseEvent event) {
        if(LoaderSchermen.getInstance().isLoggedIn()){
            LoaderSchermen.getInstance().setWidthAndHeight(this);
            this.setCenter(new ReservatieSchermController());
        }
    }

    @FXML
    private void gaNaarBeheerders(MouseEvent event) {
        if(LoaderSchermen.getInstance().isLoggedIn()){
            LoaderSchermen.getInstance().setWidthAndHeight(this);
            this.setCenter(new BeheerderSchermController());
        }
    }

    @FXML
    private void logUit(MouseEvent event) {
        GridPane gp = (GridPane)this.getTop();
        Label lblUitloggen = (Label) gp.getChildren().get(gp.getChildren().size() -1 );
        lblUitloggen.setText("");
        LoaderSchermen.getInstance().setLoggedIn(false);
        LoaderSchermen.getInstance().setWidthAndHeight(this);
        this.setCenter(new LoginSchermController());
    }

    @FXML
    private void gaNaarMails(MouseEvent event) {
        if(LoaderSchermen.getInstance().isLoggedIn()){
            LoaderSchermen.getInstance().setWidthAndHeight(this);
            this.setCenter(new MailSchermController());
        }
    }
    
}
