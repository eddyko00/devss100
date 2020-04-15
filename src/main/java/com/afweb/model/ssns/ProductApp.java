/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.model.ssns;

/**
 *
 * @author koed
 */
public class ProductApp {

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the feat
     */
    public String getFeat() {
        return feat;
    }

    /**
     * @param feat the feat to set
     */
    public void setFeat(String feat) {
        this.feat = feat;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the statusCd
     */
    public String getStatusCd() {
        return statusCd;
    }

    /**
     * @param statusCd the statusCd to set
     */
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    /**
     * @return the categoryCd
     */
    public String getCategoryCd() {
        return categoryCd;
    }

    /**
     * @param categoryCd the categoryCd to set
     */
    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    private String feat = "";
    private String category = "";
    private String statusCd = "";
    private String categoryCd ="";
    private String host = "";    
}
