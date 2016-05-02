/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.MateriaalController;
import controller.MateriaalHulpController;
import java.util.*;

import domein.Doelgroep;
import domein.Firma;
import domein.HulpMethode;
import domein.Leergebied;
import exceptions.EmailException;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
    private ImageView imgView;
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
    private String foto = "";
    private CheckComboBox<String> checkDoelgroepen;
    private CheckComboBox<String> checkLeergebieden;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;
    private GridPane gp;
    private Leergebied l = new Leergebied("l");
    private Doelgroep d = new Doelgroep("d");

    public MateriaalNieuwSchermController() {
        LoaderSchermen.getInstance().setLocation("MateriaalNieuwScherm.fxml", this);
        this.mc = ControllerSingelton.getMateriaalControllerInstance();
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

        List<String> firmas = firmaRepo.geefAlleFirmas();
        firmas.add("-- geen firma --");
        comboFirma.setItems(FXCollections.observableArrayList(firmas));
        comboFirma.setValue("-- geen firma --");

        txfContactPersoon.setDisable(true);

        gp = (GridPane) this.getChildren().get(0);
        gp.add(checkDoelgroepen, 1, 5);
        gp.add(checkLeergebieden, 3, 5);
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
        plaats = txfPlaats.getText().trim();
        firmaNaam = comboFirma.getValue();
        firmaContact = txfContactPersoon.getText();
        artikelNrString = txfArtikelNummer.getText();
        aantalString = txfAantal.getText();
        aantalOnbeschikbaarString = txfOnbeschikbaar.getText();
        prijsString = txfPrijs.getText();
        uitleenbaar = radioStudent.isSelected();

        if(invoerControle()){
            try {
                Firma firma = null;
                if(firmaNaam != null && !firmaNaam.equals("-- geen firma --")){
                    firma = mc.geefFirma(firmaNaam, firmaContact);
                }
                mc.controleerUniekheidMateriaalnaam(null, naam);
                mc.voegMateriaalToe(foto, naam, omschrijving, plaats,firma, artikelNrString, aantalString, aantalOnbeschikbaarString, prijsString, uitleenbaar, doelgroepen, leergebieden);
                lblError.setText("");
                LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd opgeslagen", "Het materiaal: "+naam+" werd succesvol opgeslaan", "Ok");
            }
            catch(EmailException e){
                lblError.setText(e.getLocalizedMessage());
                txfContactPersoon.getStyleClass().add("errorField");
            }
            catch (Exception e) {
                lblError.setText(e.getLocalizedMessage());
            }
        }
    }
    private <E> boolean invoerControle(){
        Map<String, E> data = new HashMap<>();
        data.put("naam", (E) txfNaam);
        data.put("aantal", (E) txfAantal);
        data.put("aantalonbeschikbaar", (E) txfOnbeschikbaar);
        data.put("doelgroepen", (E) listDoelgroep);
        data.put("leergebieden", (E) listLeergbedied);
        data.put("prijs", (E) txfPrijs);
        data.put("artikelnummer",(E) txfArtikelNummer);
        data.put("contact", (E) txfContactPersoon);
        data.put("label", (E) lblError);
        return MateriaalHulpController.controleerInvoer(data);
    }
    @FXML
    private void terugNaarOverzicht(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController());
    }

    @FXML
    private void voegFotoToe(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        foto = file.getAbsolutePath();
        imgView.setImage(SwingFXUtils.toFXImage(HulpMethode.convertUrlToImage(foto), null));
    }

    @FXML
    private void nieuweDoelgroep(ActionEvent event) {
        String doelgroep = MateriaalHulpController.textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
        if (checkDoelgroepen.getItems().contains(doelgroep)) {
            lblError.setText("Deze doelgroep bestaat al!");
        }
        if (!doelgroep.isEmpty() && !doelgroep.trim().isEmpty() && !checkDoelgroepen.getItems().contains(doelgroep)) {
            checkDoelgroepen = MateriaalHulpController.nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
            MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
            gp.add(checkDoelgroepen, 1, 5);
            gebiedenRepo.voegNieuwGebiedToe(doelgroep, d);
        }
    }

    @FXML
    private void nieuwLeergebied(ActionEvent event) {
        String leergebied = MateriaalHulpController.textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
        if (checkLeergebieden.getItems().contains(leergebied)) {
            lblError.setText("Dit leergebied bestaat al!");
        }
        if (!leergebied.isEmpty() && !leergebied.trim().isEmpty() && !checkLeergebieden.getItems().contains(leergebied)) {
            checkLeergebieden = MateriaalHulpController.nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
            MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
            gp.add(checkLeergebieden, 3, 5);
            gebiedenRepo.voegNieuwGebiedToe(leergebied, l);
        }
    }
    @FXML
    private void comboFirmaOnClick(ActionEvent event) {
        if(comboFirma.getSelectionModel().getSelectedItem()!=null){

            String naam=comboFirma.getSelectionModel().getSelectedItem();
            if(naam.equals("-- geen firma --")){
                txfContactPersoon.setDisable(true);
                txfContactPersoon.setText("");
            }
            else{
                txfContactPersoon.setDisable(false);
                Firma f=firmaRepo.geefFirma(naam);
                comboFirma.setPromptText(comboFirma.getSelectionModel().getSelectedItem());
                comboFirma.setValue(comboFirma.getSelectionModel().getSelectedItem());
                txfContactPersoon.setText(f.getEmailContact());
            }

        }
    }
    @FXML
    private void btnNieuweFirma(ActionEvent event) {
        String[] firma = MateriaalHulpController.inputDialogFirma();
        if(firma != null){
            String firmaNaam = firma[0];
            String contactFirma = firma[1];
            if (comboFirma.getItems().contains(firmaNaam)) {
                lblError.setText("Deze firma bestaat al!");
            } else {
                firmaRepo.voegFirmaToe(firmaNaam,contactFirma);
                List<String> firmas = new ArrayList<>();
                firmas.addAll(comboFirma.getItems());
                firmas.add(firmaNaam);
                comboFirma.setItems(FXCollections.observableArrayList(firmas));
                comboFirma.setPromptText(firmaNaam);
                txfContactPersoon.setText(contactFirma);
                comboFirma.setValue(firmaNaam);

            }
        }

    }
}
