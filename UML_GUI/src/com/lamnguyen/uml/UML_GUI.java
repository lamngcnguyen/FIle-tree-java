package com.lamnguyen.uml;

import javax.swing.*;
import java.awt.*;

/**
 * Handle main GUI functions
 * @author Nguyen Ngoc Lam
 */

public class UML_GUI {
	private static UML_Window UMLWindow;
	private static JSplitPane splitPane;
	private static ToolBar toolBar;
	private static MenuBar menuBar;
	private static Project_Tree project_tree;
	private static DiagramPanel diagramPanel;

	/**
	 * Main UML window getter
	 * @return the main UML window to be used in other classes
	 */
	public static UML_Window getUMLWindow() {
		return UMLWindow;
	}

	/**
	 * Project tree getter
	 * @return the project tree to be drawn in the drawGUI() method
	 */
	public static Project_Tree getProjectTree() {
		return project_tree;
	}

	/**
	 * Project draw panel getter
	 * @return the project draw panel to be drawn in the drawGUI() method
	 */
	public static DiagramPanel getDiagramPanel(){
		return diagramPanel;
	}

	/**
	 * The main method to run everything
	 */
	public static void run() {
		UMLWindow = UML_Window.getUMLWindow();
		menuBar = MenuBar.getMenuBar();
		toolBar = ToolBar.getToolBar();
		project_tree = new Project_Tree();
		project_tree.setMinimumSize(new Dimension(240, 240));
		diagramPanel = new DiagramPanel();
		diagramPanel.setMinimumSize(new Dimension(240,240));


		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, project_tree, diagramPanel);

		UMLWindow.add(toolBar, BorderLayout.PAGE_START);
		UMLWindow.add(splitPane, BorderLayout.CENTER);
		UMLWindow.setJMenuBar(menuBar);
	}
}
