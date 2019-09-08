package accessors;

import model.Club;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface ClubsAccessor {

    /**
     * Method to download all {@link Club}s from the database.
     */
    @GET("/api/club")
    Call<List<Club>> getAll();

    /**
     * Method to download {@link Club} with selected id from the database.
     */
    @GET("/api/club/{id}")
    Call<Club> getOne(@Path("id") Long id);

    /**
     * Method to add selected {@link Club} to the database.
     */
    @POST("/api/club")
    Call<Club> add(@Body Club club);

    /**
     * Method to update selected {@link Club} in the database.
     */
    @PUT("/api/club")
    Call<Club> update(@Body Club club);

    /**
     * Method to remove {@link Club} with selected id from the database.
     */
    @DELETE("/api/club/{id}")
    Call<Club> remove(@Path("id") Long id);
}
