package com.afweb.util;

public class CKey {

    //local pc
    public static String FileLocalPath = "C:\\TEMP\\";
    public static final boolean LocalPCflag = true; // true;

    //////////////////////
    // remember to update the application properties      
    public static final int LOCAL_MYSQL = 4; //jdbc:mysql://localhost:3306/db_sample     
    public static final int REMOTE_MYSQL = 2; // https://eddyko.000webhostapp.com/webgetreq.php php mysql
    public static final int MYSQL = 0;   //direct mysql

    public static final int SQL_DATABASE = REMOTE_MYSQL;
    //
    //////////////////////
    //
    public static boolean PROXY = false; //false; //true; 
    public static String PROXYURL_TMP = "webproxystatic-on.tslabc.tabceluabcs.com";
    
    public static String URL_PRODUCT_TMP = "https://sabcoa-mp-rmsabck-prabc.tsabcl.teabclus.com";

    public static boolean NN_DEBUG = true; //false; //true; 
    public static boolean OPENSHIFT_DB1 = true; //false; 
    public static boolean UI_ONLY = true;

//    //
//    //
    public static String WEBPOST_OP_PHP = "/health.php";
    public static String URL_PATH_OP_DB_PHP1_TMP = "http://devphp-project000.paas-app-east-np.tabcsl.telabcus.com"; //eddyko00     
//    public String URL_PATH_OP_DB_PHP1 = "";
    public static final String URL_PATH_OP_TMP = "http://devssns-project000.paas-app-east-np.tabcsl.telabcus.com";
//    public static final String URL_PATH_OP = "";
//    public static String URL_PATH_OP_DB_PHP1 = "http://iiswebphp-web006.apps.us-east-1.starter.openshift-online.com"; //eddyko00     
//    public static final String URL_PATH_OP = "http://iisweb-web006.apps.us-east-1.starter.openshift-online.com";
    //***********    
//***********    
//    public static final String SERVERDB_REMOTE_URL = URL_PATH_OP_DB_PHP1;  //LocalPCflag = false;
//    public static final String SERVERDB_URL = URL_PATH_OP;
//

    public static String dataSourceURL = "";
    public static final String ADMIN_USERNAME = "Admin1";
////////////////////////////  
////////////////////////////    
    public static final String COMMA = ",";
    public static final String MSG_DELIMITER = "~";
    public static final String QUOTE = "'";
    public static final String DASH = "-";
    public static final String DB_DELIMITER = "`";

    public CKey() {

    }

}
