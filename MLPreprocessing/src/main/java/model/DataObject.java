package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class DataObject {
    String path;
    private ArrayList<Coords> coordsList;
    private double area;
    private List<Map<String, Object>> jsonElement;

    public DataObject(String path, JsonArray array) {
        this.path = path;

        ArrayList<Coords> coords = new ArrayList<Coords>();
        if (array != null) {
            for (JsonElement element : array) {
                if (element.isJsonArray()) {
                    for (JsonElement subElement : element.getAsJsonArray()) {
                        coords.add(new Coords(subElement));
                    }
                } else {
                    coords.add(new Coords(element));
                }
            }
        }
        this.coordsList = coords;
        area = 0;
    }

    public DataObject(String path, JsonElement element) {
        this.path = path;

        double cx = Double.parseDouble(element.getAsJsonObject().get("centerX").getAsString());
        double cy = Double.parseDouble(element.getAsJsonObject().get("centerY").getAsString());
        double a = Double.parseDouble(element.getAsJsonObject().get("angle").getAsString());
        double r = Double.parseDouble(element.getAsJsonObject().get("scale").getAsString());

        double w = 3.375 * 25.4;
        double h = 2.125 * 25.4;
        double[][] wh;
        wh = new double[][] {{-w, -h}, {w, -h}, {w, h}, {-w, h}, {-w, -h}};
        INDArray pts = Nd4j.create(wh);
        INDArray nd1 = Nd4j.create(new double[][] {{Math.cos(a), -Math.sin(a)}, {Math.sin(a), Math.cos(a)}});
        INDArray nd2 = Nd4j.create(new double[][] {{cx, cy}});
        pts = pts.mul(0.5);

        pts = pts.mmul(nd1).mul(r).add(nd2);

        double[][] matrix = pts.toDoubleMatrix();

        this.coordsList = calculateCoords(matrix);
        area = 0;
    }

    private ArrayList<Coords> calculateCoords(double[][] matrix) {
        ArrayList<Coords> result = new ArrayList<Coords>();
        int a = matrix.length;
        int b = matrix[0].length;

        for (int i = 0; i < matrix.length; i++) {
            result.add(new Coords(matrix[i][0], matrix[i][1]));
        }
        return result;
    }

    public void addCoord(Coords coords) {
        coordsList.add(coords);
    }

    public void setJsonElement(List<Map<String, Object>> jsonElement) {
        this.jsonElement = jsonElement;
    }

    public List<Map<String, Object>> getJsonElement() {
        return jsonElement;
    }

    public String getPath() {
        return path;
    }

    public int getCoordsCount() {
        return coordsList.size();
    }

    public ArrayList<Coords> getCoords() {
        return coordsList;
    }

    public double getArea() {
        if (area == 0) {
            area = Imgproc.contourArea(toMatOfPoints());
        }
        return area;
    }

    private MatOfPoint toMatOfPoints() {
        ArrayList<Point> points = new ArrayList<Point>();

        for (Coords coord : coordsList) {
            points.add(new Point(coord.getX(), coord.getY()));
        }

        MatOfPoint result = new MatOfPoint();
        result.fromList(points);

        return result;
    }
}
