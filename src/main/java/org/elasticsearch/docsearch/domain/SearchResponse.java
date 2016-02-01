package org.elasticsearch.docsearch.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by berk on 2/1/16.
 */
public class SearchResponse {

    private int took;
    @JsonProperty("timed_out")
    private boolean timedOut;

    @JsonProperty("_shards")
    private ShardsEntity shards;


    private HitsEntity hits;

    public void setTook(int took) {
        this.took = took;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public void setShards(ShardsEntity shards) {
        this.shards = shards;
    }

    public void setHits(HitsEntity hits) {
        this.hits = hits;
    }

    public int getTook() {
        return took;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public ShardsEntity getShards() {
        return shards;
    }

    public HitsEntity getHits() {
        return hits;
    }

    public static class ShardsEntity {
        private int total;
        private int successful;
        private int failed;

        public void setTotal(int total) {
            this.total = total;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }

        public int getTotal() {
            return total;
        }

        public int getSuccessful() {
            return successful;
        }

        public int getFailed() {
            return failed;
        }

    }

    public static class HitsEntity {
        private int total;
        @JsonProperty("max_score")
        private double maxScore;
        private List<HitEntity> hits;

        public void setTotal(int total) {
            this.total = total;
        }

        public void setMaxScore(double maxScore) {
            this.maxScore = maxScore;
        }

        public void setHits(List<HitEntity> hits) {
            this.hits = hits;
        }

        public int getTotal() {
            return total;
        }

        public double getMaxScore() {
            return maxScore;
        }

        public List<HitEntity> getHits() {
            return hits;
        }

        public static class HitEntity {
            @JsonProperty("_index")
            private String indexName;
            @JsonProperty("_type")
            private String typeName;
            @JsonProperty("_id")
            private String id;
            @JsonProperty("_score")
            private double score;
            private Map<String, List<String>> fields;
            private Map<String, List<String>> highlight;

            public Map<String, List<String>> getHighlight() {
                return highlight;
            }

            public void setHighlight(Map<String, List<String>> highlight) {
                this.highlight = highlight;
            }

            public void setIndexName(String indexName) {
                this.indexName = indexName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setScore(double score) {
                this.score = score;
            }


            public String getIndexName() {
                return indexName;
            }

            public String getTypeName() {
                return typeName;
            }

            public String getId() {
                return id;
            }

            public double getScore() {
                return score;
            }

            public Map<String, List<String>> getFields() {
                return fields;
            }

            public void setFields(Map<String, List<String>> fields) {
                this.fields = fields;
            }
        }
    }
}
