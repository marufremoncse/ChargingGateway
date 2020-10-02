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
public class SubService {
	int  id;
	String service;
	String details;
	//String ussd;
	Services monthly;
	Services biweekly;
	Services weekly;
	Services daily;
	public SubService(){
		
	}
public SubService(int id,String service,String details){
	this.id=id;
	this.service=service;
}
public void setMonthlyService(int charge,int plan){
	this.monthly=new Services(charge,plan);
}
public void setBiWeeklyService(int charge,int plan){
	this.biweekly=new Services(charge,plan);
}
public void setWeeklyService(int charge,int plan){
	this.weekly=new Services(charge,plan);
}
public void setDailyService(int charge,int plan){
	this.daily=new Services(charge,plan);
}
public Services getMonthlyService(){
	return this.monthly;
}
public Services getBiWeeklyService(){
	return this.biweekly;
}
public Services getWeeklyService(){
	return this.weekly;
}
public Services getDailyService(){
	return this.daily;
}
}
class Services{
	int charge;
	int plan;

	public Services(int charge,int plan){
		this.charge=charge;
		this.plan=plan;

	}
	public int getCharge(){
		return this.charge;
	}
	public int getPlan(){
		return this.plan;
	}
}
