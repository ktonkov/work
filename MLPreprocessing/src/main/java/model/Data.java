package model;

import com.google.gson.*;
import com.jayway.jsonpath.Predicate;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Data {

    private ArrayList<DataSet> dataSets;
    private ArrayList<DataFile> jsonFiles;
    private ArrayList<DataFile> imageFiles;
    private ArrayList<DataFile> filesToRemove;

    public Data(File[] filesArray, String imagePostfixToIgnore) {
        dataSets = new ArrayList<DataSet>();
        filesToRemove = new ArrayList<DataFile>();
        jsonFiles = new ArrayList<DataFile>();
        imageFiles = new ArrayList<DataFile>();
        for (File file : filesArray) {
            if (!file.isDirectory()) {
                DataFile dataFile = new DataFile(file);
                //allFiles.add(dataFile);
                if (dataFile.getExt().equals("json")) {
                    jsonFiles.add(dataFile);
                } else if ((dataFile.getExt().equals("png") || dataFile.getExt().equals("jpg"))
                        && imagePostfixToIgnore != null && !dataFile.getName().endsWith(imagePostfixToIgnore)) {
                    imageFiles.add(dataFile);
                }
            }
        }
        System.out.println("Total jsons: " + jsonFiles.size());
        System.out.println("Total images: " + imageFiles.size());
    }

    public void removeUnique() throws IOException {
        ArrayList<DataFile> temporaryJsons = new ArrayList<DataFile>();
        for (DataFile jsonFile : jsonFiles) {
            DataFile image = imageForJson(jsonFile.getName());
            if (image != null) {
                DataSet dataSet = new DataSet(jsonFile, image);
                temporaryJsons.add(jsonFile);
                imageFiles.remove(image);
                dataSets.add(dataSet);
            }
        }
        jsonFiles.removeAll(temporaryJsons);
        addToRemove(jsonFiles, "no_img");
        addToRemove(imageFiles, "no_json");
        System.out.println("Total data sets: " + temporaryJsons.size());
        System.out.println("Removed unique: " + filesToRemove.size());
    }

    public void removeUnique(String[] imagePrefixes) throws IOException {
        ArrayList<DataFile> temporaryJsons = new ArrayList<DataFile>();
        for (DataFile jsonFile : jsonFiles) {
            ArrayList<DataFile> images = new ArrayList<DataFile>();
            for (String prefix : imagePrefixes) {
                DataFile image = imageForJson(prefix + jsonFile.getName());
                if (image != null) {
                    images.add(image);
                }
            }
            if (images.size() == imagePrefixes.length) {
                DataSet dataSet = new DataSet(jsonFile, images);
                temporaryJsons.add(jsonFile);
                imageFiles.removeAll(images);
                dataSets.add(dataSet);
            }
        }
        jsonFiles.removeAll(temporaryJsons);

        addToRemove(jsonFiles, "no_img");
        addToRemove(imageFiles, "no_json");

        System.out.println("Total data sets: " + temporaryJsons.size());
        System.out.println("Removed unique: " + filesToRemove.size());
    }

    public void validateJsonsHasPath(String path) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            if (!file.hasPath(path)) {
                tempFilesToRemove.add(file);
                imageFiles.removeAll(file.getImages());
                jsonFiles.remove(file.getJsonFile());
            }
        }
        addToRemove(tempFilesToRemove, "json_has_no_" + path, true);
        System.out.println("Files with out " + path + ": " + tempFilesToRemove.size());
    }

    public void validateJsonsHasKeyValue(String path, String value) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            if (!file.hasKeyValue(path, value)) {
                tempFilesToRemove.add(file);
                imageFiles.removeAll(file.getImages());
                jsonFiles.remove(file.getJsonFile());
            }
        }
        addToRemove(tempFilesToRemove, "json_has_no_" + path + "_" + value, true);
        System.out.println("Files with " + path + " not equals to " + value + ": " + tempFilesToRemove.size());
    }

    public void validateJsonsArraySize(String path, int size) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            if (!file.hasArraySize(path, size)) {
                tempFilesToRemove.add(file);
                imageFiles.removeAll(file.getImages());
                jsonFiles.remove(file.getJsonFile());
            }
        }
        addToRemove(tempFilesToRemove, "json_" + path + "_size_not_equals_to_" + size, true);
        System.out.println("Files with  " + path + " size not equals to " + size + ": " + tempFilesToRemove.size());
    }

    public void validateJsonsArraySizeDeep(String path, int size) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            for (JsonElement element : file.getLastJsonArray(path)) {
                if (element.getAsJsonArray().size() != size) {
                    tempFilesToRemove.add(file);
                    //maskFiles.remove(file.getMask());
                    imageFiles.removeAll(file.getImages());
                    jsonFiles.remove(file.getJsonFile());
                }
            }
        }
        addToRemove(tempFilesToRemove, "json_" + path + "_size_not_equals_to_" + size, true);
        System.out.println("Files with  " + path + " size not equals to " + size + ": " + tempFilesToRemove.size());
    }

    public void addObjects(String pathToObjectCoords, String objectType, ArrayList<Predicate> objectPredicates) {
        for (DataSet file : dataSets) {
            ArrayList<DataObject> objects = new ArrayList<DataObject>();

            for (JsonElement element : file.getJsonArray(pathToObjectCoords, objectPredicates)) {
                if (objectType.equals("jsonArray")) {
                    objects.add(new DataObject(pathToObjectCoords, element.getAsJsonArray()));
                } else if (objectType.equals("jsonObject")) {
                    objects.add(new DataObject(pathToObjectCoords, element));
                }
            }
            file.addObjects(objects);
        }
    }
/*
    public void filterObjects(String key, String value) {
        for (DataSet file : dataSets) {
            ArrayList<DataObject> objectsToRemove = new ArrayList<DataObject>();
            for (DataObject object : file.getObjects()) {
                if (object.getJsonElement() == null || !object.getJsonElement().getAsJsonObject().has(key) || !object.getJsonElement().getAsJsonObject().get(key).getAsString().equals(value)) {
                    objectsToRemove.add(object);
                }
            }
            file.removeAll(objectsToRemove);
        }
    }

    public void filterObjectsStartsWith(String key, String value) {
        for (DataSet file : dataSets) {
            ArrayList<DataObject> objectsToRemove = new ArrayList<DataObject>();
            for (DataObject object : file.getObjects()) {
                if (!object.getJsonElement().getAsJsonObject().get(key).getAsString().startsWith(value)) {
                    objectsToRemove.add(object);
                }
            }
            file.removeAll(objectsToRemove);
        }
    }

    public void validateObjectsCount(int count) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            if (file.getObjects().size() != count) {
                tempFilesToRemove.add(file);
                //maskFiles.remove(file.getMask());
                imageFiles.removeAll(file.getImages());
                jsonFiles.remove(file.getJsonFile());
            }
        }
        removeFiles(tempFilesToRemove);
        System.out.println("Jsons with object count not  " + count + ": " + tempFilesToRemove.size());
    }
*/

    public void validateCoordPlace(String path) {
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {

                for (Coords coords: object.getCoords()) {
                    if (coords.getX() < 0 || coords.getY() < 0) {
                        tempFilesToRemove.add(file);
                        imageFiles.removeAll(file.getImages());
                        jsonFiles.remove(file.getJsonFile());
                        break;
                    }
                }
            }
        }

        addToRemove(tempFilesToRemove, "coords_negative_", true);
        System.out.println("Files with negative coords: " + tempFilesToRemove.size());
    }

    public void validateCoordCountQuantileLow(String path, double low) {
        ArrayList<Integer> data = new ArrayList<Integer>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                data.add(object.getCoordsCount());
            }
        }
        Collections.sort(data);
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                int position = getPosition(object.getCoordsCount(), data);
                if (quantileLow(position, low, data)) {
                    tempFilesToRemove.add(file);
                    imageFiles.removeAll(file.getImages());
                    jsonFiles.remove(file.getJsonFile());
                    break;
                }
            }
        }
        addToRemove(tempFilesToRemove, "number_of_coords_is_in_low_" + low, true);
        System.out.println("Files with number of coords in low " + low + ": " + tempFilesToRemove.size());
    }

    public void validateCoordCountQuantileHigh(String path, double high) {
        ArrayList<Integer> data = new ArrayList<Integer>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                data.add(object.getCoordsCount());
            }
        }
        Collections.sort(data);
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                int position = getPosition(object.getCoordsCount(), data);
                if (quantileHigh(position, high, data)) {
                    tempFilesToRemove.add(file);
                    imageFiles.removeAll(file.getImages());
                    jsonFiles.remove(file.getJsonFile());
                    break;
                }
            }
        }
        addToRemove(tempFilesToRemove, "number_of_coords_is_in_high_" + high, true);
        System.out.println("Files with number of coords in high " + high + ": " + tempFilesToRemove.size());
    }

    public void validateAreaQuantileLow(String path, double low) {
        ArrayList<Double> data = new ArrayList<Double>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                data.add(object.getArea());
            }
        }
        Collections.sort(data);
        ArrayList<DataSet> tempFilesToRemove = new ArrayList<DataSet>();
        for (DataSet file : dataSets) {
            for (DataObject object : file.getObjects(path)) {
                int position = getPosition(object.getArea(), data);
                if (quantileLow(position, low, data, true)) {
                    tempFilesToRemove.add(file);
                    imageFiles.removeAll(file.getImages());
                    jsonFiles.remove(file.getJsonFile());
                    break;
                }
            }
        }
        addToRemove(tempFilesToRemove, "object_area_is_in_low_" + low, true);
        System.out.println("Files with bject area in low " + low + ": " + tempFilesToRemove.size());
    }

    public void finish(String processedPath, String outliersPath) throws IOException {
        for (DataSet dataSet : dataSets) {
            FileUtils.copyFileToDirectory(dataSet.getJsonFile().getFile(), new File(processedPath));
            System.out.println("File " + dataSet.getJsonFile().getFile().getName() + " copied to " + processedPath);
            for (DataFile image : dataSet.getImages()) {
                FileUtils.copyFileToDirectory(image.getFile(), new File(processedPath));
                System.out.println("File " + image.getFile().getName() + " copied to " + processedPath);
            }
        }
        for (DataFile file : filesToRemove) {
            FileUtils.copyFile(file.getFile(),
                    new File(outliersPath + "\\" + file.getOutlierName() + "." + file.getExt()));
            System.out.println("File " + file.getFile().getName() + " copied to " + outliersPath);
        }
    }


    private DataFile imageForJson(String name) {
        for (DataFile imageFile : imageFiles) {
            if (name.equals(imageFile.getName())) {
                return imageFile;
            }
        }
        return null;
    }

    private void addToRemove(ArrayList<DataFile> filesToAdd, String reason) {
        for (DataFile file : filesToAdd) {
            file.setOutlierReason(reason);
            filesToRemove.add(file);
        }
    }

    private void addToRemove(ArrayList<DataSet> filesToAdd, String reason, boolean erasure) {
        dataSets.removeAll(filesToAdd);
        for (DataSet dataSet : filesToAdd) {
            ArrayList<DataFile> images = dataSet.getImages();
            for (DataFile image : images) {
                image.setOutlierReason(reason);
                filesToRemove.add(image);
            }
            dataSet.getJsonFile().setOutlierReason(reason);
            filesToRemove.add(dataSet.getJsonFile());
        }
    }

    private static JsonElement getJsonElement(JsonElement json, String path){

        String[] parts = path.split("\\.|\\[|\\]");
        JsonElement result = json;

        for (String key : parts) {

            key = key.trim();
            if (key.isEmpty())
                continue;

            if (result == null){
                result = JsonNull.INSTANCE;
                break;
            }

            if (result.isJsonObject()){
                result = ((JsonObject)result).get(key);
            }
            else if (result.isJsonArray()){
                int ix = Integer.valueOf(key) - 1;
                result = ((JsonArray)result).get(ix);
            }
            else break;
        }

        return result;
    }

    private boolean quantileLow(int position, double percent, ArrayList<Integer> data) {
        return position <= (float)(data.size() / 100) * percent;
    }

    private boolean quantileLow(int position, double percent, ArrayList<Double> data, boolean fake) {
        return position <= (float)(data.size() / 100) * percent;
    }

    private boolean quantileHigh(int position, double percent, ArrayList<Integer> data) {
        return position >= (float)(data.size() / 100) * percent;
    }

    private int getPosition(int count, ArrayList<Integer> data) {
        int pos = data.size();
        for (int i = 0; i < data.size(); i++) {
            if (count <= data.get(i)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private int getPosition(double count, ArrayList<Double> data) {
        int pos = data.size();
        for (int i = 0; i < data.size(); i++) {
            if (count <= data.get(i)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private void removeFiles(ArrayList<DataSet> _files) {
        for (DataSet file : _files) {
            dataSets.remove(file);
            filesToRemove.addAll(file.getImages());
            filesToRemove.add(file.getJsonFile());
        }
    }

    public ArrayList<DataSet> getDataSets() {
        return dataSets;
    }

    public ArrayList<DataFile> getJsonFiles() {
        return jsonFiles;
    }

}
