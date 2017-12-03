package com.lamnguyen.uml;

import com.manhnguyen.classdiagram.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * Create the project tree on the left side panel
 * @author Nguyen Ngoc Lam
 */
public class Project_Tree extends JScrollPane {
    /**
     * Project_Tree constructor
     */
    public Project_Tree(){
        super();
        //Create the empty tree when no project is loaded
        createTree(new DefaultMutableTreeNode("No Project Loaded"));
    }

    /**
     * Add nodes to the project tree
     * @param classes array list contains classes in the project
     * @param projectName name of the project
     * @return the project tree
     */
    private DefaultMutableTreeNode addNodes(ArrayList<ClassTree> classes, String projectName){
        DefaultMutableTreeNode project = new DefaultMutableTreeNode(projectName);
        for(ClassTree c : classes) {
            //Project root node
            DefaultMutableTreeNode projectTree = new DefaultMutableTreeNode(c.getName());
            //Attribute root node
            DefaultMutableTreeNode attributeTree = new DefaultMutableTreeNode("Attributes");
            projectTree.add(attributeTree);
            //Creating attribute tree
            for(String attribute : c.getAttributeList()){
                DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode(attribute);
                attributeTree.add(attributeNode);
            }
            //Method root node
            DefaultMutableTreeNode methodTree = new DefaultMutableTreeNode("Methods");
            projectTree.add(methodTree);
            //Method attribute tree
            for(String method : c.getMethodList()){
                DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(method);
                methodTree.add(methodNode);
            }
            project.add(projectTree);
        }
        return project;
    }

    /**
     * Create the project tree
     * @param treeNode node to be added to the tree
     */
    private void createTree(DefaultMutableTreeNode treeNode){
        JTree tree = new JTree(treeNode);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
//        Icon classIcon = new ImageIcon("");

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        setViewportView(tree);
        setPreferredSize(new Dimension(300, 800));
    }

    /**
     * Draw the project tree
     * @param classes array list contains classes in the project
     * @param projectName name of the project
     */
    protected void draw(ArrayList<ClassTree> classes, String projectName){
        DefaultMutableTreeNode projectTree = addNodes(classes, projectName);
        createTree(projectTree);
    }
}
