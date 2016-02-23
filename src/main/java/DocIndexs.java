import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import utils.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by berk on 1/29/16.
 */
public class DocIndexs {
    private final static String path = "/home/lyz/workspace/elasticsearch-definitive-guide-cn";

    public static boolean createIndex(TransportClient client, String indexName) throws UnknownHostException {
        client.admin().indices().prepareCreate(indexName).execute().actionGet();
        return true;
    }

    public static boolean index(TransportClient client, XContentBuilder builderIndex, String type, String index) throws IOException {

        IndexResponse response = client.prepareIndex(index, type).setSource(builderIndex.string())
                .get();
        System.out.println(response);
        return true;
    }

    public static boolean createMapping(TransportClient client, String indexName, String type) throws IOException {
        File f = new File("/home/lyz/workspace/es-docsearch/src/main/resources/doc-default.json");
        InputStream in = new FileInputStream(f);
        byte b[] = new byte[(int) f.length()];     //创建合适文件大小的数组
        in.read(b);    //读取文件中的内容到b[]数组
//        InputStream is = Streams.class.getResourceAsStream("/home/lyz/workspace/es-docsearch/src/main/resources/doc.json");
        String mapping = new String(b);
        in.close();
        PutMappingRequest mappingInfo =
                Requests.putMappingRequest(indexName).type(type).source(mapping);
        client.admin().indices().putMapping(mappingInfo).actionGet();

        return true;
    }

    public static Map<String, String> getIndexFile() throws IOException {
        LinkedList<String> folderList = new LinkedList<String>();
        folderList.add(path);
        Map<String, String> fileList = new HashMap<>();
        while (folderList.size() > 0) {

            File file = new File(folderList.peek());
            folderList.removeFirst();
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
        String indexname = "docindex-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Settings settings = Settings.settingsBuilder().put("cluster.name", "my-application").build();

        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bd0"), 9300));
        if (createIndex(client, indexname)) {
            if (createMapping(client, indexname, "htmltype")) {
                Map<String, String> indexdoc = getIndexFile();
                for (String keys : indexdoc.keySet()) {
                    XContentBuilder builderIndex = XContentFactory.jsonBuilder().startObject()
                            .field("file", indexdoc.get(keys))
                            .field("title", keys.split("/")[keys.split("/").length - 1])
                            .field("date", LocalDateTime.now())
                            .endObject();
                    index(client, builderIndex, "htmltype", indexname);
                }
            }

        }
        client.close();
    }
}
