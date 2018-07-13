package org.springframework.boot.autoconfigure.data.requery;

import io.requery.sql.TableCreationMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryProperties
 *
 * @author debop
 */
@ConfigurationProperties("spring.data.requery")
public class RequeryProperties {

    private String modelName = "";

    private Integer batchUpdateSize = 100;

    private Integer statementCacheSize = 1024;

    private TableCreationMode tableCreationMode = TableCreationMode.CREATE_NOT_EXISTS;


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getBatchUpdateSize() {
        return batchUpdateSize;
    }

    public void setBatchUpdateSize(Integer batchUpdateSize) {
        this.batchUpdateSize = batchUpdateSize;
    }

    public Integer getStatementCacheSize() {
        return statementCacheSize;
    }

    public void setStatementCacheSize(Integer statementCacheSize) {
        this.statementCacheSize = statementCacheSize;
    }

    public TableCreationMode getTableCreationMode() {
        return tableCreationMode;
    }

    public void setTableCreationMode(TableCreationMode tableCreationMode) {
        this.tableCreationMode = tableCreationMode;
    }

}
