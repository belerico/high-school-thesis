package signals.wave;

import signals.Sinusoid;
import signals.Cosinusoid;

public class TriangleWave extends Wave {

	public TriangleWave(double frequency, double amplitude, int nSin, boolean isOdd) {
		super(frequency, amplitude, nSin, isOdd);
	}
	public TriangleWave(int nSin) {
		this(1000, 5, nSin, true);
	}
	@Override
	public TriangleWave copy() {
		return new TriangleWave(this.frequency, this.amplitude, this.sinusoids.length, this.isOdd);
	}
	@Override
	protected void initSinusoids(int nSin) {
		this.sinusoids = new Sinusoid[nSin];
		double ampl = 0;
		for (int i=0; i<this.sinusoids.length; i++) {
			if (this.isOdd) {
				ampl = 8 / (Math.pow((i * 2 + 1), 2) * Math.pow(Math.PI, 2)) * Math.pow(-1, i) * this.amplitude;
				this.sinusoids[i] = new Sinusoid(((i * 2 + 1) * this.frequency), ampl, 0);
			} else {
				ampl = 8 / (Math.pow((i * 2 + 1), 2) * Math.pow(Math.PI, 2)) * Math.pow(1, i) * this.amplitude;
				this.sinusoids[i] = new Cosinusoid(((i * 2 + 1) * this.frequency), ampl, 0);
			}
		}
		ampl = 0;
	}
}