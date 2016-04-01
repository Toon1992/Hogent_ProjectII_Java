package controller;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;
import domein.Materiaal;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import repository.MateriaalCatalogus;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;
import repository.MateriaalCatalogus.MateriaalFilter;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public class MateriaalController extends Observable{
    private MateriaalCatalogus materiaalCatalogus;
    public MateriaalController(){
        setMateriaalCatalogus(new MateriaalCatalogus());
    }
    private void setMateriaalCatalogus(MateriaalCatalogus materiaalCatalogus) {
        this.materiaalCatalogus = materiaalCatalogus;
    }
    public void voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden)
    {
        materiaalCatalogus.voegMateriaalToe(new Materiaal(foto,naam,omschrijving,plaats,artikelNr,aantal,aantalOnbeschikbaar,prijs,uitleenbaar,firma,doelgroepen,leergebieden));
    }
    public SortedList<Materiaal> getMateriaalFilterList(){
        return materiaalCatalogus.geefMaterialen();
    }
    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list){
        return materiaalCatalogus.objectCollectionToObservableList(list);
    }
    public void zoek(List<String> zoekterm){
        materiaalCatalogus.zoek(zoekterm.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }
    public void filter(MateriaalFilter filterNaam, List<String> filters){
        materiaalCatalogus.filterMaterialen(filterNaam, filters.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }
    public void setCurrentMateriaal(Materiaal materiaal){
        setChanged();
        notifyObservers(materiaal);
    }
    
    public MateriaalCatalogus getMateriaalCatalogus(){
        return materiaalCatalogus;
    }
}
