package au.org.ala.delta.directives;

import java.io.File;
import java.util.Stack;

public abstract class AbstractDeltaContext {
    
    protected Stack<ParsingContext> _parsingContexts = new Stack<ParsingContext>();
    
    public ParsingContext getCurrentParsingContext() {
        if (_parsingContexts.size() > 0) {
            return _parsingContexts.peek();
        }
        return null;
    }

    public ParsingContext newParsingContext() {
        ParsingContext context = new ParsingContext();
        _parsingContexts.push(context);
        return context;
    }

    public ParsingContext endCurrentParsingContext() {
        if (_parsingContexts.size() > 0) {
            return _parsingContexts.pop();
        }
        return null;
    }

}
