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
import exceptions.AantalException;
import exceptions.NaamException;
import gui.LoaderSchermen;
import java.io.File;
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
    @FXML
    private Label lblError;

    private MateriaalController mc;
    private List<Doelgroep> doelgroepen;
    private List<Leergebied> leergebieden;
    private FileChooser fileChooser;
    private ToggleGroup group = new ToggleGroup();
    private String foto = "/images/plus.png";

    public MateriaalNieuwSchermController(MateriaalController mc) {
        LoaderSchermen.getInstance().setLocation("MateriaalNieuwScherm.fxml", this);
        this.mc = mc;

        //Scene scene = this.getScene();
        doelgroepen = new ArrayList<>();
        doelgroepen.addAll(Arrays.asList(new Doelgroep("Kleuter"), new Doelgroep("Lager"), new Doelgroep("Secundair")));
        ObservableList<String> doelgroepenString = FXCollections.observableArrayList();
        doelgroepen.stream().forEach((d) -> {
            doelgroepenString.add(d.getNaam());
        });
        listDoelgroep.setItems(doelgroepenString);
        listDoelgroep.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        leergebieden = new ArrayList<>();
        leergebieden.addAll(Arrays.asList(new Leergebied("Mens"), new Leergebied("Maatschappij"), new Leergebied("Geschiedenis"), new Leergebied("Wetenschap"), new Leergebied("Biologie"), new Leergebied("Fysica"), new Leergebied("Techniek"), new Leergebied("Wiskunde"), new Leergebied("Aardrijkskunde")));
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
        String naam = "",  omschrijving = "", plaats = "", firmaNaam = "", firmaContact = "", artikelNrString = "", aantalString = "", aantalOnbeschikbaarString = "", prijsString = "";
        boolean uitleenbaar;
        Set<Doelgroep> doelgroepen = new HashSet<>();
        Set<Leergebied> leergebieden = new HashSet<>();

        listDoelgroep.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            doelgroepen.add(new Doelgroep(s));
        });

        listLeergbedied.getSelectionModel().getSelectedItems().stream().forEach((s) -> {
            leergebieden.add(new Leergebied(s));
        });
        naam = txfNaam.getText();
        omschrijving = txfOmschrijving.getText();
        plaats = txfPlaats.getText();
        firmaNaam = txfFirma.getText();
        firmaContact = txfContactPersoon.getText();
        artikelNrString = txfArtikelNummer.getText();
        aantalString = txfAantal.getText();
        aantalOnbeschikbaarString = txfOnbeschikbaar.getText();
        prijsString = txfPrijs.getText();

        uitleenbaar = radioStudent.isSelected();
        try {
            mc.voegMateriaalToe(foto, naam, omschrijving, plaats, firmaNaam, firmaContact, artikelNrString, aantalString, aantalOnbeschikbaarString, prijsString, uitleenbaar, doelgroepen, leergebieden);
            terugNaarOverzicht(null);
        } catch (NaamException | AantalException e) {
            lblError.setText(e.getLocalizedMessage());
        }

    }

    @FXML
    private void terugNaarOverzicht(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController(mc));
    }

    @FXML
    private void voegFotoToe(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
         File file = fileChooser.showOpenDialog(stage);
        foto = file.getAbsolutePath();
        txfUrl.setText(file.getAbsolutePath());
    }

}
