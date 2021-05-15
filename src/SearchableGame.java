import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

abstract class SearchableGame<T extends State<?>>{
	private T start;
	private T goal;
	abstract protected Vector<T> operator(T state);
	abstract protected double heuristic(T sate);
	abstract protected double get_cost_for_step(T t1, T t2);
	abstract protected char get_char_for_step(T t1, T t2);
	
	protected void setStart(T start){
		this.start = start;
	}
	
	protected void setGoal(T goal){
		this.goal = goal;		
	}
	
	public T get_start(){
		return this.start;
	}
	
	public T get_goal(){
		return this.goal;
	}
	
	public Result result_from_graph(Node<T> n, long num, long time){
		if(n.getParent() == null) return new Result("", num, 0, time);
		Stack<T> path = new Stack<T>();
		String path_string = "";
		double cost = 0;
		while(n != null){
			path_string = 	(n.getParent()==null) ? path_string.substring(0, path_string.length()-1) :
							"" + get_char_for_step(n.getParent().getState(), n.getState()) + '-' + path_string;
			
			cost =	(n.getParent()==null) ?
					cost :
					cost + get_cost_for_step(n.getParent().getState(), n.getState());
			path.push(n.getState());
			n = n.getParent();
		}
		return new Result(path_string, num, cost, time);
	}
	
	public Result BFS(){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		long startTime = System.currentTimeMillis();
		long num = 0;
		Queue<Node <T>> quere = new LinkedList<Node <T>>();
		HashSet<T> hash = new HashSet<T>();
		quere.add(new Node<T>(this.start, null));
		hash.add(this.start);
		while(!(quere.isEmpty())){
			Node<T> n = quere.remove();
			num++;
			Vector<T> childs = operator(n.getState());
			for (T state : childs) {
				if(state.equals(this.goal)){
					return result_from_graph(new Node<T>(state, n), num, System.currentTimeMillis()-startTime);
				}
				if(!hash.contains(state)){
					hash.add(state);
					quere.add(new Node<T>(state, n));
				}
			}
		}
		return null;
	}
	
	static long DFIDstartTime;
	static long DFIDNum;
	static long DFID_NUM_OF_ALL_NODE;
	static long DFID_NUM_OF_ALL_NODE_PAST;
	public Result DFID(){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		DFIDstartTime = System.currentTimeMillis();
		DFID_NUM_OF_ALL_NODE_PAST = Long.MIN_VALUE;
		DFIDNum = 0;
		long l = 1;
		HashSet<T> loop_avoidence = new HashSet<T>();
		while(true){
			DFID_NUM_OF_ALL_NODE = 0;
			loop_avoidence.clear();
			loop_avoidence.add(start);
			Result result_until_depth_l = limit_DFS(l, new Node<T>(start, null), loop_avoidence);
			if (result_until_depth_l != null){
				return result_until_depth_l;
			}
			loop_avoidence.remove(start);
			l++;
			if(DFID_NUM_OF_ALL_NODE == DFID_NUM_OF_ALL_NODE_PAST) return null;
			DFID_NUM_OF_ALL_NODE_PAST = DFID_NUM_OF_ALL_NODE;
		}
	}
	
	private Result limit_DFS(long high, Node<T> n, HashSet<T> loop_avoidence){
		DFID_NUM_OF_ALL_NODE++;
		if(high == 0) return null;
		DFIDNum++;
		Vector<T> childs = operator(n.getState());
		childs.removeIf(t->loop_avoidence.contains(t));
		loop_avoidence.addAll(childs);
		for (T state : childs) {
			if(state.equals(goal)){
				return result_from_graph(new Node<T>(state, n), DFIDNum, System.currentTimeMillis()-DFIDstartTime);
			}
			Result result_from_child = limit_DFS(high-1, new Node<T>(state, n), loop_avoidence);
			if(result_from_child != null) return result_from_child;
			loop_avoidence.remove(state);
		}
		return null;
	}
	
	abstract class TwoArgInterface {
	    public abstract double operation(WeightNode<T> a, T b);
	}
	
	private Result cost_search(TwoArgInterface f){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		long startTime = System.currentTimeMillis();
		long num = 0;
		PriorityQueue<WeightNode<T>> quere = new PriorityQueue<WeightNode<T>>();
		HashMap<T, WeightNode<T>> hash = new HashMap<T, WeightNode<T>>();
		WeightNode<T> root = new WeightNode<T>(start, null, 0, heuristic(start));
		quere.add(root);
		hash.put(root.getState(), root);
		while(!(quere.isEmpty())){
			WeightNode<T> n = quere.poll();
			num++;
			if(n.getState().equals(this.goal)){
				return result_from_graph(n, num, System.currentTimeMillis()-startTime);
			}
			Vector<T> childs = operator(n.getState());
			for (T state : childs) {
				WeightNode<T> wnode = new WeightNode<T>(state, n, n.getPathCost()+get_cost_for_step(n.getState(), state), f.operation(n, state));
				if(hash.containsKey(state) && hash.get(state).getWeight() > wnode.getWeight()){
					WeightNode<T> old = hash.remove(state);
					quere.remove(old);
				}
				if(!hash.containsKey(state)){
					hash.put(state, wnode);
					quere.add(wnode);
				}
			}
		}
		return null;
	}
	
	public Result UCS(){
		return cost_search(new TwoArgInterface() {
			public double operation(WeightNode<T> a, T b) {
				return (a.getPathCost() + get_cost_for_step(a.getState(), b));
			}
		});
	}
	
	public Result GREEDY(){
		return cost_search(new TwoArgInterface() {
			public double operation(WeightNode<T> a, T b) {
				return heuristic(b);
			}
		});
	}
	public Result ASTAR(){
		return cost_search(new TwoArgInterface() {
			public double operation(WeightNode<T> a, T b) {
				return a.getPathCost()+get_cost_for_step(a.getState(), b) + heuristic(b);
			}
		});
	}
	public Result IDASTAR(){
		long startTime = System.currentTimeMillis();
		long num = 0;
		Stack<ColorNode<T>> stack = new Stack<ColorNode<T>>();
		HashMap<T, ColorNode<T>> hash = new HashMap<T, ColorNode<T>>();
		ColorNode<T> root;
		double threshold = Double.MIN_VALUE;
		double next_threshold = heuristic(start);
		while(threshold != next_threshold){
			threshold = next_threshold;
			next_threshold = Double.POSITIVE_INFINITY;
			root = new ColorNode<T>(start, null, 0, heuristic(start));
			stack.push(root);
			hash.put(root.getState(), root);
			while(!(stack.isEmpty())){
				ColorNode<T> n = stack.peek();
				if(n.getState().equals(this.goal) && n.getPathCost() <= threshold){
					return result_from_graph(n, num, System.currentTimeMillis()-startTime);
				}
				else if(n.is_colored()){
					stack.pop();
					hash.remove(n.getState());
				}
				else if(!hash.containsKey(n.getState())){
					stack.pop();
					hash.remove(n.getState());
				}
				else if(n.getWeight() > threshold){
					stack.pop();
					hash.remove(n.getState());
					next_threshold = Math.min(next_threshold, n.getWeight());
				}
				else{
					num++;
					n.paint();
					Vector<T> childs = operator(n.getState());
					Stack<ColorNode<T>> childs_wnode = new Stack<ColorNode<T>>();
					for (T state : childs) {
						ColorNode<T> wnode = new ColorNode<T>(	state, n, n.getPathCost()+get_cost_for_step(n.getState(), state),
																n.getPathCost()+get_cost_for_step(n.getState(), state)+heuristic(state)
																);
						if(wnode.getState().equals(this.goal) && wnode.getPathCost() <= threshold){
							return result_from_graph(wnode, num, System.currentTimeMillis()-startTime);
						}
						if(!hash.containsKey(state) || hash.get(state).getWeight() > wnode.getWeight()){
							stack.remove(hash.get(state));
							hash.put(state, wnode);
							childs_wnode.push(wnode);
						}
					}
					while (!childs_wnode.isEmpty()) {
						ColorNode<T> wnode = childs_wnode.pop();
						stack.push(wnode);
						hash.put(wnode.getState(), wnode);
					}
				}
			}
		}
		return null;
	}
	public Result DFBnB(){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		long startTime = System.currentTimeMillis();
		ColorNode<T> correct_goal = null;
		double correct_cost = Double.POSITIVE_INFINITY;
		long num = 0;
		Stack<ColorNode<T>> stack = new Stack<ColorNode<T>>();
		HashMap<T, ColorNode<T>> hash = new HashMap<T, ColorNode<T>>();
		ColorNode<T> root = new ColorNode<T>(start, null, 0, heuristic(start));
		stack.push(root);
		hash.put(root.getState(), root);
		while(!stack.isEmpty()){
			ColorNode<T> n = stack.peek();
			if(n.is_colored()) {
				stack.pop();
				hash.remove(n.getState());
			}
			else{
				num++;
				n.paint();
				if(n.getWeight() < correct_cost){
					Vector<T> childs = operator(n.getState());
					List<ColorNode<T>> wnode_childs = new ArrayList<ColorNode<T>>();
					for (T state : childs) {
						ColorNode<T> wnode = new ColorNode<T>(	state, n, n.getPathCost()+get_cost_for_step(n.getState(), state),
																n.getPathCost()+get_cost_for_step(n.getState(), state)+heuristic(state)
															);
						if(wnode.getWeight() < correct_cost) {
							if(state.equals(goal)){
								correct_goal = wnode;
								correct_cost = wnode.getPathCost();
							}
							else if(hash.containsKey(state) && (hash.get(state).getWeight() > wnode.getWeight())) {
								stack.remove(hash.get(state));
								hash.remove(state);
								wnode_childs.add(wnode);
							}
							else if(!hash.containsKey(state)){
								wnode_childs.add(wnode);
							}

						}
					}
					while (!wnode_childs.isEmpty()) {
						ColorNode<T> wnode = Collections.max(wnode_childs);
						hash.put(wnode.getState(), wnode);
						stack.push(wnode);
//						hash.put(wnode.getState(), wnode);
						wnode_childs.remove(wnode);
					}

				}
			}
		}
		return (correct_goal == null) ? 	null :
											result_from_graph(correct_goal, num, System.currentTimeMillis()-startTime);
	}
	
	
	
	
	static long RECURSIVE_IDASTAR_START_TIME;
	static long RECURSIVE_IDASTAR_START_NUM;
	static long RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE;
	static long RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE_PAST;
	static double NEXT_THRESOLD;
	
	public Result RECURSIVE_IDASTAR(){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		RECURSIVE_IDASTAR_START_TIME = System.currentTimeMillis();
		RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE_PAST = Long.MIN_VALUE;
		RECURSIVE_IDASTAR_START_NUM = 0;
		double threshold = heuristic(start);
		HashMap<T, WeightNode<T>> loop_avoidence = new HashMap<T, WeightNode<T>>();
		while(true){
			RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE = 0;
			NEXT_THRESOLD = Double.POSITIVE_INFINITY;
			loop_avoidence.clear();
			WeightNode<T> start_node = new WeightNode<T>(start, null, 0, 0);
			loop_avoidence.put(start_node.getState(), start_node);
			Result result_until_threshold = limit_A_STAR(threshold, start_node, loop_avoidence);
			if (result_until_threshold != null){
				return result_until_threshold;
			}
			loop_avoidence.remove(start);
			threshold = NEXT_THRESOLD;
			if(RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE == RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE_PAST) return null;
			RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE_PAST = RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE;
		}
	}
	
	private Result limit_A_STAR(double threshold, WeightNode<T> n, HashMap<T, WeightNode<T>> loop_avoidence){
		RECURSIVE_IDASTAR_START_TIME_NUM_OF_ALL_NODE++;
		if(n.getWeight() > threshold) {
			NEXT_THRESOLD = Math.min(NEXT_THRESOLD, n.getWeight());
			return null;
		}
		RECURSIVE_IDASTAR_START_NUM++;
		Vector<T> childs = operator(n.getState());
		Vector<WeightNode<T>> wnode_childs = new Vector<WeightNode<T>>();
		for (T state : childs) {
			WeightNode<T> wnode = new WeightNode<T>(	state, n, n.getPathCost()+get_cost_for_step(n.getState(), state),
														n.getPathCost()+get_cost_for_step(n.getState(), state)+heuristic(state));
			wnode_childs.add(wnode);
			if(!loop_avoidence.containsKey(state) || loop_avoidence.get(state).getWeight() > wnode.getWeight()){
				loop_avoidence.put(state, wnode);
			}
		}
		for (WeightNode<T> wnode : wnode_childs) {
			if(wnode.getState().equals(goal) && wnode.getPathCost() <= threshold){
				return result_from_graph(wnode, RECURSIVE_IDASTAR_START_NUM, System.currentTimeMillis()-RECURSIVE_IDASTAR_START_TIME);
			}
			Result result_from_child = null;
			if(loop_avoidence.containsKey(wnode.getState()) && loop_avoidence.get(wnode.getState()).equals(wnode)){
				result_from_child = limit_A_STAR(threshold, wnode, loop_avoidence);
				loop_avoidence.remove(wnode.getState());
			}
			if(result_from_child != null) return result_from_child;
		}
		return null;
	}
	
	static long RECURSIVE_DFBNB_START_TIME;
	static long RECURSIVE_DFBNB_START_NUM;
	static double CORRECT_COST;
	static ColorNode<?> DFBNB_RESULT;
	
	@SuppressWarnings("unchecked")
	public Result RECURSIVE_DFBNB(){
		if(this.start.equals(this.goal)){
			return new Result("", 0, 0, 0);
		}
		RECURSIVE_DFBNB_START_TIME = System.currentTimeMillis();
		RECURSIVE_DFBNB_START_NUM = 0;
		CORRECT_COST = Double.POSITIVE_INFINITY;
		DFBNB_RESULT = null;
		HashMap<T, ColorNode<T>> loop_avoidence = new HashMap<T, ColorNode<T>>();
		ColorNode<T> start_node = new ColorNode<T>(start, null, 0, 0);
		loop_avoidence.put(start_node.getState(), start_node);
		limit_weight_dfs(start_node, loop_avoidence);
		loop_avoidence.remove(start_node.getState());
		return (DFBNB_RESULT == null) ? null : result_from_graph(((Node<T>) DFBNB_RESULT), RECURSIVE_DFBNB_START_NUM, System.currentTimeMillis()-RECURSIVE_DFBNB_START_TIME);
	}
	
	private void limit_weight_dfs(ColorNode<T> n, HashMap<T, ColorNode<T>> loop_avoidence){
		if(n.is_colored()) return;
		RECURSIVE_DFBNB_START_NUM++;
		if(n.getWeight() >= CORRECT_COST) {
			return;
		}
		Vector<T> childs = operator(n.getState());
		List<ColorNode<T>> wnode_child = new ArrayList<ColorNode<T>>();
		for (T state : childs) {
			ColorNode<T> wnode = new ColorNode<T>(	state, n, n.getPathCost()+get_cost_for_step(n.getState(), state),
													n.getPathCost()+get_cost_for_step(n.getState(), state)+heuristic(state));			
			if(wnode.getWeight() < CORRECT_COST) {
				if(wnode.getState().equals(goal)){
					DFBNB_RESULT = wnode;
					CORRECT_COST = wnode.getPathCost();
				}
				else if(loop_avoidence.containsKey(state)) {
					if(loop_avoidence.get(state).getWeight() > wnode.getWeight()) {
						loop_avoidence.get(state).paint();
						wnode_child.add(wnode);
						loop_avoidence.put(state, wnode);
					}
				}
				else {
					wnode_child.add(wnode);
					loop_avoidence.put(state, wnode);
					}
			}
		}
		while(!wnode_child.isEmpty()){
			ColorNode<T> wnode = Collections.min(wnode_child);
			if(!loop_avoidence.containsKey(wnode.getState()) || !loop_avoidence.get(wnode.getState()).equals(wnode)) continue;
			limit_weight_dfs(wnode,loop_avoidence);
			loop_avoidence.remove(wnode.getState());
			wnode_child.remove(wnode);
		}
	}
}