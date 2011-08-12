package au.org.ala.delta.editor.slotfile;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.ala.delta.DeltaTestCase;
import au.org.ala.delta.model.image.ImageOverlay;


/**
 * Tests the VOImageDesc class.
 */ 
public class VOImageDescTest extends DeltaTestCase {
 
	/** Holds the instance of the class we are testing */
	private DeltaVOP _vop;
	
	private File _slotFile;
	
	@Before
	public void setUp() throws Exception {
		_slotFile = copyURLToFile("/SAMPLE.DLT");
			
		_vop = new DeltaVOP(_slotFile.getAbsolutePath(), false);
	}

	@After
	public void tearDown() throws Exception {
		if (_vop != null) {
			_vop.close();
		}
		super.tearDown();
	}
	
	/**
	 * Since we are working with the sample data set we know which taxa have images.
	 */
	@Test
	public void testReadWriteImageDesc() {
		
		VOImageDesc desc = getImageDesc(4, 0);
		List<ImageOverlay> overlays = desc.readAllOverlays();
		
		overlays.get(0).comment = "blah";
		desc.writeAllOverlays(overlays);
		
		overlays = desc.readAllOverlays();
		assertEquals("blah", overlays.get(0).comment);
	}
	
	/**
	 * Tests an overlay can updated correctly.
	 */
	@Test
	public void testReplaceOverlay() {
		
		VOImageDesc desc = getImageDesc(7, 0);
		List<ImageOverlay> overlays = desc.readAllOverlays();
		ImageOverlay overlay = overlays.get(0);
		overlay.overlayText = "Test";
		
		desc.replaceOverlay(overlay, true);
		
		overlay = desc.readOverlay(overlay.getId());
		
		assertEquals(desc.readAllOverlays().size(), overlays.size());
		
		assertEquals("Test", overlay.overlayText);
		
		_vop.commit(_vop.getPermSlotFile());
		_vop.close();
		
		_vop = new DeltaVOP(_slotFile.getAbsolutePath(), false);
		
		desc = getImageDesc(7, 0);
		overlay = desc.readOverlay(overlay.getId());
		assertEquals("Test", overlay.overlayText);
	}
	
	@Test
	public void testWriteAllOverlays() {
		VOImageDesc desc = getImageDesc(7, 0);
		List<ImageOverlay> overlays = desc.readAllOverlays();
		
		desc.writeAllOverlays(overlays);
		
		List<ImageOverlay> overlays2 = desc.readAllOverlays();
		
		compareOverlayList(overlays, overlays2);
		
		_vop.commit(_vop.getPermSlotFile());
		_vop.close();
		
		_vop = new DeltaVOP(_slotFile.getAbsolutePath(), false);
		
		desc = getImageDesc(7, 0);
		overlays2 = desc.readAllOverlays();
		
		compareOverlayList(overlays, overlays2);
		
	}
	
	
	@Test
	public void testDeleteOverlays() {
		VOImageDesc desc = getCharacterImageDesc(6, 0);
		
		List<ImageOverlay> overlays = desc.readAllOverlays();
		for (ImageOverlay overlay : overlays) {
			desc.removeOverlay(overlay.getId());
			System.out.println("Deleting : "+overlay.getId());
			List<ImageOverlay> tmpOverlays = desc.readAllOverlays();
			for (ImageOverlay tmp : tmpOverlays) {
				System.out.println(tmp.getId());
			}
		}
		System.out.println(desc.readAllOverlays());
		assertEquals(0, desc.readAllOverlays().size());
	}

	private void compareOverlayList(List<ImageOverlay> overlays,
			List<ImageOverlay> overlays2) {
		assertEquals(overlays.size(), overlays2.size());
		
		for (int i=0; i<overlays.size(); i++) {
			ImageOverlay overlay = overlays.get(i);
			ImageOverlay overlay2 = overlays2.get(i);
			
			assertEquals(overlay.getId(), overlay2.getId());
		}
	}
	
	private VOImageDesc getImageDesc(int itemNumber, int imageNumber) {
		int id = _vop.getDeltaMaster().uniIdFromItemNo(itemNumber);
		VOItemDesc item = (VOItemDesc)_vop.getDescFromId(id);
		List<Integer> imageIds = item.readImageList();
		
		VOImageDesc desc = (VOImageDesc)_vop.getDescFromId(imageIds.get(imageNumber));
		
		return desc;
	}
	
	private VOImageDesc getCharacterImageDesc(int characterNumber, int imageNumber) {
		int id = _vop.getDeltaMaster().uniIdFromCharNo(characterNumber);
		VOCharBaseDesc item = (VOCharBaseDesc)_vop.getDescFromId(id);
		List<Integer> imageIds = item.readImageList();
		
		VOImageDesc desc = (VOImageDesc)_vop.getDescFromId(imageIds.get(imageNumber));
		
		return desc;
	}
}
