package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by donovandesmedt on 23/04/16.
 */
public class FirmaDialog {
    private String textName = "";
    private String textContact = "";
    private String[] uitvoer;
    private boolean flag;
    public String[] getFirma(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nieuwe firma");
        dialog.setHeaderText("Nieuwe firma toevoegen");

// Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(new Image("images/firma.jpg")));

// Set the button types.
        ButtonType nieuwButtonType = new ButtonType("Nieuw", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(nieuwButtonType, ButtonType.CANCEL);

        String css = this.getClass().getResource("/styleSheet/style.css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(css);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Firmanaam");
        TextField contact = new TextField();
        contact.setPromptText("E-mailadres contactpersoon");

        grid.add(new Label("Firmanaam:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("E-mailadres:"), 0, 1);
        grid.add(contact, 1, 1);

        Label lblErrorNaam = new Label("Voer een firmanaam in");
        Label lblErrorContact = new Label("Voer een geldig emailadres in");
        lblErrorNaam.getStyleClass().add("label-error");
        lblErrorNaam.setVisible(false);
        lblErrorContact.getStyleClass().add("label-error");
        lblErrorContact.setVisible(false);
        grid.add(lblErrorNaam, 1, 2);
        grid.add(lblErrorContact, 1, 3);

// Enable/Disable login button depending on whether a username was entered.
        Node nieuwButton = dialog.getDialogPane().lookupButton(nieuwButtonType);
        nieuwButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            textName = newValue.trim();
            flag = !textName.isEmpty() && !textContact.isEmpty();
            if(textName.isEmpty()){
                lblErrorNaam.setVisible(true);
                name.getStyleClass().add("errorField");
            }
            else{
                lblErrorNaam.setVisible(false);
                name.getStyleClass().remove("errorField");
            }
            nieuwButton.setDisable(!flag);
        });
        contact.textProperty().addListener((observable, oldValue, newValue) -> {
            textContact = newValue.trim();
            flag = !textName.isEmpty() && Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", textContact);
            if(!Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", textContact)) {
                lblErrorContact.setVisible(true);
                contact.getStyleClass().add("errorField");
            }
            else{
                contact.getStyleClass().remove("errorField");
                lblErrorContact.setVisible(false);

            }
            nieuwButton.setDisable(!flag);
        });
        dialog.getDialogPane().setContent(grid);


// Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == nieuwButtonType) {
                return new Pair<>(name.getText(), contact.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(gegevens -> {
            uitvoer =  new String[]{gegevens.getKey(),gegevens.getValue()};
        });
        return uitvoer;
    }
}
