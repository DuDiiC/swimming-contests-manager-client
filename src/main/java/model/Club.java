package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    private long id;

    private String name;

    private String city;

    public String toString() {
        return name + " " + city;
    }
}