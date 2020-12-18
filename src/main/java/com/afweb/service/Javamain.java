/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.service;

import com.afweb.util.CKey;

/**
 *
 * @author eddyko
 */
public class Javamain {

    /**
     * @param args the command line arguments
     */
    public static void checkParameterFlag(String cmd) {

        if (cmd.indexOf("remoteserverflag") != -1) {
            CKey.SQL_RemoteServerDB = true;
            CKey.SQL_DATABASE = CKey.REMOTE_PHP_MYSQL;

        } else if (cmd.indexOf("localmysqlflag") != -1) {
            CKey.SQL_DATABASE = CKey.LOCAL_MYSQL;
            
        } else if (cmd.indexOf("postgresqlflag") != -1) {
            CKey.OTHER_DB = CKey.POSTGRESQLDB;

        } else if (cmd.indexOf("backupFlag") != -1) {
            CKey.backupFlag = true;

        } else if (cmd.indexOf("restoreFlag") != -1) {
            CKey.restoreFlag = true;

        } else if (cmd.indexOf("proxyflag") != -1) {
            CKey.PROXY = true;
        } else if (cmd.indexOf("nndebugflag") != -1) {
            CKey.NN_DEBUG = true;
            CKey.UI_ONLY = true;

        }

    }

    public static void javamain(String[] args) {
        // TODO code application logic here
        ServiceAFweb srv = new ServiceAFweb();
        if (args != null) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    String cmd = args[i];
                    checkParameterFlag(cmd);
                } // loop
            }
        }

        while (true) {
            srv.timerHandler("");
            ServiceAFweb.AFSleep1Sec(1);
        }

    }
}
