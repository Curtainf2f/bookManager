package windows;
import manager.UserManager;
import manager.AdminManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ActionListener{
    private JButton login = new JButton("登录"), reg = new JButton("注册");
    private JButton adminLogin = new JButton("管理员登录");
    private JTextField username = new JTextField(10);
    private JPasswordField password = new JPasswordField(10);
    public LoginWindow(String title){
        setLayout(new FlowLayout());
        init();
        setVisible(true);
        setTitle(title);
        setBounds(100,100,260,130);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void init(){
        Box boxV1 = Box.createVerticalBox(), boxV2 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(new JLabel("密码："));
        boxV2.add(username);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(password);
        Box baseBoxV1 = Box.createHorizontalBox();
        baseBoxV1.add(boxV1);
        baseBoxV1.add(Box.createHorizontalStrut(10));
        baseBoxV1.add(boxV2);
        Box boxV3 = Box.createHorizontalBox();
        boxV3.add(login);
        boxV3.add(Box.createHorizontalStrut(10));
        boxV3.add(reg);
        boxV3.add(Box.createHorizontalStrut(10));
        boxV3.add(adminLogin);
        Box baseBoxV2 = Box.createHorizontalBox();
        baseBoxV2.add(boxV3);
        Box baseBoxV3 = Box.createVerticalBox();
        baseBoxV3.add(baseBoxV1);
        baseBoxV3.add(Box.createVerticalStrut(8));
        baseBoxV3.add(baseBoxV2);
        username.addActionListener(this);
        password.addActionListener(this);
        login.addActionListener(this);
        adminLogin.addActionListener(this);
        reg.addActionListener(this);
        add(baseBoxV3);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == login || e.getSource() == username || e.getSource() == password){
            try{
                String cur = UserManager.getPassword(username.getText());
                if(cur != null && cur.equals(new String(password.getPassword()))){
                    JOptionPane.showMessageDialog(this, "登录成功", "登录成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "用户名或密码错误", "错误", JOptionPane.WARNING_MESSAGE);
                }
            }catch (Exception ec){
                JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
            }
        }else if(e.getSource() == reg){
            new RegisterWindow("注册");
        }else if(e.getSource() == adminLogin){
            try{
                String cur = AdminManager.getPassword(username.getText());
                if(cur != null && cur.equals(new String(password.getPassword()))){
                    JOptionPane.showMessageDialog(this, "登录成功", "登录成功", JOptionPane.INFORMATION_MESSAGE);
                    new AdminWindow("图书管理系统（管理员模式）");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "用户名或密码错误", "错误", JOptionPane.WARNING_MESSAGE);
                }
            }catch (Exception ec){
                JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}