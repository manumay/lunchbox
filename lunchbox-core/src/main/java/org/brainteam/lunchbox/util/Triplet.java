package org.brainteam.lunchbox.util;

public class Triplet<S,T,U> extends Pair<S,T> {
	
	private final U third;
	
	public Triplet(S first, T second, U third) {
		super(first, second);
		this.third = third;
	}
	
	public U getThird() {
		return third;
	}

}
