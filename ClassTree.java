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
    
    private final Runnable runnable; // lambda method
    
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
        methodList = fu.getAllFunction(file);
        attributeList = fu.getAllAttribute(sourceCode, methodList);
        fu.remakeVisibilityMember(methodList);
        fu.remakeVisibilityMember(attributeList); 
        parentName = fu.getClassParent(sourceCode);
        parent = null;
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
    }
    
    private DefaultMutableTreeNode addNodes(){
        String s = new String();
        DefaultMutableTreeNode classInfo = new DefaultMutableTreeNode(s);
        
        return classInfo;
    }
    
    /**
     * Show information of class
     */
    public void showInformation() {
        System.out.println(name);
        runnable.run();
        System.out.println("Attribute List: ");
        if (attributeList.isEmpty()) {
            System.out.println("There's no attribute");
            runnable.run();
        } else {
            attributeList.stream().map((s) -> {
                System.out.println(s);
                return s;
            }).forEachOrdered((_item) -> {
                runnable.run();
            });
        }
        System.out.println("Method List: ");
        if (methodList.isEmpty()) {
            System.out.println("There's no method");
            runnable.run();
        } else {
            methodList.stream().map((s) -> {
                System.out.println(s);
                return s;
            }).forEachOrdered((_item) -> {
                runnable.run();
            });
        }
    }
    
    /**
     * Just a test-purpose method
     */
    public void testForShowParams(String x, String s, String m, String t, String m2){
        
    }
    
}
