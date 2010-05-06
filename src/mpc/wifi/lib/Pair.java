package mpc.wifi.lib;


public class Pair<F,S> {
	
	private F first;
	private S second;
	
	public Pair(){
	}
	
	public Pair(F f, S s){
		this.first=f;
		this.second=s;
	}

	
	public F getFirst() {
		return first;
	}

	
	public void setFirst(F first) {
		this.first = first;
	}

	
	public S getSecond() {
		return second;
	}

	
	public void setSecond(S second) {
		this.second = second;
	}
	
	
}
