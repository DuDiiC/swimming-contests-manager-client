import accessors.TrainersAccessor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Objects;

public class MainCLI {
    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TrainersAccessor trainersAccessor = retrofit.create(TrainersAccessor.class);

        try {
            trainersAccessor
                    .getAllByClub(2L)
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
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
