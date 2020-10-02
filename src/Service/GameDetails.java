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
public class GameDetails {
    int id=0;
    int keywordid=0;
    String gamename="";
   
    public GameDetails(int id,int keywordid,String gamename){
        this.id=id;
        this.keywordid=keywordid;
        this.gamename=gamename;
    }

     public void setId(int id) {
       this.id = id;
    }

    public void setKeywordid(int keywordid) {
        this.keywordid = keywordid;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public int getId() {
        return id;
    }

    public int getKeywordid() {
        return keywordid;
    }

    public String getGamename() {
        return gamename;
    }
    
    public synchronized void update(int id,int keywordid,String gamename){
        int i=0;
        if(this.keywordid!=keywordid){
            System.out.println("last gameid:"+this.keywordid);
            this.keywordid=keywordid;
            i++;
            System.out.println("Change gameid:"+this.keywordid);
        }
        if(!this.gamename.equals(gamename)){
            System.out.println("last gamename:"+this.gamename);
            this.gamename=gamename;
            i++;
            System.out.println("Change gamename:"+this.gamename);
        }
        if(i>0)
            System.out.println("GameDetails updated for ID:"+id);
    }
}

