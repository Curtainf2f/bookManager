package windows;
import customException.CoustemExecption;
import manager.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends JFrame implements ActionListener{
    private JButton reg = new JButton("注册"), exit = new JButton("退出");
    private JTextField username = new JTextField(10);
    private JPasswordField password = new JPasswordField(10);
    private JPasswordField confirmPassword = new JPasswordField(10);
    public RegisterWindow(String title){
        setLayout(new FlowLayout());
        init();
        setVisible(true);
        setTitle(title);
        setBounds(100,100,260,160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    private void init(){
        Box boxV1 = Box.createVerticalBox(), boxV2 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(new JLabel("密码："));
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(new JLabel("确认密码："));
        boxV2.add(username);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(password);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(confirmPassword);
        Box baseBoxV1 = Box.createHorizontalBox();
        baseBoxV1.add(boxV1);
        baseBoxV1.add(Box.createHorizontalStrut(10));
        baseBoxV1.add(boxV2);
        Box boxV3 = Box.createHorizontalBox();
        boxV3.add(reg);
        boxV3.add(Box.createHorizontalStrut(10));
        boxV3.add(exit);
        Box baseBoxV2 = Box.createHorizontalBox();
        baseBoxV2.add(boxV3);
        Box baseBoxV3 = Box.createVerticalBox();
        baseBoxV3.add(baseBoxV1);
        baseBoxV3.add(Box.createVerticalStrut(8));
        baseBoxV3.add(baseBoxV2);
        username.addActionListener(this);
        password.addActionListener(this);
        confirmPassword.addActionListener(this);
        reg.addActionListener(this);
        exit.addActionListener(this);
        add(baseBoxV3);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == reg || e.getSource() == username || e.getSource() == password || e.getSource() == confirmPassword){
            try{
                if (!(new String(password.getPassword()).equals(new String(confirmPassword.getPassword()))))
                    throw new CoustemExecption("两次密码不匹配");
                UserManager.addData(username.getText(), new String(password.getPassword()),null);
                JOptionPane.showMessageDialog(this, "注册成功", "注册成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }catch (Exception ec){
                JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
            }
        }else if(e.getSource() == exit){
            dispose();
        }
    }
}