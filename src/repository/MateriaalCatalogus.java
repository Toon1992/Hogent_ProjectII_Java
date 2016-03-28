/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import domein.Materiaal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.MateriaalDaoJpa;

/**
 *
 * @author donovandesmedt
 */
public class MateriaalCatalogus
{

    private FilteredList<Materiaal> filteredmaterialen;
    private MateriaalDaoJpa materiaalDao;
    private ObservableList<Materiaal> filterMateriaal;

    public MateriaalCatalogus()
    {
        materiaalDao = new MateriaalDaoJpa();
    }

    public SortedList<Materiaal> geefMaterialen()
    {
        if (filteredmaterialen == null)
        {
            filterMateriaal = FXCollections.observableList(materiaalDao.findAll());
            filteredmaterialen = new FilteredList(filterMateriaal, p -> true);
        }
        return new SortedList<>(filteredmaterialen);
    }

    public void voegMateriaalToe(Materiaal materiaal)
    {
        materiaalDao.insert(materiaal);
        materiaalDao.commitTransaction();
        filteredmaterialen.add(materiaal);
    }

    public void wijzigMateriaal(Materiaal materiaal)
    {
        materiaalDao.startTransaction();
        materiaalDao.update(materiaal);
        materiaalDao.commitTransaction();

    }

    public void verwijderMateriaal(Materiaal materiaal)
    {
        materiaalDao.startTransaction();
        materiaalDao.delete(materiaal);
        materiaalDao.commitTransaction();
        filterMateriaal.remove(materiaal);
        filteredmaterialen = new FilteredList(filterMateriaal, p -> true);

    }

    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list)
    {
        List<String> stringLijst = list.stream().map(e -> e.toString()).collect(Collectors.toList());
        return FXCollections.observableArrayList(stringLijst);
    }

    public void zoek(String zoekterm)
    {
        filteredmaterialen.setPredicate(m -> 
                {
                    if (zoekterm == null || zoekterm.isEmpty())
                    {
                        return true;
                    }
                    if (m.getNaam().toLowerCase().contains(zoekterm.toLowerCase()) || m.getOmschrijving().toLowerCase().contains(zoekterm.toLowerCase()))
                    {
                        return true;
                    }
                    if (m.getLeergebieden().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm.toLowerCase())))
                    {
                        return true;
                    }
                    if (m.getDoelgroepen().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm.toLowerCase())))
                    {
                        return true;
                    }
                    return false;
        });

    }
}
