package model.wrapper;

import accessors.ClubsAccessor;
import configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Club;
import model.Competitor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * The intermediate class between the representation of the competitor in JSON format
 * and the model class {@link Competitor} in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorWrapper {

    private Long pesel;

    private String name;

    private String surname;

    private String gender;

    // many competitors one club
    private Long clubId;

    /**
     * @return {@link CompetitorWrapper} object corresponds with the selected {@link Competitor} object.
     */
    public static CompetitorWrapper wrap(Competitor competitor) {
        return new CompetitorWrapper(
                competitor.getPesel(),
                competitor.getName(),
                competitor.getSurname(),
                competitor.getGender(),
                competitor.getClub().getId()
        );
    }

    /**
     * @return {@link Competitor} object corresponds with the actual instance of {@link CompetitorWrapper} class.
     */
    public Competitor unwrap() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getInstance().getServerAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClubsAccessor clubsAccessor = retrofit.create(ClubsAccessor.class);

        Club club = clubsAccessor.getOne(clubId).execute().body();

        return new Competitor(
                pesel,
                name,
                surname,
                gender,
                club
        );
    }

}