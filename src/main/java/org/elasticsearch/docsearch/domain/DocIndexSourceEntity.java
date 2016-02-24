package org.elasticsearch.docsearch.domain;


import java.io.Serializable;

/**
 * Created by lyz on 2/23/16.
 */
public class DocIndexSourceEntity implements Serializable {
    @Override
    public String toString() {
        return "DocIndexSourceEntity{" +
                "title='" + title + '\'' +
                ", contentBase64='" + contentBase64 + '\'' +
                ", indexDateTime='" + indexDateTime + '\'' +
                ", path='" + path + '\'' +
                ", parentTitle='" + parentTitle + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    private String title;
    private String contentBase64;
    private String indexDateTime;
    private String path;
    private String parentTitle;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public String getContentBase64() {
        return contentBase64;
    }

    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }

    public String getIndexDateTime() {
        return indexDateTime;
    }

    public void setIndexDateTime(String indexDateTime) {
        this.indexDateTime = indexDateTime;
    }
}
