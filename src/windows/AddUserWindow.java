package windows;

import manager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUserWindow extends JFrame implements ActionListener {
    private JLabel userNameLabel = new JLabel("用户名:");
    private JLabel passwordLabel = new JLabel("密码:");
    private JTextField userName = new JTextField(10);
    private JPasswordField password = new JPasswordField(10);
    private JButton confirm = new JButton("确认");
    public AddUserWindow(){
        setLayout(new FlowLayout());
        add(userNameLabel);
        add(userName);
        add(passwordLabel);
        add(password);
        add(confirm);
        userName.addActionListener(this);
        password.addActionListener(this);
        confirm.addActionListener(this);
        setBounds(0,0,200,150);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("添加用户");
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        try{
            UserManager.addData(userName.getText(), new String(password.getPassword()), null);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }
}
