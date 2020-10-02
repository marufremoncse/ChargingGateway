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
public class KeywordDetailsList {
    DBHandler dbHandler_conf;
    DBHandler dbHandler_op = new DBHandler("op");
    Connection con;
    HashMap<String, KeywordDetails> hmKeywordDetails = new HashMap<String, KeywordDetails>();
    HashMap<String, GameSupplementKeyword> hmGameSupKeyDetails = new HashMap<String, GameSupplementKeyword>();
    
    public KeywordDetailsList(DBHandler dbHandler) {
        // TODO Auto-generated constructor stub
        this.dbHandler_conf=dbHandler;
        con=dbHandler.getDb_con();
    }
    public HashMap<String, KeywordDetails> getHmKeywordDetails() {
           return hmKeywordDetails;
    }
    public HashMap<String, GameSupplementKeyword> getHmGameSupKeyDetails() {
           return hmGameSupKeyDetails;
    }        
    public boolean ishmKeywordDetailsExistKeyword(String key_words){
        return hmKeywordDetails.containsKey(key_words);
    }
    public boolean ishmGameSupKeyDetailsExistKeyword(String key_words){
        return hmGameSupKeyDetails.containsKey(key_words);
    }
    public KeywordDetails getKeywordDetails(String key_words){
        return hmKeywordDetails.get(key_words);
    }	 
    public GameSupplementKeyword getGameSupKeyDetails(String key_words){
        return hmGameSupKeyDetails.get(key_words);
    }
    public void createKeywordDetailsList(int operatorid) {

        try {
            System.out.println("KeywordDetailsList HashMap Generation begins...");
            String sql = "select * from cbs_keyword_details";
            Statement stq=con.createStatement();
            ResultSet resultSet = stq.executeQuery(sql);
            while (resultSet.next()) {	            	
                KeywordDetails keyword = new KeywordDetails(
                resultSet.getInt("id"),
                resultSet.getString("key_word"),
                resultSet.getString("game_type"),
                resultSet.getString("company_name"),
                resultSet.getString("unique_game_identifier"),
                resultSet.getString("unique_sub_identifier"),
                resultSet.getString("sms_spliter"),
                resultSet.getInt("msisdn_pos"),
                resultSet.getInt("imei_pos"),
                resultSet.getInt("code_pos"),
                resultSet.getInt("game_name_pos"),
                resultSet.getInt("device_name_pos"),
                resultSet.getInt("device_model_pos"),
                resultSet.getInt("same_key_valid"),
                resultSet.getInt("reply_game_name_source"),
                resultSet.getString("unlockurl"),
                resultSet.getString("unlockurl_response_splitter"),
                resultSet.getString("unlock_sms"),
                resultSet.getInt("url_resp_gamename_pos"),
                resultSet.getInt("url_resp_unlockcode_pos"),
                resultSet.getInt("url_resp_price_pos"),
                resultSet.getString("charge_sms_success"),
                resultSet.getString("charge_sms_fail"),
                resultSet.getInt("device_log"),
                resultSet.getInt("unlockcode_log"),
                resultSet.getString("ins_date"),
                resultSet.getInt("validation"),
                resultSet.getInt("notifications"),
                resultSet.getString("charge_sms_error"),
                resultSet.getInt("url_resp_imei_pos"),
                resultSet.getInt("url_resp_imsi_pos"),
                resultSet.getInt("url_resp_brand_pos"),
                resultSet.getInt("url_resp_model_pos"),
                resultSet.getInt("sync")
            );
                        
                        if(keyword.getgamenamepos()>-1){
                            GameDetailsList gamelist = new GameDetailsList(dbHandler_op,resultSet.getInt("id"));
                            gamelist.createGameDetailsList();
                            keyword.setgamelist(gamelist);
                        }else{
                            keyword.setgamelist(null);
                        }
                        GameBlockKeyList gameblockkeylist=new GameBlockKeyList(dbHandler_op,resultSet.getInt("id"));
                        gameblockkeylist.createGameBlockKeyList();
                        keyword.setGameblockkeylist(gameblockkeylist);
	                
	            	hmKeywordDetails.put(resultSet.getString("key_word"), keyword);
	            }
	            stq.close();
	            resultSet.close();
	            System.out.println("KeywordDetailsList HashMap Generation ends..");
                    createGameSpplementKeywordList(operatorid);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
              
    public void createGameSpplementKeywordList(int operatorid) {

           try {
               System.out.println("GameSpplementKeywordList HashMap Generation begins..");
               String sql = "select * from cbs_keyword_supplement where operator="+operatorid;
               Statement stq=con.createStatement();
               ResultSet resultSet = stq.executeQuery(sql);
               while (resultSet.next()) {

                   GameSupplementKeyword supgame = new GameSupplementKeyword(
                           resultSet.getInt("id"),
                           resultSet.getInt("keyword_id"),
                           resultSet.getString("sup_key_word"),
                           resultSet.getInt("operator")
                           );

                   //GameDetails game=hmGameDetails.get(resultSet.getString("key_word"));
                   //supgame.setGame(game);
                   hmGameSupKeyDetails.put(resultSet.getString("sup_key_word"), supgame);
               }
               stq.close();
               resultSet.close();
               System.out.println("GameSpplementKeywordList HashMap Generation ends..");
           } catch (SQLException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
		
    public synchronized void updateKeywordDetailsList() {

        try {
            System.out.println("KeywordDetailsList HashMap update begins...");
            String sql = "select * from cbs_keyword_details";
            Statement stq=con.createStatement();
            ResultSet resultSet = stq.executeQuery(sql);
            while (resultSet.next()) {
                if(hmKeywordDetails.containsKey(resultSet.getString("key_word"))){
                        GameDetailsList gamelist;
                        KeywordDetails keyword=hmKeywordDetails.get(resultSet.getString("key_word"));
                        if(keyword.getgamenamepos()>-1){
                            if(resultSet.getInt("game_name_pos")>-1){
                                gamelist=keyword.getgamelist();
                                gamelist.updateGameDetailsList();
                            }else{
                                 gamelist=null;
                            }
                        }else{
                             if(resultSet.getInt("game_name_pos")>-1){
                                gamelist = new GameDetailsList(dbHandler_op,resultSet.getInt("id"));
                                gamelist.createGameDetailsList();
                                keyword.setgamelist(gamelist);
                             }else{
                                 gamelist=null;
                             }
                        }
                        GameBlockKeyList gameblockkeylist=keyword.getGameblockkeylist();
                        gameblockkeylist.updateGameBlockKeyList();
                        keyword.update(
                                resultSet.getInt("id"),
                                resultSet.getString("key_word"),
                                resultSet.getString("game_type"),
                                resultSet.getString("company_name"),
                                resultSet.getString("unique_game_identifier"),
                                resultSet.getString("unique_sub_identifier"),
                                resultSet.getString("sms_spliter"),
                                resultSet.getInt("msisdn_pos"),
                                resultSet.getInt("imei_pos"),
                                resultSet.getInt("code_pos"),
                                resultSet.getInt("game_name_pos"),
                                resultSet.getInt("device_name_pos"),
                                resultSet.getInt("device_model_pos"),
                                resultSet.getInt("same_key_valid"),
                                resultSet.getInt("reply_game_name_source"),
                                resultSet.getString("unlockurl"),
                                resultSet.getString("unlockurl_response_splitter"),
                                resultSet.getString("unlock_sms"),
                                resultSet.getInt("url_resp_gamename_pos"),
                                resultSet.getInt("url_resp_unlockcode_pos"),
                                resultSet.getInt("url_resp_price_pos"),
                                resultSet.getString("charge_sms_success"),
                                resultSet.getString("charge_sms_fail"),
                                resultSet.getInt("device_log"),
                                resultSet.getInt("unlockcode_log"),
                                resultSet.getString("ins_date"),
                                resultSet.getInt("validation"),
                                resultSet.getInt("notifications"),
                                resultSet.getString("charge_sms_error"),
                                resultSet.getInt("url_resp_imei_pos"),
                                resultSet.getInt("url_resp_imsi_pos"),
                                resultSet.getInt("url_resp_brand_pos"),
                                resultSet.getInt("url_resp_model_pos"),
                                resultSet.getInt("sync")
                                );
                }else{

                        KeywordDetails keyword = new KeywordDetails(
                                resultSet.getInt("id"),
                                resultSet.getString("key_word"),
                                resultSet.getString("game_type"),
                                resultSet.getString("company_name"),
                                resultSet.getString("unique_game_identifier"),
                                resultSet.getString("unique_sub_identifier"),
                                resultSet.getString("sms_spliter"),
                                resultSet.getInt("msisdn_pos"),
                                resultSet.getInt("imei_pos"),
                                resultSet.getInt("code_pos"),
                                resultSet.getInt("game_name_pos"),
                                resultSet.getInt("device_name_pos"),
                                resultSet.getInt("device_model_pos"),
                                resultSet.getInt("same_key_valid"),
                                resultSet.getInt("reply_game_name_source"),
                                resultSet.getString("unlockurl"),
                                resultSet.getString("unlockurl_response_splitter"),
                                resultSet.getString("unlock_sms"),
                                resultSet.getInt("url_resp_gamename_pos"),
                                resultSet.getInt("url_resp_unlockcode_pos"),
                                resultSet.getInt("url_resp_price_pos"),
                                resultSet.getString("charge_sms_success"),
                                resultSet.getString("charge_sms_fail"),
                                resultSet.getInt("device_log"),
                                resultSet.getInt("unlockcode_log"),
                                resultSet.getString("ins_date"),
                                resultSet.getInt("validation"),
                                resultSet.getInt("notifications"),
                                resultSet.getString("charge_sms_error"),
                                resultSet.getInt("url_resp_imei_pos"),
                                resultSet.getInt("url_resp_imsi_pos"),
                                resultSet.getInt("url_resp_brand_pos"),
                                resultSet.getInt("url_resp_model_pos"),
                                resultSet.getInt("sync")
                             );
                        if(keyword.getgamenamepos()>-1){
                            GameDetailsList gamelist = new GameDetailsList(dbHandler_op,resultSet.getInt("id"));
                            gamelist.createGameDetailsList();
                            keyword.setgamelist(gamelist);
                        }else{
                            keyword.setgamelist(null);
                        }
                        GameBlockKeyList gameblockkeylist=new GameBlockKeyList(dbHandler_op,resultSet.getInt("id"));
                        gameblockkeylist.createGameBlockKeyList();
                        keyword.setGameblockkeylist(gameblockkeylist);
                        System.out.println("KeywordDetails Information created for ID:"+resultSet.getInt("id"));
                        hmKeywordDetails.put(resultSet.getString("key_word"), keyword);
                }
            }
            stq.close();
            resultSet.close();
            System.out.println("KeywordDetailsList HashMap update ends..");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

