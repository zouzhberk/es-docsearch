package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.docsearch.domain.DocIndexSourceEntity;
import org.joda.time.DateTime;

/**
 * Created by berk on 1/29/16.
 */
final public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象序列化为JSON字符串
     *
     * @param object
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static void main(String[] args) {
        DocIndexSourceEntity entity = new DocIndexSourceEntity();
        entity.setContentBase64("asfa");
        entity.setIndexDateTime(new DateTime().toString());
        entity.setParentTitle("a");
        entity.setPath("1212");
        entity.setTitle("azsaf");
        System.out.println(serialize(entity));
    }
//    /**
//     * 将JSON字符串反序列化为对象
//     *
//     * @param object
//     * @return JSON字符串
//     */
//    public static <T> T deserialize(String json, Class<T> clazz) {
//        Object object = null;
//        try {
//            object = objectMapper.readValue(json, TypeFactory.rawClass(clazz));
//        } catch (JsonParseException e) {
//            logger.error("JsonParseException when serialize object to json", e);
//        } catch (JsonMappingException e) {
//            logger.error("JsonMappingException when serialize object to json", e);
//        } catch (IOException e) {
//            logger.error("IOException when serialize object to json", e);
//        }
//        return (T) object;
//    }
//
//    /**
//     * 将JSON字符串反序列化为对象
//     *
//     * @param object
//     * @return JSON字符串
//     */
//    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
//        try {
//            return (T) objectMapper.readValue(json, typeRef);
//        } catch (JsonParseException e) {
//            logger.error("JsonParseException when deserialize json", e);
//        } catch (JsonMappingException e) {
//            logger.error("JsonMappingException when deserialize json", e);
//        } catch (IOException e) {
//            logger.error("IOException when deserialize json", e);
//        }
//        return null;
//    }

}
