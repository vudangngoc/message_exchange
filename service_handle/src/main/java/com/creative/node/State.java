package com.creative.node;

import java.util.ArrayList;
import java.util.List;

public class State {
	private String name;
	private List<State> nextState;

	public String getName(){
		return this.name;
	}
	public String setName(String name){
		return this.name = name;
	}
	
	public List<State> getNextState(){
		return this.nextState;
	}
	public void setNextState(List<State> listState){
		this.nextState = listState;
	}
	public boolean addNextState(State state){
		if(this.nextState == null) this.nextState = new ArrayList<State>();
		boolean result = false;
		for(State s : this.nextState){
			if(s.getName().equals(state.getName())){
				result = true;
				break;
			}
		}
		if(!result) this.nextState.add(state);
		return result;
	}
}
