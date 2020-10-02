/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author marufur
 */
public class KeywordChargeConf {
    int id=0;
    int keywordid=0;
    int operator=0;//1 for BL, 2 for GP, 3 for Robi/Airtel, 4 for Teletalk
    String chargestep="";
    int totalstep=0;

   
    Vector chargeconfsequence = new Vector();
    HashMap<Integer, ChargeSteps> hmGameChargeStepSeq = new HashMap<Integer, ChargeSteps>();
    
    
    public KeywordChargeConf(int id,int keywordid,int operator,String chargestep) {
            // TODO Auto-generated constructor stub
            this.id=id;
            this.keywordid=keywordid;
            this.operator=operator;
            this.chargestep=chargestep;
            this.totalstep=0;
            processChargeStep();
            
    }
    
    private void processChargeStep(){
        try{
            if(chargestep.length()>2){
                String steps[]=chargestep.split(";");
                totalstep=steps.length;
                if(totalstep>0){
                    for(int i=0;i<totalstep;i++){
                        String singlestep[]=steps[i].split("\\|");
                        if(singlestep.length==2){
                            int chargeid=Integer.parseInt(singlestep[0]);
                            int validity=Integer.parseInt(singlestep[1]);
                            setChargeSteps(i,chargeid,validity);
                        } else{
                            System.out.println("Error in Charge steps:"+steps[i]+" For gameid="+keywordid+"  ID:"+id);
                            totalstep=0;
                        }                   
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            totalstep=0;
        }
    }
    public ChargeSteps getChargeSteps(int serial){
        if(serial<totalstep){
            return (ChargeSteps)chargeconfsequence.get(serial);
        }else
            return null;
    }
     
    private void setChargeSteps(int id,int chargeid,int validity){
        ChargeSteps charge=new ChargeSteps(id,chargeid,validity);
        chargeconfsequence.add(id, charge);
        hmGameChargeStepSeq.put(new Integer(chargeid), charge);
    }
    
    public boolean IsChargeStepsByChargeIDExist(int chargeid){
            return hmGameChargeStepSeq.containsKey(new Integer(chargeid));
    }
    public ChargeSteps getChargeStepsByChargeID(int chargeid){
            return (ChargeSteps)hmGameChargeStepSeq.get(new Integer(chargeid));
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setKeywordid(int keywordid) {
        this.keywordid = keywordid;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setChargestep(String chargestep) {
        this.chargestep = chargestep;
    }

    public int getId() {
        return id;
    }

    public int getKeywordid() {
        return keywordid;
    }

    public int getOperator() {
        return operator;
    }

    public String getChargestep() {
        return chargestep;
    }
    
    public int getTotalstep() {
        return totalstep;
    }
    
    public synchronized void update(int id,int gameid,int operator,String chargestep) {
		// TODO Auto-generated constructor stub
	int i=0;
        if(!this.chargestep.equals(chargestep)){
            System.out.println("last chargestep:"+this.chargestep);
            this.chargestep=chargestep;
            processChargeStep();
            i++;
            System.out.println("Change chargestep:"+this.chargestep);
        }
        
        if(this.id!=id){
            System.out.println("last id:"+this.id);
            this.id=id;
            i++;
            System.out.println("Change id:"+this.id);
        }
        
        if(this.keywordid!=keywordid){
            System.out.println("last gameid:"+this.keywordid);
            this.keywordid=keywordid;
            i++;
            System.out.println("Change imeipos:"+this.keywordid);
        }
        
        if(this.operator!=operator){
            System.out.println("last operator:"+this.operator);
            this.operator=operator;
            i++;
            System.out.println("Change operator:"+this.operator);
        }        
        
        if(i>0)
            System.out.println("GameDetails updated for ID:"+id);
    }    
}