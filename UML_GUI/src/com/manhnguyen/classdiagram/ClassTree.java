package com.manhnguyen.classdiagram;

import java.awt.BorderLayout;
import java.util.*;
import java.io.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class tree represents
 * @author Nguyen Duc Manh
 */
public class ClassTree extends JPanel{

    String name; // name of class
    ClassTree parent; // null if class doesnt implement or extend anything
    ArrayList<ClassTree> children; // children list of class
    File source; // source code of class
    ArrayList<String> attributeList; // attribute list
    ArrayList<String> methodList; // method list
    String packageName; // package name
    String parentName; // parent name
    ArrayList<String> rawAttributeList;
    ArrayList<String> rawMethodList;
    ArrayList<String> formatedSourceCode; // formatted source code. after removing function command lines and every shit lines which we dont need


    private final Runnable runnable; // lambda method

    public String getName() {
        return name;
    }

    public ArrayList<ClassTree> getChildren() {
        return children;
    }

    public String getParentName(){
        return parentName;
    }

    public ArrayList<String> getAttributeList() {
        return attributeList;
    }

    public ArrayList<String> getMethodList() {
        return methodList;
    }

    /**
     * Specify constructor with param
     * @param file source code
     */
    public ClassTree(File file) {
        this.runnable = System.out::println;
        source = file;
        ReadingFactor fu = new ReadingFactor();
        ArrayList<String> sourceCode = fu.getSource(file);
        name = fu.findClassName(sourceCode);
        methodList = fu.getAllFunction(sourceCode);
        attributeList = fu.getAllAttribute(sourceCode);
        rawMethodList = methodList;
        rawAttributeList = attributeList;
        fu.remakeVisibilityMember(methodList);
        fu.remakeVisibilityMember(attributeList);
        parentName = fu.getClassParent(sourceCode);
        parent = null;
        formatedSourceCode = sourceCode;
        // Make a tree list with all the nodes, and make it a JTree
        JTree tree = new JTree(addNodes());

        // Add a listener
        tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) e
                    .getPath().getLastPathComponent();
            System.out.println("You selected " + node);
        });

        // Lastly, put the JTree into a JScrollPane.
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);

        for (String s : sourceCode) {
            System.out.println(s);
        }
    }

    private DefaultMutableTreeNode addNodes(){
        String s = new String();
        DefaultMutableTreeNode classInfo = new DefaultMutableTreeNode(s);

        return classInfo;
    }

    /**
     * Search for function
     * @param searchedString searched string
     * @return string declaration
     */
    public String searchForFunction(String searchedString) {
        ReadingFactor rf = new ReadingFactor();
        return rf.findFunctionByName(rawAttributeList, searchedString);
    }


    /**
     * Show information of class
     */
    public String showInformation() {
        StringBuilder export = new StringBuilder();

        export.append(name + "\n");
        export.append("\n");
        export.append("Parent: ");
        export.append(parentName);
        export.append("\n");
        export.append("Attribute List: \n");
        if (attributeList.isEmpty()) {
            export.append("There's no attribute");
            export.append("\n");
        } else {
            attributeList.stream().map((s) -> {
                export.append(s);
                return s;
            }).forEachOrdered((_item) -> {
                export.append("\n");
            });
        }
        export.append("Method List: \n");
        if (methodList.isEmpty()) {
            export.append("There's no method");
            export.append("\n");
        } else {
            methodList.stream().map((s) -> {
                export.append(s);
                return s;
            }).forEachOrdered((_item) -> {
                export.append("\n");
            });
        }
        export.append("\n");
        return export.toString();
    }


    /**
     * Just a test-purpose method
     */
    public void testForShowParams(String x, String s, String m, String t, String m2){

    }

}
