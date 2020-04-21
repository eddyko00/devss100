package com.example.herokudemo;

import com.afweb.model.*;
import com.afweb.process.*;
import com.afweb.util.*;
import com.afweb.service.ServiceAFweb;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//https://www.baeldung.com/spring-cors
@CrossOrigin(origins = "http://localhost:8383")
@RestController
public class IndexController {

    private static AFwebService afWebService = new AFwebService();

    @GetMapping("/")
    public String index() {
        return "Hello there! I'm running v1.1";
    }

    /////////////////////////////////////////////////////////////////////////    
    @RequestMapping(value = "/help", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList SystemHelpPage() {

        ArrayList arrayString = new ArrayList();

        arrayString.add("/server");
        arrayString.add("/server/url0");
        arrayString.add("/server/url0/set?url=stop");
        //
        arrayString.add("/cust/add?email={email}&pass={pass}&firstName={firstName}&lastName={lastName}");
        arrayString.add("/cust/login?email={email}&pass={pass}");
        arrayString.add("/cust/{username}/login&pass={pass}");

        arrayString.add("/cust/{username}/id/{id}/prod?length={0 for all}");
        arrayString.add("/cust/{username}/id/{id}/prod/id/{pid}");
        arrayString.add("/cust/{username}/id/{id}/prod/id/{pid}/rt");
        arrayString.add("/cust/{username}/id/{id}/prod/featureall");
        arrayString.add("/cust/{username}/id/{id}/prod/feature?name=");

        arrayString.add("/cust/{username}/id/{id}/app?length={0 for all}");
        arrayString.add("/cust/{username}/id/{id}/app/id/{pid}");
        arrayString.add("/cust/{username}/id/{id}/app/id/{pid}/rt/getapp");
        arrayString.add("/cust/{username}/id/{id}/app/id/{pid}/rt/gettimeslot");
        arrayString.add("/cust/{username}/id/{id}/app/featureall");
        arrayString.add("/cust/{username}/id/{id}/app/feature?name=");

        arrayString.add("/cust/{username}/id/{id}/wifi?length={0 for all}");
        arrayString.add("/cust/{username}/id/{id}/wifi/id/{pid}");
        arrayString.add("/cust/{username}/id/{id}/wifi/id/{pid}/rt/getdevie");
        arrayString.add("/cust/{username}/id/{id}/wifi/id/{pid}/rt/getdevicestatus");
        arrayString.add("/cust/{username}/id/{id}/wifi/featureall");
        arrayString.add("/cust/{username}/id/{id}/wifi/feature?name=");

        arrayString.add("/cust/{username}/sys/stop");
        arrayString.add("/cust/{username}/sys/clearlock");
        arrayString.add("/cust/{username}/sys/start");
        arrayString.add("/cust/{username}/sys/lock");
        arrayString.add("/cust/{username}/sys/reopenssnsdata");
        arrayString.add("/cust/{username}/sys/custlist");
        arrayString.add("/cust/{username}/sys/cust/{customername}/status/{status}/substatus/{substatus}");

        return arrayString;
    }

    @RequestMapping(value = "/server", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getServerObj() {

        return afWebService.getServerList();
    }

    @RequestMapping(value = "/server/url0", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String getServerURL() {
        return RESTtimer.serverURL_0;
    }

    @RequestMapping(value = "/server/url0/set", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String setServerURL(
            @RequestParam(value = "url", required = true) String urlSt,
            HttpServletRequest request, HttpServletResponse response
    ) {

        RESTtimer.serverURL_0 = urlSt.trim();
        return "done...";
    }

    @RequestMapping(value = "/timerhandler", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus timerHandlerREST(
            @RequestParam(value = "resttimerMsg", required = false) String resttimerMsg
    ) {

        WebStatus msg = new WebStatus();
        msg.setResult(true);
        msg.setResultID(ConstantKey.ENABLE);

        //process timer handler
        int timerCnt = afWebService.timerHandler(resttimerMsg);

        msg.setResponse("timerCnt " + timerCnt);
        return msg;
    }

    @RequestMapping(value = "/cust/login", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    LoginObj getCustObjLogin(
            @RequestParam(value = "email", required = true) String emailSt,
            @RequestParam(value = "pass", required = true) String passSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            LoginObj loginObj = new LoginObj();
            loginObj.setCustObj(null);
            WebStatus webStatus = new WebStatus();
            webStatus.setResultID(100);
            loginObj.setWebMsg(webStatus);
            return loginObj;
        }
        if (emailSt == null) {
            return null;
        }
        if (passSt == null) {
            return null;
        }
        LoginObj loginObj = afWebService.getCustomerEmailLogin(emailSt, passSt);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return loginObj;
    }

    @RequestMapping(value = "/cust/{username}/login", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    LoginObj getCustObjUserLogin(
            @PathVariable("username") String username,
            @RequestParam(value = "pass", required = true) String passSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            LoginObj loginObj = new LoginObj();
            loginObj.setCustObj(null);
            WebStatus webStatus = new WebStatus();
            webStatus.setResultID(100);
            loginObj.setWebMsg(webStatus);
            return loginObj;
        }
        if (passSt == null) {
            return null;
        }
        LoginObj loginObj = afWebService.getCustomerLogin(username, passSt);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return loginObj;
    }

/////////////////////////////////////
    @RequestMapping(value = "/cust/{username}/id/{id}/wifi", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getwifiprod(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "length", required = false) String lengthSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;
        if (lengthSt != null) {
            length = Integer.parseInt(lengthSt);
        }
        ArrayList<SsnsAcc> ret = afWebService.getSsnsprod(username, idSt, length, SsnsService.APP_WIFI);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/wifi/id/{pid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    SsnsAcc getwifipid(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        SsnsAcc ret = afWebService.getSsnsprodById(username, idSt, pidSt, SsnsService.APP_WIFI);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/wifi/id/{pid}/rt/getdevice", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getwifiidrt(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        String oper = SsnsService.WI_Getdev;
        ArrayList<String> ret = afWebService.testSsnsprodWifiByIdRT(username, idSt, pidSt, SsnsService.APP_WIFI, oper);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/wifi/id/{pid}/rt/getdevicestatus", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getwifiidrtstatus(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        String oper = SsnsService.WI_GetDeviceStatus;
        ArrayList<String> ret = afWebService.testSsnsprodWifiByIdRT(username, idSt, pidSt, SsnsService.APP_WIFI, oper);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/wifi/featureall", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<String> getwififeature(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        ArrayList<String> ret = afWebService.getSsnsprodByFeature(username, idSt, SsnsService.APP_WIFI);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/wifi/feature/name", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getwififeature(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "name", required = true) String name,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;     
        ArrayList<SsnsAcc> ret = afWebService.getSsnsprodByFeatureName(username, idSt, name, SsnsService.APP_WIFI, length);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

/////////////////////////////////////    
    @RequestMapping(value = "/cust/{username}/id/{id}/app", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getappprod(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "length", required = false) String lengthSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;
        if (lengthSt != null) {
            length = Integer.parseInt(lengthSt);
        }
        ArrayList<SsnsAcc> ret = afWebService.getSsnsprod(username, idSt, length, SsnsService.APP_APP);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/app/id/{pid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    SsnsAcc getappid(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        SsnsAcc ret = afWebService.getSsnsprodById(username, idSt, pidSt, SsnsService.APP_APP);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/app/id/{pid}/rt/getapp", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getappidrt(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        String oper = SsnsService.APP_GET_APP;
        ArrayList<String> ret = afWebService.testSsnsprodAppByIdRT(username, idSt, pidSt, SsnsService.APP_APP, oper);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/app/id/{pid}/rt/gettimeslot", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getappidrtTimeSlot(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        String oper = SsnsService.APP_GET_TIMES;
        ArrayList<String> ret = afWebService.testSsnsprodAppByIdRT(username, idSt, pidSt, SsnsService.APP_APP, oper);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/app/featureall", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<String> getappfeature(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        ArrayList<String> ret = afWebService.getSsnsprodByFeature(username, idSt, SsnsService.APP_APP);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/app/feature/name", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getappfeature(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "name", required = true) String name,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;

        ArrayList<SsnsAcc> ret = afWebService.getSsnsprodByFeatureName(username, idSt, name, SsnsService.APP_APP, length);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }
////////////////////////////////////

    @RequestMapping(value = "/cust/{username}/id/{id}/prod", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getprodttv(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "length", required = false) String lengthSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;
        if (lengthSt != null) {
            length = Integer.parseInt(lengthSt);
        }
        ArrayList<SsnsAcc> ret = afWebService.getSsnsprod(username, idSt, length, SsnsService.APP_PRODUCT);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/prod/id/{pid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    SsnsAcc getprodttvid(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        SsnsAcc ret = afWebService.getSsnsprodById(username, idSt, pidSt, SsnsService.APP_PRODUCT);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/prod/id/{pid}/rt", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getprodttvidrt(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @PathVariable("pid") String pidSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }

        ArrayList<String> ret = afWebService.testSsnsprodByIdRT(username, idSt, pidSt, SsnsService.APP_PRODUCT);
        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/prod/featureall", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<String> getprodttvfeature(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        ArrayList<String> ret = afWebService.getSsnsprodByFeature(username, idSt, SsnsService.APP_PRODUCT);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }

    @RequestMapping(value = "/cust/{username}/id/{id}/prod/feature/name", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList<SsnsAcc> getprodttvfeatureName(
            @PathVariable("username") String username,
            @PathVariable("id") String idSt,
            @RequestParam(value = "name", required = true) String name,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return null;
        }
        int length = 20; //10;
        ArrayList<SsnsAcc> ret = afWebService.getSsnsprodByFeatureName(username, idSt, name, SsnsService.APP_PRODUCT, length);

        ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
        return ret;
    }
//////////////////////////////////////////////

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus serverPing() {
        WebStatus msg = new WebStatus();

        msg = afWebService.serverPing();
        return msg;
    }

    //////////////
    @RequestMapping(value = "/cust/{username}/sys/stop", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus SystemStop(@PathVariable("username") String username) {
        WebStatus msg = new WebStatus();

        if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
            msg.setResponse(afWebService.SystemStop());
            msg.setResult(true);
            return msg;
        }

        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/start", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus SystemStart(@PathVariable("username") String username) {
        WebStatus msg = new WebStatus();
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
                msg.setResponse(afWebService.SystemStart());
                msg.setResult(true);
                return msg;
            }
        }

        CustomerObj cust = afWebService.getCustomerIgnoreMaintenance(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                msg.setResponse(afWebService.SystemStart());
                msg.setResult(true);
                return msg;
            }
        }
        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/reopenssnsdata", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus SystemReopen(@PathVariable("username") String username) {
        WebStatus msg = new WebStatus();
        // remote is stopped
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
                msg.setResponse(afWebService.SystemClearLock());
                msg.setResult(true);
                return msg;
            }
        }
        CustomerObj cust = afWebService.getCustomerIgnoreMaintenance(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                msg.setResponse(afWebService.SystemReOpenData());
                msg.setResult(true);
                return msg;
            }
        }

        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/clearlock", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    WebStatus SystemClearLock(@PathVariable("username") String username) {
        WebStatus msg = new WebStatus();
        // remote is stopped
        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
                msg.setResponse(afWebService.SystemClearLock());
                msg.setResult(true);
                return msg;
            }
        }
        CustomerObj cust = afWebService.getCustomerIgnoreMaintenance(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                msg.setResponse(afWebService.SystemClearLock());
                msg.setResult(true);
                return msg;
            }
        }

        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/lock", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getLockAll(
            @PathVariable("username") String username
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);

        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
                ArrayList result = afWebService.getAllLock();
                return result;
            }
        }

        CustomerObj cust = afWebService.getCustomerPassword(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                ArrayList result = afWebService.getAllLock();
                ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
                return result;
            }
        }
        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/request", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    RequestObj SystemSQLRequest(
            @PathVariable("username") String username,
            @RequestBody String input
    ) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        RequestObj sqlReq = null;
        try {
            sqlReq = new ObjectMapper().readValue(input, RequestObj.class);
        } catch (IOException ex) {
            return null;
        }

        if (ServiceAFweb.getServerObj().isSysMaintenance() == true) {
            if (username.toLowerCase().equals(CKey.ADMIN_USERNAME.toLowerCase())) {
                RequestObj sqlResp = afWebService.SystemSQLRequest(sqlReq);
                return sqlResp;
            }
        }

        CustomerObj cust = afWebService.getCustomerPassword(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                RequestObj sqlResp = afWebService.SystemSQLRequest(sqlReq);
                ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
                return sqlResp;
            }
        }
        return null;
    }

//////////////
    @RequestMapping(value = "/cust/{username}/sys/custlist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ArrayList getCustList(
            @PathVariable("username") String username,
            @RequestParam(value = "length", required = false) String lengthSt) {
        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        int length = 0; //20;
        if (lengthSt != null) {
            length = Integer.parseInt(lengthSt);
        }
        CustomerObj cust = afWebService.getCustomerPassword(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                ArrayList custNameList = afWebService.getCustomerList(length);
                ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
                return custNameList;
            }
        }
        return null;
    }

    @RequestMapping(value = "/cust/{username}/sys/cust/{customername}/status/{status}/substatus/{substatus}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    int updateCustomer(
            @PathVariable("username") String username,
            @PathVariable("customername") String customername,
            @PathVariable("status") String status,
            @PathVariable("substatus") String substatus
    ) {

        ServiceAFweb.getServerObj().setCntControRequest(ServiceAFweb.getServerObj().getCntControRequest() + 1);
        if (customername == null) {
            return 0;
        }
        CustomerObj cust = afWebService.getCustomerPassword(username, null);
        if (cust != null) {
            if (cust.getType() == CustomerObj.INT_ADMIN_USER) {
                int result = afWebService.updateCustStatusSubStatus(customername, status, substatus);
                ServiceAFweb.getServerObj().setCntControlResp(ServiceAFweb.getServerObj().getCntControlResp() + 1);
                return result;
            }
        }
        return 0;
    }

    /////////////////////////////////////////////////////////////////////////    
    @RequestMapping(value = "/timer")
    public ModelAndView timerPage() {
        ModelAndView model = new ModelAndView("helloWorld");

        model.addObject("message", AFwebService.getServerObj().getServerName() + " " + AFwebService.getServerObj().getVerString() + "</br>"
                + AFwebService.getServerObj().getLastServUpdateESTdate() + "</br>"
                + AFwebService.getServerObj().getTimerMsg() + "</br>" + AFwebService.getServerObj().getTimerThreadMsg());
        return model;
    }

}
