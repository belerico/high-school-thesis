package result;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.text.DecimalFormat;
import solver.LPSolver;

public class ResultPane extends JPanel {

	private JLabel z = null;
	private JLabel[] deltaZ = null;
	private JLabel[] kt = null;
	private JLabel[][] coeff = null;
	private Object[][] history = null;
	private boolean exceptionFired = false;
	private final DecimalFormat F = new DecimalFormat("#.###");

	private JPanel getDataPane(double[][] coeff, double[] kt, double[] z, boolean drawSlackVars) {
		int row = coeff.length + 2, col = coeff[0].length + 1;
		this.coeff = new JLabel[coeff.length][coeff[0].length];
		this.kt = new JLabel[kt.length];
		this.deltaZ = new JLabel[z.length];
		JPanel data = new JPanel();
		data.setLayout(new GridLayout(0, col, 5, 10));
		if (drawSlackVars) {
			for (int j=0; j<(coeff[0].length - coeff.length); j++)
				data.add(new JLabel("<html>x<sub>" + (j+1) + "</sub></html> ", JLabel.CENTER));
			for (int j=0; j<coeff.length; j++)
				data.add(new JLabel("<html>s<sub>" + (j+1) + "</sub></html> ", JLabel.CENTER));
		} else {
			for (int j=0; j<coeff[0].length; j++)
				data.add(new JLabel("<html>x<sub>" + (j+1) + "</sub></html> ", JLabel.CENTER));
		}
		data.add(new JLabel("Known terms ", JLabel.CENTER));
		for (int i=0; i<row-1; i++) {
			for (int j=0; j<col; j++) {
				if (i<row-2 && j == col-1)
					data.add(this.kt[i] = new JLabel(this.getRoundedValueToString(kt[i]), JLabel.CENTER));
				else if (i == row-2 && j<col-1)
					data.add(this.deltaZ[j] = new JLabel(this.getRoundedValueToString(z[j]), JLabel.CENTER));
				else if (i<row-2) {
					data.add(this.coeff[i][j] = new JLabel(this.getRoundedValueToString(coeff[i][j]), JLabel.CENTER));
					this.coeff[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
			}
		}
		data.add(new JPanel());
		for (int j=0; j<this.deltaZ.length; j++)
			data.add(new JLabel(this.deltaZ[j].getText(), JLabel.CENTER));
		data.add(new JPanel());
		data.add(this.z);
		return data;
	}
	private JPanel getDescPane(int row) {
		JPanel desc = new JPanel();
		desc.setLayout(new GridLayout(0, 1, 0, 10));
		desc.add(new JPanel());
		for (int i=0; i<row-1; i++)
			desc.add(new JLabel(" Equation n." + (i+1), JLabel.CENTER));
		desc.add(new JLabel(" \u0394Z", JLabel.CENTER));
		desc.add(new JLabel(" Objective function", JLabel.CENTER));
		desc.add(new JLabel(" Z", JLabel.CENTER));
		return desc;
	}
	private String getRoundedValueToString(double value) {
		String str = F.format(value);
		return (str.equals("-0")) ? "0" : str; 
	}
	private void saveHistory(double[][] coeff, double[] kt, double[] z) {
		LPSolver solver = new LPSolver(coeff, kt, z);
		try {
			this.z = new JLabel(this.getRoundedValueToString(solver.z()), JLabel.CENTER);
			while (solver.optimize());
			this.history = solver.getHistory();
			solver = null;
			this.z.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		} catch (RuntimeException e) {
			this.history = solver.getHistory();
			solver = null;
			this.z = new JLabel(e.getMessage(), JLabel.CENTER);
			this.z.setBorder(BorderFactory.createLineBorder(Color.RED));
			this.exceptionFired = true;
		}
	}
	private JPanel getHistoryPane() {
		String[] historyStr = new String[this.history.length];
		historyStr[0] = "Initial table";
		for (int i=1; i<historyStr.length-1; i++)
			historyStr[i] = "Step n." + i;
		historyStr[historyStr.length-1] = "Final table";
		JComboBox<String> historyComboBox = new JComboBox<String>(historyStr);
		historyStr = null;
		historyComboBox.addActionListener( (e) -> {
			this.update(((JComboBox)e.getSource()).getSelectedIndex());
		});
		((JLabel)historyComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		JPanel historyPane = new JPanel();
		historyPane.add(historyComboBox);
		return historyPane;
	}
	private void update(int index) {
		Object[] elements = this.history[index];
		double[][] coeff = (double[][])elements[0];
		for (int i=0; i<coeff.length; i++) {
			for (int j=0; j<coeff[0].length; j++)
				this.coeff[i][j].setText(this.getRoundedValueToString(coeff[i][j]));
		}
		coeff = null;
		double[] tmp = (double[])elements[1];
		for (int i=0; i<tmp.length; i++)
			this.kt[i].setText(this.getRoundedValueToString(tmp[i]));
		tmp = (double[])elements[2];
		for (int j=0; j<tmp.length; j++)
			this.deltaZ[j].setText(this.getRoundedValueToString(tmp[j]));
		tmp = null;
		if (!this.exceptionFired)
			this.z.setText(this.getRoundedValueToString((double)elements[3]));
	}
	public ResultPane(double[][] coeff, double[] kt, double[] z, boolean drawSlackVars) {
		this.saveHistory(coeff, kt, z);
		super.setLayout(new BorderLayout(5, 0));
		super.add(this.getDataPane(coeff, kt, z, drawSlackVars), BorderLayout.CENTER);
		super.add(this.getDescPane(coeff.length + 1), BorderLayout.WEST);
		super.add(this.getHistoryPane(), BorderLayout.NORTH);
		super.add(new JPanel(), BorderLayout.EAST);
		super.add(new JPanel(), BorderLayout.SOUTH);
	}
}