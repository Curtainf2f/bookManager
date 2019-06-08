package windows;

import readerTypeManager.ReaderTypeManager;
import userManager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class BoundReaderWindow extends JFrame implements ActionListener {
    private JLabel nameL = new JLabel("姓名:");
    private JLabel typeL = new JLabel("读者类型:");
    private JLabel ageL = new JLabel("年龄:");
    private JLabel sexL = new JLabel("性别:");
    private JLabel phoneL = new JLabel("电话号码:");
    private JLabel deptL = new JLabel("院系:");
    private JTextField name = new JTextField(13);
    private JComboBox type;
    private JTextField age = new JTextField(13);
    private JTextField sex = new JTextField(13);
    private JTextField phone = new JTextField(10);
    private JTextField dept = new JTextField(13);
    private JButton confirm = new JButton("确认");
    private String userName;
    public BoundReaderWindow(String userName) throws Exception{
        this.userName = userName;
        setLayout(new FlowLayout());
        ResultSet rs = ReaderTypeManager.getData();
        rs.last();
        int m = rs.getRow();
        Object[] tmp = new Object[m+1];
        tmp[0] = "-----请选择-----";
        rs.beforeFirst();
        for(int i = 1; i <= m; i ++){
            rs.next();
            tmp[i] = rs.getObject(2);
        }
        type = new JComboBox(tmp);
        add(nameL);
        add(name);
        add(typeL);
        add(type);
        add(ageL);
        add(age);
        add(sexL);
        add(sex);
        add(phoneL);
        add(phone);
        add(deptL);
        add(dept);
        add(confirm);
        name.addActionListener(this);
        age.addActionListener(this);
        sex.addActionListener(this);
        phone.addActionListener(this);
        dept.addActionListener(this);
        confirm.addActionListener(this);
        setBounds(0,0,200,350);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("绑定读者");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        try{
            UserManager.boundReader(userName, ReaderTypeManager.getReaderTypeId((String) type.getSelectedItem()), name.getText(), Integer.valueOf(age.getText()), sex.getText(), phone.getText(), dept.getText());
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }
}
