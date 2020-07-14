package model;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import net.minidev.json.JSONArray;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSet {
    private DataFile jsonFile;
    private ArrayList<DataFile> images;
    private String jsonObject;
    private ArrayList<ArrayList<DataObject>> objects;



    public DataSet(DataFile jsonFile, ArrayList<DataFile> images) throws IOException {
        this.jsonFile = jsonFile;
        this.images = images;
        objects = new ArrayList<ArrayList<DataObject>>();

        setJsonObject();
    }

    public DataSet(DataFile jsonFile, DataFile image) throws IOException {
        this.jsonFile = jsonFile;
        //this.mask = mask;
        images = new ArrayList<DataFile>();
        images.add(image);
        objects = new ArrayList<ArrayList<DataObject>>();

        setJsonObject();
    }

    public boolean hasPath(String path) {
        return JsonPath.parse(jsonObject).read(path) != null;
    }

    public boolean hasKeyValue(String path, String value) {
        String thisValue = JsonPath.parse(jsonObject).read(path);
        return thisValue.equals(value);
    }

    public boolean hasArraySize(String path, int size) {

        List<Map<String, Object>> array = JsonPath.parse(jsonObject).read(path);

        return array.size() == size;
    }

    public JsonArray getJsonArray(String path) {
        JSONArray array = JsonPath.parse(jsonObject).read(path);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonArray jsonArray = gson.fromJson(array.toJSONString(), JsonArray.class);

        return jsonArray;
    }

    public JsonArray getJsonArray(String path, ArrayList<Predicate> predicates) {
        Predicate[] objectPredicates = new Predicate[predicates.size()];
        for (int i = 0; i < objectPredicates.length; i++) {
            objectPredicates[i] = predicates.get(i);
        }
        JSONArray array;
        if (objectPredicates.length > 0) {
            array = JsonPath.parse(jsonObject).read(path + "[?]", objectPredicates);
        } else {
            array = JsonPath.parse(jsonObject).read(path);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonArray jsonArray = gson.fromJson(array.toJSONString(), JsonArray.class);

        return jsonArray;
    }

    public JsonArray getLastJsonArray(String path) {
        JSONArray array = JsonPath.parse(jsonObject).read(path);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonArray jsonArray = gson.fromJson(array.toJSONString(), JsonArray.class);

        return getLast(jsonArray, new JsonArray());
    }

    public JsonArray getLastJsonArray(String path, ArrayList<Predicate> predicates) {
        Predicate[] objectPredicates = new Predicate[predicates.size()];
        for (int i = 0; i < objectPredicates.length; i++) {
            objectPredicates[i] = predicates.get(i);
        }
        JSONArray array;
        if (objectPredicates.length > 0) {
            array = JsonPath.parse(jsonObject).read(path + "[?]", objectPredicates);
        } else {
            array = JsonPath.parse(jsonObject).read(path);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonArray jsonArray = gson.fromJson(array.toJSONString(), JsonArray.class);

        return getLast(jsonArray, new JsonArray());
    }

    private JsonArray getLast(JsonArray array, JsonArray result) {

        if (!array.get(0).isJsonArray()) {
            return array;
        }
        if (!array.get(0).getAsJsonArray().get(0).isJsonArray()) {
            return array;
        }
        for (JsonElement element : array) {
            result.addAll(getLast(element.getAsJsonArray(), result));
        }
        return result;
    }

    private void setJsonObject() throws IOException {
        jsonObject = JsonPath.parse(jsonFile.getFile()).jsonString();
    }

    public JsonElement getAtPath(String path)
            throws JsonSyntaxException {
        JsonObject obj = new GsonBuilder().create().fromJson(jsonObject, JsonObject.class);
        String[] seg = path.split("\\.");
        for (String element : seg) {
            if (obj != null) {
                JsonElement ele = obj.get(element);
                if (!ele.isJsonObject())
                    return ele;
                else
                    obj = ele.getAsJsonObject();
            } else {
                return null;
            }
        }
        return obj;
    }

    public DataFile getJsonFile() {
        return jsonFile;
    }

    public ArrayList<DataFile> getImages() {
        return images;
    }

    public void addObjects(ArrayList<DataObject> objects) {
        this.objects.add(objects);
    }

    public void remove(DataObject object) {
        objects.remove(object);
    }

    public void removeAll(ArrayList<DataObject> objectsToRemove) {
        objects.removeAll(objectsToRemove);
    }


    public ArrayList<DataObject> getObjects(String path) {
        for (ArrayList<DataObject> _objects : objects) {
            for (DataObject object : _objects) {
                if (object.getPath().equals(path)) {
                    return _objects;
                }
            }
        }
        return null;
    }
}
