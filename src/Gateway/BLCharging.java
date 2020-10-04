/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author marufur
 */
public class BLCharging extends Charging{
    ChargeStatus cs = new ChargeStatus();
    String applicationname = "CGW.Gateway.CGW";
    String charging_url ;
    
    String trid="";
    String msgTtrid="";
    String resultcode="-1";
    String resultdesc="";	
    String response="";
    String CHARGE_RESPONSE="";
    
    String msisdn="";
    String codesms = "";
    String chargecode="";
    int amount=0;
    String game_name;
    int operator;
    String timestamp="";
    long tm = System.currentTimeMillis();
    long tmt;
    HttpURLConnection sconnection;
    
    public BLCharging(String msisdn,String codesms,String chargecode,String msgTtrid,int amount,String game_name,int operator,String timestamp){       
        this.msisdn = msisdn;
        this.codesms = codesms;
        this.chargecode =chargecode;
        this.amount = amount;
        this.game_name = game_name;
        this.operator = operator;
        this.timestamp = timestamp;
        charging();
    }
    
    public void charging(){
        try {
            charging_url = "http://192.168.102.36/bl_charge/blChargingWrapper.php?cpname=Symphony&cppin=symphony&tid=" + timestamp + "&msisdn=" + msisdn+ "&chargecode=" + chargecode + "&type=ondemand";
            System.out.println(charging_url);
            String chargeresponse = "";            
            URL url = new URL(charging_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            this.sconnection = connection;
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                chargeresponse += line;
            }
            rd.close();
            CHARGE_RESPONSE = chargeresponse;
        } catch (Exception e) {
            e.printStackTrace();
            CHARGE_RESPONSE = e.toString();
        }
        
        System.out.println(CHARGE_RESPONSE);
        tmt = System.currentTimeMillis();
        
        DecodeResponse(CHARGE_RESPONSE);
        if(decode()){
            cs.setGWStatus(getResultCode(),operator);
            cs.setTrid(getTrid());
            cs.setResponse(getResponse());            
        }else{
            cs.setGWStatus("1000",operator);
            cs.setResponse(getResponse());
            cs.setTrid(getTrid());
            
        }
        cs.setTt(tmt - tm);
    }
        
	
    public void DecodeResponse(String response){
        this.response=response;		
    }
    public String getResultCode(){
        return resultcode;
    }
    public String getTrid() {
        return trid;
    }
    public String getResultdesc() {
        return resultdesc;
    }
    public String getResponse() {
        return response;
    }
    public boolean decode(){
        try{			
            String[] res=response.split("::");

            if(res.length==3){
                this.trid=res[0].trim();
                this.resultcode=res[1].trim();
                this.resultdesc=res[2].trim();
                System.out.println(trid+":"+resultcode+":"+resultdesc);
                return true;
            }else
                return false;		

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(trid+":"+resultcode+":"+resultdesc);
            return false;
        }
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
