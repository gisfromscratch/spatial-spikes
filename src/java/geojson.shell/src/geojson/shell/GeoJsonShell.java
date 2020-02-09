package geojson.shell;

import com.esri.core.geometry.GeoJsonImportFlags;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;

public class GeoJsonShell {
    public static void main(String[] args) throws Exception {
        String geoJson = "{ \"type\": \"Point\", \"coordinates\": [ 1.53414, 42.50729 ] }";
        MapGeometry mapGeometry = GeometryEngine.geoJsonToGeometry(geoJson, GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Point);
        
    }
}