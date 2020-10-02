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
public class KeywordChargeConfList {
 
    DBHandler dbHandler;
    Connection con;
    int operator;
    
    HashMap<Integer, KeywordChargeConf> hmKeywordChargeConf = new HashMap<Integer, KeywordChargeConf>();
    
	public KeywordChargeConfList(DBHandler dbHandler,int operator) {
            // TODO Auto-generated constructor stub
            this.operator=operator;
            this.dbHandler=dbHandler;
            con=dbHandler.getDb_con();
	}
	
        public HashMap<Integer, KeywordChargeConf> gethmKeywordChargeConf() {
            return hmKeywordChargeConf;
        }
        public boolean ishmKeywordChargeConfExist(int keyword_id){
            return hmKeywordChargeConf.containsKey(new Integer(keyword_id));
        }
	 public KeywordChargeConf getKeywordChargeConf(int keyword_id){
			return hmKeywordChargeConf.get(new Integer(keyword_id));
		}
	 
	 public void createKeywordChargeConfList() {

	        try {
	            System.out.println("KeywordChargeConfList HashMap Generation begins...");
	            String sql = "select * from cbs_keyword_charge_conf where operator="+operator;
	            Statement stq=con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {
	            	
	            	KeywordChargeConf keywordChargeConf = new KeywordChargeConf(
	                        resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getInt("operator"),
                                resultSet.getString("charge_steps")                       	                        
	                        );
	                
	            	hmKeywordChargeConf.put(new Integer(resultSet.getInt("keyword_id")), keywordChargeConf);
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("KeywordChargeConfList HashMap Generation ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
		
    public synchronized void updateKeywordChargeConfList() {

        try {
            System.out.println("KeywordChargeConfList HashMap update begins..");
            String sql = "select * from cbs_keyword_charge_conf where operator="+operator;
            Statement stq=con.createStatement();
            ResultSet resultSet = stq.executeQuery(sql);
            while (resultSet.next()) {
                if(hmKeywordChargeConf.containsKey(new Integer(resultSet.getInt("keyword_id")))){
                        KeywordChargeConf keywordChargeConf=hmKeywordChargeConf.get(new Integer(resultSet.getInt("keyword_id")));
                        keywordChargeConf.update(
                            resultSet.getInt("id"),
                            resultSet.getInt("keyword_id"),
                            resultSet.getInt("operator"),
                            resultSet.getString("charge_steps")
                        );
                }else{

                        KeywordChargeConf keywordChargeConfList = new KeywordChargeConf(
                            resultSet.getInt("id"),
                            resultSet.getInt("keyword_id"),
                            resultSet.getInt("operator"),
                            resultSet.getString("charge_steps")
                        );
                        System.out.println("KeywordChargeConfList Information created for ID:"+resultSet.getInt("id"));
                        hmKeywordChargeConf.put(new Integer(resultSet.getInt("keyword_id")), keywordChargeConfList);
                }
            }
            stq.close();
            resultSet.close();
            System.out.println("KeywordChargeConfList HashMap update ends..");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
