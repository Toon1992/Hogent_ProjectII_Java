/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Dag;
import domein.Gebruiker;
import domein.Materiaal;
import domein.Reservatie;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.ReservatieDaoJpa;
import stateMachine.ReservatieStateEnum;

/**
 *
 * @author ToonDT
 */
public class ReservatieRepository implements IReservatieRepository
{

    private FilteredList<Reservatie> filterReservaties;
    private ReservatieDaoJpa reservatieDao;
    private ObservableList<Reservatie> filterReservatie;

    public ReservatieRepository()
    {
        reservatieDao = new ReservatieDaoJpa();
    }

    @Override
    public SortedList<Reservatie> geefReservaties()
    {
        if (filterReservaties == null)
        {
            filterReservatie = FXCollections.observableList(reservatieDao.findAll());
            filterReservaties = new FilteredList(filterReservatie, p -> true);
        }
        return new SortedList<>(filterReservaties);
    }

    @Override
    public void Zoek(String zoekTerm)
    {
        filterReservaties.setPredicate(r
                -> 
                {
                    if (zoekTerm == null || zoekTerm.isEmpty())
                    {
                        return true;
                    }

                    return r.getGebruiker().getNaam().toLowerCase().contains(zoekTerm.toLowerCase()) || r.getMateriaal().getNaam().toLowerCase().contains(zoekTerm.toLowerCase());
        });
    }

    @Override
    public void zoekOpBeginDatum(LocalDate zoekTerm)
    {
        Date datum = geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null)
                    {
                        return true;
                    }

                    return bevindtZichInDatumRange(datum, r, true);
        });
    }

    @Override
    public void zoekOpEindDatum(LocalDate zoekTerm)
    {
        Date datum = geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null)
                    {
                        return true;
                    }

                    return bevindtZichInDatumRange(datum, r, false);
        });
    }

    private boolean bevindtZichInDatumRange(Date datum, Reservatie reservatie, boolean opBeginDatum)
    {
        Date reservatieDatum;
        if (opBeginDatum)
        {
            reservatieDatum = reservatie.getBeginDatum();
        } else
        {
            reservatieDatum = reservatie.getEindDatum();
        }

        if (reservatieDatum.getYear() >= datum.getYear())
        {
            if (reservatieDatum.getMonth() >= datum.getMonth())
            {
                return reservatieDatum.getDay() >= datum.getDay();
            }
        }

        return false;
    }

    private Date geefEersteDagVanDeWeek(LocalDate datum)
    {
        switch (datum.getDayOfWeek())
        {
            case TUESDAY:
                datum = datum.minusDays(1);
                break;
            case WEDNESDAY:
                datum = datum.minusDays(2);
                break;
            case THURSDAY:
                datum = datum.minusDays(3);
                break;
            case FRIDAY:
                datum = datum.minusDays(4);
                break;
            case SATURDAY:
                datum = datum.minusDays(5);
                break;
            case SUNDAY:
                datum = datum.minusDays(6);
                break;
            default:
        }

        return convertLocalDateToDate(datum);
    }

    private Date convertLocalDateToDate(LocalDate datum)
    {
        Instant instant = Instant.from(datum.atStartOfDay(ZoneId.of("GMT")));
        return Date.from(instant);
    }

    @Override
    public List<Reservatie> geefReservatiesByDatum(Date startDatum, Date eindDatum, Materiaal materiaal){
        return reservatieDao.getReservaties(startDatum, eindDatum, materiaal);
    }
    @Override
    public int[] berekenAantalbeschikbaarMateriaal(Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, int aantal, int origineelAantal){
        List<Reservatie> overschrijvendeReservaties = geefReservatiesByDatum(startDate, endDate, materiaal);
        int aantalStudent = 0;
        //Indien lector enkel de reservaties van lector opvragen
        if(gebruiker.getType().equals("LE")){
            aantalStudent = overschrijvendeReservaties.stream().filter(r -> r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd)).mapToInt(r -> r.getAantalUitgeleend()).sum();
            overschrijvendeReservaties = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("LE")).collect(Collectors.toList());
        }
        //Aantal stuks dat reeds onbeschikbaar zijn voor de gebruiker (lector of student)
        int aantalGereserveerdeStuks = overschrijvendeReservaties.stream().mapToInt(r -> r.getAantalUitgeleend()).sum() - origineelAantal ;
        int aantalBeschikbaar = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar() - aantalGereserveerdeStuks;
        int aantalOverruled = aantalStudent+aantalGereserveerdeStuks+ aantal - materiaal.getAantal() - materiaal.getAantalOnbeschikbaar();
        return new int[]{aantalBeschikbaar, aantalOverruled};
    }
    @Override
    public void overruleStudent(int aantalOverruled){
        List<Reservatie> reservaties = geefReservaties();
        reservaties = reservaties.stream().filter(r -> r.getGebruiker().getType().equals("ST") && r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd)).sorted(Comparator.comparing(Reservatie::getAanmaakDatum)).collect(Collectors.toList());
        boolean nogTeOverrulen = true;
        while (nogTeOverrulen){
            Reservatie reservatie = reservaties.get(0);

            int aantal = reservatie.getAantalUitgeleend();

            reservatie.setReservatieStateEnum(ReservatieStateEnum.Overruled);
            wijzigReservatieObject(reservatie);

            //Indien er nog een aantal stuks overschieten na het overrulen, wordt er een nieuwe reservatie gemaakt
            if(aantal - aantalOverruled > 0)
                voegReservatieToe(new Reservatie(aantal - aantalOverruled,0, reservatie.getBeginDatum(), reservatie.getEindDatum(), new Date(),null,ReservatieStateEnum.Gereserveerd, reservatie.getGebruiker(), reservatie.getMateriaal()));

            aantalOverruled -= aantal;
            reservaties.remove(reservatie);

            if(aantalOverruled <= 0){
                nogTeOverrulen = false;
            }
        }
    }
    @Override
    public Reservatie maakReservatieObject(int aantal, int aantalTerug, Date startDate, Date endDate, ReservatieStateEnum status, Gebruiker gebruiker, Materiaal materiaal){
        SortedSet<Dag> dagen = new TreeSet<>();
        if(gebruiker.getType().equals("LE")){
            Date maandag = geefEersteDagVanDeWeek(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date vrijdag = convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
            //alle dagen tussen begin en einddatum in de list plaatsen
            Date beginDatum = startDate;
            while(beginDatum.before(endDate)){
                dagen.add(new Dag(beginDatum));
                beginDatum = convertLocalDateToDate(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1));
            }
            dagen.add(new Dag(endDate));
        }
        return new Reservatie(aantal, aantalTerug, startDate, endDate, new Date(), dagen, status, gebruiker, materiaal);
    }
    @Override
    public void voegReservatieToe(Reservatie reservatie){
        reservatieDao.startTransaction();
        reservatieDao.insert(reservatie);
        reservatieDao.commitTransaction();
        filterReservatie.remove(reservatie);
        filterReservatie.add(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }
    @Override
    public void wijzigReservatie(Reservatie reservatie, int aantal, Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, ReservatieStateEnum status){
        Reservatie oldReservatie = reservatie;
        //De parameters setten
        reservatie.setAantalUitgeleend(aantal);
        reservatie.setGebruiker(gebruiker);
        reservatie.setStartDatum(startDate);
        reservatie.setEindDatum(endDate);
        reservatie.setMateriaal(materiaal);
        reservatie.setReservatieStateEnum(status);

        wijzigReservatieObject(reservatie);

        filterReservatie.remove(oldReservatie);
        filterReservatie.add(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }
    private void wijzigReservatieObject(Reservatie reservatie){
        reservatieDao.startTransaction();
        reservatieDao.update(reservatie);
        reservatieDao.commitTransaction();
    }
    @Override
    public void verwijderReservatue(Reservatie reservatie)
    {
        reservatieDao.startTransaction();
        reservatieDao.delete(reservatie);
        reservatieDao.commitTransaction();
        filterReservatie.remove(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }
}
