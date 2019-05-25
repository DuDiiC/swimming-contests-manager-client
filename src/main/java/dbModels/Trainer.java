package dbModels;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRAINER")
@SequenceGenerator(name = "generate_trainer_id", sequenceName = "generate_trainer_id", allocationSize = 1)
public class Trainer implements Serializable {

    @Id
    @Column(name = "licence_nr", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generate_trainer_id")
    private Long licenceNr;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    // many trainers one club
    @ManyToOne
    @JoinColumn(name = "CLUB_CLUB_ID", nullable = false)
    private Club club;

    public Trainer() {
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    public long getLicenceNr() {
        return licenceNr;
    }

    public void setLicenceNr(long licenceNr) {
        this.licenceNr = licenceNr;
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

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
