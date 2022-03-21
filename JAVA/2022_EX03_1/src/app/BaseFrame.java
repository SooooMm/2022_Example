package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale.Category;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.Resultset.Concurrency;


import db.Setting;

public class BaseFrame extends JFrame{
	static final String[] categoryList = "편의점,영화관,화장품,음식점,백화점,의류점,커피전문점,은행".split(",");
	static final String[] gender= ",남자,여자,무관".split(",");
	static final String[] grad = "대학교 졸업,고등학교 졸업,중학교 졸업,무관".split(",");
	static final String[] apply = "심사중,합격,불합격".split(",");
	
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static Connection con;
	static Statement stmt;
	
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/2022_ex03_1?serverTimezone=UTC","root",Setting.DB_PWD);
			stmt = con.createStatement();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class ComboItem{
		int id;
		String text;
		
		public ComboItem(int id, String text) {
			this.id = id;
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	JPanel north = new JPanel();
	JPanel south = new JPanel();
	JPanel east = new JPanel();
	JPanel west = new JPanel();
	JPanel center = new JPanel();
	Thread thread;
	WindowListener prevListener;
	
	
	
	public BaseFrame(String title, int w, int h) {
		setTitle(title);
		setSize(w,h);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				destroyThread();
			}
		});
	}
	
	
	protected void destroyThread() {
		if(thread !=null && thread.isAlive())
			thread.interrupt();
	}
	public static ResultSet getPreparedResultSet(String sql, Object ...objects) {	
		try {
			var pstmt = con.prepareStatement(sql);
			
			for (int i = 0; i < objects.length; i++) {
				pstmt.setObject(i + 1, objects[i]);
			}
			
			return pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int executeSQL(String sql, Object ...objects) {
		try {
			var pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(int i=0; i<objects.length; i++) {
				pstmt.setObject(i+1, objects[i]);
			}
			
			pstmt.executeUpdate();
			
			var rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
		
	}
	
	BaseFrame addPrevForm(Runnable r) {
		prevListener = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			};
		};
		
		addWindowListener(prevListener);
		return this;
	}
	void disposewithRemovingPrevForm() {
		removeWindowListener(prevListener);
		dispose();
	}
	
	public void setBorderLayout()
	{
		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(south, BorderLayout.SOUTH);
		add(west, BorderLayout.WEST);
		add(east, BorderLayout.EAST);
		add(center);
	}
	public JLabel createLable(JLabel lb, Font font) {
		lb.setFont(font);
		return lb;
	}
	public JLabel createLable(BufferedImage img,int w, int h) {
		var lb = new JLabel();
		lb.setIcon(getResizedIcon(img, w, h));
		lb.setBorder(new LineBorder(Color.black));
		return lb;
	}
	public JButton createButton(String txt, ActionListener action) {
		var btn = new JButton(txt);
		btn.addActionListener(action);
		return btn;
	}
	
	public static ImageIcon getResizedIcon(BufferedImage img,int w,int h) {
		return new ImageIcon(img.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH));
	}
	public static <T extends JComponent> T createcomp(T comp, int x, int y, int w, int h) {
		comp.setPreferredSize(new Dimension(w,h));
		comp.setBounds(x, y, w, h);     
		return comp;
	}
	public static <T extends JComponent> T createcomp(T comp,int w, int h) {
		return createcomp(comp, 0, 0,w,h);
	}
	public static void iMsg(String m) {
		JOptionPane.showMessageDialog(null, m, "정보", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);
	}
	public static void eMsg(String m) {
		JOptionPane.showMessageDialog(null, m, "경고", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
	}
	
	public static String strToNo(String category) {
		var tmp = new ArrayList<String>();
		
		if(category.length()==0) return "";
		
		for(String s : category.split(",")) {
			tmp.add(""+(Arrays.asList(categoryList).indexOf(s)+1));
			
		}
		return String.join(",", tmp);
	}
	public static String noToStr(String c) {
		var tmp = new ArrayList<String>();
		
		for(String s : c.split(",")) {
			tmp.add(categoryList[Integer.valueOf(s)-1]);
		}
		
		return String.join(",", tmp);
	}
	
}
