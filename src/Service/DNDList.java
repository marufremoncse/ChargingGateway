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
public class DNDList {
    int operatorID;
    Connection mysql_con;
    HashMap<String, String> hashMapDND = new HashMap<String, String>();
    public DNDList(DBHandler dbHandler) {
		// TODO Auto-generated constructor stub
		//dbHandler = new DBHandler();
		//this.dbHandler=dbHandler;
		mysql_con=dbHandler.getDb_con();
	}
	public HashMap<String, String> getHashMapDND() {
            return hashMapDND;
	}
	
        public boolean isDNDExist(String msisdn){
            return hashMapDND.containsKey(msisdn);
	}
        
	public String getDND(String msisdn){
            return hashMapDND.get(msisdn);
	}
         
        public void createDNDList(int operatorID) {
            this.operatorID = operatorID;
            try {
                System.out.println("DND HashMap Generation begins...");
                String sql = "select * from cbs_dnd where operator="+operatorID;
                Statement stq=mysql_con.createStatement();
                ResultSet resultSet = stq.executeQuery(sql);
                while (resultSet.next()) {
                    String msisdn=resultSet.getString("msisdn");
                    hashMapDND.put(msisdn,msisdn);
                }
                stq.close();
                resultSet.close();
                System.out.println("DND HashMap Generation ends");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    
}

