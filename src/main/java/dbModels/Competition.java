package dbModels;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPETITION")
@SequenceGenerator(name = "generate_competition_id", sequenceName = "generate_competition_id", allocationSize = 1)
public class Competition implements Serializable {

    @Id
    @Column(name="competition_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generate_competition_id")
    private Long competitionId;

    @Column(name = "style", nullable = false, length = 20)
    private String style;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "gender", nullable = false, length = 2)
    private String gender;

    // jedna konkurencja z wieloma rekordami
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Record> records = new ArrayList<>();

    // wiele konkrencji przypisanych do wielu zawodow przez tabele "w_zawodach"
    @ManyToMany(mappedBy = "competitions")
    private List<Contest> contests = new ArrayList<>();

    public Competition() {
    }

    @Override
    public String toString() {
        return style + " " + distance.toString() + "m";
    }

    public long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(long competitionId) {
        this.competitionId = competitionId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }
}

