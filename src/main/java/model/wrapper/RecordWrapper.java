package model.wrapper;

import accessors.CompetitionsAccessor;
import accessors.CompetitorsAccessor;
import configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Competition;
import model.Competitor;
import model.Record;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * The intermediate class between the representation of the record in JSON format
 * and the model class {@link Record} in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordWrapper {

    private Long id;

    private Integer minutes;

    private Integer seconds;

    private Integer hundredths;

    // many records one competitor
    private Long competitorPesel;

    // many records one competition
    private Long competitionId;

    /**
     * @return {@link RecordWrapper} object corresponds with the selected {@link Record} object.
     */
    public static RecordWrapper wrap(Record record) {
        return new RecordWrapper(
                record.getId(),
                record.getMinutes(),
                record.getSeconds(),
                record.getHundredths(),
                record.getCompetitor().getPesel(),
                record.getCompetition().getId()
        );
    }

    /**
     * @return {@link Record} object corresponds with the actual instance of {@link RecordWrapper} class.
     */
    public Record unwrap() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getInstance().getServerAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CompetitorsAccessor competitorsAccessor = retrofit.create(CompetitorsAccessor.class);
        CompetitionsAccessor competitionsAccessor = retrofit.create(CompetitionsAccessor.class);

        Competitor competitor = competitorsAccessor.getOne(competitorPesel).execute().body().unwrap();
        Competition competition = competitionsAccessor.getOne(competitionId).execute().body();

        return new Record(
                id,
                minutes,
                seconds,
                hundredths,
                competitor,
                competition
        );
    }
}
