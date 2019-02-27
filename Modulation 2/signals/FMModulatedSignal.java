package signals;

import java.text.DecimalFormat;

public class FMModulatedSignal extends ModulatedSignal{
	
	private double deltaF = 0;
	
    public FMModulatedSignal(Cosinusoid info, Signal load, double deltaF) {
    	super(info, load, load.getFrequency(), load.getAmplitude(), load.getDegInitPhase());
    	this.deltaF = deltaF;
    }
    @Override
    public FMModulatedSignal copy() {
    	return new FMModulatedSignal((Cosinusoid)(super.info), super.load, this.deltaF);
    }
    @Override
    public double getModulationIndex() {
    	return (this.getDeltaF() / super.info.getFrequency());
    }
    public double getKF() {
    	return ((this.deltaF * 2 * Math.PI) / super.info.getAmplitude());
    }
    public double getDeltaF(){
    	return this.deltaF;
    }
    public double getFrequency(double t) {
    	return this.getAngularFrequency(t) / (2 * Math.PI);
    }
    public double getAngularFrequency(double t) {
    	return 2 * Math.PI * (super.load.getFrequency() + this.deltaF * Math.cos(super.info.getAngularFrequency() * t));
    }
    @Override   
    public double v(double t) {	//t parameter indicates the specific time frame in seconds
    	return (super.load.getAmplitude() * Math.cos((super.load.getAngularFrequency() * t) + (this.getModulationIndex() * Math.sin(super.info.getAngularFrequency() * t))));
    }
    @Override
    public String toString() {
    	double t = this.getRandomTime();
		DecimalFormat f = new DecimalFormat("##.###");
    	return "Type: " + ("" + this.getClass()).substring(14) + "\n" + 
    		   "Amplitude: " + this.amplitude + " volt\n" +
		       "Peak-to-peak value: " + super.getPeakToPeakValue() + " volt\n" +
		       "\u0394F: " + this.deltaF + "\n" + 
		       "KF: " + f.format(this.getKF()) + "\n" + 
		       "Modulation index: " + this.getModulationIndex() + "\n" + 	
		       "t: " + t + "\n" + 
		       "Frequency(t): " + f.format(this.getFrequency(t)) + " Hz\n" + 
		       "Angular frequency: " + f.format(this.getAngularFrequency(t)) + " rad/s\n" +
		       "Initial phase: " + this.getDegInitPhase() + " deg";
    }
    private double getRandomTime() {
    	String period = "" + super.getPeriod();
    	int length = period.length();
    	return (length <= 3) ? Math.random() / Math.pow(10, length-2) : Math.random() * Math.pow(10, period.charAt(length-1));
    }
}
