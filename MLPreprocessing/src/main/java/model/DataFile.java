package model;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class DataFile {
    private File file;
    private String name;
    private String ext;
    private String outlierName;

    public DataFile(File file) {
        this.file = file;
        this.name = FilenameUtils.getBaseName(file.getName());
        this.ext = FilenameUtils.getExtension(file.getName());
        this.outlierName = name;
    }

    public void setOutlierReason(String reason) {
        this.outlierName = reason + "_" + name;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public String getOutlierName() {
        return outlierName;
    }
}
