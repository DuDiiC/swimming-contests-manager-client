package accessors.converters;

import accessors.CompetitionsAccessor;
import configuration.RetrofitBuilder;
import model.Competition;

import java.io.IOException;
import java.util.List;

/**
 * The middle class between the {@link CompetitionsAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Competition} in the client application.
 */
public class CompetitionsConverter {

    private CompetitionsAccessor competitionsAccessor = RetrofitBuilder.getBuilder()
            .create(CompetitionsAccessor.class);

    /**
     * Corresponds to the method {@link CompetitionsAccessor#getAll()}.
     */
    public List<Competition> getAll() throws IOException {
        return competitionsAccessor.getAll()
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link CompetitionsAccessor#getOne(Long)}.
     */
    public Competition getOne(Long id) throws IOException {
        return competitionsAccessor.getOne(id)
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link CompetitionsAccessor#add(Competition)}.
     */
    public Competition add(Competition competition) throws IOException {
        return competitionsAccessor.add(competition)
                .execute()
                .body();
    }

    /**
     * Corresponds to the method {@link CompetitionsAccessor#remove(Long)}.
     */
    public Competition remove(Long id) throws IOException {
        return competitionsAccessor.remove(id)
                .execute()
                .body();
    }

}
