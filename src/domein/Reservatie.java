/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

import stateMachine.Geblokkeerd;
import stateMachine.Gereserveerd;
import stateMachine.Opgehaald;
import stateMachine.Overruled;
import stateMachine.ReservatieGebruikerEnum;
import stateMachine.ReservatieState;
import stateMachine.ReservatieStateEnum;
import stateMachine.TeLaat;

/**
 *
 * @author ToonDT
 */
@Entity
@NamedQueries(
        {
            @NamedQuery(name = "Reservatie.findBydatum", query = "Select r FROM Reservatie r WHERE r.materiaal = :Materiaal  AND (:EindDatum >= r.startDatum AND :StartDatum <= r.eindDatum)")
        })
public class Reservatie
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservatieID;

    private int aantalUitgeleend;
    private int aantalGereserveerd;
    private int aantalTeruggebracht;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDatum, eindDatum, aanmaakDatum;

    @Enumerated(EnumType.ORDINAL)
    private ReservatieStateEnum reservatieStateEnum;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "GebruikerEmail")
    private Gebruiker gebruiker;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MateriaalId")
    private Materiaal materiaal;

    @Transient
    private ReservatieState reservatieState;

    @Enumerated(EnumType.STRING)
    @Column(name = "Discriminator")
    private ReservatieGebruikerEnum discriminator;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<Dag> dagen = new HashSet<>();

    protected Reservatie()
    {
    }

    ;
   
   public Reservatie(int aantalGereserveerd, int aantalUitgeleend, int aantalTeruggebracht, Date startDatum, Date eindDatum, Date aanmaakDatum, Set<Dag> dagen, ReservatieStateEnum reservatieEnum, Gebruiker gebruiker, Materiaal materiaal)
    {
        setAantalGereserveerd(aantalGereserveerd);
        setAantalTeruggebracht(aantalTeruggebracht);
        setAantalUitgeleend(aantalUitgeleend);
        setStartDatum(startDatum);
        setEindDatum(eindDatum);
        setAanmaakDatum(aanmaakDatum);
        setReservatieStateEnum(reservatieEnum);
        setGebruiker(gebruiker);
        setMateriaal(materiaal);
        setDiscriminator(gebruiker.getType());
        setDagen(dagen);
        reservatieState = getReservatieState();
    }

    public void toState(ReservatieState reservatieState)
    {
        this.reservatieState = reservatieState;
        this.reservatieState.setReservatie(this);
    }

    public StringProperty statusProperty()
    {
        System.out.println(getReservatieState().getClass().getSimpleName());
        return new SimpleStringProperty(getReservatieState().getClass().getSimpleName());
    }

    public StringProperty naamGebruikerProperty()
    {
        return gebruiker.naamProperty();
    }

    public StringProperty naamMateriaalProperty()
    {
        return materiaal.naamProperty();
    }

    public IntegerProperty aantalProperty()
    {
        return new SimpleIntegerProperty(getAantalGereserveerd());
    }

    public StringProperty beginDatumProperty()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return new SimpleStringProperty(dateFormat.format(startDatum));
    }

    public StringProperty eindDatumProperty()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return new SimpleStringProperty(dateFormat.format(eindDatum));
    }

    public ReservatieState getReservatieState()
    {
        switch (reservatieStateEnum)
        {
            case Geblokkeerd:
                return new Geblokkeerd(this);
            case Gereserveerd:
                return new Gereserveerd(this);
            case Opgehaald:
                return new Opgehaald(this);
            case TeLaat:
                return new TeLaat(this);
            case Overruled:
                return new Overruled(this);
        }
        return null;
    }

    public void setReservatieState(ReservatieState reservatieState)
    {
        this.reservatieState = reservatieState;

        switch (reservatieState.getClass().getSimpleName())
        {
            case "Geblokkeerd":
                reservatieStateEnum = ReservatieStateEnum.Geblokkeerd;
                break;
            case "Gereserveerd":
                reservatieStateEnum = ReservatieStateEnum.Gereserveerd;
                break;
            case "TeLaat":
                reservatieStateEnum = ReservatieStateEnum.TeLaat;
                break;
            case "Opgehaald":
                reservatieStateEnum = ReservatieStateEnum.Opgehaald;
                break;
            case "Overruled":
                reservatieStateEnum = ReservatieStateEnum.Overruled;
                break;

        }
    }

    public int getReservatieID()
    {
        return reservatieID;
    }

    public int getAantalGereserveerd()
    {
        return aantalGereserveerd;
    }

    public int getAantalTeruggebracht()
    {
        return aantalTeruggebracht;
    }

    public void setAantalGereserveerd(int aantalUitgeleend)
    {
        this.aantalGereserveerd = aantalUitgeleend;
    }

    public void setAantalTeruggebracht(int aantalTeruggebracht)
    {
        this.aantalTeruggebracht = aantalTeruggebracht;
    }

    public int getAantalUitgeleend()
    {
        return aantalUitgeleend;
    }

    public void setAantalUitgeleend(int aantalUitgeleend)
    {
        this.aantalUitgeleend = aantalUitgeleend;
    }

    public Date getBeginDatum()
    {
        return startDatum;
    }

    public void setStartDatum(Date startDatum)
    {
        this.startDatum = startDatum;
    }

    public Date getEindDatum()
    {
        return eindDatum;
    }

    public void setEindDatum(Date eindDatum)
    {
        this.eindDatum = eindDatum;
    }

    public ReservatieStateEnum getReservatieStateEnum()
    {
        return reservatieStateEnum;
    }

    public void setReservatieStateEnum(ReservatieStateEnum reservatieStateEnum)
    {
        this.reservatieStateEnum = reservatieStateEnum;
    }

    public Set<Dag> getDagen()
    {
        return dagen;
    }

    public void setDagen(Set<Dag> dagen)
    {
        this.dagen = dagen;
    }

    public Gebruiker getGebruiker()
    {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker)
    {
        this.gebruiker = gebruiker;
    }

    public Materiaal getMateriaal()
    {
        return materiaal;
    }

    public void setMateriaal(Materiaal materiaal)
    {
        this.materiaal = materiaal;
    }

    public Date getAanmaakDatum()
    {
        return aanmaakDatum;
    }

    public void setAanmaakDatum(Date aanmaakDatum)
    {
        this.aanmaakDatum = aanmaakDatum;
    }

    public ReservatieGebruikerEnum getDiscriminator()
    {
        return discriminator;
    }

    public void setDiscriminator(String type)
    {
        switch (type.toLowerCase())
        {
            case "st":
                this.discriminator = discriminator.ReservatieStudent;
                break;
            case "le":
                this.discriminator = discriminator.BlokkeringLector;
                break;
        }
    }

}
