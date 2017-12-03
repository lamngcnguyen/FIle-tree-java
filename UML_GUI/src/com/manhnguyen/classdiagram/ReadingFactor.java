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
    private static final String permission[] = {"public", "private", "default", "protected"};
    private static final String classRelationShip[] = {"extends", "implements"};
    private static final String per[] = {"abstract", "interface"};

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
            formatLegalStyle(result);
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
                while (true) {
                    if (lineSplit[index].equals("static") || lineSplit[index].equals("final")) {
                        index++;
                    } else {
                        break;
                    }
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
            if (!isStringInList(per, s) && !isStringInList(permission, s) && !isStringInList(statementKeywords, s) && !isStringInList(classRelationShip, s) && !s.equals(className) && !s.equals("class") && !s.equals("{")) {
                return s;
            }
        }
        return result;
    }

    /**
     * Build a sub string from a string
     *
     * @param source source string
     * @param start  start position
     * @param end    end position
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
     * @param list   array of string
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
     * Remove all commands of functions
     *
     * @param source    source code
     * @param startLine start line of function
     */
    public void removeFunctionCommand(ArrayList<String> source, int startLine) {
        String s = "{";
        int openBracketNum = 1;
        int closeBracketNum = 0;
        for (int i = startLine + 1; i < source.size(); i++) {
            String line = source.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '{') {
                    if (j > 0) {
                        openBracketNum++;
                    }
                } else {
                    if (line.charAt(j) == '}') {
                        closeBracketNum++;
                    }
                }
            }
            source.remove(i);
            i--;
            if (openBracketNum == closeBracketNum) {
                return;
            }
        }
    }
    /**
     * Search functions in source file
     *
     * @param data source code
     * @return function list
     */
    public ArrayList<String> searchForFunctionDeclareLine(ArrayList<String> data) {
        ArrayList<String> result = new ArrayList<>();
        String test = "{";
        char t = '{';
        for (int i = 0; i < data.size(); i++) {
            String line = data.get(i);
            if (line.charAt(line.length() - 1) == '{') {
                boolean isFunction = true;
                String lineSplit[] = line.split(" ");
                for (String s : lineSplit) {
                    if (isStringInList(statementKeywords, s)) {
                        isFunction = false;
                    }
                }
                for (String s : lineSplit) {
                    if (s.equals("class")) {
                        isFunction = false;
                    }
                }
                if (isFunction) {
                    result.add(line.substring(0, line.length() - 1));
                    removeFunctionCommand(data, i);
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
     * Get all attribute of source code
     *
     * @param source source code
     * @return attribute list
     */
    public ArrayList<String> getAllAttribute(ArrayList<String> source) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : source) {
            if (!s.contains("{") && s.contains(";") && !s.contains("}")) {
                if (s.contains("=")) {
                    result.add(s.substring(0, s.indexOf("=")));
                } else {
                    result.add(s.substring(0, s.indexOf(";")));
                }
            } else {
                if (!s.contains("{") && !s.contains("}") && !s.contains(";")) {
                    if (s.contains("=")) {
                        result.add(s.substring(0, s.indexOf("=")));
                    } else {
                        result.add(s);
                    }
                }
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
    public ArrayList<String> getAllFunction(ArrayList<String> source) {
        removeEmptyLines(source);
        removeIndentation(source);
        removePackageAndImport(source);
        removeComment(source);
        formatLegalStyle(source);
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
     * @param name         name of function
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
     * Add space before open bracket if missing
     *
     * @param s source string
     * @return legal style string
     */
    public String addSpaceBeforeOpenBracket(String s) {
        int spacePos[] = new int[s.length()];
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '{') {
                if (i > 0) {
                    if (s.charAt(i - 1) != ' ') {
                        spacePos[n] = i;
                        n++;
                    }
                }
            }
        }
        String result = "";
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (j < n) {
                if (i == spacePos[j]) {
                    result += ' ';
                    j++;
                }
            }
            result += s.charAt(i);
        }
        return result;
        /*
        int spacePos[] = new int[s.length()];
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '{') {
                if (i > 0) {
                    if (s.charAt(i - 1) != ' ') {
                        spacePos[n] = i;
                        n++;
                    }
                }
            }
        }
        String result = "";
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (j < n) {
                if (i == spacePos[j]) {
                    result += ' ';
                    j++;
                }
            }
            result += s.charAt(i);
        }
        return result;
        */
    }

    /**
     * Format shit code to legal style
     *
     * @param source source code
     */

    public void formatLegalStyle(ArrayList<String> source) {
        removeString(source);
        for (int i = 0; i < source.size(); i++) {
            String s = source.get(i);
            s = addSpaceBeforeOpenBracket(s);
            String set;
            set = source.set(i, s);
        }
    }

    /**
     * Remove all string of source code
     *
     * @param source source code
     */
    public void removeString(ArrayList<String> source) {
        for (int i = 0; i < source.size(); i++) {
            source.set(i, removeString(source.get(i)));
        }
    }

    /**
     * Remove all string from code line
     *
     * @param source code line
     * @return code line without string variable
     */
    public String removeString(String source) {
        String split[] = source.split(" ");
        String result = "";
        for (String s : split) {
            if (!s.contains("\"") && !s.contains("\'")) {
                result += s + " ";
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        if (source.charAt(source.length() - 1) == ';') {
            result += ";";
        }
        return result;
    }

}