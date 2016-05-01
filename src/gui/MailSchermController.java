/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.ControllerSingelton;
import controller.MailController;
import domein.MailTemplate;
import domein.Materiaal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import repository.MailRepository;

/**
 * FXML Controller class
 *
 * @author manu
 */
public class MailSchermController extends HBox {

    @FXML
    private HTMLEditor editor;

    private List<MailTemplate> mails;

    private MailController mailController;
    @FXML
    private ListView<String> listViewMail;

    private MailTemplate currentMail;
    @FXML
    private Button btnWijzig;
    private List<String> namen;
    private String woordVerplicht;
    @FXML
    private ListView<String> listviewVerplichteItems;
    @FXML
    private ImageView btnTooltip;

    public MailSchermController() {
        LoaderSchermen.getInstance().setLocation("MailScherm.fxml", this);
        this.mailController = ControllerSingelton.getMailControllerInstance();
        mails = mailController.geefAlleMails();
       
        if (mails == null) {
            namen = new ArrayList<>();
        } else {
            namen = mails.stream().map(b -> b.getOnderwerp()).collect(Collectors.toList());
        }
        
        Tooltip t=new Tooltip("Deze items moeten verplicht aanwezig zijn in de mail. De naam duidt op waarmee het wordt ingevuld. Deze moeten ook in het vet geschreven worden!");
        listviewVerplichteItems.setTooltip(t);
        editor.setDisable(true);
        btnWijzig.setDisable(true);
        selectListViewListener();
        listViewMail.setItems(FXCollections.observableArrayList(namen));
        listviewVerplichteItems.setEditable(false);

    }

    private void selectListViewListener() {
        listViewMail.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    if (oldValue == null || !oldValue.equals(newValue)) {
                        editor.setDisable(false);
                        btnWijzig.setDisable(false);

                        currentMail = mails.stream().filter(m -> m.getOnderwerp().equals(newValue)).findFirst().get();
                        editor.setHtmlText(currentMail.getBody());
                        setVerplichteItems();

                    }
                }
            }
        });
    }

    @FXML
    private void btnWijzigOnAction(ActionEvent event) {

        if (mailVoldoetAanVerplichteItems()) {
            boolean result = LoaderSchermen.getInstance().popupMessageTwoButtons("Wijzig mail", "Bent u zeker dat u de wijzigingen wilt doorvoeren voor volgende mail:" + currentMail.getOnderwerp(), "Ja", "Nee");
            if (result) {
                MailTemplate mail = mails.stream().filter(m -> m.getOnderwerp().equals(currentMail.getOnderwerp())).findFirst().get();
                mail.setBody(editor.getHtmlText());
                mailController.wijzigMail(mail);
            } else {
                editor.setHtmlText(currentMail.getBody());
            }

        }
        else{
            LoaderSchermen.getInstance().popupMessageOneButton("Fout in mail", "Er ontbreekt een verplicht item in de gewijzigde mail: " + woordVerplicht +"!" +" Let op: maak dat alles in het vet staat!", "Ok");
        }

    }

    private void setVerplichteItems() {
        switch (currentMail.getOnderwerp()) {
            case "Bevestiging reservatie":
                listviewVerplichteItems.setItems(FXCollections.observableArrayList("_NAAM", "_STARTDATUM", "_EINDDATUM", "_ITEMS"));
                break;
            case "Blokkering":
                listviewVerplichteItems.setItems(FXCollections.observableArrayList("_NAAM", "_DATUMS", "_ITEMS"));
                break;
            case "Reservatie gewijzigd":
                listviewVerplichteItems.setItems(FXCollections.observableArrayList("_NAAM", "_STARTDATUM", "_ITEMS"));
                break;
        }

    }

    private boolean mailVoldoetAanVerplichteItems() {
        String tekst = editor.getHtmlText();
        for (String woord : listviewVerplichteItems.getItems()) {
            if (!tekst.contains(woord)) {
                woordVerplicht=woord;
                return false;
                
            }
        }
        return true;
    }


    

}
