package com.lab.serverlab1.model.ManagedBeans;
import java.text.DecimalFormat;
public class SimilarImage {
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    private String fileName;
    private float similarity;

    public SimilarImage(String fileName, float similarity) {
        this.fileName = fileName;
        this.similarity = similarity;
    }

    public String getSimilarityPercentage(){
        DecimalFormat df = new DecimalFormat("#%");
        return df.format(similarity);
    }

}
