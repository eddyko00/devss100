/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.process;

import com.afweb.model.*;
import com.afweb.model.ssns.*;
import com.afweb.service.ServiceAFweb;

import com.afweb.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import java.util.Map;

import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 *
 * @author koed
 */
public class SsnsService {

    protected static Logger logger = Logger.getLogger("AccountDB");

    public static String APP_WIFI = "wifi";
    public static String APP_APP = "appointment";
    public static String APP_TTVSUB = "ttvsub";
    public static String APP_TTVREQ = "ttvreq";

    public static String APP_PRODUCT = "product";
    //

    public static String APP_PRODUCT_TYPE_TTV = "TTV";
    public static String APP_PRODUCT_TYPE_HSIC = "HSIC";
    public static String APP_PRODUCT_TYPE_SING = "SING";
    public static String APP_PRODUCT_TYPE_APP = "APP";

    public static String APP_GET_APP = "getAppointment";
    public static String APP_CAN_APP = "cancelAppointment";
    public static String APP_GET_TIMES = "searchTimeSlot";
    public static String APP_UPDATE = "updateAppointment";

    public static String PROD_GET_PROD = "getProductList";
    public static String PROD_GET_BYID = "getProductById";

    public static String WI_1 = "getDeviceStatus";
    public static String WI_2 = "callbackNotification";
    public static String WI_3 = "getDevices";
    public static String WI_4 = "configureDeviceStatus";

    private SsnsDataImp ssnsDataImp = new SsnsDataImp();

    public String getFeatureSsnsAppointment(SsnsData dataObj) {
        ProductData pData = new ProductData();
        if (dataObj == null) {
            return "";
        }
        String appTId = "";
        String banid = "";
        String cust = "";
        String host = "";
        String dataSt = "";
        try {
            String oper = dataObj.getOper();
            if (oper.equals(APP_UPDATE)) { //"updateAppointment")) {
                dataSt = dataObj.getData();
                dataSt = ServiceAFweb.replaceAll("\"", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("[", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("]", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("{", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("}", "", dataSt);
                String[] operList = dataSt.split(",");
                if (operList.length > 3) {
                    appTId = operList[0];
                    banid = operList[1];
                    banid = banid.replace("ban:", "");
                    cust = operList[2];
                    cust = cust.replace("customerId:", "");
                    for (int k = 0; k < operList.length; k++) {
                        String inLine = operList[k];

                        if (inLine.indexOf("hostSystemCd:") != -1) {
                            host = inLine;
                            host = host.replace("hostSystemCd:", "");
                        }
                    }

                }
            } else if (oper.equals(APP_GET_TIMES)) { //"timeslot")) {
                dataSt = dataObj.getData();
                dataSt = ServiceAFweb.replaceAll("\"", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("[", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("]", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("{", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("}", "", dataSt);
                String[] operList = dataSt.split(",");
                if (operList.length > 3) {

                    int custInti = 0;
                    for (int k = 0; k < operList.length; k++) {
                        String inLine = operList[k];
                        if (inLine.indexOf("ban:") != -1) {
                            banid = inLine;
                            banid = host.replace("ban:", "");
                            continue;
                        }
                        if (inLine.indexOf("customerId:") != -1) {

                            cust = inLine;
                            cust = host.replace("customerId:", "");
                            continue;
                        }
                        if (inLine.indexOf("id:") != -1) {
                            if (custInti == 1) {
                                continue;
                            }
                            custInti = 1;
                            appTId = inLine;
                            appTId = appTId.replace("id:", "");
                            continue;
                        }
                        if (inLine.indexOf("hostSystemCd:") != -1) {
                            host = inLine;
                            host = host.replace("hostSystemCd:", "");
                            continue;
                        }
                    }

                }
            } else if (oper.equals(APP_GET_APP)) { //"getAppointment")) {
                dataSt = dataObj.getData();
                dataSt = ServiceAFweb.replaceAll("\"", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("[", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("]", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("{", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("}", "", dataSt);
                String[] operList = dataSt.split(",");
                if (operList.length > 3) {
                    banid = operList[0];
                    cust = operList[1];
                }
            } else if (oper.equals(APP_CAN_APP)) {//"cancelAppointment")) {
                dataSt = dataObj.getData();
                dataSt = ServiceAFweb.replaceAll("\"", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("[", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("]", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("{", "", dataSt);
                dataSt = ServiceAFweb.replaceAll("}", "", dataSt);
                String[] operList = dataSt.split(",");
                if (operList.length > 3) {
                    appTId = operList[0];
                    banid = operList[1];
                    if (banid.equals("null")) {
                        banid = "";
                    }
                    cust = operList[2];
                    if (cust.equals("null")) {
                        cust = "";
                    }
                    host = operList[3];

                }

            } else {
                logger.info("> getFeatureSsnsAppointment Other oper " + oper);
            }
            if (oper.equals(APP_GET_APP)) {
                ;
            } else {
                if (appTId.equals("")) {
                    return "";
                }
            }
            logger.info(dataSt);

/////////////
            //call devop to get customer id
            if ((banid.length() == 0) && (cust.length() == 0)) {
                if (host.equals("FIFA") || host.equals("LYNX")) {
                    // not that useful
//                    String custid = getCustIdAppointmentDevop(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
//                    if (custid.length() != 0) {
//                        cust = custid;
//                        dataObj.setCusid(custid);
//                    }
                }
            }
            SsnsAcc NAccObj = new SsnsAcc();
            NAccObj.setDown("splunkflow");
            boolean stat = this.updateSsnsAppointment(oper, appTId, banid, cust, host, pData, dataObj, NAccObj);
            if (stat == true) {
                ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NAccObj.getName(), NAccObj.getUid());
                boolean exist = false;
                if (ssnsAccObjList != null) {
                    if (ssnsAccObjList.size() != 0) {
                        SsnsAcc ssnsObj = ssnsAccObjList.get(0);
                        if (ssnsObj.getDown().equals("splunkflow")) {
                            exist = true;
                        }
                    }
                }
                if (exist == false) {
                    int ret = getSsnsDataImp().insertSsnsAccObject(NAccObj);
                }

            }

            if (stat == true) {
                getSsnsDataImp().updatSsnsDataStatusById(dataObj.getId(), ConstantKey.COMPLETED);
            } else {
                if (oper.equals(APP_GET_APP)) {
                    getSsnsDataImp().updatSsnsDataStatusById(dataObj.getId(), ConstantKey.COMPLETED);
                }
            }

        } catch (Exception ex) {
            logger.info("> getFeatureSsnsAppointment Exception" + ex.getMessage());
        }
        return "";
    }

    public boolean updateSsnsAppointment(String oper, String appTId, String banid, String cust, String host, ProductData pData, SsnsData dataObj, SsnsAcc NAccObj) {
        try {
            String featTTV = "";

            if (oper.equals(APP_UPDATE) || oper.equals(APP_GET_APP) || oper.equals(APP_GET_TIMES)) {
                if ((banid.length() == 0) && (cust.length() == 0)) {
                    featTTV = APP_PRODUCT_TYPE_APP;
                    featTTV += ":" + oper;
                    featTTV += ":" + host;
                    featTTV += ":ContactEng";
                } else {
                    String outputSt = null;
                    if (oper.equals(APP_GET_TIMES)) {  //"searchTimeSlot";
//                        not sure why it does not work so just call get appointment to get the feature
                        outputSt = SsnsAppointment(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
                    } else {
                        outputSt = SsnsAppointment(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
                    }
                    if (outputSt == null) {
                        return false;
                    }
                    if (outputSt.length() < 80) {  // or test "appointmentList":[]
                        return false;
                    }
                    ProductApp prodTTV = parseAppointmentFeature(outputSt, oper);
                    pData.setpAPP(prodTTV);
                    featTTV = prodTTV.getFeat();
                }
            } else if (oper.equals(APP_CAN_APP)) {   //"cancelAppointment";
                featTTV = APP_PRODUCT_TYPE_APP;
                featTTV += ":" + oper;
                featTTV += ":" + host;
                if ((banid.length() == 0) && (cust.length() == 0)) {
                    featTTV += ":ContactEng";
                }
            } else {
                return false;
            }

            logger.info("> updateSsnsAppointment featTTV " + featTTV);
/////////////TTV   
            if (NAccObj.getDown().equals("splunkflow")) {

                ArrayList<String> flow = new ArrayList();
                int faulure = getSsnsFlowTrace(dataObj, flow);
                if (flow == null) {
                    logger.info("> updateSsnsAppointment skip no flow");
                    return false;
                }
                pData.setFlow(flow);

                if (faulure == 1) {
                    featTTV += ":failed";
                }
            }

            NAccObj.setName(featTTV);
            NAccObj.setBanid(banid);
            NAccObj.setCusid(cust);
            NAccObj.setTiid(appTId);
            NAccObj.setUid(dataObj.getUid());
            NAccObj.setApp(dataObj.getApp());
            NAccObj.setOper(oper);

//          NAccObj.setDown(""); // set by NAccObj
            NAccObj.setRet(host);
            NAccObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NAccObj.setData(nameSt);

            NAccObj.setUpdatedatel(dataObj.getUpdatedatel());
            NAccObj.setUpdatedatedisplay(new java.sql.Date(dataObj.getUpdatedatel()));

            return true;
        } catch (Exception ex) {
            logger.info("> updateSsnsAppointment Exception" + ex.getMessage());
        }
        return false;
    }

    public static ProductApp parseAppointmentFeature(String outputSt, String oper) {

        if (outputSt == null) {
            return null;
        }
        ProductApp prodTTV = new ProductApp();
        int catCdInit = 0;
        int statInit = 0;
        int catInit = 0;
        int hostInit = 0;

        ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);
        for (int j = 0; j < outputList.size(); j++) {
            String inLine = outputList.get(j);
//            logger.info("" + inLine);

            if (inLine.indexOf("category") != -1) {
                if (catInit == 1) {
                    continue;
                }
                catInit = 1;
                String valueSt = inLine;
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("category:", "", valueSt);
                valueSt = ServiceAFweb.replaceAll(",", "", valueSt);
                prodTTV.setCategory(valueSt);

                continue;
            }
            if (inLine.indexOf("statusCd") != -1) {
                if (catInit == 0) {
                    continue;
                }
                if (statInit == 1) {
                    continue;
                }

                statInit = 1;
                String valueSt = inLine;
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("statusCd:", "", valueSt);
                valueSt = ServiceAFweb.replaceAll(",", "", valueSt);
                prodTTV.setStatusCd(valueSt);
                continue;
            }
            if (inLine.indexOf("productCategoryCd") != -1) {

                if (catCdInit == 1) {
                    continue;
                }
                catCdInit = 1;
                String valueSt = inLine;
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("productCategoryCd:", "", valueSt);
                valueSt = ServiceAFweb.replaceAll(",", "", valueSt);
                prodTTV.setCategoryCd(valueSt);
                continue;
            }
            if (inLine.indexOf("hostSystemCd") != -1) {

                if (hostInit == 1) {
                    continue;
                }
                hostInit = 1;
                String valueSt = inLine;
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("hostSystemCd:", "", valueSt);
                valueSt = ServiceAFweb.replaceAll(",", "", valueSt);
                prodTTV.setHost(valueSt);
                continue;
            }
        }

        String featTTV = APP_PRODUCT_TYPE_APP;
        featTTV += ":" + oper;
        featTTV += ":" + prodTTV.getHost();
        featTTV += ":" + prodTTV.getCategory();
        featTTV += ":" + prodTTV.getStatusCd();
        featTTV += ":" + prodTTV.getCategoryCd();

        prodTTV.setFeat(featTTV);

        return prodTTV;
    }

    public String getCustIdAppointmentDevop(String ProductURL, String appTId, String banid, String cust, String host) {

        String url = "http://localhost:8080/v2/cmo/selfmgmt/appointmentmanagement/devop/searchtimeslot";
        HashMap newbodymap = new HashMap();
        newbodymap.put("customerId", cust);
        newbodymap.put("id", appTId);
        newbodymap.put("hostSystemCd", host);
        try {
            String custid = "";
            String output = this.sendRequest_Ssns(METHOD_POST, url, null, newbodymap);

            if (output == null) {
                return "";
            }
            ArrayList arrayItem = new ObjectMapper().readValue(output, ArrayList.class);
            if (arrayItem.size() < 1) {
                return "";
            }
            output = (String) arrayItem.get(1);
            output = ServiceAFweb.replaceAll("\"", "", output);
            output = ServiceAFweb.replaceAll("\\", "", output);
            String[] oList = output.split(",");
            for (int i = 0; i < oList.length; i++) {
                String line = oList[i];
                if (line.indexOf("customerId:") != -1) {
                    custid = ServiceAFweb.replaceAll("customerId:", "", line);
                    if (custid.equals("null")) {
                        return "";
                    }
                    return custid;
                }
            }

            return "";
        } catch (Exception ex) {
            logger.info("> SsnsAppointment exception " + ex.getMessage());
        }
        return null;
    }

    public String SsnsTimeslot(String ProductURL, String appTId, String banid, String cust, String host) {

//        String url = ProductURL + "/v2/cmo/selfmgmt/appointmentmanagement/searchtimeslot";
        String url = "http://localhost:8080/v2/cmo/selfmgmt/appointmentmanagement/searchtimeslot";

        HashMap newbodymap = new HashMap();
        newbodymap.put("customerId", cust);
        newbodymap.put("id", appTId);
        newbodymap.put("hostSystemCd", host);
        try {
            String output = this.sendRequest_Ssns(METHOD_POST, url, null, newbodymap);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsAppointment exception " + ex.getMessage());
        }
        return null;
    }

    public String getFeatureSsnsProdTestingApp(SsnsAcc dataObj, ArrayList<String> outputList, String Oper) {
        if (dataObj == null) {
            return "";
        }

        dataObj.getData();
        String banid = dataObj.getBanid();
        String appTId = dataObj.getTiid();
        String cust = dataObj.getCusid();
        String host = dataObj.getRet();
        String outputSt = null;
        if (Oper == APP_GET_APP) {
            outputSt = SsnsAppointment(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
            ArrayList<String> outList = ServiceAFweb.prettyPrintJSON(outputSt);
            outputList.addAll(outList);
            ProductTTV prodTTV = parseProductTtvFeature(outputSt, dataObj.getOper());

            return prodTTV.getFeatTTV();
        } else if (Oper == APP_GET_TIMES) {
            outputSt = SsnsTimeslot(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
            ArrayList<String> outList = ServiceAFweb.prettyPrintJSON(outputSt);
            outputList.addAll(outList);
            String feat = dataObj.getName();
            int NumofStart = 0;
            for (int i = 0; i < outList.size(); i++) {
                String inLine = outList.get(i);
                if (inLine.indexOf("startDate") != -1) {
                    NumofStart++;
                }
            }
            feat += ":startdate:" + NumofStart;
            String featList[] = feat.split(":");
            String newFeat = "";
            for (int i = 0; i < featList.length; i++) {
                String line = featList[i];
                if (i == 0) {
                    newFeat += line;
                    continue;
                }
                if (i == 1) {
                    newFeat += ":" + Oper;
                    continue;
                }
                newFeat += ":" + line;
            }
            return newFeat;
        }
        if (outputSt == null) {
            return "";
        }

        return "";
    }

    public String SsnsAppointment(String ProductURL, String appTId, String banid, String cust, String host) {
        if (host.length() > 0) {
            host = host.replace("9", ""); // remove OMS9
        }
        String url = ProductURL + "/v2/cmo/selfmgmt/appointmentmanagement/appointment?customerid=" + cust;
        if (banid.length() > 0) {
            url = ProductURL + "/v2/cmo/selfmgmt/appointmentmanagement/appointment?ban=" + banid + "&customerid=" + cust;
            if (host.length() > 0) {
                url += "&appointmentlist.hostsystemcd.in=" + host;
            }
        }
        try {
            String output = this.sendRequest_Ssns(METHOD_GET, url, null, null);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsAppointment exception " + ex.getMessage());
        }
        return null;
    }

    public String getFeatureSsnsProdTestingProdttv(SsnsAcc dataObj, ArrayList<String> outputList, String oper) {
        if (dataObj == null) {
            return "";
        }

        String banid = dataObj.getBanid();
        String prodid = dataObj.getTiid();
        String outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, prodid, oper);
        if (outputSt == null) {
            return "";
        }
        ArrayList<String> outList = ServiceAFweb.prettyPrintJSON(outputSt);
        outputList.addAll(outList);
        ProductTTV prodTTV = parseProductTtvFeature(outputSt, dataObj.getOper());
        return prodTTV.getFeatTTV();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////    

    public String getFeatureSsnsProdiuctInventory(SsnsData dataObj) {

        ProductData pData = new ProductData();
        if (dataObj == null) {
            return "";
        }
        String prodid = "";
        String banid = "";
        try {

            String oper = dataObj.getOper();
            String daSt = dataObj.getData();
            //["xxx","xxx","product.characteristic.channelInfoList",null,null,null]
            daSt = ServiceAFweb.replaceAll("[", "", daSt);
            daSt = ServiceAFweb.replaceAll("]", "", daSt);
            daSt = ServiceAFweb.replaceAll("\"", "", daSt);
            String[] daList = daSt.split(",");
            if (daList.length < 3) {
                return "";
            }
            if (oper.equals(PROD_GET_BYID)) {
                prodid = daList[0];
                banid = daList[1];
            } else if (oper.equals(PROD_GET_PROD)) {
                banid = daList[0];
            } else {
                logger.info("> getFeatureSsnsProdiuctInventory Other oper " + oper);
            }
            if (banid.equals("null")) {
                return "";
            }
            logger.info(daSt);

/////////////
            if (oper.equals(PROD_GET_BYID)) {

                String outputSt = SsnsProdiuctInventoryByProdId(ServiceAFweb.URL_PRODUCT, banid, prodid);
                if (outputSt == null) {
                    return "";
                }

                ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);
                String valueSt = "";
                for (int j = 0; j < outputList.size(); j++) {
                    String inLine = outputList.get(outputList.size() - 1 - j);
                    if (inLine.indexOf("productType") != -1) {
                        valueSt = inLine;
                        valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                        valueSt = ServiceAFweb.replaceAll("productType:", "", valueSt);
                        break;
                    }
                }
// 
                if (valueSt.length() == 0) {
                    return "";
                }
                String PIoper = valueSt;

                SsnsAcc NAccObj = new SsnsAcc();
                NAccObj.setTiid(prodid);
                NAccObj.setRet(PIoper);
                NAccObj.setDown("splunkflow");

                boolean stat = this.updateSsnsProdiuctInventoryByProdId(PIoper, banid, prodid, pData, dataObj, NAccObj);
                if (stat == true) {
                    ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NAccObj.getName(), NAccObj.getUid());
                    boolean exist = false;
                    if (ssnsAccObjList != null) {
                        if (ssnsAccObjList.size() != 0) {
                            SsnsAcc ssnsObj = ssnsAccObjList.get(0);
                            if (ssnsObj.getDown().equals("splunkflow")) {
                                exist = true;
                            }
                        }
                    }
                    if (exist == false) {
                        int ret = getSsnsDataImp().insertSsnsAccObject(NAccObj);
                    }
                }
            }
//            
            if (oper.equals(PROD_GET_PROD)) {
                SsnsAcc NAccObj = new SsnsAcc();
                NAccObj.setTiid(prodid);
                NAccObj.setRet(APP_PRODUCT_TYPE_SING);
                NAccObj.setDown("splunkflow");
                String PIoper = APP_PRODUCT_TYPE_SING;
                boolean stat = this.updateSsnsProdiuctInventory(PIoper, banid, prodid, pData, dataObj, NAccObj);
                if (stat == true) {
                    ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NAccObj.getName(), NAccObj.getUid());
                    boolean exist = false;
                    if (ssnsAccObjList != null) {
                        if (ssnsAccObjList.size() != 0) {
                            SsnsAcc ssnsObj = ssnsAccObjList.get(0);
                            if (ssnsObj.getDown().equals("splunkflow")) {
                                exist = true;
                            }
                        }
                    }
                    if (exist == false) {
                        int ret = getSsnsDataImp().insertSsnsAccObject(NAccObj);
                    }
                }
                NAccObj = new SsnsAcc();
                NAccObj.setTiid(prodid);
                NAccObj.setRet(APP_PRODUCT_TYPE_HSIC);
                NAccObj.setDown("splunkflow");
                PIoper = APP_PRODUCT_TYPE_HSIC;
                stat = this.updateSsnsProdiuctInventory(PIoper, banid, prodid, pData, dataObj, NAccObj);
                if (stat == true) {
                    ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NAccObj.getName(), NAccObj.getUid());
                    boolean exist = false;
                    if (ssnsAccObjList != null) {
                        if (ssnsAccObjList.size() != 0) {
                            SsnsAcc ssnsObj = ssnsAccObjList.get(0);
                            if (ssnsObj.getDown().equals("splunkflow")) {
                                exist = true;
                            }
                        }
                    }
                    if (exist == false) {
                        int ret = getSsnsDataImp().insertSsnsAccObject(NAccObj);
                    }
                }

                NAccObj = new SsnsAcc();
                NAccObj.setTiid(prodid);
                NAccObj.setRet(APP_PRODUCT_TYPE_TTV);
                NAccObj.setDown("splunkflow");
                PIoper = APP_PRODUCT_TYPE_TTV;
                stat = this.updateSsnsProdiuctInventory(PIoper, banid, prodid, pData, dataObj, NAccObj);
                if (stat == true) {
                    ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NAccObj.getName(), NAccObj.getUid());
                    boolean exist = false;
                    if (ssnsAccObjList != null) {
                        if (ssnsAccObjList.size() != 0) {
                            SsnsAcc ssnsObj = ssnsAccObjList.get(0);
                            if (ssnsObj.getDown().equals("splunkflow")) {
                                exist = true;
                            }
                        }
                    }
                    if (exist == false) {
                        int ret = getSsnsDataImp().insertSsnsAccObject(NAccObj);
                    }
                }
            }
            //update to complete so will not process again
            getSsnsDataImp().updatSsnsDataStatusById(dataObj.getId(), ConstantKey.COMPLETED);

        } catch (Exception ex) {
            logger.info("> getFeatureSsnsProdiuctInventory Exception" + ex.getMessage());
        }
        return "";
    }

    public static ProductTTV parseProductPhoneFeature(String outputSt, String oper) {
        if (outputSt == null) {
            return null;
        }
        try {
            ProductTTV prodTTV = new ProductTTV();
            int quotaAmtInit = 0;
            int fifaInit = 0;
            int planInit = 0;
            int vmInit = 0;

            ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);

            for (int j = 0; j < outputList.size(); j++) {
                String inLine = outputList.get(outputList.size() - 1 - j);
//            logger.info("" + inLine);
                //"name":"isFIFA",
                if (inLine.indexOf("isFIFA") != -1) {
                    if (fifaInit == 1) {
                        continue;
                    }
                    fifaInit = 1;
                    String valueSt = outputList.get(j - 1);
                    if (valueSt.indexOf("false") != -1) {
                        prodTTV.setIsFIFA(0);
                    }
                    if (valueSt.indexOf("true") != -1) {
                        prodTTV.setIsFIFA(1);
                    }
                    continue;
                }

                if (inLine.indexOf("hasVoicemail") != -1) {
                    if (vmInit == 1) {
                        continue;
                    }
                    vmInit = 1;
                    String valueSt = outputList.get(j - 1);
                    if (valueSt.indexOf("false") != -1) {
                        prodTTV.setVoicemail(0);
                    }
                    if (valueSt.indexOf("true") != -1) {
                        prodTTV.setVoicemail(1);
                    }
                    continue;
                }

                if (inLine.indexOf("CallControl") != -1) {

                    if (quotaAmtInit == 1) {
                        continue;
                    }
                    quotaAmtInit = 1;
                    boolean exit = false;
                    for (int k = j; k <= outputList.size(); k++) {
                        String inL = outputList.get(outputList.size() - 1 - k);
                        if (inL.indexOf("productNm") != -1) {
                            String valueSt = outputList.get(outputList.size() - 1 - k + 1);
                            valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                            valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                            valueSt = ServiceAFweb.replaceAll(" ", "_", valueSt);
                            prodTTV.setCallControl(valueSt);
                            exit = true;
                            break;
                        }
                        if (exit == true) {
                            break;
                        }
                    }
                    continue;
                }

                if (inLine.indexOf("HomePhoneBundle") != -1) {

                    if (planInit == 1) {
                        continue;
                    }
                    planInit = 1;
                    boolean exit = false;
                    for (int k = j; k <= outputList.size(); k++) {
                        String inL = outputList.get(outputList.size() - 1 - k);
                        if (inL.indexOf("productRelationship") != -1) {
                            for (int m = k; m <= outputList.size(); m++) {
                                String inLL = outputList.get(outputList.size() - 1 - m);
                                if (inLL.indexOf("productNm") != -1) {
                                    String valueSt = outputList.get(outputList.size() - 1 - m + 1);
                                    valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                                    valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                                    valueSt = ServiceAFweb.replaceAll(" ", "_", valueSt);
                                    prodTTV.setPrimaryPricePlan(valueSt);
                                    exit = true;
                                    break;
                                }
                                if (exit == true) {
                                    break;
                                }
                            }
                            if (exit == true) {
                                break;
                            }
                        }
                        if (exit == true) {
                            break;
                        }
                    }
                    continue;
                }
            }
            String featTTV = APP_PRODUCT_TYPE_SING;
            featTTV += ":" + oper;
            String fifa = "fifa";
            if (prodTTV.getIsFIFA() == 0) {
                fifa = "comp";
            }
            featTTV += ":" + fifa;
            String vm = "voicemail";
            if (prodTTV.getIsFIFA() == 0) {
                vm = "noVM";
            }
            featTTV += ":" + vm;
            featTTV += ":" + prodTTV.getPrimaryPricePlan();
            featTTV += ":" + prodTTV.getCallControl();
            prodTTV.setFeatTTV(featTTV);

            return prodTTV;
        } catch (Exception ex) {

        }
        return null;
    }

    public static ProductTTV parseProductInternetFeature(String outputSt, String oper) {

        if (outputSt == null) {
            return null;
        }
        try {
            ProductTTV prodTTV = new ProductTTV();
            int quotaAmtInit = 0;
            int fifaInit = 0;
            int planInit = 0;
            int fifaFlag = 0;

            ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);

            for (int j = 0; j < outputList.size(); j++) {
                String inLine = outputList.get(j);
//                        logger.info("" + inLine);
                //"name":"isFIFA",
                if (inLine.indexOf("isFIFA") != -1) {
                    if (fifaInit == 1) {
                        continue;
                    }
                    fifaInit = 1;
                    String valueSt = outputList.get(j + 1);
                    if (valueSt.indexOf("false") != -1) {
                        prodTTV.setIsFIFA(0);
                        fifaFlag = 0;
                    }
                    if (valueSt.indexOf("true") != -1) {
                        prodTTV.setIsFIFA(1);
                        fifaFlag = 1;
                    }
                    break;
                }
            }

            for (int j = 0; j < outputList.size(); j++) {
                String inLine = outputList.get(outputList.size() - 1 - j);
//            logger.info("" + inLine);
                //"name":"isFIFA",
                if (inLine.indexOf("isFIFA") != -1) {
                    if (fifaInit == 1) {
                        continue;
                    }
                    fifaInit = 1;
                    String valueSt = outputList.get(j - 1);
                    if (valueSt.indexOf("false") != -1) {
                        prodTTV.setIsFIFA(0);
                    }
                    if (valueSt.indexOf("true") != -1) {
                        prodTTV.setIsFIFA(1);
                    }
                    continue;
                }
                if (inLine.indexOf("SecurityBundle") != -1) {

                    if (quotaAmtInit == 1) {
                        continue;
                    }
                    quotaAmtInit = 1;
                    boolean exit = false;
                    for (int k = j; k <= outputList.size(); k++) {
                        String inL = outputList.get(outputList.size() - 1 - k);
                        if (inL.indexOf("productRelationship") != -1) {
                            for (int m = k; m <= outputList.size(); m++) {
                                String inLL = outputList.get(outputList.size() - 1 - m);
                                if (inLL.indexOf("productNm") != -1) {
                                    String valueSt = outputList.get(outputList.size() - 1 - m + 1);
                                    valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                                    valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                                    valueSt = ServiceAFweb.replaceAll(" ", "_", valueSt);
                                    prodTTV.setSecurityBundle(valueSt);
                                    exit = true;
                                    break;
                                }
                                if (exit == true) {
                                    break;
                                }
                            }
                            if (exit == true) {
                                break;
                            }
                        }
                        if (exit == true) {
                            break;
                        }
                    }

                    continue;
                }

                if (inLine.indexOf("PrimaryPricePlan") != -1) {

                    if (planInit == 1) {
                        continue;
                    }
                    planInit = 1;
                    if (fifaFlag == 0) {
                        boolean exit = false;
                        for (int k = j; k <= outputList.size(); k++) {
                            String inL = outputList.get(outputList.size() - 1 - k);
                            if (inL.indexOf("productRelationship") != -1) {
                                for (int m = k; m <= outputList.size(); m++) {
                                    String inLL = outputList.get(outputList.size() - 1 - m);
                                    if (inLL.indexOf("productNm") != -1) {
                                        String valueSt = outputList.get(outputList.size() - 1 - m + 1);
                                        valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                                        valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                                        valueSt = ServiceAFweb.replaceAll(" ", "_", valueSt);
                                        prodTTV.setPrimaryPricePlan(valueSt);
                                        exit = true;
                                        break;
                                    }
                                    if (exit == true) {
                                        break;
                                    }
                                }
                                if (exit == true) {
                                    break;
                                }
                            }
                            if (exit == true) {
                                break;
                            }
                        }
                    } else if (fifaFlag == 1) {
                        boolean exit = false;
                        for (int k = j; k <= outputList.size(); k++) {
                            String inL = outputList.get(outputList.size() - 1 - k);
                            if (inL.indexOf("productNm") != -1) {
                                String valueSt = outputList.get(outputList.size() - 1 - k + 1);
                                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                                valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                                valueSt = ServiceAFweb.replaceAll(" ", "_", valueSt);
                                prodTTV.setPrimaryPricePlan(valueSt);
                                exit = true;
                                break;

                            }
                            if (exit == true) {
                                break;
                            }
                        }
                    }
                    continue;
                }
                if (inLine.indexOf("EmailFeatures") != -1) {
                    prodTTV.setEmailFeatures("EmailFeatures");
                    continue;
                }
                if (inLine.indexOf("UnlimitedUsage") != -1) {
                    prodTTV.setUnlimitedUsage("UnlimitedUsage");
                    continue;
                }
            }
            String featTTV = APP_PRODUCT_TYPE_HSIC;
            featTTV += ":" + oper;
            String fifa = "fifa";
            if (prodTTV.getIsFIFA() == 0) {
                fifa = "comp";
            }
            featTTV += ":" + fifa;
            featTTV += ":" + prodTTV.getPrimaryPricePlan();
            featTTV += ":" + prodTTV.getSecurityBundle();
            featTTV += ":" + "email";
            if (prodTTV.getEmailFeatures().length() > 0) {
                featTTV += ":" + "Y";
            } else {
                featTTV += ":" + "N";
            }
            featTTV += ":" + "UnlimitedUsage";
            if (prodTTV.getUnlimitedUsage().length() > 0) {
                featTTV += ":" + "Y";
            } else {
                featTTV += ":" + "N";
            }
            prodTTV.setFeatTTV(featTTV);

            return prodTTV;
        } catch (Exception ex) {

        }
        return null;
    }

    public boolean updateSsnsProdiuctInventoryByProdId(String oper, String banid, String prodid, ProductData pData, SsnsData dataObj, SsnsAcc NAccObj) {
        try {

            String featTTV = "";
            String outputSt = null;

            outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, prodid, oper);
            if (outputSt == null) {
                return false;
            }

            if (oper.equals(APP_PRODUCT_TYPE_HSIC)) {
                ProductTTV prodHSIC = parseProductInternetFeature(outputSt, dataObj.getOper());
                pData.setpHSIC(prodHSIC);
                featTTV = prodHSIC.getFeatTTV();
            } else if (oper.equals(APP_PRODUCT_TYPE_TTV)) {
                ProductTTV prodTTV = parseProductTtvFeature(outputSt, dataObj.getOper());
                pData.setpTTV(prodTTV);
                featTTV = prodTTV.getFeatTTV();
            } else if (oper.equals(APP_PRODUCT_TYPE_SING)) {
                ProductTTV prodSING = parseProductPhoneFeature(outputSt, dataObj.getOper());
                pData.setpSING(prodSING);
                featTTV = prodSING.getFeatTTV();
            }

            logger.info("> updateSsnsProdiuctInventory feat " + featTTV);
/////////////TTV   
            ArrayList<String> flow = new ArrayList();
            int faulure = getSsnsFlowTrace(dataObj, flow);
            if (flow == null) {
                logger.info("> updateSsnsProdiuctInventory skip no flow");
                return false;
            }

            pData.setFlow(flow);
            pData.setFlow(flow);
            if (faulure == 1) {
                featTTV += ":failed";
            }
            NAccObj.setName(featTTV);
            NAccObj.setBanid(banid);
            NAccObj.setCusid(dataObj.getCusid());
            NAccObj.setUid(dataObj.getUid());
            NAccObj.setApp(dataObj.getApp());
            NAccObj.setTiid(dataObj.getTiid());
            NAccObj.setOper(dataObj.getOper());

            NAccObj.setDown(NAccObj.getDown());
            NAccObj.setRet(NAccObj.getRet());

            NAccObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NAccObj.setData(nameSt);

            NAccObj.setUpdatedatel(dataObj.getUpdatedatel());
            NAccObj.setUpdatedatedisplay(new java.sql.Date(dataObj.getUpdatedatel()));

            return true;
        } catch (Exception ex) {
            logger.info("> updateSsnsProdiuctInventory Exception" + ex.getMessage());
        }
        return false;
    }

    public boolean updateSsnsProdiuctInventory(String oper, String banid, String prodid, ProductData pData, SsnsData dataObj, SsnsAcc NAccObj) {
        try {

            String featTTV = "";
            String outputSt = null;

            outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, prodid, oper);
            if (outputSt == null) {
                return false;
            }
            ProductTTV prodTTV = null;
            if (oper.equals(SsnsService.APP_PRODUCT_TYPE_HSIC)) {
                prodTTV = parseProductInternetFeature(outputSt, dataObj.getOper());
                pData.setpHSIC(prodTTV);
            } else if (oper.equals(SsnsService.APP_PRODUCT_TYPE_TTV)) {
                prodTTV = parseProductTtvFeature(outputSt, dataObj.getOper());
                pData.setpTTV(prodTTV);
            } else if (oper.equals(SsnsService.APP_PRODUCT_TYPE_SING)) {
                prodTTV = parseProductPhoneFeature(outputSt, dataObj.getOper());
                pData.setpSING(prodTTV);
            }

            if (prodTTV == null) {
                return false;
            }

            featTTV = prodTTV.getFeatTTV();
            logger.info("> updateSsnsProdiuctInventory feat " + featTTV);
/////////////TTV  
            ArrayList<String> flow = new ArrayList();
            int faulure = getSsnsFlowTrace(dataObj, flow);
            if (flow == null) {
                logger.info("> updateSsnsProdiuctInventory skip no flow");
                return false;
            }

            pData.setFlow(flow);
            String feat = prodTTV.getFeatTTV();
            if (faulure == 1) {
                feat += ":failed";
            }
            NAccObj.setName(feat);
            NAccObj.setBanid(banid);
            NAccObj.setCusid(dataObj.getCusid());
            NAccObj.setUid(dataObj.getUid());
            NAccObj.setApp(dataObj.getApp());
            NAccObj.setTiid(dataObj.getTiid());
            NAccObj.setOper(dataObj.getOper());

            NAccObj.setDown(NAccObj.getDown());
            NAccObj.setRet(NAccObj.getRet());

            NAccObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NAccObj.setData(nameSt);

            NAccObj.setUpdatedatel(dataObj.getUpdatedatel());
            NAccObj.setUpdatedatedisplay(new java.sql.Date(dataObj.getUpdatedatel()));

            return true;
        } catch (Exception ex) {
            logger.info("> updateSsnsProdiuctInventory Exception" + ex.getMessage());
        }
        return false;
    }

    public static ProductTTV parseProductTtvFeature(String outputSt, String oper) {

        if (outputSt == null) {
            return null;
        }
        ProductTTV prodTTV = new ProductTTV();
        int productCdInit = 0;
        int ChannelListInit = 0;
        int offerInit = 0;
        int fifaInit = 0;
        int regionInit = 0;
        prodTTV.setProductCd("Essentials");
        prodTTV.setRegion("NoRegion");

        ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);
        for (int j = 0; j < outputList.size(); j++) {
            String inLine = outputList.get(j);
//                        logger.info("" + inLine);
            //"name":"isFIFA",
            if (inLine.indexOf("isFIFA") != -1) {
                if (fifaInit == 1) {
                    continue;
                }
                fifaInit = 1;
                String valueSt = outputList.get(j + 1);
                if (valueSt.indexOf("false") != -1) {
                    prodTTV.setIsFIFA(0);
                }
                if (valueSt.indexOf("true") != -1) {
                    prodTTV.setIsFIFA(1);
                }
                continue;
            }
            if (inLine.indexOf("offer") != -1) {
                if (offerInit == 1) {
                    continue;
                }
                offerInit = 1;
                String valueSt = outputList.get(j + 1);
                if (valueSt.indexOf("MediaroomTV-HS2.0") != -1) {
                    prodTTV.setOffer("Mediaroom20");
                    continue;
                }
                if (valueSt.indexOf("MediaroomTV-HS") != -1) {
                    prodTTV.setOffer("Mediaroom");
                    continue;
                }
                if (valueSt.indexOf("TVX") != -1) {
                    prodTTV.setOffer("TVX");
                }
                continue;
            }

            if (inLine.indexOf("region") != -1) {
                if (regionInit == 1) {
                    continue;
                }
                regionInit = 1;
                String valueSt = outputList.get(j + 1);
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                prodTTV.setRegion(valueSt);
                continue;
            }
            if (inLine.indexOf("productCd") != -1) {
                if (productCdInit == 1) {
                    continue;
                }
                productCdInit = 1;
                String valueSt = outputList.get(j + 1);
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                prodTTV.setProductCd(valueSt);
                continue;
            }
            if (inLine.indexOf("ChannelList") != -1) {
                if (ChannelListInit == 1) {
                    continue;
                }
                ChannelListInit = 1;
                String valueSt = outputList.get(j + 3);
                if (valueSt.indexOf("channelId") != -1) {
                    prodTTV.setChannelList(1);
                } else {
                    prodTTV.setChannelList(1);
                }
                continue;
            }
        }

        String featTTV = APP_PRODUCT_TYPE_TTV;
        featTTV += ":" + oper;
        String fifa = "fifa";
        if (prodTTV.getIsFIFA() == 0) {
            fifa = "comp";
        }
        featTTV += ":" + fifa;
        featTTV += ":" + prodTTV.getRegion();
        featTTV += ":" + prodTTV.getOffer();
        featTTV += ":" + prodTTV.getProductCd();

        String chann = "chlist:N";
        if (prodTTV.getChannelList() == 1) {
            chann = "chlist:Y";
        }
        featTTV += ":" + chann;
        prodTTV.setFeatTTV(featTTV);

        return prodTTV;
    }

    public String SsnsProdiuctInventoryByProdId(String ProductURL, String ban, String prodid) {
        String url = ProductURL + "/v1/cmo/selfmgmt/productinventory/product/" + prodid + "?billingAccount.id=" + ban;
        try {
            String output = this.sendRequest_Ssns(METHOD_GET, url, null, null);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsProdiuctInventory exception " + ex.getMessage());
        }
        return null;
    }

    public String SsnsProdiuctInventory(String ProductURL, String ban, String prodid, String productType) {
        String url = "";
        if (prodid.length() == 0) {
            url = ProductURL + "/v1/cmo/selfmgmt/productinventory/product?billingAccount.id=" + ban
                    + "&productType=" + productType;
            if (productType.equals(APP_PRODUCT_TYPE_TTV)) {
                url += "&fields=product.characteristic.channelInfoList";
            } else if (productType.equals(APP_PRODUCT_TYPE_SING)) {
                url += "&fields=product.characteristic.voicemail";
            }
        } else {
            url = ProductURL + "/v1/cmo/selfmgmt/productinventory/product/" + prodid + "?billingAccount.id=" + ban;
            if (productType.equals(APP_PRODUCT_TYPE_TTV)) {
                url += "&fields=product.characteristic.channelInfoList";
            } else if (productType.equals(APP_PRODUCT_TYPE_SING)) {
                url += "&fields=product.characteristic.voicemail";
            }
        }
        try {
            String output = this.sendRequest_Ssns(METHOD_GET, url, null, null);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsProdiuctInventory exception " + ex.getMessage());
        }
        return null;
    }

    public int getSsnsFlowTrace(SsnsData dataObj, ArrayList<String> flow) {

        String uid = dataObj.getUid();
        int failure = 0;

        ArrayList<SsnsData> ssnsList = getSsnsDataImp().getSsnsDataObjListByUid(dataObj.getApp(), uid);
        if (ssnsList != null) {
//            logger.info("> ssnsList " + ssnsList.size());
            for (int i = 0; i < ssnsList.size(); i++) {
                SsnsData data = ssnsList.get(i);
                String flowSt = data.getDown();
                if (flowSt.length() == 0) {
                    flowSt = data.getOper();
                }
                flowSt += ":" + data.getExec();
                String dataTxt = data.getData();
                if (dataTxt.indexOf("stacktrace") != -1) {
                    failure = 1;
                }
                String http = data.getRet();
                if (http.indexOf("httpCd=500") != -1) {
                    failure = 1;
                }
//                logger.info("> flow " + flowSt);
                flow.add(flowSt);
            }
        }
        return failure;
    }

    /////////////////////////////////////////////////////////////
    // operations names constants
    private static final String METHOD_POST = "post";
    private static final String METHOD_GET = "get";

    private String sendRequest_Ssns(String method, String subResourcePath, Map<String, String> queryParams, Map<String, String> bodyParams) throws Exception {
        String response = null;
        for (int i = 0; i < 4; i++) {
            try {
                response = sendRequest_Process_Ssns(method, subResourcePath, queryParams, bodyParams);
                if (response != null) {
                    return response;
                }
                ServiceAFweb.AFSleep1Sec(i);
            } catch (Exception ex) {
                logger.info("sendRequest " + method + " Rety " + (i + 1));
            }
        }
        response = sendRequest_Process_Ssns(method, subResourcePath, queryParams, bodyParams);
        return response;
    }

    private String sendRequest_Process_Ssns(String method, String subResourcePath, Map<String, String> queryParams, Map<String, String> bodyParams)
            throws Exception {
        try {

            String URLPath = subResourcePath;

            String webResourceString = "";
            // assume only one param
            if (queryParams != null && !queryParams.isEmpty()) {
                for (String key : queryParams.keySet()) {
                    webResourceString = "?" + key + "=" + queryParams.get(key);
                }
            }

            String bodyElement = "";
            if (bodyParams != null) {
                bodyElement = new ObjectMapper().writeValueAsString(bodyParams);
            }

            URLPath += webResourceString;
            URL request = new URL(URLPath);

            HttpURLConnection con = null; //(HttpURLConnection) request.openConnection();

            if (CKey.PROXY == true) {
                //////Add Proxy 
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ServiceAFweb.PROXYURL, 8080));
                con = (HttpURLConnection) request.openConnection(proxy);
                //////Add Proxy 
            } else {
                con = (HttpURLConnection) request.openConnection();
            }

//            if (URLPath.indexOf(":8080") == -1) {
            String authStr = "APP_SELFSERVEUSGBIZSVC" + ":" + "soaorgid";
            // encode data on your side using BASE64
            byte[] bytesEncoded = Base64.encodeBase64(authStr.getBytes());
            String authEncoded = new String(bytesEncoded);
            con.setRequestProperty("Authorization", "Basic " + authEncoded);
//            }

            if (method.equals(METHOD_POST)) {
                con.setRequestMethod("POST");
            } else if (method.equals(METHOD_GET)) {
                con.setRequestMethod("GET");
            }
            con.setRequestProperty("User-Agent", USER_AGENT);
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            if (method.equals(METHOD_POST)) {
                // For POST only - START
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                byte[] input = bodyElement.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
                os.close();
                // For POST only - END
            }

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Response Code:: " + responseCode);
            }
            if (responseCode >= 200 && responseCode < 300) {
                ;
            } else {
//                System.out.println("Response Code:: " + responseCode);
//                System.out.println("bodyElement :: " + bodyElement);
                return null;
            }
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;

                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {

                    response.append(inputLine);
                }
                in.close();
                // print result
                return response.toString();
            } else {
                logger.info("POST request not worked");
            }

        } catch (Exception e) {
            logger.info("Error sending REST request:" + e);
            throw e;
        }
        return null;
    }

    ////////
    public static String[] splitIncludeEmpty(String inputStr, char delimiter) {
        if (inputStr == null) {
            return null;
        }
        if (inputStr.charAt(inputStr.length() - 1) == delimiter) {
            // the 000webhostapp always add extra ~ at the end see the source
            inputStr += "End";
            String[] tempString = inputStr.split("" + delimiter);
            int size = tempString.length - 1;
            String[] outString = new String[size];
            for (int i = 0; i < size; i++) {
                outString[i] = tempString[i];
            }
            return outString;
        }
        return inputStr.split("" + delimiter);
    }

    /**
     * @return the ssnsDataImp
     */
    public SsnsDataImp getSsnsDataImp() {
        return ssnsDataImp;
    }

    /**
     * @param ssnsDataImp the ssnsDataImp to set
     */
    public void setSsnsDataImp(SsnsDataImp ssnsDataImp) {
        this.ssnsDataImp = ssnsDataImp;
    }

}
