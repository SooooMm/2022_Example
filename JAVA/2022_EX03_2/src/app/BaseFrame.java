package app;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BaseFrame extends JFrame{

	public static void iMsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"����",JOptionPane.INFORMATION_MESSAGE|JOptionPane.OK_OPTION);
	}
	public static void eMsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"���",JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
	}
}
