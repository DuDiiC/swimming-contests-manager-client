package accessors.converters;

import accessors.TrainersAccessor;
import configuration.RetrofitBuilder;
import model.Trainer;
import model.wrapper.TrainerWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The middle class between the {@link TrainersAccessor} returning the {@link retrofit2.Call} and the database model class
 * {@link Trainer} in the client application.
 */
public class TrainersConverter {

    private TrainersAccessor trainersAccessor = RetrofitBuilder.getBuilder()
            .create(TrainersAccessor.class);

    /**
     * Corresponds to the method {@link TrainersAccessor#getAll()}.
     */
    public List<Trainer> getAll() throws IOException {

        return trainersAccessor.getAll()
                    .execute()
                    .body()
                    .stream()
                    .map(trainerWrapper -> {
                        try {
                            return trainerWrapper.unwrap();
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link TrainersAccessor#getAllByClub(Long)}.
     */
    public List<Trainer> getAllByClub(Long clubId) throws IOException {
        return trainersAccessor.getAllByClub(clubId)
                .execute()
                .body()
                .stream()
                .map(trainerWrapper -> {
                    try {
                        return trainerWrapper.unwrap();
                    } catch(IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Corresponds to the method {@link TrainersAccessor#getOne(Long)}.
     */
    public Trainer getOne(Long id) throws IOException {
        return trainersAccessor.getOne(id)
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link TrainersAccessor#add(TrainerWrapper)}.
     */
    public Trainer add(Trainer trainer) throws IOException {
        return trainersAccessor.add(TrainerWrapper.wrap(trainer))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link TrainersAccessor#update(TrainerWrapper)}.
     */
    public Trainer update(Trainer trainer) throws IOException {
        return trainersAccessor.update(TrainerWrapper.wrap(trainer))
                .execute()
                .body()
                .unwrap();
    }

    /**
     * Corresponds to the method {@link TrainersAccessor#remove(Long)}.
     */
    public Trainer remove(Long id) throws IOException {
        return trainersAccessor.remove(id)
                .execute()
                .body()
                .unwrap();
    }
}
