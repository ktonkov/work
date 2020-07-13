package processing;

import com.jayway.jsonpath.Predicate;
import model.Data;
import org.opencv.core.Core;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Process {

    private static Properties properties;

    Data data;

    public static void main(String[] args) throws IOException {
        Process process = new Process();
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        properties = new Properties();
        properties.load(new FileReader(new File("src/main/resources/local.properties")));
        process.run();
    }

    public void run() throws IOException {
        File folder = new File(properties.getProperty("rawDataPath"));
        data = new Data(folder.listFiles(), properties.getProperty("imagePostfixToIgnore"));

        if (properties.getProperty("imagePrefixes") != null) {
            String[] imagePrefixes = properties.getProperty("imagePrefixes").split(",");
            data.removeUnique(imagePrefixes);
        } else {
            data.removeUnique();
        }

        if (properties.getProperty("jsonHasPaths") != null) {
            String[] paths = properties.getProperty("jsonHasPaths").split(",");
            for (String path : paths) {
                data.validateJsonsHasPath(path);
            }
        }

        if (properties.getProperty("jsonKeyPaths") != null
                && properties.getProperty("jsonValues") != null) {
            String[] paths = properties.getProperty("jsonKeyPaths").split(",");
            String[] values = properties.getProperty("jsonValues").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateJsonsHasKeyValue(paths[i], values[i]);
            }
        }

        ArrayList<Predicate> objectPredicates = new ArrayList<Predicate>();

        if (properties.getProperty("keysForObjectStartsWith") != null
                && properties.getProperty("valuesForObjectStartsWith") != null) {
            final String[] keys = properties.getProperty("keysForObjectStartsWith").split(",");
            final String[] values = properties.getProperty("valuesForObjectStartsWith").split(",");
            for (int i = 0; i < keys.length; i++) {
                final int finalI = i;
                Predicate startsWith = new Predicate() {
                    @Override
                    public boolean apply(PredicateContext ctx) {
                        return ctx.item(Map.class).get(keys[finalI]).toString().startsWith(values[finalI]);
                    }
                };
                objectPredicates.add(startsWith);
            }
        }

        if (properties.getProperty("keysForObject") != null
                && properties.getProperty("valuesForObject") != null) {
            final String[] keys = properties.getProperty("keysForObject").split(",");
            final String[] values = properties.getProperty("valuesForObject").split(",");
            for (int i = 0; i < keys.length; i++) {
                final int finalI = i;
                Predicate equals = new Predicate() {
                    @Override
                    public boolean apply(PredicateContext ctx) {
                        return ctx.item(Map.class).get(keys[finalI]).toString().equals(values[finalI]);
                    }
                };
                objectPredicates.add(equals);
            }
        }

        if (properties.getProperty("jsonArrayCountPaths") != null
                && properties.getProperty("jsonArrayCounts") != null) {
            String[] paths = properties.getProperty("jsonArrayCountPaths").split(",");
            String[] values = properties.getProperty("jsonArrayCounts").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateJsonsArraySize(paths[i], Integer.parseInt(values[i]));
            }
        }

        String[] pathsToObjectCoords = properties.getProperty("pathForObjectCoords").split(",");
        String[] objectTypes = properties.getProperty("objectTypes").split(",");
        for (int i = 0; i < pathsToObjectCoords.length; i++) {
            data.addObjects(pathsToObjectCoords[i], objectTypes[i], objectPredicates);
        }

        if (properties.getProperty("objectPathsLowPercentile") != null
                && properties.getProperty("lowPercentile") != null) {
            String[] paths = properties.getProperty("objectPathsLowPercentile").split(",");
            String[] lowPercentiles = properties.getProperty("lowPercentile").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateCoordCountQuantileLow(paths[i], Double.parseDouble(lowPercentiles[i]));
            }
        }

        if (properties.getProperty("objectPathsHighPercentile") != null
                && properties.getProperty("highPercentile") != null) {
            String[] paths = properties.getProperty("objectPathsHighPercentile").split(",");
            String[] highPercentiles = properties.getProperty("highPercentile").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateCoordCountQuantileHigh(paths[i], Double.parseDouble(highPercentiles[i]));
            }
        }

        if (properties.getProperty("objectPathsMinArea") != null
                && properties.getProperty("minArea") != null) {
            String[] paths = properties.getProperty("objectPathsMinArea").split(",");
            String[] lowPercentiles = properties.getProperty("minArea").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateAreaQuantileLow(paths[i], Double.parseDouble(lowPercentiles[i]));
            }
        }

        if (properties.getProperty("pathsToObjectsNotNegativeCoords") != null) {
            String[] paths = properties.getProperty("pathsToObjectsNotNegativeCoords").split(",");
            for (int i = 0; i < paths.length; i++) {
                data.validateCoordPlace(paths[i]);
            }
        }

        data.finish(properties.getProperty("processedPath"), properties.getProperty("outliersPath"));
        System.out.println("Done");
    }
}
