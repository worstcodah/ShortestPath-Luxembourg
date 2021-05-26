package algorithms;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import entities.Edge;
import entities.Node;
import javafx.util.Pair;

public class Dijkstra {

	protected Node source;
	protected Node destination;
	protected int nrNodes;
	protected int[] cost_so_far;
	protected Node[] parent;
	protected LinkedList<Edge>[] adjacencylist;
	protected Map<Integer, Node> nodeCollection;

	public Dijkstra(int nrNodes, Node source, Node destination, LinkedList<Edge>[] adjacencylist,
			Map<Integer, Node> nodeCollection) {
		this.nrNodes = nrNodes;
		this.setSource(source);
		this.setDestination(destination);
		this.adjacencylist = adjacencylist;
		this.nodeCollection = nodeCollection;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public void dijkstraMinimumDistance() {
		cost_so_far = new int[nrNodes];
		parent = new Node[nrNodes];

		for (int i = 0; i < nrNodes; i++) {
			cost_so_far[i] = Integer.MAX_VALUE;
		}

		PriorityQueue<Pair<Long, Integer>> pq = new PriorityQueue<>(nrNodes, new Comparator<Pair<Long, Integer>>() {
			@Override
			public int compare(Pair<Long, Integer> p1, Pair<Long, Integer> p2) {

				long key1 = p1.getKey();
				long key2 = p2.getKey();
				return (int) (key1 - key2);
			}
		});

		Pair<Long, Integer> sourcePair = new Pair<Long, Integer>((long) 0, source.getId());
		cost_so_far[source.getId()] = 0;

		pq.offer(sourcePair);

		while (!pq.isEmpty()) {

			Pair<Long, Integer> extractedPair = pq.poll();

			int currentNode = extractedPair.getValue();

			// if (currentNode == destination.getId())
			// break;

			LinkedList<Edge> list = adjacencylist[currentNode];
			for (int i = 0; i < list.size(); i++) {
				Edge edge = list.get(i);

				long new_cost = cost_so_far[currentNode] + edge.getLength();
				if (new_cost < cost_so_far[edge.getEnd().getId()]) {
					cost_so_far[edge.getEnd().getId()] = (int) new_cost;
					parent[edge.getEnd().getId()] = nodeCollection.get(currentNode);
					Pair<Long, Integer> p = new Pair<>(new_cost, edge.getEnd().getId());
					pq.offer(p);
				}
			}
		}
	}

	public Stack<Node> drawDijkstraPath() {
		Stack<Node> stack = new Stack<Node>();

		stack.push(destination);
		Node currentNode = destination;
		while (source.getId() != currentNode.getId()) {
			currentNode = parent[currentNode.getId()];
			stack.push(currentNode);
		}

		System.out.println("Costul drumului obtinut cu Dijkstra = " + cost_so_far[destination.getId()]);

		return stack;
	}
}
