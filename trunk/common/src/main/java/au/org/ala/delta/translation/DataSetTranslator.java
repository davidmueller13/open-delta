package au.org.ala.delta.translation;


public interface DataSetTranslator {

	public void translateCharacters();
	
	public void translateItems();
	
	public void translateOutputParameter(String parameterName);
}