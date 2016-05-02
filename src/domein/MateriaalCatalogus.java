/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import exceptions.AantalException;
import exceptions.EmailException;
import exceptions.MultiException;
import exceptions.NaamException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.FirmaDao;
import persistentie.MateriaalDao;

/**
 *
 * @author donovandesmedt
 */
public class MateriaalCatalogus {

    private FilteredList<Materiaal> filteredmaterialen;
    private Set<Materiaal> newMaterialList;
    private List<Materiaal> opgehaaldeMaterialen;
    private Set<Materiaal> nonFilteredSet;
    private Map<String, Set<Materiaal>> filterMap = new HashMap<>();
    private MateriaalDao materiaalDao;
    private FirmaDao firmaDao;
    private ObservableList<Materiaal> filterMateriaal;
    private List<String> lokalen;

    public MateriaalCatalogus(MateriaalDao materiaalDao, FirmaDao firmaDao) {
        setMateriaalDao(materiaalDao);
        setFirmaDao(firmaDao);
    }

    public void setMateriaalDao(MateriaalDao materiaalDao)
    {
        this.materiaalDao = materiaalDao;
    }
    
    public void setFirmaDao(FirmaDao firmaDao)
    {
        this.firmaDao = firmaDao;
    }
    
    public SortedList<Materiaal> geefMaterialen() {
        if (opgehaaldeMaterialen == null) {
            newMaterialList = materiaalDao.findAll().stream().collect(Collectors.toSet());
            opgehaaldeMaterialen = materiaalDao.findAll();
        }
        filterMateriaal = FXCollections.observableList(opgehaaldeMaterialen);
        filteredmaterialen = new FilteredList(filterMateriaal, p -> true);
        return new SortedList<>(filteredmaterialen);
    }
    
    
    public List<String> geefLokalen(){
        lokalen = new ArrayList<>();
        geefMaterialen().stream().forEach(materiaal -> {
            String lokaal = materiaal.getPlaats();
            if (!lokalen.contains(lokaal) && !lokaal.trim().isEmpty()){
                lokalen.add(lokaal);
            }
        });
        return lokalen;
    }
    public Materiaal voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, Firma firma, String artikelNrString, String aantalString, String aantalOnbeschikbaarString, String prijsString, boolean uitleenbaar, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden) throws NaamException, AantalException {
        int aantalOnbeschikbaar = 0, artikelNr = 0, aantal;
        double prijs = 0.0;
        if (naam.equals("")) {
            if (aantalString.equals("")) {
                throw new MultiException("De verplichte vakken mogen niet leeg zijn!");
            }
            throw new NaamException("Naam mag niet leeg zijn.");
        } if (aantalString.equals("")) {
            throw new AantalException("Aantal mag niet leeg zijn!");
        } else {

            try{
                aantal = Integer.parseInt(aantalString);
            }
            catch (Exception e){
                throw new AantalException("Invoerveld aantal moet een positief getal bevattten");
            }
            if (!aantalOnbeschikbaarString.isEmpty()) {
                try{
                aantalOnbeschikbaar = Integer.parseInt(aantalOnbeschikbaarString);
                    if(aantalOnbeschikbaar < 0){
                        throw new IllegalArgumentException("Aantalonbeschikbaar moet groter zijn dan 0");
                    }
                    if(aantalOnbeschikbaar > aantal){
                        throw new IllegalArgumentException("Aantalonbeschikbaar kan niet groter zijn dan aantal in catalogus");
                    }
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Aantalonbeschikbaar moet een getal zijn!");
                }
            }
            if (!artikelNrString.isEmpty()) {
                try{
                artikelNr = Integer.parseInt(artikelNrString);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Artikelnummer moet een getal zijn!");
                }
            }
            if (!prijsString.isEmpty()) {
                try{
                prijs = Double.parseDouble(prijsString);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Prijs moet een getal zijn!");
                }
            }
           
            if(doelgroepen.isEmpty())
            {
                throw new MultiException("De verplichte vakken mogen niet leeg zijn!");
            }
            
            if(leergebieden.isEmpty())
            {
                throw new MultiException("De verplichte vakken mogen niet leeg zijn!");
            }
            


            Materiaal materiaal = new Materiaal(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, firma, doelgroepen, leergebieden);
            opgehaaldeMaterialen.add(materiaal);
            return materiaal;
        }
    }
    public Firma geefFirma(String firmaNaam, String firmaContact){
        Firma f = firmaDao.geefFirma(firmaNaam);
        if(!Pattern.matches("\\w+(\\.\\w*)*@\\w+\\.\\w+(\\.\\w+)*", firmaContact)){
            throw new EmailException("Email firma ongeldig");
        }
        if(!f.getEmailContact().equals(firmaContact)) {
            f.setEmailContact(firmaContact);
            firmaDao.update(f);
        }
        return f;
    }
    public void controleerUniekheidMateriaalnaam(Materiaal materiaal, String naam){
        for (Materiaal m : materiaalDao.getMaterialen().stream().filter(mat -> !mat.equals(materiaal)).collect(Collectors.toList())) {
            if(m.getNaam().toLowerCase().equals(naam.toLowerCase()))
            {
                throw new NaamException("Er bestaat al een materiaal met deze naam!");
            }
        }
    }
    public void verwijderMateriaal(Materiaal materiaal) {
        filterMateriaal.remove(materiaal);
        filteredmaterialen = new FilteredList(filterMateriaal, p -> true);
    }

    public <E> ObservableList<String> objectCollectionToObservableList(Collection<E> list) {
        List<String> stringLijst = list.stream().map(e -> e.toString()).collect(Collectors.toList());
        return FXCollections.observableArrayList(stringLijst);
    }

    public void zoek(Set<String> zoektermen) {
        filteredmaterialen.setPredicate(m -> {
            if (zoektermen == null
                    || zoektermen.isEmpty()
                    || m.getNaam().toLowerCase().contains(zoektermen.iterator().next())
                    || m.getOmschrijving().toLowerCase().contains(zoektermen.iterator().next())
                    || zoektermen.stream().anyMatch(zoekterm -> m.getLeergebieden().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                    || zoektermen.stream().anyMatch(zoekterm -> m.getDoelgroepen().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                    || zoektermen.contains(m.getPlaats().toLowerCase())
                    || zoektermen.contains(m.artikelNummerProperty().get())
                    || (m.getFirma() != null && zoektermen.contains(m.getFirma().getNaam().toLowerCase()))) {
                return true;
            }
            return false;
        });

    }

    public void filterMaterialen(MateriaalFilter filtersoort, Set<String> zoektermen) {
        filter(filtersoort, zoektermen);
        nonFilteredSet = new HashSet<>();
        if (filterMap.isEmpty()) {
            filterMap.put(filtersoort.toString(), filteredmaterialen.stream().collect(Collectors.toSet()));
        }
        if (!filterMap.containsKey(filtersoort.toString())) {
            updateFiltermap();
            filterMap.put(filtersoort.toString(), nonFilteredSet);
        } else {
            filterMap.put(filtersoort.toString(), filteredmaterialen.stream().collect(Collectors.toSet()));
            updateFiltermap();
        }
    }

    private void updateFiltermap() {
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
                if (newMaterialList.stream().anyMatch(m -> m.getNaam().equals(materiaal.getNaam()))) {
                    return true;
                }
                return false;
            });
        });
    }

    private void filter(MateriaalFilter soort, Set<String> zoektermen) {
        switch (soort) {
            case DOELGROEP:
                filteredmaterialen.setPredicate(m -> {
                    if (zoektermen.stream().anyMatch(zoekterm -> m.getDoelgroepen().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                            || zoektermen.isEmpty()) {
                        return true;
                    }
                    return false;
                });
                break;
            case LEERGEBIED:
                filteredmaterialen.setPredicate(m -> {
                    if (zoektermen.stream().anyMatch(zoekterm -> m.getLeergebieden().stream().anyMatch(l -> l.getNaam().toLowerCase().contains(zoekterm)))
                            || zoektermen.isEmpty()) {
                        return true;
                    }
                    return false;
                });
                break;
            case FIRMA:
                filteredmaterialen.setPredicate(m -> {
                    if(zoektermen.isEmpty()){
                        return true;
                    }
                    if(m.getFirma() == null){
                        return false;
                    }
                    if (zoektermen.contains(m.getFirma().getNaam().toLowerCase()) || zoektermen.isEmpty()) {
                        return true;
                    }
                    return false;
                });
                break;
            case PLAATS:
                filteredmaterialen.setPredicate(m -> {
                    if (zoektermen.contains(m.getPlaats().toLowerCase()) || zoektermen.isEmpty()) {
                        return true;
                    }
                    return false;
                });
                break;
        }
    }

    public enum MateriaalFilter {
        DOELGROEP, LEERGEBIED, FIRMA, PLAATS
    }

}

