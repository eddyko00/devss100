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
public class ReportData {

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the idSt
     */
    public String getIdSt() {
        return idSt;
    }

    /**
     * @param idSt the idSt to set
     */
    public void setIdSt(String idSt) {
        this.idSt = idSt;
    }
    private String idSt = "";
    private String name = "";
}
