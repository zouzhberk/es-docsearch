package org.elasticsearch.docsearch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.docsearch.domain.DocIndexSourceEntity;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.*;
import rx.Observable;
import utils.DocIndexSourceHelper;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by lyz on 2/1/16.
 */
public interface DocIndexService {

    @PUT("/{index}")
    public Observable<Object> createIndex(@Path("index") String indexName, @Body DocSearchService.RequestEntity body);

    @PUT("/{index}/{type}/_mapping")
    public Observable<Object> putMapping(@Path("index") String indexName, @Path(("type")) String type, @Body DocSearchService.RequestEntity body);

    @POST("/{index}/{type}/{id}")
    public Observable<Object> indexDoc(@Path("index") String indexName, @Path(("type")) String type, @Path("id") String id, @Body DocSearchService.RequestEntity body);

    public static Observable<Object> putMapping(DocIndexService service, String indexName, String type, DocSearchService.RequestEntity mappingJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return service.putMapping(indexName, type, mappingJson);
    }

    public static Observable<Object> indexDoc(DocIndexService service, String indexName, String type, DocIndexSourceEntity entity) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        DocSearchService.RequestEntity request = gson.fromJson(gson.toJson(entity), DocSearchService.RequestEntity.class);
        return service.indexDoc(indexName, type, entity.getPath(), request);
    }

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


        DocSearchService.RequestEntity param = gson.fromJson(indexbody, DocSearchService.RequestEntity.class);
        long start = System.currentTimeMillis();
        String indexname = "helpcenter-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        esservice.createIndex(indexname, param).toBlocking().subscribe(x -> System.out.println(x));
        String indexmapping =
                " {\n" +
                        "  \"indextype\": {\n" +
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
                        "        \"index\": \"not_analyzed\",\n" +
                        "        \"term_vector\": \"with_positions_offsets\"\n" +
                        "      },\n" +
                        "      \"date\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"strict_date_optional_time||epoch_millis\"\n" +
                        "      },\n" +
                        "      \"path\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"index\": \"not_analyzed\"\n" +
                        "      },\n" +
                        "      \"parenttitle\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"index\": \"not_analyzed\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        DocSearchService.RequestEntity paramMapping = gson.fromJson(indexmapping, DocSearchService.RequestEntity.class);
//        esservice.putMapping(indexname, "doctype", paramMapping).toBlocking().subscribe(x -> System.out.println(x));
        DocIndexSourceHelper.listDocTypeName(Paths.get(DocIndexSourceHelper.DOC_PATH)).forEach(x -> {
            System.out.println(indexmapping.replaceAll("indextype", x.toString()));
            putMapping(esservice, indexname, x.toString(), gson.fromJson(indexmapping.replaceAll("indextype", x.toString()),
                    DocSearchService.RequestEntity.class)).toBlocking().subscribe(System.out::println);
        });
        DocIndexSourceHelper.listDocEntries(Paths.get(DocIndexSourceHelper.DOC_PATH)).forEach(x -> indexDoc(esservice, indexname, Paths.get(x.getPath())
                .getParent().getParent().getFileName().toString(), x).toBlocking().subscribe(System.out::println));
        //  esservice.deleteIndex(indexname).subscribe(x -> System.out.println(x));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
