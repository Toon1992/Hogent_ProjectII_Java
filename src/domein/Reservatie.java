/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.sql.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
   private Date beginDatum,eindDatum;
   private int reservatieEnum;
   
    @OneToOne
    @JoinColumn(name = "Gebruiker")
   private Gebruiker gebruiker;
   
   
   @OneToOne
   @JoinColumn(name = "Materiaal")
   private Materiaal materiaal;
   
   protected Reservatie(){};
   
   public Reservatie(int reservatieID, int aantal, Date beginDatum, Date eindDatum, int reservatieEnum, Gebruiker gebruiker, Materiaal materiaal)
   {
       setReservatieID(reservatieID);
       setAantal(aantal);
       setBeginDatum(beginDatum);
       setEindDatum(eindDatum);
       setReservatieEnum(reservatieEnum);
       setGebruiker(gebruiker);
       setMateriaal(materiaal);      
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
   
   public ObjectProperty beginDatumProperty()
   {
       return new SimpleObjectProperty(getBeginDatum());
   }
   
     public ObjectProperty eindDatumProperty()
   {
       return new SimpleObjectProperty(getEindDatum());
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

    public int getReservatieEnum()
    {
        return reservatieEnum;
    }

    protected void setReservatieEnum(int reservatieEnum)
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
