package dbModels;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "COMPETITOR")
public class Competitor implements Serializable {

    @Id
    @Column(name = "pesel", nullable = false)
    private Long pesel;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "gender", nullable = false, length = 2)
    private String gender;

    // many competitors one club
    @ManyToOne
    @JoinColumn(name = "CLUB_CLUB_ID", nullable = false)
    private Club club;

    // many records one competitor
    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Record> records = new ArrayList<>();

    // many competitors many contests with "competitors_in_contest" entity
    @ManyToMany(mappedBy = "competitors")
    private List<Contest> contests = new ArrayList<>();

    public String toString() {
        return name + " " + surname;
    }
}
