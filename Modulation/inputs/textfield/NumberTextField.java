package inputs.textfield;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NumberTextField extends JTextField {

	private boolean changed;
	private PatternFilter numberFilter = null;

	public NumberTextField(String text) {
		this(text, text.length() / 2 + 1, "^-?((0(\\.\\d*)?)|([1-9]\\d*(\\.\\d*)?)?)$");	//Regex that matches all positive and negative decimal number
	}
	public NumberTextField(int cols) {
		this("", cols, "^-?((0(\\.\\d*)?)|([1-9]\\d*(\\.\\d*)?)?)$");
	}
	public NumberTextField(int cols, String pattern) {
		this("", cols, pattern);
	}
	public NumberTextField(String text, int cols, String pattern) {
		super(text, cols);
		AbstractDocument d = (AbstractDocument)(super.getDocument());
		d.setDocumentFilter(new PatternFilter(pattern));
		d.addDocumentListener( new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changed = true;
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed = true;
			}
		});
		d = null;
	}
	public boolean isTextChanged() {
		return this.changed;
	}
	public void setTextChanged(boolean flag) {
		this.changed = flag;
	}
}