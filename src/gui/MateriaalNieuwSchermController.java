/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.MateriaalController;
import controller.MateriaalHulpController;
import java.util.*;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;
import exceptions.AantalException;
import exceptions.NaamException;
import gui.LoaderSchermen;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import repository.FirmaRepository;
import repository.GebiedenRepository;
import domein.MateriaalCatalogus.*;

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
    @FXML
    private ComboBox<String> comboFirma;

    private MateriaalController mc;
    private List<Doelgroep> doelgroepen;
    private List<Leergebied> leergebieden;
    private FileChooser fileChooser;
    private ToggleGroup group = new ToggleGroup();
    private String foto = "/images/plus.png";
    private CheckComboBox<String> checkDoelgroepen;
    private CheckComboBox<String> checkLeergebieden;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;
    private GridPane gp;
    private Leergebied l = new Leergebied("l");
    private Doelgroep d = new Doelgroep("d");

    public MateriaalNieuwSchermController(MateriaalController mc) {
        LoaderSchermen.getInstance().setLocation("MateriaalNieuwScherm.fxml", this);
        this.mc = mc;
        initializeItems();
    }

    private void initializeItems() {
        gebiedenRepo = new GebiedenRepository();
        firmaRepo = new FirmaRepository();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies een foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(true);
        radioLector.setToggleGroup(group);

        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(d)));
        checkDoelgroepen.setMaxWidth(200);

        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(l)));
        checkLeergebieden.setMaxWidth(200);

        comboFirma.setItems(FXCollections.observableArrayList(firmaRepo.geefAlleFirmas()));

        gp = (GridPane) this.getChildren().get(0);
        gp.add(checkDoelgroepen, 1, 4);
        gp.add(checkLeergebieden, 3, 4);
        MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
        MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
    }

    @FXML
    private void voegToe(ActionEvent event) {
        String naam = "", omschrijving = "", plaats = "", firmaNaam = "", firmaContact = "", artikelNrString = "", aantalString = "", aantalOnbeschikbaarString = "", prijsString = "";
        boolean uitleenbaar;
        Set<Doelgroep> doelgroepen = new HashSet<>(gebiedenRepo.geefGebiedenVoorNamen(listDoelgroep.getItems(), d));
        Set<Leergebied> leergebieden = new HashSet<>(gebiedenRepo.geefGebiedenVoorNamen(listLeergbedied.getItems(), l));

        naam = txfNaam.getText();
        omschrijving = txfOmschrijving.getText();
        plaats = txfPlaats.getText();
        firmaNaam = comboFirma.getValue();
        firmaContact = txfContactPersoon.getText();
        artikelNrString = txfArtikelNummer.getText();
        aantalString = txfAantal.getText();
        aantalOnbeschikbaarString = txfOnbeschikbaar.getText();
        prijsString = txfPrijs.getText();

        uitleenbaar = radioStudent.isSelected();
        try {
            mc.voegMateriaalToe(foto, naam, omschrijving, plaats, firmaNaam, firmaContact, artikelNrString, aantalString, aantalOnbeschikbaarString, prijsString, uitleenbaar, doelgroepen, leergebieden);
            LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd opgeslagen", "Het materiaal: "+naam+" werd succesvol opgeslaan", "Ok");
        } catch (IllegalArgumentException e) {
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

    @FXML
    private void nieuweDoelgroep(ActionEvent event) {
        String doelgroep = MateriaalHulpController.textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
        if (checkDoelgroepen.getItems().contains(doelgroep)) {
            lblError.setText("Deze doelgroep bestaat al!");
        }
        if (!doelgroep.isEmpty() && !checkDoelgroepen.getItems().contains(doelgroep)) {
            checkDoelgroepen = MateriaalHulpController.nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
            MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
            gp.add(checkDoelgroepen, 1, 4);
            gebiedenRepo.voegNieuwGebiedToe(doelgroep, d);
        }
    }

    @FXML
    private void nieuwLeergebied(ActionEvent event) {
        String leergebied = MateriaalHulpController.textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
        if (checkLeergebieden.getItems().contains(leergebied)) {
            lblError.setText("Dit leergebied bestaat al!");
        }
        if (!leergebied.isEmpty() && !checkLeergebieden.getItems().contains(leergebied)) {
            checkLeergebieden = MateriaalHulpController.nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
            MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
            gp.add(checkLeergebieden, 3, 4);
            gebiedenRepo.voegNieuwGebiedToe(leergebied, l);
        }
    }
    @FXML
    private void comboFirmaOnClick(ActionEvent event) {
        if(comboFirma.getSelectionModel().getSelectedItem()!=null){
            String naam=comboFirma.getSelectionModel().getSelectedItem();
            Firma f=firmaRepo.geefFirma(naam);
            comboFirma.setPromptText(comboFirma.getSelectionModel().getSelectedItem());
            comboFirma.setValue(comboFirma.getSelectionModel().getSelectedItem());
            txfContactPersoon.setText(f.getEmailContact());
        }
    }
    @FXML
    private void btnNieuweFirma(ActionEvent event) {
        String firma = MateriaalHulpController.textInputDialog("Nieuwe firma", "Voeg een nieuwe firma toe", "Voer naam in:");
        if (comboFirma.getItems().contains(firma)) {
            lblError.setText("Deze firma bestaat al!");
        } else {
            firmaRepo.voegFirmaToe(firma, txfContactPersoon.getText());
            List<String> firmas = new ArrayList<>();
            firmas.addAll(comboFirma.getItems());
            firmas.add(firma);
            comboFirma.setItems(FXCollections.observableArrayList(firmas));
            comboFirma.setPromptText(firma);
            txfContactPersoon.setText("");
            comboFirma.setValue(firma);

        }
    }
}
