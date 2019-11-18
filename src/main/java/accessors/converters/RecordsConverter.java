package accessors.converters;

import accessors.RecordsAccessor;
import configuration.RetrofitBuilder;
import model.Record;
import model.wrapper.RecordWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The middle class between the {@link RecordsAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Record} in the client application.
 */
public class RecordsConverter {

    private RecordsAccessor recordsAccessor = RetrofitBuilder.getBuilder()
            .create(RecordsAccessor.class);

    /**
     * Corresponds to the method {@link RecordsAccessor#getAll()}.
     */
    public List<Record> getAll() throws IOException {
        return recordsAccessor.getAll()
                .execute()
                .body()
                .stream()
                .map(recordWrapper -> {
                    try {
                        return recordWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link RecordsAccessor#getAllByCompetitor(Long)}.
     */
    public List<Record> getAllByCompetitor(Long pesel) throws IOException {
        return recordsAccessor.getAllByCompetitor(pesel)
                .execute()
                .body()
                .stream()
                .map(recordWrapper -> {
                    try {
                        return recordWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link RecordsAccessor#getOne(Long)}.
     */
    public Record getOne(Long id) throws IOException {
        return recordsAccessor.getOne(id)
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link RecordsAccessor#getBestByCompetition(Long)}.
     */
    public Record getBestByCompetition(Long competitionId) {
        try {
            return recordsAccessor.getBestByCompetition(competitionId)
                    .execute()
                    .body()
                    .unwrap();
        } catch (Exception e) {
            return null;
        }


    }

    /**
     * Corresponds to the method {@link RecordsAccessor#add(RecordWrapper)}.
     */
    public Record add(Record record) throws IOException {
        return recordsAccessor.add(RecordWrapper.wrap(record))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link RecordsAccessor#remove(Long)}.
     */
    public Record remove(Long id) throws IOException {
        return recordsAccessor.remove(id)
                .execute()
                .body()
                .unwrap();
    }
}
