package utils;

import org.elasticsearch.common.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created by berk on 1/29/16.
 */
final public class Base64Utils {


    public static byte[] encodeToBase64(Path path) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        InputStream buffer1 = Files.newInputStream(path);
        long size = Files.size(path);
        byte[] bytes = new byte[Long.valueOf(size).intValue()];

        Streams.readFully(buffer1, bytes);
        return encoder.encode(bytes);
    }

    public static byte[] decodeToBytes(String base64String) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64String);
    }

    public static String decodeToString(String base64String) throws IOException {
        return new String(decodeToBytes(base64String));
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/home/berk/IdeaProjects/es-docsearch/README.md");
        String base64str = new String(encodeToBase64(path));

        System.out.println(base64str);

        byte[] output = decodeToBytes(base64str);
        System.out.println(new String(output));

    }

}
