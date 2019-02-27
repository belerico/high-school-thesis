package inputs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class ChoicePane extends JPanel{
	
	private String signalType = "";
	private ButtonGroup group = new ButtonGroup();

	private JPanel getTitlePane(String title, int hGap, int vGap) {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
		JLabel titleLbl = new JLabel(title);
		titleLbl.setFont(new Font("Dialog", Font.PLAIN, 15));
		tmp.add(titleLbl);
		return tmp;
	}
	private JPanel getPane(String title, String[] names, int rows, int cols, int hGap, int vGap) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		/*TITLE CODE*/
		JPanel tmp = new JPanel();
		tmp.setLayout(new BorderLayout());
		tmp.add(new JSeparator(), BorderLayout.NORTH);
		JLabel titleLbl = new JLabel(title);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		tmp.add(titleLbl, BorderLayout.CENTER);
		titleLbl = null;
		tmp.add(new JSeparator(), BorderLayout.SOUTH);
		p.add(tmp, BorderLayout.NORTH);
		/*CONTENT PANE CODE*/
		tmp = new JPanel();
		tmp.setLayout(new GridLayout(rows, cols, hGap, vGap));
		JRadioButton btn = null;
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				btn = new JRadioButton(names[j], (names[j].equals("Sinusoid") ? true : false));
				btn.setActionCommand(names[j]);
				group.add(btn);
				btn.setHorizontalAlignment(SwingConstants.CENTER);
				btn.setFont(new Font("Dialog", Font.PLAIN, 13));
				tmp.add(btn);
			}
		}
		btn = null;
		p.add(tmp, BorderLayout.CENTER);
		tmp = null;
		return p;
	}
	public ChoicePane() {
		super.setLayout(new BorderLayout());
		super.add(this.getTitlePane("Scegliere il segnale che si vuole disegnare", 0, 30), BorderLayout.NORTH);
		JPanel choice = new JPanel();
		choice.setLayout(new GridLayout(3, 1, 0, 0));
		choice.add(this.getPane("MATHEMATICAL CURVES", new String[] {"Sinusoid", "Cosinusoid"}, 1, 2, 0, 0));
		choice.add(this.getPane("NON-SINUSOIDAL PERIODIC WAVEFORM", new String[] {"Square wave", "Triangle wave", "Sawtooth wave"}, 1, 3, 0, 0));
		choice.add(this.getPane("MODULATED SIGNALS", new String[]{"AM", "FM"}, 1, 2, 0, 0));
		super.add(choice, BorderLayout.CENTER);
		choice = null;
	}
    public String getSignalType() {
    	return this.signalType;
    }
	public boolean isSignalTypeChanged() {
		if (!this.signalType.equals(this.group.getSelection().getActionCommand())) {
			this.signalType = this.group.getSelection().getActionCommand();
			return true;
		} else 
			return false;
	}
}