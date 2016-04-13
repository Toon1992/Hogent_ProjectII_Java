/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import domein.Doelgroep;
import domein.Leergebied;
import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import org.controlsfx.control.CheckComboBox;
import repository.FirmaRepository;
import repository.GebiedenRepository;
import repository.MateriaalCatalogus.MateriaalFilter;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalOverzichtSchermController extends HBox {

    @FXML
    private TableView<Materiaal> materiaalTable;
    @FXML
    private TableColumn<Materiaal,BufferedImage> columnImg;
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
    private TextField txfZoek;

    private SortedList<Materiaal> sortedMateriaal;
    private MateriaalController mc;
    private Materiaal materiaal;
    private CheckComboBox<String> checkDoelgroepen;
    private CheckComboBox<String> checkLeergebieden;
    private CheckComboBox<String> checkPlaats;
    private CheckComboBox<String> checkUitleenbaarheid;
    private CheckComboBox<String> checkFirma;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;
    private Leergebied l = new Leergebied("l");
    private Doelgroep d = new Doelgroep("d");
    @FXML
    private GridPane gridFilters;
    @FXML
    private Button btnVerwijder;
    public MateriaalOverzichtSchermController(MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("MateriaalOverzichtScherm.fxml", this);
        this.mc = mc;
        initializeVariables();
        initializeTableViewMaterialen();
        addListeners();
    }
    private void initializeVariables() {
        sortedMateriaal = mc.getMateriaalFilterList();
        materiaalTable.setItems(sortedMateriaal);
        sortedMateriaal.comparatorProperty().bind(materiaalTable.comparatorProperty());
        gebiedenRepo = new GebiedenRepository();
        firmaRepo = new FirmaRepository();
    }
    private void initializeTableViewMaterialen(){
        this.columnNaam.setCellValueFactory(materiaal -> materiaal.getValue().naamProperty());
        this.columnPlaats.setCellValueFactory(materiaal -> materiaal.getValue().plaatsProperty());
        this.columnUitleenbaarheid.setCellValueFactory(materiaal -> materiaal.getValue().uitleenbaarProperty());
        this.columnImg.setCellValueFactory(new PropertyValueFactory("foto"));
        this.columnImg.setCellFactory(materiaal -> new TableCell<Materiaal,BufferedImage>(){
            @Override
            protected void updateItem(BufferedImage item, boolean empty) {
                if(item != null){
                    super.updateItem(item, empty);
                    VBox box= new VBox();
                    box.setAlignment(Pos.CENTER);
                    ImageView imageview = new ImageView(SwingFXUtils.toFXImage(item, null));
                    imageview.setFitHeight(75);
                    imageview.setFitWidth(75);
                    box.getChildren().addAll(imageview);
                    setGraphic(box);
                }
                else{
                    setGraphic(null);
                }
            }
        });
        txfZoek.setPromptText("Zoeken");
        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(d))));
        checkDoelgroepen.setMaxWidth(150);
        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(l))));
        checkLeergebieden.setMaxWidth(150);
        checkFirma = new CheckComboBox<>(FXCollections.observableArrayList( "Globe", "Prisma", "Texas Instruments", "kimax", "Wissner"));
        checkFirma.setMaxWidth(150);
        checkPlaats = new CheckComboBox<>(FXCollections.observableArrayList(mc.getLokalen()));
        checkPlaats.setMaxWidth(150);
        checkUitleenbaarheid = new CheckComboBox<>(FXCollections.observableArrayList("Student", "Lector"));
        checkUitleenbaarheid.setMaxWidth(150);

        VBox vBox = (VBox) this.getChildren().get(0);
        GridPane gp = (GridPane) vBox.getChildren().get(0);
        gp.add(checkDoelgroepen,1,2);
        gp.add(checkLeergebieden,1, 3);
        gp.add(checkUitleenbaarheid,1, 4);
        gp.add(checkFirma,1,5);
        gp.add(checkPlaats,1, 6);

        materiaalTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    materiaal = newValue;
                    mc.setCurrentMateriaal(materiaal);
                }
            }

        });
    }
    private void addListeners(){
        checkcomboboxListener(checkDoelgroepen, "doelgroepen");
        checkcomboboxListener(checkLeergebieden, "leergebieden");
        checkcomboboxListener(checkUitleenbaarheid, "uitleenbaarheid");
        checkcomboboxListener(checkFirma, "firma");
        checkcomboboxListener(checkPlaats, "plaats");
    }
    @FXML
    private void wijzigMateriaal(ActionEvent event) {
        if(materiaal == null){
            LoaderSchermen.getInstance().popupMessageTwoButtons("Selecteer materiaal","Selecteer een materiaal", "Annuleer", "Ok");
        }
        else{
            BorderPane bp = (BorderPane) this.getParent();
            LoaderSchermen.getInstance().setNode(this);
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
        mc.zoek(Arrays.asList(zoekterm));
    }
    private <E> void checkcomboboxListener(CheckComboBox<E> check, String name){
        check.getCheckModel().getCheckedItems().addListener(new ListChangeListener<E>() {
            public void onChanged(ListChangeListener.Change<? extends E> c) {
                MateriaalFilter filterName = null;
                switch (name.toLowerCase()){
                    case "doelgroepen": filterName = MateriaalFilter.DOELGROEP; break;
                    case "leergebieden": filterName = MateriaalFilter.LEERGEBIED; break;
                    case "uitleenbaarheid": filterName = MateriaalFilter.UITLEENBAARHEID; break;
                    case "firma": filterName = MateriaalFilter.FIRMA; break;
                    case "plaats": filterName = MateriaalFilter.PLAATS; break;
                }
                mc.filter(filterName, check.getCheckModel().getCheckedItems().stream().map(e -> e.toString()).collect(Collectors.toList()));
            }
        });
    }

    @FXML
    private void verwijderMateriaal(ActionEvent event) {
        if(materiaal == null){
            LoaderSchermen.getInstance().popupMessageTwoButtons("Selecteer materiaal","Selecteer een materiaal", "Annuleer", "Ok");
        }
        else {
            try{
                 mc.verwijderMateriaal(materiaal);
            }catch(Exception ex){
                LoaderSchermen.getInstance().popupMessageOneButton("Verwijderen", String.format("Er zijn nog openstaande reservaties voor materiaal: %s",materiaal.getNaam()), "Ok");
            }
           
        }
    }
    @FXML
    private void resetFilters(ActionEvent event){
        txfZoek.setText("");
        checkDoelgroepen.getCheckModel().clearChecks();
        checkLeergebieden.getCheckModel().clearChecks();
        checkUitleenbaarheid.getCheckModel().clearChecks();
        checkFirma.getCheckModel().clearChecks();
        checkPlaats.getCheckModel().clearChecks();
        materiaalTable.setItems(mc.getMateriaalFilterList());
    }


}
