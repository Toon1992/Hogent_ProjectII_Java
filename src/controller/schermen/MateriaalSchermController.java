/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.schermen;

import controller.MateriaalController;
import domein.Materiaal;

import gui.LoaderSchermen;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalSchermController extends HBox{

    @FXML
    private TextField txfZoek;
    @FXML
    private Button btnZoek;
    @FXML
    private TableView<Materiaal> materiaalTable;
    @FXML
    private TableColumn<Materiaal, String> columnNaam;
    private MateriaalController mc;
    private TextField txfNaam;
    private TextArea txfOmschrijving;
    private TextField txfArtikelNummer;
    private TextField txfAantal;
    private TextField txfOnbeschikbaar;
    private TextField txfContactPersoon;
    private RadioButton radioStudent;
    private TextField txfPlaats;
    private TextField txfPrijs;
    @FXML
    private TableColumn<Materiaal, String> columnPlaats;
    @FXML
    private TableColumn<Materiaal,String> columnUitleenbaarheid;
    private SortedList<Materiaal> sortedMateriaal;
    @FXML
    private ImageView imgViewAdd;
    private ToggleGroup group = new ToggleGroup();
    private Materiaal materiaal;
    private Label lblError;
    
    public MateriaalSchermController(MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("MateriaalScherm.fxml", this);
        this.mc = mc;

        MateriaalDetailSchermController mdsc = new MateriaalDetailSchermController(mc);
        mc.addObserver(mdsc);
        this.getChildren().add(mdsc);

        sortedMateriaal = mc.getMateriaalFilterList();
        materiaalTable.setItems(sortedMateriaal);
        sortedMateriaal.comparatorProperty().bind(materiaalTable.comparatorProperty());
        this.columnNaam.setCellValueFactory(materiaal -> materiaal.getValue().naamProperty());
        this.columnPlaats.setCellValueFactory(materiaal -> materiaal.getValue().plaatsProperty());
        this.columnUitleenbaarheid.setCellValueFactory(materiaal -> materiaal.getValue().uitleenbaarProperty());
        materiaalTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    Materiaal materiaal = newValue;
                    mc.setCurrentMateriaal(materiaal);
                }
            }

        });
    }


    @FXML
    private void voegMateriaalToe(MouseEvent event) {
        LoaderSchermen.getInstance().load("Materiaal toevoegen", new MateriaalToevoegenSchermController(mc), 1166, 643, this);
    }


    @FXML
    private void zoeken(KeyEvent event) {
        String zoekterm = txfZoek.getText() + event.getCharacter().trim();
        mc.zoek(zoekterm);
    }

    @FXML
    private void zoekenButton(ActionEvent event) {
    }
    
}
