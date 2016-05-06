/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.FirmaController;
import controller.GebiedenController;
import controller.MateriaalController;
import controller.MateriaalHulpController;
import java.util.*;

import domein.Doelgroep;
import domein.Firma;
import domein.HulpMethode;
import domein.Leergebied;
import domein.Materiaal;
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
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * FXML Controller class
 *
 * @author donovandesmedt
 */
public class MateriaalNieuwSchermController extends VBox
{

    @FXML
    private TextArea txfOmschrijving;
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
    private GebiedenController gebiedenController;
    private FirmaController firmaController;
    private GridPane gp;
    private Leergebied l = new Leergebied("l");
    private Doelgroep d = new Doelgroep("d");
    private List<String> firmas;
    private Materiaal currentMateriaal;

    public MateriaalNieuwSchermController(Materiaal currentMateriaal)
    {
        LoaderSchermen.getInstance().setLocation("MateriaalNieuwScherm.fxml", this);
        this.mc = ControllerSingelton.getMateriaalControllerInstance();
        this.currentMateriaal = currentMateriaal;

        if (currentMateriaal == null)
        {
            btnToevoegen.setText("Voeg toe");
        } else
        {
            btnToevoegen.setText("Wijzigen");
        }

        initializeItems();
    }

    protected void vulDoelgroepenLijstIn()
    {
        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenController.geefAlleGebieden(d)));
        checkDoelgroepen.setMaxWidth(200);
        gp.add(checkDoelgroepen, 1, 5);
    }

    protected void vulLeergebiedenLijstIn()
    {
        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenController.geefAlleGebieden(l)));
        checkLeergebieden.setMaxWidth(200);
        gp.add(checkLeergebieden, 3, 5);
    }

    protected void vulFirmaLijstenIn()
    {
        firmas = firmaController.geefAlleFirmas();
        firmas.add("-- geen firma --");
        comboFirma.setItems(FXCollections.observableArrayList(firmas));
        comboFirma.setValue("-- geen firma --");

    }

    protected void vulGridPaneOp()
    {
        MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
        MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
    }

    protected void vullijsten()
    {
        gp = (GridPane) this.getChildren().get(0);

        vulDoelgroepenLijstIn();
        vulLeergebiedenLijstIn();
        vulFirmaLijstenIn();

        vulGridPaneOp();

    }

    private void initializeItems()
    {
        gebiedenController = ControllerSingelton.getGebiedenControllerInstance();
        firmaController = ControllerSingelton.getFirmaControllerInstance();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies een foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(true);
        radioLector.setToggleGroup(group);

        if (currentMateriaal != null)
        {
            update();
        }

        vullijsten();

        txfContactPersoon.setDisable(true);

    }

    private void update()
    {
        if (currentMateriaal.getFoto() != null)
        {
            imgView.setImage(SwingFXUtils.toFXImage(currentMateriaal.getFoto(), null));
        } else
        {
            imgView.setImage(new Image("images/add.png"));
        }
        txfAantal.setText(String.format("%d", currentMateriaal.getAantal()));
        txfArtikelNummer.setText(String.format("%d", currentMateriaal.getArtikelNr()));
        if (currentMateriaal.getFirma() != null)
        {
            txfContactPersoon.setText(currentMateriaal.getFirma().getEmailContact());
            comboFirma.setPromptText(currentMateriaal.getFirma().getNaam());
            comboFirma.setValue(currentMateriaal.getFirma().getNaam());
            txfContactPersoon.setDisable(false);
        } else
        {
            comboFirma.setValue("-- geen firma --");
            txfContactPersoon.setText("");
            txfContactPersoon.setDisable(true);
        }
        listDoelgroep.setItems(mc.objectCollectionToObservableList(currentMateriaal.getDoelgroepen()).sorted());
        listLeergbedied.setItems(mc.objectCollectionToObservableList(currentMateriaal.getLeergebieden()).sorted());

        txfNaam.setText(currentMateriaal.getNaam());
        txfOmschrijving.setText(currentMateriaal.getOmschrijving());
        txfOnbeschikbaar.setText(String.format("%d", currentMateriaal.getAantalOnbeschikbaar()));
        txfPlaats.setText(currentMateriaal.getPlaats());
        txfPrijs.setText(String.format("%.2f", currentMateriaal.getPrijs()));
        radioStudent.setToggleGroup(group);
        radioStudent.setSelected(currentMateriaal.getIsReserveerbaar());
        radioLector.setSelected(!currentMateriaal.getIsReserveerbaar());
        radioLector.setToggleGroup(group);

    }

    @FXML
    private void voegToe(ActionEvent event)
    {
        String naam = "", omschrijving = "", plaats = "", firmaNaam = "", firmaContact = "", artikelNrString = "", aantalString = "", aantalOnbeschikbaarString = "", prijsString = "";
        boolean uitleenbaar;
        Set<Doelgroep> doelgroepen = new HashSet<>(gebiedenController.geefGebiedenVoorNamen(listDoelgroep.getItems(), d));
        Set<Leergebied> leergebieden = new HashSet<>(gebiedenController.geefGebiedenVoorNamen(listLeergbedied.getItems(), l));

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

        if (invoerControle())
        {
            try
            {
                Firma firma = null;
                if (firmaNaam != null && !firmaNaam.equals("-- geen firma --"))
                {
                    firma = mc.geefFirma(firmaNaam, firmaContact);
                }
                mc.controleerUniekheidMateriaalnaam(null, naam);
                mc.voegMateriaalToe(foto, naam, omschrijving, plaats, firma, artikelNrString, aantalString, aantalOnbeschikbaarString, prijsString, uitleenbaar, doelgroepen, leergebieden);
                lblError.setText("");
                LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd opgeslagen", "Het materiaal: " + naam + " werd succesvol opgeslaan", "Ok");
            } catch (EmailException e)
            {
                lblError.setText(e.getLocalizedMessage());
                txfContactPersoon.getStyleClass().add("errorField");
            } catch (Exception e)
            {
                lblError.setText(e.getLocalizedMessage());
            }
        }
    }

    private <E> boolean invoerControle()
    {
        Map<String, E> data = new HashMap<>();
        data.put("naam", (E) txfNaam);
        data.put("aantal", (E) txfAantal);
        data.put("aantalonbeschikbaar", (E) txfOnbeschikbaar);
        data.put("doelgroepen", (E) listDoelgroep);
        data.put("leergebieden", (E) listLeergbedied);
        data.put("prijs", (E) txfPrijs);
        data.put("artikelnummer", (E) txfArtikelNummer);
        data.put("contact", (E) txfContactPersoon);
        data.put("label", (E) lblError);
        return MateriaalHulpController.controleerInvoer(data);
    }

    @FXML
    private void terugNaarOverzicht(ActionEvent event)
    {
        boolean result = LoaderSchermen.getInstance().popupMessageTwoButtons("Terug naar overzicht", "Wilt u terug gaan naar het overzicht?", "Ja", "Nee");
        if (result)
        {
            BorderPane bp = (BorderPane) this.getParent();
            LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController());
        }

    }

    @FXML
    private void voegFotoToe(ActionEvent event)
    {
        Stage stage = (Stage) this.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
        {
            foto = file.getAbsolutePath();
            if (foto != null && foto.isEmpty())
            {
                imgView.setImage(SwingFXUtils.toFXImage(HulpMethode.convertUrlToImage(foto), null));
            }
        }
    }

    @FXML
    private void nieuweDoelgroep(ActionEvent event)
    {
        String doelgroep = MateriaalHulpController.textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
        if (checkDoelgroepen.getItems().contains(doelgroep))
        {
            lblError.setText("Deze doelgroep bestaat al!");
        }
        if (!doelgroep.isEmpty() && !doelgroep.trim().isEmpty() && !checkDoelgroepen.getItems().contains(doelgroep))
        {
            checkDoelgroepen = MateriaalHulpController.nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
            MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
            gp.add(checkDoelgroepen, 1, 5);
            gebiedenController.voegNieuwGebiedToe(doelgroep, d);
        }
    }

    @FXML
    private void nieuwLeergebied(ActionEvent event)
    {
        String leergebied = MateriaalHulpController.textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
        if (checkLeergebieden.getItems().contains(leergebied))
        {
            lblError.setText("Dit leergebied bestaat al!");
        }
        if (!leergebied.isEmpty() && !leergebied.trim().isEmpty() && !checkLeergebieden.getItems().contains(leergebied))
        {
            checkLeergebieden = MateriaalHulpController.nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
            MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
            gp.add(checkLeergebieden, 3, 5);
            gebiedenController.voegNieuwGebiedToe(leergebied, l);
        }
    }

    @FXML
    private void comboFirmaOnClick(ActionEvent event)
    {
        if (comboFirma.getSelectionModel().getSelectedItem() != null)
        {

            String naam = comboFirma.getSelectionModel().getSelectedItem();
            if (naam.equals("-- geen firma --"))
            {
                txfContactPersoon.setDisable(true);
                txfContactPersoon.setText("");
            } else
            {
                txfContactPersoon.setDisable(false);
                Firma f = firmaController.geefFirma(naam);
                comboFirma.setPromptText(comboFirma.getSelectionModel().getSelectedItem());
                comboFirma.setValue(comboFirma.getSelectionModel().getSelectedItem());
                txfContactPersoon.setText(f.getEmailContact());
            }

        }
    }

    @FXML
    private void btnNieuweFirma(ActionEvent event)
    {
        String[] firma = MateriaalHulpController.inputDialogFirma();
        if (firma != null)
        {
            String firmaNaam = firma[0];
            String contactFirma = firma[1];
            if (comboFirma.getItems().contains(firmaNaam))
            {
                lblError.setText("Deze firma bestaat al!");
            } else
            {
                firmaController.voegFirmaToe(firmaNaam, contactFirma);
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

    @FXML
    private void deleteDoelGroep(ActionEvent event)
    {
        openDeleteScherm(gebiedenController.geefAlleGebieden(new Doelgroep("d")), "Doelgroepen", 'd');

    }

    @FXML
    private void deleteLeergebied(ActionEvent event)
    {
        openDeleteScherm(gebiedenController.geefAlleGebieden(new Leergebied("l")), "Leergebieden", 'l');

    }

    public List<String> getCheckedGebieden(String type)
    {
        List<String> list = new ArrayList<>();
        switch (type)
        {
            case "d":
                checkDoelgroepen.getCheckModel().getCheckedItems().forEach(item -> list.add(item));
                break;
            case "l":
                checkLeergebieden.getCheckModel().getCheckedItems().forEach(item -> list.add(item));
                break;
        }
        return list;
    }

    public void checkItems(List<String> items, String type)
    {
        switch (type)
        {
            case "d":
            {
                items.forEach(i -> checkDoelgroepen.getCheckModel().check(i));
                listDoelgroep.setItems(FXCollections.observableArrayList(items));
                break;
            }
            case "l":
            {
                items.forEach(i -> checkLeergebieden.getCheckModel().check(i));
                listLeergbedied.setItems(FXCollections.observableArrayList(items));
                break;
            }
        }
    }

    @FXML
    private void deleteFirma(ActionEvent event)
    {
        openDeleteScherm(firmaController.geefAlleFirmas(), "Firma's", 'f');
    }

    private void openDeleteScherm(List<String> items, String lblName, char code)
    {
        Stage newStage = new Stage();
        newStage.setTitle("Verwijderen");
        newStage.setScene(new Scene(new DeleteSchermController(this, items, lblName, code)));
        newStage.show();
    }

    @FXML
    private void verwijderImage(ActionEvent event)
    {
        foto = "";
        imgView.setImage(new Image("images/add.png"));
    }
}
