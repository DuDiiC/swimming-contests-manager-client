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

    // one club many trainers
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trainer> trainers = new ArrayList<>();

    // one club many competitors
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Competitor> competitors = new ArrayList<>();

    @Override
    public String toString() {
        return name + " " + city;
    }
}


