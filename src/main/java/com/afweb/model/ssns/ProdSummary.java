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
public class ProdSummary {
    private int id;
    private String cusid;
    private String banid;
    private String tiid;
    private String oper;
    private String postParam;
    private String status;    

    /**
     * @return the banid
     */
    public String getBanid() {
        return banid;
    }

    /**
     * @param banid the banid to set
     */
    public void setBanid(String banid) {
        this.banid = banid;
    }

    /**
     * @return the tiid
     */
    public String getTiid() {
        return tiid;
    }

    /**
     * @param tiid the tiid to set
     */
    public void setTiid(String tiid) {
        this.tiid = tiid;
    }

    /**
     * @return the oper
     */
    public String getOper() {
        return oper;
    }

    /**
     * @param oper the oper to set
     */
    public void setOper(String oper) {
        this.oper = oper;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the cusid
     */
    public String getCusid() {
        return cusid;
    }

    /**
     * @param cusid the cusid to set
     */
    public void setCusid(String cusid) {
        this.cusid = cusid;
    }
}
