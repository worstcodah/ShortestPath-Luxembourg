package algorithms;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import entities.Edge;
import entities.Node;

public class BellmanFord {

	protected Node source;
	protected Node destination;
	protected int nrNodes;
	protected int[] cost_so_far;
	protected Node[] parent;
	protected List<Edge> edgeCollection;
	protected Map<Integer, Node> nodeCollection;

	public BellmanFord(int nrNodes, Node source, Node destination, List<Edge> edgeCollection,
			Map<Integer, Node> nodeCollection) {

		this.nrNodes = nrNodes;
		this.setSource(source);
		this.setDestination(destination);
		this.edgeCollection = edgeCollection;
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

	public void bellmanFordMinimumDistance() {

		cost_so_far = new int[nrNodes];
		parent = new Node[nrNodes];

		for (int i = 0; i < nrNodes; ++i) {
			cost_so_far[i] = Integer.MAX_VALUE;
		}

		cost_so_far[source.getId()] = 0;
		boolean settled;
		int currentNode;
		for (currentNode = 0; currentNode < nrNodes - 1; ++currentNode) {
			settled = false;

			for (Edge edge : edgeCollection) {
				Node nodeStart = edge.getStart();
				Node nodeEnd = edge.getEnd();

				Long length = edge.getLength();

				if (cost_so_far[nodeStart.getId()] + length < cost_so_far[nodeEnd.getId()]) {

					cost_so_far[nodeEnd.getId()] = (int) (cost_so_far[nodeStart.getId()] + length);
					parent[nodeEnd.getId()] = nodeStart;

					settled = true;
				}

			}
			if (settled == false)
				break;

		}
	}

	public Stack<Node> drawBellmanFordPath() {
		Stack<Node> stack = new Stack<Node>();

		stack.push(destination);
		Node currentNode = destination;

		while (source.getId() != currentNode.getId()) {

			currentNode = parent[currentNode.getId()];
			stack.push(currentNode);

		}

		System.out.println("Costul drumului obtinut cu Bellman-Ford = " + cost_so_far[destination.getId()]);

		return stack;
	}
}
