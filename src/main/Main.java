package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import entities.Graph;

public class Main {
	public static int x;
	public static int y;
	static JFrame f;

	public static void Resize(int x, int y) {
		f.setSize(x, y);
		x = f.getWidth();
		y = f.getHeight();
	}

	private static void createAndShowGui() {
		Long now = System.currentTimeMillis();
		Graph graph = new Graph();
		f = new JFrame("Algoritmica Grafurilor");
		f.setSize(1000, 1000);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		x = f.getWidth();
		y = f.getHeight();
		f.add(graph);
		f.setVisible(true);
		Long after = System.currentTimeMillis();
		System.out.println("Reading nodes and arcs from .xml file took " + (after - now) + " ms");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			createAndShowGui();
		});
	}
}