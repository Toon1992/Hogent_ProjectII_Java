package controller;

import domein.*;
import exceptions.AantalException;
import exceptions.NaamException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;
import domein.MateriaalCatalogus.MateriaalFilter;
import persistentie.FirmaDaoJpa;
import persistentie.MateriaalDaoJpa;
import repository.GeneriekeRepository;

/**
 * Created by donovandesmedt on 25/03/16.
 */
public class MateriaalController extends Observable
{

    private MateriaalCatalogus materiaalCatalogus;
    private GeneriekeRepository genRepo;

    public MateriaalController()
    {
        setMateriaalCatalogus(new MateriaalCatalogus(new MateriaalDaoJpa(), new FirmaDaoJpa()));
        setGeneriekeRepository(new GeneriekeRepository());
    }
    private void setGeneriekeRepository(GeneriekeRepository repository){
        this.genRepo = repository;
    }

    private void setMateriaalCatalogus(MateriaalCatalogus materiaalCatalogus)
    {
        this.materiaalCatalogus = materiaalCatalogus;
    }
    public void controleerUniekheidMateriaalnaam(String naam){
        materiaalCatalogus.controleerUniekheidMateriaalnaam(naam);
    }
    public void voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, Firma firma, String artikelNrString, String aantalString, String aantalOnbeschikbaarString, String prijsString, boolean uitleenbaar, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden) throws NaamException, AantalException
    {
        Materiaal materiaal = materiaalCatalogus.voegMateriaalToe(foto, naam, omschrijving, plaats, firma, artikelNrString, aantalString, aantalOnbeschikbaarString, prijsString, uitleenbaar, doelgroepen, leergebieden);
        genRepo.saveObject(materiaal);
    }
    public SortedList<Materiaal> getMateriaalFilterList()
    {
        return materiaalCatalogus.geefMaterialen().sorted();
    }
    public Firma geefFirma(String naam, String email){
        return materiaalCatalogus.geefFirma(naam, email);
    }
    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list)
    {
        return materiaalCatalogus.objectCollectionToObservableList(list);
    }

    public void zoek(List<String> zoekterm)
    {
        materiaalCatalogus.zoek(zoekterm.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    public void filter(MateriaalFilter filterNaam, List<String> filters)
    {
        materiaalCatalogus.filterMaterialen(filterNaam, filters.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }
    public List<String> getLokalen(){
        return materiaalCatalogus.geefLokalen();
    }
    public void setCurrentMateriaal(Materiaal materiaal)
    {
        setChanged();
        notifyObservers(materiaal);
    }

    public MateriaalCatalogus getMateriaalCatalogus()
    {
        return materiaalCatalogus;
    }

    public void verwijderMateriaal(Materiaal materiaal)
    {
        genRepo.verwijderObject(materiaal);
        materiaalCatalogus.verwijderMateriaal(materiaal);
    }

    public void wijzigMateriaal(Materiaal materiaal)
    {
        genRepo.wijzigObject(materiaal);
    }
}
