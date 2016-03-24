/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Thomas
 */
public class DomeinController extends Observable{
    private Beheerder beheerder;
    private BeheerderRepository beheerderRepository;
    private MateriaalRepository materiaalRepository;
    public DomeinController(){
        setBeheerderRepository(new BeheerderRepository());
        setMateriaalRepository(new MateriaalRepository());
    }
    public void setBeheerderRepository(BeheerderRepository beheerderRepository){
        this.beheerderRepository = beheerderRepository;
    }
    public void setMateriaalRepository(MateriaalRepository materiaalRepository){
        this.materiaalRepository = materiaalRepository;
    } 
    public void registreer(String email, String password, String naam){
        beheerderRepository.voegGebruikerToe(new HoofdBeheerder(email, password, naam));
    }
    public void login(String email, String wachtwoord) throws Exception{
        beheerderRepository.login(email, wachtwoord);
    }
    public void voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, int artikelNr, int aantal, int aantalOnbeschikbaar, double prijs, boolean uitleenbaar, Firma firma, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden)
    {
        beheerder.voegMateriaalToe(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
    }
    public SortedList<Materiaal> getMateriaalFilterList(){
        return materiaalRepository.geefMaterialen();
    }
    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list){
        return materiaalRepository.objectCollectionToObservableList(list);
    }
    public void zoek(String zoekterm){
        materiaalRepository.zoek(zoekterm);
    }
    public void setCurrentMateriaal(Materiaal materiaal){
        setChanged();
        notifyObservers(materiaal);
    }
}
