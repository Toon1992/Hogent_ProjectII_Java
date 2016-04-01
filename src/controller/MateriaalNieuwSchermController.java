/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.*;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;
import gui.LoaderSchermen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalNieuwSchermController extends VBox {

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
    private ListView<String> listDoelgroep;
    @FXML
    private ListView<String> listLeergbedied;
    @FXML
    private TextField txfNaam;
    @FXML
    private Button btnTerug;
    @FXML
    private TextField txfUrl;
    @FXML
    private Button btnAddImage;
    @FXML
    private Button btnToevoegen;

    private MateriaalController mc;
    private List<Doelgroep> doelgroepen;
    private List<Leergebied> leergebieden;
    private FileChooser fileChooser;
    private Stage stage = new Stage();
    private String foto = "test";
    private ToggleGroup group = new ToggleGroup();
    public MateriaalNieuwSchermController(MateriaalController mc){
        LoaderSchermen.getInstance().setLocation("MateriaalNieuwScherm.fxml", this);
        this.mc = mc;

        //Scene scene = this.getScene();
        doelgroepen = new ArrayList<>();
        doelgroepen.add(new Doelgroep("test"));
        doelgroepen.add(new Doelgroep("test2"));
        ObservableList<String> doelgroepenString = FXCollections.observableArrayList();
        doelgroepen.stream().forEach((d) -> {
            doelgroepenString.add(d.getNaam());
        });
        listDoelgroep.setItems(doelgroepenString);
        listDoelgroep.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        leergebieden = new ArrayList<>();
        leergebieden.add(new Leergebied("test"));
        leergebieden.add(new Leergebied("test2"));
        ObservableList<String> leergebiedenString = FXCollections.observableArrayList();
        leergebieden.stream().forEach((d) -> {
            leergebiedenString.add(d.getNaam());
        });
        listLeergbedied.setItems(leergebiedenString);
        listLeergbedied.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies een foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(true);
        radioLector.setToggleGroup(group);
    }

    @FXML
    private void voegToe(ActionEvent event) {
        String naam ="", omschrijving ="", plaats ="", firmaNaam = "", firmaContact = "";
        int artikelNr = 0, aantal = 0, aantalOnbeschikbaar = 0;
        double prijs = 0;
        boolean uitleenbaar;
        Firma firma = null;
        Set<Doelgroep> doelgroepen = new HashSet<>();
        Set<Leergebied> leergebieden = new HashSet<>();
        if (txfNaam.getText() != null) {
            naam = txfNaam.getText();
        }
        if (txfOmschrijving.getText() != null) {
            omschrijving = txfOmschrijving.getText();
        }
        if (txfPlaats.getText() != null) {
            plaats = txfPlaats.getText();
        }
        if (txfArtikelNummer.getText() != null) {
            artikelNr = Integer.parseInt(txfArtikelNummer.getText());
        }
        if (txfAantal.getText() != null) {
            aantal = Integer.parseInt(txfAantal.getText());
        }
        if (txfOnbeschikbaar.getText() != null) {
            aantalOnbeschikbaar = Integer.parseInt(txfOnbeschikbaar.getText());
        }
        if (txfPrijs.getText() != null) {
            prijs = Double.parseDouble(txfPrijs.getText());
        }
        if (txfFirma.getText() != null) {
            firmaNaam = txfFirma.getText();
        }
        if (txfContactPersoon.getText() != null) {
            firmaContact = txfContactPersoon.getText();
        }
        if (txfFirma.getText() != null && txfContactPersoon.getText() != null) {
            firma = new Firma(firmaNaam, firmaContact);
        }
        listDoelgroep.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            doelgroepen.add(new Doelgroep(s));
        });

        listLeergbedied.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            leergebieden.add(new Leergebied(s));
        });

        uitleenbaar = radioStudent.isSelected();
        mc.voegMateriaalToe(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
        terugNaarOverzicht(null);
        System.out.println("toegevoegd");
    }

    @FXML
    private void terugNaarOverzicht(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController(mc));
    }

    @FXML
    private void voegFotoToe(ActionEvent event) {
        //        File file = fileChooser.showOpenDialog(stage);
//        foto = file.getAbsolutePath();
    }
    
}
