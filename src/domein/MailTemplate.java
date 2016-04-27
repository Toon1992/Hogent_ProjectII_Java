/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author manu
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
{
    @NamedQuery(name = "MailTemplate.findbyOnderwerp", query = "Select a FROM MailTemplate a WHERE a.subject= :onderwerp")
})
@Table(name="MailTemplate")
public abstract class MailTemplate {
   
    @Column(name="Subject")
    @Id
    private String subject;
    
    @Column(name="Body")
    private String body;
    
     @Column(name="Onderwerp")
    private String onderwerp;
    
    protected MailTemplate(){
        
    }
    public MailTemplate(String onderwerp,String body){
        this.body=body;
        this.subject=onderwerp;
    }
    
    

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOnderwerp() {
        return subject;
    }

    public void setOnderwerp(String onderwerp) {
        this.subject = onderwerp;
    }
    
    
}
