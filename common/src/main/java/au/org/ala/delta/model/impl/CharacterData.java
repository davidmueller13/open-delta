package au.org.ala.delta.model.impl;

public interface CharacterData {

	public String getName();
	
	public String getDescription();
	
	public boolean isExclusive();
	
	public boolean isMandatory();
	
	public String getUnits();
	
	public String getStateText(int stateNumber);
	
	public int getNumberOfStates();

	public void setMandatory(boolean b);

	public void setDescription(String desc);

	public void setUnits(String units);
}
