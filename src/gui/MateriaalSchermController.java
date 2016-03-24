/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Materiaal;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
public class MateriaalSchermController extends HBox implements Observer{

    @FXML
    private TextField txfZoek;
    @FXML
    private Button btnZoek;
    @FXML
    private TableView<Materiaal> materiaalTable;
    @FXML
    private TableColumn<Materiaal, String> columnNaam;
    private DomeinController dc;
    @FXML
    private TextField txfNaam;
    @FXML
    private TextArea txfOmschrijving;
    @FXML
    private TextField txfFirma;
    @FXML
    private TextField txfArtikelNummer;
    @FXML
    private TextField txfAantal;
    @FXML
    private TextField txfOnbeschikbaar;
    @FXML
    private TextField txfContactPersoon;
    @FXML
    private RadioButton radioStudent;
    @FXML
    private RadioButton radioLector;
    @FXML
    private TextField txfPlaats;
    @FXML
    private TextField txfPrijs;
    @FXML
    private Button btnOpslaan;
    @FXML
    private TableColumn<Materiaal, String> columnPlaats;
    @FXML
    private TableColumn<Materiaal,String> columnUitleenbaarheid;
    private SortedList<Materiaal> sortedMateriaal;
    @FXML
    private ImageView imgViewAdd;
    @FXML
    private ImageView imgViewMateriaal;
    @FXML
    private ListView<String> listDoelgroep;
    @FXML
    private ListView<String> listLeergbedied;
    
    
    public MateriaalSchermController(DomeinController dc){
        LoaderSchermen.getInstance().setLocation("MateriaalScherm.fxml", this);
        this.dc = dc;
        imgViewAdd.setImage(new Image("/images/plus.png"));
        imgViewAdd.setFitWidth(100);
        sortedMateriaal = dc.getMateriaalFilterList();
        materiaalTable.setItems(sortedMateriaal);
        this.columnNaam.setCellValueFactory(materiaal -> materiaal.getValue().naamProperty());
        this.columnPlaats.setCellValueFactory(materiaal -> materiaal.getValue().plaatsProperty());
        this.columnUitleenbaarheid.setCellValueFactory(materiaal -> materiaal.getValue().uitleenbaarProperty());
        materiaalTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    Materiaal materiaal = newValue;
                    dc.setCurrentMateriaal(materiaal);
                }
            }

        });
    }

    @FXML
    private void zoeken(ActionEvent event) {
    }

    @FXML
    private void voegMateriaalToe(MouseEvent event) {
        LoaderSchermen.getInstance().load("Materiaal toevoegen", new MateriaalToevoegenSchermController(dc), 1166, 643, this);
    }

    @FXML
    private void wijzigFoto(MouseEvent event) {
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event) {
    }

    @Override
    public void update(Observable o, Object obj) {
        Materiaal m = (Materiaal) obj;
        imgViewMateriaal.setImage(new Image(m.getFoto()));
        txfAantal.setText(String.format("%d", m.getAantal()));
        txfArtikelNummer.setText(String.format("%d", m.getArtikelNr()));
        txfContactPersoon.setText(m.getFirma().getEmailContact());
        listDoelgroep.setItems(dc.objectCollectionToObservableList(m.getDoelgroepen()).sorted());
        listLeergbedied.setItems(dc.objectCollectionToObservableList(m.getLeergebieden()).sorted());
        txfFirma.setText(m.getFirma().getNaam());
        txfNaam.setText(m.getNaam());
        txfOmschrijving.setText(m.getOmschrijving());
        txfOnbeschikbaar.setText(String.format("%d", m.getAantalOnbeschikbaar()));
        txfPlaats.setText(m.getPlaats());
        txfPrijs.setText(String.format("%.2f", m.getPrijs()));
       
    }
    
}
