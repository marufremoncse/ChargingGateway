/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import DBHandler.DBHandler;
import Service.ChargeConf;
import Service.ChargeSteps;
import Service.KeywordChargeConf;
import Service.KeywordDetails;
import Service.GameSupplementKeyword;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author marufur
 */

public class RequestProcessor extends Thread {
    
    RequestQueue reqQ;
    int threadID;
    String encodeFormat="UTF-8";
    long ping=0,pong=0;
    Charging chrg=null;
    HttpURLConnection conng;
    int socketval=0;
    
    public RequestProcessor(RequestQueue reqQ, int threadID) {
        this.reqQ = reqQ;
        this.threadID = threadID;
    }

    public void run() {
        System.out.println("Starting Thread :" + threadID);
        int qSize = reqQ.getQueueSize();
        for (int i = 0; i < qSize; i++) {
            RequestDetails requestDetails = reqQ.getFromQueue(i);
            try{
                System.out.println("Processing Request Details :" + i + " of Thread " + threadID);
                System.out.println(requestDetails.toString());
                if(reqQ.parent.dndList.isDNDExist(requestDetails.getMsisdn())){
                    System.out.println(requestDetails.getMsisdn()+" is in DND List");
                    reqQ.parent.insertSMSSent(requestDetails, "dnd",-1);
                }
                else{
                    if(requestDetails.processMsgKey()){
                        if(reqQ.parent.keyworddetailsList.ishmKeywordDetailsExistKeyword(requestDetails.getSmsKey()) || reqQ.parent.keyworddetailsList.ishmGameSupKeyDetailsExistKeyword(requestDetails.getSmsKey())){
                            KeywordDetails keyworddetails;
                            if(reqQ.parent.keyworddetailsList.ishmGameSupKeyDetailsExistKeyword(requestDetails.getSmsKey())){
                                GameSupplementKeyword supgame= reqQ.parent.keyworddetailsList.getGameSupKeyDetails(requestDetails.getSmsKey());
                                keyworddetails = supgame.getKeyword();
                                requestDetails.setSuplementgameid(supgame.getId());
                                requestDetails.setIssupplementkeyexist(true);
                                System.out.println("Supplementery Keyword Found: Keyword Name:"+keyworddetails.getKeyword());
                            }
                            else{
                                keyworddetails = reqQ.parent.keyworddetailsList.getKeywordDetails(requestDetails.getSmsKey());
                                System.out.println("Single Keyword Found: Keyword Name:"+keyworddetails.getKeyword());
                            }
                            if(requestDetails.processMsg(keyworddetails)){
                                if(reqQ.parent.keywordChargeConfList.ishmKeywordChargeConfExist(keyworddetails.getId())){
                                    KeywordChargeConf keywordchargeconf = reqQ.parent.keywordChargeConfList.getKeywordChargeConf(keyworddetails.getId());
                                    UnlockStatus st = retrieveUnlock(requestDetails,false);
                                    int checkSync = keyworddetails.getSync();
                                    if(checkSync==1){
                                        if(st.isStatus()){
                                            String codesms = keyworddetails.getUnlocksms().replaceAll("<CODE>", st.getUnlockkey());
                                            codesms = codesms.replaceAll("<GAMENAME>", requestDetails.getGamename());
                                            requestDetails.setReturncode(st.getUnlockkey());
                                            if(reqQ.parent.isExpired(requestDetails)){
                                                int chargeallow=1;
                                                getChargingTimeStamp(requestDetails);
                                                if(st.IsGamepriceexist()) {
                                                    if(st.getGamePrice()==0){
                                                        chargeallow=0;
                                                    }
                                                    else if(st.getGamePrice()>0){
                                                        System.out.println("Continue charging:"+requestDetails.getId());
                                                    }
                                                    else{
                                                        chargeallow=-1; 
                                                    }
                                                }
                           
                                                if(chargeallow>0){
                                                    int steps = keywordchargeconf.getTotalstep();
                                                    ChargeStatus cs = new ChargeStatus();
                                                    ChargeConf chargeconf;
                                                    for(int s=0;s<steps;s++){                                                        
                                                        ChargeSteps chargestep = keywordchargeconf.getChargeSteps(s);                                                        
                                                        chargeconf = reqQ.parent.chargeConfList.getChargingConf(chargestep.getChargeid());
                                                        if(chargeconf.getPrice()>st.getGamePrice() && st.IsGamepriceexist()){
                                                            continue;
                                                        }
                                                        if(chargeconf.getPrice()==0) {
                                                            cs.setGWStatus("100", requestDetails.getOperator());
                                                            cs.setResponse("0 charge bypass");
                                                            cs.setTrid("0");
                                                        }else
                                                            cs = process_dob(requestDetails,chargeconf,codesms);
                                                        if(cs.getStatus()==0){
                                                            requestDetails.setChargeamount(chargeconf.getPriceWithVat());
                                                            requestDetails.setPrice(chargeconf.getPrice());
                                                            requestDetails.setValidity(chargestep.getValidity());
                                                            requestDetails.setTid(cs.getTrid());
                                                            String chargesuccesssms=keyworddetails.getChargesmssuccess().replaceAll("<PRICEVAT>", ""+requestDetails.getChargeamount());
                                                            reqQ.parent.insertChargeSuccesslog(requestDetails, cs, chargeconf);
                                                            switch(requestDetails.getOperator()){
                                                                case 1:
                                                                case 3:
                                                                    if(requestDetails.getChargeamount()>0.0){
                                                                        reqQ.parent.insertSMSSent(requestDetails, chargesuccesssms,1);
                                                                    }                                                                    
                                                                    reqQ.parent.insertSMSSent(requestDetails, codesms,1);
                                                                    break;
                                                                case 2:
                                                                    if(requestDetails.getChargeamount()>0.0){
                                                                        cs = process_dob(requestDetails,null,chargesuccesssms);
                                                                    }                                                                      
                                                                    break;
                                                            }
                                                            
                                                            reqQ.parent.insertSubWithCode(requestDetails);
                                                            if(keyworddetails.getDeviceLog()==1)
                                                                reqQ.parent.insertDeviceLog(requestDetails);
                                                            if(keyworddetails.getUnlockcodelog()==1)
                                                                reqQ.parent.insertUnlockCodeLog(requestDetails,st.getResponse(),st.getTt());
                                                            break;
                                                        }
                                                        else{
                                                            reqQ.parent.insertChargeFaillog(requestDetails, cs, chargeconf);
                                                            if(cs.getRetry()) {
                                                                s--;
                                                                try {
                                                                    Thread.sleep(100);
                                                                }catch(Exception e) {
                                                                    System.out.println("Sleep Error");
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(cs.getStatus()!=0) {
                                                        reqQ.parent.insertSMSSent(requestDetails,keyworddetails.getChargesmsfail(),2); 
                                                        if(keyworddetails.getDeviceLog()==1)
                                                            reqQ.parent.insertDeviceLog(requestDetails);
                                                    }
                                                   
                                                        
                                                    
                                                    if(keyworddetails.getNotification()==1)
                                                        sentNotification(requestDetails, cs);

                                                }
                                                else if(chargeallow==0){
                                                    getChargingTimeStamp(requestDetails);
                                                    requestDetails.setChargeamount(0);
                                                    requestDetails.setPrice(0);
                                                    ChargeStatus cs= process_dob(requestDetails,null,codesms);
                                                    if(cs.getStatus()==0)
                                                        reqQ.parent.insertSMSSent(requestDetails, codesms,4);
                                                    else
                                                        reqQ.parent.insertSMSSent(requestDetails, codesms, 6);
                                                    if(keyworddetails.getDeviceLog()==1)
                                                        reqQ.parent.insertDeviceLog(requestDetails);
                                                    if(keyworddetails.getUnlockcodelog()==1)
                                                        reqQ.parent.insertUnlockCodeLog(requestDetails,st.getResponse(),st.getTt());
                                                    break;
                                                }   
                                            }
                                            else{
                                                if(keyworddetails.isSameKeyValid()){
                                                    getChargingTimeStamp(requestDetails);
                                                    requestDetails.setChargeamount(0);
                                                    requestDetails.setPrice(0);
                                                    if(requestDetails.getReturncode().length()>1){
                                                        ChargeStatus cs= process_dob(requestDetails,null,codesms);
                                                        if(cs.getStatus()==0)
                                                            reqQ.parent.insertSMSSent(requestDetails, codesms,3);
                                                        else
                                                            reqQ.parent.insertSMSSent(requestDetails, codesms, 6);
                                                    }
                                                    else if(requestDetails.getReturncode().equalsIgnoreCase("E")){
                                                        ChargeStatus cs= process_dob(requestDetails,null,codesms);
                                                        if(cs.getStatus()==0)
                                                            reqQ.parent.insertSMSSent(requestDetails, codesms,3);
                                                        else
                                                            reqQ.parent.insertSMSSent(requestDetails, codesms, 6);
                                                        if(keyworddetails.getDeviceLog()==1)
                                                            reqQ.parent.insertDeviceLog(requestDetails);
                                                        if(keyworddetails.getUnlockcodelog()==1)
                                                            reqQ.parent.insertUnlockCodeLog(requestDetails,st.getResponse(),st.getTt());
                                                    }
                                                    else{
                                                        reqQ.parent.insertSMSSent(requestDetails,keyworddetails.getChargesmserror(),12);
                                                    }
                                                }
                                                else{
                                                    getChargingTimeStamp(requestDetails);
                                                    requestDetails.setChargeamount(0);
                                                    requestDetails.setPrice(0);
                                                    ChargeStatus cs= process_dob(requestDetails,null,codesms);
                                                    if(cs.getStatus()==0)
                                                        reqQ.parent.insertSMSSent(requestDetails, codesms,3);
                                                    else
                                                        reqQ.parent.insertSMSSent(requestDetails, codesms, 6);
                                                    if(keyworddetails.getDeviceLog()==1)
                                                        reqQ.parent.insertDeviceLog(requestDetails);
                                                    if(keyworddetails.getUnlockcodelog()==1)
                                                        reqQ.parent.insertUnlockCodeLog(requestDetails,st.getResponse(),st.getTt());
                                                }
                                            }
                                        }
                                        else {
                                            reqQ.parent.insertSMSSent(requestDetails, keyworddetails.getChargesmserror(),10);
                                        }
                                    }
                                    else if(checkSync==0){
                                        reqQ.parent.insertRequestDetails(requestDetails);
                                    }                                    
                                }
                                else {
                                    String msg = "Invalid Request. Keyword charge not configured";
                                    reqQ.parent.insertSMSSent(requestDetails, keyworddetails.getChargesmserror(),7);
                                    requestDetails.setGamename("BTRC");
                                    ChargeStatus cs= process_dob(requestDetails,null,msg);
                                }
                            }
                            else {
                                String msg = "Invalid Request. Message process Error(MO Parse)";
                                reqQ.parent.insertSMSSent(requestDetails,keyworddetails.getChargesmserror(),5);
                                requestDetails.setGamename("BTRC");
                                ChargeStatus cs= process_dob(requestDetails,null,msg);
                            }
                        }
                        else {
                            String msg="Sorry, incorrect keyword. For details, please dial our helpline: 16272";
                            reqQ.parent.insertSMSSent(requestDetails, msg,9);
                            requestDetails.setGamename("BTRC");                            
                            ChargeStatus cs= process_dob(requestDetails,null,msg);
                        }
                    }
                    else {
                        String msg="Sorry, incorrect/unknown keyword. For details, please dial our helpline: 16272";
                        reqQ.parent.insertSMSSent(requestDetails, msg,8);
                        requestDetails.setGamename("BTRC");                        
                        ChargeStatus cs= process_dob(requestDetails,null,msg);
                    }
                }
            }catch(Exception e) {
                System.out.println("************************");
                e.printStackTrace();
                reqQ.parent.insertSMSSent(requestDetails, "Exception",11);
            }
        }
        System.out.println("THREAD " + threadID + " Exit::::");
    }

    public int getRemainingAmountStepID(KeywordDetails gamedetails,RequestDetails requestDetails,KeywordChargeConf gamechargeconf){
        try{
            int amountcharged=requestDetails.getPrice();
            int gamecharge=reqQ.parent.chargeConfList.getChargingConf(gamechargeconf.getChargeSteps(0).getChargeid()).getPrice();
            if(amountcharged==gamecharge)
                return -1;
            else{
                int remaincharge=gamecharge-amountcharged;
                ChargeConf chargeconf=reqQ.parent.chargeConfList.getChargingConfWithPrice(remaincharge);

                return gamechargeconf.getChargeStepsByChargeID(chargeconf.getId()).getId();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("####################");
            System.out.println("#######################################");
            System.out.println("##### Issue to charge Remaining #######");
            System.out.println("#######################################");
            return -1;
        }
    }

    public ChargeStatus process_dob(RequestDetails requestDetails, ChargeConf chargeconf,String codesms){
        System.out.println("THREAD:" + threadID + "->ReqID:" + requestDetails.getId() + " MSISDN:" + requestDetails.getMsisdn());
        int amount=0;
        String chargecode="";
        String game_name = requestDetails.getGamename();
        boolean success = false;
        String status = "F";
        String msisdn = requestDetails.getMsisdn();
        String timeStamp = getChargingTimeStamp(requestDetails);
        String msgid = requestDetails.getMsgid();
        int operator = requestDetails.getOperator();
        long tm = System.currentTimeMillis();
        ping=tm;
        this.setPong(1);//pong=-1;
        this.socketval=1;
        
        ChargeStatus cs=new ChargeStatus();
        if(chargeconf!=null){
            switch(operator){
                case 1:
                    amount=chargeconf.getPrice();
                    chargecode = chargeconf.getChargecode();
                    chrg = new BLCharging(msisdn,codesms,chargecode,msgid,amount,game_name,operator,timeStamp);
                    break;
                case 2:
                    chrg = new GPCharging(msisdn,codesms,chargecode,msgid,amount,game_name,operator,timeStamp);
                    break;
                case 3: 
                    chrg = new RobiCharging(msisdn,codesms,chargecode,msgid,chargeconf.getPriceWithVat(),game_name,operator,timeStamp);                    
                    break;
            }
            cs = chrg.result();            
        }
        else if(chargeconf==null){
            switch(operator){
                case 1:
                case 3:
                    cs.setGWStatus("1000",operator);
                    cs.setResponse("Success");
                    cs.setTrid("0");
                    cs.setTt(0);
                    break;
                case 2:
                    chrg = new GPCharging(msisdn,codesms,chargecode,msgid,amount,game_name,operator,timeStamp);
                    cs = chrg.result();
                    break;
            }    
        }
        long tmt = System.currentTimeMillis();
        this.setPong(0);//pong=0;
        this.chrg=null;
        long tt=tmt - tm;
        reqQ.parent.updateOperatorTime(tt);
        cs.setTt(tt);
        this.socketval=0;
        return cs;
    } 


    private static String getChargingTimeStamp(RequestDetails requestDetails) {
        SimpleDateFormat sdf1 = null;
        SimpleDateFormat sdf2 = null;
        Calendar rightNow = null;
        String tms="",ids="000"+requestDetails.getId();
        try {
                String DATE_FORMAT1 = "yyyyMMdd";
                String DATE_FORMAT2 = "HHmmssS";
                long tm = System.currentTimeMillis();
                tms=""+tm;
                tms=tms.substring(tms.length()-11);
                ids=ids.substring(ids.length()-3);
        } catch (Exception ex) {
                ex.printStackTrace();

        }

        requestDetails.setTid(tms+ids);
        return tms+ids;
    }

    public UnlockStatus retrieveUnlock(RequestDetails requestDetails,boolean isvalidation){
        //public Status process(RequestDetails requestDetails,SenderInfo senderinfo, Route route){
        System.out.print("THREAD:" + threadID + "->ReqID:" + requestDetails.getId() + " MSISDN:" + requestDetails.getMsisdn());
        UnlockStatus st = new UnlockStatus(requestDetails);
        boolean status = false;
        st.setStatus(status);
        String API_RESPONSE = "";

        long tm = System.currentTimeMillis();
        ping=tm;
        this.setPong(1);//pong=-1;
        
        try{
            //String msisdn          = requestDetails.getMsisdn();
            //String sender=senderinfo.getSender();
            String httpurl=requestDetails.keyworddetails.getUnlockurl();
            httpurl=httpurl.replaceAll("<MS>",requestDetails.getMsisdn());
            httpurl=httpurl.replaceAll("<OP>",""+requestDetails.getOperator());
            httpurl=httpurl.replaceAll("<IMEI>",requestDetails.getImei());
            httpurl=httpurl.replaceAll("<CD>",requestDetails.getGameCode());
            httpurl=httpurl.replaceAll("<PRICE>",""+requestDetails.getPrice());
            httpurl=httpurl.replaceAll("<SMS>",URLEncoder.encode(requestDetails.getMsg(),encodeFormat));
            httpurl=httpurl.replaceAll("<OPCODE>",URLEncoder.encode(""+requestDetails.getOperator(),encodeFormat));
            httpurl=httpurl.replaceAll("<SMSID>",URLEncoder.encode(""+requestDetails.getId(),encodeFormat));
            httpurl=httpurl.replaceAll("<TID>",""+requestDetails.getTid());
            httpurl=httpurl.replaceAll("<MTRID>",URLEncoder.encode(""+requestDetails.getMsgid(),encodeFormat));
            if(isvalidation)
                    httpurl=httpurl+"&validation=1";

            String apiresponse = "";
            System.out.println("URL::"+httpurl);
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            this.conng=conn;
            this.socketval=2;
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                    apiresponse += line;
            }
            rd.close();
            API_RESPONSE = apiresponse.replaceAll("'","");
            System.out.println("RESPONSE: "+API_RESPONSE);
            status=true;

        }catch(Exception e){
            e.printStackTrace();
            API_RESPONSE = e.toString();
            status=false;		
        }
        
        this.setPong(0);//pong=0;
        this.conng=null;
        long tmt = System.currentTimeMillis();

        System.out.println("Status:"+status);
        System.out.println("RESPONSE:"+API_RESPONSE);
        
        st.setStatus(status);
        st.setResponse(API_RESPONSE.trim());
        
        long tt=tmt - tm;
        reqQ.parent.updateGameTime(tt);
        st.setTt(tt);
        this.socketval=0;
        
        return st;
    }
	
    public void sentNotification(RequestDetails requestDetails,ChargeStatus cs){
        System.out.print("THREAD:" + threadID + "->ReqID:" + requestDetails.getId() + " MSISDN:" + requestDetails.getMsisdn());
        boolean status=false;
        String API_RESPONSE = "";
        int price=requestDetails.getPrice();
        
        long tm = System.currentTimeMillis();
        ping=tm;
        this.setPong(1);//pong=-1;
        
        if(cs.getStatus()!=0)
            price=0;
              
        try{
            //String msisdn       = requestDetails.getMsisdn();
            //String sender       =  senderinfo.getSender();
            String httpurl=requestDetails.keyworddetails.getUnlockurl();
            httpurl=httpurl.replaceAll("<MS>",requestDetails.getMsisdn());
            httpurl=httpurl.replaceAll("<OP>",""+requestDetails.getOperator());
            httpurl=httpurl.replaceAll("<IMEI>",requestDetails.getImei());
            httpurl=httpurl.replaceAll("<CD>",requestDetails.getGameCode());
            httpurl=httpurl.replaceAll("<PRICE>",""+price);//requestDetails.getPrice());
            httpurl=httpurl.replaceAll("<SMS>",URLEncoder.encode(requestDetails.getMsg(),encodeFormat));
            httpurl=httpurl.replaceAll("<OPCODE>",URLEncoder.encode(""+requestDetails.getOperator(),encodeFormat));
            httpurl=httpurl.replaceAll("<SMSID>",URLEncoder.encode(""+requestDetails.getId(),encodeFormat));
            httpurl=httpurl.replaceAll("<TID>",""+requestDetails.getTid());
            httpurl=httpurl.replaceAll("<MTRID>",URLEncoder.encode(""+requestDetails.getMsgid(),encodeFormat));
            httpurl=httpurl+"&notification=1";

            String apiresponse = "";
            System.out.println("URL::"+httpurl);
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            this.conng=conn;
            this.socketval=2;
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                    apiresponse += line;
            }
            rd.close();
            API_RESPONSE = apiresponse.replaceAll("'","");
            System.out.println("RESPONSE: "+API_RESPONSE);
            status=true;

        }catch(Exception e){
            e.printStackTrace();
            API_RESPONSE = e.toString();
            status=false;
        }

        this.setPong(0);//pong=0;
        this.conng=null;
        long tmt = System.currentTimeMillis();

        System.out.println("Status:"+status);
        System.out.println("RESPONSE:"+API_RESPONSE);
        
        
        long tt=tmt - tm;
        reqQ.parent.updateGameTime(tt);
        this.socketval=0;
    }
    
    public long getPing() {
    	return this.ping;
    }
    
    public long getPong() {
    	return this.pong;
    }
    
    public synchronized void setPong(int option) {
    	switch(option){
    	case 2:
            if(this.pong!=0)
                this.pong=System.currentTimeMillis();
            break;
    	case 0:
            this.pong=0;
            break;
    	case 1:
            this.pong=-1;    	    		
    	}
    }
    
    public synchronized void forceSocket() {
    	this.reqQ.parent.updateForceCouter(1);
    	try {
            switch(this.socketval) {
                case 1:
                    System.out.println("##watchdog##Charging Socket forced disconnect");
                    if(this.chrg!=null)
                        this.chrg.ChargingClose();
                    break;
                case 2:
                    System.out.println("##watchdog##Game Unlock Socket forced disconnect");
                    if(this.conng!=null)
                        this.conng.disconnect();
                    break;
            }
    	}catch(Exception e) {
            e.printStackTrace();
    	}
    }
}

