/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

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
   private Date beginDatum,eindDatum;
   
   private int reservatieEnum;
   
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "Gebruiker")   
   private Gebruiker gebruiker;
   
   
   @OneToOne(cascade=CascadeType.PERSIST)
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
