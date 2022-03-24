package db;

import java.io.FileInputStream;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import app.BaseFrame;

public class Setting {

	public static String DB_PWD = "Suy0Sm0tdo";
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			var con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", DB_PWD);
			var stmt = con.createStatement();
			
			// LOAD DATA ���� �ɼ� ����
			stmt.execute("SET GLOBAL local_infile= 1");
			
			// DB ����� ����
			stmt.execute("DROP SCHEMA IF EXISTS `2022_EX03_02`");
			// DB ����
			stmt.execute("CREATE SCHEMA `2022_EX03_02` DEFAULT CHARACTER SET utf8 ;");
			
			BaseFrame.iMsg("���� ����");
		}catch (Exception e) {
			e.printStackTrace();
			BaseFrame.eMsg("���� ����");
		}
		
	}

}
