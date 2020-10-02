/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import Service.KeywordDetails;

/**
 *
 * @author marufur
 */
public class RequestDetails {
    CGW mgw;
    long id;
    String msisdn;
    String msg;        
    int keywordID;
    int chargeConfID;
    int operator;
    int shortCode;
    int price; 
    float chargeAmount;
    int validity;
    String unlocksms="";
      
    String extra="";        
    String tid="";        
    String msg_rest="";
    String imei="";
    String imsi="";
    String smskey="";
    String gameCode="";       
    String deviceName="";
    String deviceModel="";
        
    boolean ismsg_restExist=false;
    boolean isimeiExist=false;
    boolean isimsiExist=false;
    boolean issmskeyExist=false;
    boolean isGameCodeExist=false;
    boolean isDeviceNameExist=false;
    boolean isDeviceModelExist=false;
        
    String returncode="";        

    KeywordDetails keyworddetails;
    String identifier="";
    String subidentifier="";    

    boolean isgameidExist=false;
    int gameid=0;
    boolean issupplementkeyexist=false;
    int suplementgameid=0;
    
    String keywordname="";
    String gamename="";
    String msgid="";   

    boolean isgamenamefromurlresponse=false;
        
    public RequestDetails(long id,String msisdn,String msg,int shortCode,String msgid,int operator,CGW mgw){
        this.id=id;
        this.msisdn=msisdn;
        this.msg=msg;
        this.operator=operator;
        this.shortCode=shortCode;
        this.mgw=mgw;
        this.msgid=msgid;
    }

    public boolean processMsg(KeywordDetails keyworddetails){
        this.keyworddetails=keyworddetails;
        this.keywordID = this.keyworddetails.getId();
        this.unlocksms = this.keyworddetails.getUnlocksms();
        if(ismsg_restExist){
            System.out.println("MSG_REST::"+msg_rest);
            String msg_part[]=msg_rest.split(this.keyworddetails.getSmssplitter());
            int partlength = msg_part.length;
            if(partlength>0){
                if(this.keyworddetails.getMsisdnpos()>0 && partlength>=this.keyworddetails.getMsisdnpos()){
                    this.msisdn = msg_part[this.keyworddetails.getMsisdnpos()-1].trim();
                }              
                if(this.keyworddetails.getImeipos()>0 && partlength>=this.keyworddetails.getImeipos()){
                    this.imei=msg_part[this.keyworddetails.getImeipos()-1].trim();
                    if(this.imei.length()>1)
                        this.setIsimeiExist(true);
                    else
                        this.setIsimeiExist(false);
                }
                if(this.keyworddetails.getCodepos()>0 && partlength>=this.keyworddetails.getCodepos()){
                    this.gameCode=msg_part[this.keyworddetails.getCodepos()-1].trim();
                    if(this.gameCode.length()>1)
                        this.isGameCodeExist=true;
                    else
                        this.isGameCodeExist=false;
                }
                if(this.keyworddetails.getDeviceNamePos()>0 && partlength>=this.keyworddetails.getDeviceNamePos()){
                    this.setDeviceName(msg_part[this.keyworddetails.getDeviceNamePos()-1].trim());
                    if(this.getDeviceName().length()>1)
                        this.isDeviceNameExist=true;
                    else
                        this.isDeviceNameExist=false;
                }
                if(this.keyworddetails.getDeviceModelPos()>0 && partlength>=this.keyworddetails.getDeviceModelPos()){
                    this.setDeviceModel(msg_part[this.keyworddetails.getDeviceModelPos()-1].trim());
                    if(this.getDeviceModel().length()>1)
                        this.isDeviceModelExist=true;
                    else
                        this.isDeviceModelExist=false;
                }
                
                if(this.keyworddetails.getgamenamepos()>0 && partlength>=this.keyworddetails.getgamenamepos()){
                    this.gamename=msg_part[this.keyworddetails.getgamenamepos()-1].trim();
                    if(this.keyworddetails.getgamelist().ishmGameDetailsExistKeyword(this.gamename)){
                        this.setGameid( this.keyworddetails.getgamelist().getGameDetails(this.gamename).getId());
                    }else{
                        this.setGameid(mgw.insertGameDetails(this));
                    }
                    if(this.getGameid()>0)
                        this.isgameidExist=true;
                    else
                        this.isgameidExist=false;
                }
                System.out.println("GameName:"+this.getGamename());
                int gamenamesource = this.keyworddetails.getReplygamenamesource();
                switch(gamenamesource){
                    case 1:
                        this.setGamename(this.gamename);
                        break;
                    case 2:
                        this.isgamenamefromurlresponse=true;
                        break;
                }
                System.out.println("KeywordName:"+this.getSmsKey());
                this.identifier = this.keyworddetails.getUniquegameidentifier().replaceAll("<0>", smskey.trim());
                this.subidentifier=this.keyworddetails.getUniquesubidentifier().replaceAll("<0>", smskey.trim());
                for(int i=1;i<=partlength;i++){
                    System.out.println("Partlength:"+partlength+" Position:"+i+" identifier:"+this.identifier+" Subidentifier:"+this.subidentifier);
                    this.identifier=this.identifier.replaceAll("<"+i+">", msg_part[i-1].trim());
                    this.subidentifier=this.subidentifier.replaceAll("<"+i+">", msg_part[i-1].trim());
                }
                for(int i=0;i<10;i++){
                    this.identifier=this.identifier.replaceAll("<"+i+">","");
                    this.subidentifier=this.subidentifier.replaceAll("<"+i+">",""); 
                }
                
                this.identifier=this.identifier.replaceAll("<GAMENAME>", this.keyworddetails.getKeyword());
                this.identifier=this.identifier.replaceAll("<SUBGAMENAME>", this.gamename);
                this.identifier=this.identifier.replaceAll("<KEYWORD>", this.getSmsKey());
		this.identifier=this.identifier.replaceAll("<SMS>",this.msg);
                this.identifier=this.identifier.trim();
                
                this.subidentifier=this.subidentifier.replaceAll("<GAMENAME>", this.keyworddetails.getKeyword());
                this.subidentifier=this.subidentifier.replaceAll("<SUBGAMENAME>", this.gamename);
                this.subidentifier=this.subidentifier.replaceAll("<KEYWORD>", this.getSmsKey());
		this.subidentifier=this.subidentifier.replaceAll("<SMS>",this.msg);
                this.subidentifier=this.subidentifier.trim();
                
                System.out.println("IMEI::"+this.imei+":GAMECODE::"+this.gameCode+":IDENTIFIER"+this.identifier+":SUBIDENTIFIER"+this.subidentifier+":DEVICENAME::"+this.getDeviceName()+":DEVICEMODEL::"+this.getDeviceModel());
                
                if(this.keyworddetails.isBlockKeyExist()){
                    for(int i=1;i<=partlength;i++){
                        if(this.keyworddetails.isBlockKeyByPosition(i, msg_part[i-1])){
                            System.out.println("Block Keyword:"+msg_part[i-1]+" In position:"+i);
                            return false;
                        }
                    }
                }
                return true;
            }
            else
                return false;
        }else
            return false;
    }
    
    public void setId(long id) {
        this.id = id;
    }   
    
    public long getId() {
        return id;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
    
    public String getMsgid() {
        return msgid;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setImei(String imei) {
        this.imei = imei;
        setIsimeiExist(true);
    }
    
    public String getImei() {
        return imei;
    }
    
    public void setImsi(String imsi) {
        this.imsi = imsi;
        setIsimsiExist(true);
    }
    
    public String getImsi() {
        return imsi;
    }

    public int getChargeConfID() {
        return chargeConfID;
    }

    public int getOperator() {
        return operator;
    }

    public String getExtra() {
        return extra;
    }

    public int getShortCode() {
        return shortCode;
    }

    public String getTid() {
        return tid;
    }

    public String getSmsKey() {
        return smskey;
    }
     
    public void setSmsKey(String smskey) {
        this.smskey = smskey;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
    
    public int getGameid() {
        return gameid;
    }

    public void setChargeConfID(int chargeConfID) {
        this.chargeConfID = chargeConfID;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setShortCode(int shortCode) {
        this.shortCode = shortCode;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setIsimeiExist(boolean isimeiExist) {
        this.isimeiExist = isimeiExist;
    }
    
    public void setIsimsiExist(boolean isimsiExist) {
        this.isimsiExist = isimsiExist;
    }

    public boolean isIsimeiExist() {
        return isimeiExist;
    }
    public boolean isIsimsiExist() {
        return isimsiExist;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        this.isDeviceNameExist=true;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
        this.isDeviceModelExist=true;
    }
    
    public void setChargeamount(float chargeamount) {
        this.chargeAmount = chargeamount;
    }

    public float getChargeamount() {
        return chargeAmount;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }
    
    public int getValidity() {
        return validity;
    }
    
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameCode() {
        return gameCode;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setUnlocksms(String unlocksms) {
        this.unlocksms = unlocksms;
    }
    
    public String getUnlocksms() {
        return unlocksms;
    }   
    
    public boolean processMsgKey(){
        int i=msg.indexOf(" ");
        if(i>1){
         this.setSmsKey(msg.substring(0,i).trim().toUpperCase());
         this.msg_rest = msg.substring(i).trim();
         if(this.msg_rest.length()>1)
            ismsg_restExist=true;
         return true;
        }else{
        	this.setSmsKey(msg.toUpperCase());
        	this.msg_rest="";
            return true;
        }
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public void setSubidentifier(String subidentifier) {
        this.subidentifier = subidentifier;
    }

    public String getSubidentifier() {
        return subidentifier;
    }
    
    public void setIsgameidExist(boolean isgameidExist) {
        this.isgameidExist = isgameidExist;
    }    

    public boolean isIsgameidExist() {
        return isgameidExist;
    }

    public void setKeywordid(int keywordID) {
        this.keywordID = keywordID;
    }
    
    public int getKeywordid() {
        return keywordID;
    }
    
    public void setIssupplementkeyexist(boolean issupplementkeyexist) {
        this.issupplementkeyexist = issupplementkeyexist;
    }

    public void setSuplementgameid(int suplementgameid) {
        this.suplementgameid = suplementgameid;
    }
    
    public int getSuplementgameid() {
        return suplementgameid;
    }

    public boolean isIssupplementkeyExist() {
        return issupplementkeyexist;
    }
    
    public void setKeywordname(String keywordname) {
        this.keywordname = keywordname;
    }
    
    public String getKeywordname() {
        return keywordname;
    }
    
    public void setGamename(String gamename) {
        this.gamename = gamename;
    }
    
    public String getGamename() {
        return gamename;
    }
    
    public boolean isIsgamenamefromurlresponse() {
        return isgamenamefromurlresponse;
    }     
     
    public void setGamefromUrl(String gamename){
        this.gamename = gamename;
        if(this.keyworddetails.getgamelist().ishmGameDetailsExistKeyword(this.gamename)){
            this.setGameid(this.keyworddetails.getgamelist().getGameDetails(this.gamename).getId());
        }else{
            this.setGameid(mgw.insertGameDetails(this));
        }
        if(this.getGameid()>0)
            this.isgameidExist=true;
        else
            this.isgameidExist=false;
    }
    
    public String toString(){
        return "RequestDetails [id=" + id + ", msisdn=" + msisdn + ", smstext="
                + msg+"]";
    }   
}

