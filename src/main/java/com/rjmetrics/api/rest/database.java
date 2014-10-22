package com.rjmetrics.api.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.rjmetrics.api.utils.DatabaseContainer;
import com.rjmetrics.api.utils.Message;


@Component
@Path("/database")
public class database {
	private static Logger log = LoggerFactory.getLogger(database.class);
	
	/*
	 * If i have time, ill add some constants for the magic strings here...
	 * 
	 * private STATIC blah
	 */
	
	@Autowired
	private DatabaseContainer dbc;
	
	/**
	 * GET /tables - List tables
	 * @return
	 */
	@GET
	@Path("/tables")
	@Produces("application/json")
	public Response getTables(){
		try{
			HashMap tempDBC = this.dbc.getDBC();
			Set<String> tables = tempDBC.keySet();
			return Response.status(Response.Status.OK).entity(tables).build();
		}catch(Exception e)
		{
			return this.createWebException(e);
		}
		
	}
	/**
	 *  GET /tables/:table - Retrieve the entire contents of :table 
	 * @param table
	 * @return
	 */
	@GET
	@Path("/tables/{table}")
	@Produces("application/json")
	public Response getTablebyTable(@PathParam("table") String table)
	{
		try{
			HashMap<String,JsonObject> tbl = this.dbc.getTable(table);
			
			//show only the values for the table
			Collection<JsonObject> out = tbl.values();
			return Response.status(200).entity(out.toString()).build();
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	/**
	 *  GET /tables/:table/:key - Retrieve the contents of :key in :table 
	 * @param e
	 * @return
	 */
	@GET
	@Path("/tables/{table}/{key}")
	@Produces("application/json")
	public Response getTableDatabyKey(@PathParam("table") String table, @PathParam("key") String key){
		try{
			HashMap<String,JsonObject> temp  = this.dbc.getTable(table);
			JsonObject obj =  temp.get(key);
			String resp = obj.toString();
			return Response.status(200).entity(resp).build();
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	/**
	 * - Expects a JSON map in the request body. Merges the contents of that map into :table, overwriting any existing keys.s
	 * @param table
	 * @return
	 */
	@POST
	@Path("/tables/{table}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response PostTableData(@PathParam("table") String table, String obj){
		try{
			JsonParser parser = new JsonParser();
			JsonArray jarray = (JsonArray)parser.parse(obj);
	
			/*
			 * If we aready have the table, just add the keys in. this will replace any existing ones
			 */
			
			
			if(this.dbc.getDBC().containsKey(table)){
				HashMap<String,JsonObject> tbl = this.dbc.getTable(table);
				for(int index = 0; index < jarray.size(); index++){
					JsonObject tempObj = jarray.get(index).getAsJsonObject();
					String key = tempObj.get("key").getAsString();
					
					//add the key and object to our array
					tbl.put(key, tempObj);
				}
			}
			else{ 
				//we got to create this the table stuff
				HashMap<String,JsonObject> tempMap = new HashMap<String,JsonObject>();
				for(int index = 0; index < jarray.size(); index++){
					JsonObject tempObj = jarray.get(index).getAsJsonObject();
					String key = tempObj.get("key").getAsString();
					
					//add the key and object to our array
					tempMap.put(key, tempObj);
				}
				
				this.dbc.getDBC().put(table, tempMap);
			}
			
			//we return a 201 since we are adding data, we want to let the know it was complete
			return Response.status(Response.Status.CREATED).build();
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	/**
	 * PUT /tables/:table/:key - Expects a JSON document in the request body. Sets the value of 
	 * @param table
	 * @param key
	 * @param object
	 * @return
	 */
	@PUT
	@Path("/tables/{table}/{key}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response PutTableData(@PathParam("table") String table, @PathParam("key") String key, String object){
		try{
			
			//if the table doesnt exist, return the proper response
			if(!this.dbc.getDBC().containsKey(table))
				return this.createWebException(null);
			else{
				//table exist, lets add the key to our map
				JsonParser parser = new JsonParser();
				/* hmm should I change this to also allow Arrays? */
				JsonObject json = (JsonObject)parser.parse(object);
				if(key.equals(json.get("key").getAsString()) == false){ //make sure what the JSON says is true
					//they lied
					Throwable e = new Throwable("JSON key and PATH key are not the same!");
					return this.createWebException(e);
				}
				else{
					this.dbc.getTable(table).put(key, json);
					return Response.status(Response.Status.CREATED).build();
				}
			}
		}catch(ClassCastException exception){
			//try to do this for a json Array...people might send this as well...if this is the case, we will only use the first elem
			JsonParser parser = new JsonParser();
			
			//changing the json to an array.
			JsonArray json = (JsonArray)parser.parse(object);
			JsonObject jobj = json.get(0).getAsJsonObject(); //we will get the first elem as an obj.
			if(key.equals(jobj.get("key").getAsString()) == false){ //make sure what the JSON says is true
				//they lied
				Throwable e = new Throwable("JSON key and PATH key are not the same!");
				return this.createWebException(e);
			}
			else{
				this.dbc.getTable(table).put(key, jobj);
				return Response.status(Response.Status.CREATED).build();
			}
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	/**
	 *  DELETE /tables/:table/:key - Disassociates any value with :key in :table 
	 * @return
	 */
	@DELETE
	@Path("/tables/{table}/{key}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response DeleteTableByKey(@PathParam("table") String table, @PathParam("key") String key, String object){
		try{
			//the null pointer will be caught here
			HashMap<String, JsonObject> temp = new HashMap<String, JsonObject>();
			temp = this.dbc.getTable(table);
			if(temp.containsKey(key) == false){ //no key found. let the user know
				return this.createWebException(new NullPointerException("Cannot find this key"));
			}
			else{
				temp.remove(key); //remove the data associated with the key
				return Response.status(200).build();
			}
				
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	/**
	 *  DELETE /tables/:table - Empties all keys and values from :table 
	 * @param table
	 * @return
	 */
	@DELETE
	@Path("/tables/{table}")
	public Response EmptyTable(@PathParam("table") String table){
		try{
			this.dbc.getDBC().remove(table); // delete the keyset that contains the table
			return Response.status(200).build();
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	@DELETE
	@Path("/tables")
	public Response EmptyAll(){
		try{
			this.dbc.DropAll();
			return Response.status(200).entity("All Database Tables dropped").build();
		}catch(Exception e){
			return this.createWebException(e);
		}
	}
	
	
	private Response createWebException(final Throwable e)
	{
		Message msg = new Message();
		
		if (e == null){
			msg.setCode(404);
			msg.setMessage("Table not found");
			return Response.status(404).entity(msg).build();
		} 
		else if(e instanceof NullPointerException )
		{
			msg.setCode(404);
			msg.setMessage("Null Pointer, entity not found");
			return Response.status(404).entity(msg).build();
		}
		else if(e instanceof JsonParseException ){
			msg.setCode(500);
			msg.setMessage("Incorrect JSON");
			return Response.status(500).entity(msg).build();
		}
			
		else {
			msg.setCode(500);
			msg.setMessage("Server Error");
			log.error("Error:",e);
			return Response.status(500).entity(msg).build();
		}
	}
}
