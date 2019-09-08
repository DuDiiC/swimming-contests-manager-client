package model.wrapper;

import accessors.ClubsAccessor;
import configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Club;
import model.Trainer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * The intermediate class between the representation of the trainer in JSON format
 * and the model class {@link Trainer} in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWrapper {

    private Long licenceNr;

    private String name;

    private String surname;

    // many trainers one club
    private Long clubId;

    /**
     * @return {@link TrainerWrapper} object corresponds with the selected {@link Trainer} object.
     */
    public static TrainerWrapper wrap(Trainer trainer) {
        return new TrainerWrapper(
                trainer.getLicenceNr(),
                trainer.getName(),
                trainer.getSurname(),
                trainer.getClub().getId()
        );
    }

    /**
     * @return {@link Trainer} object corresponds with the actual instance of {@link TrainerWrapper} class.
     */
    public Trainer unwrap() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getInstance().getServerAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClubsAccessor clubsAccessor = retrofit.create(ClubsAccessor.class);

        Club club = clubsAccessor.getOne(clubId).execute().body();

        return new Trainer(
                licenceNr,
                name,
                surname,
                club
        );
    }

}
