package iss.work.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import iss.work.addressbook.model.GroupData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {
    Logger logger = LoggerFactory.getLogger(GroupDataGenerator.class);

    @Parameter(names = "-c", description = "Group count")
    public int count;
    @Parameter(names = "-f", description = "Target file")
    public String file;

    public static void main(String[] args) throws IOException {
        GroupDataGenerator generator = new GroupDataGenerator();
        JCommander jCommander = new JCommander(generator);
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        List<GroupData> groups = generateGroups(count);
        switch (file.substring(file.lastIndexOf("."))) {
            case ".csv":
                saveAsCsv(groups, new File(file));
                break;
            case ".xml":
                saveAsXml(groups, new File(file));
                break;
            case ".json":
                saveAsJson(groups, new File(file));
                break;
            default:
                logger.error("Unrecognized format " + file.substring(file.lastIndexOf(".")));
                break;
        }
    }

    private void saveAsJson(List<GroupData> groups, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(groups);
        Writer writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    private void saveAsXml(List<GroupData> groups, File file) throws IOException {
        XStream xStream = new XStream();
        xStream.processAnnotations(GroupData.class);
        String xml = xStream.toXML(groups);
        Writer writer = new FileWriter(file);
        writer.write(xml);
        writer.close();
    }

    private void saveAsCsv(List<GroupData> groups, File file) throws IOException {
        Writer writer = new FileWriter(file);
        for (GroupData group : groups) {
            writer.write(String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter()));
        }
        writer.close();
    }

    private List<GroupData> generateGroups(int count) {
        ArrayList<GroupData> groups = new ArrayList<GroupData>();
        for(int i = 0; i < count; i++) {
            GroupData group = new GroupData().withName(String.format("test %s", i))
                    .withHeader(String.format("header %s", i))
                    .withHeader(String.format("footer %s", i));
            groups.add(group);
        }
        return groups;
    }
}
