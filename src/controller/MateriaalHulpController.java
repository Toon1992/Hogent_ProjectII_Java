package controller;

import gui.FirmaDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import org.controlsfx.control.CheckComboBox;
import domein.MateriaalCatalogus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by donovandesmedt on 13/04/16.
 */
public class MateriaalHulpController {
    public static void linkComboboxListView(ListView<String> listView, CheckComboBox<String> check, MateriaalFilter filter) {
        listView.getItems().stream().forEach(item -> {
            check.getCheckModel().check(item);
        });
        checkcomboboxListener(check, listView, filter);
    }

    private static <E> void checkcomboboxListener(CheckComboBox<E> check,ListView<String> listView, MateriaalFilter filter) {
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
    public static CheckComboBox<String> nieuwItemListView(CheckComboBox<String> check, ListView listView, String item){
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

    public static String textInputDialog(String title, String header, String content){
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
    public static String[] inputDialogFirma(){
        return new FirmaDialog().getFirma();
    }
}
