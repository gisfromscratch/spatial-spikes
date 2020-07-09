package geojson.shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.esri.core.geometry.GeoJsonImportFlags;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJsonShell {

    private static List<SimpleFeature> readFeaturesFromGeoJsonFile(String filePath, Geometry.Type geometryType)
            throws IOException
    {
        ArrayList<SimpleFeature> features = new ArrayList<>();
        byte[] geoJsonData = Files.readAllBytes(Paths.get(filePath));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(geoJsonData);
        JsonNode featuresNode = rootNode.path("features");
        Iterator<JsonNode> featureElements = featuresNode.elements();
        while (featureElements.hasNext()) {
            JsonNode featureNode = featureElements.next();
            JsonNode geometryNode = featureNode.get("geometry");
            String geoJson = geometryNode.toString();
            MapGeometry mapGeometry = GeometryEngine.geoJsonToGeometry(geoJson, GeoJsonImportFlags.geoJsonImportDefaults, geometryType);
            JsonNode propertiesNode = featureNode.get("properties");
            Map<String, Object> attributes = mapper.convertValue(propertiesNode, new TypeReference<Map<String, Object>>(){});
            SimpleFeature feature = new SimpleFeature(mapGeometry, attributes);
            features.add(feature);
        }

        return features;
    }

    private static Map<SimpleFeature, List<SimpleFeature>> intersect(List<SimpleFeature> countries, List<SimpleFeature> cities) {
        HashMap<SimpleFeature, List<SimpleFeature>> intersections = new HashMap<>(cities.size());
        Iterator<SimpleFeature> cityElements = cities.iterator();
        while (cityElements.hasNext()) {
            SimpleFeature city = cityElements.next();
            ArrayList<SimpleFeature> countryIntersections = new ArrayList<>();
            Iterator<SimpleFeature> countryElements = countries.iterator();
            while (countryElements.hasNext()) {
                SimpleFeature country = countryElements.next();
                Geometry intersection = GeometryEngine.intersect(country.getGeometry(), city.getGeometry(), city.getSpatialReference());
                if (!intersection.isEmpty()) {
                    countryIntersections.add(country);
                }
            }
            intersections.put(city, countryIntersections);
        }

        return intersections;
    }

    public enum FileArgument {
        Unknown,
        LetFile,
        RightFile
    }

    public static void main(String[] args) throws Exception {
        String leftFile = "../../../data/world_cities1000_sample.geojson";
        String rightFile = "../../../data/world_countries_sample.geojson";
        FileArgument argument = FileArgument.Unknown;
        for (String arg : args) {
            switch (arg) {
                case "-l":
                    argument = FileArgument.LetFile;
                    break;

                case "-r":
                    argument = FileArgument.RightFile;
                    break;

                default:
                    switch (argument) {
                        case LetFile:
                            leftFile = arg;
                            break;
                        case RightFile:
                            rightFile = arg;
                            break;
                        case Unknown:
                            break;
                    }
                    argument = FileArgument.Unknown;
                    break;
            }
        }

        int numberOfMismatches = 0;
        int numberOfWrongCountryCodes = 0;
        String leftCountryCodeAttributeName = "field_9";
        String rightCountryCodeAttributeName = "ISO";
        List<SimpleFeature> cities = readFeaturesFromGeoJsonFile(leftFile, Geometry.Type.Point);
        List<SimpleFeature> countries = readFeaturesFromGeoJsonFile(rightFile, Geometry.Type.Polygon);
        Map<SimpleFeature, List<SimpleFeature>> intersections = intersect(countries, cities);
        for (Map.Entry<SimpleFeature, List<SimpleFeature>> intersection : intersections.entrySet()) {
            SimpleFeature city = intersection.getKey();
            List<SimpleFeature> countryIntersections = intersection.getValue();
            if (countryIntersections.isEmpty()) {
                numberOfMismatches++;
            } else {
                Object cityCountryCode = city.getAttribute(leftCountryCodeAttributeName);
                String cityCountryCodeAsText = cityCountryCode.toString();
                for (SimpleFeature country : countryIntersections) {
                    Object countryCode = country.getAttribute(rightCountryCodeAttributeName);
                    String countryCodeAsText = countryCode.toString();
                    if (!cityCountryCodeAsText.equalsIgnoreCase(countryCodeAsText)) {
                        numberOfWrongCountryCodes++;
                    }
                }
            }
        }
        System.out.println(String.format("%d mismatches and %d wrong country codes", numberOfMismatches, numberOfWrongCountryCodes));
    }
}