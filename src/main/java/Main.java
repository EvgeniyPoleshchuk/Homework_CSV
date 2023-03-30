import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main extends Parser{
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        List<Employee> list = parseCSV(COLUMNMAPPING,FILENAME);
        String json = listToJson(list);
        writeString(json,"data.json");

        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json1,"data2.json");

        String json2 = readString("data.json");
        List<Employee> list2 = jsonToList(json2);
        list2.forEach(System.out::println);
    }



}

