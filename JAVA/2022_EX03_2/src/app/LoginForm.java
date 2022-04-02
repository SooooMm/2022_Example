package app;

import java.awt.Font;
import java.awt.Image;
import java.net.NoRouteToHostException;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.cj.Session;
import com.mysql.cj.protocol.Resultset;

import session.*;

public class LoginForm extends BaseFrame{
	public LoginForm() {
		super("로그인", 350, 175);
		setBorderLayout();
		addPrvForm(() -> new MainFrame().setVisible(true));
		
		north.add(createLabel(new JLabel("로그인"), new Font("HY헤드라인M", 1, 24)));
		var tfid = new JTextField(12);
		var tfpw = new JPasswordField(12);
		
		center.add(createComp(new JLabel("아이디"), 60, 30));
		center.add(tfid);
		center.add(createComp(new JLabel("비밀번호"), 60, 30));
		center.add(tfpw);
		
		east.add(createComp(createButton("로그인", e->{
			if(tfid.getText().length()==0||tfpw.getText().length()==0) {
				emsg("빈칸이 존재합니다");
				return;
			}
			if(tfid.getText().equals("admin")&&tfpw.getText().equals("1234")) {
				dispostWithPRV();
				imsg("관리자로 로그인 하였습니다");
				new adminMainFrame().setVisible(true);	
				return;
			}
			
			try {
				var rs = getPreparedResultSet("SELECT * FROM user WHERE binary u_id = ? AND  binary u_pw = ?",tfid.getText(),tfpw.getText());
				
				if(rs.next()) {
					
					dispose();
					session.Session.userNo = rs.getInt("u_no");
					session.Session.userName = rs.getString("u_name");
					session.Session.userGenger = rs.getInt("u_gender");
					session.Session.userGraduate = rs.getInt("u_graduate");
					session.Session.userImg = ImageIO.read(rs.getBlob("u_img").getBinaryStream());
					
					imsg(session.Session.userNo+"님 환영합니다");
				}else {
					emsg("회원정보가 일치하지 않습니다");
					tfid.setText("");
					tfpw.setText("");
					tfid.grabFocus();
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			
		}), 80, 80));
	}
	public static void main(String[] args) {
		new LoginForm().setVisible(true);
	}

}
