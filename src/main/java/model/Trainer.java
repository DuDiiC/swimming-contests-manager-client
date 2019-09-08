package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    private Long licenceNr;

    private String name;

    private String surname;

    // many trainers one club
    private Club club;

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
