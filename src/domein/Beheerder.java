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
    @NamedQuery(name = "Beheerder.findByEmail", query = "Select b FROM Beheerder b WHERE b.email = :Email"),
    @NamedQuery(name = "Beheerder.FindAll", query = "SELECT b FROM Beheerder b")
})
@Table(name="Beheerder")
public class Beheerder
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GebruikersId")
    private int gebruikersId;
    
    @Column(name = "Email")
    private String email;
    
    @Column(name = "IsHoofd")
    private boolean isHoofd;

    public boolean isHoofd()
    {
        return isHoofd;
    }

    public void setIsHoofd(boolean isHoofd)
    {
        this.isHoofd = isHoofd;
    }

    protected Beheerder()
    {
    }

    public Beheerder(String email, boolean isHoofd)
    {
        this.email = email;
        this.isHoofd = isHoofd;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
    

}
