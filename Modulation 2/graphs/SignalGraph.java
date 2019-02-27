package graphs;

import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import signals.Signal;
import signals.AMModulatedSignal;
import geom.Point;

public class SignalGraph extends JPanel {

	private JLabel coords = new JLabel("<html><font color='#EEEEEE'>0</font></html>");

	private JPanel getCoordsPane() {
		JPanel coordsPane = new JPanel();
    	coordsPane.add(this.coords);
    	return coordsPane;
	}
	public SignalGraph(Signal s) {
		this(s, 500, 2, s.getPeriod(), s.getAmplitude());
	}
	public SignalGraph(Signal s, int density) {
		this(s, density, 2, s.getPeriod(), s.getAmplitude());
	}
    public SignalGraph(Signal s, int density, int periodsToDraw) {
    	this(s, density, periodsToDraw, s.getPeriod(), s.getAmplitude());
    }
    protected SignalGraph(Signal s, int density, int periodsToDraw, double maxX, double maxY) {
    	super.setLayout(new BorderLayout());
    	super.add(this.getCoordsPane(), BorderLayout.NORTH);
    	super.add(new GraphPane(s, density, periodsToDraw, maxX, maxY), BorderLayout.CENTER);
	}
	private class GraphPane extends JPanel {

		private static final int H_GAP = 20;
		private static final int V_GAP = 40;
		private HashMap<RenderingHints.Key, Object> m = null;
		private double offset = 0.0;
		private double scaleX = 0.0;
		private double scaleY = 0.0;
		private double maxX = 0.0;
		private double maxY = 0.0;
		private double minY = 0.0;
		private int periodsToDraw = 0;
		private Point[] points = null;
		private Signal s = null;

		private void initPoints(int density) {
			this.points = new Point[density];
			double y = 0;
			double t = 0;
			double dt = this.maxX / density;
			for (int i=0; i<density; i++, t += dt) {
				y = this.s.v(t);
				points[i] = new Point(t, y);
				if (y > this.maxY)
					this.maxY = y;
				else if (y < this.minY)
					this.minY = y;
			}		
		}
		private void addMouseListeners() {
			super.addMouseMotionListener( new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					double xCoord = ((e.getX() - H_GAP) / scaleX);
					double yCoord = (((e.getComponent().getHeight() / 2) - e.getY()) / scaleY);
					coords.setText("(" + xCoord + "; " + yCoord + ")");
				}
			});
			super.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					coords.setText("<html><font color='#EEEEEE'>0</font></html>");
				}
			});
		}
		private void initRenderingHints() {
			this.m = new HashMap<RenderingHints.Key, Object>(3);
			m.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			m.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			m.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		}
		private GraphPane(Signal s, int density, int periodsToDraw, double maxX, double maxY) {
			this.s = s;
	    	this.setPeriodsToDraw(periodsToDraw);
	    	this.maxX = maxX * this.periodsToDraw + (maxX * this.periodsToDraw / density);
	    	this.maxY = maxY;
			this.minY = -this.maxY;
	    	this.offset = super.getHeight() / 2.0;
			if (this.s.v(this.maxX * this.periodsToDraw / density) != 0)
				density++;
	    	this.initPoints(this.checkDensity(density));
	    	this.updateScale();
	    	this.addMouseListeners();
			this.initRenderingHints();
		}
		private int checkDensity(int density) {
			if (density > 499)
	 			return density;
	 		return 500;
		}
	    private void setPeriodsToDraw(int periodsToDraw) {
	 		if (periodsToDraw > 0)
	 			this.periodsToDraw = periodsToDraw;
	 	}
	    private void updateScale() {
			/*
	  		 *	Ottenuta rapportando la larghezza della finestra di disegno
	  		 *	con la x massima
	  		 */
			this.scaleX = (super.getWidth() - (H_GAP * 2)) / this.maxX;
	    	this.scaleY = (super.getHeight() - V_GAP) / (this.maxY - this.minY);
	    	/*
	  		 *	posizione del grafico nella finestra di disegno
	  		 */
	    	this.offset = super.getHeight() / 2.0;
		}
		private void drawEnvelopes(Graphics2D g2D) {
			int x1, x2, y1, y2;
			AMModulatedSignal s = (AMModulatedSignal)(this.s);
			g2D.setColor(Color.RED);
	  		for (int i=0; i<this.points.length - 1; i++) {
			 	x1 = (int)(this.scaleX * this.points[i].getX()) + H_GAP;
			  	y1 = (int)(this.offset - (this.scaleY * s.r(this.points[i].getX())));
			  	x2 = (int)(scaleX * this.points[i + 1].getX()) + H_GAP;
			  	y2 = (int)(this.offset - (this.scaleY * s.r(this.points[i + 1].getX())));
				g2D.drawLine(x1, y1, x2, y2);
				y1 = (int)(this.offset - (this.scaleY * -s.r(this.points[i].getX())));
				y2 = (int)(this.offset - (this.scaleY * -s.r(this.points[i + 1].getX())));
				g2D.drawLine(x1, y1, x2, y2);
			}
		}
		private void drawSignal(Graphics2D g2D) {
			int x1, x2, y1, y2;
			g2D.setColor(Color.blue);
			//g2D.setClip(0, (int)(this.offset - (this.s.getAmplitude() * this.scaleY)), (int)(super.getWidth()), (int)((this.s.getPeakToPeakValue() * this.scaleY) + 2));
	  		for (int i=0; i<this.points.length - 1; i++) {
			 	x1 = (int)(this.scaleX * this.points[i].getX()) + H_GAP;
			  	y1 = (int)(this.offset - (this.scaleY * this.points[i].getY()));
			  	x2 = (int)(scaleX * this.points[i + 1].getX()) + H_GAP;
			  	y2 = (int)(this.offset - (this.scaleY * this.points[i + 1].getY()));
				g2D.drawLine(x1, y1, x2, y2);
			}
			if (this.s instanceof AMModulatedSignal)
				this.drawEnvelopes(g2D);
		}
		private void drawYAxisArrow(Graphics2D g2D, Point p) {
			//g2D.setClip((int)(p.getX() - 5), (int)(p.getY()), 10, 5);
			g2D.drawLine((int)(p.getX() - 5), (int)(p.getY() + 5), (int)(p.getX()), (int)(p.getY()));
			g2D.drawLine((int)(p.getX() + 5), (int)(p.getY() + 5), (int)(p.getX()), (int)(p.getY()));
		}
		private void drawXAxisArrow(Graphics2D g2D, Point p) {
			//g2D.setClip((int)(p.getX() - 5), (int)(p.getY() - 5), 5, 12);
			g2D.drawLine((int)(p.getX() - 5), (int)(p.getY() + 5), (int)(p.getX()), (int)(p.getY()));
			g2D.drawLine((int)(p.getX() - 5), (int)(p.getY() - 5), (int)(p.getX()), (int)(p.getY()));
		}
		private void drawXAxis(Graphics2D g2D) {
			//g2D.setClip(0, (int)(this.offset - 1), (int)(super.getWidth()), 2);
			g2D.drawLine(0, (int)(super.getHeight() / 2), (int)(this.getWidth()), (int)(super.getHeight() / 2));
			this.drawXAxisArrow(g2D, new Point(super.getWidth(), this.offset));
		}
		private void drawYAxis(Graphics2D g2D) {
			//g2D.setClip((int)(H_GAP - 1), 0, 2, (int)(super.getHeight()));
			g2D.drawLine(H_GAP, 0, H_GAP, (int)(super.getHeight()));
			this.drawYAxisArrow(g2D, new Point(H_GAP, 0));
		}
		private void drawAxes(Graphics2D g2D) {
			g2D.drawString("t", super.getWidth() - 8, super.getHeight() / 2 + 20);
			g2D.drawString("v(t)", 2, 20);
			this.drawYAxis(g2D);
			this.drawXAxis(g2D);
		}
		private void graphIt(Graphics2D g2D) {
			this.updateScale();
			this.drawAxes(g2D);
			this.drawSignal(g2D);
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHints(this.m);
			this.graphIt(g2D);
	 	}
	}
}