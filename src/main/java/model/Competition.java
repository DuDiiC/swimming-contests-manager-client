package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    private Long id;

    private String style;

    private Integer distance;

    private String gender;

    public String toString() {
        return style + " " + distance + " m " + gender;
    }
}

