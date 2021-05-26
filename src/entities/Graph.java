package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import algorithms.BellmanFord;
import algorithms.Dijkstra;

import java.time.LocalTime;

public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int NODE_DIAM = 2;

	private Map<Integer, Node> nodeCollection = new HashMap<Integer, Node>();
	private List<Edge> edgeCollection = new ArrayList<Edge>();
	LinkedList<Edge>[] adjacencyList;
	Stack<Node> pathNodes = new Stack<Node>();
	private int nrNodes;

	public Long max_lat = Long.MIN_VALUE;
	public Long max_long = Long.MIN_VALUE;
	public Long min_long = Long.MAX_VALUE;
	public Long min_lat = Long.MAX_VALUE;

	public int nrClicks = 0;
	public Node nodeStart;
	public Node nodeEnd;
	public boolean visible1 = false;
	public boolean visible2 = false;

	private Scanner sc;

	public Graph() {
		sc = new Scanner(System.in);
		readFromFile();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {

					nodeStart = findNearestNode(e.getPoint());
					System.out.println("Source node= " + nodeStart.getId());
					repaint();
				}

				if (SwingUtilities.isRightMouseButton(e)) {
					nodeEnd = findNearestNode(e.getPoint());

					System.out.println("Destination node= " + nodeEnd.getId());
					repaint();

				}

				if (SwingUtilities.isMiddleMouseButton(e)) {

					if (nodeStart == null) {

						System.err.println("Nu ati ales nodul de start (click stanga) !");
					}

					if (nodeEnd == null) {

						System.err.println("Nu ati ales nodul destinatie (click dreapta) !");
					}

					if (nodeStart != null && nodeEnd != null) {

						if (nodeStart.getId() == nodeEnd.getId()) {

							System.err.println("Nodurile start si destinatie coincid !");
							System.out.println("Cost 0");
						}

						else {
							chooseAlgorithm();

							repaint();

						}
					}

				}

			}
		});

	}

	private void chooseAlgorithm() {
		System.out.println("Choose the algorithm:" + " \n 0 : Dijkstra" + "\n 1 : Bellman Ford ");
		int alg = sc.nextInt();
		switch (alg) {
		case 0:

			long before = System.currentTimeMillis();
			Dijkstra algorithm = new Dijkstra(nrNodes, nodeStart, nodeEnd, adjacencyList, nodeCollection);
			algorithm.dijkstraMinimumDistance();
			pathNodes = algorithm.drawDijkstraPath();

			long after = System.currentTimeMillis();
			System.out.println(" Dijkstra s execution took " + (after - before) + " milliseconds");
			repaint();
			break;
		case 1:

			BellmanFord algorithm1 = new BellmanFord(nrNodes, nodeStart, nodeEnd, edgeCollection, nodeCollection);
			before = System.currentTimeMillis();
			LocalTime now = LocalTime.now();
			System.out.println(now);
			algorithm1.bellmanFordMinimumDistance();
			after = System.currentTimeMillis();
			pathNodes = algorithm1.drawBellmanFordPath();
			now = LocalTime.now();
			System.out.println(now);

			System.out.println(" BellmanFord s execution took " + (after - before) + " milliseconds");
			repaint();
			break;
		default:
			System.out.println("Wrong number inserted.");
			break;
		}
	}

	private Node findNearestNode(Point nod) {

		Node closest_node = null;
		double nearest_dist = Integer.MAX_VALUE;
		for (Node node : nodeCollection.values()) {
			double distance = distance(nod.getX(), nod.getY(), node.getLongitude(), node.getLatitude());
			if (distance < nearest_dist) {
				nearest_dist = distance;
				closest_node = node;
			}
		}
		return closest_node;

	}

	private double distance(double x1, double y1, double x2, double y2) {
		double dx = Math.abs(x1 - x2);
		double dy = Math.abs(y1 - y2);
		return dx * dx + dy * dy;
	}

	public void addEdge(int from, int to, Long length) {
		//Edge edge = new Edge(nodeCollection.get(from), nodeCollection.get(to), length);
		adjacencyList[from].addFirst(new Edge(nodeCollection.get(from), nodeCollection.get(to), length));

		//edge = new Edge(nodeCollection.get(to), nodeCollection.get(from), length);
		adjacencyList[to].addFirst(new Edge(nodeCollection.get(to), nodeCollection.get(from), length)); // for undirected graph
	}

	@SuppressWarnings("unchecked")
	public void readFromFile() {

		/*
		 * try {
		 * 
		 * Long now = System.currentTimeMillis(); //System.out.println("Time now: " +
		 * now.toString());
		 * 
		 * File file = new File("src/map2.xml");
		 * 
		 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 * DocumentBuilder db = dbf.newDocumentBuilder(); Document doc = db.parse(file);
		 * doc.getDocumentElement().normalize();
		 * 
		 * NodeList nodeList = doc.getElementsByTagName("node"); int size =
		 * nodeList.getLength();
		 * 
		 * nrNodes = size; adjacencyList = new LinkedList[size]; for (int i = 0; i <
		 * size; i++) { adjacencyList[i] = new LinkedList<Edge>(); }
		 * 
		 * for (int itr = 0; itr < size; itr++) { Node node = nodeList.item(itr); if
		 * (node.getNodeType() == Node.ELEMENT_NODE) { Element eElement = (Element)
		 * node; long id = Long.parseLong(eElement.getAttribute("id")); Long longitude =
		 * Long.parseLong(eElement.getAttribute("longitude")); if (longitude > max_long)
		 * max_long = longitude; if (longitude < min_long) min_long = longitude; Long
		 * latitude = Long.parseLong(eElement.getAttribute("latitude")); if (latitude >
		 * max_lat) max_lat = latitude; if (latitude < min_lat) min_lat = latitude;
		 * 
		 * nodeCollection.put((int) id, new GraphNode((int) id, longitude, latitude)); }
		 * }
		 * 
		 * NodeList arcList = doc.getElementsByTagName("arc"); size =
		 * arcList.getLength(); for (int index = 0; index < size; index++) { Node arc =
		 * arcList.item(index); if (arc.getNodeType() == Node.ELEMENT_NODE) { Element
		 * eElement = (Element) arc; long from =
		 * Long.parseLong(eElement.getAttribute("from")); long to =
		 * Long.parseLong(eElement.getAttribute("to")); long length =
		 * Long.parseLong(eElement.getAttribute("length"));
		 * 
		 * edgeCollection.add(new Edge(nodeCollection.get((int) from),
		 * nodeCollection.get((int) to), length)); addEdge((int) from, (int) to,
		 * length);
		 * 
		 * }
		 * 
		 * } repaint();
		 * 
		 * Long after = System.currentTimeMillis();
		 * //System.out.println("Time after reading XML file: " + (after-now));
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;

		try {
			saxParser = parserFactory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DefaultHandler parserHandler = new DefaultHandler() {

			public void startElement(String uri, String localName, String name, Attributes attributes) {

				if (name.equals("node")) {
					// ++nrNodes;

					int length = attributes.getLength();
					int id = 0;
					Long longitude = null, latitude = null;

					for (int i = 0; i < length; ++i) {

						String attributeName = attributes.getLocalName(i);

						if (attributeName.equals("id")) {
							String idString = attributes.getValue(i);
							id = Integer.parseInt(idString);

						}

						if (attributeName.equals("longitude")) {
							String longitudeString = attributes.getValue(i);
							longitude = Long.parseLong(longitudeString);
							if (longitude > max_long)
								max_long = longitude;
							if (longitude < min_long)
								min_long = longitude;

						}

						if (attributeName.equals("latitude")) {
							String latitudeString = attributes.getValue(i);
							latitude = Long.parseLong(latitudeString);

							if (latitude > max_lat)
								max_lat = latitude;
							if (latitude < min_lat)
								min_lat = latitude;

						}

					}
					nodeCollection.put(id, new Node(id, longitude, latitude));
				}

				else if (adjacencyList == null || adjacencyList.length == 0) {

					adjacencyList = new LinkedList[nodeCollection.size()];
					for (int i = 0; i < adjacencyList.length; ++i) {
						adjacencyList[i] = new LinkedList<Edge>();
					}

				}

				if (name.equals("arc")) {

					int length = attributes.getLength();
					int from = 0, to = 0;
					Long arcLength = null;

					for (int i = 0; i < length; ++i) {

						String attributeName = attributes.getLocalName(i);

						if (attributeName.equals("from")) {
							String fromString = attributes.getValue(i);
							from = Integer.parseInt(fromString);

						}

						if (attributeName.equals("to")) {
							String toString = attributes.getValue(i);
							to = Integer.parseInt(toString);

						}

						if (attributeName.equals("length")) {
							String lengthString = attributes.getValue(i);
							arcLength = Long.parseLong(lengthString);

						}

					}
					edgeCollection.add(new Edge(nodeCollection.get(from), nodeCollection.get(to), arcLength));
					addEdge(from, to, arcLength);

				}
			}

		};

		try {
			saxParser.parse("src/map2.xml", parserHandler);
			nrNodes = (nodeCollection.size());
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		for (Node node : nodeCollection.values()) {
			node.drawNode(g, max_long, max_lat, min_long, min_lat, NODE_DIAM);

		}

		for (Edge edge : edgeCollection) {
			edge.drawEdge(g, NODE_DIAM);
		}

		Graphics2D g2d = (Graphics2D) g;

		if (nodeStart != null) {
			g2d.setColor(Color.RED);
			Shape circle = new Ellipse2D.Double(nodeStart.getLongitude() - 5, nodeStart.getLatitude() - 5, 10, 10);
			g2d.draw(circle);
			g2d.fill(circle);
		}
		if (nodeEnd != null) {
			Shape circle = new Ellipse2D.Double(nodeEnd.getLongitude() - 5, nodeEnd.getLatitude() - 5, 10, 10);
			g2d.draw(circle);
			g2d.fill(circle);
		}

		g2d.setStroke(new java.awt.BasicStroke(3));
		if (!pathNodes.empty()) {
			g2d.setColor(Color.RED);
			while (!pathNodes.empty()) {
				Node start = pathNodes.peek();

				pathNodes.pop();
				if (!pathNodes.empty()) {
					Node end = pathNodes.peek();
					Shape line = new Line2D.Double(start.getScreen_longitude() + NODE_DIAM / 2,
							start.getScreen_latitude() + NODE_DIAM / 2, end.getScreen_longitude() + NODE_DIAM / 2,
							end.getScreen_latitude() + NODE_DIAM / 2);
					g2d.draw(line);
				}
			}
		}
	}
}
