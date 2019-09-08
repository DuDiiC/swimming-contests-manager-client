package model.wrapper;

import accessors.CompetitionsAccessor;
import accessors.CompetitorsAccessor;
import configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Competition;
import model.Competitor;
import model.Contest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The intermediate class between the representation of the contest in JSON format
 * and the model class {@link Contest} in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestWrapper {

    private Long id;

    private String name;

    private String date;

    private String city;

    // many contests many competitons
    private List<Long> competitionsIds;

    // many contests many competitors
    private List<Long> competitorsIds;

    /**
     * @return {@link ContestWrapper} object corresponds with the selected {@link Contest} object.
     */
    public static ContestWrapper wrap(Contest contest) {
        return new ContestWrapper(
                contest.getId(),
                contest.getName(),
                contest.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                contest.getCity(),
                contest.getCompetitions()
                    .stream()
                    .map(Competition::getId)
                    .collect(Collectors.toList()),
                contest.getCompetitors()
                    .stream()
                    .map(Competitor::getPesel)
                    .collect(Collectors.toList())
        );
    }

    /**
     * @return {@link Contest} object corresponds with the actual instance of {@link ContestWrapper} class.
     */
    public Contest unwrap() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getInstance().getServerAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CompetitionsAccessor competitionsAccessor = retrofit.create(CompetitionsAccessor.class);
        CompetitorsAccessor competitorsAccessor = retrofit.create(CompetitorsAccessor.class);

        Set<Competition> competitions = competitionsIds.stream()
                .distinct()
                .map(competitionsAccessor::getOne)
                .map(competitionCall -> {
                    try {
                        return competitionCall.execute();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(Response::body)
                .collect(Collectors.toSet());

//        Stream<Response<Competition>> competitionsResponseStream = competitionsIds.stream()
//                .distinct()
//                .map(competitionsAccessor::getOne)
//                .map(competitionCall -> {
//                    try {
//                        return competitionCall.execute();
//                    } catch (IOException e) {
//                        return null;
//                    }
//                });
//
//        if (competitionsResponseStream.anyMatch(Objects::isNull)) throw new IOException("Missing dependencies");
//
//        Set<Competition> competitions = competitionsResponseStream
//                .map(Response::body)
//                .collect(Collectors.toSet());

        Set<Competitor> competitors = competitorsIds.stream()
                .distinct()
                .map(competitorsAccessor::getOne)
                .map(competitorWrapperCall -> {
                    try {
                        return competitorWrapperCall.execute();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(Response::body)
                .map(competitorWrapper -> {
                    try {
                        return competitorWrapper.unwrap();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

//        Stream<Response<CompetitorWrapper>> competitorsResponseStream = competitorsIds.stream()
//                .distinct()
//                .map(competitorsAccessor::getOne)
//                .map(competitorWrapperCall -> {
//                    try {
//                        return competitorWrapperCall.execute();
//                    } catch (IOException e) {
//                        return null;
//                    }
//                });
//
//        if (competitorsResponseStream.anyMatch(Objects::isNull)) throw new IOException("Missing dependencies");
//
//        Stream<Competitor> competitorsStream = competitorsResponseStream
//                .map(Response::body)
//                .map(competitorWrapper -> {
//                    try {
//                        return competitorWrapper.unwrap();
//                    } catch (IOException e) {
//                        return null;
//                    }
//                });
//
//        if (competitorsStream.anyMatch(Objects::isNull)) throw new IOException("Missing dependencies");
//
//        Set<Competitor> competitors = competitorsStream.collect(Collectors.toSet());

        return new Contest(
                id,
                name,
                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                city,
                competitions,
                competitors
        );
    }

}