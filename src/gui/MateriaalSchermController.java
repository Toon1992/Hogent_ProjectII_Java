/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Doelgroep;
import domein.DomeinController;
import domein.Materiaal;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

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
    private ToggleGroup group = new ToggleGroup();
    private Materiaal m;
    @FXML
    private Label lblError;
    
    public MateriaalSchermController(DomeinController dc){
        LoaderSchermen.getInstance().setLocation("MateriaalScherm.fxml", this);
        this.dc = dc;
        sortedMateriaal = dc.getMateriaalFilterList();
        materiaalTable.setItems(sortedMateriaal);
        sortedMateriaal.comparatorProperty().bind(materiaalTable.comparatorProperty());
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
    private void voegMateriaalToe(MouseEvent event) {
        LoaderSchermen.getInstance().load("Materiaal toevoegen", new MateriaalToevoegenSchermController(dc), 1166, 643, this);
    }

    @FXML
    private void wijzigFoto(MouseEvent event) {
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event) {
        
        try{
            m.setNaam(txfNaam.getText());
            m.setOmschrijving(txfOmschrijving.getText());
            m.setAantal(Integer.parseInt(txfAantal.getText()));
            m.setAantalOnbeschikbaar(Integer.parseInt(txfOnbeschikbaar.getText()));
            m.setPlaats(txfPlaats.getText());
            m.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
            String prijs=txfPrijs.getText().replace(",", ".");
            m.setPrijs(Double.valueOf(prijs));
            m.getFirma().setEmailContact(txfContactPersoon.getText());
            m.setUitleenbaarheid(radioStudent.isSelected());
            lblError.setText("");
            Alert succesvol=new Alert(Alert.AlertType.CONFIRMATION);
            succesvol.setTitle("Materiaal gewijzigd");
            succesvol.setHeaderText(m.getNaam());
            succesvol.setContentText("Al uw wijzigingen zijn correct doorgevoerd!");
            succesvol.showAndWait();
            
        }
        catch(NumberFormatException ex){
            lblError.setText("Er werd een foute waarde ingegeven.");
        }
        catch(IllegalArgumentException ex){
            lblError.setText(ex.getMessage());
        }
        
        
    }

    @Override
    public void update(Observable o, Object obj) {
        m = (Materiaal) obj;
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
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(m.getUitleenbaarheid());
        radioLector.setSelected(!m.getUitleenbaarheid());
        radioLector.setToggleGroup(group);

    }

    @FXML
    private void zoeken(KeyEvent event) {
        String zoekterm = txfZoek.getText() + event.getCharacter().trim();
        dc.zoek(zoekterm);
    }

    @FXML
    private void zoekenButton(ActionEvent event) {
    }
    
}
