package graphs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import signals.Signal;
import signals.ModulatedSignal;
import signals.AMModulatedSignal;

public class ModulatedSignalGraph extends JPanel{
	
	private JPanel getTitledGraphPane(SignalGraph g, String title) {
		JPanel container = new JPanel();
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new FlowLayout(FlowLayout.CENTER));
		titlePane.add(new JLabel(title));
		container.setLayout(new BorderLayout());
		container.add(titlePane, BorderLayout.NORTH);
		titlePane = null;
		container.add(g, BorderLayout.CENTER);
		return container;	
	}
	public ModulatedSignalGraph(ModulatedSignal m) {
		this(m, 2000, 2);
	}
	public ModulatedSignalGraph(ModulatedSignal m, int density) {
		this(m, density, 2);
	}
    public ModulatedSignalGraph(ModulatedSignal s, int density, int periodsToDraw) {
    	Signal info = s.getInfoSignal();
    	Signal load = s.getLoadSignal();
    	double period = Math.max(info.getPeriod(), load.getPeriod());
    	double amplitude = (s instanceof AMModulatedSignal) ? s.getAmplitude() : Math.max(info.getAmplitude(), load.getAmplitude());
		super.setLayout(new GridLayout(3, 1, 0, 5));
		super.add(this.getTitledGraphPane(new SignalGraph(info, density, periodsToDraw, period, amplitude), "MODULANTE"));
		info = null;
		super.add(this.getTitledGraphPane(new SignalGraph(load, density, periodsToDraw, period, amplitude), "PORTANTE"));
		load = null;
		super.add(this.getTitledGraphPane(new SignalGraph(s, density, periodsToDraw, period, amplitude), "SEGNALE MODULATO"));
		s = null;	
    }
}