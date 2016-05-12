package domein;

import javax.persistence.*;
import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name="ReservatieId")
    public Reservatie reservatie;

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
