package windows;

import database.Database;
import readerManager.ReaderManager;
import readerTypeManager.ReaderTypeManager;
import userManager.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class AdminWindow extends JFrame {
    private JScrollPane usersScroll = new JScrollPane(), readerTypeScroll = new JScrollPane();
    private JTable usersTable, readerTypeTable;
    private ResultSet users, readerType;
    private JButton addUserButton, addReaderTypeButton;
    private JLabel readerName = new JLabel("姓名");
    public AdminWindow(String title){
        setLayout(null);
        init();
        setVisible(true);
        setTitle(title);
        setBounds(0,0,1024,768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void init(){
        try{
            Component window = this;
            addUserButton = new JButton("添加");
            add(addUserButton);
            addUserButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addUser(window);
                }
            });
            addUserButton.setBounds(50,50,200,100);
            addUserButton.setVisible(false);
            addReaderTypeButton = new JButton("添加");
            add(addReaderTypeButton);
            addReaderTypeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try{
                        ReaderTypeManager.addData(JOptionPane.showInputDialog(window,"请输入需要添加的读者类型", "添加读者类型", JOptionPane.INFORMATION_MESSAGE));
                        readerType = ReaderTypeManager.getData();
                        readerTableTypeReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            addReaderTypeButton.setBounds(846,50,140,100);
            addReaderTypeButton.setVisible(false);
            createReaderInfoBox();
            usersScroll.setBounds(0, 0, 300, 200);
            readerTypeScroll.setBounds(824,0, 200, 200);
            users = UserManager.getData();
            readerType = ReaderTypeManager.getData();
            tableReload();
            add(usersScroll);
            add(readerTypeScroll);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void tableReload(){
        usersTableReload();
        readerTableTypeReload();
    }
    private void usersTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(users);
            users.last();
            int n = users.getRow();
            for(int i = 0; i < n; i ++){
                Object name = ReaderManager.getReaderName((Integer) tmp[i][2]);
                if(name == null) tmp[i][2] = "未绑定读者信息";
                else tmp[i][2] += "(" + name + ")";
            }
            usersTable = new JTable(tmp, new String[]{"用户名","密码","读者姓名"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            users.last();
            if (users.getRow() == 0){
                addUserButton.setVisible(true);
            }else{
                addUserButton.setVisible(false);
            }
            usersTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK){
                        ((AdminWindow)window).setTitle(usersTable.getSelectedRow() + "," + usersTable.getSelectedColumn());
                    }else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                        try{
                            usersTableRightClick(e.getComponent(),e.getX(), e.getY(), e.getSource(), usersTable.getSelectedRow() + 1, usersTable.getSelectedColumn() + 1);
                        }catch (Exception ec){ec.printStackTrace();}
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            usersTable .getTableHeader().setReorderingAllowed(false);
            usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            usersScroll.getViewport().add(usersTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private Box readerInfoBox=Box.createVerticalBox();
    private JLabel readerTypeLabel = new JLabel("读者类型:");
    private JLabel readerNameLabel = new JLabel("姓名:");
    private JLabel readerAgeLabel = new JLabel("年龄:");
    private JLabel readerSexLabel = new JLabel("性别:");
    private JLabel readerPhoneLabel = new JLabel("手机:");
    private JLabel readerDeptLabel = new JLabel("院系:");
    private JLabel readerDateLabel = new JLabel("注册时间:");
    private JLabel readerTypeShow = new JLabel("              ");
    private JLabel readerNameShow = new JLabel("              ");
    private JLabel readerAgeShow = new JLabel("              ");
    private JLabel readerSexShow = new JLabel("              ");
    private JLabel readerPhoneShow = new JLabel("              ");
    private JLabel readerDeptShow = new JLabel("              ");
    private JLabel readerDateShow = new JLabel("              ");
    private JButton readerChange = new JButton("修改");
    private JLabel nonReader = new JLabel("没有读者信息");
    private void createReaderInfoBox(){
        Box boxV1 = Box.createVerticalBox();
        boxV1.add(readerTypeLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerNameLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerAgeLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerSexLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerPhoneLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerDeptLabel);
        boxV1.add(Box.createVerticalStrut(8));
        boxV1.add(readerDateLabel);
        Box boxV2 = Box.createVerticalBox();
        boxV2.add(readerTypeShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerNameShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerAgeShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerSexShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerPhoneShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerDeptShow);
        boxV2.add(Box.createVerticalStrut(8));
        boxV2.add(readerDateShow);
        Box boxV3 = Box.createHorizontalBox();
        boxV3.add(boxV1);
        boxV3.add(Box.createHorizontalStrut(10));
        boxV3.add(boxV2);
        readerInfoBox.add(boxV3);
        readerInfoBox.add(Box.createVerticalStrut(8));
        readerInfoBox.add(readerChange);
        readerInfoBox.setBounds(300,0,300,200);
        add(readerInfoBox);
        readerInfoBox.setVisible(false);
        add(nonReader);
        nonReader.setBounds(400,100,300,50);
    }
    private void readerInfo(Integer readerId){
        try{
            if (ReaderManager.getData(readerId, 1) != null){

                nonReader.setVisible(false);
                readerInfoBox.setVisible(true);
            }else{
                nonReader.setVisible(true);
                readerInfoBox.setVisible(false);
            }
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void readerTableTypeReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(readerType);
            readerTypeTable = new JTable(tmp, new String[]{"读者类型编号","读者类型"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            readerType.last();
            if (readerType.getRow() == 0){
                addReaderTypeButton.setVisible(true);
            }else{
                addReaderTypeButton.setVisible(false);
            }
            readerTypeTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK){
                        ((AdminWindow)window).setTitle(readerTypeTable.getSelectedRow() + "," + readerTypeTable.getSelectedColumn());
                    }else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                        try{
                            readerTypeTableRightClick(e.getComponent(),e.getX(), e.getY(), e.getSource(), readerTypeTable.getSelectedRow() + 1, readerTypeTable.getSelectedColumn() + 1);
                        }catch (Exception ec){ec.printStackTrace();}
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            readerTypeTable.getTableHeader().setReorderingAllowed(false);
            readerTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            readerTypeScroll.getViewport().add(readerTypeTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void addUser(Component window){
        new AddUserWindow().addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {
                try{
                    users = UserManager.getData();
                    tableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void boundReader(String userName){
        Component window = this;
        try {
            new BoundReaderWindow(userName).addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) { }
                public void windowClosing(WindowEvent e) { }
                public void windowClosed(WindowEvent e) { }
                public void windowIconified(WindowEvent e) { }
                public void windowDeiconified(WindowEvent e) { }
                public void windowActivated(WindowEvent e) {}
                public void windowDeactivated(WindowEvent e) {
                    try{
                        users = UserManager.getData();
                        tableReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }catch (Exception ec){
            JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void usersTableRightClick(Component cp, int x, int y, Object table, int selectRow, int selectCol){
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem add = new JMenuItem("添加");
        JMenuItem set = new JMenuItem("修改");
        JMenuItem del = new JMenuItem("删除");
        JMenuItem boundReader = new JMenuItem("绑定读者");
        tablerightClickShow.add(add);
        tablerightClickShow.add(set);
        tablerightClickShow.add(del);
        tablerightClickShow.add(boundReader);
        Component window = this;
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser(window);
            }
        });
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(selectCol == 3){
                    JOptionPane.showMessageDialog(window, "读者信息需要绑定不可直接修改", "错误", JOptionPane.WARNING_MESSAGE);
                }else {
                    try{
                        Object change = JOptionPane.showInputDialog(window, "修改" + usersTable.getValueAt(selectRow-1, 0) + "的" + usersTable.getColumnName(selectCol-1), "修改用户", JOptionPane.INFORMATION_MESSAGE);
                        users = Database.setData(users, selectRow, selectCol, change);
                        tableReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Database.delData(users, selectRow);
                    users = UserManager.getData();
                    tableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        boundReader.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    boundReader((String) usersTable.getValueAt(selectRow-1, 0));
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        tablerightClickShow.show(cp, x, y);
    }
    private void readerTypeTableRightClick(Component cp, int x, int y, Object table, int selectRow, int selectCol){
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem add = new JMenuItem("添加");
        JMenuItem set = new JMenuItem("修改");
        JMenuItem del = new JMenuItem("删除");
        tablerightClickShow.add(add);
        tablerightClickShow.add(set);
        tablerightClickShow.add(del);
        Component window = this;
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    ReaderTypeManager.addData(JOptionPane.showInputDialog(window,"请输入需要添加的读者类型", "添加读者类型", JOptionPane.INFORMATION_MESSAGE));
                    readerType = ReaderTypeManager.getData();
                    readerTableTypeReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Object change = JOptionPane.showInputDialog(window, "修改" + readerTypeTable.getValueAt(selectRow-1, 0) + "的" + readerTypeTable.getColumnName(selectCol-1), "修改", JOptionPane.INFORMATION_MESSAGE);
                    readerType = Database.setData(readerType, selectRow, selectCol, change);
                    readerTableTypeReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    readerType.absolute(selectRow);
                    ReaderTypeManager.delData(readerType.getInt(1));
                    readerType = ReaderTypeManager.getData();
                    readerTableTypeReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        tablerightClickShow.show(cp, x, y);
    }
}
