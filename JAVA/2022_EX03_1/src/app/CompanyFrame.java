package app;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CompanyFrame extends BaseFrame {
	public CompanyFrame(int companyNo, boolean editMode) {
		super(editMode ? "�����������" : "���������",250,410);
		
		setBorderLayout();
		south.setLayout(new BorderLayout());
		north.setLayout(new BorderLayout());
		
		var rs = getPreparedResultSet("SELECT c_name, c_ceo, c_address, c_category, c_employee, c_img FROM company WHERE c_no = "+companyNo);
		
		try {
			rs.next();
			
			var lbImg = createLable(ImageIO.read(rs.getBlob("c_img").getBinaryStream()),240,160);
			north.add(lbImg);
			
			var lbList = "�����,��ǥ��,�ּ�,����,������".split(",");
			var tfList = new JTextField[lbList.length];
			
			for(int i=0; i<lbList.length; i++) {
				center.add(createcomp(new JLabel(lbList[i]), 65, 24));
				center.add(tfList[i]=new JTextField(12));
				tfList[i].setEditable(false); //�ԷºҰ��ؽ�Ʈ���󺯰�
				
				if(i==3)
					tfList[i].setText(noToStr(rs.getString(i+1)));
				else
					tfList[i].setText(rs.getString(i+1));
			}
			
			north.setBorder(new EmptyBorder(8,8,8,8));
			south.setBorder(new EmptyBorder(8,8,8,8));
			
			//����
			if(editMode) {
				tfList[1].setEditable(true);
				tfList[2].setEditable(true);
				
				var chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Images", "jpg")); //? ����
				
				lbImg.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2) {
							chooser.showOpenDialog(null);
							
							if(chooser.getSelectedFile() !=null) {
								try {
									lbImg.setIcon(getResizedIcon(ImageIO.read(chooser.getSelectedFile()), 240, 160));
				
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						}
					}
				});
				
				tfList[3].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						setVisible(false);
						//��������
						//var skillsFrame
					}
				});
				
				south.add(createButton("����", e->{
					if(Arrays.stream(tfList).anyMatch(tf -> tf.getText().length() ==0)) {
						eMsg("��ĭ�� �����մϴ�");
						return;
					}
					
					try {
						executeSQL("UPDATE company SET c_ceo = ?, c_address = ?, c_category = ? WHERE c_no = ?",
								tfList[1].getText(), tfList[2].getText(), strToNo(tfList[3].getText()), companyNo);
						
						if(chooser.getSelectedFile() != null) {
							Files.copy(chooser.getSelectedFile().toPath(),Paths.get("datafiles/���/"+tfList[0].getText()+"1.jpg"),StandardCopyOption.REPLACE_EXISTING);
							
							executeSQL("UPDATE company SET c_img = ? WHERE c_no=?", new FileInputStream(chooser.getSelectedFile()),companyNo);
						}
						
						iMsg("������ �Ϸ�Ǿ����ϴ�");
						dispose();
					}catch (Exception e2) {
						e2.printStackTrace();
					}
					

				}));
				
				
			}else {
				south.add(createButton("�ݱ�", e->dispose()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		new CompanyFrame(10, true).setVisible(true);
	}
}
