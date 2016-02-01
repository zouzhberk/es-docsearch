package org.elasticsearch.docsearch.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.docsearch.domain.ServerEntity;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

import java.io.IOException;


/**
 * Created by berk on 1/30/16.
 */
public interface ElasticSearchService {

    @GET("/")
    public Observable<ServerEntity> getServerInfo();

    @POST("/_search")
    public void search(@Body SearchRequest request);

    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:9200")
                .addConverterFactory(JacksonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory
                        .create())
                .build();
        ElasticSearchService esservice = retrofit.create(ElasticSearchService.class);
        esservice.getServerInfo().subscribe(x -> {
            System.out.println(x);
        });

    }
}
