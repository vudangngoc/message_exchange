package com.creative;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;

public class RedisHashMap implements ConcurrentMap<String, String> {

	private String subset;
	private Jedis jedis;

	public RedisHashMap(Jedis jedis, String subset){
		this.jedis = jedis;
		this.subset = subset;
		if(!jedis.isConnected()) jedis.connect();
	}
	public String getState(){
		
		return "";
	}
	@Override
	public int size() {
		return jedis.hlen(subset).intValue();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return jedis.hexists(subset, key.toString());
	}
	@Deprecated
	@Override
	public boolean containsValue(Object value) {
		return jedis.hvals(subset).contains(value);
	}

	@Override
	public String get(Object key) {
		String result = jedis.hget(subset, key.toString());
		return result == null ? "" : result;
	}

	@Override
	public String put(String key, String value) {
		String result = jedis.hget(subset, key.toString());
		jedis.hset(subset, key, value);
		return result;
	}

	@Override
	public String remove(Object key) {
		String result = jedis.hget(subset, key.toString());
		jedis.hdel(subset, key.toString());
		return result;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		jedis.hmset(subset, (Map<String, String>) m);
		
	}

	@Override
	public void clear() {
		jedis.del(subset);
		
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return jedis.hkeys(subset);
	}

	@Override
	public Collection<String> values() {
		// TODO Auto-generated method stub
		return jedis.hvals(subset);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String putIfAbsent(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object key, Object value) {
		return jedis.hdel(subset, key.toString()) == 1;
	}

	@Override
	public boolean replace(String key, String oldValue, String newValue) {
		String inDB = jedis.hget(subset, key.toString());
		jedis.hset(subset, key, newValue);
		return inDB.equals(oldValue);
	}

	@Override
	public String replace(String key, String value) {
		String result = jedis.hget(subset, key.toString());
		jedis.hset(subset, key, value);
		return result;
	}

}
