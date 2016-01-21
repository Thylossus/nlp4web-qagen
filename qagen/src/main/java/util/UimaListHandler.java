package util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.EmptyIntegerList;
import org.apache.uima.jcas.cas.EmptyStringList;
import org.apache.uima.jcas.cas.IntegerList;
import org.apache.uima.jcas.cas.NonEmptyIntegerList;
import org.apache.uima.jcas.cas.NonEmptyStringList;
import org.apache.uima.jcas.cas.StringList;

public class UimaListHandler {

	/**
	 * Write a non empty string list to a string for debugging purposes.
	 * @param list The non empty string list.
	 * @return A string representation of the list.
	 */
	public static String listToString(NonEmptyStringList list) {
		StringBuilder sb = new StringBuilder();
		
		String head = list.getHead();
		StringList tail = list.getTail();
		
		while (head != null) {
			sb.append(head);
			
			if (tail instanceof NonEmptyStringList) {
				tail = ((NonEmptyStringList) tail).getTail();
				
				if (tail instanceof NonEmptyStringList) {
					sb.append(", ");
					head = ((NonEmptyStringList) tail).getHead();
				} else {
					head = null;
				}
			} else {
				head = null;
			}
			
		}
		
		return sb.toString();
	}
	
	/**
	 * Write a non empty integer list to a string for debugging purposes.
	 * @param list The non empty integer list.
	 * @return A string representation of the list.
	 */
	public static String listToString(NonEmptyIntegerList list) {
		StringBuilder sb = new StringBuilder();
		
		Integer head = list.getHead();
		IntegerList tail = list.getTail();
		
		while (head != null) {
			sb.append(head);
			
			if (tail instanceof NonEmptyIntegerList) {
				tail = ((NonEmptyIntegerList) tail).getTail();
				
				if (tail instanceof NonEmptyIntegerList) {
					sb.append(", ");
					head = ((NonEmptyIntegerList) tail).getHead();
				} else {
					head = null;
				}
			} else {
				head = null;
			}
			
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates a non empty string list from a collection of strings.
	 * See http://www.programcreek.com/java-api-examples/index.php?api=org.apache.uima.jcas.cas.NonEmptyStringList.
	 * 
	 * The provided collection must not be empty.
	 * 
	 * @param jcas Reference to the CAS.
	 * @param strings The collection of strings.
	 * @return A non empty string list.
	 */
	public static NonEmptyStringList stringCollectionToList(JCas jcas, Collection<String> strings) {
		if (strings.size() == 0) {
			throw new IllegalArgumentException("The provided collection must not be empty.");
		}
		
		NonEmptyStringList head = new NonEmptyStringList(jcas);
		NonEmptyStringList list = head;
		
		Iterator<String> iter = strings.iterator();
		
		while (iter.hasNext()) {
			head.setHead(iter.next());
			
			if (iter.hasNext()) {
				head.setTail(new NonEmptyStringList(jcas));
				head = (NonEmptyStringList) head.getTail();
			} else {
				head.setTail(new EmptyStringList(jcas));
			}
		}
		
		return list;
	}
	
	/**
	 * Creates a non empty integer list from a collection of integers.
	 * See http://www.programcreek.com/java-api-examples/index.php?api=org.apache.uima.jcas.cas.NonEmptyStringList.
	 * 
	 * The provided collection must not be empty.
	 * 
	 * @param jcas Reference to the CAS.
	 * @param strings The collection of integers.
	 * @return A non empty integer list.
	 */
	public static NonEmptyIntegerList integerCollectionToList(JCas jcas, Collection<Integer> integers) {
		if (integers.size() == 0) {
			throw new IllegalArgumentException("The provided collection must not be empty.");
		}
		
		NonEmptyIntegerList head = new NonEmptyIntegerList(jcas);
		NonEmptyIntegerList list = head;
		
		Iterator<Integer> iter = integers.iterator();
		
		while (iter.hasNext()) {
			head.setHead(iter.next());
			
			if (iter.hasNext()) {
				head.setTail(new NonEmptyIntegerList(jcas));
				head = (NonEmptyIntegerList) head.getTail();
			} else {
				head.setTail(new EmptyIntegerList(jcas));
			}
		}
		
		return list;
	}
	
}
