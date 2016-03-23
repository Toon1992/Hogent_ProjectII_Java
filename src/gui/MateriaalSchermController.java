/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalSchermController extends HBox {

    @FXML
    private TextField txfZoek;
    @FXML
    private Button btnZoek;
    @FXML
    private TableView<?> materiaalTable;
    @FXML
    private TableColumn<?, ?> columnNaam;
    @FXML
    private TableColumn<?, ?> columnLeergebied;
    @FXML
    private TableColumn<?, ?> columnDoelgroep;
    @FXML
    private ImageView imgView;
    private DomeinController dc;
    @FXML
    private ImageView imgViewe;
    public MateriaalSchermController(DomeinController dc){
        LoaderSchermen.getInstance().setLocation("MateriaalScherm.fxml", this);
        this.dc = dc;
        imgView.setImage(new Image("/images/plus.png"));
        imgView.setFitWidth(100);
    }

    @FXML
    private void zoeken(ActionEvent event) {
    }

    @FXML
    private void voegMateriaalToe(MouseEvent event) {
        LoaderSchermen.getInstance().load("Materiaal toevoegen", new MateriaalToevoegenSchermController(dc), 1166, 643, this);
    }
    
}
