/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateway;

import java.util.Vector;

/**
 *
 * @author marufur
 */
public class RequestQueue {
	CGW parent;
        Vector<RequestDetails> requestLists = new Vector<RequestDetails>();
	public RequestQueue(CGW parent){
		this.parent = parent;
	}
	public void addToQueue(RequestDetails req){
		this.requestLists.add(req);
	}
	public int getQueueSize(){
		return this.requestLists.size();
	}
	public RequestDetails getFromQueue(int index){
		return this.requestLists.get(index);
	}
}
