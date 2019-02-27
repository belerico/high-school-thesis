package signals.wave;

import signals.Signal;
import signals.Sinusoid;

public abstract class Wave extends Signal{
	
	protected boolean isOdd;
	protected Sinusoid[] sinusoids = null;
	
	private int checkNSin(int nSin) throws IllegalArgumentException{
		if (nSin < 1)
			throw new IllegalArgumentException("At least a signal must be composed of one sinusoid");
		return nSin;
	}
	private int checkIndex(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index > this.sinusoids.length)
			throw new ArrayIndexOutOfBoundsException("index: " + index + " length: " + this.sinusoids.length);
		return index;
	}
	public Wave(double frequency, double amplitude, int nSin, boolean isOdd) throws IllegalArgumentException{
		super(frequency, amplitude, 0);
		this.isOdd = isOdd;
		this.initSinusoids(this.checkNSin(nSin));
	}
	public boolean isOdd() {
		return isOdd;
	}

	public double getFrequency(int sinusoidNum) {
		return this.sinusoids[this.checkIndex(sinusoidNum - 1)].getFrequency();
	}
	@Override
	public double getFrequency() {
		return this.sinusoids[0].getFrequency();
	}

	public double getAmplitude(int sinusoidNum) {
		return this.sinusoids[this.checkIndex(sinusoidNum - 1)].getAmplitude();
	}

	public double getMaxAmplitude() {
		double max = this.sinusoids[0].getAmplitude();
		for (int i=1; i<this.sinusoids.length; i++) 
			max = Math.max(this.sinusoids[i].getAmplitude(), max);
		return max;
	}
	@Override
	public double v(double t) {
		double sum = 0;
		for (int i=0; i<this.sinusoids.length; i++)
			sum += this.sinusoids[i].v(t);
		return sum;
	}
	protected abstract void initSinusoids(int nSin);
}