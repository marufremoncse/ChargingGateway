/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import DBHandler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
*
* @author marufur
*/

public class ChargeConfList {
    
    DBHandler dbHandler_conf;
    int operatorid;
    Connection con;
	  
    HashMap<Integer, ChargeConf> hmchrgconf = new HashMap<Integer, ChargeConf>();
    HashMap<Integer, ChargeConf> hmchrgconfwithprice = new HashMap<Integer, ChargeConf>();
	    
	public ChargeConfList(DBHandler dbHandler) {
		this.dbHandler_conf=dbHandler;
                con=dbHandler.getDb_con();
	}
        
	public HashMap<Integer, ChargeConf> getHmchrgConf() {
            return hmchrgconf;
        }
        
        public boolean isChargingConfExist(int chargeid){
            return hmchrgconf.containsKey(new Integer(chargeid));
        }
        
        public ChargeConf getChargingConf(int chargeid){
            return hmchrgconf.get(new Integer(chargeid));
        }
         
        public HashMap<Integer, ChargeConf> getHmChargeConfWithPrice() {
            return hmchrgconfwithprice;
        }
        
	public boolean isChargingConfWithPriceExist(int price){
            return hmchrgconfwithprice.containsKey(new Integer(price));
	}
        
	public ChargeConf getChargingConfWithPrice(int price){
            return hmchrgconfwithprice.get(new Integer(price));
	}

        public void createChargeConfList(int operatorid) {
	    		this.operatorid=operatorid;
	        try {
	            System.out.println("ChargeConf HashMap Generation begins...");
	            String sql = "select * from cbs_charge_conf where operator="+operatorid;
	            Statement stq=con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {

	                ChargeConf charge = new ChargeConf(
	                        resultSet.getInt("id"),
	                        resultSet.getString("charge_code"),
	                        resultSet.getInt("price"),
	                        resultSet.getFloat("price_with_vat"),
	                        resultSet.getInt("operator"),
                                resultSet.getInt("validity")
	                        );
	                
	                hmchrgconf.put(new Integer(resultSet.getInt("id")), charge);
                        hmchrgconfwithprice.put(new Integer(resultSet.getInt("price")), charge);
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("ChargeConf HashMap Generation ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
        public synchronized void updateChargeConfList(int operatorid) {
    		if(this.operatorid!=operatorid)
    			hmchrgconf.clear();
        try {
            System.out.println("ChargeConf HashMap update begins..");
            String sql = "select * from cbs_charge_conf where operator="+operatorid;
            Statement stq = con.createStatement();
            ResultSet resultSet = stq.executeQuery(sql);
            while (resultSet.next()) {
            	
            	if(hmchrgconf.containsKey(new Integer(resultSet.getInt("id")))){
            		ChargeConf charge = hmchrgconf.get(new Integer(resultSet.getInt("id")));
            		charge.update(
	                        resultSet.getInt("id"),
	                        resultSet.getString("charge_code"),
	                        resultSet.getInt("price"),
	                        resultSet.getFloat("price_with_vat"),
	                        resultSet.getInt("operator"),
                                resultSet.getInt("validity")
	                        );
            	}else{

	                ChargeConf charge = new ChargeConf(
	                        resultSet.getInt("id"),
	                        resultSet.getString("charge_code"),
	                        resultSet.getInt("price"),
	                        resultSet.getFloat("price_with_vat"),
	                        resultSet.getInt("operator"),
                                resultSet.getInt("validity")
	                        );
	                System.out.println("Charge Conf created for ID:"+resultSet.getInt("id"));
	                hmchrgconf.put(new Integer(resultSet.getInt("id")), charge);
                        hmchrgconfwithprice.put(new Integer(resultSet.getInt("price")), charge);
            	}
            }
            stq.close();
            resultSet.close();
            System.out.println("ChargeConf HashMap update ends..");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int getOperatorid() {
        return operatorid;
    }
    public void setChargerouteid(int operatorid) {
        this.operatorid = operatorid;
    }    
}

