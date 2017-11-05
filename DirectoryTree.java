import java.io.*;
import java.util.*;
/**
 * Class to build and print a directory tree
 * @author Nguyen Ngoc Lam
 * Student ID: 16022408
 * Disclaimer: this code is referenced from stackoverflow thread (link: https://stackoverflow.com/questions/10655085/print-directory-tree)
 */
public class DirectoryTree {
    /**
     * Print the directory tree and its file and folder names.
     * @param object user requested object to execute. The object should be a folder.
     * @return string containing tree infomations
     * @throws IllegalArgumentException
     */
    public static String printDirectoryTree(File object) throws IllegalArgumentException {
        if (!object.isDirectory()) {
            throw new IllegalArgumentException("This is not a folder");
        }
        int indent = 0;
        StringBuilder string = new StringBuilder();
        printSubfolders(object, indent, string);
        return string.toString();
    }

    /**
     * Add subfolder's informations to the string.
     * @param object user requested object to execute
     * @param indent indents added for subfolders and files
     * @param string string to add infomations
     */
    private static void printSubfolders(File object, int indent, StringBuilder string) {
        string.append(getIndentString(indent));
        string.append("+--");
        string.append(object.getName());
        string.append("/");
        string.append("\n");
        for (File file : object.listFiles()) {
            if (file.isDirectory()) {
                printSubfolders(file, indent + 1, string);
            } else {
                printFile(file, indent + 1, string);
            }
        }
    }

    /**
     * Add file's informations to the string
     * @param object user requested folder to execute
     * @param indent indents added for subfolders and files
     * @param string string to add infomations
     */
    private static void printFile(File file, int indent, StringBuilder string) {
        string.append(getIndentString(indent));
        string.append("+--");
        string.append(file.getName());
        string.append("\n");
    }

    /**
     * Print a indent for every lines on screen.
     * @param indent indents returned from previous code
     * @return indent print (should look like this --> |   )
     */
    private static String getIndentString(int indent) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            string.append("|  ");
        }
        return string.toString();
    }

    /**
     * Main method to handle user input and everything else.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            try{
                System.out.print("Enter folder directory: ");
                String input = sc.nextLine();
                File current = new java.io.File(input);
                try{
                    System.out.println(printDirectoryTree(current));
                }
                catch(NullPointerException e){
                    System.out.println("Do not have permission to access this folder");
                    System.out.println("Try adding a space after input to overide");
                    System.out.println();
                    continue;
                }
                break;
            } catch (IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }
    }
}
