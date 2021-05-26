package entities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class Edge {
	private Node start;
	private Node end;
	private Long length;

	public Edge(Node start, Node end, Long length) {
		this.start = start;
		this.end = end;
		this.length = length;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public void drawEdge(Graphics g, int node_diam) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		Shape line = new Line2D.Double(start.getScreen_longitude() + (node_diam / 2),
				start.getScreen_latitude() + (node_diam / 2), end.getScreen_longitude() + (node_diam / 2),
				end.getScreen_latitude() + (node_diam / 2));

		g2d.draw(line);

	}

}
