package spelling;

import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * An trie data structure that implements the Dictionary and the AutoComplete
 * ADT
 * 
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements Dictionary, AutoComplete {

	private TrieNode root;
	private int size;

	public AutoCompleteDictionaryTrie() {
		root = new TrieNode();
	}

	/**
	 * Insert a word into the trie. For the basic part of the assignment (part
	 * 2), you should ignore the word's case. That is, you should convert the
	 * string to all lower case as you insert it.
	 * Return false if duplicate word is added.
	 */
	public boolean addWord(String word) {
		// TODO: Implement this method.
		TrieNode temp = root.insert(word.toLowerCase().charAt(0));
		if (temp == null)
			temp = root.getChild(word.toLowerCase().charAt(0));
		for (int i = 1; i < word.length(); i++) {
			TrieNode tempCopy = temp;
			temp = temp.insert(word.toLowerCase().charAt(i));
			if (temp == null)
				temp = tempCopy.getChild(word.toLowerCase().charAt(i));
		}
		if (temp.endsWord() == false) {//For not adding duplicate entries
			temp.setEndsWord(true);
			size++;
			return true;
		}
		return false;
	}

	/**
	 * Return the number of words in the dictionary. This is NOT necessarily the
	 * same as the number of TrieNodes in the trie.
	 */
	public int size() {
		// TODO: Implement this method
		return size;
	}

	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) {
		// TODO: Implement this method
		if (s == null || s.length() == 0)
			return false;
		String sCopy = s.toLowerCase();
		TrieNode temp = root.getChild(sCopy.charAt(0));
		if (sCopy.length() == 1 && temp != null && temp.endsWord())
			return true;
		else if (sCopy.length() > 1) {
			for (int i = 1; i < sCopy.length(); i++) {
				if (temp != null)
					temp = temp.getChild(sCopy.charAt(i));
				else
					return false;
			}
			if (temp != null && temp.endsWord())
				return true;
		}
		return false;
	}

	/**
	 * * Returns up to the n "best" predictions, including the word itself, in
	 * terms of length If this string is not in the trie, it returns null. Doing
	 * a breadth first search will result in a list of words of increasing
	 * length. Select list size is more than n, select the last n words else the
	 * entire list.
	 * 
	 * @param text
	 *            The text to use at the word stem
	 * @param n
	 *            The maximum number of predictions desired.
	 * @return A list containing the up to n best predictions
	 */

	@Override
	public List<String> predictCompletions(String prefix, int numCompletions) {
		// TODO: Implement this method
		// This method should implement the following algorithm:
		// 1. Find the stem in the trie. If the stem does not appear in the
		// trie, return an empty list
		// 2. Once the stem is found, perform a breadth first search to generate
		// completions using the following algorithm:
		// Create a queue (LinkedList) and add the node that completes the stem
		// to the back of the list.
		// Create a list of completions to return (initially empty)
		// While the queue is not empty and you don't have enough completions:
		// remove the first Node from the queue
		// If it is a word, add it to the completions list
		// Add all of its child nodes to the back of the queue
		// Return the list of completions
		List<String> completions = new LinkedList<String>();
		TrieNode stem = findStem(prefix);
		if (stem != null)
			completions = getUptoNCompletions(stem, numCompletions);
		return completions;
	}

	private List<String> getUptoNCompletions(TrieNode stem, int numCompletions) {
		List<String> completions = new LinkedList<String>();
		List<TrieNode> list = new LinkedList<TrieNode>();
		list.add(stem);
		while (list.isEmpty() == false) {
			TrieNode temp = list.remove(0);
			if (temp.endsWord()) {
				completions.add(temp.getText());
			}
			for (Character c : temp.getValidNextCharacters()) {
				list.add(temp.getChild(c));
			}
		}
		if (completions.size() <= numCompletions)
			return completions;
		else {
			int count = 0;
			List<String> nCompletions = new LinkedList<String>();
			while (count < numCompletions) {
				nCompletions.add(completions.remove(0));
				count++;
			}
			return nCompletions;
		}
	}

	private TrieNode findStem(String prefix) {
		if (prefix == null || prefix.equals("") || prefix.length() == 0)
			return root;
		String prefixCopy = prefix.toLowerCase();
		TrieNode temp = root;
		for (int i = 0; i < prefixCopy.length(); i++) {
			if (temp != null)
				temp = temp.getChild(prefixCopy.charAt(i));
			else
				return null;
		}
		if (temp != null)
			return temp;
		return null;
	}

	// For debugging
	public void printTree() {
		printNode(root);
	}

	/** Do a pre-order traversal from this node down */
	public void printNode(TrieNode curr) {
		if (curr == null)
			return;

		System.out.println(curr.getText());

		TrieNode next = null;
		for (Character c : curr.getValidNextCharacters()) {
			next = curr.getChild(c);
			printNode(next);
		}
	}

}