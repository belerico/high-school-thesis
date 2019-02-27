import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Point;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import inputs.*;
import graphs.*;
import message.*;
import signals.*;
import signals.wave.*;

public class SignalDrawer extends JFrame{

	private JPanel[] panels = {new ChoicePane(), null, null};
	private ActionListener backListener = new BackToChoicePane();
	private ActionListener nextListener = new NextToDataPane();
	private JButton back = new JButton("Back");
	private JButton next = new JButton("Next");
	private JMenuBar menu = null;
	private MessageBox showInfo = null;
	private Signal s = null;
	private String signalType = "";
	
	//Init panel containing buttons Next and Back
	private JPanel getBtnsPane() {
		JPanel btnsPane = new JPanel();
		btnsPane.setLayout(new GridLayout(1, 2));
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.LEFT));
		tmp.add(this.back);
		btnsPane.add(tmp);
		tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tmp.add(this.next);
		btnsPane.add(tmp);
		tmp = null;		//Helps gc
		return btnsPane;
	}
	//Init this JFrame
	public SignalDrawer() {
		this.showPane(panels[0]);
		this.back.addActionListener(this.backListener);	
		this.next.addActionListener(this.nextListener);
		this.back.setVisible(false);
		super.add(this.getBtnsPane(), BorderLayout.SOUTH);
		super.setTitle("SignalDrawer 2.0");
		super.setSize(420, 430);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null);
		super.setResizable(false);
		super.setVisible(true);
	}
	private void initJMenuBar() {
		JMenu info = new JMenu("Info");
		JMenuItem signalInfo = new JMenuItem("Signal info");
		signalInfo.addActionListener( (e) -> {
			if (this.showInfo == null) {
				this.showInfo = new MessageBox("Info", this.s.toString(), super.getX() + super.getWidth() + 5, super.getY() + 5, 300, 270);
				System.out.println("Show new messagebox");
			} else if (!this.showInfo.isShowing()) {
				Point p = super.getLocationOnScreen();
				this.showInfo.setLocation((int)(p.getX() + super.getWidth() + 5), (int)(p.getY() + 5));
				this.showInfo.setVisible(true);
				System.out.println("Show old messagebox");
			}
		});
		info.add(signalInfo);
		this.menu = new JMenuBar();
		this.menu.add(info);
		super.setJMenuBar(menu);
	}
	private void showPane(JPanel pane) {
		super.add(pane, BorderLayout.CENTER);
		if (pane instanceof ModulatedSignalGraph || pane instanceof SignalGraph) {
			super.setSize(800, 600);
			super.setResizable(true);
		}
	}
	private void removePane(JPanel pane) {
		super.remove(pane);
		super.validate();
		super.repaint();
	}
	private void updateHistory(int index) {
		this.removePane(this.panels[index]);
		this.showPane(this.panels[index-1]);
	}
	private boolean isDataChanged(JPanel p) {
		return (p instanceof SignalDataPane) ? ((SignalDataPane)p).isDataChanged() : ((ModulatedSignalDataPane)p).isDataChanged();
	}
	private boolean isItPossible(JPanel pane) {
		return (pane instanceof SignalDataPane) ? ((SignalDataPane)pane).checkInputs() : ((ModulatedSignalDataPane)pane).checkInputs();
	}
	private String[] getData(JPanel pane) {
		return (pane instanceof SignalDataPane) ? ((SignalDataPane)pane).getData().split(";") : ((ModulatedSignalDataPane)pane).getData().split(";");
	}
	private JPanel getGraphPane(String[] data) {
		switch(this.signalType) {
			case "Sinusoid":	this.s = new Sinusoid(Double.valueOf(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]));
								return (this.panels[2] = new SignalGraph(this.s));
			case "Cosinusoid": 	this.s = new Cosinusoid(Double.valueOf(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]));
									return (this.panels[2] = new SignalGraph(this.s));
			case "Square wave":	this.s = new SquareWave(Double.valueOf(data[0]), Double.valueOf(data[1]), Integer.valueOf(data[2]), ((SignalDataPane)(panels[1])).isOdd());
								return (this.panels[2] = new SignalGraph(this.s));
			case "Triangle wave": 	this.s = new TriangleWave(Double.valueOf(data[0]), Double.valueOf(data[1]), Integer.valueOf(data[2]), ((SignalDataPane)(panels[1])).isOdd());
									return (this.panels[2] = new SignalGraph(this.s));
			case "Sawtooth wave": 	this.s = new SawtoothWave(Double.valueOf(data[0]), Double.valueOf(data[1]), Integer.valueOf(data[2]));
									return (this.panels[2] = new SignalGraph(this.s));
			case "AM": 	this.s = new AMModulatedSignal(new Sinusoid(Double.valueOf(data[0]), Double.valueOf(data[1]), 0), new Sinusoid(Double.valueOf(data[2]), Double.valueOf(data[3]), 0));
						return (this.panels[2] = new ModulatedSignalGraph((ModulatedSignal)(this.s)));
			case "FM":	this.s = new FMModulatedSignal(new Cosinusoid(Double.valueOf(data[0]), Double.valueOf(data[1]), 0), new Sinusoid(Double.valueOf(data[2]), Double.valueOf(data[3]), 0), Double.valueOf(data[4]));
						return (this.panels[2] = new ModulatedSignalGraph((ModulatedSignal)(this.s)));
			default: return null;
		}
	}
	private class NextToDataPane implements ActionListener {							  
		@Override																		
		public void actionPerformed(ActionEvent e) {
			removePane(panels[0]);														
			if (((ChoicePane)panels[0]).isSignalTypeChanged()) {						
				signalType = ((ChoicePane)(panels[0])).getSignalType();					
				if (!signalType.equals("AM") && !signalType.equals("FM"))				
					showPane(panels[1] = new SignalDataPane(signalType));				
				else
					showPane(panels[1] = new ModulatedSignalDataPane(signalType));		
				System.out.println("Show new input");
			} else {
				showPane(panels[1]);													
				System.out.println("Show old input");
			}
			next.removeActionListener(nextListener);									
			next.addActionListener(nextListener = new NextToGraphPane());
			back.setVisible(true);
		}
	}
	private class NextToGraphPane implements ActionListener {							 																		
		@Override																		
		public void actionPerformed(ActionEvent e) {									
			if (isItPossible(panels[1])) {												
				removePane(panels[1]);													
				if (isDataChanged(panels[1])) {											
					showPane(getGraphPane(getData(panels[1])));							
					if (showInfo != null) {
						showInfo.setMessage(s.toString());
						System.out.println("toString() changed");
					}
					System.out.println("Show new graph");
				} else {
					showPane(panels[2]);
					System.out.println("Show old graph");
				}
				if (menu == null)
					initJMenuBar();
				else
					menu.setVisible(true);
				if (panels[1] instanceof SignalDataPane)
					((SignalDataPane)panels[1]).setTextChanged(false);
				else
					((ModulatedSignalDataPane)panels[1]).setTextChanged(false);
				back.removeActionListener(backListener);
				back.addActionListener(backListener = new BackToDataPane());
				next.setVisible(false);
			}
		}
	}
	private class BackToChoicePane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			back.setVisible(false);
			updateHistory(1);
			next.removeActionListener(nextListener);
			next.addActionListener(nextListener = new NextToDataPane());
		}
	}
	private class BackToDataPane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			next.setVisible(true);
			menu.setVisible(false);
			if (showInfo != null)
				showInfo.setVisible(false);
			updateHistory(2);
			back.removeActionListener(backListener);
			back.addActionListener(backListener = new BackToChoicePane());
			setSize(420, 430);
			setResizable(false);
		}
	}
	public static void main(String[] args) {
		new SignalDrawer();
	}
}