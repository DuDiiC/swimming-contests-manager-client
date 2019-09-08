package configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Singleton class with serwer adress, from where client have data.
 */
public class Configuration {

    private static Configuration instance;

    /**
     * Returns instance of {@link Configuration} class, or creates new one if hasn't existed yet.
     */
    public static Configuration getInstance() {
        if (instance == null) {
            synchronized (Configuration.class) {
                instance = new Configuration();
            }
        }
        return instance;
    }

    /**
     * String value for server adress.
     */
    @Getter @Setter private String serverAddress;

    private Configuration() {
        this.serverAddress = "http://localhost";
    }
}
