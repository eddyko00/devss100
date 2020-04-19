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
     * @return the pWIFI
     */
    public ProductApp getpWIFI() {
        return pWIFI;
    }

    /**
     * @param pWIFI the pWIFI to set
     */
    public void setpWIFI(ProductApp pWIFI) {
        this.pWIFI = pWIFI;
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

    /**
     * @return the pAPP
     */
    public ProductApp getpAPP() {
        return pAPP;
    }

    /**
     * @param pAPP the pAPP to set
     */
    public void setpAPP(ProductApp pAPP) {
        this.pAPP = pAPP;
    }

    private ProductTTV pSING;    
    private ProductTTV pTTV;
    private ProductTTV pHSIC;
    private ProductApp pAPP;
    private ProductApp pWIFI;    
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
