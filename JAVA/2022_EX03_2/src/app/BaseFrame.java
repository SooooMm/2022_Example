package app;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mysql.cj.protocol.Resultset;

import db.*;

public class BaseFrame extends JFrame{
	static Connection con;
	static Statement stmt;
	
	JPanel east = new JPanel();
	JPanel west = new JPanel();
	JPanel south = new JPanel();
	JPanel north = new JPanel();
	JPanel center = new JPanel();
	Thread thread;
	WindowListener prv;
	
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/2022_ex03_2?serverTimeZone=UTC", "root", Setting.DB_PWD);
			stmt = con.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public BaseFrame(String title,int w, int h) {
		setTitle(title);
		setSize(w,h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
		});
	}
	public void setBorderLayout() {
		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(south, BorderLayout.SOUTH);
		add(west, BorderLayout.WEST);
		add(east, BorderLayout.EAST);
		add(center);
	}
	BaseFrame addPrvForm(Runnable r) {
		prv = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		};
		
		addWindowListener(prv);
		return this;
	}
	JLabel createLabel(JLabel label, Font font) {
		label.setFont(font);
		return label;
	}
	JButton createButton(String txt, ActionListener ac) {
		var btn = new JButton(txt);
		btn.addActionListener(ac);
		
		return btn;
	}
	public static <T extends JComponent> T createComp(T comp, int x, int y, int w, int h) {
		comp.setPreferredSize(new Dimension(w, h));
		comp.setBounds(x, y, w, h);
		
		return comp;
	}
	
	public static <T extends JComponent> T createComp(T comp, int w, int h) {
		return createComp(comp, 0, 0, w, h);
	}
	void dispostWithPRV() {
		removeWindowListener(prv);
		dispose();
	}

	
	void disposeWindow() {
		if(thread!=null&&thread.isAlive())
			thread.interrupt();
	}
	public static void imsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"정보",JOptionPane.INFORMATION_MESSAGE|JOptionPane.OK_OPTION);
	}
	public static void emsg(String txt) {
		JOptionPane.showMessageDialog(null, txt,"경고",JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
	}
	
	public static ResultSet getPreparedResultSet(String sql, Object ...objs) {
		try {
			var pstmt = con.prepareStatement(sql);
			
			for (int i = 0; i < objs.length; i++) {
				pstmt.setObject(i + 1, objs[i]);
			}
			
			return pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
