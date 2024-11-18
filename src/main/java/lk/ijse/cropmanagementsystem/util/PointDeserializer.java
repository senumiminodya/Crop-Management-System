package lk.ijse.cropmanagementsystem.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.geo.Point;

import java.io.IOException;

public class PointDeserializer extends JsonDeserializer<Point> {
    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        // Parse the JSON object containing x and y
        JsonNode node = p.getCodec().readTree(p);

        // Safely get the x and y values as doubles
        double x = node.get("x").doubleValue();
        double y = node.get("y").doubleValue();

        return new Point(x, y);
    }
}
