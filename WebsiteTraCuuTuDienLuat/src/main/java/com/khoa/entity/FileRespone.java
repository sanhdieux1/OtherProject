package com.khoa.entity;

public class FileRespone {
    private String fileName;
    private boolean isSaved;
    private boolean isConverted;
    private boolean isInsertedDatabase;
    private boolean isIgnore;
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public boolean isIgnore() {
        return isIgnore;
    }
    public void setIgnore(boolean isIgnore) {
        this.isIgnore = isIgnore;
    }
    public boolean isSaved() {
        return isSaved;
    }
    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }
    public boolean isConverted() {
        return isConverted;
    }
    public void setConverted(boolean isConverted) {
        this.isConverted = isConverted;
    }
    public boolean isInsertedDatabase() {
        return isInsertedDatabase;
    }
    public void setInsertedDatabase(boolean isInsertedDatabase) {
        this.isInsertedDatabase = isInsertedDatabase;
    }
    
    
}
