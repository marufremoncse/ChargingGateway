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
public class ChargeConf {
    int id;
    String chargeCode="";
    int price;
    float priceWithVat;
    int operator;//1 for BL, 2 for GP, 3 for Robi/Airtel, 4 for Teletalk
    int validity;   

    public ChargeConf(int id,String chargeCode,int price,float priceWithVat,int operator,int validity) {
        // TODO Auto-generated constructor stub
        this.id = id;
        this.chargeCode = chargeCode;
        this.price = price;
        this.priceWithVat = priceWithVat;
        this.operator = operator;
        this.validity = validity;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setChargecode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPricewithvat(float priceWithVat) {
        this.priceWithVat = priceWithVat;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public int getId() {
        return id;
    }

    public String getChargecode() {
        return chargeCode;
    }

    public int getPrice() {
        return price;
    }

    public float getPriceWithVat() {
        return priceWithVat;
    }

    public int getOperator() {
        return operator;
    }

    public int getValidity() {
        return validity;
    }
	
    public synchronized void update(int id,String chargeCode,int price, float priceWithVat,int operator,int validity) {
        // TODO Auto-generated constructor stub
	int i=0;
        if(!this.chargeCode.equals(chargeCode)){
            System.out.println("Last chargeCode:"+this.chargeCode);
            this.chargeCode = chargeCode;
            i++;
            System.out.println("Change chargeCode:"+this.chargeCode);
        }
        if(this.price!=price){
            System.out.println("Last price:"+this.price);
            this.price = price;
            i++;
            System.out.println("Change price:"+this.price);
        }
        if(this.priceWithVat!=priceWithVat){
            System.out.println("Last pricewithvat:"+this.priceWithVat);
            this.priceWithVat = priceWithVat;
            i++;
            System.out.println("Change priceWithVat:"+this.priceWithVat);
        }
        if(this.operator!=operator){
            System.out.println("Last operator:"+this.operator);
            this.operator = operator;
            i++;
            System.out.println("Change operator:"+this.operator);
        }
        if(this.validity!=validity){
            System.out.println("Last validity:"+this.validity);
            this.validity = validity;
            i++;
            System.out.println("Change validity:"+this.validity);
        }
        if(i>0)
            System.out.println("ChargConf updated for ID:"+id);
    }
}

