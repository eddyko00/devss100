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

    public int startMonitor(ServiceAFweb serviceAFweb, String name) { //CKey.ADMIN_USERNAME) {
        try {
            //creat monitor
            ArrayList<String> testIdList = new ArrayList();
            ArrayList<String> testFeatList = new ArrayList();

            //check if outstanding testing
            ArrayList arrayTemp = getMoniterIDList();
            if (arrayTemp != null) {
                for (int i = 0; i < arrayTemp.size(); i++) {
                    String tObjSt = moniterNameArray.get(i);
                    testData tObj = new ObjectMapper().readValue(tObjSt, testData.class);
                    if (tObj.getUsername().equals(name)) {
                        return 2;
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
                            tObj.setTesturl("PR");
                            String st = new ObjectMapper().writeValueAsString(tObj);

                            testIdList.add(st);
                            added++;

                            if (added > 4) {
                                break;  // test 5 on each feature
                            }
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
            tObj.setTesturl("PR");
            String st = new ObjectMapper().writeValueAsString(tObj);
            testIdList.add(0, st);  // add front

            tObj.setAccid(0);
            tObj.setType(ConstantKey.COMPLETED);
            tObj.setUsername(name);
            tObj.setTesturl("PR");
            st = new ObjectMapper().writeValueAsString(tObj);
            testIdList.add(st);

            reportdata.setFeatList(testFeatList);
            reportdata.setTestListObj(testIdList);
            String dataSt = new ObjectMapper().writeValueAsString(reportdata);

            SsReport reportObj = new SsReport();
            reportObj.setName(name);
            reportObj.setStatus(ConstantKey.INITIAL);
            reportObj.setUid(SsnsService.REPORT_ALL);

            reportObj.setData(dataSt);

            ArrayList<SsReport> ssReportObjList = getSsnsDataImp().getSsReportObjListByUid(reportObj.getName(), reportObj.getUid());
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

    public ArrayList<String> getMoniterIDList() {
        try {
            //Start process
            String name = CKey.ADMIN_USERNAME;
            String uid = SsnsService.REPORT_ALL;
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
        ArrayList moniterNameArrayTemp = getMoniterIDList();
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
                testData tObj = new testData();
                String tObjSt = moniterNameArray.get(0);
                moniterNameArray.remove(0);
//
                this.execMonitorTesting(serviceAFweb, tObjSt);
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
                                String passSt = "false";
                                if (feat.equals(accObj.getName())) {
                                    passSt = "true";
                                } else {
                                    passSt = "true";
                                    String[] featL = feat.split(":");
                                    String[] nameL = accObj.getName().split(":");
                                    if ((featL.length > 4) && (nameL.length > 4)) {
                                        if (!featL[2].equals(nameL[2])) {
                                            passSt = "false";
                                        }
                                        if (!featL[3].equals(nameL[3])) {
                                            passSt = "false";
                                        }
                                        if (!featL[4].equals(nameL[4])) {
                                            passSt = "false";
                                        }
                                    } else if ((featL.length > 3) && (nameL.length > 3)) {
                                        if (!featL[2].equals(nameL[2])) {
                                            passSt = "false";
                                        }
                                        if (!featL[3].equals(nameL[3])) {
                                            passSt = "false";
                                        }
                                    }
                                }
//                                if (passSt.equals("false")) {
//                                    logger.info("> execMonitorTesting false " + feat + " name:" + accObj.getName());
//                                }
                                SsReport reportObj = new SsReport();
                                reportObj.setName(CKey.ADMIN_USERNAME);
                                reportObj.setStatus(ConstantKey.OPEN);
                                reportObj.setUid(SsnsService.REPORT_REPORT);
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
