package geojson.shell;

import java.util.Map;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.SpatialReference;

/**
 * Represents a simple feature having a geometry and attributes.
 */
public class SimpleFeature {

    private final MapGeometry geometry;
    private final Map<String, Object> attributes;

    public SimpleFeature(MapGeometry geometry, Map<String, Object> attributes) {
        this.geometry = geometry;
        this.attributes = attributes;
    }

    public Geometry getGeometry() {
        return geometry.getGeometry();
    }

    public SpatialReference getSpatialReference() {
        return geometry.getSpatialReference();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}