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
public class WatchDog extends Thread {
    int numberofthread;
    CGW mainpro;
    long serverresponsetime=0;
    long operatorresponsetime=0;
    long gameresponsetime=0;
    int maxwaitingtime=30;//Sec
    //int numofreq=0;
	
    
    public WatchDog(CGW mainpr, int numberofthread) {
        this.mainpro = mainpr;
        this.numberofthread = numberofthread;
        System.out.println("##watchdog##Initiate watchdog..");
    }
    
    public void run() {
    	System.out.println("##watchdog##Watchdog started..");
    	boolean loop=true;
    	long maxgaptime=maxwaitingtime*1000;
    	while(loop) {
            loop=false;
            for(int t=0;t<this.numberofthread;t++) {
                try {
                    RequestProcessor reqt =mainpro.reqT.get(t);
                    if(reqt.isAlive()) {
                    loop=true;
                    long ping = reqt.getPing();
                    long pong = reqt.getPong();
                    if(pong==-1) {
                        reqt.setPong(2);
                    }else if(pong==0) {
                        System.out.println("##watchdog##WatchDog Response Ok for Thread="+t);
                    }else {
                        reqt.setPong(2);
                        pong = reqt.getPong();
                        long gap=pong-ping;
                        System.out.println("##watchdog##Time Gap:"+gap+" For thread:"+t);
                        if(gap>maxgaptime) {
                            System.out.println("##watchdog##Need Attention##Time Gap:"+gap+" For thread:"+t);
                            reqt.forceSocket();
                        }
                    }                		
                }	                    
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
                Thread.sleep(1000);
        }catch (Exception ex) {
        ex.printStackTrace();
        }
    }
    	
    mainpro.insertProcessLog();
    
    System.out.println("##watchdog##Watchdog Finished..");
    }
}

