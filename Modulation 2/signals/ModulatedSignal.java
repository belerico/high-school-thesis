package signals;

import java.text.DecimalFormat;

public abstract class ModulatedSignal extends Signal{

	protected Signal info = null;
	protected Signal load = null;

	public ModulatedSignal(Signal info, Signal load, double frequency, double amplitude, double degInitPhase) {
		super(frequency, amplitude, degInitPhase);
		this.info = info;
		this.load = load;
	}
    public Signal getInfoSignal() {
    	return this.info;
    }
    public Signal getLoadSignal() {
    	return this.load;
    }
    public abstract double getModulationIndex();

    public abstract ModulatedSignal copy();
		
    public abstract double v(double t);
    
    @Override
    public String toString() {
		DecimalFormat f = new DecimalFormat("##.###");
    	return "Type: " + ("" + this.getClass()).substring(14) + "\n" + 
    		   "Amplitude: " + this.amplitude + " volt\n" +
		       "Peak-to-peak value: " + super.getPeakToPeakValue() + " volt\n" +
		       "Modulation index: " + f.format(this.getModulationIndex()) + "\n" + 	
		       "Frequency: " + this.frequency + " Hz\n" +
		       "Angular frequency: " + f.format(this.getAngularFrequency()) + " rad/s\n" +
		       "Period: " + f.format((super.getPeriod() * 1000)) + " ms\n" +
		       "Initial phase: " + f.format(this.getDegInitPhase()) + " deg";
    }
}
