package signals.wave;

import signals.Sinusoid;

public class SawtoothWave extends Wave {

	public SawtoothWave(double frequency, double amplitude, int nSin) {
		super(frequency, amplitude, nSin, true);
	}
	public SawtoothWave(int nSin) {
		this(1000, 5, nSin);
	}
	@Override
	public SawtoothWave copy() {
		return new SawtoothWave(this.frequency, this.amplitude, this.sinusoids.length);
	}
	@Override
	protected void initSinusoids(int nSin) {
		this.sinusoids = new Sinusoid[nSin];
		double ampl = 0;
		for (int i=0; i<this.sinusoids.length; i++) {
			ampl = (this.amplitude * 2) / ((i + 1) * Math.PI);
			this.sinusoids[i] = new Sinusoid(((i + 1) * this.frequency), Math.pow(-1, i) * ampl, 0);
		}
		ampl = 0;
	}
}