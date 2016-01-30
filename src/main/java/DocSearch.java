import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.docsearch.DocSearchRequest;
import org.elasticsearch.docsearch.DocumentEntity;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.NodeBuilder;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Created by berk on 1/29/16.
 */
public class DocSearch {


    public static void main(String[] args) {
        System.out.println("hello world");
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client;
        InetSocketAddress address = new InetSocketAddress("localhost", 9300);
        TransportAddress transportAddress = new InetSocketTransportAddress(address);
        client = TransportClient.builder().settings(settings).build().addTransportAddress(transportAddress);


        ListenableActionFuture<SearchResponse> future = client.prepareSearch("test")
                .setTypes("htmltype")
                .addField("file.content")
                .addHighlightedField("file.content")
                .setHighlighterQuery(QueryBuilders.matchQuery("file.content", "搜索引擎"))
                .execute();

//
//        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//        SearchResponse response = future.actionGet();
//        long took = response.getTook().getMillis();
//        System.out.println(took);
//        SearchHits hits = response.getHits();
//        System.out.println(hits.totalHits());
//
//        hits.forEach(hit -> {
//            hit.getHighlightFields().forEach((x, y) -> {
//                System.out.println(new String(x));
//                System.out.println(y.getName());
//                System.out.println(Arrays.toString(y.fragments()));
//                //System.out.println(Arrays.toString(y.fragments()));
//            });
//        });

        PublishSubject.from(future).observeOn(Schedulers.newThread()).map(x -> x.getHits()).flatMap(x -> Observable
                .from(x)).flatMap(x -> Observable.from(x.getHighlightFields().values()))
                .subscribe(x -> {
                    System.out.println(Arrays.toString(x.fragments()));
                });
    }

    public List<DocumentEntity> search(DocSearchRequest param) {
        String clusterName = "";
        Settings.Builder setting = Settings.settingsBuilder().put("http.enabled", false);
        NodeBuilder.nodeBuilder().client(true).clusterName(clusterName).settings(setting);

        return null;
    }
}
