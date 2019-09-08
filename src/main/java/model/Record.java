package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Comparable<Record> {
    private Long id;

    private Integer minutes;

    private Integer seconds;

    private Integer hundredths;

    // many records one competitor
    private Competitor competitor;

    // many records one competition
    private Competition competition;

    @Override
    public String toString() {
        String time = "";
        if (minutes != null && minutes != 0) {
            if (minutes >= 10) {
                time += minutes.toString();
            } else {
                time += "0" + minutes.toString();
            }
        } else {
            time += "00";
        }
        time += ":";
        if (seconds >= 10) {
            time += seconds.toString();
        } else {
            time += "0" + seconds.toString();
        }
        time += ":";
        if (hundredths >= 10) {
            time += hundredths.toString();
        } else {
            time += "0" + hundredths.toString();
        }

        return time;

    }

    // TODO: NAPRAWIC
    @Override
    public int compareTo(Record o) {
        Double left = (this.minutes.doubleValue() * 60.0) + this.seconds.doubleValue() + (this.hundredths.doubleValue() / 100.0);
        Double right = (o.minutes.doubleValue() * 60.0) + this.seconds.doubleValue() + (o.hundredths.doubleValue() / 100.0);
        return Double.compare(left, right);
    }
}