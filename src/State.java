abstract class State<T>{
	private T state;
	protected State(T t) {
		this.state = t;
	}
	abstract public boolean equals(Object other);
	abstract public int hashCode();
	abstract public String toString();
	public T getState(){
		return this.state;
	}
}