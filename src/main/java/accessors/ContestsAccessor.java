package accessors;

import model.wrapper.ContestWrapper;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface ContestsAccessor {

    /**
     * Method to download all {@link ContestWrapper}s from the database.
     */
    @GET("/api/contest")
    Call<List<ContestWrapper>> getAll();

    /**
     * Method to add selected {@link ContestWrapper} to the database.
     */
    @POST("/api/contest")
    Call<ContestWrapper> add(@Body ContestWrapper contest);

    /**
     * Method to update selected {@link ContestWrapper} in the database.
     */
    @PUT("/api/contest")
    Call<ContestWrapper> update(@Body ContestWrapper contest);

    /**
     * Method to remove {@link ContestWrapper} with selected id from the database.
     */
    @DELETE("/api/contest/{id}")
    Call<ContestWrapper> remove(@Path("id") Long id);
}
