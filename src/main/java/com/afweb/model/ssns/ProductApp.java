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
     * @return the cujoAgent
     */
    public int getCujoAgent() {
        return cujoAgent;
    }

    /**
     * @param cujoAgent the cujoAgent to set
     */
    public void setCujoAgent(int cujoAgent) {
        this.cujoAgent = cujoAgent;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the guestDevice
     */
    public int getGuestDevice() {
        return guestDevice;
    }

    /**
     * @param guestDevice the guestDevice to set
     */
    public void setGuestDevice(int guestDevice) {
        this.guestDevice = guestDevice;
    }

    /**
     * @return the smartSteering
     */
    public String getSmartSteering() {
        return smartSteering;
    }

    /**
     * @param smartSteering the smartSteering to set
     */
    public void setSmartSteering(String smartSteering) {
        this.smartSteering = smartSteering;
    }

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

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
    private String categoryCd = "";
    private String host = "";
    private String appid = "";
    private String smartSteering="";
    private int guestDevice=0;
    private String frequency="";
    private int cujoAgent=0;    
}
