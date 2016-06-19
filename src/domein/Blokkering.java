package domein;

import javax.persistence.Entity;

/**
 * Created by donovandesmedt on 19/06/16.
 */
@Entity
public class Blokkering extends MailTemplate{
    protected Blokkering(){}
    public Blokkering(String onderwerp, String body){super(onderwerp, body);}
}
