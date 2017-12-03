package com.manhnguyen.classdiagram;

import java.util.*;
import java.io.*;

/**
 * Class diagram represents
 * @author Manh Nguyen
 */
public class ClassDiagram {
	
	ClassTree x; // test attribute

    /**
     * Search for function information
     * @param classes class list
     * @param functionName function name
     * @return info of function
     */
    public String searchForFunction(ArrayList<ClassTree> classes, String functionName) {
        String result = "";
        for (ClassTree c : classes) {
            ArrayList<String> sourceCode = c.formatedSourceCode;
            for (String s : sourceCode) {
                if (s.contains(functionName.substring(0, functionName.indexOf("("))) && s.contains("{")) {
                    result += c.name + " : " + s.substring(0, s.indexOf("{")) + "\n";
                }
            }
        }
        return result;
    }

    /**
     * Search for attribute information
     * @param classes class list
     * @param attributeName name of attribute
     * @return info of attribute
     */
    public String searchForAttribute(ArrayList<ClassTree> classes, String attributeName) {
        String result = "";
        ReadingFactor rf = new ReadingFactor();
        for (ClassTree c : classes) {
            ArrayList<String> sourceCode = c.formatedSourceCode;
            for (String s : sourceCode) {
                if (s.contains(attributeName) && !s.contains("{")) {
                    String line = "";
                    if (s.contains("=")) {
                        line = s.substring(0, s.indexOf("="));
                    }
                    if (s.contains(";")) {
                        line = s.substring(0, s.indexOf(";"));
                    }
                    String[] lineSplit = line.split(" ");
                    boolean isFound = false;
                    for (String ss : lineSplit) {
                        if (ss.equals(attributeName)) {
                            isFound = true;
                            break;
                        }
                    }
                    if (isFound) {
                        if (s.contains("=")) {
                            result += c.name + " : " + s.substring(0, s.indexOf("=")) + "\n";
                        }
                        if (s.contains(";")) {
                            result += c.name + " : " + s.substring(0, s.indexOf(";")) + "\n";
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Check Has-A relationship of a project
     * @param classes class list
     */
    public void checkHasARelationship(ArrayList<ClassTree> classes) {
    	ArrayList<String> classNameList = new ArrayList<>();
    	ArrayList<ArrayList<String>> attrList = new ArrayList<>();
    	for (ClassTree c : classes) {
    		classNameList.add(c.name);
    		ArrayList<String> attributeNameList = new ArrayList<>();
    		for (String s : c.attributeList) {
    			attributeNameList.add(s.substring(s.indexOf(":") + 2, s.length()));
    		}
    		attrList.add(attributeNameList);
    		for (String s : attributeNameList) {
    			System.out.println(s);
    		}
    	}
    	for (int i = 0; i < classes.size(); i++) {
    		ArrayList<String> objectList = new ArrayList<>();
    		ArrayList<String> attrTypeList = attrList.get(i);
    		for (String s : attrTypeList) {
    			if (objectList.contains(s)) {
    				continue;
    			}
    			if (classNameList.contains(s)) {
    				objectList.add(s);
    			}
    		}
    		ClassTree newClass = classes.get(i);
    		newClass.objectAttributeList = objectList;
    		classes.set(i, newClass);
    	}
    }

    /**
     * Make relationship between classes
     *
     * @param classes
     */
    public static String linkClass(ArrayList<ClassTree> classes) {
        StringBuilder export = new StringBuilder();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).parentName != null) {
                ClassTree c = classes.get(i);
                for (int j = 0; j < classes.size(); j++) {
                    if (classes.get(j).name.equals(c.parentName)) {
                        if(!c.name.equals(classes.get(j).name)) {
                            ClassTree x = classes.get(j);
                            c.parent = classes.get(j);
                            classes.set(i, c);
                            classes.set(j, x);
                            export.append(classes.get(j).getName() + " is parent of " + c.getName() + "\n");
                        }
                    }
                }
            }
        }
        return export.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileUtils fu = new FileUtils();
        ArrayList<File> files = fu.getAllJavaFiles(new File("C:\\Users\\lam\\OneDrive\\Programming\\Java\\int2204\\Tuan6"));
        System.out.println(new File("C:\\Users\\lam\\OneDrive\\Programming\\Java\\int2204\\Tuan6").getAbsolutePath());
        ArrayList<ClassTree> classes = new ArrayList<>();
        for (File f : files) {
            ClassTree c = new ClassTree(f);
            classes.add(c);
            System.out.println(c.showInformation());
        }
        ClassDiagram cd = new ClassDiagram();
        cd.checkHasARelationship(classes);
        System.out.println("Func : " + cd.searchForFunction(classes, "main()"));
        System.out.println("Atri : " + cd.searchForAttribute(classes, "name"));
        for (ClassTree c : classes) {
        	System.out.print(c.name + ": ");
        	for (String s : c.objectAttributeList) {
        		System.out.print(s + " ");
        	}
        	System.out.println();
        }
        linkClass(classes);
        File f = new File("/Users/dhungc3/OneDrive/Programming/Java/int2204/Tuan6/");
        ClassTree c = new ClassTree(f);
        c.showInformation();
        System.out.println(c.searchForFunction("linkClass (ArrayList<ClassTree>"));
    }

}