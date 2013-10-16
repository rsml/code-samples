import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Class: Tree2345
 *
 * Created by Ross Miller on 4/20/10
 * 
 * This class parses a text file read in as a command line argument and produces a concordence
 * 
 * VARIABLES
 * ---------
 * private static File file
 * 		A file object for the filename given on the command line
 * 
 * 
 * METHODS
 * -------
 * public static void main(String args[]);
 * 		Reads in a filename from command line and calls for the file to be parsed
 * 
 * private static void parseFile()
 * 		Reads all the input from a file, extracts the words, stores them in a (2,5)-tree along with their paragraph
 * 		and line number. Then it prints out the tree.
 * 
 */
public class Concordance {
	private static File file;

    /**
     * Reads in a filename from command line and calls for the file to be parsed
     * Preconditions: The argument on the command line is a valid file path
     * @param args[] Command line arguments; The first one should be a valid file path
     */
	public static void main(String args[]) {
		file = new File(args[0]);
		parseFile();
	}

    /**
     * Reads all the input from a file, extracts the words, stores them in a (2,5)-tree along with their paragraph
     * 		and line number. Then it prints out the tree
     * Preconditions: The private static variable, 'file' is a valid File object
     * Postconditions: The contents of the tree have been printed out to stdout
     */
	private static void parseFile() {
		Tree2345 tree = new Tree2345();

		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		LinkedList<LinkedList<String>> allParagraphs = new LinkedList<LinkedList<String>>();
		LinkedList<String> paragraph = new LinkedList<String>();

		// First go through and seperate out paragraphs
		while (scanner.hasNext()) {
			String nextLine = scanner.nextLine();
			if (nextLine.equals("")) {
				allParagraphs.add(paragraph);
				paragraph = new LinkedList<String>();
			} else {
				paragraph.add(nextLine);
			}
			if (!scanner.hasNext()) {
				allParagraphs.add(paragraph);
			}
		}

		// Remove empty paragraphs
		LinkedList<LinkedList<String>> cleanedParagraphs = new LinkedList<LinkedList<String>>();
		for (int i = 0; i < allParagraphs.size(); i++) {
			if (allParagraphs.get(i).size() > 0) {
				cleanedParagraphs.add(allParagraphs.get(i));
			}
		}

		// Iterate through paragraphs
		for (int i = 0; i < cleanedParagraphs.size(); i++) {
			paragraph = cleanedParagraphs.get(i);

			// Iterate through lines
			for (int j = 0; j < paragraph.size(); j++) {
				String line = paragraph.get(j).toLowerCase();

				// Starts with a letter or number and may may letters, numbers,
				// hyphens, or apostrophes afterwards
				Pattern p = Pattern.compile("[a-zA-z0-9][a-zA-Z0-9-']*");
				Matcher m = p.matcher(line);
				while (m.find()) {
					int start = m.start();
					int end = m.end();
					tree.add(line.substring(start, end), i + 1, j + 1);
				}
			}
		}

		//Output everything
		System.out.println(tree);

	}
}
