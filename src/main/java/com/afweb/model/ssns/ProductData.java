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
     * @return the callback
     */
    public ArrayList<String> getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(ArrayList<String> callback) {
        this.callback = callback;
    }

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


    private String postParam = "";
    private ArrayList<String> flow;
    private ArrayList<String> callback;



}
