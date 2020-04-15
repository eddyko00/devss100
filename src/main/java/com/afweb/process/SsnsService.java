/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.process;

import com.afweb.model.*;
import com.afweb.model.ssns.*;
import com.afweb.service.ServiceAFweb;
import static com.afweb.service.ServiceAFweb.*;

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

    private SsnsDataImp ssnsDataImp = new SsnsDataImp();

    public String getFeatureSsnsAppointment(SsnsData dataObj) {
        String featTTV = "";
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
            if (oper.equals("updateAppointment")) {
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
            } else if (oper.equals("cancelAppointment")) {
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

            if (appTId.equals("")) {
                return "";
            }
            logger.info(dataSt);

/////////////
            boolean stat = this.updateSsnsAppointment(appTId, banid, cust, host, pData, dataObj);
            if (stat == true) {
                getSsnsDataImp().updatSsnsDataStatusById(dataObj.getId(), ConstantKey.COMPLETED);
            }

        } catch (Exception ex) {
            logger.info("> getFeatureSsnsAppointment Exception" + ex.getMessage());
        }
        return "";
    }

    public boolean updateSsnsAppointment(String appTId, String banid, String cust, String host, ProductData pData, SsnsData dataObj) {
        try {
            String featTTV = "";
            String oper = dataObj.getOper();
            if (oper.equals("updateAppointment")) {
                if ((banid.length() == 0) && (cust.length() == 0)) {
                    featTTV = APP_PRODUCT_TYPE_APP;
                    featTTV += ":" + host;
                    featTTV += ":" + oper;
                    featTTV += ":ContactEng";
                } else {
                    String outputSt = SsnsAppointment(ServiceAFweb.URL_PRODUCT, appTId, banid, cust, host);
                    if (outputSt == null) {
                        return false;
                    }
                    ProductApp prodTTV = parseAppointmentFeature(outputSt);
                    pData.setpAPP(prodTTV);
                    featTTV = prodTTV.getFeat();
                }
            } else if (oper.equals("cancelAppointment")) {
                featTTV = APP_PRODUCT_TYPE_APP;
                featTTV += ":" + host;
                featTTV += ":" + oper;
                if ((banid.length() == 0) && (cust.length() == 0)) {
                    featTTV += ":ContactEng";
                }
            } else {
                return false;
            }

            logger.info("> updateSsnsAppointment featTTV " + featTTV);
/////////////TTV   
            ArrayList<String> flow = getFlowSsnsProdiuctInventory(dataObj);
            if (flow == null) {
                logger.info("> updateSsnsAppointment skip no flow");
                return false;
            }

            pData.setFlow(flow);

            SsnsData NdataObj = new SsnsData();
            NdataObj.setName(featTTV);
            NdataObj.setBanid(banid);
            NdataObj.setUid(dataObj.getUid());
            NdataObj.setApp(dataObj.getApp());
            NdataObj.setOper(dataObj.getOper());
            NdataObj.setDown(dataObj.getDown());
            NdataObj.setRet(dataObj.getRet());
            NdataObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NdataObj.setData(nameSt);
            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            NdataObj.setUpdatedatedisplay(new java.sql.Date(dateNow.getTimeInMillis()));
            NdataObj.setUpdatedatel(dateNow.getTimeInMillis());

//            
            ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NdataObj.getName(), NdataObj.getUid());
            boolean exist = false;
            if (ssnsAccObjList != null) {
                if (ssnsAccObjList.size() != 0) {
                    exist = true;
                    return false;
                }
            }
            if (exist == false) {
                int ret = getSsnsDataImp().insertSsnsAccObject(NdataObj);
                if (ret == 1) {
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.info("> updateSsnsAppointment Exception" + ex.getMessage());
        }
        return false;
    }

    public static ProductApp parseAppointmentFeature(String outputSt) {

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
        featTTV += ":" + prodTTV.getHost();
        featTTV += ":" + prodTTV.getCategory();
        featTTV += ":" + prodTTV.getStatusCd();
        featTTV += ":" + prodTTV.getCategoryCd();

        prodTTV.setFeat(featTTV);

        return prodTTV;
    }

    public String SsnsAppointment(String ProductURL, String appTId, String banid, String cust, String host) {
        if (host.length() > 0) {
            host = host.replace("9", ""); // remove OMS9
        }
        String url = ProductURL + "/v2/cmo/selfmgmt/appointmentmanagement/appointment?customerid=" + cust;
        if (banid.length() > 0) {
            url = ProductURL + "/v2/cmo/selfmgmt/appointmentmanagement/appointment?ban=" + banid + "&customerid=" + cust
                    + "&appointmentlist.hostsystemcd.in=" + host;
        }
        try {
            String output = this.sendRequest_Ssns(METHOD_GET, url, null, null);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsAppointment exception " + ex.getMessage());
        }
        return null;
    }

    public String getFeatureSsnsProdTesting(SsnsAcc dataObj, ArrayList<String> outputList) {
        if (dataObj == null) {
            return "";
        }
        String banid = dataObj.getBanid();
        String outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, SsnsService.APP_PRODUCT_TYPE_TTV);
        if (outputSt == null) {
            return "";
        }
        ArrayList<String> outList = ServiceAFweb.prettyPrintJSON(outputSt);
        outputList.addAll(outList);
        ProductTTV prodTTV = parseProductTtvFeature(outputSt);
        return prodTTV.getFeatTTV();
    }

    public String getFeatureSsnsProdiuctInventory(SsnsData dataObj) {
        String featTTV = "";
        ProductData pData = new ProductData();
        if (dataObj == null) {
            return "";
        }
        String prodBanid = "";
        String banid = "";
        try {

            String oper = dataObj.getOper();
            if (oper.equals("getProductById")) {
                ;
            } else if (oper.equals("getProductList")) {
                ;
            } else {
                logger.info("> getFeatureSsnsProdiuctInventory Other oper " + oper);
            }

            String daSt = dataObj.getData();
            //["xxx","xxx","product.characteristic.channelInfoList",null,null,null]
            daSt = ServiceAFweb.replaceAll("[", "", daSt);
            daSt = ServiceAFweb.replaceAll("]", "", daSt);
            daSt = ServiceAFweb.replaceAll("\"", "", daSt);
            String[] daList = daSt.split(",");
            if (daList.length < 3) {
                return "";
            }
            prodBanid = daList[0];
            banid = daList[1];
            if (banid.equals("null")) {
                banid = prodBanid;
            }
            if (banid.equals("null")) {
                return "";
            }
            logger.info(daSt);

/////////////
            boolean stat = this.updateSsnsProdiuctInventoryInternet(banid, pData, dataObj);

            stat = this.updateSsnsProdiuctInventoryTTV(banid, pData, dataObj);

            //update to complete so will not process again
            getSsnsDataImp().updatSsnsDataStatusById(dataObj.getId(), ConstantKey.COMPLETED);

        } catch (Exception ex) {
            logger.info("> getFeatureSsnsProdiuctInventory Exception" + ex.getMessage());
        }
        return "";
    }

    public boolean updateSsnsProdiuctInventoryInternet(String banid, ProductData pData, SsnsData dataObj) {
        try {
            String featTTV = "";
            String outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, SsnsService.APP_PRODUCT_TYPE_HSIC);
            if (outputSt == null) {
                return false;
            }
            ProductTTV prodTTV = parseProductInternetFeature(outputSt);

            pData.setpHSIC(prodTTV);
            featTTV = prodTTV.getFeatTTV();
            logger.info("> updateSsnsProdiuctInventoryInternet HSIC feat " + featTTV);
/////////////TTV   
            ArrayList<String> flow = getFlowSsnsProdiuctInventory(dataObj);
            if (flow == null) {
                logger.info("> updateSsnsProdiuctInventoryInternet HSIC skip no flow");
                return false;
            }

            pData.setFlow(flow);

            SsnsData NdataObj = new SsnsData();
            NdataObj.setName(prodTTV.getFeatTTV());
            NdataObj.setBanid(banid);
            NdataObj.setUid(dataObj.getUid());
            NdataObj.setApp(dataObj.getApp());
            NdataObj.setOper(dataObj.getOper());
            NdataObj.setDown(dataObj.getDown());
            NdataObj.setRet(dataObj.getRet());
            NdataObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NdataObj.setData(nameSt);
            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            NdataObj.setUpdatedatedisplay(new java.sql.Date(dateNow.getTimeInMillis()));
            NdataObj.setUpdatedatel(dateNow.getTimeInMillis());

//            
            ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NdataObj.getName(), NdataObj.getUid());
            boolean exist = false;
            if (ssnsAccObjList != null) {
                if (ssnsAccObjList.size() != 0) {
                    exist = true;
                    return false;
                }
            }
            if (exist == false) {
                int ret = getSsnsDataImp().insertSsnsAccObject(NdataObj);
                if (ret == 1) {
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.info("> updateSsnsProdiuctInventoryInternet HSIC Exception" + ex.getMessage());
        }
        return false;
    }

    public static ProductTTV parseProductInternetFeature(String outputSt) {

        if (outputSt == null) {
            return null;
        }
        ProductTTV prodTTV = new ProductTTV();
        int productCdInit = 0;
        int quotaAmtInit = 0;
        int fifaInit = 0;

        ArrayList<String> outputList = ServiceAFweb.prettyPrintJSON(outputSt);
        for (int j = 0; j < outputList.size(); j++) {
            String inLine = outputList.get(j);
//            logger.info("" + inLine);
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
            if (inLine.indexOf("quotaAmt") != -1) {

                if (quotaAmtInit == 1) {
                    continue;
                }
                String valueSt = outputList.get(j + 1);
                if (valueSt.indexOf("value") == -1) {
                    continue;
                }

                if (valueSt.indexOf("B") == -1) {
                    ;
                } else {
                    continue;
                }
                quotaAmtInit = 1;
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);

                prodTTV.setOffer(valueSt);

                valueSt = outputList.get(j - 3);
                if (valueSt.indexOf("value") == -1) {
                    continue;
                }
                valueSt = ServiceAFweb.replaceAll("\"", "", valueSt);
                valueSt = ServiceAFweb.replaceAll("value:", "", valueSt);
                valueSt = ServiceAFweb.replaceAll(" ", "-", valueSt);
                prodTTV.setProductCd(valueSt);
                continue;
            }

        }

        String featTTV = APP_PRODUCT_TYPE_HSIC;

        String fifa = "fifa";
        if (prodTTV.getIsFIFA() == 0) {
            fifa = "comp";
        }
        featTTV += ":" + fifa;
        featTTV += ":" + prodTTV.getOffer();
        featTTV += ":" + prodTTV.getProductCd();

        prodTTV.setFeatTTV(featTTV);

        return prodTTV;
    }

    public boolean updateSsnsProdiuctInventoryTTV(String banid, ProductData pData, SsnsData dataObj) {
        try {
            String featTTV = "";
            String outputSt = SsnsProdiuctInventory(ServiceAFweb.URL_PRODUCT, banid, SsnsService.APP_PRODUCT_TYPE_TTV);
            if (outputSt == null) {
                return false;
            }
            ProductTTV prodTTV = parseProductTtvFeature(outputSt);

            pData.setpTTV(prodTTV);
            featTTV = prodTTV.getFeatTTV();
            logger.info("> updateSsnsProdiuctInventory TTV feat " + featTTV);
/////////////TTV   
            ArrayList<String> flow = getFlowSsnsProdiuctInventory(dataObj);
            if (flow == null) {
                logger.info("> updateSsnsProdiuctInventory TTV skip no flow");
                return false;
            }

            pData.setFlow(flow);

            SsnsData NdataObj = new SsnsData();
            NdataObj.setName(prodTTV.getFeatTTV());
            NdataObj.setBanid(banid);
            NdataObj.setUid(dataObj.getUid());
            NdataObj.setApp(dataObj.getApp());
            NdataObj.setOper(dataObj.getOper());
            NdataObj.setDown(dataObj.getDown());
            NdataObj.setRet(dataObj.getRet());
            NdataObj.setExec(dataObj.getExec());

            String nameSt = new ObjectMapper().writeValueAsString(pData);
            NdataObj.setData(nameSt);
            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            NdataObj.setUpdatedatedisplay(new java.sql.Date(dateNow.getTimeInMillis()));
            NdataObj.setUpdatedatel(dateNow.getTimeInMillis());

//            
            ArrayList<SsnsAcc> ssnsAccObjList = getSsnsDataImp().getSsnsAccObjList(NdataObj.getName(), NdataObj.getUid());
            boolean exist = false;
            if (ssnsAccObjList != null) {
                if (ssnsAccObjList.size() != 0) {
                    exist = true;
                    return false;
                }
            }
            if (exist == false) {
                int ret = getSsnsDataImp().insertSsnsAccObject(NdataObj);
                if (ret == 1) {
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.info("> updateSsnsProdiuctInventory TTV Exception" + ex.getMessage());
        }
        return false;
    }

    public static ProductTTV parseProductTtvFeature(String outputSt) {

        if (outputSt == null) {
            return null;
        }
        ProductTTV prodTTV = new ProductTTV();
        int productCdInit = 0;
        int ChannelListInit = 0;
        int offerInit = 0;
        int fifaInit = 0;

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
                    prodTTV.setOffer("HS2.0");
                    continue;
                }
                if (valueSt.indexOf("MediaroomTV-HS") != -1) {
                    prodTTV.setOffer("HS");
                    continue;
                }
                if (valueSt.indexOf("TVX") != -1) {
                    prodTTV.setOffer("TVX");
                }

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

        String fifa = "fifa";
        if (prodTTV.getIsFIFA() == 0) {
            fifa = "comp";
        }
        featTTV += ":" + fifa;
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

    public String SsnsProdiuctInventory(String ProductURL, String ban, String productType) {
        String url = ProductURL + "/v1/cmo/selfmgmt/productinventory/product?billingAccount.id=" + ban
                + "&productType=" + productType + "&fields=product.characteristic.channelInfoList";
        try {
            String output = this.sendRequest_Ssns(METHOD_GET, url, null, null);
            return output;
        } catch (Exception ex) {
            logger.info("> SsnsProdiuctInventory exception " + ex.getMessage());
        }
        return null;
    }

    public ArrayList<String> getFlowSsnsProdiuctInventory(SsnsData dataObj) {
        ArrayList<String> flow = new ArrayList();
        ArrayList<SsnsData> ssnsList = getSsnsDataImp().getSsnsDataObjListByUid(dataObj.getApp(), dataObj.getUid());
        if (ssnsList != null) {
//            logger.info("> ssnsList " + ssnsList.size());
            for (int i = 0; i < ssnsList.size(); i++) {
                SsnsData data = ssnsList.get(i);
                String flowSt = data.getDown();
                if (flowSt.length() == 0) {
                    flowSt = data.getOper();
                }
                flowSt += ":" + data.getExec();
//                logger.info("> flow " + flowSt);
                flow.add(flowSt);
            }
        }
        return flow;
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
                AFSleep1Sec(i);
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

            if (bodyParams != null && !bodyParams.isEmpty()) {
                String bodyTmp = "";
                for (String key : bodyParams.keySet()) {
                    bodyTmp = bodyParams.get(key);
                    bodyTmp = bodyTmp.replaceAll("&", "-");
                    bodyTmp = bodyTmp.replaceAll("%", "%25");
                    bodyElement = key + "=" + bodyTmp;
                }

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

            if (URLPath.indexOf(":8080") == -1) {
                String authStr = "APP_SELFSERVEUSGBIZSVC" + ":" + "soaorgid";
                // encode data on your side using BASE64
                byte[] bytesEncoded = Base64.encodeBase64(authStr.getBytes());
                String authEncoded = new String(bytesEncoded);
                con.setRequestProperty("Authorization", "Basic " + authEncoded);
            }

            if (method.equals(METHOD_POST)) {
                con.setRequestMethod("POST");
            } else if (method.equals(METHOD_GET)) {
                con.setRequestMethod("GET");
            }
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//                con.setRequestProperty("Content-Type", "application/json; utf-8");

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
