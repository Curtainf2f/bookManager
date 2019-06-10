package windows;

import customException.CoustemExecption;
import manager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordWindow extends JFrame implements ActionListener {
    private JLabel oldPasswordL = new JLabel("旧密码:");
    private JLabel newPasswordL = new JLabel("新密码:");
    private JLabel confirmPasswordL = new JLabel("确认密码:");
    private JPasswordField oldPassword = new JPasswordField(10);
    private JPasswordField newPassword = new JPasswordField(10);
    private JPasswordField confirmPassword = new JPasswordField(10);
    private JButton confirm = new JButton("确认");
    private String userName;
    public ChangePasswordWindow(String userName){
        this.userName = userName;
        setLayout(new FlowLayout());
        add(oldPasswordL);
        add(oldPassword);
        add(newPasswordL);
        add(newPassword);
        add(confirmPasswordL);
        add(confirmPassword);
        add(confirm);
        oldPassword.addActionListener(this);
        newPassword.addActionListener(this);
        confirmPassword.addActionListener(this);
        confirm.addActionListener(this);
        setBounds(0,0,200,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("修改密码");
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        try{
            if(UserManager.checkPassword(new String(newPassword.getPassword())) != null){
                throw new CoustemExecption(UserManager.checkPassword(new String(newPassword.getPassword())));
            }
            if(!(new String(newPassword.getPassword()).equals(new String(confirmPassword.getPassword())))){
                throw new CoustemExecption("两次密码不匹配");
            }
            if(!UserManager.getPassword(userName).equals(new String(oldPassword.getPassword()))){
                throw new CoustemExecption("旧密码错误");
            }
            UserManager.setData(userName, 2, new String(newPassword.getPassword()));
            JOptionPane.showMessageDialog(this,"修改成功");
            dispose();
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
