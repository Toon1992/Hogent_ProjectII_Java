/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@NamedQueries(
{
    @NamedQuery(name = "Beheerder.findByEmail", query = "Select b FROM Beheerder b WHERE b.email = :Email and b.wachtwoord = :Wachtwoord")
})
@Table(name="Beheerder")
public class Beheerder
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gebruikersId;
    
    @Column(name = "Email")
    private String email;
    
    @Column(name = "Wachtwoord")
    private String wachtwoord;
    private String naam;

    protected Beheerder()
    {
    }

    public Beheerder(String email, String naam, String wachtwoord)
    {
        this.email = email;
        this.naam = naam;
        this.wachtwoord = wachtwoord;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getWachtwoord()
    {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord)
    {
        this.wachtwoord = wachtwoord;
    }

    public String getNaam()
    {
        return naam;
    }

    public void setNaam(String naam)
    {
        this.naam = naam;
    }

}
