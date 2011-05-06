package au.org.ala.delta.editor.ui.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.OrderedMultiStateCharacter;
import au.org.ala.delta.model.RealCharacter;
import au.org.ala.delta.model.TextCharacter;
import au.org.ala.delta.model.UnorderedMultiStateCharacter;
import au.org.ala.delta.ui.util.IconHelper;
import au.org.ala.delta.util.Platform;

public class EditorUIUtils {

    private static final String ICON_PATH = "/au/org/ala/delta/editor/resources/icons";
    private static final ImageIcon _textIcon = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/textchar.png");
    private static final ImageIcon _realIcon = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/realchar.png");
    private static final ImageIcon _intIcon = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/intchar.png");
    private static final ImageIcon _omIcon = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/omchar.png");
    private static final ImageIcon _umIcon = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/umchar.png");
    private static final ImageIcon _inapplicableOverlay = IconHelper.createImageIconFromAbsolutePath(ICON_PATH + "/inapplicable_overlay.png");
    
    private static final Map<CharacterType, ImageIcon> _inapplicableIcons = new HashMap<CharacterType, ImageIcon>();
    
    public static ImageIcon createLargeIcon() {
        return IconHelper.createBlue32ImageIcon();
    }

    public static ImageIcon createDeltaImageIcon() {
        return IconHelper.createBlue16ImageIcon();
    }

    public static ImageIcon createInternalFrameNormalIcon() {

        if (Platform.isWindowsAero()) {
            return IconHelper.createBlue14ImageIcon();
        } else {
            return IconHelper.createBlue16ImageIcon();
        }

    }

    public static ImageIcon createInternalFrameMaximizedIcon() {
        if (Platform.isWindowsAero()) {
            return IconHelper.createBlue12ImageIcon();
        } else {
            return IconHelper.createBlue14ImageIcon();
        }
    }
    
    public static ImageIcon iconForCharacter(au.org.ala.delta.model.Character ch, boolean inapplicable) {
        ImageIcon icon = null;
        if (ch instanceof TextCharacter) {
            icon = _textIcon;
        } else if (ch instanceof RealCharacter) {
            icon = _realIcon;
        } else if (ch instanceof IntegerCharacter) {
            icon = _intIcon;
        } else if (ch instanceof OrderedMultiStateCharacter) {
            icon = _omIcon;
        } else if (ch instanceof UnorderedMultiStateCharacter) {
            icon = _umIcon;
        }
        if (!inapplicable) {
        	return icon;
        }
        
        synchronized (_inapplicableIcons) {
        	
        	if (_inapplicableIcons.containsKey(ch.getCharacterType())) {
        		return _inapplicableIcons.get(ch.getCharacterType());
        	}
        	
            ImageIcon overlay = _inapplicableOverlay;            
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), 0, 0, overlay.getIconWidth(), overlay.getIconHeight(), null);
            g.drawImage(overlay.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), 0, 0, overlay.getIconWidth(), overlay.getIconHeight(), null);            
            icon = new ImageIcon(image);
            _inapplicableIcons.put(ch.getCharacterType(), icon);			
		}
        return icon;
    }
    
    /**
     * Returns the appropriate icon for the supplied character.
     * 
     * @param ch
     *            the character to get the icon for.
     * @return an icon representing the type of the supplied Character
     */
    public static ImageIcon iconForCharacter(au.org.ala.delta.model.Character ch) {
    	return iconForCharacter(ch, false);
    }
   
}
