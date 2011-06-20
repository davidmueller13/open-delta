package au.org.ala.delta.ui.image.overlay;

import java.awt.Dimension;

import javax.swing.JComponent;

import au.org.ala.delta.ui.image.ImageViewer;

public class OverlayLocation {

	private JComponent _component;
	private ImageViewer _image;
	private au.org.ala.delta.model.image.OverlayLocation _location;
	
	public OverlayLocation(ImageViewer image, JComponent component, au.org.ala.delta.model.image.OverlayLocation location) {
		_component = component;
		_image = image;
		_location = location;
	}
	
	public int getX() {
		double scaledWidth = _image.getImageWidth();
		double width = _image.getPreferredImageWidth();
		
		double toPixels = width / 1000d;
		
		double halfComponentWidth = 0.5 * preferredWidth();
		double midPointXInPixels = _location.X*toPixels + halfComponentWidth;
		
		double imageScale = scaledWidth/width;
		
		return (int)Math.round(midPointXInPixels*imageScale - halfComponentWidth);
	}
	
	public int getY() {
		double scaledHeight = _image.getImageHeight();
		double height = _image.getPreferredImageHeight();
		
		double toPixels = height / 1000d;
		
		double halfComponentHeight = 0.5 * preferredHeight();
		double midPointYInPixels = _location.Y*toPixels + halfComponentHeight;
		
		double imageScale = scaledHeight/height;
		
		
		return (int)Math.round(midPointYInPixels*imageScale - halfComponentHeight);
	}
	
	public int preferredHeight() {
		Dimension preferredSize = _component.getPreferredSize();
		if (preferredSize != null) {
			return preferredSize.height;
		}
		double scaledHeight = _image.getImageHeight();
		
		return (int)(_location.H / 1000d * scaledHeight);
		
		
	}
	
	public int preferredWidth() {
		Dimension preferredSize = _component.getPreferredSize();
		if (preferredSize != null) {
			return preferredSize.width;
		}
		
		double scaledWidth = _image.getImageWidth();
		
		return (int)(_location.W / 1000d * scaledWidth);
	}
	
}
