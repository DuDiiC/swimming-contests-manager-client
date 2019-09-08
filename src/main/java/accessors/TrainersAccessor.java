package accessors;

import model.Club;
import retrofit2.Call;
import retrofit2.http.*;
import model.wrapper.TrainerWrapper;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface TrainersAccessor {

    /**
     * Method to download all {@link TrainerWrapper}s from the database.
     */
    @GET("/api/trainer")
    Call<List<TrainerWrapper>> getAll();

    /**
     * Method to download all {@link TrainerWrapper}s from selected {@link Club}'s id from the database.
     */
    @GET("/api/trainer/club/{clubId}")
    Call<List<TrainerWrapper>> getAllByClub(@Path("clubId") Long clubId);

    /**
     * Method to download {@link TrainerWrapper} with selected id from the database.
     */
    @GET("/api/trainer/{id}")
    Call<TrainerWrapper> getOne(@Path("id") Long id);

    /**
     * Method to add selected {@link TrainerWrapper} to the database.
     */
    @POST("/api/trainer")
    Call<TrainerWrapper> add(@Body TrainerWrapper trainer);

    /**
     * Method to add selected {@link TrainerWrapper} to the database.
     */
    @PUT("/api/trainer")
    Call<TrainerWrapper> update(@Body TrainerWrapper trainer);

    /**
     * Method to remove {@link TrainerWrapper} with selected id from the database.
     */
    @DELETE("/api/trainer/{id}")
    Call<TrainerWrapper> remove(@Path("id") Long id);
}
