package com.lamnguyen.uml;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Display a file system in a JTree view
 * @author Nguyen Ngoc Lam
 * This code is based on Ian Darwin's solution for displaying folder tree in Java Swing
 * 
 */
public class FileTree extends JPanel {
    /**
     * FileTree Constructor
     *
     * @param dir file directory
     */
    
    public FileTree(File dir) {
        setLayout(new BorderLayout());

        // Make a tree list with all the nodes, and make it a JTree
        JTree tree = new JTree(addNodes(null, dir, dir.getName()));

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

    /**
     *  Add nodes from under "dir" into curTop. Highly recursive.
     *  @param curTop current top position
     *  @param dir directory
     */
    private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir, String displayName) {
        String curPath = dir.getPath();
        String displayPath = dir.getName();
        
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(displayPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
        }
        
        ArrayList<String> dirList = new ArrayList();
        String[] pathList = dir.list();
        dirList.addAll(Arrays.asList(pathList));
        Collections.sort(dirList, String.CASE_INSENSITIVE_ORDER);
        File f;
        ArrayList<String> files = new ArrayList();
        //ArrayList<String> directories = new ArrayList();
        
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < dirList.size(); i++) {
            String thisObject = (String) dirList.get(i);
            String newPath;
            if (curPath.equals("."))
                newPath = thisObject;
            else
                newPath = curPath + File.separator + thisObject;
            if ((f = new File(newPath)).isDirectory())
                addNodes(curDir, f, displayPath);
            else
                files.add(thisObject);
        }
        
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++)
            curDir.add(new DefaultMutableTreeNode(files.get(fnum)));
        return curDir;
    }
    
    /**
     * Display File tree to a panel
     * @param path
     * @param panel
     */
    public static void displayFileTree(String path, JPanel panel){
        if (path.equals("")) {
            DefaultMutableTreeNode nullTree = new DefaultMutableTreeNode("null");
            JTree tree = new JTree(nullTree);
            panel.add(tree);
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));          
                panel.add(new FileTree(new File(path)));
        }
    }
}