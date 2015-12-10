package com.creative;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DoubleHashMap <V> implements ConcurrentMap<String,V>{

  private ConcurrentHashMap<String, ConcurrentHashMap<String,V>> data = new ConcurrentHashMap<>();
  @Override
  public int size() {
    int sum = 0;
    Enumeration<String> keys = data.keys();
    while(keys.hasMoreElements()){
      sum += data.get(keys.nextElement()).size();
    }
    return sum;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        return domain.contains(k[1]);
      }
    }
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public V get(Object key) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        return domain.get(k[1]);
      }
    }
    return null;
  }

  @Override
  public V put(String key, V value) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[1]);
      if(domain == null){
        domain = new ConcurrentHashMap<String,V>();
        data.put(k[1], domain);
      }
      return domain.put(k[0],value);
    }
    return null;
  }

  @Override
  public V remove(Object key) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        return domain.remove(k[1]);
      }
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends String, ? extends V> m) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void clear() {
    data.clear();
    
  }

  @Override
  public Set<String> keySet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<V> values() {
    Collection<V> result = new ArrayList<>();
    Enumeration<String> keys = data.keys();
    while(keys.hasMoreElements()){
      result.addAll(data.get(keys.nextElement()).values());
    }
    return result;
  }

  @Override
  public Set<java.util.Map.Entry<String, V>> entrySet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public V putIfAbsent(String key, V value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean remove(Object key, Object value) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        domain.remove(k[1]);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean replace(String key, V oldValue, V newValue) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        return domain.replace(k[1],oldValue,newValue);
      }
    }
    return false;
  }

  @Override
  public V replace(String key, V value) {
    String[] k = key.toString().split("@");
    if(k.length == 2){
      ConcurrentHashMap<String, V> domain = data.get(k[0]);
      if(domain != null){
        return domain.replace(k[1],value);
      }
    }
    return null;
  }

}
