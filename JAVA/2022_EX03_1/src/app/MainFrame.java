package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import sesssion.Session;

public class MainFrame extends BaseFrame{
	JLabel[] lbCompany = new JLabel[5];
	JLabel[] lbCount = new JLabel[5];
	JPanel animationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
	JComboBox cbProvince = new JComboBox("전체,서울,부산,대구,인천,광주,대전,울산,세종,경기,강원,충북,충남,전북,전남,경북,경남,제주".split(","));
	
	public MainFrame() {
		super("Main", 330, 430);
		setBorderLayout();
		north.add(createLable(new JLabel("아르바이트"), new Font("HY헤드라인M",1,20)));
		center.setLayout(new BorderLayout());
		
		var centerN = new JPanel();
		var centerC = new JPanel(null);
		var searchtf = new JTextField(12);
		
		centerN.add(new JLabel("기업검색"));
		centerN.add(searchtf);
		centerN.add(createButton("검색",e->{
			if(searchtf.getText().length()==0) {
				eMsg("검색할 기업명을 입력하세요.");
				return;
			}
			//검색기능
			String keyword = "%"+searchtf.getText()+"%";
			var rs = getPreparedResultSet("SELECT * FROM company WHERE c_name LIKE ?", keyword);
			
			try {
				if(rs.next()) {
					//c_search 증가
					executeSQL("UPDATE company SET c_search = c_search+1 WHERE c_no ", rs.getInt("c_no"));
					
					//건수 업데이트
					updateSearch();
					
					//상세기업정보 폼 실행
					setVisible(false);
					new CompanyFrame(rs.getInt("c_no"),false)
						.addPrevForm(()->setVisible(true)).setVisible(true);
				}else {
					eMsg("검색한 기업이 없습니다.");
					searchtf.setText("");
					searchtf.grabFocus();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
				
			
			
		}));
		
		centerC.add(createcomp(new JLabel("인기기업"), 10, 0, 80,24 ));
		for(int i=1; i<=5; i++) {
			centerC.add(createcomp(new JLabel(i+""), 10, i*30, 20, 25));
			centerC.add(lbCompany[i-1] = createcomp(new JLabel("기업"), 40, i * 30, 80, 24));
			centerC.add(lbCount[i-1] = createcomp(new JLabel("0"), 130, i * 30, 30, 24));
			
		}
		
		
		if (Session.userNo == 0) {
			// 로그인X
			centerC.add(createcomp(createButton("로그인", e -> {
				dispose();
				
				new LoginFrame()
					.setVisible(true);
			})
					, 170, 28, 100, 36));
			centerC.add(createcomp(createButton("회원가입", e -> {
				dispose();
				
				new SignUpFrame()
					.setVisible(true);
				
			})
					, 170, 28 + 40, 100, 36));
			centerC.add(createcomp(createButton("닫기", e -> dispose())
					, 170, 28 + 80, 100, 36));
		}else {
			north.add(createLable(Session.userImg, 30, 30));
			north.add(new JLabel(Session.userName+"님 환영합니다"));
			
			centerC.add(createcomp(createButton("로그아웃", e -> {
				dispose();
				Session.userNo = 0;
				Session.userName = "";
				new MainFrame().setVisible(true);
			})
					, 170, 28, 100, 36));
			centerC.add(createcomp(createButton("채용정보", e -> {
				dispose();
				new JobInfoFrame().setVisible(true);
			})
					, 170, 28 + 40, 100, 36));
			centerC.add(createcomp(createButton("마이페이지", e -> {
				dispose();
				new MyPageFrame().setVisible(true);
			})
					, 170, 28 + 80, 100, 36));
			centerC.add(createcomp(createButton("닫기", e -> dispose())
					, 170, 28 + 120, 100, 36));
		}
		
		
		
		
		center.add(centerN,BorderLayout.NORTH);
		center.add(centerC);
		
		centerC.add(createcomp(new JLabel("지역"), 10,190,80, 25));
		centerC.add(cbProvince);
		
		var wrapper = createcomp(new JPanel(null), 5,220, 290, 75);
		wrapper.add(createcomp(animationPanel, 1, 1, 0, 0));
		centerC.add(wrapper);
		
		updateSearch();
		craeteAnimation("");
		
	}
	public void craeteAnimation(String area) {
		destroyThread();
		
		animationPanel.removeAll();
		
		area=area.equals("전체")?"":area;
		
		var rs = getPreparedResultSet("SELECT * FROM company WHERE c_address LIKE '"+area+"%'");
		var nameList = new ArrayList<String>();
		var imgList = new ArrayList<ImageIcon>();
	
		try {
			while (rs.next()){
				nameList.add(rs.getString("c_name"));
				imgList.add(getResizedIcon(ImageIO.read(rs.getBlob("c_img").getBinaryStream()), 70, 70));
			}
			
			if(nameList.size()>0) {
				for(int i=0; i<imgList.size(); i++) {
					var panel = createcomp(new JPanel(new BorderLayout()), 70, 70);
					panel.setBorder(new LineBorder(Color.black));
					panel.add(new JLabel(imgList.get(i)));
					panel.add(new JLabel(nameList.get(i),JLabel.CENTER), BorderLayout.SOUTH);
					
					animationPanel.add(panel);
				}
				
				animationPanel.setBounds(1,1,nameList.size()*71,71);
				System.out.println(nameList.size());
			}else {
				eMsg("선택한 기업정보가 없습니다");
				cbProvince.setSelectedItem("전체");
				craeteAnimation("");
			}
			
			animationPanel.validate();
			animationPanel.repaint();
			
			thread = new Thread( ()->{
				try {
					while(true) {
						animationPanel.setBounds(animationPanel.getBounds().x-1, 1, nameList.size()*71, 71);
						
						Thread.sleep(10);
					}
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			});
			
			thread.start();
		} catch (Exception e) {
			
		}
	}
	public void updateSearch() {
		try {
			var rs = stmt.executeQuery("SELECT c_name,c_search from company order by c_search desc, c_no limit 5;");
			for(int i=0; rs.next(); i++) {
				lbCompany[i].setText(rs.getString(1));
				lbCount[i].setText(rs.getString(2));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		new MainFrame().setVisible(true);
	}

}
