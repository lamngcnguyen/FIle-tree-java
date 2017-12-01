package com.manhnguyen.classdiagram;

import java.util.*;
import java.io.*;

/**
 * File utils represents
 * 
 * @author Manh Nguyen
 */
public class FileUtils {
    
    /**
     * Get extension of file 
     * @param file file
     * @return extension of file
     */
    public String getExtOfFile(File file) {
        String extension = "";
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
    
    /**
     * Get all java files in folder
     * @param root root folder
     * @return list of java files
     */
    public ArrayList<File> getAllJavaFiles(File root) {
        ArrayList<File> result = new ArrayList<>();
        File listFile[];
        listFile = root.listFiles();
        for (File file : listFile) {
            if (file.isFile()) {
                if (getExtOfFile(file).equals("java")) {
                    result.add(file);
                }
            } else {
                ArrayList<File> childResult = getAllJavaFiles(file);
                childResult.forEach((f) -> {
                    result.add(f);
                });
            }
        }
        return result;
    }
    
}
