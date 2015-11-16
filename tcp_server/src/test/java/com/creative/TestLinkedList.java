package com.creative;
import static org.junit.Assert.*;


import org.junit.Test;

public class TestLinkedList {
	@Test
	public void testAdd(){
		//Given
		OrderLinkedList<Item> list = new OrderLinkedList<>();
		//When
		list.add(new Item(3));
		list.add(new Item(1));
		list.add(new Item(2));
		list.add(new Item(4));
		list.add(new Item(3));
		//Then
		assertEquals(5,list.getSize());
		assertEquals(1,list.removeHead().data);
		assertEquals(2,list.removeHead().data);
		assertEquals(3,list.removeHead().data);
		assertEquals(3,list.removeHead().data);
		assertEquals(4,list.removeHead().data);
		assertEquals(0,list.getSize());		
	}

	@Test
	public void testRemoveSimilar(){
		//Given
		OrderLinkedList<Item> list = new OrderLinkedList<>();
		//When
		list.add(new Item(3));
		list.add(new Item(1));
		list.add(new Item(2));
		list.add(new Item(4));
		list.add(new Item(3));
		//Then
		Item head1 = list.getAndRemoveSimilar(new Item(2));
		assertEquals(2,head1.data);
		assertEquals(4,list.getSize());

	}
	@Test
	public void testRemoveHead(){
		//Given
		OrderLinkedList<Item> list = new OrderLinkedList<>();
		//When
		list.add(new Item(3));
		list.add(new Item(1));
		list.add(new Item(2));
		list.add(new Item(4));
		list.add(new Item(3));
		Item head0 = list.getHead();
		//Then
		Item head1 = list.removeHead();
		assertEquals(head0.data,head1.data);

	}

	@Test
	public void testRemoveAll(){
		//Given
		OrderLinkedList<Item> list = new OrderLinkedList<>();
		//When
		list.add(new Item(3));
		list.add(new Item(1));
		list.add(new Item(2));
		list.add(new Item(4));
		list.add(new Item(3));
		//Then
		list.removeAll();
		assertNull(list.getHead());
		assertEquals(0,list.getSize());

	}

	@Test
	public void testUpdateShortcut(){
		//Given
		OrderLinkedList<Item> list = new OrderLinkedList<>();
		//When
		for(int i = 0;i<500;i++)
			list.add(new Item(i));
		//Then		
		assertEquals(5,list.updateShortcut());
	}

	private static class Item implements Comparable<Item>{
		public Item(int data){
			this.data = data;
		}
		public int data;
		@Override
		public int compareTo(Item o) {
			return this.data - o.data;
		}
		@Override
		public boolean equals(Object o){
			if(o instanceof Item){
				return ((Item)o).data == this.data;
			}
			return false;
		}
	}
}
