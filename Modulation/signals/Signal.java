package signals;

import java.text.DecimalFormat;

public abstract class Signal {

	protected double frequency;	//Signal frequency in Hz
	protected double amplitude;	//Signal amplitude in Volt
	protected double initPhase;	//Signal Phase in radians
	
    public Signal(double frequency, double amplitude, double degInitPhase) throws IllegalArgumentException{	//degInitPhase parameter specifies the initial angle phase in degrees
    	this.setFrequency(frequency);
    	this.setAmplitude(amplitude);
    	this.setInitPhase(degInitPhase);
    }
    public Signal(){
    	this.frequency = 1000;
    	this.amplitude = 5;
    	this.initPhase = 0;
    }
    public double getFrequency(){
    	return this.frequency;
    }
    public double getAmplitude(){
    	return this.amplitude;
    }
    public double getInitPhase(){
    	return this.initPhase;
    }
    public double getDegInitPhase(){
    	return Math.toDegrees(this.initPhase);
    }
    public double getPeriod(){
    	return (1 / this.frequency);
    }
    public double getAngularFrequency(){
    	return (2 * Math.PI * this.frequency);
    }
    public double getPeakToPeakValue(){
    	return (this.amplitude * 2);
    }
    public void setFrequency(double frequency) throws IllegalArgumentException{
    	if (frequency < 0)
    		throw new IllegalArgumentException("Frequency must be greater than 0");
    	this.frequency = frequency;
    }
    public void setAmplitude(double amplitude) {
    	this.amplitude = amplitude;
    }
    public void setInitPhase(double degInitPhase){	//degInitPhase parameter specifies the initial angle phase in degrees
    	this.initPhase = Math.toRadians(degInitPhase);
    }
    public void setPeriod(double secPeriod){	//the time taken for one full oscillation in seconds
    	this.setFrequency(1 / secPeriod);
    }
    public void setAngularFrequency(double angularFrequency) throws IllegalArgumentException{
    	if (angularFrequency < 0)
    		throw new IllegalArgumentException("Angular frequency must be greater than 0");
    	this.setFrequency(angularFrequency / 2 * Math.PI);
    }
    @Override
    public String toString(){
		DecimalFormat f = new DecimalFormat("##.###");
    	return "Type: " + ("" + this.getClass()).substring(14) + "\n" + 
    		   "Amplitude: " + this.amplitude + " volt\n" +
    		   "Peak-to-peak value: " + this.getPeakToPeakValue() + " volt\n" +
    		   "Frequency: " + this.frequency + " Hz\n" +
    		   "Angular frequency: " + f.format(this.getAngularFrequency()) + " rad/s\n" +
    		   "Period: " + f.format((this.getPeriod() * 1000)) + " ms\n" +
    		   "Phase: " + f.format(this.getDegInitPhase()) + " deg";
    }
    public abstract Signal copy();

    public abstract double v(double t);
}
