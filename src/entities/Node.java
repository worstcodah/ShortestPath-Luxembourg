package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import main.Main;

public class Node {

	private final Integer id;
	private final Long longitude;
	private final Long latitude;
	private Long screen_longitude;
	private Long screen_latitude;

	public Node(Integer id, Long longitude, Long latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		// screen_longitude = longitude;
		// screen_latitude = latitude;
	}

	public Integer getId() {
		return id;
	}

	public Long getLongitude() {
		return screen_longitude;
	}

	public Long getLatitude() {
		return screen_latitude;
	}

	public void drawNode(Graphics g, Long max_long, Long max_lat, Long min_long, Long min_lat, int node_diam) {
		Graphics2D g2d = (Graphics2D) g;

		double y1 = Main.x * Math.abs(max_lat - min_lat) / Math.abs(max_long - min_long) + 30;
		double x1 = Main.y * Math.abs(max_long - min_long) / Math.abs(max_lat - min_lat) + 60;

		double a = Math.abs(max_long - min_long) / (y1 - 30);
		double b = Math.abs(max_lat - min_lat) / (x1 - 60);

		double x = ((longitude - min_long) / a);
		double y = (latitude - min_lat) / b;

		x += 5;
		y += 10;
		screen_longitude = (long) x;
		screen_latitude = (long) y;

		Main.Resize((int) y1, (int) x1);

		Shape circle = new Ellipse2D.Double(x, y, node_diam, node_diam);
		g2d.setColor(Color.BLACK);
		g2d.draw(circle);

	}

	public Long getScreen_longitude() {
		return screen_longitude;
	}

	public Long getScreen_latitude() {
		return screen_latitude;
	}
}
