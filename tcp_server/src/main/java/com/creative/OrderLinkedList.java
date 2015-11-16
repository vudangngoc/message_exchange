package com.creative;

import java.util.ArrayList;
import java.util.List;

public class OrderLinkedList<T extends Comparable<T>> {
	public OrderLinkedList(){
		shortcut = new ArrayList<OrderLinkedList<T>.Item>();
		this.size = 0;
	}
	
	public static int PARTITION_SIZE = 100;
	private Item head;
	private List<Item> shortcut;
	private int size;
	public int getSize(){
		return size;
	}
	
	public boolean add(T data){
		if(data == null) return false;
		if(head == null) {
			head = new Item(data);
			shortcut.add(head);
			size++;			
			return true;
		} else if(head.getData().compareTo(data) > 0){
			Item temp = new Item(data);
			temp.setNext(head);
			head = temp;
			shortcut.add(0, head);
			size++;
			return true;
		}
		for(int index = 1; index < shortcut.size(); index ++){
			if(shortcut.get(index).getData().compareTo(data) > 0){
				insertFrom(shortcut.get(index - 1),data);
				return true;
			}
		}
		insertFrom(shortcut.get(shortcut.size() - 1), data);
		return true;
	}
	
	private void insertFrom(OrderLinkedList<T>.Item item, T data) {
		if(item.getData().compareTo(data) > 0) return;
		Item temp = item;
		while(temp.getNext() != null && temp.getNext().getData().compareTo(data) < 0){
			temp = temp.getNext();
		}
		Item temp2 = new Item(data);
		if(temp.getNext() != null){
			temp2.setNext(temp.getNext());
			temp.setNext(temp2);
		}else{
			temp.setNext(temp2);			
		}
		size++;
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
		Item temp = head;
		if(head != null && head.getData().equals(object))
			return removeHead();
		while(true){
			if(temp.getNext().getData() != null && temp.getNext().getData().equals(object)){
				T data = temp.getNext().getData();
				temp.setNext(temp.getNext().getNext());
				size--;
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
		return shortcut.size();
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
}
