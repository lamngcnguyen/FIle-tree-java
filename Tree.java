import java.util.*;
import java.io.*;

/**
 * Represent tree class of File
 * @author Manh Nguyen
 */
public class Tree<T> {

	String name; // name of object
	File data; // data of object
	ArrayList<Tree<T>> children; // children list of object
	static int file_num;

	/**
	 * Default constructor
	 */
	public Tree() {
		children = new ArrayList<>();
	}

	/**
	 * Specify constructor with param
	 *
	 * @param object object put to tree
	 */
	public Tree(File object) {
		name = object.getName();
		data = object;
		children = new ArrayList<>();
		addChildren();
	}

	/**
	 * Add children of current object
	 */
	public void addChildren() {
		File folder[] = data.listFiles();
		if (folder == null) return;
		file_num += folder.length;
		for (int i = 0; i < folder.length; i++) {
			Tree<T> leaf = new Tree(folder[i]);
			children.add(leaf);
		}
	}

	/**
	 * Print name of all children of children of children. Or print all of leaves
	 */
	public void printChildren() {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).children == null) {
				continue;
			}
			if (children.get(i).children.size() != 0) {
				System.out.print(children.get(i).name + ": ");
				children.get(i).printChildren();
			} else {
				System.out.print(children.get(i).name + " ");
			}
			System.out.println();
		}
	}

}
