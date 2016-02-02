package org.elasticsearch.docsearch.service;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.Map;

/**
 * Created by berk on 2/1/16.
 */
public class QueryEntity {
    private Map<String, String> match;

    public Map<String, String> getMatch() {

        return match;
    }

    public void setMatch(Map<String, String> match) {
        this.match = match;
    }
}
