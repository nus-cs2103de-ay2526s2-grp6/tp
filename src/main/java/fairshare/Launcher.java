package fairshare;

/**
 * A launcher class to workaround classpath issues with JavaFX.
 * This is the actual entry point set in build.gradle.
 */
public class Launcher {

    /**
     * Entry point of the application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        FairShare.main(args);
    }
}
