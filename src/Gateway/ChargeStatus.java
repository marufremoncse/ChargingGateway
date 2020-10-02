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
public class ChargeStatus {
        private int status=-1;
        private String gwstatus;
        private long tt;
        private String trid;
        private String response;
        private boolean retry=false;
        
        public boolean getRetry() {
        	return this.retry;
        }
        public void setRetry(boolean retry) {
        	this.retry = retry;
        }
        public int getStatus() {
            return this.status; 
        }
        public void setStatus(int status) {
            this.status = status; 
        }
        public String getGWStatus() {
            return this.gwstatus; 
        }
        public void setGWStatus(String gwstatus,int operator) { 
        	this.gwstatus = gwstatus; 
                try{
        	switch(operator) {
        	case 1:
                    if(gwstatus.equalsIgnoreCase("100"))
                            this.status=0;
        		else 
                            this.status=Integer.parseInt(gwstatus);
        		break;
        		
        	case 2:
                    if(gwstatus.equalsIgnoreCase("SUCCESS"))
                        this.status=0;
                    else 
                        this.status=1;               
                    break;
        	
        	case 3:
                    if(gwstatus.equalsIgnoreCase("SUCCESS"))
        		this.status=0;
                    else if(gwstatus.equalsIgnoreCase("FAIL"))
                        this.status=1;
                    else
        		this.status=Integer.parseInt(gwstatus);
                    break;
        	default:
        		this.status=Integer.parseInt(gwstatus);
        		break;	
        	}
                }catch(Exception e){
                    e.printStackTrace();
                    this.status=-1;
                }        
        }
        
        public long getTt() {
            return tt; 
        }
        public void setTt(long tt) {
            this.tt = tt; 
        }        
        public String getTrid() {
            return trid; 
        }
        public void setTrid(String trid) {
            this.trid = trid; 
        }        
        public String getResponse() {
            return response; 
        }
        public void setResponse(String response) {
            this.response = response; 
        }        
        public String toString(){
            return "TRID: "+getTrid()+", Status: "+getStatus()+", Response: "+getResponse();
        } 
}

