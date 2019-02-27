package inputs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;
import java.awt.GridLayout;
import inputs.textfield.NumberTextField;

public class InitPane extends JPanel {

	private NumberTextField eqNum = new NumberTextField(10, "^[1-9]\\d{0,1}$");
	private NumberTextField varNum = new NumberTextField(10, "^[1-9]\\d{0,1}$");
	private final MyDocumentListener DOC_LISTENER = new MyDocumentListener();
	private boolean somethingChanged = false;

	public InitPane() {
		this.addDocumentListener(this.eqNum);
		this.addDocumentListener(this.varNum);
		JPanel pane = new JPanel();
		super.setLayout(new GridLayout(0, 1));
		JLabel lbl = new JLabel("Specifies equations number:");
		lbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pane.add(lbl);
		pane.add(this.eqNum);
		super.add(pane);
		pane = new JPanel();
		lbl = new JLabel("Specifies variables number: ");
		lbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pane.add(lbl);
		pane.add(this.varNum);
		super.add(pane);
		pane = null;
		lbl = null;
	}
	public void setSomethingChanged(boolean flag) {
		this.somethingChanged = flag;
	}
	public boolean isSomethingChanged() {
		return this.somethingChanged;
	}
	public void requestFocusOnFirstTextField() {
		this.eqNum.requestFocus();
	}
	public int getEquationsNumber() {
		return Integer.valueOf(this.eqNum.getText());
	}
	public int getVariablesNumber() {
		return Integer.valueOf(this.varNum.getText());
	}
	public boolean isInputsEmpty() {
		if (this.eqNum.getText().equals("")) {
			this.eqNum.requestFocus();
			return true;
		} else if (this.varNum.getText().equals("")) {
			this.varNum.requestFocus();
			return true;
		} else
			return false;
	}
	private void addDocumentListener(JTextComponent c) {
		AbstractDocument d = (AbstractDocument)(c.getDocument());
		d.addDocumentListener(this.DOC_LISTENER);
		d = null;
	}
	private class MyDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {}

		@Override
		public void insertUpdate(DocumentEvent e) {
			somethingChanged = true;
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			somethingChanged = true;
		}
	}
}
