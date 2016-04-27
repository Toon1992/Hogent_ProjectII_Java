/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domein.HulpMethode;
import domein.Materiaal;
import domein.Reservatie;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import persistentie.ReservatieDao;
import domein.ReservatieCatalogus;
import stateMachine.ReservatieStateEnum;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 *
 * @author donovandesmedt
 */
public class ReservatieCatalogusTest {
    private ReservatieData data;
    private ReservatieCatalogus repository;
    private List<Reservatie> reservaties;
    @Mock
    private ReservatieDao reservatieDao;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        data = new ReservatieData();
        trainDummy();
        repository = new ReservatieCatalogus(reservatieDao);
    }

    public void trainDummy() {
        Mockito.when(reservatieDao.findAll()).thenReturn(data.getSortedReservaties());
    }

    @Test
    public void testZoekReservatieCorrecteZoektermGeeftReservatieTerug(){
        reservaties = repository.geefReservaties();
        String zoekterm = "wereldbol";
        repository.Zoek(zoekterm);
        Assert.assertEquals(3, reservaties.size());
    }
    @Test
    public void testZoekReservatieLegeStringGeeftAlleReservatiesTerug(){
        reservaties = repository.geefReservaties();
        String zoekterm = "";
        repository.Zoek(zoekterm);
        Assert.assertEquals(data.getSortedReservaties().size(), reservaties.size());
    }
    @Test
    public void testZoekReservatieSlechteZoektermGeeftGeenReservatieTerug(){
        reservaties = repository.geefReservaties();
        String zoekterm = "blablabla";
        repository.Zoek(zoekterm);
        Assert.assertEquals(0, reservaties.size());
    }
    @Test
    public void testZoekReservatieOpCorrecteBeginDatumGeeftReservatieTerug(){
        reservaties = repository.geefReservaties();
        Date beginDatum = data.getReservatieStudent1().getBeginDatum();
        repository.zoekOpBeginDatum(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        Assert.assertEquals(2, reservaties.size());
    }
    @Test
    public void testZoekReservatieOpSlechteBeginDatumGeeftGeenReservatieTerug(){
        reservaties = repository.geefReservaties();
        Date beginDatum = new Date(111, 4, 11);
        repository.zoekOpBeginDatum(beginDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        Assert.assertEquals(0, reservaties.size());
    }
    @Test
    public void geefReservatiesByCorrecteDatumGeeftReservatieTerug(){
        Reservatie reservatie = data.getReservatieStudent1();
        Date beginDatum = reservatie.getBeginDatum();
        Date eindDatum = reservatie.getEindDatum();
        Materiaal materiaal = reservatie.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, reservatie.getMateriaal())).thenReturn(Arrays.asList(reservatie));

        Assert.assertEquals(1, repository.geefReservatiesByDatum(beginDatum, eindDatum, materiaal).size());
    }
    @Test
    public void geefReservatiesBySlechteDatumGeeftGeenReservatieTerug(){
        Reservatie reservatie = data.getReservatieStudent1();
        Date beginDatum = new Date(114, 1, 1);
        Date eindDatum = new Date(114, 1, 1);
        Materiaal materiaal = reservatie.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, reservatie.getMateriaal())).thenReturn(Arrays.asList());
        Assert.assertEquals(0, repository.geefReservatiesByDatum(beginDatum, eindDatum, materiaal).size());
    }
    @Test
    public void berekenAantalBeschikbaar4ReservatieAantal0Return4BeschikbaarStudent(){
        Reservatie r1 = data.getReservatieStudent1();
        int[] data = berekenAantalBeschikbaar(Arrays.asList(), r1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];
        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaar4ReservatieAantal4Returns0BeschikbaarStudent(){
        Reservatie r1 = data.getReservatieStudent1();
        int[] data = berekenAantalBeschikbaar(Arrays.asList(r1), r1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];
        Assert.assertEquals(2, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent1_ReservatieAantal3_Returns4BeschikbaarLector_1OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();
        Reservatie reservatieStudent2 = data.getReservatieStudent2();

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1, reservatieStudent2), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(1, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent0_ReservatieAantal3_Returns4BeschikbaarLector_2OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();
        Reservatie reservatieStudent2 = data.getReservatieStudent2();
        reservatieStudent2.setAantalGereserveerd(2);

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1, reservatieStudent2), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(2, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarLector1_ReservatieAantal3_Returns1BeschikbaarLector_0OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();


        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieLector1), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(1, aantalBeschikbaar);
        Assert.assertEquals(2, aantalOverruled);
    }
    @Test
    public void berekenAantalBeschikbaarStudent2_ReservatieAantal2_Returns4BeschikbaarLector_0OverruledStudent(){
        Reservatie reservatieLector1 = data.getReservatieLector1();
        Reservatie reservatieStudent1 = data.getReservatieStudent1();

        int[] data = berekenAantalBeschikbaar(Arrays.asList(reservatieStudent1), reservatieLector1);
        int aantalBeschikbaar = data[0];
        int aantalOverruled = data[1];

        Assert.assertEquals(4, aantalBeschikbaar);
        Assert.assertEquals(0, aantalOverruled);
    }
    @Test
    public void verwijder2BinnengebrachteReservatiesReturns2(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie r2 = data.getReservatieStudent2();

        Date gisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));
        Date eergisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(2));

        r1.setEindDatum(gisteren);
        r2.setEindDatum(eergisteren);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1,r2));
        Set<Reservatie> teVerwijderenReservaties = repository.verwijderVervallenReservaties();
        Assert.assertEquals(2, teVerwijderenReservaties.size() );
    }
    @Test
    public void verwijder1BinnengebrachteReservatiesReturns1(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie r2 = data.getReservatieStudent2();

        Date gisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));
        Date morgen = convertLocalDateToDate(convertDateToLocalDate(new Date()).plusDays(1));

        r1.setEindDatum(gisteren);
        r2.setEindDatum(morgen);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1,r2));
        Set<Reservatie> teVerwijderenReservaties = repository.verwijderVervallenReservaties();
        Assert.assertEquals(1, teVerwijderenReservaties.size() );
        Assert.assertEquals(r1, teVerwijderenReservaties.iterator().next());
    }
    @Test
    public void verwijderReservatieMetOnvoldoendeBinnengebrachteStuksNa1Week(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie r2 = data.getReservatieStudent2();
        Reservatie r3 = data.getReservatieStudent3();

        Date vorigeWeek = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(7));
        Date gisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));

        r1.setEindDatum(vorigeWeek);
        r1.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
        r3.setEindDatum(vorigeWeek);
        r2.setEindDatum(gisteren);
        r2.setReservatieStateEnum(ReservatieStateEnum.TeLaat);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1,r2,r3));
        Set<Reservatie> teVerwijderenReservaties = repository.verwijderVervallenReservaties();
        Assert.assertEquals(3, teVerwijderenReservaties.size() );
    }
    @Test
    public void verwijderNooitUitgeleendeReservatie(){
        Reservatie r1 = data.getReservatieStudent1();

        Date vorigeWeek = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(7));

        r1.setEindDatum(vorigeWeek);
        r1.setAantalUitgeleend(0);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1));
        Set<Reservatie> teVerwijderenReservaties = repository.verwijderVervallenReservaties();
        Assert.assertEquals(1, teVerwijderenReservaties.size() );
        Assert.assertEquals(r1, teVerwijderenReservaties.iterator().next());
    }
    @Test
    public void aanpassen1TelaatBinnenGebrachteReservatiesReturns1(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie r2 = data.getReservatieStudent2();

        Date morgen = convertLocalDateToDate(convertDateToLocalDate(new Date()).plusDays(1));
        Date gisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));

        r1.setEindDatum(gisteren);
        r1.setAantalUitgeleend(2);
        r2.setEindDatum(morgen);
        r2.setAantalUitgeleend(2);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1,r2));
        Set<Reservatie> teVerwijderenReservaties = repository.aanpassenTeLaatTerugGebrachteReservaties();
        Assert.assertEquals(1, teVerwijderenReservaties.size());
        Assert.assertEquals(r1, teVerwijderenReservaties.iterator().next());
    }
    @Test
    public void aanpassen1NooitUitgeleendeReservatie0(){
        Reservatie r1 = data.getReservatieStudent1();

        Date gisteren = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));

        r1.setEindDatum(gisteren);
        r1.setAantalUitgeleend(0);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1));
        Set<Reservatie> teVerwijderenReservaties = repository.aanpassenTeLaatTerugGebrachteReservaties();
        Assert.assertEquals(0, teVerwijderenReservaties.size());
    }
    @Test
    public void aanpassenOpeenVolgendeReservatiesTeLaatTeruggebrachteReservaties2BeschikbaarReturns0(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie opV1 = data.getReservatieStudent2();

        Date vorigeWeekStart = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(13));
        Date vorigeWeekEind = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(8));

        Date gisterenStart = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(6));
        Date gisterenEind = convertLocalDateToDate(convertDateToLocalDate(new Date()).minusDays(1));

        r1.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
        r1.setStartDatum(vorigeWeekStart);
        r1.setEindDatum(vorigeWeekEind);
        r1.setAantalGereserveerd(3);
        r1.setAantalUitgeleend(3);

        opV1.setStartDatum(gisterenStart);
        opV1.setEindDatum(gisterenEind);
        opV1.setAantalGereserveerd(2);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1, opV1));
        Set<Reservatie> teVerwijderenReservaties = repository.aanpassenOpeenvolgendeReservaties(new HashSet<Reservatie>(Arrays.asList(r1)));
        Assert.assertEquals(0, teVerwijderenReservaties.size());
    }
    @Test
    public void aanpassenOpeenVolgendeReservatiesTeLaatTeruggebrachteReservaties0BeschikbaarReturns1(){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie opV1 = data.getReservatieStudent2();

        Date vorigeWeekStart = HulpMethode.getFirstDayOfWeek(17);
        Date vorigeWeekEind = HulpMethode.convertLocalDateToDate(vorigeWeekStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

        Date dezeWeekStart = HulpMethode.getFirstDayOfWeek(18);
        Date dezeWeekEind = HulpMethode.convertLocalDateToDate(dezeWeekStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

        r1.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
        r1.setStartDatum(vorigeWeekStart);
        r1.setEindDatum(vorigeWeekEind);
        r1.setAantalGereserveerd(3);
        r1.setAantalUitgeleend(3);

        opV1.setStartDatum(dezeWeekStart);
        opV1.setEindDatum(dezeWeekEind);
        opV1.setAantalGereserveerd(4);

        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1, opV1));
        Set<Reservatie> teVerwijderenReservaties = repository.aanpassenOpeenvolgendeReservaties(new HashSet<Reservatie>(Arrays.asList(r1)));
        Reservatie result = teVerwijderenReservaties.iterator().next();
        Assert.assertEquals(1, teVerwijderenReservaties.size());
        Assert.assertEquals(opV1, result);
        Assert.assertEquals(ReservatieStateEnum.TeLaat, result.getReservatieStateEnum());
    }
    @Test
    public void wijzigLaatBinnenGebrachteReservatieWijzigtAfhankelijkeReservatiesReturns2(){
        Reservatie r1 = wijzigLaatBinnenGebrachteReservatiesHulp(2, 1);
        Set<Reservatie> gewijzigdeReservateis = repository.wijzigLaatbinnenGebrachteReservatie(r1);
        Assert.assertEquals(2, gewijzigdeReservateis.size());

    }
    @Test
    public void wijzigLaatBinnenGebrachteReservatieWijzigtAfhankelijkeReservatiesReturns1(){
        Reservatie r1 = wijzigLaatBinnenGebrachteReservatiesHulp(3,1);
        Set<Reservatie> gewijzigdeReservateis = repository.wijzigLaatbinnenGebrachteReservatie(r1);
        Assert.assertEquals(1, gewijzigdeReservateis.size());

    }
    private Reservatie wijzigLaatBinnenGebrachteReservatiesHulp(int aantalReservatie1, int aantalReservatie2){
        Reservatie r1 = data.getReservatieStudent1();
        Reservatie opV1 = data.getReservatieStudent2();
        Reservatie opV2 = data.getReservatieStudent4();

        Date vorigeWeekStart = HulpMethode.getFirstDayOfWeek(17);
        Date vorigeWeekEind = HulpMethode.convertLocalDateToDate(vorigeWeekStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

        Date dezeWeekStart = HulpMethode.getFirstDayOfWeek(18);
        Date dezeWeekEind = HulpMethode.convertLocalDateToDate(dezeWeekStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(4));

        r1.setReservatieStateEnum(ReservatieStateEnum.Gereserveerd);
        r1.setStartDatum(vorigeWeekStart);
        r1.setEindDatum(vorigeWeekEind);
        r1.setAantalGereserveerd(3);
        r1.setAantalUitgeleend(3);
        r1.setAantalTeruggebracht(3);

        opV1.setStartDatum(dezeWeekStart);
        opV1.setEindDatum(dezeWeekEind);
        opV1.setAantalGereserveerd(aantalReservatie1);
        opV1.setReservatieStateEnum(ReservatieStateEnum.TeLaat);

        opV2.setStartDatum(dezeWeekStart);
        opV2.setEindDatum(dezeWeekEind);
        opV2.setAantalGereserveerd(aantalReservatie2);
        opV2.setReservatieStateEnum(ReservatieStateEnum.TeLaat);
        Mockito.when(reservatieDao.findAll()).thenReturn(Arrays.asList(r1, opV1, opV2));
        return r1;
    }

    private Date convertLocalDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private LocalDate convertDateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private int[] berekenAantalBeschikbaar(List<Reservatie> reservaties, Reservatie data){
        Date beginDatum = data.getBeginDatum();
        Date eindDatum = data.getEindDatum();
        Materiaal materiaal = data.getMateriaal();
        Mockito.when(reservatieDao.getReservaties(beginDatum, eindDatum, data.getMateriaal())).thenReturn(reservaties);
        return repository.berekenAantalbeschikbaarMateriaal(data.getGebruiker(), beginDatum, eindDatum, data.getMateriaal(), 2, 0);
    }
}
