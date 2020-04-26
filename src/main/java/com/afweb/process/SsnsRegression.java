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

import com.afweb.util.*;

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

    public static String R_PASS = "pass";
    public static String R_FAIL = "false";

    public static String REPORT_USER = "user";
    public static String REPORT_MOMITOR = "monitor";
    public static String REPORT_REPORT = "report";
    public static String REPORT_TESE_CASE = "test";

    public int startMonitor(ServiceAFweb serviceAFweb, String name) { //CKey.ADMIN_USERNAME) {
        try {

            //creat monitor
            ArrayList<String> testIdList = new ArrayList();
            ArrayList<String> testFeatList = new ArrayList();

            //check if outstanding testing
            SsReport userReportObj = null;
            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(name, REPORT_USER);
            if (ssReportObjList != null) {
                if (ssReportObjList.size() > 0) {
                    userReportObj = ssReportObjList.get(0);
                    if (userReportObj.getStatus() == ConstantKey.INITIAL) {

                        return 2; // report already running
                    }
                }
            }

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
            reportObj.setUid(REPORT_REPORT);  // 

            String dataSt = new ObjectMapper().writeValueAsString(reportdata);
            reportObj.setData(dataSt);

            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            long ctime = dateNow.getTimeInMillis();
            reportObj.setUpdatedatel(ctime);
            reportObj.setUpdatedatedisplay(new java.sql.Date(ctime));

            // create report
            int ret = getSsnsDataImp().insertSsReportObject(reportObj);

            //update userReportObj to start
            if (userReportObj == null) {
                userReportObj = new SsReport();
                userReportObj.setName(name);
                userReportObj.setStatus(ConstantKey.INITIAL);
                userReportObj.setType(ConstantKey.OPEN);
                userReportObj.setUid(REPORT_USER);  // 

                userReportObj.setUpdatedatel(ctime);
                userReportObj.setUpdatedatedisplay(new java.sql.Date(ctime));

                // create report
                ret = getSsnsDataImp().insertSsReportObject(userReportObj);

            } else {
                userReportObj.setStatus(ConstantKey.INITIAL);
                userReportObj.setType(ConstantKey.OPEN);
                userReportObj.setUpdatedatel(ctime);
                userReportObj.setUpdatedatedisplay(new java.sql.Date(ctime));
                ret = getSsnsDataImp().updatSsReportDataStatusTypeById(userReportObj.getId(), userReportObj.getData(),
                        userReportObj.getStatus(), userReportObj.getType());

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
            String uid = REPORT_REPORT;
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

    public ArrayList<String> getMoniterIDList(SsReport reportObj) {
        try {
            if (reportObj.getStatus() == ConstantKey.INITIAL) {
                String dataSt = reportObj.getData();
                if (dataSt.length() > 0) {
                    ReportData reportdata = new ObjectMapper().readValue(dataSt, ReportData.class);
                    ArrayList<String> testIdList = reportdata.getTestListObj();
                    return testIdList;
                }
            }
        } catch (Exception ex) {
            logger.info("> getMoniterIDList Exception " + ex.getMessage());
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
        if (moniterNameArray.size() == 0) {
            return 0;
        }
        int result = 0;

        try {
            ArrayList<String> idList = new ArrayList();

            String name = moniterNameArray.get(0);
            moniterNameArray.remove(0);
//
            SsReport userReportObj = null;
            String uid = REPORT_REPORT;
            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);
            if (ssReportObjList != null) {
                if (ssReportObjList.size() > 0) {
                    userReportObj = ssReportObjList.get(0);
                }
            }

            if (userReportObj == null) {
                return 0;
            }
            idList = getMoniterIDList(userReportObj);

            if (idList != null) {

                String LockName = "ETL_MONITOR_" + name;
                Calendar dateNow = TimeConvertion.getCurrentCalendar();
                long lockDateValue = dateNow.getTimeInMillis();

                int lockReturn = serviceAFweb.setLockNameProcess(LockName, ConstantKey.MON_LOCKTYPE, lockDateValue, ServiceAFweb.getServerObj().getSrvProjName() + "processFeatureApp");
                if (CKey.NN_DEBUG == true) {
                    lockReturn = 1;
                }
                if (lockReturn == 0) {
                    return 0;
                }

                logger.info("processMonitorTesting for 1 minutes " + name + " size " + idList.size());

                long currentTime = System.currentTimeMillis();
                long lockDate1Min = TimeConvertion.addMinutes(currentTime, 1);

                while (idList.size() > 0) {
                    currentTime = System.currentTimeMillis();
                    if (lockDate1Min < currentTime) {
                        break;
                    }
                    String tObjSt = idList.get(0);
                    idList.remove(0);
                    execMonitorTesting(serviceAFweb, tObjSt, userReportObj);
                }

                if (userReportObj != null) {
                    String dataSt = userReportObj.getData();
                    if (dataSt.length() > 0) {
                        ReportData reportdata = new ObjectMapper().readValue(dataSt, ReportData.class);
                        reportdata.setTestListObj(idList);
                        dataSt = new ObjectMapper().writeValueAsString(reportdata);
                        userReportObj.setData(dataSt);
                    }
                    dateNow = TimeConvertion.getCurrentCalendar();
                    long ctime = dateNow.getTimeInMillis();
                    userReportObj.setUpdatedatel(ctime);
                    userReportObj.setUpdatedatedisplay(new java.sql.Date(ctime));
                    int ret = getSsnsDataImp().updatSsReportDataStatusTypeById(userReportObj.getId(), userReportObj.getData(),
                            userReportObj.getStatus(), userReportObj.getType());
                }
                serviceAFweb.removeNameLock(LockName, ConstantKey.MON_LOCKTYPE);
            }
            AFSleep();

        } catch (Exception ex) {
            logger.info("> processMonitorTesting Exception " + ex.getMessage());
        }

        return result;

    }

    public void execMonitorTesting(ServiceAFweb serviceAFweb, String tObjSt, SsReport userReportObj) {
        try {
            testData tObj = new ObjectMapper().readValue(tObjSt, testData.class);
            if (tObj.getType() == ConstantKey.INITIAL) {
                // send communication to start
                return;
            }
            if (tObj.getType() == ConstantKey.COMPLETED) {
                // send communication to completed

                if (userReportObj != null) {

                    userReportObj.setStatus(ConstantKey.COMPLETED);
                    userReportObj.setType(ConstantKey.OPEN);

                    userReportObj.setData("");
                    Calendar dateNow = TimeConvertion.getCurrentCalendar();
                    long ctime = dateNow.getTimeInMillis();
                    userReportObj.setUpdatedatel(ctime);
                    userReportObj.setUpdatedatedisplay(new java.sql.Date(ctime));
                    userReportObj.setRet("complete:" + new java.sql.Date(ctime));
                    int ret = getSsnsDataImp().updatSsReportDataStatusTypeRetById(userReportObj.getId(), userReportObj.getData(),
                            userReportObj.getStatus(), userReportObj.getType(), userReportObj.getRet());

                }
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
                                String nameRepId = userReportObj.getName() + "_" + userReportObj.getId();
                                reportObj.setName(nameRepId);
                                reportObj.setStatus(ConstantKey.OPEN);
                                reportObj.setRet(passSt);
                                reportObj.setExec(exec);

                                reportObj.setApp(accObj.getApp());
                                reportObj.setCusid(accObj.getCusid());
                                reportObj.setBanid(accObj.getBanid());
                                reportObj.setOper(accObj.getOper());
                                reportObj.setTiid(accObj.getTiid());

                                reportObj.setType(userReportObj.getId()); // reference to report test case
                                reportObj.setUid(REPORT_TESE_CASE);

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

    public void reportMoniter(ServiceAFweb serviceAFweb, String name) {
        // report
        try {
            String uid = REPORT_REPORT;
            ArrayList<SsReport> reportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);

            if (reportObjList == null) {
                return;
            }
            if (reportObjList.size() == 0) {
                return;
            }

            SsReport repObj = reportObjList.get(0);

            ArrayList<String> testReportList = new ArrayList();
            String nameRepId = name + "_" + repObj.getId();

            this.getReportStat(serviceAFweb, nameRepId, SsnsService.APP_PRODUCT, testReportList);
            this.getReportStat(serviceAFweb, nameRepId, SsnsService.APP_WIFI, testReportList);
            this.getReportStat(serviceAFweb, nameRepId, SsnsService.APP_APP, testReportList);
            this.getReportStat(serviceAFweb, nameRepId, SsnsService.APP_TTVC, testReportList);
            logger.info("> reportList  " + testReportList.size());

            if (repObj.getStatus() == ConstantKey.COMPLETED) {
                ReportData reportdata = new ReportData();

                String dataSt = repObj.getData();
                if (dataSt.length() > 0) {
                    reportdata = new ObjectMapper().readValue(dataSt, ReportData.class);
                }
                reportdata.setReportList(testReportList);

                dataSt = new ObjectMapper().writeValueAsString(reportdata);
                repObj.setData(dataSt);

                Calendar dateNow = TimeConvertion.getCurrentCalendar();
                long ctime = dateNow.getTimeInMillis();
                repObj.setUpdatedatel(ctime);
                repObj.setUpdatedatedisplay(new java.sql.Date(ctime));
                int ret = getSsnsDataImp().updatSsReportDataStatusTypeById(repObj.getId(), repObj.getData(),
                        repObj.getStatus(), repObj.getType());
            }
////////////////
///////// put back to the main user
            uid = REPORT_USER;
            reportObjList = getSsnsDataImp().getSsReportObjListByUid(name, uid);

            if (reportObjList == null) {
                return;
            }
            if (reportObjList.size() == 0) {
                return;
            }
            ReportData reportdata = new ReportData();

            String dataSt = repObj.getData();
            if (dataSt.length() > 0) {
                reportdata = new ObjectMapper().readValue(dataSt, ReportData.class);
            }
            reportdata.setReportList(testReportList);

            dataSt = new ObjectMapper().writeValueAsString(reportdata);
            repObj.setData(dataSt);
            Calendar dateNow = TimeConvertion.getCurrentCalendar();
            long ctime = dateNow.getTimeInMillis();
            repObj.setUpdatedatel(ctime);
            repObj.setUpdatedatedisplay(new java.sql.Date(ctime));
            int ret = getSsnsDataImp().updatSsReportDataStatusTypeById(repObj.getId(), repObj.getData(),
                    repObj.getStatus(), repObj.getType());

        } catch (Exception ex) {

        }
    }

    public void getReportStat(ServiceAFweb serviceAFweb, String nameRepId, String app, ArrayList<String> reportList) {
        int Pass = 0;
        int Fail = 0;

        ArrayList<String> namelist = getSsnsDataImp().getSsReportObjListByFeatureOper(nameRepId, app);

        ArrayList<String> retlist = new ArrayList();
        if (namelist != null) {
            if (namelist.size() == 0) {
                return;
            }
            for (int i = 0; i < namelist.size(); i++) {
                String oper = namelist.get(i);
                retlist.add(oper);
                String cnt = getSsnsDataImp().getSsReportObjListByFeatureOperCnt(nameRepId, oper);
                retlist.add(cnt);
            }
        }
        namelist = getSsnsDataImp().getSsReportObjListByFeatureOper(nameRepId, app);

        ArrayList<String> operList = new ArrayList();
        if (namelist != null) {
            for (int i = 0; i < namelist.size(); i++) {
                String oper = namelist.get(i);
                operList.add(oper);
                String cnt = getSsnsDataImp().getSsReportObjListByFeatureOperCnt(nameRepId, oper);
                operList.add(cnt);
            }
        }

        if (operList != null) {
            for (int j = 0; j < operList.size(); j += 2) {
                String oper = operList.get(j);
                String reportLine = oper + "," + operList.get(j + 1);
                reportList.add(reportLine);

                ArrayList<SsReport> reportOperList = getSsnsDataImp().getSsReportByFeatureOperIdList(nameRepId, app, oper, 0);
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

    /////
}
