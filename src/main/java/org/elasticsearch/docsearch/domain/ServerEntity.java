package org.elasticsearch.docsearch.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by berk on 1/30/16.
 */
public class ServerEntity {


    /**
     * name : Nekra Sinclar
     * cluster_name : my-application
     * version : {"number":"2.1.1","build_hash":"40e2c53a6b6c2972b3d13846e450e66f4375bd71",
     * "build_timestamp":"2015-12-15T13:05:55Z","build_snapshot":false,"lucene_version":"5.3.1"}
     * tagline : You Know, for Search
     */

    private String name;
    @JsonProperty("cluster_name")
    private String clusterName;
    /**
     * number : 2.1.1
     * build_hash : 40e2c53a6b6c2972b3d13846e450e66f4375bd71
     * build_timestamp : 2015-12-15T13:05:55Z
     * build_snapshot : false
     * lucene_version : 5.3.1
     */

    private VersionEntity version;
    private String tagline;

    public void setName(String name) {
        this.name = name;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setVersion(VersionEntity version) {
        this.version = version;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getName() {
        return name;
    }

    public String getClusterName() {
        return clusterName;
    }

    public VersionEntity getVersion() {
        return version;
    }

    public String getTagline() {
        return tagline;
    }

    @Override
    public String toString() {
        return "ServerEntity{" +
                "name='" + name + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", version=" + version +
                ", tagline='" + tagline + '\'' +
                '}';
    }

    public static class VersionEntity {
        private String number;
        @JsonProperty("build_hash")
        private String buildHash;
        @JsonProperty("build_timestamp")
        private String buildTimestamp;
        @JsonProperty("build_snapshot")
        private boolean buildSnapshot;
        @JsonProperty("lucene_version")
        private String luceneVersion;

        public void setNumber(String number) {
            this.number = number;
        }

        public void setBuildHash(String buildHash) {
            this.buildHash = buildHash;
        }

        public void setBuildTimestamp(String buildTimestamp) {
            this.buildTimestamp = buildTimestamp;
        }

        public void setBuildSnapshot(boolean buildSnapshot) {
            this.buildSnapshot = buildSnapshot;
        }

        public void setLuceneVersion(String luceneVersion) {
            this.luceneVersion = luceneVersion;
        }

        public String getNumber() {
            return number;
        }

        public String getBuildHash() {
            return buildHash;
        }

        public String getBuildTimestamp() {
            return buildTimestamp;
        }

        public boolean isBuildSnapshot() {
            return buildSnapshot;
        }

        public String getLuceneVersion() {
            return luceneVersion;
        }

        @Override
        public String toString() {
            return "VersionEntity{" +
                    "number='" + number + '\'' +
                    ", buildHash='" + buildHash + '\'' +
                    ", buildTimestamp='" + buildTimestamp + '\'' +
                    ", buildSnapshot=" + buildSnapshot +
                    ", luceneVersion='" + luceneVersion + '\'' +
                    '}';
        }
    }
}
