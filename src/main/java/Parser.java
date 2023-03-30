import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static String[] COLUMNMAPPING = {"id", "firstName", "lastName", "country", "age"};
    public static String FILENAME = "data.csv";

    public static List<Employee> parseCSV(String[] id, String name) {
        try (CSVReader reader = new CSVReader(new FileReader(name))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(id);
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String file) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();
        List<String> attributes = new ArrayList<>();
        DocumentBuilder dbf = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dbf.parse(file);
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("employee")) {
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        attributes.add(node1.getTextContent());
                    }
                }
                employees.add(new Employee(Long.parseLong(attributes.get(0)),
                        attributes.get(1),
                        attributes.get(2), attributes.get(3), Integer.parseInt(attributes.get(4))));
            }
        }
        return employees;
    }

    public static String readString(String fileName) {
        try {
            return new String(Files.readAllBytes(Path.of(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> jsonToList(String fileName) {

        return new Gson().fromJson(fileName, new TypeToken<List<Employee>>() {
        }.getType());
    }

}