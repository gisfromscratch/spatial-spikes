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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJsonShell {

    private static List<MapGeometry> readGeometriesFromGeoJsonFile(String filePath, Geometry.Type geometryType)
            throws IOException
    {
        ArrayList<MapGeometry> geometries = new ArrayList<>();
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
            geometries.add(mapGeometry);
        }

        return geometries;
    }

    private static Map<MapGeometry, List<MapGeometry>> intersect(List<MapGeometry> countries, List<MapGeometry> cities) {
        HashMap<MapGeometry, List<MapGeometry>> intersections = new HashMap<>(cities.size());
        Iterator<MapGeometry> cityElements = cities.iterator();
        while (cityElements.hasNext()) {
            MapGeometry city = cityElements.next();
            ArrayList<MapGeometry> countryIntersections = new ArrayList<>();
            Iterator<MapGeometry> countryElements = countries.iterator();
            while (countryElements.hasNext()) {
                MapGeometry country = countryElements.next();
                Geometry intersection = GeometryEngine.intersect(country.getGeometry(), city.getGeometry(), city.getSpatialReference());
                if (!intersection.isEmpty()) {
                    countryIntersections.add(country);
                }
            }
            intersections.put(city, countryIntersections);
        }

        return intersections;
    }

    public static void main(String[] args) throws Exception {
        int numberOfMismatches = 0;
        List<MapGeometry> cities = readGeometriesFromGeoJsonFile("../../../data/world_cities1000_sample.geojson", Geometry.Type.Point);
        List<MapGeometry> countries = readGeometriesFromGeoJsonFile("../../../data/world_countries_sample.geojson", Geometry.Type.Polygon);
        Map<MapGeometry, List<MapGeometry>> intersections = intersect(countries, cities);
        for (Map.Entry<MapGeometry, List<MapGeometry>> intersection : intersections.entrySet()) {
            List<MapGeometry> countryIntersections = intersection.getValue();
            if (countryIntersections.isEmpty()) {
                numberOfMismatches++;
            }
        }
        System.out.println(numberOfMismatches);
    }
}