package org.elasticsearch.docsearch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.*;
import rx.Observable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by lyz on 2/1/16.
 */
public interface DocIndexService {

    @PUT("/{index}")
    public Observable<Object> createIndex(@Path("index") String indexName, @Body ElasticSearchService.RequestEntity body);

    @PUT("/{index}/{type}/_mapping")
    public Observable<Object> putMapping(@Path("index") String indexName, @Path(("type")) String type, @Body ElasticSearchService.RequestEntity body);

    @POST("/{index}/{type}/{id}")
    public Observable<Object> indexDoc(@Path("index") String indexName, @Path(("type")) String type, @Path("id") String id, @Body ElasticSearchService.RequestEntity body);

    @DELETE("/{index}")
    public Observable<Object> deleteIndex(@Path("index") String indexName);

    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:9200")
                .addConverterFactory(JacksonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory
                        .create())
                .build();
        DocIndexService esservice = retrofit.create(DocIndexService.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String indexbody = "{\n" +
                "    \"settings\": {\n" +
                "      \"number_of_shards\": \"5\",\n" +
                "      \"number_of_replicas\": \"1\"\n" +
                "    }\n" +
                "  }";


        ElasticSearchService.RequestEntity param = gson.fromJson(indexbody, ElasticSearchService.RequestEntity.class);
        long start = System.currentTimeMillis();
        String indexname = "docindex-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        esservice.createIndex(indexname, param).toBlocking().subscribe(x -> System.out.println(x));
        String indexmapping =
                " {\n" +
                        "    \"properties\": {\n" +
                        "      \"file\": {\n" +
                        "        \"type\": \"attachment\",\n" +
                        "        \"fields\": {\n" +
                        "          \"content\": {\n" +
                        "            \"type\": \"string\",\n" +
                        "            \"store\": true,\n" +
                        "            \"term_vector\": \"with_positions_offsets\",\n" +
                        "            \"analyzer\": \"ik\"\n" +
                        "          }\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"title\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"store\": true,\n" +
                        "        \"analyzer\": \"ik\",\n" +
                        "        \"term_vector\": \"with_positions_offsets\"\n" +
                        "      },\n" +
                        "      \"date\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"strict_date_optional_time||epoch_millis\"\n" +
                        "      },\n" +
                        "      \"path\": {\n" +
                        "        \"type\": \"string\"\n" +
                        "      },\n" +
                        "      \"parenttitle\": {\n" +
                        "        \"type\": \"string\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  } ";
        ElasticSearchService.RequestEntity paramMapping = gson.fromJson(indexmapping, ElasticSearchService.RequestEntity.class);
        esservice.putMapping(indexname, "doctype", paramMapping).subscribe(x -> System.out.println(x));
        esservice.deleteIndex(indexname).subscribe(x -> System.out.println(x));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
