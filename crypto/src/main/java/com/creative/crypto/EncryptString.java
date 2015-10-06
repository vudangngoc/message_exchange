package com.creative.crypto;

public interface EncryptString {

	String encode(String s, String key);

	String decode(String s, String key);

}