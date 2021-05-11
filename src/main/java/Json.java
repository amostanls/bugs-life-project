import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Json {

    private static ObjectMapper om = getDefaultOM();

    private static ObjectMapper getDefaultOM() {
        ObjectMapper defaultOM = new ObjectMapper();
        defaultOM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultOM;
    }

    public static JsonNode parse(URL source) throws JsonProcessingException, IOException {
//        return om.readTree(source);
        return om.readTree(source);
    }

    public static <E> E fromJson(JsonNode node, Class<E> calzz) throws JsonProcessingException {
        return om.treeToValue(node, calzz);
    }

    public static JsonNode toJson(Object o) {
        return om.valueToTree(o);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateString(node, false);
    }

    public static String prettyPrint(JsonNode node) throws JsonProcessingException {
        return generateString(node, true);
    }

    private static String generateString(JsonNode node, boolean pretty) throws JsonProcessingException {
        ObjectWriter ow = om.writer();
        if (pretty) {
            ow = ow.with(SerializationFeature.INDENT_OUTPUT);
        }
        return ow.writeValueAsString(node);
    }
    
    public static <E> void writeListIntoJson(List<E> list) {
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(new File("DataBase.json"), list);
        } catch (IOException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static <E> ArrayList<E> readAllData (Class<E> calzz) {
        ArrayList<E> list = new ArrayList<>();
        try {
            list = om.readerFor(calzz).readValue(new File("DataBase.json"));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
    
    public static <E> E readAll (String str, Class<E> clazz) throws MalformedURLException, IOException {
        return om.readValue(str, clazz);
    }
    
    public static <E> E readAll (URL jsonUrl, Class<E> clazz) throws MalformedURLException, IOException {
        return om.readValue(jsonUrl, clazz);
    }
}
