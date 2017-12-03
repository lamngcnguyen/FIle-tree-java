package com.lamnguyen.uml;

import javax.swing.*;
import java.awt.*;

/**
 * Create main UML_GUI window
 * @author Nguyen Ngoc Lam
 */

public class UML_Window extends JFrame {
	private static UML_Window window = new UML_Window();

	/**
	 * UML_Window Constructor, creating main window
	 */
	private UML_Window() {
		super("UML GUI");

		this.setSize(1280, 720);
		this.setMinimumSize(new Dimension(640, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Return main window to be used in other classes
	 * @return UMLWindow
	 */

	public static UML_Window getUMLWindow() {
		return window;
	}
}
