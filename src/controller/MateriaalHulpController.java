package controller;

import gui.FirmaDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
import domein.MateriaalCatalogus.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by donovandesmedt on 13/04/16.
 */
public class MateriaalHulpController {

    public String getCSS(){
        return this.getClass().getResource("/styleSheet/dialog.css").toExternalForm();
    }
    public static void linkComboboxListView(ListView<String> listView, CheckComboBox<String> check, MateriaalFilter filter) {
        listView.getItems().stream().forEach(item ->
        {
            check.getCheckModel().check(item);
        });
        checkcomboboxListener(check, listView, filter);
        if (check.getCheckModel().getCheckedItems().isEmpty())
            listView.setItems(FXCollections.observableArrayList());
    }

    private static <E> void checkcomboboxListener(CheckComboBox<E> check, ListView<String> listView, MateriaalFilter filter) {
        check.getCheckModel().getCheckedItems().addListener(new ListChangeListener<E>() {
            public void onChanged(ListChangeListener.Change<? extends E> c) {
                switch (filter) {
                    case DOELGROEP:
                        itemsListViewWijzigen(check.getCheckModel().getCheckedItems().stream().map(e -> e.toString()).collect(Collectors.toList()), listView);
                        break;
                    case LEERGEBIED:
                        itemsListViewWijzigen(check.getCheckModel().getCheckedItems().stream().map(e -> e.toString()).collect(Collectors.toList()), listView);
                        break;
                }
            }
        });
    }

    private static void itemsListViewWijzigen(List<String> items, ListView<String> listView) {
        List<String> nieuweList = new ArrayList<>();
        items.stream().forEach(item -> nieuweList.add(item));
        listView.setItems(new FilteredList<String>(FXCollections.observableArrayList(nieuweList), p -> true));
    }

    public static CheckComboBox<String> nieuwItemListView(CheckComboBox<String> check, ListView listView, String item) {
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
        check.setMaxWidth(200);
        return check;
    }

    public static String textInputDialog(String title, String header, String content) {
        StringBuilder uitvoer = new StringBuilder();
        TextInputDialog dialog = new TextInputDialog();
        String css = new MateriaalHulpController().getCSS();
        dialog.getDialogPane().getStylesheets().add(css);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.showAndWait().ifPresent(response ->
        {
            if (!response.isEmpty()) {
                uitvoer.append(response);
            }
        });
        return uitvoer.toString();
    }

    public static <E> boolean controleerInvoer(Map<String, E> data) {
        int aantal = 0;
        Map<String, E> fouteWaarden = new HashMap<>();
        String naam = ((TextField) data.get("naam")).getText();
        if (naam.trim().isEmpty()) {
            fouteWaarden.put("naam", data.get("naam"));
        }
        try {
            aantal = Integer.parseInt(((TextField) data.get("aantal")).getText());
            if (aantal < 0) {
                fouteWaarden.put("aantal", data.get("aantal"));
            }
        } catch (Exception e) {
            fouteWaarden.put("aantal", data.get("aantal"));
        }
        try {
            if (((ListView) data.get("leergebieden")).getItems().isEmpty()) {
                fouteWaarden.put("leergebieden", data.get("leergebieden"));
            }
        } catch (Exception e) {
            fouteWaarden.put("leergebieden", data.get("leergebieden"));
        }
        try {
            if (((ListView) data.get("doelgroepen")).getItems().isEmpty()) {
                fouteWaarden.put("doelgroepen", data.get("doelgroepen"));
            }
        } catch (Exception e) {
            fouteWaarden.put("doelgroepen", data.get("doelgroepen"));
        }
        if (!((TextField) data.get("aantalonbeschikbaar")).getText().trim().isEmpty()) {
            try {
                int aantalOnbeschikbaar = Integer.parseInt(((TextField) data.get("aantalonbeschikbaar")).getText());
                if (aantalOnbeschikbaar < 0 || aantalOnbeschikbaar > aantal) {
                    fouteWaarden.put("aantalonbeschikbaar", data.get("aantalonbeschikbaar"));
                }
            } catch (Exception e) {
                fouteWaarden.put("aantalonbeschikbaar", data.get("aantalonbeschikbaar"));
            }
        }
        if (!((TextField) data.get("artikelnummer")).getText().trim().isEmpty()) {
            try {
                int artikelnr = Integer.parseInt(((TextField) data.get("artikelnummer")).getText());
                if (artikelnr < 0) {
                    fouteWaarden.put("artikelnummer", data.get("artikelnummer"));
                }
            } catch (Exception e) {
                fouteWaarden.put("artikelnummer", data.get("artikelnummer"));
            }
        }
        if (!((TextField) data.get("prijs")).getText().trim().isEmpty()) {
            try {
                String prijsString = ((TextField) data.get("prijs")).getText().replace(",", ".");
                double prijs = Double.valueOf(prijsString);
                if (prijs < 0) {
                    fouteWaarden.put("prijs", data.get("prijs"));
                }
            } catch (Exception e) {
                fouteWaarden.put("prijs", data.get("prijs"));
            }
        }

        data.entrySet().stream().forEach(entry ->
        {
            if (entry.getKey().equals("doelgroepen")) {
                ((ListView) data.get("doelgroepen")).getStyleClass().remove("errorField");
            } else if (entry.getKey().equals("leergebieden")) {
                ((ListView) data.get("leergebieden")).getStyleClass().remove("errorField");
            } else if (entry.getKey().equals("label")) {
                ((Label) data.get("label")).getStyleClass().remove("errorField");
            } else {
                ((TextField) entry.getValue()).getStyleClass().remove("errorField");
            }
            ((Label) data.get("label")).setText("");
        });
        if (!fouteWaarden.entrySet().isEmpty()) {
            fouteWaarden.entrySet().stream().forEach(entry ->
            {
                if (entry.getKey().equals("doelgroepen")) {
                    ((ListView) data.get("doelgroepen")).getStyleClass().add("errorField");
                } else if (entry.getKey().equals("leergebieden")) {
                    ((ListView) data.get("leergebieden")).getStyleClass().add("errorField");
                } else if (entry.getKey().equals("label")) {
                    ((Label) data.get("label")).getStyleClass().add("errorField");
                } else {
                    ((TextField) entry.getValue()).getStyleClass().add("errorField");
                }
            });
            ((Label) data.get("label")).setText("Niet alle invoervelden zijn correct ingevuld");
        }
        return fouteWaarden.entrySet().isEmpty();
    }

    public static String[] inputDialogFirma() {
        return new FirmaDialog().getFirma();
    }
}
