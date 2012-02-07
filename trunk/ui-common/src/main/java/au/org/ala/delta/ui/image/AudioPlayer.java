/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *   
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *   
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.ui.image;

import java.awt.LinearGradientPaint;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 * Helper class for playing audio.
 */
public class AudioPlayer {

	/**
	 * Plays a short sound using the java sound Clip mechanism which loads the sound file into memory.
	 * 
	 * @param sound
	 *            the URL containing the audio stream.
	 */
	public static void playClip(URL sound) throws LineUnavailableException {

		try {
			Line.Info lineInfo = new Line.Info(Clip.class);
			Line line = null;
			
			Mixer.Info[] mixers = AudioSystem.getMixerInfo();
			if (mixers.length > 0) {			
				Mixer.Info mixerInfo = mixers[0];
				line = AudioSystem.getMixer(mixerInfo).getLine(lineInfo);
			} else {
				line = AudioSystem.getLine(lineInfo);	
			}
			
			if ( line == null) {
				
				return;
			}
			 
			Clip clip = (Clip) line;
			AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
			clip.open(ais);

			clip.addLineListener(new LineListener() {
				public void update(LineEvent evt) {
					
					if (evt.getType() == LineEvent.Type.STOP) {						
						evt.getLine().close();
					}
					
				}
				
			});

			clip.start();
		} catch (LineUnavailableException laex) {
			throw laex;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
