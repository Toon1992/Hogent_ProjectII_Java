/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import persistentie.ReservatieDao;
import persistentie.ReservatieDaoJpa;
import repository.GeneriekeRepository;
import stateMachine.ReservatieStateEnum;

/**
 *
 * @author ToonDT
 */
public class ReservatieCatalogus
{

    private FilteredList<Reservatie> filterReservaties;
    private ReservatieDao reservatieDao;
    private ObservableList<Reservatie> filterReservatie;
    public ReservatieCatalogus(ReservatieDao reservatieDao)
    {
        setReservatieDao(reservatieDao);
    }
    public void setReservatieDao(ReservatieDao reservatieDao){
        this.reservatieDao = reservatieDao;
    }
    public SortedList<Reservatie> geefReservaties()
    {
        if (filterReservaties == null)
        {
            filterReservatie = FXCollections.observableList(reservatieDao.findAll());
            filterReservaties = new FilteredList(filterReservatie, p -> true);
        }
        return new SortedList<>(filterReservaties);
    }


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


    public void zoekOpBeginDatum(LocalDate zoekTerm)
    {
        Date datum = HulpMethode.geefEersteDagVanDeWeek(zoekTerm);

        filterReservaties.setPredicate(r -> 
                {
                    if (zoekTerm == null)
                    {
                        return true;
                    }

                    return bevindtZichInDatumRange(datum, r, true);
        });
    }


    public void zoekOpEindDatum(LocalDate zoekTerm)
    {
        Date datum = HulpMethode.geefEersteDagVanDeWeek(zoekTerm);

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

    public List<Reservatie> geefReservatiesByDatum(Date startDatum, Date eindDatum, Materiaal materiaal){
        return reservatieDao.getReservaties(startDatum, eindDatum, materiaal);
    }

    public int[] berekenAantalbeschikbaarMateriaal(Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, int aantal, int origineelAantal){
        List<Reservatie> overschrijvendeReservaties = geefReservatiesByDatum(startDate, endDate, materiaal);
        int aantalStudent = 0;
        //Indien lector enkel de reservaties van lector opvragen
        if(gebruiker.getType().equals("LE")){
            aantalStudent = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("ST")).mapToInt(r -> r.getAantalGereserveerd()).sum();
            overschrijvendeReservaties = overschrijvendeReservaties.stream().filter(r -> r.getGebruiker().getType().equals("LE")).collect(Collectors.toList());
        }
        //Aantal stuks dat reeds onbeschikbaar zijn voor de gebruiker (lector of student)
        int aantalGereserveerdeStuks = overschrijvendeReservaties.stream().mapToInt(r -> r.getAantalGereserveerd()).sum() - origineelAantal ;
        aantalGereserveerdeStuks = aantalGereserveerdeStuks < 0 ? 0: aantalGereserveerdeStuks;
        int aantalBeschikbaar = materiaal.getAantal() - materiaal.getAantalOnbeschikbaar() - aantalGereserveerdeStuks;
        int aantalOverruled = aantalStudent+aantalGereserveerdeStuks+ aantal - materiaal.getAantal() - materiaal.getAantalOnbeschikbaar();
        aantalOverruled = aantalOverruled < 0? 0: aantalOverruled;
        return new int[]{aantalBeschikbaar, aantalOverruled};
    }

    public Set<Reservatie> overruleStudent(int aantalOverruled, Materiaal materiaal){
        Set<Reservatie> teOverrulenReservatie = new HashSet<>();
        List<Reservatie> reservaties = geefReservaties().stream().filter(r -> r.getMateriaal().equals(materiaal)).collect(Collectors.toList());
        reservaties = reservaties.stream().filter(r -> r.getGebruiker().getType().equals("ST") && r.getReservatieStateEnum().equals(ReservatieStateEnum.Gereserveerd)).sorted(Comparator.comparing(Reservatie::getAanmaakDatum)).collect(Collectors.toList());
        boolean nogTeOverrulen = true;
        while (nogTeOverrulen){
            Reservatie reservatie = reservaties.get(0);

            int aantal = reservatie.getAantalGereserveerd();

            reservatie.setReservatieStateEnum(ReservatieStateEnum.Overruled);
            wijzigReservatieObject(reservatie);

            //Indien er nog een aantal stuks overschieten na het overrulen, wordt er een nieuwe reservatie gemaakt
            if(aantal - aantalOverruled > 0)
                teOverrulenReservatie.add(new Reservatie(aantal - aantalOverruled,0, 0, reservatie.getBeginDatum(), reservatie.getEindDatum(), new Date(),null,ReservatieStateEnum.Gereserveerd, reservatie.getGebruiker(), reservatie.getMateriaal()));

            aantalOverruled -= aantal;
            reservaties.remove(reservatie);

            if(aantalOverruled <= 0){
                nogTeOverrulen = false;
            }
        }
        return teOverrulenReservatie;
    }

    public Reservatie maakReservatieObject(int aantal,int aantalUitgeleend, int aantalTerug, Date startDate, Date endDate, ReservatieStateEnum status, Gebruiker gebruiker, Materiaal materiaal){
        SortedSet<Dag> dagen = new TreeSet<>();
        if(gebruiker.getType().equals("LE")){
            Date maandag = HulpMethode.geefEersteDagVanDeWeek(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
            //alle dagen tussen begin en einddatum in de list plaatsen
            Date beginDatum = startDate;
            while(beginDatum.before(endDate)){
                dagen.add(new Dag(beginDatum));
                beginDatum = HulpMethode.convertLocalDateToDate(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1));
            }
            dagen.add(new Dag(endDate));
        }
        return new Reservatie(aantal, aantalUitgeleend, aantalTerug, startDate, endDate, new Date(), dagen, status, gebruiker, materiaal);
    }
    public Set<Reservatie> verwijderVervallenReservaties(){
        LocalDate thismomentLastWeek = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date deadline = new Date();
        deadline.setHours(17);
        deadline.setMinutes(1);

        Date vorigeWeek = HulpMethode.convertLocalDateToDate(thismomentLastWeek.minusWeeks(1));

        // De reservaties waarbij niet alle stuks tijdig werden teruggebracht worden een week bewaard na de deadline om het nodige administratieve werk te doen
        Set<Reservatie> reservaties = geefReservaties().stream().filter(reservatie ->
            reservatie.getEindDatum().before(vorigeWeek) && (reservatie.getAantalTeruggebracht() < reservatie.getAantalUitgeleend()) && reservatie.getAantalTeruggebracht() > 0
        ).collect(Collectors.toSet());

        // De reservaties waarvan alle stuks werden teruggebracht worden instant verwijderd wanneer de deadline verlopen is.
        reservaties.addAll(geefReservaties().stream().filter(reservatie -> reservatie.getEindDatum().before(deadline) && reservatie.getAantalUitgeleend() == reservatie.getAantalTeruggebracht()).collect(Collectors.toList()));
        return reservaties;
    }
    public Set<Reservatie> aanpassenTeLaatTerugGebrachteReservaties(){
        Date deadline = new Date();
        deadline.setHours(17);
        deadline.setMinutes(1);

        //Alle reservaties die niet tijdig zijn teruggebracht.
        Set<Reservatie> reservatiesTeLaat = geefReservaties().stream().filter(reservatie -> reservatie.getEindDatum().before(deadline) && reservatie.getAantalTeruggebracht() == 0 && reservatie.getAantalUitgeleend() > 0).collect(Collectors.toSet());
        reservatiesTeLaat.forEach(reservatie ->  reservatie.setReservatieStateEnum(ReservatieStateEnum.TeLaat));

        //Alle reservaties die hierop volgen moeten ook op status te laat worden gezet.
        Set<Reservatie> aangepasteReservaties = aanpassenOpeenvolgendeReservaties(reservatiesTeLaat);
        reservatiesTeLaat.addAll(aangepasteReservaties);

        return reservatiesTeLaat;
    }
    private Set<Reservatie> aanpassenOpeenvolgendeReservaties(Set<Reservatie> reservaties){
        int week, aantal;
        Reservatie reser;
        //Materiaal materiaal;
        List<Reservatie> reservatiePool;
        Set<Reservatie> aangepasteReservaties = new HashSet<>();
        for (Reservatie reservatie :reservaties) {

            //Variabelen
            Materiaal materiaal = reservatie.getMateriaal();
            aantal = reservatie.getAantalUitgeleend();
            week = HulpMethode.getWeekOfDate(reservatie.getEindDatum()) + 1;
            Date maandag = HulpMethode.getFirstDayOfWeek(week);
            Date vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

            //Alle reservaties van hetzelfde materiaal die gepland staan voor de opeenvolgende week
            reservatiePool = geefReservaties().stream().filter(r -> r.getMateriaal().equals(materiaal) && r.getBeginDatum().before(vrijdag) && r.getEindDatum().after(maandag)).sorted(Comparator.comparing(Reservatie::getAanmaakDatum)).collect(Collectors.toList());

            while(aantal > 0 && reservatiePool.size() > 0){
                reser = reservatiePool.get(0);
                reser.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
                aantal -= reser.getAantalGereserveerd();
                aangepasteReservaties.add(reser);
            }
        }
        return aangepasteReservaties;
    }
    public Set<Reservatie> checkAlleReservatiesBeschikbaar(){
        // Kijken of de reservaties van vorige week op TeLaat staan, indien ja, de opeenvolgende reservaties ook op te laat zetten.
        Date maandag, vrijdag;
        maandag = HulpMethode.geefEersteDagVanDeWeek(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        vrijdag = HulpMethode.convertLocalDateToDate(maandag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));
        Set<Reservatie> reservaties = geefReservaties().stream().filter(reservatie -> reservatie.getReservatieStateEnum().equals(ReservatieStateEnum.TeLaat) && reservatie.getBeginDatum().before(vrijdag) && reservatie.getEindDatum().after(maandag)).collect(Collectors.toSet());
        Set<Reservatie> aangepasteReservaties = aanpassenOpeenvolgendeReservaties(reservaties);
        return aangepasteReservaties;
    }
    public void voegReservatieToe(Reservatie reservatie){
        filterReservatie.remove(reservatie);
        filterReservatie.add(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }

    public void wijzigReservatie(Reservatie reservatie, int aantal,int aantalUit, int aantalTerug, Gebruiker gebruiker, Date startDate, Date endDate, Materiaal materiaal, ReservatieStateEnum status){
        Reservatie oldReservatie = reservatie;
        //De parameters setten
        reservatie.setAantalGereserveerd(aantal);
        reservatie.setAantalUitgeleend(aantalUit);
        reservatie.setAantalTeruggebracht(aantalTerug);
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
        ReservatieDaoJpa jpa = (ReservatieDaoJpa) reservatieDao;
        jpa.startTransaction();
        jpa.update(reservatie);
        jpa.commitTransaction();
    }

    public void verwijderReservatue(Reservatie reservatie)
    {
        filterReservatie.remove(reservatie);
        filterReservaties = new FilteredList(filterReservatie, p -> true);
    }
}
