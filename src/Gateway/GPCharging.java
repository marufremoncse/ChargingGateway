/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author marufur
 */
public class GPCharging extends Charging{
    ChargeStatus cs = new ChargeStatus();
    String refid,starttm,endtm,sdate;
    String chcode="PPU0002860000117109187";
    String responseCode="",responseRefid="",responseSrvRefid="",chargedAmount="",errorCode="",errmsg="";
    public int resCode;
    float namount=0;
    SimpleDateFormat dateformate = new SimpleDateFormat();
    SimpleDateFormat endtformate = new SimpleDateFormat();
    String skey="2da7de62bf7644059edfabbd6813be8a";
    String sendasmsuri = "http://192.168.5.241:9090/digital5/messaging/v5.0/sendsms";
    String msisdn="";
    String codesms="";
    String chargecode="";
    String msgTtrid="";
    int amount=0;
    String game_name;
    int operator;
    String timestamp="";
    long tm = System.currentTimeMillis();
    long tmt;
    HttpURLConnection sconnection;
	    
    public GPCharging(String msisdn,String codesms,String chargecode,String msgTtrid,int amount,String game_name,int operator,String timestamp){       
        this.msisdn = msisdn;
        this.codesms = codesms;
        this.chargecode = chargecode;
        this.msgTtrid = msgTtrid;
        this.amount = amount;
        this.game_name = game_name;
        this.operator = operator;
        this.timestamp = timestamp;
        //charging();
    }
	
    public void charging()  {

        String nres="FAILED"; 
        Map<String,String> resValues = new HashMap<String,String>();
        
        try
        {
            //chcode = getServiceIdVal(amount);
            float mamount = amount;
            if(amount>0) {
                float percentage = (float) 27.5;  //21.75 //19.45;
                //$mdn = substr($mobile, -10);
                float aprice = amount;
                this.namount = ((percentage/100)*aprice);
                //generate refid 
            }
            Calendar cals = Calendar.getInstance();
            Date ndls = cals.getTime();

            Random rand = new Random();
            //int mrnd = (int) rand.nextInt(1000);
            this.refid = this.dateformate.format(ndls)+rand.nextInt(1000)+rand.nextInt(100);

            JSONObject json = new JSONObject();

            JSONObject item1 = new JSONObject();
            item1.put("servicekey",this.skey);
            item1.put("endUserId",msisdn);
            item1.put("accesschannel","MTSMS");
            item1.put("referenceCode",this.refid);

            json.put("accesInfo", item1);

            JSONObject item2 = new JSONObject();
            item2.put("code", this.chcode);
            item2.put("amount", mamount);
            item2.put("taxAmount", namount);
            item2.put("description", game_name);
            item2.put("currency", "BDT");

            json.put("charge", item2);

            JSONObject item3 = new JSONObject();
            item3.put("msgTransactionId", msgTtrid);
            item3.put("language", "EN");
            item3.put("senderId", "16303");
            item3.put("message", codesms);
            item3.put("msgType", "Text");
            item3.put("validity", "1");
            item3.put("deliveryReport", "1");    //deliveryReport  0->Disabled 1->Enabled

            json.put("smsInfo", item3);

            System.out.print(json.toString());
            String requestJSON = json.toString();

            //start time
            this.starttm = this.endtformate.format(ndls);
            System.out.println("start=" + this.starttm);

            //send MT reqeust to GP
            nres = send(requestJSON, msisdn);
            System.out.println("nres="+nres);

            //end time
            Calendar cale = Calendar.getInstance();
            Date ndle = cale.getTime();
            this.endtm = this.endtformate.format(ndle);
            //System.out.println("end=" + enddt);  

            JSONParser j = new JSONParser();
            JSONObject o = (JSONObject) j.parse(nres);
            JSONObject amt = (JSONObject) o.get("statusInfo");

            if (amt != null) {

                JSONObject pamount = (JSONObject) amt.get("errorInfo");
                if(pamount != null)
                {
                    //System.out.println("Error : " + pamount.get("errorCode").toString());
                    this.responseCode = amt.get("statusCode").toString();
                    this.responseRefid = amt.get("referenceCode").toString();
                    this.responseSrvRefid = amt.get("serverReferenceCode").toString();
                    this.errorCode = pamount.get("errorCode").toString();
                    this.errmsg = pamount.get("errorDescription").toString();
                    resValues.put("status", "FAILED");
                    resValues.put("refid", this.refid);
                    resValues.put("resCode", this.responseCode);
                    resValues.put("resRefid", this.responseRefid);
                    resValues.put("resSrvRefid", this.responseSrvRefid);
                    resValues.put("chargedAmount", "0");
                    resValues.put("errorCode", this.errorCode);
                    resValues.put("errorMsg", this.errmsg);
                }
                else
                {
                    this.responseCode = amt.get("statusCode").toString();
                    this.responseRefid = amt.get("referenceCode").toString();
                    this.responseSrvRefid = amt.get("serverReferenceCode").toString();
                    this.chargedAmount =  amt.get("totalAmountCharged").toString();
                    resValues.put("status", "SUCCESS");
                    resValues.put("refid", this.refid);
                    resValues.put("resCode", this.responseCode);
                    resValues.put("resRefid", this.responseRefid);
                    resValues.put("resSrvRefid", this.responseSrvRefid);
                    resValues.put("chargedAmount", this.chargedAmount);
                    resValues.put("errorCode", "0");
                    resValues.put("errorMsg", "Charge Success");
                }

            } else {   
                resValues.put("status", "FAILED");
                resValues.put("refid", this.refid);
                resValues.put("resCode", this.responseCode);
                resValues.put("resRefid", this.responseRefid);
                resValues.put("resSrvRefid", this.responseSrvRefid);
                resValues.put("chargedAmount", "0");
                resValues.put("errorCode", this.errorCode);
                resValues.put("errorMsg", this.errmsg);
            }

            resValues.put("chargeCode", this.chcode);
            resValues.put("taxAmount", Float.toString(this.namount));
            StringBuffer logdata = new StringBuffer();
            logdata.append(msisdn + "," + this.starttm + "," + this.endtm + "," + this.refid + "," + this.chcode + "," + amount + "," + this.namount + "," + game_name + "," + msgTtrid + ",16303," + codesms + ",Text," + this.sdate + "," + this.responseCode + "," + this.responseRefid + "," + this.responseSrvRefid + "," + this.chargedAmount + "," + this.errorCode + "," + this.errmsg + "' \r\n");

         //   responselog reslog = new responselog();
            System.out.println("log::"+logdata.toString());
        }
        catch(Exception e)
        { 
             System.out.println("Exception: " + e); 
             resValues.put("status", "FAILED");
             resValues.put("refid", this.refid);
             resValues.put("errorCode", "5000");
             resValues.put("errorMsg", e.getMessage());
        }       
        
        System.out.println(resValues.get("status"));
        tmt = System.currentTimeMillis();

        cs.setGWStatus(resValues.get("status"),this.operator);
        cs.setResponse(resValues.get("errorMsg"));
        cs.setTrid(resValues.get("resSrvRefid"));
        if(resValues.get("errorCode").trim().equals("51032") || resValues.get("errorCode").trim().equals("51005"))
            cs.setRetry(true);
        else
            cs.setRetry(false);

        cs.setTt(tmt - tm);         
    }
    
    //public synchronized String send(String requestJSON, String mdn) {
    public String send(String requestJSON, String mdn) {
        String resp = "";
       
  //      requestMtLog reqlog = new requestMtLog();

        StringBuffer sbData = new StringBuffer();
        sbData.append(requestJSON);
        // InputStream is = null;
         try {
             System.out.println(requestJSON);
             Map paramNameToValue = new HashMap(); // parameter name to value map

            try {
                String method = "POST";
               
                URL url = null;
                url = new URL(this.sendasmsuri);
                
                long startTime = System.currentTimeMillis();
                System.out.println("Connecting to URL - " + url);
                // open HTTPS connection
                //doTrustToCertificates();
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod(method);
                //BASE64Encoder encoder = new BASE64Encoder();
                //String encoded = encoder.encode((authentication).getBytes("UTF-8"));
                //connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);
                OutputStream dos = connection.getOutputStream();
                dos.write(requestJSON.getBytes());
		dos.flush();
                // execute HTTPS request
                int returnCode = connection.getResponseCode();
                System.out.println(returnCode);
                this.resCode = returnCode;
                InputStream connectionIn = null;
                if (returnCode == 200) {
                    connectionIn = connection.getInputStream();
                } else {
                    connectionIn = connection.getErrorStream();
                }

                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);
                // print resulting stream
                BufferedReader buffer = new BufferedReader(new InputStreamReader(connectionIn));
                String inputLine;
                while ((inputLine = buffer.readLine()) != null) {
                    //System.out.println(inputLine);
                    //resp = inputLine;
                    resp += inputLine.trim();
                }
                System.out.println(resp);
                buffer.close();
                this.sconnection = connection;
                connection.disconnect();
                   
                sbData.append("Response : ");
                sbData.append(resp);
                
            } catch (Exception e) {
                resp = "FAILED";
                sbData.append("ERROR");
                System.out.println("IOException: " + e);

            } finally {
                try {
                    //is.close();
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        //reqlog.writemtreqlog("log", sbData.toString());
        //return sbData.toString();
        return resp;
    }
    public ChargeStatus result(){
        return cs;
    }
    public void ChargingClose() {
    	try {
            this.sconnection.disconnect();
    	}catch (Exception e) {
            e.printStackTrace();
    	}
    }  
}
