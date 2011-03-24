package au.org.ala.delta.model.impl;

import java.util.List;

import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;

public interface ItemData {
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public List<Attribute> getAttributes();
	
	public Attribute getAttribute(Character character);

	public void addAttribute(Character character, String value);

	public boolean isVariant();
	
	public void setVariant(boolean variant);
}
