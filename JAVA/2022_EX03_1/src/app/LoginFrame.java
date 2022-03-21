package app;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import sesssion.Session;

public class LoginFrame extends BaseFrame {
	public LoginFrame() {
		super("로그인", 320, 160);
		setBorderLayout();
		
		addPrevForm(() -> new MainFrame().setVisible(true));
		
		east.setLayout(new BorderLayout());
		north.add(createLable(new JLabel("아르바이트"), new Font("HY헤드라인M", 1, 24)));
		
		var tfId = new JTextField(12);
		var tfPW = new JTextField(12);
		
		center.add(createcomp(new JLabel("아이디"), 60, 30));
		center.add(tfId);
		center.add(createcomp(new JLabel("비밀번호"), 60, 30));
		center.add(tfPW);
		
		east.setBorder(new EmptyBorder(8,3,3,3));
		east.add(createButton("로그인", e->{
			if(tfId.getText().length()==0||tfPW.getText().length()==0) {
				eMsg("빈칸이 존재합니다");
				return;
			}
			
			if(tfId.getText().equals("admin")||tfPW.getText().equals("1234")) {
				disposewithRemovingPrevForm();
				iMsg("관리자로 로그인하였습니다.");
				
				new AdminMainFrame().setVisible(true);
				return;
			}
			
			try {
				var rs = getPreparedResultSet("SELECT * FROM user WHERE binary u_id = ? AND binary u_pw = ? ", tfId.getText(),tfPW.getText());
				
				if(rs.next()) {
					dispose();
					
					Session.userNo = rs.getInt("u_no");
					Session.userName = rs.getString("u_name");
					Session.userImg = ImageIO.read(rs.getBlob("u_img").getBinaryStream());
					Session.userGender = rs.getInt("u_gender");
					Session.userGraduate = rs.getInt("u_graduate");
					iMsg(Session.userName + "님 환영합니다.");
					
				}else {
					eMsg("회원 정보가 일치하지 않습니다.");
					tfId.setText("");
					tfPW.setText("");
					tfId.grabFocus();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}));
	}

	public static void main(String[] args) {
		new LoginFrame().setVisible(true);
	}
}


