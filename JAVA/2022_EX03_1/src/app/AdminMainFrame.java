package app;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class AdminMainFrame extends BaseFrame{
	
	String focusName;
	public AdminMainFrame() {
		super("관리자 메인", 600, 620);
		
		setBorderLayout();
		addPrevForm(() -> new LoginFrame().setVisible(true));
		
		var rs = getPreparedResultSet("SELECT * FROM company");
		
		try {
			while (rs.next()){
				int comNo = rs.getInt("c_no");
				String name = rs.getString("c_name");
				var lbImg = createcomp(new JLabel("") {
					//??
					protected void paintComponent(java.awt.Graphics g) {
						Graphics2D g2d = (Graphics2D)g;
						
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, focusName==name ? 1 : 0.2f));
						
						super.paintComponent(g);
					};
				},100,100);
				
				lbImg.setIcon(getResizedIcon(ImageIO.read(rs.getBlob("c_img").getBinaryStream()), 100, 100));
				lbImg.setBorder(new LineBorder(Color.black));
				lbImg.setToolTipText(rs.getString("c_name"));
				
				lbImg.addMouseListener(new MouseAdapter() {
				@Override
					public void mouseEntered(MouseEvent e) { //마우스가 영역 안으로 들어올때
						focusName = name;
						lbImg.repaint();
					}
				
					public void mouseExited(MouseEvent e) {
						focusName = null;
						lbImg.repaint();
					};
					
					public void mouseClicked(MouseEvent e) {
						disposewithRemovingPrevForm();
						new CompanyFrame(comNo, true).addPrevForm(()->new AdminMainFrame().setVisible(true)).setVisible(true);
					};
				});
				
				center.add(lbImg);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		south.add(createButton("채용 정보",  e->{
			disposewithRemovingPrevForm();
			new AdminJobInfoFrame().setVisible(true);
		}));
		south.add(createButton("지원자 목록", e -> {
			disposewithRemovingPrevForm();
			new AdminApplicantInfoFrame().setVisible(true);
		}));
		south.add(createButton("공고 등록", e -> {
			disposewithRemovingPrevForm();
			new AdminEmploymentFrame(0)
				.addPrevForm(() -> new AdminMainFrame().setVisible(true))
				.setVisible(true);
		}));
		south.add(createButton("지원자 분석", e -> {
			disposewithRemovingPrevForm();
			new AdminChartFrame().setVisible(true);
		}));
		south.add(createButton("닫기", e -> dispose()));
	}
	public static void main(String[] args) {
		new AdminMainFrame().setVisible(true);
	}

}