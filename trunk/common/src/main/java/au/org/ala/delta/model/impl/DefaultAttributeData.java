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
package au.org.ala.delta.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.FloatRange;
import org.apache.commons.lang.math.NumberRange;

import au.org.ala.delta.directives.validation.DirectiveException;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.NumericRange;
import au.org.ala.delta.model.attribute.AttrChunk;
import au.org.ala.delta.model.attribute.ChunkType;
import au.org.ala.delta.model.attribute.DefaultParsedAttribute;
import au.org.ala.delta.model.attribute.ParsedAttribute;


/**
 * A simple implementation of AttributeData that stores attribute data
 * in-memory. The attribute value can only be set from a string representation.
 */
public class DefaultAttributeData implements AttributeData {

    private String _value;
    private DefaultParsedAttribute _parsedAttribute;
    private Character _character;
    
    public DefaultAttributeData(Character character) {
    	_character = character;
    	_parsedAttribute = new DefaultParsedAttribute(_character);
    }
    
    @Override
    public String getValueAsString() {
        return _value;
    }

    @Override
    public void setValueFromString(String value) throws DirectiveException {
        _value = value;
        _parsedAttribute.parse(value, false);
    }

    @Override
    public boolean isStatePresent(int stateNumber) {
        return _parsedAttribute.encodesState(stateNumber, true);
    }

    public void setStatePresent(int stateNumber, boolean present) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public boolean isUnknown() {
    	if (StringUtils.isEmpty(_value)) {
    		return true;
    	}
    	return _parsedAttribute.isUnknown();
    }
    
    @Override 
    public boolean isCodedUnknown() {
    	return _parsedAttribute.isUnknown();
    }

    @Override
    public boolean isInapplicable() {
        return _parsedAttribute.isInapplicable();
    }

    @Override
    public boolean isExclusivelyInapplicable(boolean ignoreComments) {
        return _parsedAttribute.isExclusivelyInapplicable(ignoreComments);
    }


    @Override
    public FloatRange getRealRange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRealRange(FloatRange range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getPresentStateOrIntegerValues() {
  
    	return new HashSet<Integer>(getPresentStatesAsList());
    }

    @Override
    public List<Integer> getPresentStatesAsList() {
    	List<Integer> states = new ArrayList<Integer>();
    	_parsedAttribute.getEncodedStates(states, new short[1]);
    	return states;
    }
    
    @Override
    public void setPresentStateOrIntegerValues(Set<Integer> values) {
        throw new UnsupportedOperationException();
    }

	@Override
	public boolean isVariable() {
		for (AttrChunk chunk : _parsedAttribute) {
			if (chunk.getType() == ChunkType.CHUNK_VARIABLE) {
				return true;
			}
		}
		return false;
	}

    @Override
    public boolean hasValueSet() {
        return !StringUtils.isEmpty(_value);
    }
	
	@Override
	public boolean isRangeEncoded() {
		return (_value != null) && (_value.indexOf("-") >= 0);
	}

	@Override
	public boolean isCommentOnly() {
		for (AttrChunk chunk : _parsedAttribute) {
        	if ((chunk.getType() != ChunkType.CHUNK_TEXT) &&
        	    (chunk.getType() != ChunkType.CHUNK_LONGTEXT) &&
        	    (chunk.getType() != ChunkType.CHUNK_STOP)) {
        		return false;
        	}
         }
		return true;
	}

	@Override
	public List<NumericRange> getNumericValue() {
		List<NumericRange> ranges = new ArrayList<NumericRange>();
		NumericRange range = new NumericRange();
		List<Number> numbers = new ArrayList<Number>();
		for (AttrChunk chunk : _parsedAttribute) {
			switch (chunk.getType()) {
			case ChunkType.CHUNK_AND:
			case ChunkType.CHUNK_OR:
				addNumericRange(ranges, range, numbers);
				range = new NumericRange();
				numbers = new ArrayList<Number>();
				break;
			case ChunkType.CHUNK_EXLO_NUMBER:
				range.setExtremeLow(chunk.getNumber());
				break;
			case ChunkType.CHUNK_EXHI_NUMBER:
				range.setExtremeHigh(chunk.getNumber());
				break;
			case ChunkType.CHUNK_NUMBER:
				numbers.add(chunk.getNumber());
				break;
			}
		}
		addNumericRange(ranges, range, numbers);
		
		return ranges;
	}

	private void addNumericRange(List<NumericRange> ranges, NumericRange range, List<Number> numbers) {
		if (!range.hasExtremeHigh() && !range.hasExtremeLow() && numbers.isEmpty()) {
			return;
		}
		
		if (numbers.size() == 1) {
			range.setRange(new NumberRange(numbers.get(0)));
		}
		else if (numbers.size() == 2) {
			range.setRange(new NumberRange(numbers.get(0), numbers.get(1)));
		}
		else if (numbers.size() == 3) {
			range.setRange(new NumberRange(numbers.get(0), numbers.get(2)));
			range.setMiddle(numbers.get(1));
		}
		ranges.add(range);
	}

	@Override
	public ParsedAttribute parsedAttribute() {
		return _parsedAttribute;
	}	
	
	
	
}
