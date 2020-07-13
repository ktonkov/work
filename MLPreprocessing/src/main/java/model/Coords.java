package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Map;

public class Coords {
    private double x;
    private double y;

    public Coords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coords(JsonElement element) {
        this.x = Double.parseDouble(element.getAsJsonObject().get("x").getAsString());
        this.y = Double.parseDouble(element.getAsJsonObject().get("y").getAsString());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
