package dbModels;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPETITOR")
public class Competitor implements Serializable {

    @Id
    @Column(name = "pesel", nullable = false)
    private Long pesel;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "gender", nullable = false, length = 2)
    private String gender;

    // wielu zawodnikow w jednym klubie
    @ManyToOne
    @JoinColumn(name = "CLUB_CLUB_ID", nullable = false)
    private Club club;

    // wiele rekordow dla jednego zawodnika
    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Record> records = new ArrayList<>();

    // wielu zawodnikow bierze udzial w wielu zawodach przez tabele "bierze_udzial_w"
    @ManyToMany(mappedBy = "competitors")
    private List<Contest> contests = new ArrayList<>();

    public Competitor() {
    }

    public String toString() {
        return name + " " + surname;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }
}
