package com.creative.json;

public class TestJson {
	public void testParseString(){
		String data = "{COMMAND:abc,FROM:def,TO:ghi,DATA:ghi}";
		JsonData result = new JsonData(data);
		System.out.print(result.get("COMMAND"));
	}
}
