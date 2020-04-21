package com.afweb.process;

import com.afweb.model.*;

import java.util.ArrayList;

import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author eddy
 */
public class SsnsDataImp {

    protected static Logger logger = Logger.getLogger("SsnsDataImp");

    private SsnsDataDB ssnsdb = new SsnsDataDB();

    public void setDataSource(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        ssnsdb.setJdbcTemplate(jdbcTemplate);
        ssnsdb.setDataSource(dataSource);
    }

    public int updateSQLArrayList(ArrayList SQLTran) {
        return ssnsdb.updateSQLArrayList(SQLTran);
    }

//////////////////////////////////////    
    public String getAllLockDBSQL(String sql) {
        return ssnsdb.getAllLockDBSQL(sql);
    }

    public String getAllSsnsDataDBSQL(String sql, int length) {
        return ssnsdb.getAllSsnsDataDBSQL(sql, length);
    }

    ////////////////////////////////
    public ArrayList getAllLock() {
        return ssnsdb.getAllLock();
    }

    public int setLockName(String name, int type, long lockDateValue, String comment) {
        return ssnsdb.setLockName(name, type, lockDateValue, comment);
    }

    public AFLockObject getLockName(String name, int type) {
        return ssnsdb.getLockName(name, type);
    }

    public int setRenewLock(String name, int type, long lockDateValue) {
        return ssnsdb.setRenewLock(name, type, lockDateValue);
    }

    public int removeLock(String name, int type) {
        return ssnsdb.removeLock(name, type);
    }
    public int updateSsnsDataOpenStatus(String app) {
        return ssnsdb.updateSsnsDataOpenStatus(app);
    }
    public int updateSsnsDataAllOpenStatus() {
        return ssnsdb.updateSsnsDataAllOpenStatus();
    }

    public int deleteSsnsAccApp(String app) {
        return ssnsdb.deleteSsnsAccApp(app);
    }

    public int deleteSsnsDataApp(String app) {
        return ssnsdb.deleteSsnsDataApp(app);
    }

    public int deleteSsnsData(String name) {
        return ssnsdb.deleteSsnsData(name);
    }

//    public int insertSsnsDataObject(SsnsData Data) {
//        return ssnsdb.insertSsnsDataObject(Data);
//    }
    public int updatSsnsAccNameStatusTypeById(int id, String name, int status, int type) {
        return ssnsdb.updatSsnsAccNameStatusTypeById(id, name, status, type);
    }

    public int insertSsnsAccObject(SsnsAcc nData) {
        return ssnsdb.insertSsnsAccObject(nData);
    }

    public int updatSsnsDataStatusById(int id, int status) {
        return ssnsdb.updatSsnsDataStatusById(id, status);
    }

    public SsnsData getSsnsDataObjListByID(int id) {
        ArrayList<SsnsData> ssnsObjList = ssnsdb.getSsnsDataObjListByID(id);
        if (ssnsObjList != null) {
            if (ssnsObjList.size() > 0) {
                return ssnsObjList.get(0);
            }
        }
        return null;
    }

    public ArrayList<SsnsData> getSsnsDataObjListByUid(String app, String uid) {
        return ssnsdb.getSsnsDataObjListByUid(app, uid);
    }

    public ArrayList getSsnsDataIDList(String app, String ret, int status, int length) {
        return ssnsdb.getSsnsDataIDList(app, ret, status, length);
    }

    public ArrayList<SsnsData> getSsnsDataObjList(String app, String ret, int status, int length) {
        return ssnsdb.getSsnsDataObjList(app, ret, status, length);
    }

    public SsnsData getSsnsDataObj(String name) {
        ArrayList<SsnsData> ssnsObjList = ssnsdb.getSsnsDataObj(name, 1);
        if (ssnsObjList != null) {
            if (ssnsObjList.size() > 0) {
                return ssnsObjList.get(0);
            }
        }
        return null;
    }

    public ArrayList<SsnsData> getSsnsDataObjList(String name, int length) {
        return ssnsdb.getSsnsDataObj(name, length);
    }

    public ArrayList<SsnsData> getSsnsDataObjList(String name, int type, long updatedatel) {
        return ssnsdb.getSsnsDataObj(name, type, updatedatel);
    }
    public String getSsnsAccObjListByFeatureCnt(String name) {
        return ssnsdb.getSsnsAccObjListByFeatureCnt(name);
    }
    public ArrayList<String> getSsnsAccObjListByFeature(String app) {
        return ssnsdb.getSsnsAccObjListByFeature(app);
    }

    public ArrayList<SsnsAcc> getSsnsAccObjListByFeature(String app, String name, int length) {
        return ssnsdb.getSsnsAccObjListByFeature(app, name, length);
    }

    public ArrayList<SsnsAcc> getSsnsAccObjListByID(String app, String pid) {
        return ssnsdb.getSsnsAccObjListByID(app, pid);
    }

    public ArrayList<SsnsAcc> getSsnsAccObjListByApp(String app, int length) {
        return ssnsdb.getSsnsAccObjListByApp(app, length);
    }

    public ArrayList<SsnsAcc> getSsnsAccObjList(String name, String uid) {
        return ssnsdb.getSsnsAccObjList(name, uid);
    }

    public int deleteAllSsnsData(int month) {
        return ssnsdb.deleteAllSsnsData(month);
    }

    public SsnsData getSsnsDataObjapp_uuid_datel(SsnsData item) {
        ArrayList<SsnsData> ssnsObjList = ssnsdb.getSsnsDataObjapp_uuid_datel(item);
        if (ssnsObjList != null) {
            if (ssnsObjList.size() > 0) {
                return ssnsObjList.get(0);
            }
        }
        return null;
    }

    ////////////////////////////////
    public boolean restSsnsDataDB() {
        return ssnsdb.restSsnsDataDB();
    }

    public boolean cleanSsnsDataDB() {
        return ssnsdb.cleanSsnsDataDB();
    }

    public int deleteAllLock() {
        return ssnsdb.deleteAllLock();
    }

    public int testSsnsDataDB() {
        try {
            int result = ssnsdb.testSsnsDataDB();
        } catch (Exception ex) {
        }
        return -1;  // DB error
    }

    // 0 - new db, 1 - db already exist, -1 db error
    public int initSsnsDataDB() {
        try {
            int result = ssnsdb.initSsnsDataDB();
            return result;
        } catch (Exception ex) {
        }
        return -1;  // DB error
    }

    public ArrayList getAllNameSQL(String sql) {
        return ssnsdb.getAllNameSQL(sql);
    }

    public String getRemoteMYSQL(String sql) {
        try {
            return ssnsdb.getRemoteMYSQL(sql);
        } catch (Exception ex) {
            logger.info("> getRemoteMYSQL exception " + ex.getMessage());
            return null;
        }
    }

    public int updateRemoteMYSQL(String sql) {
        try {
            return ssnsdb.updateRemoteMYSQL(sql);
        } catch (Exception ex) {
            logger.info("> getRemoteMYSQL exception " + ex.getMessage());
            return 0;
        }
    }

}
