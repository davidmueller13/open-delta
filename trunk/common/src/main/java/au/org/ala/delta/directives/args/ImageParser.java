package au.org.ala.delta.directives.args;

import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.image.ImageInfo;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageOverlayParser;

/**
 * Parses the arguments to the CHARACTER IMAGES, TAXON IMAGES directives.
 */
public class ImageParser extends DirectiveArgsParser {

	protected ImageOverlayParser _overlayParser;
	protected int _imageType;
	protected List<ImageInfo> _imageInfo;
	
	public ImageParser(DeltaContext context, Reader reader, int imageType) {
		super(context, reader);
		_overlayParser = new ImageOverlayParser();
		_imageType = imageType;
	}
	
	/**
	 * Unfortunately we cannot create Image objects as they need to be 
	 * attached to a taxon or character and these are the last directives
	 * parsed.  Hence we store the data in a form that can be used when
	 * the characters and taxa are created.
	 */
	public List<ImageInfo> getImageInfo() {
		return _imageInfo;
	}
	
	@Override
	public void parse() throws ParseException {
		
		_args = new DirectiveArguments();
		_imageInfo = new ArrayList<ImageInfo>();
		readNext();
		skipWhitespace();
		
		while (_currentChar == '#') {
			expect('#');
			Object id = readId();
			skipWhitespace();
			
			while (_currentInt >= 0 && _currentChar != '#') {
				readImage(id);
			}
		}
		
	}

	protected void readImage(Object id) throws ParseException {
		String fileName = readFileName();
		skipWhitespace();
		
		List<ImageOverlay> overlays = new ArrayList<ImageOverlay>();
		while (_currentChar == '<') {
			String overlayText = readToNext('>');
			overlayText+='>';
			overlays.addAll(_overlayParser.parseOverlays(overlayText, _imageType));
			readNext();
			skipWhitespace();
		}
		
		_imageInfo.add(new ImageInfo(id, _imageType, fileName, overlays));
	}
	
	protected Object readId() throws ParseException {
		expect('#');
		
		mark();
		readNext();
		if (Character.isDigit(_currentChar)) {
			reset();
			
			return readListId();
		}
		else {
			reset();
			
			return readItemDescription();
		}
	}
	
	protected String readFileName() throws ParseException {
		return readToNextWhiteSpaceOrEnd();
	}
}