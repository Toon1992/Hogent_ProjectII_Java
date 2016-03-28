/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalOverzichtSchermController extends HBox {

    @FXML
    private ChoiceBox<String> choiceDoelgroep;
    @FXML
    private ChoiceBox<String> choiceLeergebied;
    @FXML
    private ChoiceBox<String> choiceUitleenbaarheid;
    @FXML
    private ChoiceBox<String> choiceFirma;
    @FXML
    private ChoiceBox<String> choicePlaats;
    @FXML
    private TableView<Materiaal> materiaalTable;
    @FXML
    private TableColumn<Materiaal,String> columnImg;
    @FXML
    private TableColumn<Materiaal, String> columnNaam;
    @FXML
    private TableColumn<Materiaal, String> columnPlaats;
    @FXML
    private TableColumn<Materiaal, String> columnUitleenbaarheid;
    @FXML
    private Button btnWijzig;
    @FXML
    private Button btnNieuw;
    @FXML
    private Button btnDetails;
    @FXML
    private TextField txfZoek;

    private SortedList<Materiaal> sortedMateriaal;
    private MateriaalController mc;
    private Materiaal materiaal;

    public MateriaalOverzichtSchermController(MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("MateriaalOverzichtScherm.fxml", this);
        this.mc = mc;

        sortedMateriaal = mc.getMateriaalFilterList();
        materiaalTable.setItems(sortedMateriaal);
        sortedMateriaal.comparatorProperty().bind(materiaalTable.comparatorProperty());
        this.columnNaam.setCellValueFactory(materiaal -> materiaal.getValue().naamProperty());
        this.columnPlaats.setCellValueFactory(materiaal -> materiaal.getValue().plaatsProperty());
        this.columnUitleenbaarheid.setCellValueFactory(materiaal -> materiaal.getValue().uitleenbaarProperty());
        this.columnImg.setCellValueFactory(new PropertyValueFactory("foto"));
        /*this.columnImg.setCellFactory(materiaal -> new TableCell<Materiaal, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                if(item != null){
                    super.updateItem(item, empty);
                    VBox box= new VBox();
                    box.setAlignment(Pos.CENTER);
                    ImageView imageview = new ImageView(new Image(item));
                    imageview.setFitHeight(75);
                    imageview.setFitWidth(75);
                    box.getChildren().addAll(imageview);
                    setGraphic(box);
                }
            }
        });*/
        txfZoek.setPromptText("Zoeken");
        choiceDoelgroep.setItems(FXCollections.observableArrayList(
                "Kleuter", "Lager", "Secundair"));
        choiceLeergebied.setItems(FXCollections.observableArrayList(
                "Mens", "Maatschappij", "Geschiedenis", "Wetenschap", "Biologie", "Fysica", "Techniek", "Wiskunde", "Aardrijkskunde"));
        choiceFirma.setItems(FXCollections.observableArrayList(
                "Globe", "Weissner", "Third"));
        choiceUitleenbaarheid.setItems(FXCollections.observableArrayList(
                "Student", "Lector"));
        choicePlaats.setItems(FXCollections.observableArrayList(
                "B2.1012", "B2.036", "B4.039"));
        materiaalTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    materiaal = newValue;
                    mc.setCurrentMateriaal(materiaal);
                }
            }

        });
        choiceDoelgroep.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    mc.zoek(newValue);
                }
            }

        });
        choiceLeergebied.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    mc.zoek(newValue);
                }
            }

        });
        choiceFirma.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    mc.zoek(newValue);
                }
            }

        });
        choicePlaats.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    mc.zoek(newValue);
                }
            }

        });
        choiceUitleenbaarheid.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    mc.zoek(newValue);
                }
            }

        });
    }

    @FXML
    private void wijzigMateriaal(ActionEvent event) {
        if(materiaal == null){
            LoaderSchermen.getInstance().popupMessage("Selecteer materiaal","Selecteer een materiaal", "Annuleer", "Ok");
        }
        else{
            BorderPane bp = (BorderPane) this.getParent();
            bp.setCenter(new MateriaalDetailSchermController(mc, materiaal));
        }

    }

    @FXML
    private void nieuwMateriaal(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        bp.setCenter(new MateriaalNieuwSchermController(mc));
    }

    @FXML
    private void zoekMateriaal(KeyEvent event) {
        String zoekterm = txfZoek.getText() + event.getCharacter().trim();
        mc.zoek(zoekterm);
    }
    @FXML
    private void toonDetailsMateriaal(ActionEvent event) {
        if(materiaal == null){
            LoaderSchermen.getInstance().popupMessage("Selecteer materiaal","Selecteer een materiaal", "Annuleer", "Ok");
        }
        else{
            BorderPane bp = (BorderPane) this.getParent();
            bp.setCenter(new MateriaalDetailSchermController(mc, materiaal));
        }
    }


}
