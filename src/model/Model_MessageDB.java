/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.security.Timestamp;

/**
 *
 * @author duong
 */
public class Model_MessageDB implements Serializable {
	int idsender;
	int idreceiver;
	String content;
	String timestamp;
	public Model_MessageDB(int idsender, int idreceiver, String content) {
		super();
		this.idsender = idsender;
		this.idreceiver = idreceiver;
		this.content = content;
		
	}
	
	public Model_MessageDB(int idsender, String content) {
		super();
		this.idsender = idsender;
		this.content = content;
		
	}

    public Model_MessageDB() {
       
    }

    public int getIdsender() {
        return idsender;
    }

    public void setIdsender(int idsender) {
        this.idsender = idsender;
    }

    public int getIdreceiver() {
        return idreceiver;
    }

    public void setIdreceiver(int idreceiver) {
        this.idreceiver = idreceiver;
    }

    
        
    
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

    @Override
    public String toString() {
        return "Model_MessageDB{" + "idsender=" + idsender + ", idreceiver=" + idreceiver + ", content=" + content + ", timestamp=" + timestamp + '}';
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
	

    

    public Model_MessageDB(int idsender, int idreceiver, String content, String timestamp) {
        this.idsender = idsender;
        this.idreceiver = idreceiver;
        this.content = content;
        this.timestamp = timestamp;
    }
    
        
}