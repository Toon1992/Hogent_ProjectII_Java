/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author manu
 */
public class MateriaalOverzichtSchermController implements Initializable {

    @FXML
    private GridPane gridFilters;
    @FXML
    private TextField txfZoek;
    @FXML
    private TableView<?> materiaalTable;
    @FXML
    private TableColumn<?, ?> columnImg;
    @FXML
    private TableColumn<?, ?> columnNaam;
    @FXML
    private TableColumn<?, ?> columnPlaats;
    @FXML
    private TableColumn<?, ?> columnUitleenbaarheid;
    @FXML
    private Button btnWijzig;
    @FXML
    private Button btnNieuw;
    @FXML
    private Button btnVerwijder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void zoekMateriaal(KeyEvent event) {
    }

    @FXML
    private void wijzigMateriaal(ActionEvent event) {
    }

    @FXML
    private void nieuwMateriaal(ActionEvent event) {
    }

    @FXML
    private void verwijderMateriaal(ActionEvent event) {
    }
    
}
