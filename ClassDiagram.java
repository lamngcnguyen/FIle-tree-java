package com.manhnguyen.classdiagram;

import java.util.*;
import java.io.*;

/**
 * Class diagram represents
 * @author Manh Nguyen
 */
public class ClassDiagram {
    
    /**
     * Make relationship between classes
     * @param classes
     */
    public static void linkClass(ArrayList<ClassTree> classes) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).parentName != null) {
                ClassTree c = classes.get(i);
                for (int j = 0; j < classes.size(); j++) {
                    if (classes.get(j).name.equals(c.parentName)) {
                        if(!c.getName().equals(classes.get(j).name)) {
                            ClassTree x = classes.get(j);
                            c.parent = classes.get(j);
                            classes.set(i, c);
                            classes.set(j, x);
                            System.out.println(classes.get(j).name + " is parent of " + c.getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileUtils fu = new FileUtils();
        ArrayList<File> files = fu.getAllJavaFiles(new File("/Users/dhungc3/OneDrive/Programming/Java/int2204/Tuan6"));
        //System.out.println(new File("..").getAbsolutePath());
        ArrayList<ClassTree> classes = new ArrayList<>();
        for (File f : files) {
            ClassTree c = new ClassTree(f);
            classes.add(c);
        }
//        linkClass(classes);
        for (ClassTree c : classes) {
            c.showInformation();
        }
    }
    
}
