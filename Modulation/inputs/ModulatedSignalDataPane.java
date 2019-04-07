package inputs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import inputs.textfield.NumberTextField;

public class ModulatedSignalDataPane extends JPanel {

	private Font font = new Font(null, 0, 15);
	private NumberTextField[] inputs = 	{	new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"),
											new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"),
											new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"),
											new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"),
											null
										};

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
	private JPanel getSignalDataContainerPane(JPanel pane1, JPanel pane2) {
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(2, 1));
		container.add(pane1);
		container.add(pane2);
		return container;
	}
	private JPanel getDeltaFDataPane() {
		this.inputs[4] = new NumberTextField(15, "^(0|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$");
		JPanel kPane = new JPanel();
		kPane.setLayout(new BorderLayout());
		kPane.add(this.getTitlePane(new JLabel("Scostamento in frequenza \u0394F"), 0, 10), BorderLayout.NORTH);
		kPane.add(this.getPane(new JLabel("\u0394F:"), this.inputs[4], 55, 0), BorderLayout.CENTER);
		return kPane;
	}

	private JPanel getFinalPane(String signalType, String[] names) {
		JPanel pane = new JPanel();
		int rows = (signalType.equals("AM")) ? 2 : 3;
		pane.setLayout(new GridLayout(rows, 1));
		JPanel tmp = null;
		for (int i=0, titleIndex=0, inputIndex=0; i<2; i++, titleIndex+=3, inputIndex+=2) {
			tmp = new JPanel();
			tmp.setLayout(new BorderLayout());
			tmp.add(this.getTitlePane(new JLabel(names[titleIndex]), 0, 10), BorderLayout.NORTH);
			tmp.add(this.getSignalDataContainerPane(	this.getPane(new JLabel(names[titleIndex+1]), this.inputs[inputIndex], 5, 5), 
														this.getPane(new JLabel(names[titleIndex+2]), this.inputs[inputIndex+1], 10, 5)
													), 
					BorderLayout.CENTER);
			pane.add(tmp);
			tmp = null;
		}
		if (rows == 3)
			pane.add(this.getDeltaFDataPane());
		return pane;
	}
    public ModulatedSignalDataPane(String signalType) {
    	super.add(this.getTitlePane(new JLabel("Fill data about modulating and carrier signals"), 60, (signalType.equals("AM")) ? 30 : 10), BorderLayout.NORTH);
    	super.add(this.getFinalPane(signalType, new String[] {"Modulating","Frequency:","Amplitude:","Carrier","Frequency:","Amplitude:"}));
    }
    public String getData() {
    	String data = "";
		int end = (this.inputs[4] == null) ? 3 : 4;
    	for (int i=0; i<end; i++) 
    		data += this.inputs[i].getText() + ";";
    	return data += this.inputs[end].getText() + ";";
    }
	public boolean isDataChanged() {
		int end = (this.inputs[4] == null) ? 4 : 5;
		boolean isDataChanged = this.inputs[0].isTextChanged();
		for (int i=1; i<end; i++) 
			isDataChanged = isDataChanged || this.inputs[i].isTextChanged();
		return isDataChanged;
	}
	public void setTextChanged(boolean flag) {
		int end = (this.inputs[4] == null) ? 3 : 4;
		for (int i=0; i<end; i++)
			this.inputs[i].setTextChanged(false);
	}
	public boolean checkInputs() {
		int end = (this.inputs[4] == null) ? 3 : 4;
		for (int i=0; i<end; i++) {
			if (this.inputs[i].getText().length() == 0)
				return false;
		}
		return true;
	}
}