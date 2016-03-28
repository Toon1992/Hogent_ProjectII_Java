/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import stateMachine.Geblokkeerd;
import stateMachine.Gereserveerd;
import stateMachine.Opgehaald;
import stateMachine.Overruled;
import stateMachine.ReservatieState;
import stateMachine.ReservatieStateEnum;
import static stateMachine.ReservatieStateEnum.Overruled;
import stateMachine.TeLaat;

/**
 *
 * @author ToonDT
 */
@Entity
public class Reservatie
{

    @Id
    private int reservatieID;

    private int aantal;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date beginDatum, eindDatum;

    @Enumerated(EnumType.STRING)
    @Column(name="Status")
    private ReservatieStateEnum reservatieEnum;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Gebruiker")
    private Gebruiker gebruiker;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Materiaal")
    private Materiaal materiaal;

    @Transient
    private ReservatieState reservatieState;

    protected Reservatie()
    {
    }

    ;
   
   public Reservatie(int reservatieID, int aantal, Date beginDatum, Date eindDatum, ReservatieStateEnum reservatieEnum, Gebruiker gebruiker, Materiaal materiaal)
    {
        setReservatieID(reservatieID);
        setAantal(aantal);
        setBeginDatum(beginDatum);
        setEindDatum(eindDatum);
        setReservatieEnum(reservatieEnum);
        setGebruiker(gebruiker);
        setMateriaal(materiaal);
        
        reservatieState = getReservatieState();
    }

    public void toState(ReservatieState reservatieState)
    {
        this.reservatieState = reservatieState;
        this.reservatieState.setReservatie(this);
    }

    public StringProperty statusProperty()
    {
        return new SimpleStringProperty(reservatieState.getClass().getSimpleName());
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
        return new SimpleIntegerProperty(getAantal());
    }

    public StringProperty beginDatumProperty()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return new SimpleStringProperty(dateFormat.format(eindDatum));
    }

    public StringProperty eindDatumProperty()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return new SimpleStringProperty(dateFormat.format(eindDatum));
    }

    public ReservatieState getReservatieState()
    {
        switch (reservatieEnum)
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
                reservatieEnum = ReservatieStateEnum.Geblokkeerd;
                break;
            case "Gereserveerd":
                reservatieEnum = ReservatieStateEnum.Gereserveerd;
                break;
            case "TeLaat":
                reservatieEnum = ReservatieStateEnum.TeLaat;
                break;
            case "Opgehaald":
                reservatieEnum = ReservatieStateEnum.Opgehaald;
                break;
            case "Overruled":
                reservatieEnum = ReservatieStateEnum.Overruled;
                break;

        }
    }

    public int getReservatieID()
    {
        return reservatieID;
    }

    protected void setReservatieID(int reservatieID)
    {
        this.reservatieID = reservatieID;
    }

    public int getAantal()
    {
        return aantal;
    }

    protected void setAantal(int aantal)
    {
        this.aantal = aantal;
    }

    public Date getBeginDatum()
    {
        return beginDatum;
    }

    protected void setBeginDatum(Date beginDatum)
    {
        this.beginDatum = beginDatum;
    }

    public Date getEindDatum()
    {
        return eindDatum;
    }

    protected void setEindDatum(Date eindDatum)
    {
        this.eindDatum = eindDatum;
    }

    public ReservatieStateEnum getReservatieEnum()
    {
        return reservatieEnum;
    }

    protected void setReservatieEnum(ReservatieStateEnum reservatieEnum)
    {
        this.reservatieEnum = reservatieEnum;
    }

    public Gebruiker getGebruiker()
    {
        return gebruiker;
    }

    protected void setGebruiker(Gebruiker gebruiker)
    {
        this.gebruiker = gebruiker;
    }

    public Materiaal getMateriaal()
    {
        return materiaal;
    }

    protected void setMateriaal(Materiaal materiaal)
    {
        this.materiaal = materiaal;
    }

}
