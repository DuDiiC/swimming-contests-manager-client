package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competitor {
    private Long pesel;

    private String name;

    private String surname;

    private String gender;

    // many competitors one club
    private Club club;

    public String toString() {
        return name + " " + surname;
    }
}
