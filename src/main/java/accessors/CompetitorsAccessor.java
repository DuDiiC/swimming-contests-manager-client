package accessors;

import model.Club;
import model.wrapper.CompetitorWrapper;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface using {@link retrofit2.Retrofit} http client to generate interface implementation.
 * In each method returns {@link Call} which can make http requests.
 */
public interface CompetitorsAccessor {

    /**
     * Method to download all {@link CompetitorWrapper}s from the database.
     */
    @GET("/api/competitor")
    Call<List<CompetitorWrapper>> getAll();

    /**
     * Method to download all {@link CompetitorWrapper}s from selected {@link Club}'s id from the database.
     */
    @GET("/api/competitor/club/{clubId}")
    Call<List<CompetitorWrapper>> getAllByClub(@Path("clubId") Long clubId);

    /**
     * Method to download {@link CompetitorWrapper} with selected id from the database.
     */
    @GET("/api/competitor/{id}")
    Call<CompetitorWrapper> getOne(@Path("id") Long id);

    /**
     * Method to add selected {@link CompetitorWrapper} to the database.
     */
    @POST("/api/competitor")
    Call<CompetitorWrapper> add(@Body CompetitorWrapper competitor);

    /**
     * Method to add selected {@link CompetitorWrapper} to the database.
     */
    @PUT("/api/competitor")
    Call<CompetitorWrapper> update(@Body CompetitorWrapper competitor);

    /**
     * Method to remove all {@link CompetitorWrapper}s from selected {@link Club}'s id from the database.
     */
    @DELETE("/api/competitor/club/{clubId}")
    Call<List<CompetitorWrapper>> removeAllByClub(@Path("clubId") Long clubId);

    /**
     * Method to remove {@link CompetitorWrapper} with selected id from the database.
     */
    @DELETE("/api/competitor/{id}")
    Call<CompetitorWrapper> remove(@Path("id") Long id);
}
