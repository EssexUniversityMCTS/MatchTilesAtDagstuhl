package json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stephen Tavener
 * Takes a JSON string, and converts it into Java objects
 */
public class JSONParser {

	/** Opening mark for JSON object */
	private static final char OBJ_OPEN='{';
	/** Closing mark for JSON object */
	private static final char OBJ_CLOSE='}';
	/** Opening mark for JSON array */
	private static final char ARRAY_OPEN='[';
	/** Closing mark for JSON array */
	private static final char ARRAY_CLOSE=']';
	/** Opening mark for JSON string */
	private static final char STRING_OPEN='\"';
	/** Closing mark for JSON string */
	private static final char STRING_CLOSE='\"';
	/** mark between key and value */
	private static final char KV_SEPARATOR=':';
	/** mark between list members in array or object */
	private static final char LIST_SEPARATOR=',';
	/** mark at start of escape sequence */
	private static final char ESCAPE_START='\\';

	/** Padding for pretty formatting */
	private static final String PADDING="\t";
	/** Line end for pretty formatting */
	private static final char LINE_END='\n';

	/** JSON Constant: null */
	private static final String JNULL="null";
	/** JSON Constant: true */
	private static final String JTRUE="true";
	/** JSON Constant: false */
	private static final String JFALSE="false";
	
	/** Source string to be parsed */
	final String _source;
	/** Current character under scrutiny */
	int index;
	
	//final StringBuilder padding = new StringBuilder();
	
	/**
	 * Creates new instance based on String source
	 * @param source
	 */
	public JSONParser(final String source)
	{
		_source = source;
		index = 0;
	}
	
	/**
	 * @param source
	 * @return string turned to JSON object represented as a hashmap
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> stringToMap (final String source)
	{
		final JSONParser parser = new JSONParser(source);
		try {
			final Object nextValue = parser.nextValue();
			if (nextValue instanceof Map<?,?>)
				return (Map<String, Object>) nextValue;
			
			Map<String, Object> container = new HashMap<>();
			container.put(null,nextValue);
			return container;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(source);
		}
		return null;
	}
	
	/**
	 * @param source
	 * @return string turned to JSON object
	 */
	public static Object stringToJSON (final String source)
	{
		final JSONParser parser = new JSONParser(source);
		try {
			return parser.nextValue();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(source);
		}
		return null;
	}

	/**
	 * Next value... grabs a character, calls the relevant function to call it.
	 * Note that there is a slight inconsistency in the JSON format in that many tokens
	 * have an open and close (objects, strings, arrays), while others (numbers, booleans, null)
	 * don't...
	 * @return the next key or value... gets miffed if anything else
	 * @throws JSONParserException 
	 */
	public Object nextValue() throws JSONParserException 
	{
		final int c = skipSpaces();	// next non-space character
		switch (c) 
		{
			case OBJ_OPEN: return object();
			case ARRAY_OPEN: return array();
			case STRING_OPEN: return string();
			
			case 't': // boolean true?
				return rue();
				
			case 'f': // boolean false?
				return alse();
				
			case 'n': // null?
				return ull();

			case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '-': 
				ungetc(); // have read too far - put last character back on stack...
				return number();	// note: no hex or octal, no leading . or +
		}
		throw new JSONParserException("Found unexpected "+
				(c==-1 ? " end of file " : Character.toString((char)c)) +
				" at "+index+". Context: "+context());
	}
	
	/**
	 * Loops through { key:value, ... } and returns them as a Map
	 * @return new hashmap containing key:value pairs
	 * @throws JSONParserException
	 */
	private Map<String,Object> object() throws JSONParserException
	{
		Map<String,Object> result = new HashMap<>();
		//System.out.println(padding+"{Object");
		//padding.append("   ");
		int c = skipSpaces();
		while (c != OBJ_CLOSE) {
			// key (a quoted string)
			if (c != STRING_OPEN) throw new JSONParserException("Expected a key, found "+(char)c+" at "+index+". Context: "+context());
			final String key = string();
			
			// :
			if (!skipChar(KV_SEPARATOR)) throw new JSONParserException("Expected :, found "+lastc()+" at "+index+". Context: "+context());  // :
			
			// value
			final Object value = nextValue();
			result.put(key, value);
			//System.out.println(padding+"\""+key+"\":"+value);
			
			// List _may_ continue (with comma)
			c = skipSpaces();
			if (c==LIST_SEPARATOR) c = skipSpaces();
		}
		//padding.setLength(padding.length()-3);
		//System.out.println(padding+"Object}");

		return result;
	}

	/**
	 * @return new array containing a comma separated list
	 * @throws JSONParserException
	 */
	private List<Object> array() throws JSONParserException
	{
		List<Object> result = new ArrayList<>();
		//System.out.println(padding+"[Array");
		//padding.append("   ");
		int c = skipSpaces();
		while (c != ARRAY_CLOSE) {
			ungetc(); // last character wasn't  space, a close, or a comma... put it back on the stack
			final Object value = nextValue();
			result.add(value);
			//System.out.println(padding+value.toString());
			c = skipSpaces();
			if (c==LIST_SEPARATOR) c = skipSpaces();
		}
		//padding.setLength(padding.length()-3);
		//System.out.println(padding+"Array]");
		return result;		
	}
	/**
	 * Last character was start of string... 
	 * @return sequence of characters up to string close
	 * @throws JSONParserException 
	 */
	private String string() throws JSONParserException
	{
		final StringBuilder stringVal = new StringBuilder();
		int c = getc();
		for (; c != STRING_CLOSE && c!= -1; c = getc()) 
		{
			if (c == ESCAPE_START)
			{
				stringVal.append (interpret());
				c = peek();	// escape will have moved forwards a couple of times and now might be at the end of the file
			}
			else
				stringVal.append (Character.toString((char)c));
		}

		if (c != STRING_CLOSE) throw new JSONParserException("Found unexpected "+(char)c+" at "+index+". Context: "+context());
		
		//System.out.println("String: "+stringVal.toString());
		return stringVal.toString();
	}
	
	/**
	 * @return object containing next number in source
	 * @throws JSONParserException
	 */
	private Number number() throws JSONParserException
	{
		boolean isFP = false;
		boolean isExp = false;
		boolean isExpSignAllowed = false;
		boolean isSignAllowed = true;
		final StringBuilder numStr = new StringBuilder();
		int c = getc();
		do {
			numStr.append((char)c);
			if (c == '.') {
				if (isFP) throw new JSONParserException("Bad number format; Found unexpected "+c+" at "+index+". Context: "+context());
				isFP = true;
			}
			if (c == '-' || c == '+') {
				if (!(isSignAllowed  || isExpSignAllowed)) throw new JSONParserException("Bad number format; Found unexpected "+c+" at "+index+". Context: "+context());
			}
			if (c == 'E' || c == 'e') {
				if (isExp) throw new JSONParserException("Bad number format; Found unexpected "+c+" at "+index+". Context: "+context());
				isExp = true;
				isExpSignAllowed = true;
			} else 
				isExpSignAllowed = false;
			
			isSignAllowed = false;
			c = getc();
			//System.out.print("Next c is "+Character.toString((char)c));
		} while (Character.isDigit(c) || c == 'E' || c == 'e' || c == '.' || c == '-' || c == '+'); // some of these only allowed in exponent

		ungetc();  // last character is not part of the number... put it back!
		
        final String s = numStr.toString();
        try {
        	//System.out.println("Number: "+s);
        	if (isFP || isExp) return Double.valueOf(s);
        	return Long.valueOf(s);
        } catch (final NumberFormatException e) {
        	throw new JSONParserException("Tried to parse "+s+" as number at "+index+". Context: "+context());
        }
	}
	
	/**
	 * @return next character(s) interpreted as an escape sequence
	 * @throws JSONParserException
	 */
	private String interpret() throws JSONParserException
	{
		int c = getc();
		switch (c) {
		case '"': return "\"";		// double quotes
		case '\\': return "\\";		// backslash
		case 'b': return "\b";		// bell
		case 'f': return "\f";		// formfeed
		case 'n': return "\n";		// newline
		case 'r': return "\r";		// linefeed
		case 't': return "\t";		// tab
		case 'u': 					// unicode \\uHHHH (H= hex digit)
			int val = 0;
			for (int i=0; i<4; i++) 
			{
				c = hexDigit();
				if (c<0) throw new JSONParserException("Expected 4 hexadecimal digits after \\u but found "+peek()+" at "+index+". Context: "+context());
				val <<= 4;
				val += c;
			}
			return Character.toString((char)val);
		}
		throw new JSONParserException("Found unexpected escape code "+c+" at "+index+". Context: "+context());
	}

	/**
	 * @param s String to process.
	 * @return s with all dodgy characters replaced.
	 */
	private static final String escape(final String s)
	{
		final StringBuilder sb = new StringBuilder(s.length()*2);

		for (char ch : s.toCharArray())
		{
			if ("\"\\\b\f\n\r\t".indexOf(ch) >= 0)
				sb.append(ESCAPE_START);
			
			sb.append(ch);
		}
		return sb.toString();
		
	}
	
	/**
	 * @return next character interpreted as a hexadecimal digit
	 */
	private int hexDigit()
	{
		int c = getc();
		if (c >= '0' && c <= '9') return c-'0';
		if (c >= 'A' && c <= 'F') return c-'A'+10;
		if (c >= 'a' && c <= 'f') return c-'a'+10;
		return -1;
	}
	
	/**
	 * @return context of error (20 chars either side) for error reporting
	 */
	private String context()
	{
		final StringBuilder sb = new StringBuilder();
		int start = index-20;
		if (start <= 0) 
			start = 0;
		else
			sb.append("...");

		if (index >= _source.length())
		{
			sb.append(_source.substring(start));
			return sb.toString();
		}
		sb.append(_source.substring(start,index));
		sb.append("###");
		sb.append(_source.charAt(index));
		if (index+1 >= _source.length())
		{
			sb.append("EOF");
			return sb.toString();
		}

		int end = index+20;
		if (end >= _source.length()) 
		{
			sb.append(_source.substring(index+1));
			sb.append("|EOF");
			return sb.toString();
		}
		
		sb.append(_source.substring(index+1, end));
		sb.append("...");
		
		return sb.toString();
	}

	/**
	 * @param valid
	 * @return skips specified characters, but only if they match the expected values
	 */
	private boolean skipWithValidation(final String valid)
	{
		for (int i=0; i<valid.length(); i++)
		{
			if (getc() != valid.charAt(i)) return false;
		}
		return true;
	}
	
	/**
	 * @return TRUE if that's what's in the source file
	 * @throws JSONParserException if anything unexpected
	 */
	private Boolean rue() throws JSONParserException
	{
		if (!skipWithValidation("rue")) throw new JSONParserException("Expected true, found something else. Context: "+context());
		//System.out.println("Boolean: true");
		return Boolean.TRUE;
	}
	
	/**
	 * @return TRUE if that's what's in the source file
	 * @throws JSONParserException if anything unexpected
	 */
	private Boolean alse() throws JSONParserException
	{
		if (!skipWithValidation("alse")) throw new JSONParserException("Expected false, found something else. Context: "+context());
		//System.out.println("Boolean: false");
		return Boolean.FALSE;
	}
	
	/**
	 * @return null if that's what's in the source file
	 * @throws JSONParserException if anything unexpected
	 */
	private Object ull() throws JSONParserException
	{
		if (!skipWithValidation("ull")) throw new JSONParserException("Expected null, found something else. Context: "+context());
		//System.out.println("null");
		return null;
	}

	
	/**
	 * Reads one character, advances index to next character
	 * @return next character, or -1 at end of buffer
	 */
	private int getc()
	{
		if (index < _source.length()) return _source.charAt(index++);
		return -1;
	}

	/**
	 * @return Previous character as string, for debugging
	 */
	private String lastc()
	{
		if (index > 0) return Character.toString(_source.charAt(index-1));
		return "[BOS]";
	}

	/**
	 * Moves index back to previous character
	 */
	private void ungetc()
	{
		if (index > 0) index--;
	}
	
	/**
	 * Reads one character, but does not advance index
	 * @return next character
	 */
	private int peek()
	{
		if (index < _source.length()) return _source.charAt(index);
		return -1;
	}

	/**
	 * @return first non-space character after the current one
	 */
	private int skipSpaces() {
		//System.out.println("SkipSpaces: "+context());
		int c = getc();
		while (Character.isWhitespace(c)) c = getc();
		return c;
	}

	/**
	 * @param valid
	 * @return skips specified characters, but only if they match the expected values
	 */
	private boolean skipChar(final int valid)
	{
		int c = skipSpaces();
		return c == valid;
	}
	
	/**
	 * @param o object to be converted
	 * @return o in JSON string representation
	 * @throws JSONParserException 
	 */
	@SuppressWarnings("unchecked")
	public static final String toJSONString (final Object o) throws JSONParserException
	{
		if (o == null)
		{
			return "null";
		}
		else if (o instanceof Map<?, ?>) 
		{
			return objectString((Map<String,Object>)o);
		} 
		else if (o instanceof List<?>)
		{
			return arrayString((List<Object>)o);	
		} 
		else if (o instanceof String)
		{
			return STRING_OPEN+(String)o+STRING_CLOSE;
		} 
		else if (o instanceof Boolean)
		{
			return ((Boolean)o).toString();
		} 
		else if (o instanceof Number)
		{
			return ((Number)o).toString();
		}
		throw new JSONParserException("Can't represent java type "+o.getClass().getName()+" as JSON string");
	}
	
	/**
	 * @param list
	 * @return string representation of list
	 * @throws JSONParserException 
	 */
	private static final String arrayString(final List<Object> list) throws JSONParserException
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(ARRAY_OPEN);
		
		for (int i=0; i < list.size(); i++) 
		{
			if (i > 0) sb.append(LIST_SEPARATOR);
			sb.append(toJSONString(list.get(i)));
		}
		sb.append(ARRAY_CLOSE);
		return sb.toString();
	}
	
	/**
	 * @param map 
	 * @return string representation of list
	 * @throws JSONParserException 
	 */
	private static final String objectString(final Map<String, Object> map) throws JSONParserException
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(OBJ_OPEN);
		
		int index = 0;
		for (final String key : map.keySet()) 
		{
			if (index++ > 0) sb.append(LIST_SEPARATOR);
			sb.append(STRING_OPEN);
			sb.append(key);
			sb.append(STRING_CLOSE);
			sb.append(KV_SEPARATOR);
			sb.append(toJSONString(map.get(key)));
		}
		sb.append(OBJ_CLOSE);
		
		return sb.toString();
	}	
	
	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @return the object with the given name or null if not present
	 */
	@SuppressWarnings("unchecked")
	public static final Object searchValueForKey (final Object json, final String key)
	{
		if (json == null)
			return null;
		else if (json instanceof Map<?, ?>) 
		{
			// Is the named item in this map?
			final Map<String, Object> m = (Map<String, Object>)json;
			if (m.containsKey(key)) return m.get(key);
			
			// No? Time to get recursive
			for (final Object o : m.values()) 
			{
				final Object test = searchValueForKey(o, key);
				if (test != null) return test;
			}
		} 
		else if (json instanceof List<?>)
		{
			// Array... recurse through children to see if we have a match
			final List<Object> l = (List<Object>)json;
			for (final Object o : l)
			{
				final Object test = searchValueForKey(o, key);
				if (test != null) return test;
			}
		} 

		// Not a map, not a list. Nothing else has a named value at any level.
		return null;
	}
	
	/**
	 * Returns a JSON object for the specified key, or null
	 * @param json
	 * @param key
	 * @return the object with the given name or null if not present
	 */
	@SuppressWarnings("unchecked")
	public static final Object valueForKey (final Object json, final String key)
	{
		if (json != null && json instanceof Map<?, ?>) 
			return ((Map<String, Object>)json).get(key);
			
		// Not a map. Nothing else has a named value at any level.
		return null;
	}
	
	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return the string with the given name  or throws an exception
	 */
	public static final String findString (final Object json, final String key, final Map<String, Object> defaults)
	{
		final Object test = searchValueForKey(json, key);
		if (test instanceof String) return (String)test;
		return (String)defaults.get(key);
	}
	
	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return the long with the given name or throws an exception
	 */
	public static final long findLong (final Object json, final String key, final Map<String, Object> defaults)
	{
		final Object test = searchValueForKey(json, key);
		if (test instanceof Number) return ((Number)test).longValue();
		return ((Number)defaults.get(key)).longValue();
	}

	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return the int with the given name or throws an exception
	 */
	public static final int findInt (final Object json, final String key, final Map<String, Object> defaults)
	{
		final Object test = searchValueForKey(json, key);
		if (test instanceof Number) return ((Number)test).intValue();
		return ((Number)defaults.get(key)).intValue();
	}

	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return the double with the given name or throws an exception
	 */
	public static final double findDouble (final Object json, final String key, final Map<String, Object> defaults)
	{
		final Object test = searchValueForKey(json, key);
		if (test instanceof Number) return ((Number)test).doubleValue();
		return ((Number)defaults.get(key)).doubleValue();
	}
	
	/**
	 * Recursively searches a JSON object for the specified value
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return the boolean with the given name or throws an exception
	 */
	public static final boolean findBoolean (final Object json, final String key, final Map<String, Object> defaults)
	{
		final Object test = searchValueForKey(json, key);
		if (test instanceof Boolean) return ((Boolean)test).booleanValue();
		return ((Boolean)defaults.get(key)).booleanValue();
	}	
	
	/**
	 * Recursively searches a JSON object for the specified value
	 * @param <T>
	 * @param json
	 * @param key
	 * @param defaults 
	 * @return list of objects; single items will be converted if necessary
	 */
	@SuppressWarnings("unchecked")
	public static final <T> List<T> findList (final Object json, final String key, final Map<String, Object> defaults)
	{
		Object test = searchValueForKey(json, key);
		if (test == null) test = defaults.get(key);
		
		if (test instanceof List) return (List<T>)test;
		
		final List<T> result = new ArrayList<>();
		result.add((T)test);
		return result;
	}	
	
	/**
	 * Returns object at end of specified path, or null if not present
	 * @param json
	 * @param path '.' separated path to object
	 * @return json object specified by path
	 */
	public static final Object objectFromPath(final Object json, final String path)
	{
		// Sanity clause
		if (json == null) return null;
		
		// Split path nto ordered list of keys
		final String[] keys = path.split("\\.");
		
		// Dig deep
		Object result = json;
		for (final String key : keys)
		{
			result = valueForKey(result,key);
			if (result == null) return null;
		}
		
		//Success!
		System.out.println("Successfully found "+path+"="+result);
		return result;
	}
	
	/**
	 * Returns object at end of specified path, or null if not present
	 * @param json
	 * @param path '.' separated path to object
	 * @param defaults 
	 * @return json object specified by path
	 */
	public static final Object objectFromPath(final Object json, final String path, final Map<String,Object> defaults)
	{
		if (!defaults.containsKey(path))
			throw new UnsupportedOperationException("Defaults must be present for all objects");

		// The following is a sanity check - defaults must be present for all objects
		final Object def = defaults.get(path);
		
		// TODO: Verify default exists.
		final Object test = objectFromPath(json, path);
		if (test != null) return test;
		
		return def;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return "key":"value"
	 */
	public static final String stringKV (final String key, final String value) { return "\""+key+"\":\""+value+"\""; }
	
	/**
	 * @param key
	 * @param value
	 * @return "key":value
	 */
	public static final String stringKV (final String key, final Boolean value) { return "\""+key+"\":"+value; }
	
	/**
	 * @param key
	 * @param value
	 * @return "key":value
	 */
	public static final String stringKV (final String key, final Number value) { return "\""+key+"\":"+value; }
	
	/**
	 * @param pairs
	 * @return { pair, pair, ... pair }
	 */
	public static final String stringObject (final String... pairs) 
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(OBJ_OPEN);
		for (int i=0; i<pairs.length; i++)
		{
			if (i>0) sb.append(LIST_SEPARATOR);
			sb.append(pairs[i]);
		}
		sb.append(OBJ_CLOSE);
		return sb.toString(); 
	}
	
	/**
	 * @param elements
	 * @return [ element, element, ... element ]
	 */
	public static final String stringArray (final String... elements) 
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(ARRAY_OPEN);
		for (int i=0; i<elements.length; i++)
		{
			if (i>0) sb.append(LIST_SEPARATOR);
			sb.append(elements[i]);
		}
		sb.append(ARRAY_CLOSE);
		return sb.toString(); 
	}

	//----------------------------------------------------------------------
	// Object --> String
	//----------------------------------------------------------------------
	
	/**
	 * Recursive function which converts a JSON object into a string representation
	 * @param pad PADDING for pretty printing.
	 * @param o
	 * @return JSON string representation of object
	 */
	public static final String objectToString (final String pad, final Object o) 
	{
		if (o==null) return JNULL;

		final StringBuilder sb = new StringBuilder();
		if (o instanceof Map<?,?>)
		{
			sb.append(LINE_END);
			sb.append(pad);
			sb.append(OBJ_OPEN);

			boolean isFirst = true;
			for (final Object key : ((Map<?,?>)o).keySet())
			{
				if (!isFirst) sb.append(LIST_SEPARATOR);
				
				sb.append(LINE_END);
				sb.append(pad);
				sb.append(PADDING);
				
				sb.append(objectToString(key));
				sb.append(KV_SEPARATOR);
				sb.append(objectToString(pad+PADDING, ((Map<?,?>)o).get(key)));
				
				isFirst = false;
			}

			sb.append(LINE_END);

			sb.append(pad);
			sb.append(OBJ_CLOSE);
		}
		else if (o instanceof List<?>)
		{
			sb.append(LINE_END);
			sb.append(pad);
			sb.append(ARRAY_OPEN);

			for (int i=0; i<((List<?>)o).size(); i++)
			{
				if (i>0) sb.append(LIST_SEPARATOR);
				sb.append(LINE_END);
				sb.append(pad);				
				sb.append(objectToString(pad+PADDING, ((List<?>)o).get(i)));
			}

			sb.append(LINE_END);

			sb.append(pad);
			sb.append(ARRAY_CLOSE);			
		}
		else if (o instanceof String)
		{
			// TODO: escape sequences
			sb.append(STRING_OPEN);
			sb.append(escape((String)o));
			sb.append(STRING_CLOSE);
		}
		else if (o instanceof Number)
		{
			sb.append(o);
		}
		else if (o instanceof Boolean)
		{
			sb.append (((Boolean)o).booleanValue() ? JTRUE : JFALSE);
		}
			
		return sb.toString(); 
	}

	/**
	 * Recursive function which converts a JSON object into a string representation
	 * @param o
	 * @return JSON string representation of object
	 */
	public static final String objectToString (final Object o) 
	{
		return objectToString ("", o);
	}
	
	/**
	 * Recursive function which converts a JSON object into a string representation.
	 * Includes type information, unlike @see objectToString
	 * @param o
	 * @return JSON string representation of object
	 */
	public static final String objectDetail (final Object o) 
	{
		if (o==null) return JNULL;

		final StringBuilder sb = new StringBuilder();
		if (o instanceof Map<?,?>)
		{
			sb.append(OBJ_OPEN);
			boolean isFirst = true;
			for (final Object key : ((Map<?,?>)o).keySet())
			{
				if (!isFirst) sb.append(LIST_SEPARATOR);
				
				// Each entry in map comes back as list, including key, value, and type
				sb.append(OBJ_OPEN);
				
				sb.append(stringKV("name",(String)key));
				sb.append(LIST_SEPARATOR);
				final Object value = ((Map<?,?>)o).get(key);
				sb.append(stringKV("type",value==null ? "undefined" : value.getClass().getCanonicalName()));
				sb.append(LIST_SEPARATOR);
				
				sb.append(STRING_OPEN);
				sb.append("default");
				sb.append(STRING_CLOSE);
				sb.append(KV_SEPARATOR);
				sb.append(objectDetail(value));
				
				isFirst = false;
			}
			sb.append(OBJ_CLOSE);
		}
		else if (o instanceof List<?>)
		{
			sb.append(ARRAY_OPEN);
			for (int i=0; i<((List<?>)o).size(); i++)
			{
				if (i>0) sb.append(LIST_SEPARATOR);
				sb.append(objectDetail(((List<?>)o).get(i)));
			}
			sb.append(ARRAY_CLOSE);			
		}
		else if (o instanceof String)
		{
			sb.append(STRING_OPEN);
			sb.append(o);
			sb.append(STRING_CLOSE);
		}
		else if (o instanceof Number)
		{
			sb.append(o.toString());
		}
		else if (o instanceof Boolean)
		{
			sb.append (((Boolean)o).booleanValue() ? JTRUE : JFALSE);
		}
			
		return sb.toString(); 
	}
	
//	/**
//	 * Takes a JSON object, and recursively collapses it down to a flat set of {key, value} sets
//	 * Deep keys will be separated by '.'
//	 * Arrays will be denoted by [n]
//	 * & [ and . in the key name will be escaped  
//	 * @param source
//	 * @return
//	 */
//	public static final Map<String,Object> collapse (final Map<String, Object> source)
//	{
//		final Map<String,Object> results = new HashMap<String,Object>();
//		
//		for ()
//		return results;
//	}
//	
//	private static final String[][] substitutions = {
//		
//	};
//	
//	private static String makeKeySafe (final String key) {
//		
//	}
//	
//	private static void collapseRecurse (final String key, final Map<String, Object> destination, final Map<String, Object> source)
//	{
//		
//	}
}