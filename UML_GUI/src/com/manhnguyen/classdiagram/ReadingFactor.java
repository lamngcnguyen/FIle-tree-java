package com.manhnguyen.classdiagram;

import java.util.*;
import java.io.*;

/**
 * File utils represents class
 *
 * @author Manh Nguyen
 */
public class ReadingFactor {

    private static final String statementKeywords[] = {"assert", "break", "switch", "case", "if", "else", "while", "catch",
        "for", "try", "->", ","};
    private static final String permission[] = {"public", "private", "default", "protected", "final", "static"};
    private static final String classRelationShip[] = {"extends", "implements"};

    /**
     * Read data from a java file
     *
     * @param file source file
     * @return source code of this file
     */
    public ArrayList<String> getSource(File file) {
        ArrayList<String> result = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br;
            br = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                result.add(currentLine);
            }
            removeEmptyLines(result);
            removeIndentation(result);
            removePackageAndImport(result);
            removeComment(result);
        } catch (IOException e) {

        }
        return result;
    }

    /**
     * Remake visibility of members in UML
     *
     * @param list list of members
     */
    public void remakeVisibilityMember(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            boolean isFunc = false;
            String param = null;
            if (line.contains("(")) {
                String temp = line;
                line = line.substring(0, line.indexOf("("));
                isFunc = true;
                param = temp.substring(temp.indexOf("("), temp.length());
            }
            String lineSplit[] = splitString(line);
            boolean isPermissionDeclared = false;
            if (isStringInList(permission, lineSplit[0])) {
                String word = null;
                switch (lineSplit[0]) {
                    case "public":
                        word = "+ ";
                        break;
                    case "private":
                        word = "- ";
                        break;
                    case "protected":
                        word = "# ";
                        break;
                }
                String newLine = word;
                for (int j = 1; j < lineSplit.length; j++) {
                    newLine += lineSplit[j] + " ";
                }
                list.set(i, newLine.substring(0, newLine.length() - 1)); // remove last space char
            } else {
                list.set(i, "+ " + line);
            }
            if (isFunc) {
                list.set(i, list.get(i) + removeParam(param));
            }
            line = list.get(i);
            lineSplit = line.split(" ");
            if (lineSplit.length > 2) {
                int index = 1;
                String type = null;
                if (lineSplit[index].equals("static") || lineSplit[index].equals("final")) {
                    index++;
                }
                type = lineSplit[index];
                String newLine = "";
                for (int j = 0; j < lineSplit.length; j++) {
                    if (j != index) {
                        newLine += lineSplit[j] + " ";
                    }
                }
                newLine += ": " + type;
                list.set(i, newLine);
            } else { // constructor found
                list.remove(i);
                i--;
            }
        }
    }

    /**
     * Remove param input
     *
     * @param source source
     * @return source without param
     */
    public String removeParam(String source) {
        String[] split = splitString(source);
        String result = "(";
        for (int i = 1; i < split.length; i = i + 2) {
            result += split[i] + ", ";
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 2) + ")";
        } else {
            result += ")";
        }
        return result;
    }

    /**
     * Remove all empty lines from source code
     *
     * @param data source code
     */
    public void removeEmptyLines(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isEmpty()) {
                data.remove(i);
                i--;
                continue;
            }
            String line = data.get(i);
            boolean isEmpty = true;
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) != ' ' && line.charAt(j) != '\t') {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                data.remove(i);
                i--;
            }
        }
    }

    public void checkifnothing() {

    }

    /**
     * Delete all comment from source code
     *
     * @param data source code
     */
    public void removeComment(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            if (buildSubString(line, 0, 2).equals("//")) {
                data.remove(i);
                i--;
                continue;
            }
            int endOfCommentLine = 0;
            if (buildSubString(line, 0, 2).equals("/*")) {
                for (int j = 0; j < data.size(); j++) {
                    if (buildSubString(data.get(j), 0, 2).equals("*/")) {
                        endOfCommentLine = j;
                        break;
                    }
                }
            }
            if (endOfCommentLine > i) {
                for (int j = 0; j < endOfCommentLine - i + 1; j++) {
                    data.remove(i);
                }
            }
        }
    }

    /**
     * Build a sub string from a string
     *
     * @param source source string
     * @param start start position
     * @param end end position
     * @return sub string
     */
    public String buildSubString(String source, int start, int end) {
        String result = "";
        int i = start;
        while (i < source.length()) {
            if (i < end) {
                result += source.charAt(i);
                i++;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Remove all package and import line in file
     *
     * @param data source code
     */
    public void removePackageAndImport(ArrayList<String> data) {
        while (true) {
            String line = data.get(0);
            if (buildSubString(line, 0, 7).equals("package")) {
                data.remove(0);
            } else {
                if (buildSubString(line, 0, 6).equals("import")) {
                    data.remove(0);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Remove all indentation from source code
     *
     * @param data source code
     */
    public void removeIndentation(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) != ' ' && line.charAt(j) != '\t') {
                    data.set(i, line.substring(j, line.length()));
                    break;
                }
            }
        }
    }

    /**
     * Check if string is an element in a string list
     *
     * @param list array of string
     * @param source source string
     * @return if string is in list
     */
    public boolean isStringInList(String list[], String source) {
        for (String s : list) {
            if (s.equals(source)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search functions in source file
     *
     * @param data source code
     * @return function list
     */
    public ArrayList<String> searchForFunctionDeclareLine(ArrayList<String> data) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            if (line.charAt(line.length() - 1) == '{' && !line.contains("class")) {
                boolean isFunction = true;
                for (String s : statementKeywords) {
                    if (line.contains(s)) {
                        String lineSplit[] = splitString(line);
                        for (String x : lineSplit) {
                            if (x.equals(s)) {
                                isFunction = false;
                                break;
                            }
                        }
                    }
                }
                if (isFunction) {
                    result.add(line.substring(0, line.length() - 1));
                }
            } else {
                if (line.charAt(0) == '{' && !line.contains("class")) {
                    boolean isFunction = true;
                    String previousLine = data.get(i - 1);
                    for (String s : statementKeywords) {
                        if (previousLine.contains(s)) {
                            String lineSplit[] = splitString(previousLine);
                            for (String x : lineSplit) {
                                if (x.equals(s)) {
                                    isFunction = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (isFunction) {
                        result.add(previousLine);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Remove all commands of functions
     *
     * @param source source code
     * @param functionList function list of code
     */
    public void removeFunctionCommand(ArrayList<String> source, ArrayList<String> functionList) {
        int index = 0;
        for (int i = 0; i < source.size(); i++) {
            String s = source.get(i);
            if (s.contains(functionList.get(index)) && s.charAt(s.length() - 1) == '{') {
                int openBracketNum = 1;
                int closeBracketNum = 0;
                for (int j = i + 1; j < source.size(); j++) {
                    String line = source.get(j);
                    for (int c = 0; c < line.length(); c++) {
                        if (line.charAt(c) == '{') {
                            openBracketNum++;
                        } else {
                            if (line.charAt(c) == '}') {
                                closeBracketNum++;
                            }
                        }
                    }
                    if (openBracketNum == closeBracketNum) {
                        source.remove(j);
                        break;
                    } else {
                        source.remove(j);
                        j--;
                    }
                }
                index++;
                if (index == functionList.size()) {
                    break;
                }
            }
        }
    }

    /**
     * Get all attribute of source code
     *
     * @param source source code
     * @return attribute list
     */
    public ArrayList<String> getAllAttribute(ArrayList<String> source, ArrayList<String> functionList) {
        ArrayList<String> result = new ArrayList<>();
        removeFunctionCommand(source, functionList);
        for (String s : source) {
            if (!s.contains("{") && s.contains(";") && !s.contains("}")) {
                result.add(s.substring(0, s.indexOf(";")));
            }
        }
        return result;
    }

    /**
     * Get all static functions from a java file
     *
     * @param source source code
     * @return list of all static functions
     */
    public ArrayList<String> getAllFunction(File file) {
        ArrayList<String> source = new ArrayList<>();
        source = getSource(file);
        removeEmptyLines(source);
        removeIndentation(source);
        removePackageAndImport(source);
        removeComment(source);
        ArrayList<String> functionList = searchForFunctionDeclareLine(source);
        return functionList;
    }

    /**
     * Split string to array of string
     *
     * @param source source string
     * @return array of words in string
     */
    public String[] splitString(String source) {
        String name = source;
        name = name.replace(",", "");
        name = name.replace('(', ' ');
        name = name.replace(')', ' ');
        return name.split(" ");
    }

    /**
     * Search function in source code by name
     *
     * @param functionList list of all functions in source code
     * @param name name of function
     * @return format of function
     */
    public String findFunctionByName(List<String> functionList, String name) {
        String result = null;
        String[] functionDetails = splitString(name);
        String functionName = functionDetails[0];
        for (String function : functionList) {
            if (function.contains(functionName)) {
                String[] functionExtract = splitString(function);
                boolean isThisFunctionWhichWeWant = true;
                int startOfParam = 3;
                if (!isStringInList(permission, functionExtract[0])) {
                    startOfParam--;
                }
                int functionExtractFuncNum = 0;
                int functionDetailsFuncNum = 0;
                for (int i = 1; i < functionDetails.length; i++) {
                    functionDetailsFuncNum++;
                }
                for (int i = startOfParam; i < functionExtract.length; i = i + 2) {
                    functionExtractFuncNum++;
                }
                if (functionDetailsFuncNum != functionExtractFuncNum) {
                    isThisFunctionWhichWeWant = false;
                } else {
                    for (int i = 1; i < functionDetails.length; i++) {
                        if (startOfParam > functionExtract.length) {
                            isThisFunctionWhichWeWant = false;
                            break;
                        }
                        if (!functionExtract[startOfParam].equals(functionDetails[i])) {
                            isThisFunctionWhichWeWant = false;
                            break;
                        }
                        startOfParam += 2;
                    }
                }
                if (isThisFunctionWhichWeWant) {
                    return function;
                }
            }
        }
        return result;
    }

    /**
     * Get class name
     *
     * @param source source code
     * @return name of class
     */
    public String findClassName(ArrayList<String> source) {
        for (String i : source) {
            if (i.contains("class")) {
                String[] classDeclaration = splitString(i);
                for (int j = 0; j < classDeclaration.length; j++) {
                    if (classDeclaration[j].equals("class")) {
                        return classDeclaration[j + 1];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if class implements or extends what
     *
     * @param source source code
     * @return father class
     */
    public String getClassParent(ArrayList<String> source) {
        String line = source.get(0);
        String result = null;
        String className = findClassName(source);
        String[] lineSplit = splitString(line);
        for (String s : lineSplit) {
            if (!isStringInList(permission, s) && !isStringInList(statementKeywords, s) && !isStringInList(classRelationShip, s) && !s.equals(className) && !s.equals("class") && !s.equals("{")) {
                return s;
            }
        }
        return result;
    }

}
