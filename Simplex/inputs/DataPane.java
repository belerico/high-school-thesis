package inputs;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import inputs.textfield.NumberTextField;

public class DataPane extends JPanel {

	private NumberTextField[] z = null;
	private NumberTextField[] kt = null;
	private NumberTextField[][] coeff = null;
	private JComboBox[] eqsSign = null;
	private JComboBox[] varsSign = null;
	private JComboBox<String> opType = new JComboBox<String>(new String[]{"Maximize", "Minimize"});
	private final MyDocumentListener DOC_LISTENER = new MyDocumentListener();
	private boolean somethingChanged = false;

	private JPanel getDataPane(int row, int col) {
		JPanel data = new JPanel();
		data.setLayout(new GridLayout(0, col, 5, 10));
		for (int j=0; j<col-2; j++)
			data.add(new JLabel("<html>x<sub>" + (j+1) + "</sub></html>", JLabel.CENTER));
		data.add(new JLabel("Equations sign", JLabel.CENTER));
		data.add(new JLabel("Known terms", JLabel.CENTER));
		for (int i=0; i<row-4; i++) {
			for (int j = 0; j < col; j++) {
				if (j == col - 2) {
					data.add(this.eqsSign[i] = new JComboBox<String>(new String[]{">=", "<=", "="}));
					this.eqsSign[i].addItemListener(e -> {
						if (e.getStateChange() == 2)
							this.somethingChanged = true;
					});
					((JLabel) this.eqsSign[i].getRenderer()).setHorizontalAlignment(JLabel.CENTER);
				} else if (j == col - 1) {
					data.add(this.kt[i] = new NumberTextField(8, "^-?((0(\\.\\d{0,8})?)|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"));
					this.kt[i].setHorizontalAlignment(SwingConstants.CENTER);
					this.addDocumentListener(this.kt[i]);
				} else {
					data.add(this.coeff[i][j] = new NumberTextField(8, "^-?((0(\\.\\d{0,8})?)|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"));
					this.coeff[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					this.addDocumentListener(this.coeff[i][j]);
				}
			}
		}
		for (int j=0; j<col-2; j++) {
			data.add(this.z[j] = new NumberTextField(8, "^-?((0(\\.\\d{0,8})?)|([1-9]\\d{0,8}(\\.\\d{0,2})?)?)$"));
			this.z[j].setHorizontalAlignment(SwingConstants.CENTER);
			this.addDocumentListener(this.z[j]);
		}
		data.add(new JPanel());
		data.add(new JPanel());
		for (int j=0; j<col-2; j++) {
			data.add(this.varsSign[j] = new JComboBox<String>(new String[]{">= 0", "<= 0"}));
			this.varsSign[j].addItemListener(e -> {
				if (e.getStateChange() == 2)
					this.somethingChanged = true;
			});
			((JLabel)this.varsSign[j].getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		}
		data.add(new JPanel());
		data.add(new JPanel());
		this.opType.addItemListener(e -> {
			if (e.getStateChange() == 2)
				this.somethingChanged = true;
		});
		((JLabel)this.opType.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		data.add(this.opType);
		return data;
	}
	private JPanel getDescPane(int row) {
		JPanel desc = new JPanel();
		desc.setLayout(new GridLayout(0, 1, 0, 10));
		desc.add(new JPanel());
		for (int i=0; i<row-4; i++)
			desc.add(new JLabel(" Equation n." + (i+1), JLabel.CENTER));
		desc.add(new JLabel(" Objective function", JLabel.CENTER));
		desc.add(new JLabel(" Variables sign", JLabel.CENTER));
		desc.add(new JLabel(" Operation type", JLabel.CENTER));
		return desc;
	}
	public DataPane(int row, int col) {
		this.z = new NumberTextField[col];
		this.kt = new NumberTextField[row];
		this.coeff = new NumberTextField[row][col];
		this.eqsSign = new JComboBox[row];
		this.varsSign = new JComboBox[col];
		super.setLayout(new BorderLayout(5, 0));
		super.add(this.getDataPane(row + 4, col + 2), BorderLayout.CENTER);
		super.add(this.getDescPane(row + 4), BorderLayout.WEST);
		super.add(new JPanel(), BorderLayout.EAST);
		super.add(new JPanel(), BorderLayout.SOUTH);
		super.add(new JPanel(), BorderLayout.NORTH);
	}
	//Copio il vettore della funzione obiettivo aggiungendo le variabili di slack
	//e convertendo il contenuto delle JLabel in Double
	private double[] zToDouble(int col) {
		double[] copy = new double[col];
		for (int j=0; j<this.z.length; j++)
			copy[j] = Double.valueOf(this.z[j].getText());
		return copy;
	}
	private double[] ktToDouble() {
		double[] copy = new double[this.kt.length];
		for (int i=0; i<copy.length; i++)
			copy[i] = Double.valueOf(this.kt[i].getText());
		return copy;
	}
	//Copio la matrice dei coefficienti aggiungendo le variabili di slack
	//e convertendo il contenuto delle JLabel in Double
	private double[][] coeffToDouble(int col) {
		double[][] copy = new double[this.coeff.length][col];
		for (int i=0; i<copy.length; i++) {
			for (int j=0; j<this.coeff[0].length; j++)
				copy[i][j] = Double.valueOf(this.coeff[i][j].getText());
		}
		return copy;
	}
	private JComboBox[] equationsSignCopy() {
		JComboBox[] copy = new JComboBox[this.eqsSign.length];
		for (int i=0; i<copy.length; i++) {
			copy[i] = new JComboBox<String>(new String[]{">=", "<=", "="});
			copy[i].setSelectedIndex(this.eqsSign[i].getSelectedIndex());
		}
		return copy;
	}
	private void changeColSign(double[][] matrix, int col) {
		for (int row=0; row<matrix.length; row++)
			matrix[row][col] *= -1;
	}
	private void changeRowSign(double[][] matrix, int row) {
		for (int col=0; col<matrix[0].length; col++)
			matrix[row][col] *= -1;
	}
	public boolean addSlackVars() {
		for (int j=0; j<this.eqsSign.length; j++) {
			if (this.eqsSign[j].getSelectedIndex() != 2)
				return true;
		}
		return false;
	}
	public Object[] getData() {
        int colNum, initCol;
        if (this.addSlackVars())
            colNum = this.coeff[0].length + this.coeff.length;
        else
            colNum = this.coeff[0].length;
        initCol = this.coeff[0].length;
		//Copia di tutti gli elementi, convertendo i relativi valori
		//presenti nelle JLabel in double
		double[] z = this.zToDouble(colNum);
		double[] kt = this.ktToDouble();
		double[][] coeff = this.coeffToDouble(colNum);
		JComboBox[] eqsSignCopy = this.equationsSignCopy();
		for (int row=0; row<coeff.length; row++) {
			//Se il termine noto risulta essere < 0
			//Cambio il segno a tutta la riga, compreso il termine noto,
			//inoltre cambio il segno alla disequazione
			if (kt[row] < 0) {
				this.changeRowSign(coeff, row);
				kt[row] *= -1;
				int selectedIndex = eqsSignCopy[row].getSelectedIndex();
				if (selectedIndex == 0)
					eqsSignCopy[row].setSelectedIndex(1);
				else if (selectedIndex == 1)
					eqsSignCopy[row].setSelectedIndex(0);
			}
			//Aggiungo le variabili di scarto
			//se sono da aggiungere
			for (int col=initCol; col<colNum; col++) {
                if (initCol + row == col) {
                    int selectedIndex = eqsSignCopy[row].getSelectedIndex();
                    coeff[row][col] = (selectedIndex != 2)
                            ? (selectedIndex == 0) ? -1 : 1
                            : 0;
                }
            }
		}
		//Cambio il segno alla colonna se la relativa variabile risulta essere <= 0
		for (int col=0; col<this.varsSign.length; col++) {
			if (this.varsSign[col].getSelectedIndex() == 1) {
				this.changeColSign(coeff, col);
				z[col] *= -1;
			}
		}
		//Cambio il segno se l'operazione risulta essere uguale a "Maximize"
		if (opType.getSelectedIndex() == 0) {
			for (int col=0; col<this.z.length; col++)
				z[col] *= -1;
		}
		return new Object[]{coeff, kt, z};
	}
	public void requestFocusOnFirstTextField() {
		this.coeff[0][0].requestFocus();
	}

	public boolean isInputsInvalid() {
		String text;
		for (int row=0; row<this.coeff.length; row++) {
			for (int col=0; col<this.coeff[0].length; col++) {
				text = this.coeff[row][col].getText();
				if (text.equals("") || text.equals("-")) {
					this.coeff[row][col].requestFocus();
					return true;
				}
			}
			text = this.kt[row].getText();
			if (text.equals("") || text.equals("-")) {
				this.kt[row].requestFocus();
				return true;
			}
		}
		for (int j=0; j<this.z.length; j++) {
			text = this.z[j].getText();
			if (text.equals("") || text.equals("-")) {
				this.z[j].requestFocus();
				return true;
			}
		}
		text = null;
		return false;
	}
	public void setSomethingChanged(boolean flag) {
		this.somethingChanged = flag;
	}

	public boolean isSomethingChanged() {
		return this.somethingChanged;
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
