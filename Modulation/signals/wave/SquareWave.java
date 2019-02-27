package signals.wave;

import signals.Sinusoid;
import signals.Cosinusoid;

public class SquareWave extends Wave {

	public SquareWave(double frequency, double amplitude, int nSin, boolean isOdd) {
		super(frequency, amplitude, nSin, isOdd);
	}
	public SquareWave(int nSin) {
		this(1000, 5, nSin, true);
	}
	@Override
	public SquareWave copy() {
		return new SquareWave(this.frequency, this.amplitude, this.sinusoids.length, this.isOdd);
	}
	@Override
	protected void initSinusoids(int nSin) {
		this.sinusoids = new Sinusoid[nSin];
		double freq = 0, ampl = 0;
		for (int i=0; i<this.sinusoids.length; i++) {
			if (this.isOdd) {
				ampl = ((this.amplitude * 4) / ((i * 2 + 1) * Math.PI));
				this.sinusoids[i] = new Sinusoid(((i * 2 + 1) * this.frequency), ampl, 0);
			} else {
				ampl = ((Math.pow(-1, i) * this.amplitude * 4) / ((i * 2 + 1) * Math.PI));
				this.sinusoids[i] = new Cosinusoid(((i * 2 + 1) * this.frequency), ampl, 0);
			}
		}
		ampl = 0;
	}
}