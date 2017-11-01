import java.io.*;

/**
 * Represents tree directory building class
 *
 * @author Manh Nguyen
 */
public class TreeDirectory {

	/**
	 * Main function
	 *
	 * @param args arguments from command line
	 */
	public static void main(String[] args) {

		File current = new java.io.File("/Users/dhungc3/Desktop/Screenshots");
		Tree<File> root = new Tree(current);
		root.printChildren();
		System.out.println(root.file_num);
	}

}
