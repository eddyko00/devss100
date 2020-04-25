/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.process;

import com.afweb.model.*;

import com.afweb.model.ssns.*;
import com.afweb.service.ServiceAFweb;
import static com.afweb.service.ServiceAFweb.AFSleep;
import static com.afweb.service.ServiceAFweb.logger;
import com.afweb.util.CKey;
import com.afweb.util.TimeConvertion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author koed
 */
public class SsnsRegression {

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

    private SsnsDataImp ssnsDataImp = new SsnsDataImp();
    protected static Logger logger = Logger.getLogger("SsnsRegression");

    public static String R_PASS = "true";
    public static String R_FAIL = "false";

    public static String REPORT_START = "all";
    public static String REPORT_MOMITOR = "monitor";
    public static String REPORT_REPORT = "report";
    public static String REPORT_TESE_CASE = "test";

    public void reportMoniter(ServiceAFweb serviceAFweb) {
        // report
        try {
            String name = CKey.ADMIN_USERNAME;
            String uid = REPORT_START;

            ArrayList<SsReport> reportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);
            if (reportObjList != null) {
                if (reportObjList.size() != 0) {

                    SsReport repObj = reportObjList.get(0);
//                    String dataStTmp = repObj.getData();
//                    ReportData reportdata = new ObjectMapper().readValue(dataStTmp, ReportData.class);

                    ArrayList<String> testReportList = new ArrayList();
                    this.getReportStat(serviceAFweb, SsnsService.APP_WIFI, testReportList);
                    this.getReportStat(serviceAFweb, SsnsService.APP_APP, testReportList);
                    this.getReportStat(serviceAFweb, SsnsService.APP_PRODUCT, testReportList);
                    this.getReportStat(serviceAFweb, SsnsService.APP_TTVC, testReportList);
                    logger.info("> reportList  " + testReportList.size());

                    ReportData reportdata = new ReportData();
                    reportdata.setReportList(testReportList);

                    SsReport reportNewObj = new SsReport();
                    reportNewObj.setName(name);
                    reportNewObj.setUid(uid);
                    reportNewObj.setStatus(ConstantKey.OPEN);
                    reportNewObj.setUid(REPORT_REPORT);

                    String dataSt = new ObjectMapper().writeValueAsString(reportdata);
                    reportNewObj.setData(dataSt);

                    ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(reportNewObj.getName(), reportNewObj.getUid());
                    boolean exist = false;
                    if (ssReportObjList != null) {
                        if (ssReportObjList.size() != 0) {
                            SsReport report = ssReportObjList.get(0);
                            int status = report.getStatus();
                            int type = report.getType();
                            int ret = getSsnsDataImp().updatSsReportDataStatusTypeById(report.getId(), dataSt, status, type);
                            exist = true;
                        }
                    }
                    if (exist == false) {
                        int ret = getSsnsDataImp().insertSsReportObject(reportNewObj);
                    }

                }
            }
        } catch (Exception ex) {

        }
    }

    public void getReportStat(ServiceAFweb serviceAFweb, String app, ArrayList<String> reportList) {
        int Pass = 0;
        int Fail = 0;
        ArrayList<String> operList = serviceAFweb.getSsReportByFeature(CKey.ADMIN_USERNAME, null, app);
        if (operList != null) {
            for (int j = 0; j < operList.size(); j += 2) {
                String oper = operList.get(j);
                String reportLine = oper + "," + operList.get(j + 1);
                reportList.add(reportLine);

                ArrayList<SsReport> reportOperList = serviceAFweb.getSsReportByFeatureOperIdList(CKey.ADMIN_USERNAME, null, app, oper);
                if (reportOperList != null) {
                    for (int k = 0; k < reportOperList.size(); k++) {
                        SsReport rObj = reportOperList.get(k);
                        reportLine = rObj.getId() + "," + rObj.getCusid() + "," + rObj.getBanid() + "," + rObj.getTiid() + "," + rObj.getRet() + "," + rObj.getExec();
                        reportList.add(reportLine);

                        if (rObj.getRet().equals(R_PASS)) {
                            Pass++;
                        } else {
                            Fail++;
                        }
                    }
                }
            }
            String reportLine = app + "," + "Pass " + Pass + ",Fail " + Fail;
            reportList.add(reportLine);
        }
    }

    public int startMonitor(ServiceAFweb serviceAFweb, String name) { //CKey.ADMIN_USERNAME) {
        try {

            //creat monitor
            ArrayList<String> testIdList = new ArrayList();
            ArrayList<String> testFeatList = new ArrayList();

            //check if outstanding testing
            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(name, REPORT_START);
            if (ssReportObjList != null) {
                SsReport repObj = ssReportObjList.get(0);
                if (repObj.getStatus() == ConstantKey.INITIAL) {
                    
//                    return 2; // report running
                }
            }
//            ArrayList arrayTemp = getMoniterIDList(name);
//            if (arrayTemp != null) {
//                for (int i = 0; i < arrayTemp.size(); i++) {
//                    String tObjSt = moniterNameArray.get(i);
//                    testData tObj = new ObjectMapper().readValue(tObjSt, testData.class);
//                    if (tObj.getUsername().equals(name)) {
//                        return 2;
//                    }
//                }
//            }
            ReportData reportdata = new ReportData();
            ArrayList<String> servList = serviceAFweb.getSsnsprodAll(name, null, 0);
            for (int i = 0; i < servList.size(); i += 2) {
                String servProd = servList.get(i);
                ArrayList<String> featallList = serviceAFweb.getSsnsprodByFeature(name, null, servProd);

                for (int j = 0; j < featallList.size(); j += 2) {
                    String featN = featallList.get(j);
                    if (featN.indexOf("fail") != -1) {
                        continue;
                    }
                    testFeatList.add(featN);

                    ArrayList<SsnsAcc> SsnsAcclist = getSsnsDataImp().getSsnsAccObjListByFeature(servProd, featN, 15);
                    int added = 0;
                    if (SsnsAcclist != null) {
                        for (int k = 0; k < SsnsAcclist.size(); k++) {
                            SsnsAcc accObj = SsnsAcclist.get(k);

                            if (accObj.getType() > 10) {  // testfailed will increment this type
                                continue;
                            }
                            testData tObj = new testData();
                            tObj.setAccid(accObj.getId());
                            tObj.setUsername(name);
                            tObj.setTesturl("");
                            String st = new ObjectMapper().writeValueAsString(tObj);

                            testIdList.add(st);
                            added++;
////////////////////////////////////////////////
                            break;  // just for testing
////////////////////////////////////////////////
                        }
                        logger.info("> startMonitor  " + featN + "id added " + added);
                    }
//                    ///////just for testing
//                    break;
                }
            }

            testData tObj = new testData();
            tObj.setAccid(0);
            tObj.setType(ConstantKey.INITIAL);
            tObj.setUsername(name);
            tObj.setTesturl("");
            String st = new ObjectMapper().writeValueAsString(tObj);
            testIdList.add(0, st);  // add front

            tObj.setAccid(0);
            tObj.setType(ConstantKey.COMPLETED);
            tObj.setUsername(name);
            tObj.setTesturl("");
            st = new ObjectMapper().writeValueAsString(tObj);
            testIdList.add(st);

            reportdata.setFeatList(testFeatList);
            reportdata.setTestListObj(testIdList);

            SsReport reportObj = new SsReport();
            reportObj.setName(name);
            reportObj.setStatus(ConstantKey.INITIAL);
            reportObj.setUid(REPORT_START);

            String dataSt = new ObjectMapper().writeValueAsString(reportdata);
            reportObj.setData(dataSt);

            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            long ctime = dateNow.getTimeInMillis();
            reportObj.setUpdatedatel(ctime);
            reportObj.setUpdatedatedisplay(new java.sql.Date(ctime));

            ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(reportObj.getName(), reportObj.getUid());
            boolean exist = false;
            if (ssReportObjList != null) {
                if (ssReportObjList.size() != 0) {
                    SsReport report = ssReportObjList.get(0);
                    int status = report.getStatus();
                    int type = report.getType();
                    int ret = getSsnsDataImp().updatSsReportDataStatusTypeById(report.getId(), dataSt, status, type);
                    exist = true;
                }
            }
            if (exist == false) {
                int ret = getSsnsDataImp().insertSsReportObject(reportObj);
            }
            return 1;
        } catch (Exception ex) {
            logger.info("> startMonitor Exception " + ex.getMessage());
        }
        return 0;
    }

    public ArrayList<String> getMoniterNameList(String name) {
        ArrayList<String> reportNameL = new ArrayList();
        try {
            //Start process
            String uid = REPORT_START;
            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);
            if (ssReportObjList != null) {
                for (int i = 0; i < ssReportObjList.size(); i++) {
                    SsReport reportObj = ssReportObjList.get(i);
                    if (reportObj.getStatus() == ConstantKey.INITIAL) {
                        reportNameL.add(reportObj.getName());
                    }
                }
                return reportNameL;
            }
        } catch (Exception ex) {
            logger.info("> getMoniterNameList Exception " + ex.getMessage());
        }
        return reportNameL;
    }

    public ArrayList<String> getMoniterIDList(String name) {
        try {
            //Start process
            String uid = REPORT_START;
            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);
            if (ssReportObjList != null) {
                if (ssReportObjList.size() > 0) {
                    SsReport reportObj = ssReportObjList.get(0);
                    if (reportObj.getStatus() == ConstantKey.INITIAL) {
                        String dataSt = reportObj.getData();

                        ReportData reportdata = new ObjectMapper().readValue(dataSt, ReportData.class);
                        ArrayList<String> testIdList = reportdata.getTestListObj();
                        return testIdList;
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("> getMoniterID Exception " + ex.getMessage());
        }
        return null;
    }

    ////////////////////////////////
    ArrayList<String> moniterNameArray = new ArrayList();

    private ArrayList updateMonitorNameArray() {
        if (moniterNameArray != null && moniterNameArray.size() > 0) {
            return moniterNameArray;
        }
        ArrayList moniterNameArrayTemp = getMoniterNameList("");
        if (moniterNameArrayTemp != null) {
            moniterNameArray = moniterNameArrayTemp;
        }
        return moniterNameArray;
    }

    public int processMonitorTesting(ServiceAFweb serviceAFweb) {

        updateMonitorNameArray();
        if ((moniterNameArray == null) || (moniterNameArray.size() == 0)) {
            return 0;
        }
        int result = 0;
        Calendar dateNow = TimeConvertion.getCurrentCalendar();
        long lockDateValue = dateNow.getTimeInMillis();
        String LockName = "ETL_MONITOR";

        try {
            ArrayList<String> idList = new ArrayList();

            int lockReturn = serviceAFweb.setLockNameProcess(LockName, ConstantKey.MON_LOCKTYPE, lockDateValue, ServiceAFweb.getServerObj().getSrvProjName() + "processFeatureApp");
            if (CKey.NN_DEBUG == true) {
                lockReturn = 1;
            }
            if (lockReturn == 0) {
                return 0;
            }

            logger.info("processMonitorTesting for 1 minutes size " + moniterNameArray.size());

            long currentTime = System.currentTimeMillis();
            long lockDate1Min = TimeConvertion.addMinutes(currentTime, 1);

            for (int i = 0; i < 2; i++) {
                currentTime = System.currentTimeMillis();
//                if (CKey.NN_DEBUG != true) {
                if (lockDate1Min < currentTime) {
                    break;
                }
//                }
                if (moniterNameArray.size() == 0) {
                    break;
                }
                String name = moniterNameArray.get(0);
                moniterNameArray.remove(0);

//
                idList = getMoniterIDList(name);
                for (int j = 0; j < idList.size(); j++) {

                }

//                testData tObj = new testData();
//                this.execMonitorTesting(serviceAFweb, tObjSt);
//                
                AFSleep();
            }
        } catch (Exception ex) {
            logger.info("> processMonitorTesting Exception " + ex.getMessage());
        }
        serviceAFweb.removeNameLock(LockName, ConstantKey.MON_LOCKTYPE);
        return result;

    }

    public void execMonitorTesting(ServiceAFweb serviceAFweb, String tObjSt) {
        try {
            testData tObj = new ObjectMapper().readValue(tObjSt, testData.class);
            if (tObj.getType() == ConstantKey.INITIAL) {
                // send communication to start
                return;
            }
            if (tObj.getType() == ConstantKey.COMPLETED) {
                // send communication to completed
                return;
            }
            int id = tObj.getAccid();

            SsnsAcc accObj = getSsnsDataImp().getSsnsAccObjByID(id);
            String dataSt = accObj.getData();
            ProductData pData = new ObjectMapper().readValue(dataSt, ProductData.class);
            if (pData != null) {
                ArrayList<String> cmdList = pData.getCmd();
                if (cmdList != null) {
                    for (int j = 0; j < cmdList.size(); j += 2) {
                        String oper = cmdList.get(j + 1);

                        ArrayList<String> response = serviceAFweb.testSsnsprodPRocessByIdRT(CKey.ADMIN_USERNAME, null, accObj.getId() + "", accObj.getApp(), oper);
                        if (response != null) {
                            if (response.size() > 3) {
                                String feat = response.get(0);
                                String execSt = response.get(2);
                                execSt = ServiceAFweb.replaceAll("elapsedTime:", "", execSt);
                                long exec = Long.parseLong(execSt);
                                String passSt = R_FAIL;
                                if (feat.equals(accObj.getName())) {
                                    passSt = R_PASS;
                                } else {
                                    passSt = R_PASS;
                                    String[] featL = feat.split(":");
                                    String[] nameL = accObj.getName().split(":");
                                    if ((featL.length > 4) && (nameL.length > 4)) {
                                        if (!featL[2].equals(nameL[2])) {
                                            passSt = R_FAIL;
                                        }
                                        if (!featL[3].equals(nameL[3])) {
                                            passSt = R_FAIL;
                                        }
                                        if (!featL[4].equals(nameL[4])) {
                                            passSt = R_FAIL;
                                        }
                                    } else if ((featL.length > 3) && (nameL.length > 3)) {
                                        if (!featL[2].equals(nameL[2])) {
                                            passSt = R_FAIL;
                                        }
                                        if (!featL[3].equals(nameL[3])) {
                                            passSt = R_FAIL;
                                        }
                                    }
                                }
//                                if (passSt.equals("false")) {
//                                    logger.info("> execMonitorTesting false " + feat + " name:" + accObj.getName());
//                                }
                                SsReport reportObj = new SsReport();
                                reportObj.setName(CKey.ADMIN_USERNAME);
                                reportObj.setStatus(ConstantKey.OPEN);
                                reportObj.setUid(REPORT_REPORT);
                                reportObj.setRet(passSt);
                                reportObj.setExec(exec);

                                reportObj.setApp(accObj.getApp());
                                reportObj.setCusid(accObj.getCusid());
                                reportObj.setBanid(accObj.getBanid());
                                reportObj.setOper(accObj.getOper());
                                reportObj.setTiid(accObj.getTiid());

                                ProductData pDataNew = new ProductData();
                                pDataNew.setPostParam(pData.getPostParam());
                                pDataNew.setFlow(response);

                                String nameSt = new ObjectMapper().writeValueAsString(pDataNew);
                                reportObj.setData(nameSt);

                                Calendar dateNow = TimeConvertion.getCurrentCalendar();
                                long ctime = dateNow.getTimeInMillis();
                                reportObj.setUpdatedatel(ctime);
                                reportObj.setUpdatedatedisplay(new java.sql.Date(ctime));
                                int ret = getSsnsDataImp().insertSsReportObject(reportObj);
                            }
                        }
                        AFSleep();
                    }
                }
            }
        } catch (Exception ex) {
            logger.info("> execMonitorTesting Exception " + ex.getMessage());
        }
    }

    /////
}
