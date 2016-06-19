package domein;

import javax.persistence.Entity;

/**
 * Created by donovandesmedt on 19/06/16.
 */
@Entity
public class BlokkeringStudent extends MailTemplate{
    protected BlokkeringStudent(){}
    public BlokkeringStudent(String onderwerp, String body){super(onderwerp, body);}
}
