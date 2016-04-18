/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import domein.Doelgroep;
import domein.Firma;
import domein.Leergebied;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import domein.Materiaal;
import exceptions.AantalException;
import exceptions.NaamException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.FirmaDaoJpa;
import persistentie.GenericDaoJpa;
import persistentie.MateriaalDaoJpa;
import repository.GeneriekeRepository;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;

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
    private MateriaalDaoJpa materiaalDao;
    private FirmaDaoJpa firmaDao;
    private ObservableList<Materiaal> filterMateriaal;
    private List<String> lokalen;

    public MateriaalCatalogus() {
        materiaalDao = new MateriaalDaoJpa();
        firmaDao = new FirmaDaoJpa();
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
            if (!lokalen.contains(lokaal)){
                lokalen.add(lokaal);
            }
        });
        return lokalen;
    }
    public Materiaal voegMateriaalToe(String foto, String naam, String omschrijving, String plaats, String firmaNaam, String firmaContact, String artikelNrString, String aantalString, String aantalOnbeschikbaarString, String prijsString, boolean uitleenbaar, Set<Doelgroep> doelgroepen, Set<Leergebied> leergebieden) throws NaamException, AantalException {
        int aantalOnbeschikbaar = 0, artikelNr = 0, aantal;
        double prijs = 0.0;
        if (naam.equals("")) {
            throw new NaamException("Naam mag niet leeg zijn!");
        } else if (aantalString.equals("")) {
            throw new AantalException("Aantal mag niet leeg zijn!");
        } else {
            if (!aantalOnbeschikbaarString.isEmpty()) {
                try{
                aantalOnbeschikbaar = Integer.parseInt(aantalOnbeschikbaarString);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Moet een getal zijn!");
                }
            }
            if (!artikelNrString.isEmpty()) {
                try{
                artikelNr = Integer.parseInt(artikelNrString);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Moet een getal zijn!");
                }
            }
            if (!prijsString.isEmpty()) {
                try{
                prijs = Double.parseDouble(prijsString);
                }
                catch(NumberFormatException e)
                {
                    throw new IllegalArgumentException("Moet een getal zijn!");
                }
            }
            aantal = Integer.parseInt(aantalString);
            Firma f = firmaDao.geefFirma(firmaNaam);
                if(!f.getEmailContact().equals(firmaContact)) {
                    f.setEmailContact(firmaContact);
                    firmaDao.update(f);
                }
            //Firma f = new Firma(firmaNaam, firmaContact);
            Materiaal materiaal = new Materiaal(foto, naam, omschrijving, plaats, artikelNr, aantal, aantalOnbeschikbaar, prijs, uitleenbaar, f, doelgroepen, leergebieden);
            opgehaaldeMaterialen.add(materiaal);
            return materiaal;
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
                    || zoektermen.contains(m.uitleenbaarProperty().get().toLowerCase())
                    || zoektermen.contains(m.getFirma().getNaam().toLowerCase())) {
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

            case UITLEENBAARHEID:
                filteredmaterialen.setPredicate(m -> {
                    if (zoektermen.contains(m.uitleenbaarProperty().get().toLowerCase()) || zoektermen.isEmpty()) {
                        return true;
                    }
                    return false;
                });
                break;
            case FIRMA:
                filteredmaterialen.setPredicate(m -> {
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
        DOELGROEP, LEERGEBIED, UITLEENBAARHEID, FIRMA, PLAATS
    }

}

