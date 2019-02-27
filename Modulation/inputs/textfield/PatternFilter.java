package inputs.textfield;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PatternFilter extends DocumentFilter {

    private Pattern pattern;
	
    public PatternFilter(String pat) {
        pattern = Pattern.compile(pat);
    }
	@Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Matcher m = pattern.matcher(fb.getDocument().getText(0, fb.getDocument().getLength()) + string);
        if (m.matches())
            fb.insertString(offset, string, attr);
    }
	@Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
        if (length > 0) 
			fb.remove(offset, length);
        this.insertString(fb, offset, string, attr);
    }
}