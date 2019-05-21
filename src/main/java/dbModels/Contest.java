package dbModels;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CONTEST")
@SequenceGenerator(name = "generate_contest_id", sequenceName = "generate_contest_id", allocationSize = 1)
public class Contest implements Serializable {

    @Id
    @Column(name = "contest_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generate_contest_id")
    private Long contest_id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "CONTEST_DATE", nullable = false)
    private Date date;

    @Column(name = "city", length = 50)
    private String city;

    // wiele zawodow do ktorych przypisanych jest wiele konkurencji prez tabele "w_zawodach"
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "COMPETITIONS_IN_CONTESTS",
            joinColumns = { @JoinColumn(name = "CONTEST_CONTEST_ID") },
            inverseJoinColumns = { @JoinColumn(name = "COMPETITION_COMPETITION_ID") }
    )
    private List<Competition> competitions = new ArrayList<>();

    // wiele zawodow w ktorych bierze udzial wielu zawodnikow przez tabele "bierze_udzial_w"
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "COMPETITORS_IN_CONTESTS",
            joinColumns = { @JoinColumn(name = "CONTEST_CONTEST_ID") },
            inverseJoinColumns = { @JoinColumn(name = "COMPETITOR_PESEL") }
    )
    private List<Competitor> competitors = new ArrayList<>();

    public Contest() {
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
        String stringDate = sdf.format(date);
        return name + " (" + city + ", " + stringDate + ")";
    }

    public Long getContest_id() {
        return contest_id;
    }

    public void setContest_id(Long contest_id) {
        this.contest_id = contest_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }
}
