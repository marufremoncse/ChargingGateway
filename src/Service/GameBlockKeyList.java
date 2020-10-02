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
public class GameBlockKeyList {
    DBHandler dbHandler;
	Connection mysql_con;
        int keyword_id;
        boolean isblockkeywordexist;

    
    HashMap<String, GameBlockKey> hmGameBlockKey = new HashMap<String, GameBlockKey>();
    //HashMap<Integer, GameBlockKey> hmGameBlockKeyPosition = new HashMap<Integer, GameBlockKey>();
    
	public GameBlockKeyList(DBHandler dbHandler,int keyword_id) {
		// TODO Auto-generated constructor stub
                this.isblockkeywordexist=false;
                this.keyword_id=keyword_id;
                this.dbHandler=dbHandler;
                mysql_con=dbHandler.getDb_con();
	}
	public boolean isIsblockkeywordexist() {
            return isblockkeywordexist;
        }
        public HashMap<String, GameBlockKey> getHmGameBlockKey() {
               return hmGameBlockKey;
        }
        public boolean ishmGameBlockKeyExistKeyword(int position,String key_word){
            return hmGameBlockKey.containsKey(""+position+":"+key_word);
        }

	public GameBlockKey getGameBlockKey(int position,String key_word){
            return hmGameBlockKey.get(""+position+":"+key_word);
        }
       // public GameBlockKey getGameBlockKeyPosition(int position){
       //     return hmGameBlockKeyPosition.get(new Integer(position));
      //  }
	
	public void createGameBlockKeyList() {

	        try {
	            System.out.println("GameBlockKeyList HashMap Generation begins...");
	            String sql = "select * from cbs_game_block_keyword where keyword_id="+this.keyword_id;
	            Statement stq=mysql_con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {
	            	
	            	GameBlockKey gameblockkey = new GameBlockKey(
	                        resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
                                resultSet.getInt("key_position"),
	                        resultSet.getString("key_value")
	                        );
	                
	            	hmGameBlockKey.put(""+resultSet.getInt("key_position")+":"+resultSet.getString("key_value"), gameblockkey);
                        //hmGameBlockKeyPosition.put(new Integer(resultSet.getInt("key_position")), gameblockkey);
                        isblockkeywordexist=true;
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("GameBlockKeyList HashMap Generation ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
		
		public synchronized void updateGameBlockKeyList() {

	        try {
	            System.out.println("GameBlockKeyList HashMap update begins..");
	            String sql = "select * from cbs_game_block_keyword where keyword_id="+this.keyword_id;
	            Statement stq=mysql_con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {
	            	if(hmGameBlockKey.containsKey(""+resultSet.getInt("key_position")+":"+resultSet.getString("key_value"))){
	            		GameBlockKey gameblockkey=hmGameBlockKey.get(""+resultSet.getInt("key_position")+":"+resultSet.getString("key_value"));
	            		gameblockkey.update(
		                resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getInt("key_position"),
	                        resultSet.getString("key_value")
		                );
	            	}else{
	            		
		            	GameBlockKey gameblockkey = new GameBlockKey(
		                resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getInt("key_position"),
	                        resultSet.getString("key_value")
		                );
		            	System.out.println("GameBlockKey Information created for ID:"+resultSet.getInt("id"));
		            	hmGameBlockKey.put(""+resultSet.getInt("key_position")+":"+resultSet.getString("key_value"), gameblockkey);
	            	}
                        isblockkeywordexist=true;
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("GameBlockKeyList HashMap update ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
}
