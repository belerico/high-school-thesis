package message;

import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MessageBox extends JFrame {
	
	private JTextArea msg = null;
	
	public MessageBox(String title, String msg, int xOff, int yOff, int width, int height) {
		super(title);
		super.setBounds(xOff, yOff, width, height);
		BoxSetUp(msg);
	}
	private void BoxSetUp(String msg){
		JPanel boxPanel = new JPanel(new BorderLayout(3,3));
		boxPanel.setBorder(new EmptyBorder(3,3,3,3));
		boxPanel.add(textAreaSetUp(msg), BorderLayout.CENTER);
		super.add(boxPanel);
		super.setResizable(false);
		super.setVisible(true);
		super.setContentPane(boxPanel);
		super.setFocusable(true);
		super.requestFocus();
	}
	private JTextArea textAreaSetUp(String msg){
		this.msg = new JTextArea(msg);
		this.msg.setRows(3);
		this.msg.setColumns(25);
		this.msg.setEditable(false);
		this.msg.setFont(new Font(null, Font.PLAIN, 15));
		return this.msg;
	}
	public void setMessage(String msg) {
		this.msg.setText(msg);		
	}
}
