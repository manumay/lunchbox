package org.brainteam.lunchbox.util;

public class Pair<S, T> implements Comparable<Pair<S, T>> {
	
	private final S first;
	private final T second;
	
	public Pair(S first, T second) {
		if (first == null) {
			throw new IllegalArgumentException("first may not be null");
		}
		if (second == null) {
			throw new IllegalArgumentException("first may not be null");
		}
		this.first = first;
		this.second = second;
	}
	
	public S getFirst() {
		return first;
	}
	
	public T getSecond() {
		return second;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || o instanceof Pair == false) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Pair<S,T> otherPair = (Pair<S, T>) second;
		return getFirst().equals(otherPair.getFirst()) && getSecond().equals(otherPair.getSecond());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Pair<S, T> other) {
		if (first instanceof Comparable) {
			return ((Comparable<S>) first).compareTo(other.first);
		}
		return 0;
	}

}
