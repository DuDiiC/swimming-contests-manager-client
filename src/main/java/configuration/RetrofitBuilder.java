package configuration;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class with one method, which create new instance of {@link Retrofit} class using server adress from
 * {@link Configuration} and using converter factory from {@link com.google.gson.Gson} library.
 */
public class RetrofitBuilder {

    /**
     * @return new {@link Retrofit} instance with configuration.
     */
    public static Retrofit getBuilder() {
        return new Retrofit.Builder()
                .baseUrl(Configuration.getInstance().getServerAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
