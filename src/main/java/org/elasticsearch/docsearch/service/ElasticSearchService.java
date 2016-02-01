package org.elasticsearch.docsearch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.docsearch.domain.SearchResponse;
import org.elasticsearch.docsearch.domain.ServerEntity;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import java.io.IOException;


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

        esservice.search().subscribe(x -> System.out.println(gson.toJson(x)));
        // QueryBuilders.indicesQuery(QueryBuilder.)
    }
}
