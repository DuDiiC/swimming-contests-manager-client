package accessors.converters;

import accessors.ClubsAccessor;
import configuration.RetrofitBuilder;
import model.Club;

import java.io.IOException;
import java.util.List;

/**
 * The middle class between the {@link ClubsAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Club} in the client application.
 */
public class ClubsConverter {

    private ClubsAccessor clubsAccessor = RetrofitBuilder.getBuilder()
            .create(ClubsAccessor.class);

    /**
     * Corresponds to the method {@link ClubsAccessor#getAll()}.
     */
    public List<Club> getAll() throws IOException {
        return clubsAccessor.getAll()
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link ClubsAccessor#getOne(Long)}.
     */
    public Club getOne(Long id) throws IOException {
        return clubsAccessor.getOne(id)
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link ClubsAccessor#add(Club)}.
     */
    public Club add(Club club) throws IOException {
        return clubsAccessor.add(club)
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link ClubsAccessor#update(Club)}.
     */
    public Club update(Club club) throws IOException {
        return clubsAccessor.update(club)
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link ClubsAccessor#remove(Long)}.
     */
    public Club remove(Long id) throws IOException {
        return clubsAccessor.remove(id)
                .execute()
                .body();
    }
}
