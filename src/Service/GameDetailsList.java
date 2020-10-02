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
public class GameDetailsList {
  
    DBHandler dbHandler;
    Connection con;
    int keywordid;
    HashMap<String, GameDetails> hmGameDetails = new HashMap<String, GameDetails>();
    
    public GameDetailsList(DBHandler dbHandler,int keywordid) {
            // TODO Auto-generated constructor stub
             this.keywordid=keywordid;
             this.dbHandler=dbHandler;
             con=dbHandler.getDb_con();
    }
	
    public HashMap<String, GameDetails> getHmGameDetails() {
           return hmGameDetails;
    }
    
    public boolean ishmGameDetailsExistKeyword(String gamename){
    	 return hmGameDetails.containsKey(gamename);
     }
    public GameDetails getGameDetails(String gamename){
                   return hmGameDetails.get(gamename);
           }
	 
	 public void createGameDetailsList() {

	        try {
	            System.out.println("GameDetailsList HashMap Generation begins..");
	            String sql = "select * from cbs_game_details where keyword_id="+this.keywordid;
	            Statement stq=con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {
	            	
	            	GameDetails game = new GameDetails(
	                        resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getString("game_name")
	                        );
	                
	            	hmGameDetails.put(resultSet.getString("game_name"), game);
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("GameDetailsList HashMap Generation ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
		
		public synchronized void updateGameDetailsList() {

	        try {
	            System.out.println("GameDetailsList HashMap update begins..");
	            String sql = "select * from cbs_game_details where keyword_id="+this.keywordid;
	            Statement stq=con.createStatement();
	            ResultSet resultSet = stq.executeQuery(sql);
	            while (resultSet.next()) {
	            	if(hmGameDetails.containsKey(resultSet.getString("game_name"))){
	            		GameDetails game=hmGameDetails.get(resultSet.getString("game_name"));
	            		game.update(
		                resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getString("game_name")
		                );
	            	}else{
	            		
		            	GameDetails game = new GameDetails(
		                resultSet.getInt("id"),
                                resultSet.getInt("keyword_id"),
	                        resultSet.getString("game_name")
		                );
		            	System.out.println("GameDetails Information created for ID:"+resultSet.getInt("id"));
		            	hmGameDetails.put(resultSet.getString("game_name"), game);
	            	}
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("GameDetailsList HashMap update ends..");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
}
