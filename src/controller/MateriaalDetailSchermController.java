/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;

import java.util.*;
import java.util.stream.Collectors;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    private GridPane gp;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;
    private CheckComboBox<String> checkDoelgroepen;
    private CheckComboBox<String> checkLeergebieden;
    private Leergebied l = new Leergebied("l");
    Doelgroep d = new Doelgroep("d");

    public MateriaalDetailSchermController(MateriaalController mc, Materiaal materiaal) {
        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml", this);
        this.materiaal = materiaal;
        this.mc = mc;
        initializeItems();
    }

    private void initializeItems(){
        gebiedenRepo = new GebiedenRepository();
        firmaRepo=new FirmaRepository();

        update(materiaal);

        checkDoelgroepen = new CheckComboBox<>(FXCollections.observableArrayList( "Kleuter onderwijs", "Lager onderwijs", "Secundair onderwijs"));
        checkDoelgroepen.setMaxWidth(150);

        checkLeergebieden = new CheckComboBox<>(FXCollections.observableArrayList("Mens", "Maatschappij", "Geschiedenis", "Wetenschap", "Biologie", "Fysica", "Techniek", "Wiskunde", "Aardrijkskunde"));
        checkLeergebieden.setMaxWidth(150);

        gp = (GridPane) this.getChildren().get(0);
        gp.add(checkDoelgroepen,1,4);
        gp.add(checkLeergebieden,3, 4);
        linkComboboxListView();

    }
    private void linkComboboxListView(){
        listLeergbedied.getItems().stream().forEach(item -> {
            checkLeergebieden.getCheckModel().check(item);
        });
        listDoelgroep.getItems().stream().forEach(item -> {
            checkDoelgroepen.getCheckModel().check(item);
        });
        checkcomboboxListener(checkDoelgroepen, MateriaalFilter.DOELGROEP);
        checkcomboboxListener(checkLeergebieden, MateriaalFilter.LEERGEBIED);
    }
    @FXML
    private void wijzigFoto(MouseEvent event) {
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
            Firma f=firmaRepo.geefFirma(materiaal.getFirma().getNaam()); // omdat als het al gewijzigd is dan kan je nooit opvragen
            f.setNaam(txfFirma.getText());
            f.setEmailContact(txfContactPersoon.getText());
            
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
        txfFirma.setText(materiaal.getFirma().getNaam());
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
        String leergebied = textInputDialog("Nieuwe leergebied", "Voeg een nieuw leergebied toe", "Voeg naam in:");
        if(!leergebied.isEmpty()&&!checkLeergebieden.getItems().contains(leergebied)){
            checkLeergebieden = nieuwItemListView(checkLeergebieden, listLeergbedied, leergebied);
            linkComboboxListView();
            gp.add(checkLeergebieden,3, 4);
            gebiedenRepo.voegNieuwGebiedToe(leergebied,l);
        }

    }
    @FXML
    private void nieuweDoelgroep(ActionEvent event){
        String doelgroep = textInputDialog("Nieuwe doelgroep", "Voeg een nieuwe doelgroep toe", "Voeg naam in:");
        if(!doelgroep.isEmpty()&&!checkDoelgroepen.getItems().contains(doelgroep)){
            checkDoelgroepen = nieuwItemListView(checkDoelgroepen, listDoelgroep, doelgroep);
            linkComboboxListView();
            gp.add(checkDoelgroepen,1,4);
            gebiedenRepo.voegNieuwGebiedToe(doelgroep,d);
        }

    }
    private CheckComboBox<String> nieuwItemListView(CheckComboBox<String> check, ListView listView, String item){
        List<String> items = new ArrayList<String>();
        //De het nieuwe item + geselecteerde items van de combobox zullen in de listview worden geplaatst.
        check.getCheckModel().getCheckedItems().stream().forEach(e -> items.add(e));
        items.add(item);
        itemsListViewWijzigen(items, listView);

        //Het nieuwe item aan de combobox toevoegen
        List<String> nieuweItems = new ArrayList<>();
        //alle eerdere items van de combobox aan de lijst toevoegen
        check.getItems().stream().forEach(e -> nieuweItems.add(e));
        nieuweItems.add(item);

        check = new CheckComboBox<String>(FXCollections.observableArrayList(nieuweItems));
        check.setMaxWidth(150);
        return check;
    }

    private String textInputDialog(String title, String header, String content){
        StringBuilder uitvoer = new StringBuilder();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.showAndWait().ifPresent(response -> {
            if (!response.isEmpty()) {
                uitvoer.append(response);
            }
        });
        return uitvoer.toString();
    }
    private <E> void checkcomboboxListener(CheckComboBox<E> check, MateriaalFilter filter){
        check.getCheckModel().getCheckedItems().addListener(new ListChangeListener<E>() {
            public void onChanged(ListChangeListener.Change<? extends E> c) {
                switch (filter){
                    case DOELGROEP:
                        itemsListViewWijzigen(check.getCheckModel().getCheckedItems().stream().map(e -> e.toString()).collect(Collectors.toList()), listDoelgroep);
                        break;
                    case LEERGEBIED:
                        itemsListViewWijzigen(check.getCheckModel().getCheckedItems().stream().map(e -> e.toString()).collect(Collectors.toList()), listLeergbedied);
                        break;
                }
            }
        });
    }
    private void itemsListViewWijzigen(List<String> items, ListView<String> listView){
        List<String> nieuweList = new ArrayList<>();
        items.stream().forEach(item ->nieuweList.add(item));
        listView.setItems(new FilteredList<String>(FXCollections.observableArrayList(nieuweList), p -> true));
    }
}
