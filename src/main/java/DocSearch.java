import org.elasticsearch.common.io.Streams;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created by berk on 1/29/16.
 */
public class DocSearch {


    public static void main(String[] args) throws IOException {

        BASE64Encoder encoder = new BASE64Encoder();

        Path path = Paths.get("/home/berk/IdeaProjects/es-docsearch/README.md");
        InputStream buffer1 = Files.newInputStream(path);
        long size = Files.size(path);

        //BufferedReader aa = new BufferedReader(buffer1);

        byte[] bytes = new byte[Long.valueOf(size).intValue()];
        Streams.readFully(buffer1, bytes);
        Base64.getEncoder().encode(bytes);
    }

}
