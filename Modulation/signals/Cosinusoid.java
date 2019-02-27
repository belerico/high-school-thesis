package signals;

import java.text.DecimalFormat;

public class Cosinusoid extends Sinusoid{
	
	public Cosinusoid(double frequency, double amplitude, double degInitPhase){	//degInitPhase parameter specifies the initial angle phase in degrees
		super.setFrequency(frequency);
		super.setAmplitude(amplitude);
		super.setInitPhase(degInitPhase + 90);
	}
    public Cosinusoid(){
    	super(1000, 5, 90);
    }
    @Override
    public Cosinusoid copy(){
    	return new Cosinusoid(this.frequency, this.amplitude, (this.getDegInitPhase() - 90));
    }
    @Override
    public String toString(){
		DecimalFormat f = new DecimalFormat("##.###");
    	return "Type: " + ("" + this.getClass()).substring(14) + "\n" + 
    		   "Amplitude: " + this.amplitude + " volt\n" +
    		   "Peak-to-peak value: " + super.getPeakToPeakValue() + " volt\n" +
    		   "Frequency: " + this.frequency + " Hz\n" +
    		   "Angular frequency: " + f.format(this.getAngularFrequency()) + " rad/s\n" +
    		   "Period: " + f.format(super.getPeriod() * 1000) + " ms\n" +
    		   "Initial phase: " + f.format(this.getDegInitPhase() - 90) + " deg";
    }
}
