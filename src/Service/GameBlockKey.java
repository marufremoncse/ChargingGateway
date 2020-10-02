/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author marufur
 */
public class GameBlockKey {
    int id;
    int keyword_id;
    int position;
    String key_word="";

    public GameBlockKey(int id,int keyword_id,int position,String key_word){
        this.id=id;
        this.keyword_id=keyword_id;
        this.position=position;
        this.key_word=key_word;               
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setKeywordid(int keyword_id) {
        this.keyword_id = keyword_id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setKeyword(String key_word) {
        this.key_word = key_word;
    }

    public int getId() {
        return id;
    }

    public int getKeywordid() {
        return keyword_id;
    }

    public int getPosition() {
        return position;
    }

    public String getKeyword() {
        return key_word;
    }
    
    public synchronized void update(int id,int keyword_id,int position,String key_word) {
		// TODO Auto-generated constructor stub
	int i=0;
        
        if(this.keyword_id!=keyword_id){
            System.out.println("last keyword_id:"+this.keyword_id);
            this.keyword_id=keyword_id;
            i++;
            System.out.println("Change keyword_id:"+this.keyword_id);
        }
        if(this.position!=position){
            System.out.println("last position:"+this.position);
            this.position=position;
            i++;
            System.out.println("Change position:"+this.position);
        }
        
         if(!this.key_word.equals(key_word)){
            System.out.println("last keyword:"+this.key_word);
            this.key_word=key_word;
            i++;
            System.out.println("Change keyword:"+this.key_word);
        }
        if(i>0)
            System.out.println("GameBlockKey updated for ID:"+id);
    }   
}
