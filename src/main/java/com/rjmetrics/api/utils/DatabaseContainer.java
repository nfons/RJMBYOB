package com.rjmetrics.api.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rjmetrics.api.rest.database;

@Repository
public class DatabaseContainer {
	private HashMap<String,HashMap<String,JsonObject>> DataMap;
	private static final String FILE_NAME = "data.ser";
	private static Logger log = LoggerFactory.getLogger(DatabaseContainer.class);
	/* read the object */
	public DatabaseContainer() throws Exception{
		DataMap  = new HashMap<String,HashMap<String,JsonObject>>();
		
		
		/** Seed the data with something **/
		String json = "{\"key\": 0, \"value\": \"Hello World\"}";
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(json);
		JsonElement key = o.get("key");
		String el = key.toString();
	
		HashMap<String,JsonObject> keymap = new HashMap<String,JsonObject>();
		keymap.put(el, o);
		
		
		DataMap.put("Strings", keymap);
		
	    try{
	      //deserialize the "database"
	      //DataMap = (HashMap<String, Object>)input.readObject();
	    }catch(Exception e){
	    	//most likely...file not found
	    	File f = new File(FILE_NAME);
	    	f.createNewFile();
	    	log.error("Cannot read file..",e);
	    	throw new Exception("Error Reading the File");
	 
	    	
	    	
	    }
	}
	
	public HashMap getTable(String table){
		return this.DataMap.get(table);
	}
	
	/**
	 * reeturns the whole DBC 
	 * @return
	 */
	public HashMap getDBC(){
		return this.DataMap;
	}
	
	public void DropAll() throws Exception{
		//flush everything we have...
		this.DataMap = new HashMap<String,HashMap<String,JsonObject>>();
		this.WriteTable(); //save it
	}
	/*
	 * Save the Data ... UN USED
	 */
	public void WriteTable() throws Exception{
		try (
			      OutputStream file = new FileOutputStream(FILE_NAME);
			      OutputStream buffer = new BufferedOutputStream(file);
			      ObjectOutput output = new ObjectOutputStream(buffer);
			    ){
			      output.writeObject(this.DataMap);
			    }  
		catch(Exception e){
			throw new Exception("Error Reading database file");
		}
	}

}
