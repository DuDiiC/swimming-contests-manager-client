package accessors;

import model.Competition;
import model.wrapper.RecordWrapper;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface RecordsAccessor {

    /**
     * Method to download all {@link RecordWrapper}s from the database.
     */
    @GET("/api/record")
    Call<List<RecordWrapper>> getAll();

    /**
     * Method to download all {@link RecordWrapper}s from selected {@link model.Competitor}'s id from the database.
     */
    @GET("/api/record/competitor/{pesel}")
    Call<List<RecordWrapper>> getAllByCompetitor(@Path("pesel") Long pesel);

    /**
     * Method to download {@link RecordWrapper} with selected id from the database.
     */
    @GET("/api/record/{id}")
    Call<RecordWrapper> getOne(@Path("id") Long id);

    /**
     * Method to download the best {@link RecordWrapper} from {@link Competition} with selected id from the database.
     */
    @GET("/api/record/competition/{competitionId}")
    Call<RecordWrapper> getBestByCompetition(@Path("competitionId") Long competitionId);

    /**
     * Method to add selected {@link RecordWrapper} to the database.
     */
    @POST("/api/record")
    Call<RecordWrapper> add(@Body RecordWrapper record);

    /**
     * Method to remove {@link RecordWrapper} with selected id from the database.
     */
    @DELETE("/api/record/{id}")
    Call<RecordWrapper> remove(@Path("id") Long id);
}
