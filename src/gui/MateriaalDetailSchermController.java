///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package gui;
//
//import controller.ControllerSingelton;
//import controller.MateriaalController;
//import controller.MateriaalHulpController;
//import domein.*;
//
//import java.io.File;
//import java.util.*;
//
//import exceptions.EmailException;
//import exceptions.NaamException;
//import javafx.collections.FXCollections;
//import javafx.embed.swing.SwingFXUtils;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import org.controlsfx.control.CheckComboBox;
//import repository.FirmaRepository;
//import repository.GebiedenRepository;
//import domein.MateriaalCatalogus.*;
//
///**
// * FXML Controller class
// *
// * @author donovandesmedt
// */
//public class MateriaalDetailSchermController extends VBox {
//
//    @FXML
//    private ImageView imgViewMateriaal;
//    @FXML
//    private TextArea txfOmschrijving;
//    @FXML
//    private TextField txfArtikelNummer;
//    @FXML
//    private TextField txfAantal;
//    @FXML
//    private TextField txfOnbeschikbaar;
//    @FXML
//    private TextField txfContactPersoon;
//    @FXML
//    private RadioButton radioStudent;
//    @FXML
//    private RadioButton radioLector;
//    @FXML
//    private TextField txfPlaats;
//    @FXML
//    private TextField txfPrijs;
//    @FXML
//    private Button btnOpslaan;
//    @FXML
//    private ListView<String> listDoelgroep;
//    @FXML
//    private ListView<String> listLeergbedied;
//    @FXML
//    private TextField txfNaam;
//    @FXML
//    private Button btnTerug;
//    private MateriaalController mc;
//    private Materiaal materiaal;
//    private ToggleGroup group = new ToggleGroup();
//    @FXML
//    private Label lblErrorMessage;
//    @FXML
//    private ComboBox<String> comboFirma;
//    private GridPane gp;
//    private GebiedenRepository gebiedenRepo;
//    private FirmaRepository firmaRepo;
//    private CheckComboBox<String> checkDoelgroepen;
//    private CheckComboBox<String> checkLeergebieden;
//    private FileChooser fileChooser;
//    private Leergebied l = new Leergebied("l");
//    Doelgroep d = new Doelgroep("d");
//    private String foto = "EMPTY";
//
//    public MateriaalDetailSchermController(Materiaal materiaal) {
//        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml", this);
//        this.materiaal = materiaal;
//        this.mc = ControllerSingelton.getMateriaalControllerInstance();
//        initializeItems();
//    }
//
//    private void initializeItems() {
//        gebiedenRepo = new GebiedenRepository();
//        firmaRepo = new FirmaRepository();
//
//        fileChooser = new FileChooser();
//        fileChooser.setTitle("Kies een foto");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
//                new FileChooser.ExtensionFilter("GIF", "*.gif"),
//                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
//                new FileChooser.ExtensionFilter("PNG", "*.png"));
//
//        update(materiaal);
//
//        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(d)));
//        checkDoelgroepen.setMaxWidth(200);
//
//        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList(gebiedenRepo.geefAlleGebieden(l)));
//        checkLeergebieden.setMaxWidth(200);
//
//        List<String> firmas = firmaRepo.geefAlleFirmas();
//        firmas.add("-- geen firma --");
//        comboFirma.setItems(FXCollections.observableArrayList(firmas));
//
//        gp = (GridPane) this.getChildren().get(0);
//        gp.add(checkDoelgroepen, 1, 6);
//        gp.add(checkLeergebieden, 3, 6);
//        MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
//        MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
//
//    }
//
//    @FXML
//    private void wijzigFoto(ActionEvent event) {
//        Stage stage = (Stage) this.getScene().getWindow();
//        File file = fileChooser.showOpenDialog(stage);
//        foto = file.getAbsolutePath();
//        imgViewMateriaal.setImage(SwingFXUtils.toFXImage(HulpMethode.convertUrlToImage(foto), null));
//    }
//
//    @FXML
//    private void verwijderFoto(ActionEvent event) 
//    {
//        foto = "";
//        imgViewMateriaal.setImage(new Image("images/add.png"));
//    }
//
//    @FXML
//    private void materiaalWijzigen(ActionEvent event) {
//        if (invoerControle()) {
//            try {
//                mc.controleerUniekheidMateriaalnaam(materiaal, txfNaam.getText());
//
//                materiaal.setNaam(txfNaam.getText());
//                materiaal.setOmschrijving(txfOmschrijving.getText());
//                int aantal = Integer.parseInt(txfAantal.getText());
//                int aantalOnbeschikbaar = Integer.parseInt(txfOnbeschikbaar.getText());
//                materiaal.setAantal(aantal);
//                materiaal.setAantalOnbeschikbaar(aantalOnbeschikbaar);
//                materiaal.setPlaats(txfPlaats.getText());
//                materiaal.setDoelgroepen(gebiedenRepo.geefGebiedenVoorNamen(listDoelgroep.getItems(), d));
//                materiaal.setLeergebieden(gebiedenRepo.geefGebiedenVoorNamen(listLeergbedied.getItems(), l));
//                materiaal.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
//                String prijs = txfPrijs.getText().replace(",", ".");
//                materiaal.setPrijs(Double.valueOf(prijs));
//                materiaal.setIsReserveerbaar(radioStudent.isSelected());
//
//                //firma maken
//                if (comboFirma.getValue() != null && !comboFirma.getValue().equals("-- geen firma --")) {
//                    Firma f = firmaRepo.geefFirma(comboFirma.getValue()); // omdat als het al gewijzigd is dan kan je nooit opvragen
//                    f.setEmailContact(txfContactPersoon.getText());
//                    materiaal.setFirma(f);
//                    firmaRepo.wijzigFirma(f);
//                }
//                if (comboFirma.getValue().equals("-- geen firma --")) {
//                    materiaal.setFirma(null);
//                }
//                if (!foto.equals("EMPTY")) {
//                    materiaal.setFoto(foto);
//                }
//
//                mc.wijzigMateriaal(materiaal);
//                lblErrorMessage.setText("");
//                LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd : " + materiaal.getNaam(), "Al uw wijzigingen zijn correct doorgevoerd", "Ok");
//                terugNaarOverzicht(null);
//            } catch (EmailException ex) {
//                txfContactPersoon.getStyleClass().add("errorField");
//                lblErrorMessage.setText(ex.getLocalizedMessage());
//            } catch (NaamException e) {
//                txfNaam.getStyleClass().add("errorField");
//                lblErrorMessage.setText(e.getLocalizedMessage());
//            } catch (Exception ex) {
//                lblErrorMessage.setText(ex.getMessage());
//            }
//        }
//    }
//
//    private <E> boolean invoerControle() {
//        Map<String, E> data = new HashMap<>();
//        data.put("naam", (E) txfNaam);
//        data.put("aantal", (E) txfAantal);
//        data.put("aantalonbeschikbaar", (E) txfOnbeschikbaar);
//        data.put("doelgroepen", (E) listDoelgroep);
//        data.put("leergebieden", (E) listLeergbedied);
//        data.put("prijs", (E) txfPrijs);
//        data.put("artikelnummer", (E) txfArtikelNummer);
//        data.put("contact", (E) txfContactPersoon);
//        data.put("label", (E) lblErrorMessage);
//        return MateriaalHulpController.controleerInvoer(data);
//    }
//
//    public void update(Materiaal materiaal) {
//        if (materiaal.getFoto() != null) {
//            imgViewMateriaal.setImage(SwingFXUtils.toFXImage(materiaal.getFoto(), null));
//        } else {
//            imgViewMateriaal.setImage(new Image("images/add.png"));
//        }
//        txfAantal.setText(String.format("%d", materiaal.getAantal()));
//        txfArtikelNummer.setText(String.format("%d", materiaal.getArtikelNr()));
//        if (materiaal.getFirma() != null) {
//            txfContactPersoon.setText(materiaal.getFirma().getEmailContact());
//            comboFirma.setPromptText(materiaal.getFirma().getNaam());
//            comboFirma.setValue(materiaal.getFirma().getNaam());
//            txfContactPersoon.setDisable(false);
//        } else {
//            comboFirma.setValue("-- geen firma --");
//            txfContactPersoon.setText("");
//            txfContactPersoon.setDisable(true);
//        }
//        listDoelgroep.setItems(mc.objectCollectionToObservableList(materiaal.getDoelgroepen()).sorted());
//        listLeergbedied.setItems(mc.objectCollectionToObservableList(materiaal.getLeergebieden()).sorted());
//
//        txfNaam.setText(materiaal.getNaam());
//        txfOmschrijving.setText(materiaal.getOmschrijving());
//        txfOnbeschikbaar.setText(String.format("%d", materiaal.getAantalOnbeschikbaar()));
//        txfPlaats.setText(materiaal.getPlaats());
//        txfPrijs.setText(String.format("%.2f", materiaal.getPrijs()));
//        radioStudent.setToggleGroup(group);
//        radioStudent.setSelected(materiaal.getIsReserveerbaar());
//        radioLector.setSelected(!materiaal.getIsReserveerbaar());
//        radioLector.setToggleGroup(group);
//
//    }
//
//    @FXML
//    private void terugNaarOverzicht(ActionEvent event) {
//
//        boolean result = LoaderSchermen.getInstance().popupMessageTwoButtons("Terug naar overzicht", "Wilt u terug gaan naar het overzicht?", "Ja", "Nee");
//        if (result) {
//            BorderPane bp = (BorderPane) this.getParent();
//            LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, (HBox) LoaderSchermen.getInstance().getNode());
//        }
//
//    }
//
//    @FXML
//    private void nieuwLeergebied(ActionEvent event) {
//        String leergebied = MateriaalHulpController.textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
//        if (checkLeergebieden.getItems().contains(leergebied)) {
//            lblErrorMessage.setText("Dit leergebied bestaat al!");
//        }
//        if (!leergebied.isEmpty() && !leergebied.trim().isEmpty() && !checkLeergebieden.getItems().contains(leergebied)) {
//            checkLeergebieden = MateriaalHulpController.nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
//            MateriaalHulpController.linkComboboxListView(listLeergbedied, checkLeergebieden, MateriaalFilter.LEERGEBIED);
//            gp.add(checkLeergebieden, 3, 6);
//            gebiedenRepo.voegNieuwGebiedToe(leergebied, l);
//        }
//
//    }
//
//    @FXML
//    private void nieuweDoelgroep(ActionEvent event) {
//        String doelgroep = MateriaalHulpController.textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
//        if (checkDoelgroepen.getItems().contains(doelgroep)) {
//            lblErrorMessage.setText("Deze doelgroep bestaat al!");
//        }
//        if (!doelgroep.isEmpty() && !doelgroep.trim().isEmpty() && !checkDoelgroepen.getItems().contains(doelgroep)) {
//            checkDoelgroepen = MateriaalHulpController.nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
//            MateriaalHulpController.linkComboboxListView(listDoelgroep, checkDoelgroepen, MateriaalFilter.DOELGROEP);
//            gp.add(checkDoelgroepen, 1, 6);
//            gebiedenRepo.voegNieuwGebiedToe(doelgroep, d);
//        }
//
//    }
//
//    @FXML
//    private void comboFirmaOnClick(ActionEvent event) {
//        if (comboFirma.getSelectionModel().getSelectedItem() != null) {
//            String firmaNaam = comboFirma.getSelectionModel().getSelectedItem();
//            if (firmaNaam.equals("-- geen firma --")) {
//                txfContactPersoon.setDisable(true);
//                txfContactPersoon.setText("");
//            } else {
//                txfContactPersoon.setDisable(false);
//                List<String> firmas = comboFirma.getItems();
//                if (firmas.contains(comboFirma.getSelectionModel().getSelectedItem())) {
//                    String naam = comboFirma.getSelectionModel().getSelectedItem();
//                    Firma f = firmaRepo.geefFirma(naam);
//                    comboFirma.setPromptText(comboFirma.getSelectionModel().getSelectedItem());
//                    txfContactPersoon.setText(f.getEmailContact());
//                }
//            }
//        }
//    }
//
//    @FXML
//    private void btnNieuweFirma(ActionEvent event) {
//        String[] firma = MateriaalHulpController.inputDialogFirma();
//        if (firma != null) {
//            String firmaNaam = firma[0];
//            String contactFirma = firma[1];
//            if (comboFirma.getItems().contains(firmaNaam)) {
//                lblErrorMessage.setText("Deze firma bestaat al!");
//            } else {
//                firmaRepo.voegFirmaToe(firmaNaam, contactFirma);
//                List<String> firmas = new ArrayList<>();
//                firmas.addAll(comboFirma.getItems());
//                firmas.add(firmaNaam);
//                comboFirma.setItems(FXCollections.observableArrayList(firmas));
//                comboFirma.setPromptText(firmaNaam);
//                comboFirma.setValue(firmaNaam);
//                txfContactPersoon.setText(contactFirma);
//
//            }
//        }
//
//    }
//
//}
