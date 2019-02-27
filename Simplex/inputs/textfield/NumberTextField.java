package inputs.textfield;

import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NumberTextField extends JTextField {

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
		super.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				selectAll();
			}
		});
		super.setFocusable(true);
		AbstractDocument d = (AbstractDocument)(super.getDocument());
		d.setDocumentFilter(new PatternFilter(pattern));
	}
}