/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import domein.Materiaal;
import gui.LoaderSchermen;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import repository.FirmaRepository;
import repository.GebiedenRepository;

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
    @FXML
    private TextField txfNieuwDoelgroep;
    @FXML
    private TextField txfNieuwLeergebied;
    private MateriaalController mc;
    private Materiaal materiaal;
    private ToggleGroup group = new ToggleGroup();
    @FXML
    private Label lblErrorMessage;
    private GebiedenRepository gebiedenRepo;
    private FirmaRepository firmaRepo;

    public MateriaalDetailSchermController(MateriaalController mc, Materiaal materiaal) {
        LoaderSchermen.getInstance().setLocation("MateriaalDetailScherm.fxml", this);
        this.materiaal = materiaal;
        this.mc = mc;
        gebiedenRepo = new GebiedenRepository();
        firmaRepo=new FirmaRepository();
        update(materiaal);
    }

    @FXML
    private void wijzigFoto(MouseEvent event) {
    }

    @FXML
    private void materiaalWijzigen(ActionEvent event) {
        try {
            Leergebied l = new Leergebied("l");
            Doelgroep d = new Doelgroep("d");
            //materiaal wijzigen
            materiaal.setNaam(txfNaam.getText());
            materiaal.setOmschrijving(txfOmschrijving.getText());
            materiaal.setAantal(Integer.parseInt(txfAantal.getText()));
            materiaal.setAantalOnbeschikbaar(Integer.parseInt(txfOnbeschikbaar.getText()));
            materiaal.setPlaats(txfPlaats.getText());
            if (!listDoelgroep.getSelectionModel().getSelectedItems().isEmpty()) {
                
                materiaal.setDoelgroepen(gebiedenRepo.geefGebiedenVoorNamen(listDoelgroep.getSelectionModel().getSelectedItems(), d));
            }
            if(!listLeergbedied.getSelectionModel().getSelectedItems().isEmpty()){
                materiaal.setLeergebieden(gebiedenRepo.geefGebiedenVoorNamen(listLeergbedied.getSelectionModel().getSelectedItems(), l));
            }

            materiaal.setArtikelNr(Integer.parseInt(txfArtikelNummer.getText()));
            String prijs = txfPrijs.getText().replace(",", ".");
            materiaal.setPrijs(Double.valueOf(prijs));

            materiaal.setIsReserveerbaar(radioStudent.isSelected());
            
            //firma
            Firma f=firmaRepo.geefFirma(materiaal.getFirma().getNaam()); // omdat als het al gewijzigd is dan kan je nooit opvragen
            f.setNaam(txfFirma.getText());
            f.setEmailContact(txfContactPersoon.getText());
            
            materiaal.setFirma(f); 
            firmaRepo.wijzigFirma(f);
            mc.wijzigMateriaal(materiaal);
            lblErrorMessage.setText("");
            LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd : " + materiaal.getNaam(),"Al uw wijzigingen zijn correct doorgevoerd", "Ok");
            

            materiaal.getFirma().setEmailContact(txfContactPersoon.getText());
            materiaal.setIsReserveerbaar(radioStudent.isSelected());
            lblErrorMessage.setText("");
            LoaderSchermen.getInstance().popupMessageOneButton("Materiaal gewijzigd" + materiaal.getNaam(), "Al uw wijzigingen zijn correct doorgevoerd!", "Ok");

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
        //LoaderSchermen.getInstance().setMateriaalOvezichtScherm(bp, new MateriaalOverzichtSchermController(mc));
    }
    @FXML
    private void voegLeergebiedToe(ActionEvent event){
        String leergebied = txfNieuwLeergebied.getText();
        if(!leergebied.isEmpty()){
            //voeg nieuw leergebied toe.
        }
    }
    @FXML
    private void voegDoelgroepToe(ActionEvent event){
        String doelgroep = txfNieuwDoelgroep.getText();
        if(!doelgroep.isEmpty()){
            //voeg nieuw leergebied toe.
        }

    }
}
