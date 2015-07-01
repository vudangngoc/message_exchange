package com.creative.node;

import java.util.ArrayList;
import java.util.List;

public class NodeType {
	private List<State> stateList;
	private State currentState;

	public List<State> getStateList(){
		return this.stateList;
	} 
	public void setStateList(List<State> stateList){
		this.stateList = stateList;
		if(this.stateList.size() > 0) currentState = this.stateList.get(0);
	}
	public boolean addState(State state){
		if(this.stateList == null) this.stateList = new ArrayList<State>();
		boolean result = false;
		for(State s : this.stateList){
			if(s.getName().equals(state.getName())){
				result = true;
				break;
			}
		}
		if(!result) this.stateList.add(state);
		if(currentState == null) currentState = stateList.get(0);
		return result;
	}
	public boolean moveNextState(State state){
		if(this.currentState == null){
			this.addState(state);
		}
		else{			
			if(currentState.getNextState() != null){
				for(State s : currentState.getNextState()){
					if(s.getName().equals(state.getName())){
						currentState = s;
						return true;
					}
				}
			} else{
				return false;
			}
		}
		return false;
	}
}
