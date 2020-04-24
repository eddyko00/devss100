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


    private ArrayList<String>  testListObj;
    private ArrayList<String>  featList;

    /**
     * @return the testListObj
     */
    public ArrayList<String> getTestListObj() {
        return testListObj;
    }

    /**
     * @param testListObj the testListObj to set
     */
    public void setTestListObj(ArrayList<String> testListObj) {
        this.testListObj = testListObj;
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
}
