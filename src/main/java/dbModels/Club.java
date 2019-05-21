package dbModels;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLUB")
@SequenceGenerator(name = "generate_club_id", sequenceName = "generate_club_id", allocationSize = 1)
public class Club implements Serializable {

    @Id
    @Column(name = "club_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generate_club_id")
    private long clubId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    // jeden klub ma wielu trenerow
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trainer> trainers = new ArrayList<>();

    // jeden klub ma wielu zawodnikow
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Competitor> competitors = new ArrayList<>();

    public Club() {
    }

    @Override
    public String toString() {
        return name + " " + city;
    }

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }
}


