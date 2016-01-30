import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import utils.Base64Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 * Created by berk on 1/29/16.
 */
public class DocIndexs {
    private final static String path = "/home/lyz/workspace/elasticsearch-definitive-guide-cn/010_Intro";

    public static boolean createIndex(String indexName) throws UnknownHostException {
        new Client().getClient().admin().indices().prepareCreate(indexName).execute().actionGet();
        return true;
    }

    public static boolean index(XContentBuilder builderIndex) throws IOException {

        IndexResponse response = new Client().getClient().prepareIndex("docindex", "htmltype").setSource(builderIndex.string())
                .get();
        System.out.println(response);
        return true;
    }

    public static boolean createMapping() {
        return true;
    }

    public static Map<String, String> getIndexFile() throws IOException {
        LinkedList<String> folderList = new LinkedList<String>();
        folderList.add(path);
        Map<String, String> fileList = new HashMap<String, String>();
        while (folderList.size() > 0) {

            File file = new File(folderList.peek());
            folderList.removeLast();
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i].getPath());
                } else {

                    fileList.put(files[i].getName(), new String(Base64Utils.encodeToBase64(files[i].toPath())));
                }
            }

        }
        return fileList;
    }

    public static void main(String[] args) throws IOException {
        if (createIndex("docindex")) {
            Map<String, String> indexdoc = getIndexFile();
            for (String keys : indexdoc.keySet()) {
                XContentBuilder builderIndex = XContentFactory.jsonBuilder().startObject().field("title", keys)
                        .field("content", indexdoc.get(keys)).endObject();
                index(builderIndex);
            }

        }
    }
}
