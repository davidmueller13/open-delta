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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.ala.delta.model.impl.AttributeData;

/**
 * A MultiStateAttribute represents an attribute value for Characters of type CharacterType.UnorderedMultiState or
 * CharacterType.OrderedMultiState.
 * It contains methods for returning the Character states coded in the Attribute.
 */
public class MultiStateAttribute extends Attribute {
    
    public MultiStateAttribute(MultiStateCharacter character, AttributeData impl) {
        super(character, impl);
    }
    
    @Override
    public MultiStateCharacter getCharacter() {
        return (MultiStateCharacter) super.getCharacter();
    }

    /**
     * Returns true if the supplied state number has been coded in this Attribute.
     * If this Attribute is not coded but the supplied state number represents the associated Characters
     * implicit value then this method returns true.
     * @param stateNumber the state number to check.
     * @return true if the supplied state is coded in this Attribute.
     */
    public boolean isStatePresent(int stateNumber) {
        
        boolean statePresent = _impl.isStatePresent(stateNumber);
        
        if (statePresent == false && isImplicit()) {
            statePresent = (stateNumber == getImplicitValue());
        }
        return statePresent;
    }
    
    /**
     * Only returns true if the supplied state has been coded.
     * This is in contrast to isStatePresent which will return true if the
     * state number is the implicit value and this attribute is uncoded.
     * @param stateNumber the state number to check.
     */
    public boolean isStateCoded(int stateNumber) {
    	return _impl.isStatePresent(stateNumber);
    }
    
    public void setStatePresent(int stateNumber, boolean present) {
        _impl.setStatePresent(stateNumber, present);
        
        notifyObservers();
    }
    
  
    public Set<Integer> getPresentStates() {
    	// We could have returned getPresentStatesAsList as a new
    	// Map, however this would introduce inefficencies in the
    	// SimpleAttributeData implementation which avoids copying 
    	// the Set for efficiency of the BEST algorithm.
    	Set<Integer> presentStates;
        
        if (!_impl.hasValueSet() && isImplicit()) {
            presentStates = new HashSet<Integer>();
            presentStates.add(getImplicitValue());
        } else {
            presentStates = _impl.getPresentStateOrIntegerValues();
        }
        
        return presentStates;
    }
    
    public void setPresentStates(Set<Integer> states) {
        _impl.setPresentStateOrIntegerValues(states);
        notifyObservers();
    }

    /**
     * @return a List of the Character states coded in this Attribute, in the order they were coded.
     */
    public List<Integer> getPresentStatesAsList() {
    	List<Integer> presentStates;
        
        if (!_impl.hasValueSet() && isImplicit()) {
            presentStates = new ArrayList<Integer>();
            presentStates.add(getImplicitValue());
        } else {
            presentStates = _impl.getPresentStatesAsList();
        }
        
        return presentStates;
    }
    
    /**
     * An implicit value is one for which no attribute value is coded but an implicit value
     * has been specified for the attributes character.
     * @return true if the value of this attribute is derived from the Characters implicit value.
     */
    public boolean isImplicit() {
        return (!_impl.hasValueSet() && getCharacter().getUncodedImplicitState() > 0);
    }
    
    /**
     * @return the implicit value of this attribute.
     */
    public int getImplicitValue() {
        if (!isImplicit()) {
            throw new IllegalStateException("Cannot get an implict value on an attribute that is not implicit.");
        }
        return getCharacter().getUncodedImplicitState();
    }

    /**
     * @return true if this attribute has been coded as "V" (variable).
     */
	public boolean isVariable() {
		return _impl.isVariable();
	}
	
	/**
	 * @return true if this encoding for this attribute includes a range of states.
	 */
	public boolean isRangeEncoded() {
		return _impl.isRangeEncoded();
	}

    @Override
    public boolean isUnknown() {
        return _impl.isUnknown() && !isImplicit();
    }
	
    public String toString() {
        return getCharacter().getCharacterId()+ ":" + getPresentStates().toString();
    }

    public int getFirstStateCoded() {
    	List<Integer> states = getPresentStatesAsList();
    	if (states.isEmpty()) {
    		return -1;
    	}
    	else {
    		return states.get(0);
    	}
    	
    }
    
    public int getLastStateCoded() {
    	List<Integer> states = getPresentStatesAsList();
    	if (states.isEmpty()) {
    		return -1;
    	}
    	else {
    		return states.get(states.size()-1);
    	}
    	
    }
}
