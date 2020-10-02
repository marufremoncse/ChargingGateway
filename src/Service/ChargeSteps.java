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
public class ChargeSteps {
    int id;    
    int chargeid;
    int validity;

    public ChargeSteps(int id,int chargeid,int validity){
        this.id=id;
        this.chargeid=chargeid;
        this.validity=validity;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public void setChargeid(int chargeid) {
        this.chargeid = chargeid;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public int getChargeid() {
        return chargeid;
    }

    public int getValidity() {
        return validity;
    }
    public synchronized void update(int id,int chargeid,int validity) {
		// TODO Auto-generated constructor stub
	int i=0;
        
        if(this.chargeid!=chargeid){
            System.out.println("last chargeid:"+this.chargeid);
            this.chargeid=chargeid;
            i++;
            System.out.println("Change chargeid:"+this.chargeid);
        }
        
        if(this.validity!=validity){
            System.out.println("last validity:"+this.validity);
            this.validity=validity;
            i++;
            System.out.println("Change validity:"+this.validity);
        }  
        
        if(i>0)
            System.out.println("ChargeSteps updated for ID:"+id);
    }   
   
    
}

