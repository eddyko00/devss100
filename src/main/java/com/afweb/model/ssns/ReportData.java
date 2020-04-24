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
public class ReportData {

    /**
     * @return the idList
     */
    public ArrayList<String> getIdList() {
        return idList;
    }

    /**
     * @param idList the idList to set
     */
    public void setIdList(ArrayList<String> idList) {
        this.idList = idList;
    }

    /**
     * @return the featList
     */
    public ArrayList<String> getFeatList() {
        return featList;
    }

    /**
     * @param featList the featList to set
     */
    public void setFeatList(ArrayList<String> featList) {
        this.featList = featList;
    }


    private ArrayList<String>  idList;
    private ArrayList<String>  featList;
}
