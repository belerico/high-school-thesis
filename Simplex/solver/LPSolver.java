package solver;

import java.util.ArrayList;

public class LPSolver {

	private double[][] coeff = null;				//Matrice dei coefficienti
	private double[] kt = null;						//Vettore dei termini noti (known terms)
	private double[] deltaZ = null;					//Vettore della funzione obiettivo
	private int[][] basedVars = null;
	private int pos = 0;							//Indice che indica in quale posizione dell'array basedVars verra' salvata la prossima variabile che entra in base
	private final double[] Z;						//Vettore costante della funzione obiettivo
	private ArrayList<Object[]> history = new ArrayList<Object[]>();

	public LPSolver(double[][] coeff, double[] kt, double[] z) {
		if (this.haveColsSameLength(coeff) && kt.length == coeff.length && z.length == coeff[0].length) {
			//Copio la matrice dei coefficienti riga per riga
			this.coeff = new double[coeff.length][coeff[0].length];
			for (int i=0; i<coeff.length; i++)
				System.arraycopy(coeff[i], 0, this.coeff[i], 0, coeff[0].length);
			//Copio il vettore dei termini noti
			this.kt = new double[kt.length];
			System.arraycopy(kt, 0, this.kt, 0, kt.length);
			//Copio il vettore della funzione obiettivo
			this.deltaZ = new double[z.length];
			this.Z = new double[z.length];
			System.arraycopy(z, 0, this.deltaZ, 0, z.length);
			System.arraycopy(z, 0, this.Z, 0, z.length);
			this.history.add(new Object[]{this.getCoeff(), this.getKT(), this.getDeltaZ(), this.z()});
			//Inizializzo l'array contenente le colonne delle variabili in base
			this.basedVars = new int[this.coeff.length][2];
			this.saveBasedVars();
		} else
			throw new RuntimeException("Check lengths");
	}
	//Metodo che controlla se tutte le colonne hanno la stessa lunghezza
	//Se così non fosse l'esecuzione del programma deve fermarsi
	private boolean haveColsSameLength(double[][] matrix) {
		for (int i=1; i<matrix.length; i++) {
			if (matrix[i].length != matrix[i-1].length)
				return false;
		}
		return true;
	}
	private boolean checkCol(int col) {
		return col >= 0 || col < this.coeff[0].length;
	}

	private boolean checkRow(int row) {
		return row >= 0 || row < this.coeff.length;
	}

	private void saveBasedVars() {
		for (int col=0; col<this.coeff[0].length; col++) {
			if (this.isBased(col))
				this.basedVars[this.pos++] = new int[]{this.getOnesRow(col), col};
		}
	}
	//Metodo che ritorna il numero di riga dove è presente un uno
	//Viene utilizzato solo ed esclusivamente per le colonne che sono in base
	private int getOnesRow(int col) {
		for (int row=0; row<this.coeff.length; row++) {
			if (this.coeff[row][col] == 1)
				return row;
		}
		return -1;
	}
	private int getZeroAmount(int col) {
		int zeroAmount = 0;
		for (int row=0; row<this.coeff.length; row++) {
			if (this.coeff[row][col] == 0)
				zeroAmount++;
		}
		if (this.deltaZ[col] == 0)
			zeroAmount++;
		return zeroAmount;
	}
	public boolean isBased(int col) {
		if (this.checkCol(col)) {
			double sum = 0.0;
			for (int row=0; row<this.coeff.length; row++)
				sum += this.coeff[row][col];
			sum += this.deltaZ[col];
			return (sum == 1 && this.getZeroAmount(col) == this.coeff.length);
		} else
			throw new RuntimeException("Wrong col");
	}
	//Ritorna l'indice dell'array in cui è presente
	//la variabile che uscirà di base
	private int getIndexComingOutVar(int row) {
		for (int i=0; i<this.pos; i++) {
			if (this.basedVars[i][0] == row)
				return i;
		}
		return -1;
	}
	private void bringInBase(int row, int col) {
		for (int i=0; i<this.coeff.length; i++) 
			this.coeff[i][col] = 0;
		this.coeff[row][col] = 1;
		this.deltaZ[col] = 0;
		int index = this.getIndexComingOutVar(row);
		if (index == -1)
			this.basedVars[this.pos++] = new int[]{row, col};
		else
			this.basedVars[index] = new int[]{row, col};
	}
	private void rowDividedByPivot(int row, double pivot) {
		for (int col=0; col<this.coeff[0].length; col++)
			this.coeff[row][col] /= pivot;
		this.kt[row] /= pivot;
	}
	public void pivot(int row, int col) {
		if (this.checkRow(row) && this.checkCol(col)) {
			if (this.coeff[row][col] == 0)
				throw new RuntimeException("Pivot on 0");
			double rowPivotImg = 0.0, colPivotImg = 0.0, pivot = this.coeff[row][col];
			for (int i=0; i<this.coeff.length; i++) {
				if (row != i) {
					colPivotImg = this.coeff[i][col];
					for (int j=0; j<this.coeff[0].length; j++) {
						if (col != j) {
							rowPivotImg = this.coeff[row][j];
							this.coeff[i][j] -= ((rowPivotImg * colPivotImg) / pivot);
						}
					}
					rowPivotImg = this.kt[row];
					this.kt[i] -= ((rowPivotImg * colPivotImg) / pivot);
				}
			}
			colPivotImg = this.deltaZ[col];
			for (int j=0; j<this.deltaZ.length; j++) {
				if (col != j) {
					rowPivotImg = this.coeff[row][j];
					this.deltaZ[j] -= ((rowPivotImg * colPivotImg) / pivot);
				}
			}
			this.rowDividedByPivot(row, pivot);
			this.bringInBase(row, col);
		} else
			throw new RuntimeException("Row or col wrong");
	}
	public double z() {
		double z = 0.0;
		for (int i=0; i<this.pos; i++)
			z += (this.kt[this.basedVars[i][0]] * this.Z[this.basedVars[i][1]]);
		return z;
	}
	//Ritorna un array contenente le colonne delle variabili che non sono in base
	//Il controllo non è stato eseguito mediante la condizione
	//if (!this.isBased(col)) { notBasedVar[k++] = col }
	//perchè risultava essere meno performante del controllo che è stato utilizzato di seguito
	private int[] getNotBasedVars(int prevPivotCol) {
		int[] notBasedVars = new int[this.coeff[0].length - this.pos];
		for (int col=0, i=0, k=0; col<this.coeff[0].length; col++) {
			i = 0;
			for (; i<this.pos; i++) {
				if (col == this.basedVars[i][1] || col < prevPivotCol)
					break;
			}
			if (i == this.pos)
				notBasedVars[k++] = col;
		}
		return notBasedVars;
	}
	private boolean computeKnownTerms(int row, int col) {
		double rowPivotImg = this.kt[row], colPivotImg = 0.0, tmp = 0.0, pivot = this.coeff[row][col];
		for (int i=0; i<this.coeff.length; i++) {
			if (row != i) {
				colPivotImg = this.coeff[i][col];
				if ((this.kt[i] - ((rowPivotImg * colPivotImg) / pivot)) < 0)
					return false;
			}
		}
		return true;
	}
	private int baseGetPivotRowFromMinRatio(int col) {
		int row = -1, i = 0;
		double min = 0.0, tmp = 0.0;
		while (i<this.coeff.length && (this.coeff[i][col] <= 0 || this.getIndexComingOutVar(i) != -1 || !this.computeKnownTerms(i, col)))
			i++;
		if (i != this.coeff.length) {
			min = this.kt[i] / this.coeff[i][col];
			row = i++;
			for (; i<this.coeff.length; i++) {
				if (this.coeff[i][col] > 0 && this.getIndexComingOutVar(i) == -1 && ((tmp = (this.kt[i] / this.coeff[i][col])) < min) && this.computeKnownTerms(i, col)) {
					min = tmp;
					row = i;
				}
			}
		}
		return row;
	}
	private int simplexGetPivotRowFromMinRatio(int col) {
		int row = -1, i = 0;
		double min = 0.0, tmp = 0.0;
		while (i<this.coeff.length && this.coeff[i][col] <= 0)
			i++;
		if (i != this.coeff.length) {
			min = this.kt[i] / this.coeff[i][col];
			row = i++;
			for (; i<this.coeff.length; i++) {
				if (this.coeff[i][col] > 0 && ((tmp = (this.kt[i] / this.coeff[i][col])) < min)) {
					min = tmp;
					row = i;
				}
			}
		}
		return row;
	}
	private boolean isColAllNegative(int col) {
		for (int row=0; row<this.coeff.length; row++) {
			if (this.coeff[row][col] > 0)
				return false;
		}
		return true;
	}
	private int getColNegativeZ() {
		int col = 0, i = 0;
		double min = this.deltaZ[0];
		for (int j=1; j<this.deltaZ.length; j++) {
			if (this.deltaZ[j] < 0) {
				if (this.isColAllNegative(j))
					throw new RuntimeException("Illimited");
				else if (this.deltaZ[j] < min) {
					min = this.deltaZ[j];
					col = j;
				}
			}
		}
		return (min < 0) ? col : -1;
	}
	public boolean optimize() {
		boolean pivotDone;
		int pivotRow = 0, col = 0, prevPivotCol = -1;
		int[] notBasedVars = null;
		while (this.pos < this.coeff.length) {
			notBasedVars = this.getNotBasedVars(prevPivotCol);
			pivotDone = false;
			for (int i=0; i<notBasedVars.length; i++) {
				if ((pivotRow = this.baseGetPivotRowFromMinRatio(notBasedVars[i])) != -1) {
					this.pivot(pivotRow, notBasedVars[i]);
					prevPivotCol = notBasedVars[i];
					this.history.add(new Object[]{this.getCoeff(), this.getKT(), this.getDeltaZ(), this.z()});
					pivotDone = true;
					break;
				}
			}
			if (!pivotDone)
				throw new RuntimeException("Uncomputable");
		}
		notBasedVars = null;
		if ((col = this.getColNegativeZ()) != -1 && (pivotRow = this.simplexGetPivotRowFromMinRatio(col)) != -1) {
			this.pivot(pivotRow, col);
			this.history.add(new Object[]{this.getCoeff(), this.getKT(), this.getDeltaZ(), this.z()});
			return true;
		} else
			return false;
	}
	public void simplex() {
		while (this.optimize());
	}

	public Object[][] getHistory() {
		Object[][] history = new Object[this.history.size()][4];
		for (int i=0; i<history.length; i++) 
			System.arraycopy(this.history.get(i), 0, history[i], 0, history[0].length);
		return history;
	}
	public double[] getDeltaZ() {
		double[] tmp = new double[this.deltaZ.length];
		System.arraycopy(this.deltaZ, 0, tmp, 0, tmp.length);
		return tmp;
	}
	public double[] getKT() {
		double[] tmp = new double[this.kt.length];
		System.arraycopy(this.kt, 0, tmp, 0, tmp.length);
		return tmp;
	}
	public double[][] getCoeff() {
		double[][] tmp = new double[this.coeff.length][this.coeff[0].length];
		for (int row=0; row<tmp.length; row++)
			System.arraycopy(this.coeff[row], 0, tmp[row], 0, tmp[0].length);
		return tmp;
	}
	@Override
	public String toString() {
		String str = "";
		for (int row=0; row<this.coeff.length; row++) {
			for (int col=0; col<this.coeff[0].length; col++)
				str += this.coeff[row][col] + "\t";
			str += this.kt[row] + "\n";
		}
		for (int i=0; i<this.deltaZ.length; i++)
			str += this.deltaZ[i] + "\t";
		return str;
	}
}