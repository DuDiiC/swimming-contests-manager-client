package accessors.converters;

import accessors.ContestsAccessor;
import configuration.RetrofitBuilder;
import model.Contest;
import model.wrapper.ContestWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The middle class between the {@link ContestsAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Contest} in the client application.
 */
public class ContestsConverter {

    private ContestsAccessor contestsAccessor = RetrofitBuilder.getBuilder()
            .create(ContestsAccessor.class);

    /**
     * Corresponds to the method {@link ContestsAccessor#getAll()}.
     */
    public List<Contest> getAll() throws IOException {
        return contestsAccessor.getAll()
                .execute()
                .body()
                .stream()
                .map(contestWrapper -> {
                    return contestWrapper.unwrap();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link ContestsAccessor#add(ContestWrapper)}.
     */
    public Contest add(Contest contest) throws IOException {
        return contestsAccessor.add(ContestWrapper.wrap(contest))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link ContestsAccessor#update(ContestWrapper)}.
     */
    public Contest update(Contest contest) throws IOException {
        return contestsAccessor.update(ContestWrapper.wrap(contest))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link ContestsAccessor#remove(Long)}.
     */
    public Contest remove(Long id) throws IOException {
        return contestsAccessor.remove(id)
                .execute()
                .body()
                .unwrap();
    }
}
