package json;

/**
 * @author Stephen Tavener
 * Validation exception for unexpected characters in JSON file
 */
public class JSONParserException extends Exception {
	/** Serial number required by class for some reason */
	private static final long serialVersionUID = -1904403823446815653L;

	/**
	 * Encapsulates message and lots of lovely stack trace goodness
	 * @param message
	 */
	public JSONParserException(final String message) { super(message);}
}
