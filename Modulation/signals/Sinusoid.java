package signals;

import signals.Signal;

public class Sinusoid extends Signal{

	public Sinusoid(double frequency, double amplitude, double degInitPhase){	//degInitPhase parameter specifies the initial angle phase in degrees
		super.setFrequency(frequency);
		super.setAmplitude(amplitude);
		super.setInitPhase(degInitPhase);
	}
    public Sinusoid(){
    	super();
    }
    @Override
    public Sinusoid copy(){
    	return new Sinusoid(this.frequency, this.amplitude, this.getDegInitPhase());
    }
    @Override
    public double v(double t){	//t parameter indicates the specific time frame in seconds
    	return this.amplitude * Math.sin((this.getAngularFrequency() * t) + this.initPhase);
    }
}