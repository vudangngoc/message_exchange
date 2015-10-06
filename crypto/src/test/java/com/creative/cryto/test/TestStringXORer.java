package com.creative.cryto.test;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.creative.crypto.EncryptString;
import com.creative.crypto.StringXORer;

public class TestStringXORer {
	@Test
	public void testEncrypt(){
		EncryptString test = new StringXORer();
		UUID uuid = UUID.randomUUID();
		String content = "{COMMAND:STATE_GET,FROM:esp7_1@demo,TO:nil,DATA:NA}";
		String key = uuid.toString();
		String encryptContent = test.encode(content, key);
		System.out.println("encrypt data: "+ encryptContent);
		System.out.println("encrypt key: "+ key);
		Assert.assertEquals(content, test.decode(encryptContent,key));
		
	}
}
