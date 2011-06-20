package au.org.ala.delta.model.image;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;



public class ImageOverlay {
	public static final int OVERLAY_TYPE_COUNT = OverlayType.LIST_END; // Total number of Overlay types we know about (+ 1)

	public static final int OLOC_FLAG_COLOUR_MASK = 0x00ffffff;
	public static final int OLOC_FLAG_HOTSPOT = 0x01000000;
	public static final int OLOC_FLAG_POPUP = 0x02000000;
	public static final int OLOC_FLAG_COLOUR = 0x04000000;

	public static final byte OL_OMIT_DESCRIPTION = 0x1;
	public static final byte OL_INCLUDE_COMMENTS = 0x2;
	public static final byte OL_CENTER_TEXT = 0x4;
	public static final byte OL_INTEGRAL_HEIGHT = 0x8;

	public static final int OLSHOW_CONTROL = 0x80000000;
	public static final int OLSHOW_HOTSPOT = 0x40000000;
	public static final int OLSHOW_POPUPS = 0x20000000;
	public static final int OLSHOW_ALL = (OLSHOW_CONTROL | OLSHOW_HOTSPOT | OLSHOW_POPUPS);
	public static final int OLSHOW_IDMASK = ~(OLSHOW_CONTROL | OLSHOW_HOTSPOT | OLSHOW_POPUPS);
	public static final int ID_OVERLAY_FIRST = 0x100;

	public static final int ID_OK = 1;
	public static final int ID_CANCEL = 2;
	public static final int ID_NOTES = 8;
	public static final int ID_OUTLINE = 9;
	public static final int ID_BUTTON_BLOCK = 0xA;
	public static final int ID_IMAGE_NOTES = 0XB;

	public int type;
	public String overlayText;
	public String comment;

	public List<OverlayLocation> location; // List of locations - first is that of the "main" overlay
	// object, (required for all overlay objects except comments)
	// then (optionally) those of any associated "hotspots"

	public int stateId; // Might be appropriate to use a "union" here, but
	public String minVal; // since TDeltaNumber has a constructor, it is not allowable
	public String maxVal; // But for any given overlay, depending on type, we really
	public String keywords; // need only 1 of stateId, (minVal && maxVal), or keywords
	public String displayText; // Buffer for constructing entire text string

	public ImageOverlay() {
		this(OverlayType.OLNONE);
	}

	public String toString() {
		return String.format("Overlay: Type=%d, overlayText=%s, Comment=%s, StateID=%d, minVal=%d, maxVal=%d, KeyWords=%s, displayText=%s\nLocations: %s", type, overlayText, comment, stateId,
				minVal, maxVal, keywords, displayText, location);
	}

	public ImageOverlay(int aType) {
		type = aType;
		location = new ArrayList<OverlayLocation>();
		overlayText = "";
		comment = "";
	}

	public boolean isType(int overlayType) {
		return type == overlayType;
	}
	
    public int getId() {
    	if (location.size() == 0) {
    		return -1;
    	}
    	return location.get(0).ID;
    }
	
	// Should "keywords" be a string? eventually I think this information will be a set of
	// group IDs. But this "group" mechanism doesn't yet exist...

	// These next members are used for drawing purposes, rather than for
	// storing actual information about the overlay.
	// TImageWindow* parent; // Pointer to "owner" window
	// void SetParent(TImageWindow* newParent) { Destroy(); parent = newParent; }

	// void Hide(unsigned long id = OLSHOW_ALL);
	// void Show(unsigned long id = OLSHOW_ALL, HDC hDCPaint = 0, TOverlayLocation::visibility how=TOverlayLocation::yes, bool isErasure = false);
	// void Destroy(unsigned long id = OLSHOW_ALL);
	// void Recreate(unsigned long id = OLSHOW_ALL, bool doCreate=true);
	// //void Refresh();
	// void Rescale(unsigned long id = OLSHOW_ALL, unsigned int flags = 0);
	// void Deselect();
	// void ChangeFont();
	// void ValidateControl(TControl* control);
	// OverlayFontType GetFontType();
	// bool HasVisibleElement() const { return !location.empty(); }
	public boolean hasVisibleElement() {
		return !(type == OverlayType.OLCOMMENT || type == OverlayType.OLSUBJECT || type == OverlayType.OLSOUND || location.isEmpty());
	}

	public boolean hasTextBox() {
		throw new NotImplementedException();
	}

	public int getNHotSpots() {
		return Math.max(0, (int) location.size() - 1);
	}

	public boolean IsButton() {
		return type == OverlayType.OLOK || type == OverlayType.OLCANCEL || type == OverlayType.OLNOTES || type == OverlayType.OLIMAGENOTES;
	}

	public int getX() {
		return getX(0);
	}

	public int getX(int id) {
		return location.get(id).X;
	}

	public int getY() {
		return getY(0);
	}

	public int getY(int id) {
		return location.get(id).Y;
	}

	public int getHeight(int id, double yscale) {
		return location.get(id).H;
		
	}

	public int getWidth(int id) {
		return location.get(id).W;
	}

	public OverlayLocation getLocation(int id) {
		return location.get(id);
	}

	public boolean containsId(int id) {
		throw new NotImplementedException();
	}

	public boolean canSelect() {
		return type == OverlayType.OLSTATE || type == OverlayType.OLVALUE;
	}

	public String getValueString() {
		throw new NotImplementedException();
	}

	public String getDisplayText() {
		throw new NotImplementedException();
	}

	public void updateText() {
		throw new NotImplementedException();
	}

	// static const char const * punct;

	public void clearAll() {
		overlayText = "";
		comment = "";
		location.clear();
		keywords = "";
		minVal = "";
		maxVal = "";

		stateId = 0;
		type = OverlayType.OLNONE;
	}
}
