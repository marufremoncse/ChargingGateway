/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

/**
 *
 * @author marufur
 */
public class UnlockStatus {
    private boolean status;
    private String response;
    private String unlockkey;
    private String gamename;
    private String gameprice="0";
    private String imei="";
    private String imsi="";
    private String brand="";
    private String model="";
    
    private boolean isgamepriceexist=false;
    private boolean isimeiexist=false;
    private boolean isimsiexist=false;
    private boolean isbrandexist=false;
    private boolean ismodelexist=false;

    
    private long tt;
    RequestDetails requestDetails; 
    public UnlockStatus(RequestDetails requestDetails){
       this.requestDetails=requestDetails;
   }
    public long getTt() { return tt; }
    public void setTt(long tt) { this.tt = tt; }
        
    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setResponse(String response) {
        this.response = response;
        if(this.status){
            if(this.requestDetails.keyworddetails.getUrlrespgamenamepos()>-1 || this.requestDetails.keyworddetails.getUrlrespimeipos()>-1 || this.requestDetails.keyworddetails.getUrlrespimsipos()>-1 || this.requestDetails.keyworddetails.getUrlrespbrandpos()>-1 || this.requestDetails.keyworddetails.getUrlrespmodelpos()>-1){
                String response_split[] = this.response.split(this.requestDetails.keyworddetails.getUnlockurlresponsesplitter());
                if(this.requestDetails.keyworddetails.getUrlrespgamenamepos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespgamenamepos()){
                    this.gamename=response_split[this.requestDetails.keyworddetails.getUrlrespgamenamepos()].replaceAll("'","");
                    this.requestDetails.setGamefromUrl(this.gamename);
                    if(this.requestDetails.isIsgamenamefromurlresponse())
                        this.requestDetails.setGamename(this.gamename);
                }
                else {
                    if(this.requestDetails.keyworddetails.getUrlrespgamenamepos()>-1)
                    this.status=false;
                }
                if(this.requestDetails.keyworddetails.getUrlrespunlockcodepos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespunlockcodepos()) {
                    this.unlockkey=response_split[this.requestDetails.keyworddetails.getUrlrespunlockcodepos()];
                    if(this.requestDetails.keyworddetails.getUrlresppricepos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlresppricepos()) {
                        this.gameprice=response_split[this.requestDetails.keyworddetails.getUrlresppricepos()];
                        setGamepriceexist(true);
                    }else 
                    	setGamepriceexist(false);
                }
                else
                    this.status=false;
                
                if(this.requestDetails.keyworddetails.getUrlrespimeipos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespimeipos()) {
                    this.imei=response_split[this.requestDetails.keyworddetails.getUrlrespimeipos()];
                    this.requestDetails.setImei(this.imei);
                    setImeiexist(true);
                }
                else
                    setImeiexist(false);
                
                if(this.requestDetails.keyworddetails.getUrlrespimsipos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespimsipos()) {
                    this.imsi=response_split[this.requestDetails.keyworddetails.getUrlrespimsipos()];
                    this.requestDetails.setImsi(this.imsi);
                    setImsiexist(true);
                }
                else
                    setImsiexist(false);
                
                if(this.requestDetails.keyworddetails.getUrlrespbrandpos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespbrandpos()) {
                    this.brand=response_split[this.requestDetails.keyworddetails.getUrlrespbrandpos()];
                    this.requestDetails.setDeviceName(this.brand);
                    setBrandexist(true);
                }else
                	setBrandexist(false);
                
                if(this.requestDetails.keyworddetails.getUrlrespmodelpos()>-1 && response_split.length>this.requestDetails.keyworddetails.getUrlrespmodelpos()) {
                    this.model=response_split[this.requestDetails.keyworddetails.getUrlrespmodelpos()];
                    this.requestDetails.setDeviceModel(this.model);
                    setModelexist(true);
                }
                else
                    setModelexist(false);                
            }
            else{
                this.unlockkey = response;
            }
        }
    }

    public void setUnlockkey(String unlockkey) {
        this.unlockkey = unlockkey;
    }

    public boolean isStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    public String getUnlockkey() {
        return unlockkey;
    }
    public void setGamepriceexist(boolean isgamepriceexist) {
    	this.isgamepriceexist=isgamepriceexist;
    }
    public boolean IsGamepriceexist() {
    	return isgamepriceexist;
    }
    public void setImeiexist(boolean isimeiexist) {
    	this.isimeiexist=isimeiexist;
    }
    public boolean IsImeiexist() {
    	return isimeiexist;
    }
    public void setImsiexist(boolean isimsiexist) {
    	this.isimsiexist=isimsiexist;
    }
    public boolean IsImsiexist() {
    	return isimsiexist;
    }
    public void setBrandexist(boolean isbrandexist) {
    	this.isbrandexist=isbrandexist;
    }
    public boolean IsBrandexist() {
    	return isbrandexist;
    }
    public void setModelexist(boolean ismodelexist) {
    	this.ismodelexist=ismodelexist;
    }
    public boolean IsModelexist() {
    	return ismodelexist;
    }
    public int getGamePrice() {
    	try {
        return Integer.parseInt(gameprice);
    	}catch(Exception e) {
    		e.printStackTrace();
    		return 0;
    	}
    }
    
    public String getGamename() {
        return gamename;
    }
}
