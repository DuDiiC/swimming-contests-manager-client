package dbModels;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RECORD")
@SequenceGenerator(name = "generate_record_id", sequenceName = "generate_record_id", allocationSize = 1)
public class Record implements Serializable {

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

    // wiele rekordow dla jednego zawodnika
    @ManyToOne
    @JoinColumn(name = "COMPETITOR_PESEL")
    private Competitor competitor;

    // wiele rekordow dla jednej konkurencji
    @ManyToOne
    @JoinColumn(name = "COMPETITION_COMPETITION_ID", nullable = false)
    private Competition competition;

    public Record() {
    }

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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getHundredth() {
        return hundredth;
    }

    public void setHundredth(Integer hundredth) {
        this.hundredth = hundredth;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
}

