/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.model.ssns;

import java.util.ArrayList;

/**
 *
 * @author koed
 */
public class ProductData {

    /**
     * @return the postParam
     */
    public String getPostParam() {
        return postParam;
    }

    /**
     * @param postParam the postParam to set
     */
    public void setPostParam(String postParam) {
        this.postParam = postParam;
    }

    /**
     * @return the pSSNS
     */
    public ProductApp getpSSNS() {
        return pSSNS;
    }

    /**
     * @param pSSNS the pSSNS to set
     */
    public void setpSSNS(ProductApp pSSNS) {
        this.pSSNS = pSSNS;
    }
    /**
     * @return the pSING
     */
    public ProductTTV getpSING() {
        return pSING;
    }

    /**
     * @param pSING the pSING to set
     */
    public void setpSING(ProductTTV pSING) {
        this.pSING = pSING;
    }

    private ProductTTV pSING;    
    private ProductTTV pTTV;
    private ProductTTV pHSIC;
    private ProductApp pSSNS; 
    private String postParam="";
    private ArrayList<String> flow;

    /**
     * @return the pTTV
     */
    public ProductTTV getpTTV() {
        return pTTV;
    }

    /**
     * @param pTTV the pTTV to set
     */
    public void setpTTV(ProductTTV pTTV) {
        this.pTTV = pTTV;
    }

    /**
     * @return the flow
     */
    public ArrayList<String> getFlow() {
        return flow;
    }

    /**
     * @param flow the flow to set
     */
    public void setFlow(ArrayList<String> flow) {
        this.flow = flow;
    }

    /**
     * @return the pHSIC
     */
    public ProductTTV getpHSIC() {
        return pHSIC;
    }

    /**
     * @param pHSIC the pHSIC to set
     */
    public void setpHSIC(ProductTTV pHSIC) {
        this.pHSIC = pHSIC;
    }
}
