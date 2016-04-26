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

    public MailSchermController() {
        LoaderSchermen.getInstance().setLocation("MailScherm.fxml", this);
        this.mailController = ControllerSingelton.getMailControllerInstance();
        mails = mailController.geefAlleMails();

        editor.setDisable(true);
        btnWijzig.setDisable(true);
        selectListViewListener();
        List<String> namen = mails.stream().map(b -> b.getOnderwerp()).collect(Collectors.toList());
        listViewMail.setItems(FXCollections.observableArrayList(namen));

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

                    }
                }
            }
        });
    }

    @FXML
    private void btnWijzigOnAction(ActionEvent event) {
        boolean result = LoaderSchermen.getInstance().popupMessageTwoButtons("Wijzig mail", "Bent u zeker dat u de wijzigingen wilt doorvoeren voor volgende mail:" + currentMail.getOnderwerp(), "Ja", "Nee");
        if (result) {
            MailTemplate mail = mails.stream().filter(m -> m.getOnderwerp().equals(currentMail.getOnderwerp())).findFirst().get();
            mail.setBody(editor.getHtmlText());

            mailController.wijzigMail(mail);
        }
        else
            editor.setHtmlText(currentMail.getBody());

    }

}
