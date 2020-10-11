/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author marufur
 */
public class RobiCharging extends Charging{
    ChargeStatus cs = new ChargeStatus();
    String applicationname = "CGW.Gateway.CGW";
    String charging_url ;
    
    String trid="";
    String msgTtrid="";
    String resultcode="-1";
    String resultdesc="";	
    String response="";
    String CHARGE_RESPONSE="";
    
    String msisdn="";
    String codesms = "";
    String chargecode="";
    float amount=0;
    String game_name;
    int operator;
    String timestamp="";
    long tm = System.currentTimeMillis();
    long tmt;
    
    public static SimpleDateFormat timeformate = new SimpleDateFormat();
     
    public static final String serviceidbill="2000192000002290";
    public static final String serviceidbillAirtel="2000192000006071";
    public static Hashtable header;
       
    private SOAPConnection sconnection;
       
    public RobiCharging(String msisdn,String codesms,String chargecode,String msgTtrid,float amount,String game_name,int operator,String timestamp){
        this.msisdn = msisdn;
        this.codesms = codesms;
        this.chargecode = chargecode;
        this.msgTtrid = msgTtrid;
        this.amount = amount;
        this.game_name = game_name;
        this.operator = operator;
        this.timestamp = timestamp;
        timeformate.applyPattern("yyyyMMddHHmmss");
        //charging();
    }
       
    public void charging(){
        Calendar cal = Calendar.getInstance();
        Date ndl=cal.getTime();
        SimpleDateFormat dateformate = new SimpleDateFormat();
        dateformate.applyPattern("yyMMddHHmmssS");
        String refcode = dateformate.format(ndl);
        
        StringTokenizer st = null;
        String nres = "";
        String res="",fee,retval="FAIL";
        String soapActionipAndPort="192.168.51.168:8310";
        //String soapActionipAndPort="192.168.230.27";        
        String chargeAmountRequest="AmountChargingService/services/AmountCharging";
        //String chargeAmountRequest="game_bill/robiGameBillingWrapper.php";
         header = new Hashtable();
        String soapAction = "chargeAmount";
        header.put("Content-Type", "text/xml; charset=utf-8");
            header.put("SOAPAction", soapAction);
            header.put("Host", soapActionipAndPort);
            
            String requestXML = getConfigRequsetXML(msisdn,amount,refcode);

            nres = send("http://" + soapActionipAndPort
                    + "/" + chargeAmountRequest, requestXML, header,cs);

            if (!nres.equalsIgnoreCase("FAILED")) {
                st = new StringTokenizer(nres, "|");

                while (st.hasMoreElements()) {
                    res = st.nextToken();
                    fee = st.nextToken();
                }

                if (res.equalsIgnoreCase("SUCCESS")) {
                    System.out.println("Success");
                    retval="SUCCESS";
                }else{
                    System.out.println("Fail");
                    retval="FAIL";
                }
            }
            
            System.out.println(retval);
            tmt = System.currentTimeMillis();
            
            cs.setGWStatus(retval,operator);
            cs.setResponse(retval);
            cs.setTrid(refcode);
            cs.setTt(tmt - tm);
    }
    public String getConfigRequsetXML(String msisdn,float amount,String refcode) {
        Calendar cal = Calendar.getInstance();
        Date ndl=cal.getTime();
		String requestXML = "";
                String spid="200019";
                String sppassword="Robi1234";
               // String mdn="8801842508951";//8801877739225";
                String timestd=timeformate.format(ndl);
                
                String spPass = spid+sppassword+timestd;
                //String spPass = Constantes.spid+Constantes.sppassword+timestd;
                String srv="Games";
                String serviceid = serviceidbill;
                String rs=""+(int)(amount*100);
                
                String finalmd5 = "";
                try {
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                    byte[] array = md.digest(spPass.getBytes());
                    StringBuffer msb = new StringBuffer();
                    for (int i = 0; i < array.length; ++i) {
                        msb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                    }
                    finalmd5 = msb.toString();
                } catch (java.security.NoSuchAlgorithmException e) {
                }
                
		StringBuffer sb = new StringBuffer();

		try {
                    String lphn = msisdn.substring(0, 5);
            
                    if(lphn.equalsIgnoreCase("88016"))
                    {
                         serviceid = serviceidbillAirtel;   
                    }
                    sb.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:v2='http://www.huawei.com.cn/schema/common/v2_1' xmlns:loc='http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local'>");
                    sb.append("<soapenv:Header>");
                    sb.append("<v2:RequestSOAPHeader>");
                    sb.append("<v2:spId>"+spid+"</v2:spId>");
                    sb.append("<v2:spPassword>"+finalmd5+"</v2:spPassword>");
                    sb.append("<v2:serviceId>"+serviceid+"</v2:serviceId>");
                    sb.append("<v2:timeStamp>"+timestd+"</v2:timeStamp>");
                    sb.append("<v2:OA>"+msisdn+"</v2:OA>");
                    sb.append("<v2:FA>"+msisdn+"</v2:FA>");
                    sb.append("<v2:token/>");
                    sb.append("</v2:RequestSOAPHeader>");
                    sb.append("</soapenv:Header>");
                    sb.append("<soapenv:Body>");
                    sb.append("<loc:chargeAmount>");
                    sb.append("<loc:endUserIdentifier>"+msisdn+"</loc:endUserIdentifier>");
                    sb.append("<loc:charge>");
                    sb.append("<description>"+srv+"</description>");
                    sb.append("<currency>TK</currency>");
                    sb.append("<amount>"+rs+"</amount>");
                    sb.append("<code>16303</code>");
                    sb.append("</loc:charge>");
                    sb.append("<loc:referenceCode>"+refcode+"</loc:referenceCode>");
                    sb.append("</loc:chargeAmount>");
                    sb.append("</soapenv:Body>");
                    sb.append("</soapenv:Envelope>");
                    
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		requestXML = sb.toString();
		return requestXML;
	}
    public synchronized String send(String urlStr, String requestXML, Hashtable header,ChargeStatus cs) {
        
        String price = "0";
        String resp = "FAILED";
        
        //requestlog reqlog = new requestlog();

        StringBuffer sbData = new StringBuffer();
        sbData.append(requestXML);
        InputStream is = null;

        try {
                System.out.println("Connecting to URL - " + urlStr);
                MessageFactory factory = MessageFactory.newInstance();
                SOAPMessage message = factory.createMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPConnection connection = null;
                this.sconnection=connection;

                String xml = requestXML;
                StringReader reader = new StringReader(xml);
                StreamSource src = new StreamSource(reader);
                soapPart.setContent(src);
                // add attachments  
                /*
                * AttachmentPart attachment = null; FileDataSource fileDataSource =
                * new FileDataSource(fileURL); DataHandler dataHandler = new
                * DataHandler(fileDataSource); attachment =
                * message.createAttachmentPart(dataHandler);
                * attachment.setContentType(fileDataSource.getContentType());
                * message.addAttachmentPart(attachment);
                */
                // save changes  
                message.saveChanges();

                SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                connection = soapConnectionFactory.createConnection();
                
                URL endpoint = new URL(urlStr);
                SOAPMessage response = connection.call(message, endpoint);
                SOAPBody responseBody= response.getSOAPPart().getEnvelope().getBody();
                       
                    try {
                            
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.writeTo(out);
                        is = new ByteArrayInputStream(out.toByteArray());
                        
                        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                        Document doc = docBuilder.parse(is);

                        DOMSource domSource = new DOMSource(doc);
                        StringWriter writer = new StringWriter();
                        StreamResult stmrs = new StreamResult(writer);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transf = transformerFactory.newTransformer();
                        transf.transform(domSource, stmrs);

                        String result = writer.toString();
                        System.out.println(result);
                        sbData.append("Response : ");
                        sbData.append(result);

                        doc.getDocumentElement().normalize();
                        //System.out.println ("Root element of the doc is " + 
                        // doc.getDocumentElement().getNodeName());
                        String res;
                        NodeList listOfPersons = doc.getElementsByTagName("ns1:chargeAmountResponse");
                        int totalPersons = listOfPersons.getLength();
                        // System.out.println("Total no of people : " + totalPersons);
                        System.out.println("start");
                        if(listOfPersons.getLength() > 0)
                        {
                            for (int s = 0; s < listOfPersons.getLength(); s++) {
                                Node firstPersonNode = listOfPersons.item(s);
                                if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
                                    System.out.println("get node");
                                    price = "charged";
                                    cs.setResponse(price);
                                   }
                            }
                        }
                        else
                        {
                            NodeList listOfFault = doc.getElementsByTagName("soapenv:Fault");
                            int totalAmount = listOfFault.getLength();
                            // System.out.println("Total no of people : " + totalPersons);
                            System.out.println("saop fault");

                            for (int n = 0; n < listOfFault.getLength(); n++) {

                                org.w3c.dom.Node firstAmountNode = listOfFault.item(n);

                                if (firstAmountNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                                    Element firstAmountElement = (Element) firstAmountNode;

                                    NodeList ammins = firstAmountElement.getElementsByTagName("faultcode");
                                    Element minsAmount = (Element) ammins.item(0);

                                    NodeList textAMList = minsAmount.getChildNodes();
                                    String errorcode = ((org.w3c.dom.Node) textAMList.item(0)).getNodeValue().trim();

                                    NodeList ammbal = firstAmountElement.getElementsByTagName("faultstring");
                                    Element balAmount = (Element) ammbal.item(0);

                                    NodeList textBLList = balAmount.getChildNodes();
                                    String ermsg = ((org.w3c.dom.Node) textBLList.item(0)).getNodeValue().trim();

                                    System.out.println(errorcode);
                                    System.out.println(ermsg);
                                    cs.setResponse(ermsg);
                                }

                            }

                        }

                        if (!price.equalsIgnoreCase("0")) {
                            resp = "SUCCESS|" + price;
                        } else {
                            resp = "FAILED";
                        }
                        
                    } catch (Exception e) {

                        resp = "FAILED";
                        System.out.println("Exception: " + e);
                    }

                
            } catch (Exception e) {
                sbData.append("ERROR");
                System.out.println("IOException: " + e);

            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
        
        System.out.println("ChargeAmount: " + price);
        //reqlog.writerenewallog("charge", sbData.toString());
        //return sbData.toString();
        return resp;
    }
    
    public ChargeStatus result(){
        return cs;
    }
    @Override
    public void chargingClose() {
    	try {
            this.sconnection.close();
    	}catch (Exception e) {
            e.printStackTrace();
    	}
    }    
}

