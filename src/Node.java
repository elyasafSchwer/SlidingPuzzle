class Node<T extends State<?>>{
	private Node<T> parent;
	private T state;
	public Node(T t, Node<T> parent){
		this.parent = parent;
		this.state = t;
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj){
	    if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Node)) return false;
	    return (this.state.equals(((Node<T>)obj).state)) && (this.parent == ((Node<T>)obj).parent);
	 }
	public Node<T> getParent(){
		return this.parent;
	}
	public T getState(){
		return this.state;
	}
	@Override
	public String toString() {
		return "Node: " + "\n" + getState();
	}
}

class WeightNode<T extends State<?>> extends Node<T> implements Comparable<WeightNode<T>>{
	public static long date = 0; 
	private double path_weight;
	private double weight;
	private long Birthday;
	public WeightNode(T t, WeightNode<T> parent, double path_weight, double weight) {
		super(t, parent);
		this.path_weight = path_weight;
		this.weight = weight;
		this.Birthday = date++;
	}
	public double getWeight(){
		return this.weight;
	}
	public double getPathCost(){
		return this.path_weight;
	}
	public String toString() {
		return "WeightNode:\nPath: " + path_weight + "\n" + "Weight: " + weight + "\n" + getState();
	}

	public int compareTo(WeightNode<T> other) {
		int compare_weight = Double.compare(this.getWeight(), other.getWeight());
		return (compare_weight != 0) ? compare_weight : (int)(this.Birthday - other.Birthday);
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof WeightNode)) return false;
	    return super.equals(obj) && (this.path_weight == ((WeightNode<T>)obj).path_weight) && (this.weight == ((WeightNode<T>)obj).path_weight);
	}
}

class ColorNode<T extends State<?>> extends WeightNode<T>{
	private boolean colored;
	public ColorNode(T t, ColorNode<T> parent, double path_weight, double weight) {
		super(t, parent, path_weight, weight);
		this.colored = false;
	}
	public void paint(){
		this.colored = true;
	}
	public boolean is_colored(){
		return this.colored;
	}
	public String toString() {
		return "WeightNode:\nPath: " + getPathCost() + "\n" + "Weight: " + getWeight() + "\n" + colored +"\n" + getState();
	}
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}