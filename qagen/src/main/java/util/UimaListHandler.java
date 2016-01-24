package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		StringList iter = list;
		
		while (iter instanceof NonEmptyStringList) {
			sb.append(((NonEmptyStringList) iter).getHead());
			iter = ((NonEmptyStringList) iter).getTail();
			
			if (iter instanceof NonEmptyStringList) {
				sb.append(", ");
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
		IntegerList iter = list;
		
		while (iter instanceof NonEmptyIntegerList) {
			sb.append(((NonEmptyIntegerList) iter).getHead());
			iter = ((NonEmptyIntegerList) iter).getTail();
			
			if (iter instanceof NonEmptyIntegerList) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates a non empty string list from a collection of strings.
	 * See http://massapi.com/class/org/apache/uima/jcas/cas/NonEmptyStringList.html
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
	 * See http://massapi.com/class/org/apache/uima/jcas/cas/NonEmptyStringList.html
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
	
	/**
	 * Convert a UIMA non empty list to a java list.
	 * @param list A UIMA list.
	 * @return A Java list.
	 */
	public static List<String> listToJavaStringList(NonEmptyStringList list) {
		LinkedList<String> jList = new LinkedList<String>();
		StringList iter = list;
		
		while (iter instanceof NonEmptyStringList) {
			jList.add(((NonEmptyStringList) iter).getHead());
			iter = ((NonEmptyStringList) iter).getTail();
		}
		
		return jList;
	}
	
	/**
	 * Convert a UIMA non empty list to a java list.
	 * @param list A UIMA list.
	 * @return A Java list.
	 */
	public static List<Integer> listToJavaIntegerList(NonEmptyIntegerList list) {
		LinkedList<Integer> jList = new LinkedList<Integer>();
		IntegerList iter = list;
		
		while (iter instanceof NonEmptyIntegerList) {
			jList.add(((NonEmptyIntegerList) iter).getHead());
			iter = ((NonEmptyIntegerList) iter).getTail();
		}
		
		return jList;
	}
}
