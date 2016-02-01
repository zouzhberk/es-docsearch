package org.elasticsearch.docsearch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.docsearch.domain.SearchResponse;
import org.elasticsearch.docsearch.domain.ServerEntity;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.*;
import rx.Observable;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by berk on 1/30/16.
 */
public interface ElasticSearchService {

    @GET("/")
    public Observable<ServerEntity> getServerInfo();

    @POST("/_search")
    public Observable<SearchResponse> search(@Query("size") int size, @Query("from") int from);

    @POST("/_search")
    public Observable<SearchResponse> search();

    @POST("/{index}/_search")
    public Observable<SearchResponse> search(@Path("index") String indexName, @Body RequestEntity body);

    @POST("/{index}/_search")
    public Observable<SearchResponse> search(@Path("index") String indexName, @Body RequestEntity body, @Query
            ("size") int size, @Query("from") int from);


    public static class RequestEntity extends HashMap<String, Object> {

    }

    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:9200")
                .addConverterFactory(JacksonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory
                        .create())
                .build();
        ElasticSearchService esservice = retrofit.create(ElasticSearchService.class);
        esservice.getServerInfo().subscribe(x -> {
            System.out.println(x);
        });
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        //  esservice.search().subscribe(x -> System.out.println(gson.toJson(x)));
        // QueryBuilders.indicesQuery(QueryBuilder.)

//        String body = SearchSourceBuilder.searchSource().fields("file.content").highlighter().highlightQuery
// (QueryBuilders.matchQuery("file" +
//                ".content","StackOverflow")).;

        String body = "{\n" +
                "    \"fields\": [\n" +
                "      \"file.content\"\n" +
                "    ],\n" +
                "    \"query\": {\n" +
                "      \"match\": {\n" +
                "        \"file.content\": \"搜索引擎\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"highlight\": {\n" +
                "      \"fields\": {\n" +
                "        \"file.content\": {\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";


        RequestEntity param = gson.fromJson(body, RequestEntity.class);
        System.out.println(param);
        long start = System.currentTimeMillis();
        esservice.search("docindex1454315265611", param, 2, 1).toBlocking().subscribe(x -> {
            System.out.println(gson.toJson(x));
        });
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


}
