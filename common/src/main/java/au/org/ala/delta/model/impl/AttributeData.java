package au.org.ala.delta.model.impl;

public interface AttributeData {
	
	public String getValue();
	
	public void setValue(String value);
	
	public boolean isStatePresent(int stateNumber);
	
	public boolean isSimple();

	public void setStatePresent(int stateNumber, boolean present);
}
