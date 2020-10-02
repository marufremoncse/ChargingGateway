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
public class GameSupplementKeyword {
    int id=0;
    int keywordid=0;
    String sup_keyword="";
    KeywordDetails keyword;  
    int operator;
    
    public GameSupplementKeyword(int id,int keywordid,String sup_keyword,int operator){
        this.id=id;
        this.keywordid=keywordid;
        this.sup_keyword=sup_keyword;
        this.operator=operator;
        
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setKeywordid(int keywordid) {
        this.keywordid = keywordid;
    }

    public void setSup_keyword(String sup_keyword) {
        this.sup_keyword = sup_keyword;
    }

    public int getId() {
        return id;
    }

    public int getKeywordid() {
        return keywordid;
    }

    public String getSup_keyword() {
        return sup_keyword;
    }
    
    public void setKeyword(KeywordDetails keyword) {
        this.keyword = keyword;
    }

    public KeywordDetails getKeyword() {
        return keyword;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    
     public synchronized void update(int id,int keywordid,String sup_keyword) {
		// TODO Auto-generated constructor stub
	int i=0;
        if(this.keywordid!=keywordid){
            System.out.println("last keywordid:"+this.keywordid);
            this.keywordid=keywordid;
            i++;
            System.out.println("Change keywordid:"+this.keywordid);
        }
        
        if(!this.sup_keyword.equals(sup_keyword)){
            System.out.println("last sup_keyword:"+this.sup_keyword);
            this.sup_keyword=sup_keyword;
            i++;
            System.out.println("Change sup_keyword:"+this.sup_keyword);
        }
        
        if(this.operator!=operator){
            System.out.println("last operator:"+this.operator);
            this.operator=operator;
            i++;
            System.out.println("Change operator:"+this.operator);
        }
        
        if(i>0)
            System.out.println("GameSupplementKeyword updated for ID:"+id);
    }    
}

