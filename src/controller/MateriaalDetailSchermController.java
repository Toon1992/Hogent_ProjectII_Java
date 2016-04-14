/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import repository.FirmaRepository;
import repository.GebiedenRepository;
import repository.MateriaalCatalogus.*;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalDetailSchermController extends VBox {

    @FXML
    private ImageView imgViewMateriaal;
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
    private MateriaalController mc;
    private Materiaal materiaal;
    private ToggleGroup group = new ToggleGroup();
    @FXML
    private Label lblErrorMessage;
    @FXML
    private TextField txfUrl;
    @FXML
    private ComboBox<String> comboFirma;
    private GridPane gp;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;
    private CheckComboBox<String> checkDoelgroepen;
    private CheckComboBox<String> checkLeergebieden;
    private FileChooser fileChooser;
    private Leergebied l = new Leergebied("l");
    Doelgroep d = new Doelgroep("d");
    private String foto;

    public MateriaalDetailSchermController(MateriaalController mc, Materiaal materiaal) {
        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml", this);
        this.materiaal = materiaal;
        this.mc = mc;
        initializeItems();
    }

    private void initializeItems(){
        gebiedenRepo = new GebiedenRepository();
        firmaRepo=new FirmaRepository();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies een foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));

        update(materiaal);

        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(d)));
        checkDoelgroepen.setMaxWidth(200);

        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(l)));
        checkLeergebieden.setMaxWidth(200);

        comboFirma.setItems(FXCollections.observableArrayList(firmaRepo.geefAlleFirmas()));

        gp = (GridPane) this.getChildren().get(0);
        gp.add(checkDoelgroepen,1,6);
        gp.add(checkLeergebieden,3, 6);
        MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
        MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);


    }

    @FXML
    private void wijzigFoto(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        foto = file.getAbsolutePath();
        txfUrl.setText(file.getAbsolutePath());
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event) {
        try {

            //materiaal wijzigen
            materiaal.setNaam(txfNaam.getText());
            materiaal.setOmschrijving(txfOmschrijving.getText());
            materiaal.setAantal(Integer.parseInt(txfAantal.getText()));
            materiaal.setAantalOnbeschikbaar(Integer.parseInt(txfOnbeschikbaar.getText()));
            materiaal.setPlaats(txfPlaats.getText());

            materiaal.setDoelgroepen(gebiedenRepo.geefGebiedenVoorNamen(listDoelgroep.getItems(), d));


            materiaal.setLeergebieden(gebiedenRepo.geefGebiedenVoorNamen(listLeergbedied.getItems(), l));


            materiaal.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
            String prijs = txfPrijs.getText().replace(",", ".");
            materiaal.setPrijs(Double.valueOf(prijs));

            materiaal.setIsReserveerbaar(radioStudent.isSelected());

            //firma maken
            Firma f=firmaRepo.geefFirma(comboFirma.getValue()); // omdat als het al gewijzigd is dan kan je nooit opvragen
            f.setEmailContact(txfContactPersoon.getText());
            if(foto != null){
                materiaal.setFoto(foto);
            }

            materiaal.setFirma(f);
            firmaRepo.wijzigFirma(f);
            mc.wijzigMateriaal(materiaal);
            lblErrorMessage.setText("");
            LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd : " + materiaal.getNaam(),"Al uw wijzigingen zijn correct doorgevoerd", "Ok");

        } catch (NumberFormatException ex) {
            lblErrorMessage.setText("Er werd een foute waarde ingegeven.");
        } catch (IllegalArgumentException ex) {
            lblErrorMessage.setText(ex.getMessage());
        }

    }

    public void update(Materiaal materiaal)
    {
        imgViewMateriaal.setImage(SwingFXUtils.toFXImage(materiaal.getFoto(), null));
        txfAantal.setText(String.format("%d", materiaal.getAantal()));
        txfArtikelNummer.setText(String.format("%d", materiaal.getArtikelNr()));
        txfContactPersoon.setText(materiaal.getFirma().getEmailContact());
        listDoelgroep.setItems(mc.objectCollectionToObservableList(materiaal.getDoelgroepen()).sorted());
        listLeergbedied.setItems(mc.objectCollectionToObservableList(materiaal.getLeergebieden()).sorted());
        comboFirma.setPromptText(materiaal.getFirma().getNaam());
        comboFirma.setValue(materiaal.getFirma().getNaam());
        txfNaam.setText(materiaal.getNaam());
        txfOmschrijving.setText(materiaal.getOmschrijving());
        txfOnbeschikbaar.setText(String.format("%d", materiaal.getAantalOnbeschikbaar()));
        txfPlaats.setText(materiaal.getPlaats());
        txfPrijs.setText(String.format("%.2f", materiaal.getPrijs()));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(materiaal.getIsReserveerbaar());
        radioLector.setSelected(!materiaal.getIsReserveerbaar());
        radioLector.setToggleGroup(group);

    }

    @FXML
    private void terugNaarOverzicht(ActionEvent event) {
        BorderPane bp = (BorderPane) this.getParent();
        LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, (HBox) LoaderSchermen.getInstance().getNode());
    }
    @FXML
    private void nieuwLeergebied(ActionEvent event){
        String leergebied = MateriaalHulpController.textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
        if(checkLeergebieden.getItems().contains(leergebied)){
            lblErrorMessage.setText("Dit leergebied bestaat al!");
        }
        if(!leergebied.isEmpty()&&!checkLeergebieden.getItems().contains(leergebied)){
            checkLeergebieden = MateriaalHulpController.nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
            MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
            gp.add(checkLeergebieden,3, 4);
            gebiedenRepo.voegNieuwGebiedToe(leergebied,l);
        }

    }
    @FXML
    private void nieuweDoelgroep(ActionEvent event){
        String doelgroep = MateriaalHulpController.textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
        if(checkDoelgroepen.getItems().contains(doelgroep)){
            lblErrorMessage.setText("Deze doelgroep bestaat al!");
        }
        if(!doelgroep.isEmpty()&&!checkDoelgroepen.getItems().contains(doelgroep)){
            checkDoelgroepen = MateriaalHulpController.nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
            MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
            gp.add(checkDoelgroepen,1,4);
            gebiedenRepo.voegNieuwGebiedToe(doelgroep,d);
        }

    }
    @FXML
    private void comboFirmaOnClick(ActionEvent event) {
        if(comboFirma.getSelectionModel().getSelectedItem()!=null){
            List<String> firmas = comboFirma.getItems();
            System.out.println(comboFirma.getSelectionModel().getSelectedItem());
            if(firmas.contains(comboFirma.getSelectionModel().getSelectedItem())){
                String naam=comboFirma.getSelectionModel().getSelectedItem();
                Firma f=firmaRepo.geefFirma(naam);
                comboFirma.setPromptText(comboFirma.getSelectionModel().getSelectedItem());
                txfContactPersoon.setText(f.getEmailContact());
            }
        }
    }


    @FXML
    private void btnNieuweFirma(ActionEvent event) {
        String firma = MateriaalHulpController.textInputDialog("Nieuwe firma", "Voeg een nieuwe firma toe", "Voer naam in:");
        if(comboFirma.getItems().contains(firma)){
            lblErrorMessage.setText("Deze firma bestaat al!");
        }
        else{
            firmaRepo.voegFirmaToe(firma, txfContactPersoon.getText());
            List<String> firmas=new ArrayList<>();
            firmas.addAll(comboFirma.getItems());
            firmas.add(firma);
            comboFirma.setItems(FXCollections.observableArrayList(firmas));
            comboFirma.setPromptText(firma);
            comboFirma.setValue(firma);
            txfContactPersoon.setText("");

        }

    }


}
