package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Created by donovandesmedt on 08/04/16.
 */
@Entity
@Table(name = "Dag")
public class Dag implements Comparable<Dag>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "DagId")
    public int dagId;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "Datum")
    public Date datum;

    protected Dag()
    {
    }

    public Dag(Date datum)
    {
        setDatum(datum);
    }

    public Date getDatum()
    {
        return datum;
    }

    public void setDatum(Date datum)
    {
        this.datum = datum;
    }

    public int getDagId()
    {
        return dagId;
    }

    @Override
    public int compareTo(Dag o)
    {
        return getDatum().before(o.getDatum()) ? 1 : getDatum().equals(o.getDatum()) ? 0 : -1;
    }
}
