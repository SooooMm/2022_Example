package app;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import app.BaseFrame;
import sesssion.Session;

public class MyPageFrame extends BaseFrame{
	DefaultTableModel dtm = new DefaultTableModel("no,번호,기업명,모집정보,시급,모집정원,최종학력,성별,합격여부".split(","), 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			// 셀 수정 막기
			return false;
		}
	};
	
	public MyPageFrame() {
		super("Mypage", 700, 400);
		
		setBorderLayout();
		north.add(createLable(new JLabel("Mypage"), new Font("HY헤드라인M", 1, 24)));
		north.setBorder(new EmptyBorder(10, 10, 10, 10));
		center.setLayout(null);
		
		center.add(createcomp(createLable(new JLabel("성명 : "+Session.userName), new Font("굴림",1,18)), 15, 0,200,30));
		center.add(createcomp(createLable(new JLabel("성별 : "+gender[Session.userGender]), new Font("굴림",1,16)), 15, 30,200,30));
		center.add(createcomp(createLable(new JLabel("최종학력 : "+ gender[Session.userGraduate]), new Font("굴림",1,16)), 15, 60,200,30));
		
		var table = new JTable(dtm);
		var cellRender = new DefaultTableCellRenderer();
		
		cellRender.setHorizontalAlignment(0);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.removeColumn(table.getColumn("no"));
		int[] widthList = {40, 100, 250, 80, 80, 120, 60, 60};
		
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		for(int i=0; i<table.getColumnCount(); i++) {
			var col = table.getColumn(table.getColumnName(i));
			col.setCellRenderer(cellRender);
			col.setPreferredWidth(widthList[i]);
		}
		
		var popupMenu = new JPopupMenu();
		var menuItem = new JMenuItem("삭제");
		
		menuItem.addActionListener(e ->{
			int no = (Integer)dtm.getValueAt(table.getSelectedRow(), 0);
			executeSQL("DELETE FROM applicant WHERE a_no = ", +no);
			iMsg("삭제가 완료되었습니다.");
			updateTable();
		});
		
		popupMenu.add(menuItem);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //하나만 선택
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {//왼쪽,휠,오른쪽
					int row = table.rowAtPoint(new Point(e.getX(),e.getY()));
					table.addRowSelectionInterval(row, row);
					popupMenu.show(table, e.getX(), e.getY());
				} 
				
			}
		});
		
		center.add(createcomp(new JScrollPane(table), 15, 90, 650, 180));
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south.add(createButton("PDF 인쇄", e->{
			try {
				table.print();
			} catch (PrinterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
		
		updateTable();
	}
	void updateTable() {
		dtm.setRowCount(0);
		
		var rs = getPreparedResultSet("SELECT a_no, c.c_name, e.e_title, e.e_pay, e.e_people, e.e_graduate, e.e_gender, a.a_apply\r\n"
				+ "FROM 2022_ex03_1.applicant a\r\n"
				+ "INNER JOIN employment e ON a.e_no = e.e_no\r\n"
				+ "INNER JOIN company c ON e.c_no = c.c_no\r\n"
				+ "WHERE a.u_no = " + Session.userNo); 
		
		
		try {
			int no =0;
			while (rs.next()) {
				dtm.addRow(new Object[] {
						rs.getInt("a_no"),
						++no,
						rs.getString("c_name"),
						rs.getString("e_title"),
						String.format("%,d", rs.getInt("e_pay")),
						rs.getInt("e_people"),
						grad[rs.getInt("e_graduate")],
						gender[rs.getInt("e_gender")],
						apply[rs.getInt("a_apply")]
				});
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String args[]) {
		Session.userNo = 1;
		Session.userName = "이솜";
		Session.userGender = 2;
		Session.userGraduate = 2;
		
		new MyPageFrame().setVisible(true);
	}
}
