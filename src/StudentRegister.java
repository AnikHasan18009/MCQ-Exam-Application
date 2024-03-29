import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StudentRegister extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
//	public static void main(String[] args) {
//	
//				try {
//					StudentRegister frame = new StudentRegister();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					JFrame er= new JFrame();
//					er.setAlwaysOnTop(true);
//					JOptionPane.showMessageDialog(er,e);
//				}
//			}
		
	public StudentRegister() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(20, 120, 800, 400);
		setBounds(220, 130, 886, 463);
		setAlwaysOnTop(true);
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setForeground(SystemColor.activeCaptionText);
		contentPane.setBackground(new Color(0, 51, 102));
		
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Student ID");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(SystemColor.text);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(26, 54, 354, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setForeground(SystemColor.text);
		lblNewLabel_1.setBounds(496, 222, 354, 30);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBorder(null);
		textField.setBounds(26, 79, 354, 30);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBorder(null);
		passwordField.setBounds(496, 253, 354, 30);
		contentPane.add(passwordField);
		
		JCheckBox cbox = new JCheckBox("Show Password");
		cbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbox.isSelected()) {
					passwordField.setEchoChar((char)0);
				}
				else
					passwordField.setEchoChar('*');
			}
		});
		cbox.setForeground(SystemColor.text);
		cbox.setBackground(new Color(0, 51, 102));
		cbox.setBounds(626, 292, 112, 23);
		contentPane.add(cbox);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.setBorder(null);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection c=DBconnection.mysqlcon();	
					Statement s= c.createStatement();
					String id=textField.getText();
					String name=textField_1.getText();
					String dept=textField_2.getText();
					String ses=textField_3.getText();
					String email=textField_4.getText();
					String pass=passwordField.getText();
					
					//String regex="^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$";
					 String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
				                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
					Pattern emailPattern = Pattern.compile(emailRegex);
					
					ResultSet r=s.executeQuery("select * from students where id='"+id+"'");
					Statement s2= c.createStatement();
					ResultSet r2=s2.executeQuery("select * from students where email='"+email+"'");
					Statement s3= c.createStatement();
					ResultSet r3=s3.executeQuery("select * from `waiting approval` where id='"+id+"'");
				      if(email.equals("")||id.equals("") || name.equals("") || dept.equals("")||ses.equals("") || pass.equals(""))
				      {
				    	  JFrame er= new JFrame();
							er.setAlwaysOnTop(true);
							JOptionPane.showMessageDialog(er,"No field should be empty!","",JOptionPane.ERROR_MESSAGE);
				      }
					else if(r.next()) {
						JFrame er= new JFrame();
						er.setAlwaysOnTop(true);
						JOptionPane.showMessageDialog(er,"This student id is already registered!","",JOptionPane.ERROR_MESSAGE);
					}
					else if(r2.next()) {
						JFrame er= new JFrame();
						er.setAlwaysOnTop(true);
						JOptionPane.showMessageDialog(er,"This email is already registered!","",JOptionPane.ERROR_MESSAGE);
					}
					else if(!emailPattern.matcher(email).matches())
				      {
						JFrame er= new JFrame();
						er.setAlwaysOnTop(true);
						JOptionPane.showMessageDialog(er,"Please enter a valid email.","",JOptionPane.ERROR_MESSAGE);
				      }
				      
					else {
						if(r3.next())
						{
			
							Connection c1=DBconnection.mysqlcon();
							PreparedStatement st= c1.prepareStatement("delete from `waiting approval` where id='"+id+"' or email='"+email+"'");
							st.executeUpdate();
						}
						Connection c2=DBconnection.mysqlcon();
						PreparedStatement st= c2.prepareStatement("INSERT INTO `waiting approval` VALUES('"+id+"','"+name+"','"+dept+"','"+pass+"','"+ses+"','"+email+"')");
						st.executeUpdate();
						JFrame mess= new JFrame();
						mess.setAlwaysOnTop(true);
						JOptionPane.showMessageDialog(mess,"Check email for teacher's approval. ");
						setVisible(false);
						
					}
				}
					catch(Exception ex)
					{
						JFrame er= new JFrame();
						er.setAlwaysOnTop(true);
						JOptionPane.showMessageDialog(er,ex);
					}
			
				
			}
		});
		btnNewButton.setBackground(SystemColor.window);
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnNewButton.setForeground(new Color(0, 51, 102));
		btnNewButton.setBounds(456, 401, 157, 23);
		contentPane.add(btnNewButton);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 102, 0));
		panel.setBounds(0, 0, 886, 30);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("X");
		btnNewButton_1.setFocusable(false);
		btnNewButton_1.setBounds(846, 0, 30, 30);
		panel.add(btnNewButton_1);
		btnNewButton_1.setBorder(null);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_1.setForeground(SystemColor.text);
		btnNewButton_1.setBackground(new Color(255, 153, 0));
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBorder(null);
		textField_1.setBounds(496, 79, 354, 30);
		contentPane.add(textField_1);
		
		JLabel lblStudentName = new JLabel("Student Name");
		lblStudentName.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudentName.setForeground(Color.WHITE);
		lblStudentName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStudentName.setBounds(496, 54, 354, 14);
		contentPane.add(lblStudentName);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBorder(null);
		textField_2.setBounds(26, 164, 354, 30);
		contentPane.add(textField_2);
		
		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepartment.setForeground(Color.WHITE);
		lblDepartment.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDepartment.setBounds(26, 139, 354, 14);
		contentPane.add(lblDepartment);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBorder(null);
		textField_3.setBounds(496, 164, 354, 30);
		contentPane.add(textField_3);
		
		JLabel lblSession = new JLabel("Session");
		lblSession.setHorizontalAlignment(SwingConstants.CENTER);
		lblSession.setForeground(Color.WHITE);
		lblSession.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSession.setBounds(496, 139, 354, 14);
		contentPane.add(lblSession);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
				passwordField.setText("");
			}
		});
		btnClear.setForeground(new Color(0, 51, 102));
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnClear.setBorder(null);
		btnClear.setBackground(Color.WHITE);
		btnClear.setBounds(259, 401, 157, 23);
		contentPane.add(btnClear);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBorder(null);
		textField_4.setBounds(26, 253, 354, 30);
		contentPane.add(textField_4);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEmail.setBounds(26, 232, 354, 14);
		contentPane.add(lblEmail);
	}
}
