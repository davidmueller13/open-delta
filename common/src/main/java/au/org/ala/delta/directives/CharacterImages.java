package au.org.ala.delta.directives;

import java.io.StringReader;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.args.ImageParser;
import au.org.ala.delta.model.image.ImageType;

/**
 * Processes the CHARACTER IMAGES directive.
 */
public class CharacterImages extends AbstractImageDirective {

	public CharacterImages() {
		super(ImageType.IMAGE_CHARACTER, "character", "images");
	}
	
	@Override
	protected ImageParser createParser(DeltaContext context, StringReader reader) {
		return new ImageParser(context, reader, ImageType.IMAGE_CHARACTER);
	}
}
