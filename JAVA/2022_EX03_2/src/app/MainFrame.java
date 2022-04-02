package app;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends BaseFrame{
	public MainFrame() {
		super("Main", 320, 480);
		setBorderLayout();
		north.add(createLabel(new JLabel("아르바이트"), new Font("HY헤드라인M", 1, 24)));
		center.setLayout(new BorderLayout());
		
		var centerC = new JPanel(null);
		var centerN = new JPanel();
		var search = new JTextField(10);
		
		centerN.add(new JLabel("기업검색"));
		centerN.add(search);
		centerN.add(createButton("검색", e->{
			System.out.println("검색");
		}));
		
		centerC.add(createComp(createButton("로그인",e->{
			dispose();
			new LoginForm().setVisible(true);
		}), 150, 40, 130, 40));
		
		centerC.add(createComp(createButton("회원가입",e->{
			dispose();
			new SignUpForm().setVisible(true);
		}), 150, 90, 130, 40));
		
		centerC.add(createComp(createButton("닫기",e->{
			dispose();
		}), 150, 140, 130, 40));

		
		centerC.add(new JButton("로그인"));
		center.add(centerC);
		center.add(centerN,BorderLayout.NORTH);
		
	}
	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}

}
