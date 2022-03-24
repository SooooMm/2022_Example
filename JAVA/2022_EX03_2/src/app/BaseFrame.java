package app;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BaseFrame extends JFrame{

	public static void iMsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"정보",JOptionPane.INFORMATION_MESSAGE|JOptionPane.OK_OPTION);
	}
	public static void eMsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"경고",JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
	}
}
