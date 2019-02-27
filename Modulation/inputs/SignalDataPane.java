package inputs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import inputs.textfield.NumberTextField;

public class SignalDataPane extends JPanel {

	private Font font = new Font(null, 0, 15);
	private NumberTextField[] inputs = {	new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"), 
											new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"), 
											null
										};
	private ButtonGroup group = null;
	private String type = "Pari";

	private JPanel getTitlePane(JLabel l, int hGap, int vGap) {
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
		l.setFont(this.font);
		titlePane.add(l);
		return titlePane;
	}
	private JPanel getPane(JLabel l, NumberTextField t, int hGap, int vGap) {
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
		l.setFont(this.font);
		pane.add(l);
		t.setFont(this.font);
		pane.add(t);
		return pane;
	}
	private JPanel getSignalDataContainerPane(String signalType, JPanel pane1, JPanel pane2, JPanel pane3) {
		JPanel container = new JPanel();
		if (signalType.equals("Square wave") || signalType.equals("Triangle wave")) {
			container.setLayout(new GridLayout(4, 1));
			JPanel p = new JPanel();
			JRadioButton btn = null;
			this.group = new ButtonGroup();
			btn = new JRadioButton("Pari", true);
			btn.setActionCommand("Pari");
			btn.setFont(this.font);
			this.group.add(btn);
			p.add(btn);
			btn = new JRadioButton("Dispari");
			btn.setActionCommand("Dispari");
			btn.setFont(this.font);
			this.group.add(btn);
			p.add(btn);
			btn = null;
			container.add(p);
			p = null;
		} else
			container.setLayout(new GridLayout(3, 1));
		container.add(pane1);
		container.add(pane2);
		container.add(pane3);
		return container;
	}
    public SignalDataPane(String signalType) {
		JPanel p = null;
		int vGap = 40, length = signalType.length();
		if (signalType.substring(length - 4, length).equals("wave")) {
			p = this.getPane(new JLabel("N. sinusoidi:"), this.inputs[2] = new NumberTextField(15, "^[1-9]\\d{0,4}?$"), 4, 10);
			if (signalType.equals("Sawtooth wave"))
				vGap = 50;
		}
		else {
			vGap = 50;
			p = this.getPane(new JLabel("Fase:"), this.inputs[2] = new NumberTextField(15, "^-?((0(\\.\\d{0,2})?)|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"), 44, 10);
		}
    	super.add(this.getTitlePane(new JLabel("Inserire i dati relativi al segnale"), 0, vGap), BorderLayout.NORTH);
    	super.add(this.getSignalDataContainerPane	(	signalType,
														this.getPane(new JLabel("Frequenza:"), this.inputs[0], 8, 10),
														this.getPane(new JLabel("Ampiezza:"), this.inputs[1], 13, 10),
														p
													), BorderLayout.CENTER);
		p = null;
    }
    public String getData() {
    	return this.inputs[0].getText() + ";" + this.inputs[1].getText() + ";" + this.inputs[2].getText();
    }
	private boolean isTypeChanged() {
		if (!this.type.equalsIgnoreCase(this.group.getSelection().getActionCommand())) {
			this.type = this.group.getSelection().getActionCommand();
			return true;
		} else
			return false;
	}
	public boolean isDataChanged() {
		return (this.inputs[0].isTextChanged() || this.inputs[1].isTextChanged() || this.inputs[2].isTextChanged()) | ((this.group != null) ? this.isTypeChanged() : false);
	}
	public void setTextChanged(boolean flag) {
		for (int i=0; i<this.inputs.length; i++)
			this.inputs[i].setTextChanged(false);
	}
	public boolean checkInputs() {
		for (int i=0; i<3; i++) {
			if (this.inputs[i].getText().length() == 0)
				return false;
		}
		if (this.inputs[2].getText().equals("-")) {
			this.inputs[2].setText("");
			return false;
		}
		return true;
	}
	public boolean isOdd() {
		return (this.group.getSelection().getActionCommand().equals("Pari")) ? false : true;
	}
}
