package db;

import java.sql.DriverManager;

public class Settinh {
	public static String DB_PW = "Suy0Sm0tdo";
	public static void main(String[] args) {
		try {
			var con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true","root",DB_PW);
			var stmt = con.createStatement();
			stmt.execute("SET GLOBAL local_infile=1");
			stmt.execute("DROP SCHEMA IF EXISTS `2022_ex03_2`");
			stmt.execute("CREATE SCHEMA `2022_ex03_2` DEFAULT CHARACTER SET utf8");
			
			stmt.execute("CREATE TABLE `2022_ex03_2`.`company` (\r\n"
					+ "  `c_no` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `c_name` VARCHAR(10) NULL,\r\n"
					+ "  `c_ceo` VARCHAR(10) NULL,\r\n"
					+ "  `c_address` VARCHAR(100) NULL,\r\n"
					+ "  `c_employee` INT NULL,\r\n"
					+ "  `c_img` LONGBLOB NULL,\r\n"
					+ "  `c_search` INT NULL,\r\n"
					+ "  PRIMARY KEY (`c_no`))\r\n"
					+ "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8;\r\n"
					+ "");
			
			stmt.execute("CREATE TABLE `2022_ex03_2`.`user` (\r\n"
					+ "  `u_no` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `u_name` VARCHAR(10) NULL,\r\n"
					+ "  `u_id` VARCHAR(10) NULL,\r\n"
					+ "  `u_pw` VARCHAR(15) NULL,\r\n"
					+ "  `u_birth` VARCHAR(15) NULL,\r\n"
					+ "  `u_email` VARCHAR(10) NULL,\r\n"
					+ "  `u_gender` INT NULL,\r\n"
					+ "  `u_graduate` INT NULL,\r\n"
					+ "  `u_address` VARCHAR(100) NULL,\r\n"
					+ "  `u_img` LONGBLOB NULL,\r\n"
					+ "  PRIMARY KEY (`u_no`))\r\n"
					+ "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8;\r\n"
					+ "");
			
			stmt.execute("CREATE TABLE `2022_ex03_2`.`employment` (\r\n"
					+ "  `e_no` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `c_no` INT NULL,\r\n"
					+ "  `e_title` VARCHAR(30) NULL,\r\n"
					+ "  `e_pay` INT NULL,\r\n"
					+ "  `e_people` INT NULL,\r\n"
					+ "  `e_gender` INT NULL,\r\n"
					+ "  `e_graduate` INT NULL,\r\n"
					+ "  PRIMARY KEY (`e_no`),\r\n"
					+ "  INDEX `fk_employment_c_no_idx` (`c_no` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `fk_employment_c_no`\r\n"
					+ "    FOREIGN KEY (`c_no`)\r\n"
					+ "    REFERENCES `2022_ex03_2`.`company` (`c_no`)\r\n"
					+ "    ON DELETE CASCADE\r\n"
					+ "    ON UPDATE CASCADE)\r\n"
					+ "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8;\r\n"
					+ "");
			
			stmt.execute("CREATE TABLE `2022_ex03_2`.`applicant` (\r\n"
					+ "  `a_no` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `e_no` INT NULL,\r\n"
					+ "  `u_no` INT NULL,\r\n"
					+ "  `a_apply` INT NULL,\r\n"
					+ "  PRIMARY KEY (`a_no`),\r\n"
					+ "  INDEX `fk_applicant_e_no_idx` (`e_no` ASC) VISIBLE,\r\n"
					+ "  INDEX `fk_applicant_u_no_idx` (`u_no` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `fk_applicant_e_no`\r\n"
					+ "    FOREIGN KEY (`e_no`)\r\n"
					+ "    REFERENCES `2022_ex03_2`.`employment` (`e_no`)\r\n"
					+ "    ON DELETE CASCADE\r\n"
					+ "    ON UPDATE CASCADE,\r\n"
					+ "  CONSTRAINT `fk_applicant_u_no`\r\n"
					+ "    FOREIGN KEY (`u_no`)\r\n"
					+ "    REFERENCES `2022_ex03_2`.`user` (`u_no`)\r\n"
					+ "    ON DELETE CASCADE\r\n"
					+ "    ON UPDATE CASCADE)\r\n"
					+ "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8;\r\n"
					+ "");
			
			stmt.execute("USE 2022_ex03_2");
			
			var tables = "company,user,employment,applicant".split(",");
			for(String table :tables) {
				stmt.execute("LOAD DATA LOCAL INFILE 'datafiles/" + table + ".txt' INTO TABLE " + table + " IGNORE 1 LINES");
				
			}
			
			var con2 = DriverManager.getConnection("jdbc:mysql://localhost/`2022_ex03_2`?serverTimezone=UTC&allowLoadLocalInfile=true","root",DB_PW);
			var
			
			System.out.println("O");
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("X");
		}
		
		
	}

}
