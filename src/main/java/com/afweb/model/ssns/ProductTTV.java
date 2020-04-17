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
public class ProductTTV {

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the PrimaryPricePlan
     */
    public String getPrimaryPricePlan() {
        return PrimaryPricePlan;
    }

    /**
     * @param PrimaryPricePlan the PrimaryPricePlan to set
     */
    public void setPrimaryPricePlan(String PrimaryPricePlan) {
        this.PrimaryPricePlan = PrimaryPricePlan;
    }

    /**
     * @return the UnlimitedUsage
     */
    public String getUnlimitedUsage() {
        return UnlimitedUsage;
    }

    /**
     * @param UnlimitedUsage the UnlimitedUsage to set
     */
    public void setUnlimitedUsage(String UnlimitedUsage) {
        this.UnlimitedUsage = UnlimitedUsage;
    }

    /**
     * @return the EmailFeatures
     */
    public String getEmailFeatures() {
        return EmailFeatures;
    }

    /**
     * @param EmailFeatures the EmailFeatures to set
     */
    public void setEmailFeatures(String EmailFeatures) {
        this.EmailFeatures = EmailFeatures;
    }

    /**
     * @return the SecurityBundle
     */
    public String getSecurityBundle() {
        return SecurityBundle;
    }

    /**
     * @param SecurityBundle the SecurityBundle to set
     */
    public void setSecurityBundle(String SecurityBundle) {
        this.SecurityBundle = SecurityBundle;
    }

    private String featTTV = "";
    private int isFIFA = 0;
    private String offer = "";
    private String productCd = "";
    private int ChannelList = 0;
    private String region="";
    private String SecurityBundle = "";
    private String EmailFeatures="";
    private String UnlimitedUsage="";
    private String PrimaryPricePlan="";

    /**
     * @return the featTTV
     */
    public String getFeatTTV() {
        return featTTV;
    }

    /**
     * @param featTTV the featTTV to set
     */
    public void setFeatTTV(String featTTV) {
        this.featTTV = featTTV;
    }

    /**
     * @return the isFIFA
     */
    public int getIsFIFA() {
        return isFIFA;
    }

    /**
     * @param isFIFA the isFIFA to set
     */
    public void setIsFIFA(int isFIFA) {
        this.isFIFA = isFIFA;
    }

    /**
     * @return the offer
     */
    public String getOffer() {
        return offer;
    }

    /**
     * @param offer the offer to set
     */
    public void setOffer(String offer) {
        this.offer = offer;
    }

    /**
     * @return the productCd
     */
    public String getProductCd() {
        return productCd;
    }

    /**
     * @param productCd the productCd to set
     */
    public void setProductCd(String productCd) {
        this.productCd = productCd;
    }

    /**
     * @return the ChannelList
     */
    public int getChannelList() {
        return ChannelList;
    }

    /**
     * @param ChannelList the ChannelList to set
     */
    public void setChannelList(int ChannelList) {
        this.ChannelList = ChannelList;
    }
}
