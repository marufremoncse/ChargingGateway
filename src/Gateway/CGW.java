/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import DBHandler.DBHandler;
import Service.ChargeConf;
import Service.ChargeConfList;
import Service.DNDList;
import Service.KeywordChargeConfList;
import Service.KeywordDetailsList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author marufur
 */
public class CGW {

    long serverResponseTime;
    long operatorResponseTime;
    long gameResponseTime;
    int numOfReq,numOfReqp,numOfForced;
    
    Connection con_conf, con_op;
    
    Vector<RequestQueue> reqQList = new Vector<RequestQueue>();
    Vector<RequestProcessor> reqT = new Vector<RequestProcessor>();    
    
    ChargeConfList chargeConfList;
    KeywordDetailsList keyworddetailsList;
    KeywordChargeConfList keywordChargeConfList;
    DNDList dndList;
    DBHandler dbHandler_conf,dbHandler_op;
    int operator;
    int numThread = 2;
    public int maxrowcount = 500;

    public CGW(int operator) {
      
        this.serverResponseTime=0;
        this.operatorResponseTime=0;
        this.gameResponseTime=0;
        this.numOfReq=0;
        this.numOfReqp=0;
        this.numOfForced=0;
    
        this.operator = operator;
    	dbHandler_conf = new DBHandler("conf");
        dbHandler_op = new DBHandler("op");
    	chargeConfList = new ChargeConfList(dbHandler_conf);
    	chargeConfList.createChargeConfList(this.operator);
    	keyworddetailsList = new KeywordDetailsList(dbHandler_conf);
    	keyworddetailsList.createKeywordDetailsList(this.operator);
        keywordChargeConfList = new KeywordChargeConfList(dbHandler_conf,this.operator);
        keywordChargeConfList.createKeywordChargeConfList();
        dndList = new DNDList(dbHandler_op);
        dndList.createDNDList(this.operator);
    	con_conf = dbHandler_conf.getDb_con();
        con_op = dbHandler_op.getDb_con();

        boolean st = this.isRunning();
        System.out.println("Status: "+ st);
        if(!this.isRunning()){
            this.setRunningStartStopTime(true);
  	
            while (this.isRunning()) {
                this.numThread = this.getNumberOfThread();
                queueProcess();
                try {
                    Thread.sleep(1000);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            this.setRunningStartStopTime(false);
            System.out.println("Command: Request for application close status in database: Exited");
            try {
                dbHandler_conf.closeConnection();
                dbHandler_op.closeConnection();
                //timesten_conn.close();
                System.out.println("Database Connection closed");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
            System.out.println("Error: Already running instance or invalid instrance status in database: Exited");
        }
    }

    public void queueProcess() {
        try {           
            reqQList.clear();
            reqT.clear();
            for (int i = 0; i < this.numThread; i++) {
                RequestQueue req = new RequestQueue(this);
                reqQList.add(i, req);
            }
            System.out.println("queueProcess Run: Num of Thread:" + numThread);
            long lastid=0;
            
            String sql = "SELECT id,msisdn,sms_text,shortcode,msgid from cbs_sms_receive where status='N' and operator="+this.operator+" order by msisdn";//id";
            Statement stsql = con_op.createStatement();         
            ResultSet resultSet = stsql.executeQuery(sql);
            System.out.println(sql);
            
            int j = 0,count=0;
            int lastthreadid=0;
            String lastmsisdn="";
            
            Statement stsqlu = con_op.createStatement();
	    boolean iscontinue=true;
            
            while (iscontinue && resultSet.next()) {            
                //System.out.println(":::::::::::::::::::::::");
                long id = resultSet.getLong("id");
                String msisdn = resultSet.getString("msisdn");
                String smstext = resultSet.getString("sms_text");
                int shortcode=resultSet.getInt("shortcode");
                String msgid = resultSet.getString("msgid");
                
                //System.out.println(msisdn+" "+smstext);
                
                RequestDetails reqd = new RequestDetails(id,msisdn,smstext,shortcode,msgid,this.operator,this);
                if(msisdn.equals(lastmsisdn)){
                    j = lastthreadid;
                    System.out.println("Consecutive duplicate msisdn:"+msisdn);
                }
                reqQList.get(j).addToQueue(reqd);
                lastthreadid=j;
                lastmsisdn=msisdn;
                if (++j >= this.numThread) {
                    j = 0;
                }
                
                String sqlu ="update cbs_sms_receive set status='P',pro_date=sysdate where id="+id;           
                stsqlu.executeUpdate(sqlu);
               
                count++;
		if(count>this.maxrowcount)
                    iscontinue=false;
            }
            stsqlu.close();
            resultSet.close();
            stsql.close();
            if(count>0){
                for (int k = 0; k < this.numThread; k++) {
                    RequestProcessor reqt = new RequestProcessor(reqQList.get(k), k);
                    reqt.start();
                    reqT.add(k, reqt);
                }
	
                /*for (int t = 0; t < this.numThread; t++) {
                    try {
                        reqT.get(t).join();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }*/
                WatchDog wdog=new WatchDog(this,numThread);
	            wdog.start();
	            try {
                	if(wdog.isAlive())
                		wdog.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
            	System.out.println("No Data");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    
    public synchronized boolean isExpired(RequestDetails requestDetails){
        String returncode="";
        boolean isexpired=true;
        try{    
            String sqlvalidity="select return_code,price from cbs_subscribers where msisdn='"+requestDetails.getMsisdn()+"' and operator="+requestDetails.getOperator()+" and keyword_id="+requestDetails.getKeywordid();//+" and expiry_date>trunc(sysdate()) ";
            if(requestDetails.getIdentifier().length()>1)
                sqlvalidity+=" and identifier="+requestDetails.getIdentifier();
            if(requestDetails.getSubidentifier().length()>1)
                sqlvalidity+=" and sub_identifier="+requestDetails.getSubidentifier();
            if(requestDetails.isIsimeiExist())
                sqlvalidity+=" and imei='"+requestDetails.getImei()+"'";
            if(requestDetails.isIsgameidExist())
                sqlvalidity+=" and game_id='"+requestDetails.getGameid()+"'";
            System.out.println("isExpired query::"+sqlvalidity);
            Statement stsql = con_op.createStatement();
            ResultSet resultSet = stsql.executeQuery(sqlvalidity);
            if (resultSet.next()) {
                returncode=resultSet.getString("return_code");
                requestDetails.setPrice(resultSet.getInt("price"));
                requestDetails.setReturncode(returncode);
                isexpired = false;
            }
            stsql.close();
            resultSet.close();
	            		            	
    	}catch(SQLException se){
                se.printStackTrace();
                
        }
        return isexpired;
    }
    
    public synchronized boolean insertSubWithCode(RequestDetails requestDetails){
    	try {
            long id=requestDetails.getId();
            String msisdn=requestDetails.getMsisdn();
            String imei="";
            if(requestDetails.isIsimeiExist())
                imei=requestDetails.getImei();
            String returncode=requestDetails.getReturncode();
            if(returncode.length()>100)
            	returncode=returncode.substring(0,20);
            int keywordid=requestDetails.getKeywordid();
            int price=requestDetails.getPrice();
            float charge=requestDetails.getChargeamount();
            int validity=requestDetails.getValidity();
            int gameid=0;
            if(requestDetails.isIsgameidExist())
                gameid=requestDetails.getGameid();
            String identifier="''";
            if(requestDetails.getIdentifier().length()>1)
                identifier=requestDetails.getIdentifier();
            String subidentifier="''";
            if(requestDetails.getSubidentifier().length()>1)
                subidentifier=requestDetails.getSubidentifier();
            int supplementgameid=0;
            if(requestDetails.isIssupplementkeyExist())
                supplementgameid=requestDetails.getSuplementgameid();
            
	        String sqli = "insert into cbs_subscribers(msisdn,imei,identifier,sub_identifier,operator,keyword_id,game_id,return_code,price,charged_amount,last_update,validity,expiry_date)"
	        		+ "values('"+msisdn+"','"+imei+"',"+identifier+","+subidentifier+","+this.operator+","+keywordid+","+gameid+",'"+returncode+"',"+price+","+charge+",sysdate,"+validity+",trunc(sysdate+"+validity+"+1))";
            
	        System.out.println("Subscriber insert query::"+sqli);
	        Statement stsqli = con_op.createStatement();
	        
            	stsqli.executeUpdate(sqli);
            
                stsqli.close();
            
            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateSubWithCode(RequestDetails requestDetails){
    	try {
            //long id=requestDetails.getId();
            String msisdn=requestDetails.getMsisdn();
            //String imei=requestDetails.getImei();
            String returncode=requestDetails.getReturncode();
            if(returncode.length()>100)
            	returncode=returncode.substring(0,20);
            int keywordid=requestDetails.getKeywordid();
         	        String sqli = "update cbs_subscribers set return_code='"+returncode+"',last_update=sysdate where msisdn='"+msisdn+"' and operator="+this.operator+" and game_id="+keywordid;
                if(requestDetails.getIdentifier().length()>1)
                    sqli+=" and identifier="+requestDetails.getIdentifier();
                if(requestDetails.getSubidentifier().length()>1)
                    sqli+=" and sub_identifier="+requestDetails.getSubidentifier();
                if(requestDetails.isIsimeiExist())
                    sqli+=" and imei='"+requestDetails.getImei()+"'";
                if(requestDetails.isIsgameidExist())
                    sqli+=" and game_id='"+requestDetails.getGameid()+"'";
                
	        System.out.println("Subscriber update query::"+sqli);
	        Statement stsqli = con_op.createStatement();
            	stsqli.executeUpdate(sqli);
                stsqli.close();            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
//     public synchronized boolean insertDeviceLog(RequestDetails requestDetails){
      public boolean insertDeviceLog(RequestDetails requestDetails){
    	try {
            long id=requestDetails.getId();
            String msisdn=requestDetails.getMsisdn();
            String imei="";
            if(requestDetails.isIsimeiExist())
                imei=requestDetails.getImei();
            String imsi="";
            if(requestDetails.isIsimsiExist())
            	imsi=requestDetails.getImsi();
            String devicename="";
            if(requestDetails.isDeviceNameExist)
                devicename=requestDetails.getDeviceName();
            String devicemodel="";
            if(requestDetails.isDeviceModelExist)
            	devicemodel=requestDetails.getDeviceModel();           
            
            String sqli = "insert into cbs_device_log(msisdn,imei,device_name,model,sms_id,imsi) "
                            + "values('"+msisdn+"','"+imei+"','"+devicename+"','"+devicemodel+"',"+id+",'"+imsi+"')";
            
	        System.out.println("Device log insert query::"+sqli);
	        Statement stsqli = con_op.createStatement();
	        
            	stsqli.executeUpdate(sqli);
            
                stsqli.close();
            
            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
     
    public synchronized int insertGameDetails(RequestDetails requestDetails){
        int gameid=0;
        int keywordid=requestDetails.getKeywordid();
        String gamename=requestDetails.getGamename();
        ResultSet rs=null;
        Statement stsqli=null;   

    	try {            
            
	        String sqli = "insert into cbs_game_details(keyword_id,game_name) "
	        		+ "values("+keywordid+",'"+gamename+"')";
            
	        System.out.println("Game Details insert query::"+sqli);
	        stsqli = con_op.createStatement();
	        
            	stsqli.executeUpdate(sqli,Statement.RETURN_GENERATED_KEYS);
                rs= stsqli.getGeneratedKeys();
                
                if(rs.next())
                        gameid=rs.getInt(1);
                rs.close();
            
                stsqli.close();
                         
            
            return gameid;
        } catch (SQLException se) {
            //se.printStackTrace();
            try {
                    rs.close();
                    stsqli.close();
            }catch (SQLException s) {
        		
             }
            requestDetails.keyworddetails.getgamelist().updateGameDetailsList();
            if(requestDetails.keyworddetails.getgamelist().ishmGameDetailsExistKeyword(gamename))
                return requestDetails.keyworddetails.getgamelist().getGameDetails(gamename).getId();
            else
                return gameid;            
        }
    }
     
//     public synchronized boolean insertUnlockCodeLog(RequestDetails requestDetails,String response,long opduration){
    public boolean insertUnlockCodeLog(RequestDetails requestDetails,String response,long opduration){
    	try {
            long id=requestDetails.getId();
            String msisdn=requestDetails.getMsisdn();
            String imei="";
            if(requestDetails.isIsimeiExist())
                imei=requestDetails.getImei();
            int keywordid=requestDetails.getKeywordid();
            int gameid=0;
            if(requestDetails.isIsgameidExist())
                gameid=requestDetails.getGameid();
            String gamename=requestDetails.getGamename();
            
	        String sqli = "insert into cbs_unlock_code_log(msisdn,imei,keyword_id,game_id,game_name,sms_id,opduration,response) "
	        		+ "values('"+msisdn+"','"+imei+"',"+keywordid+","+gameid+",'"+gamename+"',"+id+","+opduration+",'"+response+"')";
            
	        System.out.println("Unlock Code log insert query::"+sqli);
	        Statement stsqli = con_op.createStatement();
	        
            	stsqli.executeUpdate(sqli);
            
                stsqli.close();
            
            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
//    public synchronized boolean insertChargeSuccesslog(RequestDetails requestDetails,ChargeStatus cs,ChargeConf cc) {
   
    public boolean insertChargeSuccesslog(RequestDetails requestDetails,ChargeStatus cs,ChargeConf cc) {
        try {
            int amount=cc.getPrice();
            if(amount!=0){
                String msisdn=requestDetails.getMsisdn();
                String imei="";
                if(requestDetails.isIsimeiExist())
                    imei=requestDetails.getImei();
                int keywordid=requestDetails.getKeywordid();
                float amountwithvat=cc.getPriceWithVat();
                int validity=cc.getValidity();
                int chargeid=cc.getId();
                String tidref=cs.getTrid();
                long opduration=cs.getTt();
                String response=cs.getResponse();
                int gameid=0;
                if(requestDetails.isIsgameidExist())
                    gameid=requestDetails.getGameid();
                long smsid=requestDetails.getId();

                String sqli = "insert into cbs_charge_success_log(msisdn,imei,keyword_id,game_id,operator,amount,amount_with_vat,validity,charge_id,tid_ref,opduration,response,sms_id) "
                                + "values('"+msisdn+"','"+imei+"',"+keywordid+","+gameid+","+this.operator+","+amount+","+amountwithvat+","+validity+","+chargeid+",'"+tidref+"',"+opduration+",'"+response+"',"+smsid+")";

                System.out.println("Charge Success insert query::"+sqli);
                Statement stsqli = con_op.createStatement();
                stsqli.executeUpdate(sqli);
                stsqli.close();
            }    
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
//    public synchronized boolean insertChargeFaillog(RequestDetails requestDetails,ChargeStatus cs,ChargeConf cc) {
    public boolean insertChargeFaillog(RequestDetails requestDetails,ChargeStatus cs,ChargeConf cc) {
        try {
        	
            String msisdn=requestDetails.getMsisdn();
            String imei="";
            if(requestDetails.isIsimeiExist())
                imei=requestDetails.getImei();
            int keywordid=requestDetails.getKeywordid();
            int amount=cc.getPrice();
            int chargeid=cc.getId();
            int failcode=cs.getStatus();
            String tidref=cs.getTrid();
            long opduration=cs.getTt();
            String response=cs.getResponse();
            int gameid=0;
            if(requestDetails.isIsgameidExist())
                gameid=requestDetails.getGameid();
            long smsid=requestDetails.getId();
            
            String sqli = "insert into cbs_charge_fail_log(msisdn,imei,keyword_id,game_id,operator,amount,charge_id,fail_code,tid_ref,opduration,response,sms_id) "
                            + "values('"+msisdn+"','"+imei+"',"+keywordid+","+gameid+","+this.operator+","+amount+","+chargeid+","+failcode+",'"+tidref+"',"+opduration+",'"+response+"',"+smsid+")";

            System.out.println("Charge Success insert query::"+sqli);
            Statement stsqli = con_op.createStatement();
            stsqli.executeUpdate(sqli);
            stsqli.close();
            
            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
//  public synchronized boolean insertSMSSent(RequestDetails requestDetails,String replysms) {
    public boolean insertSMSSent(RequestDetails requestDetails,String replysms,int rcode) {
        try {
            long id=requestDetails.getId();
            String msisdn=requestDetails.getMsisdn();
            int shortcode=requestDetails.getShortCode();
            
            if(replysms !=null &&  replysms.trim().length()>1){
            
	        String sqli = "insert into cbs_sms_send(msisdn,sms_text,reply_addr,operator,sms_id,rcode) "
	        		+ "values('"+msisdn+"','"+replysms+"','"+shortcode+"','"+this.operator+"','"+id+"','"+rcode+"')";
            
	        System.out.println("SMS insert query::"+sqli);
	        Statement stsqli = con_op.createStatement();
	        
            	stsqli.executeUpdate(sqli);
            
                stsqli.close();
            }
            
            
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
    public synchronized boolean insertRequestDetails(RequestDetails requestDetails){
        try {
            long sms_id = requestDetails.getId();
            String msisdn = requestDetails.getMsisdn();
            String msg = requestDetails.getMsg();        
            int keyword_id = requestDetails.getKeywordid();
            int charge_conf_id = requestDetails.getChargeConfID();
            int operator = requestDetails.getOperator();
            int short_code = requestDetails.getShortCode();
            int price = requestDetails.getPrice(); 
            int validity = requestDetails.getValidity();   
            String unlocksms = requestDetails.getUnlocksms(); 
            String identifier = requestDetails.getIdentifier();
            String subidentifier = requestDetails.getSubidentifier();
            int game_id = requestDetails.getGameid();
            int supplement_game_id = requestDetails.getSuplementgameid();
            String keyword = requestDetails.getKeywordname();
            String game_name = requestDetails.getGamename();
            String msgid = requestDetails.getMsgid();

            String extra = requestDetails.getExtra();        
            String tid = requestDetails.getTid();
            String imei = requestDetails.getImei();
            String imsi = requestDetails.getImsi();
            String sms_key = requestDetails.getSmsKey();
            String game_code = requestDetails.getGameCode();       
            String device_name = requestDetails.getDeviceName();
            String device_model = requestDetails.getDeviceModel();

            identifier = identifier.replaceAll("'", "");

            String sql = "INSERT INTO cbs_request_details (sms_id,msisdn,msg,keyword_id,charge_conf_id,operator,short_code,price,validity,unlocksms,extra,tid,imei,imsi,sms_key,game_code,device_name,device_model,identifier,subidentifier,"+
                "game_id,supplement_game_id,keyword,game_name,msgid)\nVALUES('" + sms_id + "','" + msisdn + "','" + msg + "','" + keyword_id + "','" + charge_conf_id + "','" + operator + "','" + short_code + "','" + price + "','" + validity +
                "','" + unlocksms + "','" + extra + "','" + tid + "','" + imei + "','" + imsi + "','" + sms_key + "','" + game_code + "','" + device_name + "','" + device_model + "','" + identifier + "','" + subidentifier + 
                "','" + game_id + "','" + supplement_game_id + "','" + keyword + "','" + game_name + "','" + msgid + "')";                              

            Statement stsql = con_op.createStatement();  
            System.err.println(sql);
            stsql.execute(sql);
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    
    public int getNumberOfThread() {        
        int noOfThread = 1;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "select number_of_thread,max_row_count from cbs_app_conf where id=1 and operator=" + operator;
        System.out.println("getting number of thread: " + sql);
        try {
            statement = con_op.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                noOfThread = resultSet.getInt("number_of_thread");
		this.maxrowcount = resultSet.getInt("max_row_count");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
            return noOfThread;
        } finally {
            try {
                resultSet.close();
                statement.close();
            } catch (Exception e) {
            }
        }
        return noOfThread;
    }    
    
    public void setRunningStartStopTime(boolean status) {
       
        Statement statement = null;
        
        String sql = "update cbs_app_conf set last_start_time=sysdate,status=1 where id=1 and operator=" + operator;
        if(!status)
            sql = "update cbs_app_conf set last_stop_time=sysdate where id=1 and operator=" + operator;
        System.out.println("Update Running Start Stop Time: " + sql);
        try {
            statement = con_op.createStatement();
            statement.execute(sql);            
        }
        catch (SQLException ex) {
            ex.printStackTrace();           
        } 
        finally {
            try {                
                statement.close();
            } 
            catch (Exception e) {
            }
        }
        
    }
    
    public synchronized void updateReqCouter(int req) {
    	this.numOfReqp+=req;
    }
    public synchronized void updateForceCouter(int req) {
    	this.numOfForced+=req;
    }
    public synchronized void updateProcessTime(long protime) {
    	this.serverResponseTime+=protime;
    }
    public synchronized void updateOperatorTime(long protime) {
    	this.operatorResponseTime+=protime;
    }
    public synchronized void updateGameTime(long protime) {
    	this.gameResponseTime+=protime;
    }
    public synchronized boolean insertProcessLog(){
    	try {
            String sqli = "insert into iat_process_log(total_request_q,total_request_p,process_time,operator_time,game_time,pro_id,total_forced) "
	        		+ "values("+this.numOfReq+","+this.numOfReqp+","+this.serverResponseTime+","+this.operatorResponseTime+","+this.gameResponseTime+",1,"+this.numOfForced+")";
            
            System.out.println("process log insert query::"+sqli);
            Statement stsqli = con_op.createStatement();
            stsqli.executeUpdate(sqli);
            stsqli.close();
            this.numOfReq=0;
            this.numOfForced=0;
            this.numOfReqp=0;
            this.serverResponseTime=0;
            this.operatorResponseTime=0;
            this.gameResponseTime=0;
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public boolean isRunning() {
        boolean status = true;
        int status_i = 1;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "select status from cbs_app_conf where  id = 1 and operator = " + operator;
        System.out.println("checking if Running: " + sql);
        try {
            statement = con_op.createStatement();
            resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                status_i = resultSet.getInt("status");
            }
        } 
        catch (SQLException ex) {
            ex.printStackTrace();            
            return status;
        }
        finally {
            try {
                resultSet.close();
                statement.close();
            } catch (Exception e) {
            }
        }
        if(status_i==0)
            status=false;
        return status;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("Charging Platform");      
            new CGW(3);//1 for BL, 2 for GP, 3 for Robi/Airtel, 4 for Teletalk        
    }	    
}
