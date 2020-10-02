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
public class KeywordDetails {
    int id=0;
    String keyword="";
    String gametype="";
    String comapanyname="";
    String uniquegameidentifier="";
    String uniquesubidentifier="";
    
    String smssplitter="";
    int msisdnpos;
    int imeipos;
    int codepos;
    int gamenamepos;
    int deviceNamePos;
    int deviceModelPos;
    int samekeyvalid;
    int replygamenamesource;
    String unlockurl="";
    String unlockurlresponsesplitter="";
    String unlocksms="";
    int urlrespgamenamepos;
    int urlrespunlockcodepos;
    int urlresppricepos;
    int urlrespimeipos;
    int urlrespimsipos;
    int urlrespbrandpos;
    int urlrespmodelpos;
    int sync;
    
    String chargesmssuccess="";
    String chargesmsfail="";
    String chargesmserror="";
    int deviceLog;
    int unlockcodelog;
    String insdate;
    int validation=0;
    int notification=0;

    GameDetailsList gamedetailslist;
    GameBlockKeyList gameblockkeylist;  

    
     public KeywordDetails(int id,String keyword,String gametype,String comapanyname,String uniquegameidentifier,String uniquesubidentifier,String smssplitter,int msisdnpos,int imeipos,int codepos,int gamenamepos,int deviceNamePos,int deviceModelPos,int samekeyvalid,int replygamenamesource,String unlockurl,String unlockurlresponsesplitter,String unlocksms,int urlrespgamenamepos,int urlrespunlockcodepos,int urlresppricepos,String chargesmssuccess,String chargesmsfail,int deviceLog,int unlockcodelog,String insdate,int validation,int notification,String chargesmserror,int urlrespimeipos,int urlrespimsipos,int urlrespbrandpos,int urlrespmodelpos, int sync) {
        // TODO Auto-generated constructor stub
        this.id=id;
        this.keyword=keyword;
        this.gametype=gametype;
        this.comapanyname=comapanyname;
        this.uniquegameidentifier=uniquegameidentifier;
        this.uniquesubidentifier=uniquesubidentifier;
        this.smssplitter=smssplitter;
        this.msisdnpos=msisdnpos;
        this.imeipos=imeipos;
        this.codepos=codepos;
        this.gamenamepos=gamenamepos;
        this.deviceNamePos=deviceNamePos;
        this.deviceModelPos=deviceModelPos;
        this.samekeyvalid=samekeyvalid;
        this.replygamenamesource=replygamenamesource;
        this.unlockurl=unlockurl;
        this.unlockurlresponsesplitter=unlockurlresponsesplitter;
        this.unlocksms=unlocksms;
        this.urlrespgamenamepos=urlrespgamenamepos;
        this.urlrespunlockcodepos=urlrespunlockcodepos;
        this.urlresppricepos=urlresppricepos;
        this.chargesmssuccess=chargesmssuccess;
        this.chargesmsfail=chargesmsfail;            
        this.deviceLog=deviceLog;
        this.unlockcodelog=unlockcodelog;
        this.insdate = insdate;
        this.validation=validation;
        this.notification=notification;
        this.chargesmserror=chargesmserror;
        this.urlrespimeipos=urlrespimeipos;
        this.urlrespimsipos=urlrespimsipos;
        this.urlrespbrandpos=urlrespbrandpos;
        this.urlrespmodelpos=urlrespmodelpos;
        this.sync = sync;
    }
    public boolean isSameKeyValid(){
        if(this.samekeyvalid==1)
            return true;
        else
            return false;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    public void setComapanyname(String comapanyname) {
        this.comapanyname = comapanyname;
    }

    public void setSmssplitter(String smssplitter) {
        this.smssplitter = smssplitter;
    }

    public void setMsisdnpos(int msisdnpos) {
        this.msisdnpos = msisdnpos;
    }

    public void setImeipos(int imeipos) {
        this.imeipos = imeipos;
    }

    public void setCodepos(int codepos) {
        this.codepos = codepos;
    }

    public void setDeviceNamePos(int deviceNamePos) {
        this.deviceNamePos = deviceNamePos;
    }

    public void setDeviceModelPos(int deviceModelPos) {
        this.deviceModelPos = deviceModelPos;
    }

    public void setUnlockurl(String unlockurl) {
        this.unlockurl = unlockurl;
    }    

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }


    public String getGametype() {
        return gametype;
    }

    public String getComapanyname() {
        return comapanyname;
    }

    public String getSmssplitter() {
        return smssplitter;
    }

    public int getMsisdnpos() {
        return msisdnpos;
    }

    public int getImeipos() {
        return imeipos;
    }

    public int getCodepos() {
        return codepos;
    }

    public int getDeviceNamePos() {
        return deviceNamePos;
    }

    public int getDeviceModelPos() {
        return deviceModelPos;
    }

    public String getUnlockurl() {
        return unlockurl;
    }
    
    public void setSamekeyvalid(int samekeyvalid) {
        this.samekeyvalid = samekeyvalid;
    }

    public int getSamekeyvalid() {
        return samekeyvalid;
    }
    
    public void setUnlocksms(String unlocksms) {
        this.unlocksms = unlocksms;
    }

    public String getUnlocksms() {
        return unlocksms;
    }

    public void setDeviceLog(int deviceLog) {
        this.deviceLog = deviceLog;
    }

    public void setUnlockcodelog(int unlockcodelog) {
        this.unlockcodelog = unlockcodelog;
    }

    public int getDeviceLog() {
        return deviceLog;
    }

    public int getUnlockcodelog() {
        return unlockcodelog;
    }
    
    public void setUniquegameidentifier(String uniquegameidentifier) {
        this.uniquegameidentifier = uniquegameidentifier;
    }

    public void setgamenamepos(int gamenamepos) {
        this.gamenamepos = gamenamepos;
    }

    public void setReplygamenamesource(int replygamenamesource) {
        this.replygamenamesource = replygamenamesource;
    }

    public void setUnlockurlresponsesplitter(String unlockurlresponsesplitter) {
        this.unlockurlresponsesplitter = unlockurlresponsesplitter;
    }

    public void setUrlrespgamenamepos(int urlrespgamenamepos) {
        this.urlrespgamenamepos = urlrespgamenamepos;
    }

    public void setUrlrespunlockcodepos(int urlrespunlockcodepos) {
        this.urlrespunlockcodepos = urlrespunlockcodepos;
    }

    public void setUrlresppricepos(int urlresppricepos) {
        this.urlresppricepos = urlresppricepos;
    }
    
    public void setChargesmssuccess(String chargesmssuccess) {
        this.chargesmssuccess = chargesmssuccess;
    }

    public void setChargesmsfail(String chargesmsfail) {
        this.chargesmsfail = chargesmsfail;
    }

    public void setChargesmserror(String chargesmserror) {
        this.chargesmserror = chargesmserror;
    }

    public String getUniquegameidentifier() {
        return uniquegameidentifier;
    }

    public int getgamenamepos() {
        return gamenamepos;
    }

    public int getReplygamenamesource() {
        return replygamenamesource;
    }

    public String getUnlockurlresponsesplitter() {
        return unlockurlresponsesplitter;
    }

    public int getUrlrespgamenamepos() {
        return urlrespgamenamepos;
    }

    public int getUrlrespunlockcodepos() {
        return urlrespunlockcodepos;
    }
    
    public int getUrlresppricepos() {
        return urlresppricepos;
    }
    
    public int getUrlrespimeipos() {
        return urlrespimeipos;
    }
    
    public void setUrlrespimeipos(int urlrespimeipos) {
            this.urlrespimeipos = urlrespimeipos;
    }
    
    public int getUrlrespimsipos() {
            return urlrespimsipos;
    }
    
    public void setUrlrespimsipos(int urlrespimsipos) {
            this.urlrespimsipos = urlrespimsipos;
    }
    
    public int getUrlrespbrandpos() {
            return urlrespbrandpos;
    }
    
    public void setUrlrespbrandpos(int urlrespbrandpos) {
            this.urlrespbrandpos = urlrespbrandpos;
    }
    
    public int getUrlrespmodelpos() {
            return urlrespmodelpos;
    }
    
    public void setUrlrespmodelpos(int urlrespmodelpos) {
            this.urlrespmodelpos = urlrespmodelpos;
    }
    
    public String getChargesmssuccess() {
        return chargesmssuccess;
    }
    
    public String getChargesmsfail() {
        return chargesmsfail;
    }

    public String getChargesmserror() {
        return chargesmserror;
    }
    
    public int getValidation() {
        return validation;
    }
    
    public void setValidation(int validation){
        this.validation=validation;
    }
    
    public int getNotification() {
        return notification;
    }
    
    public void setNotification(int notification){
        this.notification=notification;
    }
     public void setgamelist(GameDetailsList gamelist) {
        this.gamedetailslist = gamelist;
    }

    public GameDetailsList getgamelist() {
        return gamedetailslist;
    }
    
    public void setGameblockkeylist(GameBlockKeyList gameblockkeylist) {
        this.gameblockkeylist = gameblockkeylist;
    }

    public GameBlockKeyList getGameblockkeylist() {
        return gameblockkeylist;
    }
    
    public boolean isBlockKeyExist(){
        return gameblockkeylist.isIsblockkeywordexist();
    }
    
    public boolean isBlockKeyByPosition(int position,String key_word){
        return gameblockkeylist.ishmGameBlockKeyExistKeyword(position, key_word);
    }
    
    public void setUniquesubidentifier(String uniquesubidentifier) {
        this.uniquesubidentifier = uniquesubidentifier;
    }

    public String getUniquesubidentifier() {
        return uniquesubidentifier;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
    
    
    public synchronized void update(int id,String keyword,String gametype,String comapanyname,String uniquegameidentifier,String uniquesubidentifier,String smssplitter,int msisdnpos,int imeipos,int codepos,int gamenamepos,int deviceNamePos,int deviceModelPos,int samekeyvalid,int replygamenamesource,String unlockurl,String unlockurlresponsesplitter,String unlocksms,int urlrespgamenamepos,int urlrespunlockcodepos,int urlresppricepos,String chargesmssuccess,String chargesmsfail,int deviceLog,int unlockcodelog,String insdate,int validation,int notification,String chargesmserror,int urlrespimeipos,int urlrespimsipos,int urlrespbrandpos,int urlrespmodelpos, int sync) {
		// TODO Auto-generated constructor stub
	int i=0;
        if(!this.keyword.equals(keyword)){
            System.out.println("last keyword:"+this.keyword);
            this.keyword=keyword;
            i++;
            System.out.println("Change keyword:"+this.keyword);
        }
        if(!this.gametype.equals(gametype)){
            System.out.println("last gametype:"+this.gametype);
            this.gametype=gametype;
            i++;
            System.out.println("Change gametype:"+this.gametype);
        }
        if(!this.comapanyname.equals(comapanyname)){
            System.out.println("last comapanyname:"+this.comapanyname);
            this.comapanyname=comapanyname;
            i++;
            System.out.println("Change comapanyname:"+this.comapanyname);
        }
        if(!this.uniquegameidentifier.equals(uniquegameidentifier)){
            System.out.println("last uniquegameidentifier:"+this.uniquegameidentifier);
            this.uniquegameidentifier=uniquegameidentifier;
            i++;
            System.out.println("Change uniquegameidentifier:"+this.uniquegameidentifier);
        }
        if(!this.uniquesubidentifier.equals(uniquesubidentifier)){
            System.out.println("last uniquesubidentifier:"+this.uniquesubidentifier);
            this.uniquesubidentifier=uniquesubidentifier;
            i++;
            System.out.println("Change uniquesubidentifier:"+this.uniquesubidentifier);
        }
        if(!this.smssplitter.equals(smssplitter)){
            System.out.println("last smssplitter:"+this.smssplitter);
            this.smssplitter=smssplitter;
            i++;
            System.out.println("Change smssplitter:"+this.smssplitter);
        }
        if(this.msisdnpos!=msisdnpos){
            System.out.println("last msisdnpos:"+this.msisdnpos);
            this.msisdnpos=msisdnpos;
            i++;
            System.out.println("Change msisdnpos:"+this.msisdnpos);
        }
        if(this.imeipos!=imeipos){
            System.out.println("last imeipos:"+this.imeipos);
            this.imeipos=imeipos;
            i++;
            System.out.println("Change imeipos:"+this.imeipos);
        }
        if(this.codepos!=codepos){
            System.out.println("last codepos:"+this.codepos);
            this.codepos=codepos;
            i++;
            System.out.println("Change codepos:"+this.codepos);
        }
        if(this.gamenamepos!=gamenamepos){
            System.out.println("last gamenamepos:"+this.gamenamepos);
            this.gamenamepos=gamenamepos;
            i++;
            System.out.println("Change gamenamepos:"+this.gamenamepos);
        }
        if(this.deviceNamePos!=deviceNamePos){
            System.out.println("last deviceNamePos:"+this.deviceNamePos);
            this.deviceNamePos=deviceNamePos;
            i++;
            System.out.println("Change deviceNamePos:"+this.deviceNamePos);
        }
        if(this.deviceModelPos!=deviceModelPos){
            System.out.println("last deviceModelPos:"+this.deviceModelPos);
            this.deviceModelPos=deviceModelPos;
            i++;
            System.out.println("Change deviceModelPos:"+this.deviceModelPos);
        }
        if(this.samekeyvalid!=samekeyvalid){
            System.out.println("last samekeyvalid:"+this.samekeyvalid);
            this.samekeyvalid=samekeyvalid;
            i++;
            System.out.println("Change samekeyvalid:"+this.samekeyvalid);
        }
        if(this.replygamenamesource!=replygamenamesource){
            System.out.println("last replygamenamesource:"+this.replygamenamesource);
            this.replygamenamesource=replygamenamesource;
            i++;
            System.out.println("Change replygamenamesource:"+this.replygamenamesource);
        }
        
        if(!this.unlockurl.equals(unlockurl)){
            System.out.println("last unlockurl:"+this.unlockurl);
            this.unlockurl=unlockurl;
            i++;
            System.out.println("Change unlockurl:"+this.unlockurl);
        }
        if(!this.unlockurlresponsesplitter.equals(unlockurlresponsesplitter)){
            System.out.println("last unlockurlresponsesplitter:"+this.unlockurlresponsesplitter);
            this.unlockurlresponsesplitter=unlockurlresponsesplitter;
            i++;
            System.out.println("Change unlockurlresponsesplitter:"+this.unlockurlresponsesplitter);
        }
        
        if(!this.unlocksms.equals(unlocksms)){
            System.out.println("last unlocksms:"+this.unlocksms);
            this.unlocksms=unlocksms;
            i++;
            System.out.println("Change unlocksms:"+this.unlocksms);
        }
        if(this.urlrespgamenamepos!=urlrespgamenamepos){
            System.out.println("last urlrespgamenamepos:"+this.urlrespgamenamepos);
            this.urlrespgamenamepos=urlrespgamenamepos;
            i++;
            System.out.println("Change urlrespgamenamepos:"+this.urlrespgamenamepos);
        }
        if(this.urlrespunlockcodepos!=urlrespunlockcodepos){
            System.out.println("last urlrespunlockcodepos:"+this.urlrespunlockcodepos);
            this.urlrespunlockcodepos=urlrespunlockcodepos;
            i++;
            System.out.println("Change urlrespunlockcodepos:"+this.urlrespunlockcodepos);
        }
        if(this.urlresppricepos!=urlresppricepos){
            System.out.println("last urlresppricepos:"+this.urlresppricepos);
            this.urlresppricepos=urlresppricepos;
            i++;
            System.out.println("Change urlresppricepos:"+this.urlresppricepos);
        }
        if(!this.chargesmssuccess.equals(chargesmssuccess)){
            System.out.println("last chargesmssuccess:"+this.chargesmssuccess);
            this.chargesmssuccess=chargesmssuccess;
            i++;
            System.out.println("Change chargesmssuccess:"+this.chargesmssuccess);
        }
        if(!this.chargesmsfail.equals(chargesmsfail)){
            System.out.println("last chargesmsfail:"+this.chargesmsfail);
            this.chargesmsfail=chargesmsfail;
            i++;
            System.out.println("Change chargesmsfail:"+this.chargesmsfail);
        }
        if(!this.chargesmserror.equals(chargesmserror)){
            System.out.println("last chargesmserror:"+this.chargesmserror);
            this.chargesmserror=chargesmserror;
            i++;
            System.out.println("Change chargesmserror:"+this.chargesmserror);
        }
        
        if(this.deviceLog!=deviceLog){
            System.out.println("last deviceLog:"+this.deviceLog);
            this.deviceLog=deviceLog;
            i++;
            System.out.println("Change deviceLog:"+this.deviceLog);
        }
        if(this.unlockcodelog!=unlockcodelog){
            System.out.println("last unlockcodelog:"+this.unlockcodelog);
            this.unlockcodelog=unlockcodelog;
            i++;
            System.out.println("Change unlockcodelog:"+this.unlockcodelog);
        }
        if(this.validation!=validation){
            System.out.println("last validation:"+this.validation);
            this.validation=validation;
            i++;
            System.out.println("Change validation:"+this.validation);
        }
        if(this.notification!=notification){
            System.out.println("last notification:"+this.notification);
            this.notification=notification;
            i++;
            System.out.println("Change notification:"+this.notification);
        }
        if(this.urlrespimeipos!=urlrespimeipos){
            System.out.println("last urlrespimeipos:"+this.urlrespimeipos);
            this.urlrespimeipos=urlrespimeipos;
            i++;
            System.out.println("Change urlrespimeipos:"+this.urlrespimeipos);
        }
        if(this.urlrespimsipos!=urlrespimsipos){
            System.out.println("last urlrespimsipos:"+this.urlrespimsipos);
            this.urlrespimsipos=urlrespimsipos;
            i++;
            System.out.println("Change urlrespimsipos:"+this.urlrespimsipos);
        }
        if(this.urlrespbrandpos!=urlrespbrandpos){
            System.out.println("last urlrespbrandpos:"+this.urlrespbrandpos);
            this.urlrespbrandpos=urlrespbrandpos;
            i++;
            System.out.println("Change urlrespbrandpos:"+this.urlrespbrandpos);
        }
        if(this.urlrespmodelpos!=urlrespmodelpos){
            System.out.println("last urlrespmodelpos:"+this.urlrespmodelpos);
            this.urlrespmodelpos=urlrespmodelpos;
            i++;
            System.out.println("Change urlrespmodelpos:"+this.urlrespmodelpos);
        }
        if(this.sync!=sync){
            System.out.println("last sync:"+this.sync);
            this.sync=sync;
            i++;
            System.out.println("Change sync:"+this.sync);
        }
        if(i>0)
            System.out.println("GameDetails updated for ID:"+id);
    }         
}

