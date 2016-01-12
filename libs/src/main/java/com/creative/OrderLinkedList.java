package com.creative;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.creative.disruptor.MortalHandler;

public class OrderLinkedList<T extends Comparable<T>> {
  public OrderLinkedList(){
    shortcut = new ArrayList<OrderLinkedList<T>.Item>();
    this.size = 0;
    logger.setLevel(Level.DEBUG);
  }
  final static Logger logger = Logger.getLogger(OrderLinkedList.class);
  public static int PARTITION_SIZE = 50;
  private Item head;
  private List<Item> shortcut;
  private int lastUpdate = 0;
  private int size;
  public int getSize(){
    return size;
  }

  public boolean add(T data){
    if(data == null) return false;
    lastUpdate++;
    if(lastUpdate > PARTITION_SIZE)
      updateShortcut();
    if(head == null) {
      head = new Item(data);
      shortcut.add(head);
      size++;			
      return true;
    } else if(head.getData().compareTo(data) > 0){
      Item temp = new Item(data);
      temp.setNext(head);
      head = temp;
      shortcut.set(0, head);
      size++;
      return true;
    }
    for(int index = 1; index < shortcut.size(); index ++){
      if(shortcut.get(index).getData().compareTo(data) > 0){
        return insertFrom(shortcut.get(index - 1),data);
      }
    }
    return insertFrom(shortcut.get(shortcut.size() - 1), data);
  }

  private boolean insertFrom(OrderLinkedList<T>.Item item, T data) {
  	logger.debug("Insert item " + data.toString() + " from " + item.data.toString());
    if(item.getData().compareTo(data) > 0) {
    	logger.debug("Insert item " + data.toString() + " but have lower priority compare with " + item.data.toString());
    	return false;
    }
    Item temp = item;
    while(temp.getNext() != null && temp.getNext().getData().compareTo(data) < 0){
      temp = temp.getNext();
    }
    logger.debug("Insert after item " + temp.toString());
    Item temp2 = new Item(data);
    if(temp.getNext() != null){
      temp2.setNext(temp.getNext());
      temp.setNext(temp2);
    }else{
      temp.setNext(temp2);			
    }
    size++;
    return true;
  }

  public T removeHead(){
  	
    if(head == null || head.getData() == null){
      return null;
    }
    else {
      T data = head.getData();
      Item temp = head.getNext();
      head.setNext(null);
      head = temp;
      size--;
      if(head == null)
      	shortcut.clear();
      else
      	shortcut.set(0,head);
      if(logger.getLevel().equals(Level.DEBUG)){
      	if(shortcut.size() > 0)
      		logger.debug("Removing head " + data.toString() + ", head at shortcut is " + shortcut.get(0).data.toString());
      	else
      		logger.debug("Removing head " + data.toString() + ", head at shortcut is null");
      }
      return data;
    }
  }

  public void removeAll(){
    Item temp;
    while(head != null){
      temp = head.getNext();
      head.setNext(null);
      head = temp;
    }		
    size = 0;
    shortcut.clear();
  }

  public T getHead(){
    if(head != null) return head.getData();
    return null;
  }

  public T getAndRemoveSimilar(T object){
  	if(head == null || size < 1) return null;
    Item temp = head;
    if(head != null && head.getData().equals(object))
      return removeHead();
    if(head.getNext() == null && ! head.getData().equals(object))
    	return null;
    while(true){
      if(temp.getNext().getData() != null && temp.getNext().getData().equals(object)){
        T data = temp.getNext().getData();
        temp.setNext(temp.getNext().getNext());
        size--;
        for(int i = 0; i < shortcut.size();i++)
        	if(shortcut.get(i).equals(data))
        		shortcut.remove(i);
        logger.debug("get and remove " + data.toString());
        return data;
      }
      if(temp.getNext().getNext() != null) temp = temp.getNext();
      else break; //reach tail
    }
    
    return null;
  }

  public int updateShortcut(){
    Item temp = head;
    int count = 0;
    shortcut.clear();
    while(temp != null){			
      if(count % PARTITION_SIZE == 0) 
        shortcut.add(temp);
      count ++;
      temp = temp.getNext();
    }
    lastUpdate = 0;
    logger.debug("Update shortcut head is " + shortcut.get(0).data.toString());
    return shortcut.size();
  }

  public List<T> getAll(){
    ArrayList<T> result = new ArrayList<>();
    Item temp = head;
    while(temp != null){
      result.add(temp.data);
      temp = temp.getNext();
    }
    return result;
  }
  private class Item{
    public Item(T data){
      this.data = data;
    }
    private T data;
    private Item next;
    public T getData() {
      return data;
    }
    public void setData(T data) {
      this.data = data;
    }
    public Item getNext() {
      return next;
    }
    public void setNext(Item next) {
      this.next = next;
    }
  }
  
  /**
   * Work with class have toString that return Id or something unique
   * Input list will be removed all items appear in List
   * @param List for comparing 
   */
  public void diff(List<String> input) {
  	Item temp = head;
  	while(temp != null){
  		if(input.contains(temp.data.toString())){
  			//If item appear at input, remove it in input and move next
  			input.remove(temp.data.toString());
  			temp = temp.next;			
  		}
  		else{
  			//If item don't appear at input, remove it in list and move next
  			T dataToDelete = temp.data;
  			temp = temp.next;
  			this.getAndRemoveSimilar(dataToDelete);
  		}
  	}
  	//Remain items don't appear in list
  }
}
