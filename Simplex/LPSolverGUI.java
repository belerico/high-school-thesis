import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import inputs.InitPane;
import inputs.DataPane;
import result.ResultPane;

public class LPSolverGUI extends JFrame{

	private JScrollPane scrollPane = new JScrollPane();
	private JPanel[] panels = {new InitPane(), null, null};
	private ActionListener backListener = new BackToInitPane();
	private ActionListener nextListener = new NextToDataPane();
	private JButton back = new JButton("Back");
	private JButton next = new JButton("Next");
	private final int CONTENT_PANE_MAX_WIDTH = 1200;
	private final int CONTENT_PANE_MAX_HEIGHT = 600;
	private boolean isFirstTimeShowing = true;

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
		tmp = null;	
		return btnsPane;
	}
	public LPSolverGUI() {
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.back.addActionListener(this.backListener);
		this.next.addActionListener(this.nextListener);
		this.back.setVisible(false);
		super.add(this.getBtnsPane(), BorderLayout.SOUTH);
		super.add(this.scrollPane, BorderLayout.CENTER);
		super.setTitle("LPSolver");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(true);
		this.showPane(panels[0]);
		super.setLocationRelativeTo(null);
		((InitPane)this.panels[0]).requestFocusOnFirstTextField();
	}
	private void showPane(JPanel pane) {
		this.scrollPane.setViewportView(pane);
		super.pack();
		if (pane != this.panels[0]) {
			int width = pane.getWidth();
			int height = pane.getHeight();
			Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
			int deltaW = (int)(screenDim.getWidth() * this.CONTENT_PANE_MAX_WIDTH / 1920);
			int deltaH = (int)(screenDim.getHeight() * this.CONTENT_PANE_MAX_HEIGHT / 1080);
			if (width > deltaW)
				width = deltaW;
			if (height > deltaH)
				height = deltaH;
			super.setSize(new Dimension(width + 20, height + 100));
			Point locOnScreen = super.getLocationOnScreen();
			if ((locOnScreen.getX() + width) > screenDim.getWidth() || (locOnScreen.getY() + height) > screenDim.getHeight() || this.isFirstTimeShowing) {
				super.setLocationRelativeTo(null);
				this.isFirstTimeShowing = false;
			}
		}
	}
	private void updateHistory(int index) {
		this.showPane(this.panels[index-1]);
	}

	private class NextToDataPane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			InitPane pane = (InitPane)panels[0];
			if (!pane.isInputsEmpty()) {
				if (pane.isSomethingChanged())
					showPane(panels[1] = new DataPane(pane.getEquationsNumber(), pane.getVariablesNumber()));
				else
					showPane(panels[1]);
				((DataPane)panels[1]).requestFocusOnFirstTextField();
				pane = null;
				next.removeActionListener(nextListener);
				next.addActionListener(nextListener = new NextToResultPane());
				back.setVisible(true);
			} else 
				pane = null;
		}
	}
	private class NextToResultPane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			DataPane pane = (DataPane)panels[1];
			if (!pane.isInputsInvalid()) {
				next.setVisible(false);
				Object[] data = pane.getData();
				if (pane.isSomethingChanged())
					showPane(panels[2] = new ResultPane((double[][])data[0], (double[])data[1], (double[])data[2], pane.addSlackVars()));
				else
					showPane(panels[2]);
				back.removeActionListener(backListener);
				back.addActionListener(backListener = new BackToDataPane());
			} else
				pane = null;
		}
	}
	private class BackToInitPane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			back.setVisible(false);
			InitPane pane = (InitPane)panels[0];
			pane.setSomethingChanged(false);
			pane = null;
			updateHistory(1);
			next.removeActionListener(nextListener);
			next.addActionListener(nextListener = new NextToDataPane());
		}
	}
	private class BackToDataPane implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			next.setVisible(true);
			DataPane pane = (DataPane)panels[1];
			pane.setSomethingChanged(false);
			pane = null;
			updateHistory(2);
			back.removeActionListener(backListener);
			back.addActionListener(backListener = new BackToInitPane());
		}
	}
	public static void main(String[] args) {
		new LPSolverGUI();
	}
}