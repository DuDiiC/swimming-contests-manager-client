package accessors.converters;

import accessors.CompetitorsAccessor;
import configuration.RetrofitBuilder;
import model.Competitor;
import model.wrapper.CompetitorWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The middle class between the {@link CompetitorsAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Competitor} in the client application.
 */
public class CompetitorsConverter {

    private CompetitorsAccessor competitorsAccessor = RetrofitBuilder.getBuilder()
            .create(CompetitorsAccessor.class);

    /**
     * Corresponds to the method {@link CompetitorsAccessor#getAll()}.
     */
    public List<Competitor> getAll() throws IOException {
        return competitorsAccessor.getAll()
                .execute()
                .body()
                .stream()
                .map(competitorWrapper -> {
                    try {
                        return competitorWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#getAllByClub(Long)}}.
     */
    public List<Competitor> getAllByClub(Long id) throws IOException {
        return competitorsAccessor.getAllByClub(id)
                .execute()
                .body()
                .stream()
                .map(competitorWrapper -> {
                    try {
                        return competitorWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#getOne(Long)}.
     */
    public Competitor getOne(Long id) throws IOException {
        return competitorsAccessor.getOne(id)
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#add(CompetitorWrapper)}.
     */
    public Competitor add(Competitor competitor) throws IOException {
        return competitorsAccessor.add(CompetitorWrapper.wrap(competitor))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#update(CompetitorWrapper)}.
     */
    public Competitor update(Competitor competitor) throws IOException {
        return competitorsAccessor.update(CompetitorWrapper.wrap(competitor))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#removeAllByClub(Long)}.
     */
    public List<Competitor> removeAllByClub(Long clubId) throws IOException {
        return competitorsAccessor.removeAllByClub(clubId)
                .execute()
                .body()
                .stream()
                .map(competitorWrapper -> {
                    try {
                        return competitorWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link CompetitorsAccessor#remove(Long)}.
     */
    public Competitor remove(Long id) throws IOException {
        return competitorsAccessor.remove(id)
                .execute()
                .body()
                .unwrap();
    }
}
