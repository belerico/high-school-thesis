package signals;

public class AMModulatedSignal extends ModulatedSignal{

    public AMModulatedSignal(Signal info, Signal load){
    	super(info, load, load.getFrequency(), (info.getAmplitude() + load.getAmplitude()), load.getDegInitPhase());
    }
    @Override
    public AMModulatedSignal copy(){
    	return new AMModulatedSignal(super.info, super.load);
    }
    @Override
    public double getModulationIndex(){
    	return (super.info.getAmplitude() / super.load.getAmplitude());
    }
    public double r(double t){	//t parameter indicates the specific time frame in seconds
    	return super.load.getAmplitude() + super.info.v(t);
    }
    @Override
    public double v(double t){	//t parameter indicates the specific time frame in seconds
    	return this.r(t) * super.load.v(t) / super.load.getAmplitude();
    }
}