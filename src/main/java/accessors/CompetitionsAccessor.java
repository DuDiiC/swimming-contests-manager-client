package accessors;

import model.Competition;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface CompetitionsAccessor {

    /**
     * Method to download all {@link Competition}s from the database.
     */
    @GET("/api/competition")
    Call<List<Competition>> getAll();

    /**
     * Method to download {@link Competition} with selected id from the database.
     */
    @GET("/api/competition/{id}")
    Call<Competition> getOne(@Path("id") Long id);

    /**
     * Method to add selected {@link Competition} to the database.
     */
    @POST("/api/competition")
    Call<Competition> add(@Body Competition competition);

    /**
     * Method to remove {@link Competition} with selected id from the database.
     */
    @DELETE("/api/competition/{id}")
    Call<Competition> remove(@Path("id") Long id);
}
