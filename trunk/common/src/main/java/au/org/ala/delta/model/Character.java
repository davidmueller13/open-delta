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
package au.org.ala.delta.model;

import au.org.ala.delta.model.image.Image;
import au.org.ala.delta.model.impl.CharacterData;
import au.org.ala.delta.model.impl.ControllingInfo;
import au.org.ala.delta.model.observer.CharacterObserver;
import au.org.ala.delta.model.observer.ImageObserver;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Illustratable, Comparable<Character>, ImageObserver {
    
    protected CharacterData _impl;
    private CharacterType _characterType;
    private List<CharacterObserver> _observers;

    protected Character(CharacterType characterType) {
        _characterType = characterType;
    }

    public CharacterType getCharacterType() {
        return _characterType;
    }

    public int getCharacterId() {
        return _impl.getNumber();
    }
    
    /**
	 * This needs to be done when Characters are inserted or deleted - the remaining Characters
	 * may need to be renumbered to account for the change.
	 * @param number the new number for this Character.
	 */
	public void setCharacterNumber(int number) {
		_impl.setNumber(number);
	}

    public String getDescription() {
        return _impl.getDescription();
    }

    public void setDescription(String desc) {
        _impl.setDescription(desc);
        notifyObservers();
    }

    public void addDependentCharacters(CharacterDependency dependency) {
        _impl.addDependentCharacters(dependency);
        notifyObservers();
    }

    public List<CharacterDependency> getDependentCharacters() {
        return new ArrayList<CharacterDependency>(_impl.getDependentCharacters());
    }
    
    /**
     * Adds the supplied CharacterDependency to the list of attributes
     * that control (or make inapplicable) this Character.
     * 
     * Use CharacterDependency.addDependentCharacter instead of this method!
     * @param dependency the CharacterDependency that controls this Character.
     */
    public void addControllingCharacter(CharacterDependency dependency) {
       _impl.addControllingCharacters(dependency);
       notifyObservers();
    }

    /**
     * Removes the supplied CharacterDependency to the list of attributes
     * that control (or make inapplicable) this Character.
     * 
     * Use CharacterDependency.removeDependentCharacter instead of this method!
     * @param characterDependency the CharacterDependency that controls this Character.
     */
	public void removeControllingCharacter(
			CharacterDependency characterDependency) {
		_impl.removeControllingCharacter(characterDependency);
		notifyObservers();
	}

    
    public List<CharacterDependency> getControllingCharacters() {
        return new ArrayList<CharacterDependency>(_impl.getControllingCharacters());
    }

    public void setMandatory(boolean b) {
        _impl.setMandatory(b);
        notifyObservers();
    }

    public boolean isMandatory() {
        return _impl.isMandatory();
    }

    public void setNotes(String notes) {
        _impl.setNotes(notes);
        notifyObservers();
    }

    public String getNotes() {
        return _impl.getNotes();
    }
    
    public boolean hasNotes() {
    	return StringUtils.isNotEmpty(getNotes());
    }
    
    public float getReliability() {
        return _impl.getReliability();
    }

    public void setReliability(float reliability) {
        _impl.setReliability(reliability);
    }

    public String getItemSubheading() {
        return _impl.getItemSubheading();
    }

    public void setItemSubheading(String charItemSubheading) {
        _impl.setItemSubheading(charItemSubheading);
    }
    
    public boolean getContainsSynonmyInformation() {
        return _impl.getContainsSynonmyInformation();
    }
    
    public void setContainsSynonmyInformation(boolean containsSynonmyInfo) {
        _impl.setContainsSynonmyInformation(containsSynonmyInfo);
    }
    
    public boolean getOmitOr() {
        return _impl.getOmitOr();
    }
    
    public void setOmitOr(boolean omitOr) {
        _impl.setOmitOr(omitOr);
    }
    
    public boolean getUseCc() {
        return _impl.getUseCc();
    }
    
    public void setUseCc(boolean useCc) {
        _impl.setUseCc(useCc);
    }
    
    public boolean getOmitPeriod() {
        return _impl.getOmitPeriod();
    }
    
    public void setOmitPeriod(boolean omitPeriod) {
        _impl.setOmitPeriod(omitPeriod);
    }
    
    public boolean getNewParagraph() {
        return _impl.getNewParagraph();
    }
    
    public void setNewParagraph(boolean newParagraph) {
        _impl.setNewParagraph(newParagraph);
    }
    
    public boolean getNonAutoCc() {
        return _impl.getNonAutoCc();
    }
    
    public void setNonAutoCc(boolean nonAutoCc) {
        _impl.setNonAutoCc(nonAutoCc);
    }
    
    

    @Override
    public String toString() {
        return getImpl().getNumber() + ". " + getDescription();
    }

    public CharacterData getImpl() {
        return _impl;
    }

    public void setImpl(CharacterData impl) {
        _impl = impl;
    }

    public void validateAttributeText(String text, ControllingInfo controllingInfo) {
        _impl.validateAttributeText(text, controllingInfo);
    }
    
    @Override
	public Image addImage(String fileName, String comments) {
		Image image = _impl.addImage(fileName, comments);
		image.setSubject(this);
		image.addImageObserver(this);
		notifyObservers();
		
		return image;
	}
    

	@Override
    public void addImage(Image image) {
	    _impl.addImage(image);
        image.setSubject(this);
        image.addImageObserver(this);
        notifyObservers();
    }

    @Override
	public List<Image> getImages() {
		List<Image> images = _impl.getImages();
    	
    	for (Image image : images) {
    		image.setSubject(this);
    		image.addImageObserver(this);
    	}
    	
    	return images;
	}
	
	@Override
	public int getImageCount() {
		return _impl.getImageCount();
	}

	@Override
	public void deleteImage(Image image) {
		_impl.deleteImage(image);
        notifyObservers();
	}

	@Override
	public void moveImage(Image image, int position) {
		_impl.moveImage(image, position);
        notifyObservers();
	}

	/**
     * Registers interest in being notified of changes to this Character.
     * 
     * @param observer
     *            the object interested in receiving notification of changes.
     */
    public void addCharacterObserver(CharacterObserver observer) {
        if (_observers == null) {
            _observers = new ArrayList<CharacterObserver>(1);
        }
        if (!_observers.contains(observer)) {
            _observers.add(observer);
        }
    }

    /**
     * De-registers interest in changes to this Character.
     * 
     * @param observer
     *            the object no longer interested in receiving notification of
     *            changes.
     */
    public void removeCharacterObserver(CharacterObserver observer) {
        if (_observers == null) {
            return;
        }
        _observers.remove(observer);
    }

    @Override
    public void imageChanged(Image image) {
    	if (_observers == null) {
            return;
        }
        // Notify observers in reverse order to support observer removal during
        // event handling.
        for (int i = _observers.size() - 1; i >= 0; i--) {
            _observers.get(i).imageChanged(image);
        }
    }
    
    /**
     * Notifies all registered CharacterObservers that this Character has
     * changed.
     */
    protected void notifyObservers() {
        if (_observers == null) {
            return;
        }
        // Notify observers in reverse order to support observer removal during
        // event handling.
        for (int i = _observers.size() - 1; i >= 0; i--) {
            _observers.get(i).characterChanged(this);
        }
    }

    public ControllingInfo checkApplicability(Item item) {
        return _impl.checkApplicability(item);
    }
    
    /**
	 * Items are equal if they have the same item number.
	 */
	public boolean equals(Object character) {
		if ((character == null) || !(character instanceof Character)) {
			return false;
		}		
		Character other = (Character) character;		
		return getCharacterId() == other.getCharacterId() && _characterType == other._characterType;
	}
	
	/**
	 * The character number is unique so makes a decent hashcode.
	 */
	public int hashCode() {
		return getCharacterId();
	}
	
    @Override
    public int compareTo(Character o) {
        return Integer.valueOf(this.getCharacterId()).compareTo(Integer.valueOf(o.getCharacterId()));
    }
}
