/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.*;
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
    private Set<Materiaal> newMaterialList;
    private Set<Materiaal> nonFilteredSet;
    private Map<String, Set<Materiaal>> filterMap = new HashMap<>();
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
            newMaterialList = materiaalDao.findAll().stream().collect(Collectors.toSet());
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
    public void zoek(Set<String> zoektermen){
        filteredmaterialen.setPredicate(m -> {
            if(     zoektermen == null ||
                    zoektermen.isEmpty() ||
                    m.getNaam().toLowerCase().contains(zoektermen.iterator().next()) ||
                    m.getOmschrijving().toLowerCase().contains(zoektermen.iterator().next()) ||
                    zoektermen.stream().anyMatch(zoekterm -> m.getLeergebieden().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm))) ||
                    zoektermen.stream().anyMatch(zoekterm -> m.getDoelgroepen().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))||
                    zoektermen.contains(m.getPlaats().toLowerCase())||
                    zoektermen.contains(m.uitleenbaarProperty().get().toLowerCase()) ||
                    zoektermen.contains(m.getFirma().getNaam().toLowerCase()))
                return true;
            return false;
        });


    }
    public void filterMaterialen(MateriaalFilter filtersoort, Set<String> zoektermen){
        filter(filtersoort, zoektermen);
        nonFilteredSet = new HashSet<>();
        if(filterMap.isEmpty()){
            filterMap.put(filtersoort.toString(), filteredmaterialen.stream().collect(Collectors.toSet()));
        }
        if(!filterMap.containsKey(filtersoort.toString())){
            updateFiltermap();
            filterMap.put(filtersoort.toString(), nonFilteredSet);
        }
        else{
            filterMap.put(filtersoort.toString(), filteredmaterialen.stream().collect(Collectors.toSet()));
            updateFiltermap();
        }
    }
    private void updateFiltermap(){
        filterMap.entrySet().stream().forEach(set -> {
            //Materialen van huidige filter in newMaterials steken
            newMaterialList = new HashSet<Materiaal>();
            filteredmaterialen.forEach(materiaal -> {
                newMaterialList.add(materiaal);
                nonFilteredSet.add(materiaal);
            });
            //Het gemeenschappelijke nemen van newMaterials en de rest van de filters uit de map
            newMaterialList.retainAll(set.getValue().stream().collect(Collectors.toSet()));
            // De predicate aanpassen
            filteredmaterialen.setPredicate(materiaal -> {
                if(newMaterialList.stream().anyMatch(m -> m.getNaam().equals(materiaal.getNaam()))){
                    return true;
                }
                return false;
            });
        });
    }
    private void filter(MateriaalFilter soort, Set<String> zoektermen){
        switch (soort){
            case DOELGROEP:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.stream().anyMatch(zoekterm -> m.getDoelgroepen().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                            || zoektermen.isEmpty()){
                        return true;
                    }
                    return false;
                });
                break;
            case LEERGEBIED:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.stream().anyMatch(zoekterm -> m.getLeergebieden().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                            || zoektermen.isEmpty()){
                        return true;
                    }
                    return false;
                });
                break;

            case UITLEENBAARHEID:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.contains(m.uitleenbaarProperty().get().toLowerCase()) || zoektermen.isEmpty()){
                        return true;
                    }
                    return false;
                });
                break;
            case FIRMA:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.contains(m.getFirma().getNaam().toLowerCase()) || zoektermen.isEmpty()){
                        return true;
                    }
                    return false;
                });
                break;
            case PLAATS:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.contains(m.getPlaats().toLowerCase()) || zoektermen.isEmpty()){
                        return true;
                    }
                    return false;
                });
                break;
        }
    }
    public enum MateriaalFilter{
        DOELGROEP, LEERGEBIED, UITLEENBAARHEID, FIRMA, PLAATS
    }
}
