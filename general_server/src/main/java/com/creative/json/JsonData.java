package com.creative.json;

import org.json.JSONObject;

import com.creative.context.IData;

public class JsonData implements IData{
	private JSONObject data;
	public JsonData(String data){
		this.data = new JSONObject(data);
	}
	public String get(String key){return data.getString(key);}
}
