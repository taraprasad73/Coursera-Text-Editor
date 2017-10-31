package textgen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of the MTG interface that uses a list of lists.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList;
	private boolean trained;
	// The starting "word"
	private String starter;

	// The random number generator
	private Random rnGenerator;
	private int wordSize;

	public MarkovTextGeneratorLoL(Random generator) {
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}

	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText) {
		// TODO: Implement this method

		if (sourceText != "" && sourceText != null) {
			List<String> words = getTokens("[a-zA-Z,.!?]+", sourceText);
			wordSize  = words.size();
			if (words.size() > 0) {
				starter = words.get(0);
				wordList.add(new ListNode(starter));
			}
			if (words.size() > 1) {
				wordList.get(0).addNextWord(words.get(1));
				for (int i = 1; i < words.size(); i++) {
					String currWord = words.get(i);
					ListNode temp = new ListNode(currWord);
					int index = findIndex(temp); 
					if (index >= 0) {
						String nextWord = words.get((i + 1) % words.size());
						wordList.get(index).addNextWord(nextWord);
					} else {
						wordList.add(temp);
						int nextIndex = findIndex(temp);
						String nextWord = words.get((i + 1) % words.size());
						wordList.get(nextIndex).addNextWord(nextWord);
					}
				}
			}
			trained = true;
		}
	}

	private int findIndex(ListNode temp) {
		int nextIndex = -1;
		for (int j = 0; j < wordList.size(); j++) {
			if (wordList.get(j).getWord().equals(temp.getWord())) {
				nextIndex = j;
				break;
			}
		}
		return nextIndex;
	}

	private List<String> getTokens(String pattern, String text) {
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);

		while (m.find()) {
			tokens.add(m.group());
		}
		return tokens;
	}

	/**
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
		// TODO: Implement this method
		if (!trained)
			return "";
		else if(numWords == 0) 
			return "";
		else if(wordSize == 1 && numWords > 1)
			return "";
		else {
			String text = "";
			text += starter;
			String currWord = starter;
			for (int i = 0; i < numWords - 1; i++) {
				int index = 0;
				for (int j = 0; j < wordList.size(); j++) {
					if (wordList.get(j).getWord().equals(currWord)) {
						index = j;
						break;
					}
				}
				ListNode currWordNode = wordList.get(index);
				String nextWord = currWordNode.getRandomNextWord(rnGenerator);
				text += " ";
				text += nextWord;
				currWord = nextWord;
			}
			return text;
		}
	}

	// Can be helpful for debugging
	@Override
	public String toString() {
		String toReturn = "";
		for (ListNode n : wordList) {
			toReturn += n.toString();
		}
		return toReturn;
	}

	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText) {
		// TODO: Implement this method.
		wordList = new LinkedList<ListNode>();
		wordSize = 0;
		trained = false;
		if(sourceText != null && sourceText != ""){
			train(sourceText);
		}
	}

	// TODO: Add any private helper methods you need here.

	/**
	 * This is a minimal set of tests. Note that it can be difficult to test
	 * methods/classes with randomized behavior.
	 * 
	 * @param args
	 */
	/*private static void test() {
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello Hello";
		System.out.println(textString);
		gen.train(textString);
		System.out.println("Printing gen " + gen);
		System.out.println("Generated text = " + gen.generateText(20));
		gen.retrain("");
		System.out.println("Generated text = " + gen.generateText(20));
	}*/

	public static void main(String[] args) {
		// feed the generator a fixed random value for repeatable behavior
		//test();
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, " + "You say stop, and I say go, go, go, "
				+ "Oh no. You say goodbye and I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello. " + "I say high, you say low, "
				+ "You say why, and I say I don't know. " + "Oh no. "
				+ "You say goodbye and I say hello, hello, hello. "
				+ "I don't know why you say goodbye, I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello. " + "Why, why, why, why, why, why, "
				+ "Do you say goodbye. " + "Oh no. " + "You say goodbye and I say hello, hello, hello. "
				+ "I don't know why you say goodbye, I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello. " + "You say yes, I say no, "
				+ "You say stop and I say go, go, go. " + "Oh, oh no. "
				+ "You say goodbye and I say hello, hello, hello. "
				+ "I don't know why you say goodbye, I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello, hello, hello, "
				+ "I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/**
 * Links a word to the next words in the list You should use this class in your
 * implementation.
 */
class ListNode {
	// The word that is linking to the next words
	private String word;

	// The next words that could follow it
	private List<String> nextWords;

	ListNode(String word) {
		this.word = word;
		nextWords = new LinkedList<String>();
	}

	public String getWord() {
		return word;
	}

	public void addNextWord(String nextWord) {
		nextWords.add(nextWord);
	}

	public String getRandomNextWord(Random generator) {
		// TODO: Implement this method
		// The random number generator should be passed from
		// the MarkovTextGeneratorLoL class
		return nextWords.get(generator.nextInt(nextWords.size()));
	}

	public String toString() {
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}

}