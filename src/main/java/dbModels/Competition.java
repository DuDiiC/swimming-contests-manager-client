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

    // one competition many records
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Record> records = new ArrayList<>();

    // many competitions many contests with "competitions_in_contests" entity
    @ManyToMany(mappedBy = "competitions")
    private List<Contest> contests = new ArrayList<>();

    @Override
    public String toString() {
        return style + " " + distance.toString() + "m " + gender;
    }
}

