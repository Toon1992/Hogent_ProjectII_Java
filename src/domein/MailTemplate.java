/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.*;

/**
 *
 * @author manu
 */
@Entity
@Table(name = "MailTemplate")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
{
    @NamedQuery(name = "MailTemplate.findbyOnderwerp", query = "Select a FROM MailTemplate a WHERE a.subject= :onderwerp")
})
@DiscriminatorColumn(name = "onderwerp")
public abstract class MailTemplate {
   
    @Id
    @Column(name = "Subject")
    private String subject;
    @Column(name = "Body")
    @Lob
    private String body;
//    @Column(name = "DTYPE")
//    private String dtype;
//    @Column(name = "onderwerp")
//    private String onderwerp;
    
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
