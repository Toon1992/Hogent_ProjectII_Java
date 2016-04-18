/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javafx.collections.transformation.SortedList;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import persistentie.ReservatieDaoJpa;
import domein.ReservatieCatalogus;
import repository.GeneriekeRepository;
import stateMachine.ReservatieStateEnum;

/**
 *
 * @author ToonDT
 */
public class ReservatieController
{

    private ReservatieCatalogus repository;
    private GeneriekeRepository genRepo;

    public ReservatieController()
    {
        setReservatieRepository(new ReservatieCatalogus(new ReservatieDaoJpa()));
        setGeneriekeRepository(new GeneriekeRepository());
        verwijderVervallenReservaties();
        aanpassenTeLaatTerugGebrachteReservaties();
        checkBeschikbaarheidKomendeReservaties();
    }
    private void setGeneriekeRepository(GeneriekeRepository repository){
        this.genRepo = repository;
    }
    private void setReservatieRepository(ReservatieCatalogus repository)
    {
        this.repository = repository;
    }

    public SortedList<Reservatie> getReservaties()
    {
        return repository.geefReservaties();
    }

    public void zoek(String zoekterm)
    {
        repository.Zoek(zoekterm);
    }

    public void zoekOpBeginDatum(LocalDate datum)
    {
        repository.zoekOpBeginDatum(datum);
    }

    public void zoekOpEindDatum(LocalDate datum)
    {
        repository.zoekOpEindDatum(datum);
    }

    public void setCurrentReservatie(Reservatie reservatie)
    {
//        setChanged();
//        notifyObservers(reservatie);
    }

    public void maakReservatie(int aantal, int aantalUit, int aantalTerug, Date startDate, Date endDate, ReservatieStateEnum status, Gebruiker gebruiker, Materiaal materiaal)
    {
        Reservatie reservatie = repository.maakReservatieObject(aantal, aantalUit, aantalTerug, startDate, endDate, status, gebruiker, materiaal);
        genRepo.saveObject(reservatie);
        repository.voegReservatieToe(reservatie);
    }

    public void wijzigReservatie(Reservatie reservatie, int aantal, int aantalUit, int aantalTerug, Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, ReservatieStateEnum status)
    {
        repository.wijzigReservatie(reservatie, aantal,aantalUit, aantalTerug, gebruiker, startDate, endDate, materiaal, status);
    }

    public void overruleStudent(int aantalOverruled, Materiaal materiaal)
    {
        Set<Reservatie> teOverrulenReservaties = repository.overruleStudent(aantalOverruled, materiaal);
        teOverrulenReservaties.forEach(reservatie -> {
            genRepo.saveObject(reservatie);
            repository.voegReservatieToe(reservatie);
        });
    }

    public void verwijderReservatie(Reservatie reservatie)
    {
        genRepo.verwijderObject(reservatie);
        repository.verwijderReservatue(reservatie);
    }

    public List<Reservatie> getReservatiesByDatum(Date startDatum, Date eindDatum, Materiaal materiaal)
    {
        return repository.geefReservatiesByDatum(startDatum, eindDatum, materiaal);
    }

    public int[] berekenAantalBeschikbaar(Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, int aantal, int origineelAantal)
    {
        return repository.berekenAantalbeschikbaarMateriaal(gebruiker, startDate, endDate, materiaal, aantal, origineelAantal);
    }
    public void verwijderVervallenReservaties(){
        Set<Reservatie> teVerwijderenReservaties = repository.verwijderVervallenReservaties();
        teVerwijderenReservaties.forEach(reservatie -> {
            genRepo.verwijderObject(reservatie);
            repository.verwijderReservatue(reservatie);
        });
    }
    public void aanpassenTeLaatTerugGebrachteReservaties(){
        Set<Reservatie> reservatiesTeLaat = repository.aanpassenTeLaatTerugGebrachteReservaties();
        reservatiesTeLaat.forEach(reservatie -> genRepo.wijzigObject(reservatie));
    }
    public void checkBeschikbaarheidKomendeReservaties(){
        Set<Reservatie> reservatiesTeLaat = repository.checkAlleReservatiesBeschikbaar();
        reservatiesTeLaat.forEach(reservatie -> genRepo.wijzigObject(reservatie));
    }
    public void setFormatDatepicker(DatePicker dp)
    {
        dp.setOnShowing(e -> Locale.setDefault(Locale.Category.FORMAT, Locale.FRANCE));
        String pattern = "dd-MM-yyy";
        dp.setConverter(new StringConverter<LocalDate>()
        {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date)
            {
                if (date != null)
                {
                    return dateFormatter.format(date);
                } else
                {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string)
            {
                if (string != null && !string.isEmpty())
                {
                    return LocalDate.parse(string, dateFormatter);
                } else
                {
                    return null;
                }
            }
        });

    }
}
