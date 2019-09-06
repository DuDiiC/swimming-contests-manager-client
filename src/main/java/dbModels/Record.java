package dbModels;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "RECORD")
@SequenceGenerator(name = "generate_record_id", sequenceName = "generate_record_id", allocationSize = 1)
public class Record implements Serializable, Comparable<Record> {

    @Id
    @Column(name = "record_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generate_record_id")
    private Long recordId;

    @Column(name = "minutes", length = 2)
    private Integer minutes;

    @Column(name = "seconds", nullable = false, length = 2)
    private Integer seconds;

    @Column(name = "hundredth", nullable = false, length = 2)
    private Integer hundredth;

    // many records one competitor
    @ManyToOne
    @JoinColumn(name = "COMPETITOR_PESEL")
    private Competitor competitor;

    // many records one competition
    @ManyToOne
    @JoinColumn(name = "COMPETITION_COMPETITION_ID", nullable = false)
    private Competition competition;

    @Override
    public String toString() {
        String time = "";
        if(minutes != null && minutes != 0) {
            if(minutes >= 10) {
                time += minutes.toString();
            } else {
                time += "0" + minutes.toString();
            }
        } else {
            time += "00";
        }
        time += ":";
        if(seconds >= 10) {
            time += seconds.toString();
        } else {
            time += "0" + seconds.toString();
        }
        time += ":";
        if(hundredth >= 10) {
            time += hundredth.toString();
        } else {
            time += "0" + hundredth.toString();
        }

        return time;
    }

    /**
     * Jesli jest dodatnie to nowy czas jest wolniejszy, jesli ujemne, to nowy czas jest szybszy, jesli rowne to sa takie same
     * this 00:30:30
     * o    00:30:12
     */
    @Override
    public int compareTo(Record o) {
        int thisWholeSeconds = this.minutes*60 + this.seconds;
        int oWholeSeconds = o.getMinutes()*60 + o.getSeconds();
        if(thisWholeSeconds != oWholeSeconds) {
            // jesli czas z this jest wolniejszy to dodatnie, wpw. ujemne
            return thisWholeSeconds-oWholeSeconds;
        } else {
            // jesli czas z this jest wolniejszy to dodatnie
            return this.hundredth-o.getHundredth();
        }
    }
}

