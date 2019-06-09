package windows;

import manager.BookManager;
import manager.BookTypeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;

public class AddBookWindow extends JFrame implements ActionListener {
    private JLabel typeL = new JLabel("书本类型:");
    private JLabel nameL = new JLabel("书名:");
    private JLabel authorL = new JLabel("作者:");
    private JLabel publishL = new JLabel("出版社:");
    private JLabel publishDateL = new JLabel("出版日期:");
    private JLabel publishTimesL = new JLabel("印刷次数:");
    private JLabel priceL = new JLabel("价格:");
    private JComboBox type;
    private JTextField name = new JTextField(13);
    private JTextField author = new JTextField(13);
    private JTextField publish = new JTextField(11);
    private JTextField publishDate = new JTextField(10);
    private JTextField publishTimes = new JTextField(10);
    private JTextField price = new JTextField(13);
    private JButton confirm = new JButton("确认");
    public AddBookWindow() throws Exception{
        setLayout(new FlowLayout());
        ResultSet rs = BookTypeManager.getData();
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
        add(typeL);
        add(type);
        add(nameL);
        add(name);
        add(authorL);
        add(author);
        add(publishL);
        add(publish);
        add(publishDateL);
        add(publishDate);
        add(publishTimesL);
        add(publishTimes);
        add(priceL);
        add(price);
        add(confirm);
        name.addActionListener(this);
        author.addActionListener(this);
        publish.addActionListener(this);
        publishDate.addActionListener(this);
        publishTimes.addActionListener(this);
        price.addActionListener(this);
        confirm.addActionListener(this);
        setBounds(0,0,200,350);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("添加书本");
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        try{
            BookManager.addData((Integer) BookTypeManager.getData((String)type.getSelectedItem(), 1), name.getText(), author.getText(), publish.getText(), Date.valueOf(publishDate.getText()), Integer.valueOf(publishTimes.getText()), Integer.valueOf(price.getText()));
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }
}
