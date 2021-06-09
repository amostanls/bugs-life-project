package bugs;

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

    public static ObjectMapper getDefaultOM() {
        ObjectMapper defaultOM = new ObjectMapper();
        defaultOM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultOM;
    }

    public static JsonNode parseUrl(URL source) throws JsonProcessingException, IOException {
        return om.readTree(source);
    }

    public static JsonNode parseFile(File jsonFile) throws JsonProcessingException, IOException {
        return om.readTree(jsonFile);
    }
}