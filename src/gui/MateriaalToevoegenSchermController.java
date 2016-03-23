/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Doelgroep;
import domein.DomeinController;
import domein.Firma;
import domein.Leergebied;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Thomas
 */
public class MateriaalToevoegenSchermController extends VBox {

    @FXML
    private TextField txfNaam;
    @FXML
    private TextField txfOmschrijving;
    @FXML
    private TextField txfAantalCatalogus;
    @FXML
    private TextField txfPlaats;
    @FXML
    private TextField txfAantalOnbeschikbaar;
    @FXML
    private TextField txfArtikelNr;
    @FXML
    private TextField txfPrijs;
    @FXML
    private RadioButton radioJa;
    @FXML
    private RadioButton radioNee;
    @FXML
    private TextField txfFirmaNaam;
    @FXML
    private TextField txfFirmaEmail;
    @FXML
    private Button btnFoto;
    @FXML
    private Button btnToevoegen;
    private DomeinController dc;
    private List<Doelgroep> doelgroepen;
    private List<Leergebied> leergebieden;
    private FileChooser fileChooser;
    private Stage stage = new Stage();
    private String foto = "test";
    private ToggleGroup group = new ToggleGroup();
    @FXML
    private ListView<String> listViewDoelgroepen = new ListView<>();
    @FXML
    private ListView<String> listViewLeergebieden = new ListView<>();
    

    /**
     * Initializes the controller class.
     */
    public MateriaalToevoegenSchermController(DomeinController dc) {
        LoaderSchermen.getInstance().setLocation("MateriaalToevoegenScherm.fxml", this);
        this.dc = dc;
        
        //Scene scene = this.getScene();
        doelgroepen = new ArrayList<>();
        doelgroepen.add(new Doelgroep("test"));
        doelgroepen.add(new Doelgroep("test2"));
        ObservableList<String> doelgroepenString = FXCollections.observableArrayList();
        doelgroepen.stream().forEach((d) -> {
            doelgroepenString.add(d.getNaam());
        });
        listViewDoelgroepen.setItems(doelgroepenString);
        listViewDoelgroepen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        leergebieden = new ArrayList<>();
        leergebieden.add(new Leergebied("test"));
        leergebieden.add(new Leergebied("test2"));
        ObservableList<String> leergebiedenString = FXCollections.observableArrayList();
        leergebieden.stream().forEach((d) -> {
            leergebiedenString.add(d.getNaam());
        });
        listViewLeergebieden.setItems(leergebiedenString);
        listViewLeergebieden.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies een foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        radioJa.setToggleGroup(group);
        radioJa.setSelected(true);
        radioNee.setToggleGroup(group);
    }

    @FXML
    private void voegToe(ActionEvent event) {
        String naam ="", omschrijving ="", plaats ="", firmaNaam = "", firmaContact = "";
        int artikelNr = 0, aantal = 0, aantalOnbeschikbaar = 0;
        double prijs = 0;
        boolean uitleenbaar;
        Firma firma = null;
        List<Doelgroep> doelgroepen = new ArrayList<>();
        List<Leergebied> leergebieden = new ArrayList<>();
        if (txfNaam.getText() != null) {
            naam = txfNaam.getText();
        }
        if (txfOmschrijving.getText() != null) {
            omschrijving = txfOmschrijving.getText();
        }
        if (txfPlaats.getText() != null) {
            plaats = txfPlaats.getText();
        }
        if (txfArtikelNr.getText() != null) {
            artikelNr = Integer.parseInt(txfArtikelNr.getText());
        }
        if (txfAantalCatalogus.getText() != null) {
            aantal = Integer.parseInt(txfAantalCatalogus.getText());
        }
        if (txfAantalOnbeschikbaar.getText() != null) {
            aantalOnbeschikbaar = Integer.parseInt(txfAantalOnbeschikbaar.getText());
        }
        if (txfPrijs.getText() != null) {
            prijs = Double.parseDouble(txfPrijs.getText());
        }
        if (txfFirmaNaam.getText() != null) {
            firmaNaam = txfFirmaNaam.getText();
        }
        if (txfFirmaEmail.getText() != null) {
            firmaContact = txfFirmaEmail.getText();
        }
        if (txfFirmaNaam.getText() != null && txfFirmaEmail.getText() != null) {
            firma = new Firma(firmaNaam, firmaContact);
        }
        listViewDoelgroepen.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            doelgroepen.add(new Doelgroep(s));
        });
        
        listViewLeergebieden.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            leergebieden.add(new Leergebied(s));
        });
        
        uitleenbaar = radioJa.isSelected();
        dc.voegMateriaalToe(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
        //LoaderSchermen.getInstance().load("Materialen", new MateriaalSchermController(dc), 1166, 643, this);
        System.out.println("toegevoegd");
    }

    @FXML
    private void fotoToevoegen(ActionEvent event) {
//        File file = fileChooser.showOpenDialog(stage);
//        foto = file.getAbsolutePath();
    }

}
