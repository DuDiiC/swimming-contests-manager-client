package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    private Long id;

    private String name;

    private LocalDate date;

    private String city;

    // many contests many competitons
    private Set<Competition> competitions;

    // many contests many competitors
    private Set<Competitor> competitors;

    public String toString() {
        return name + " (" + city + ")";
    }
}
