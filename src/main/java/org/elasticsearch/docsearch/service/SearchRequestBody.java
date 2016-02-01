package org.elasticsearch.docsearch.service;

import java.util.List;

/**
 * Created by berk on 2/1/16.
 */
public class SearchRequestBody {

    public static class Builder {
        public static Builder builder() {
            return new Builder();
        }

        public SearchRequestBody build() {
            return new SearchRequestBody();
        }
    }

    public QueryEntity getQuery() {
        return query;
    }

    public void setQuery(QueryEntity query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "SearchRequestBody{" +
                "query=" + query +
                ", highlight=" + highlight +
                ", fields=" + fields +
                '}';
    }

    private QueryEntity query;

    private HighlightQueryEntity highlight;

    public HighlightQueryEntity getHighlight() {
        return highlight;
    }

    public void setHighlight(HighlightQueryEntity highlight) {
        this.highlight = highlight;
    }

    private List<String> fields;

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }
}
