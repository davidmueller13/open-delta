package au.org.ala.delta.translation.print;

import au.org.ala.delta.translation.PrintFile;

public class PlainTextTypeSetter implements CharacterListTypeSetter {

	protected PrintFile _printer;
	
	public PlainTextTypeSetter(PrintFile printer) {
		_printer = printer;
	}

	@Override
	public void beforeCharacterOrHeading() {
		
	}

	@Override
	public void beforeFirstCharacter() {
		_printer.setLineWrapIndent(10);
	}

	@Override
	public void beforeCharacterHeading() {
		_printer.writeBlankLines(1, 0);
	}

	@Override
	public void afterCharacterHeading() {
		
	}

	@Override
	public void beforeCharacter() {
		
	}

	@Override
	public void beforeStateDescription() {
		_printer.setIndent(7);
	}

	@Override
	public void beforeCharacterNotes() {
		
	}

	@Override
	public void afterCharacterList() {
		
	}

}