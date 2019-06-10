package windows;

import manager.*;
import customException.CoustemExecption;
import database.Database;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

class UserPane extends JPanel{
    private Font font = new Font("微软雅黑", Font.PLAIN, 20);
    private ResultSet users, bookBorrow;
    private JTable usersTable, bookBorrowTable;
    private JScrollPane  usersScroll = new JScrollPane(), bookBorrowScroll = new JScrollPane();
    private Box readerInfoBox=Box.createVerticalBox();
    private JLabel readerTypeShow = new JLabel("");
    private JLabel readerNameShow = new JLabel("");
    private JLabel readerAgeShow = new JLabel("");
    private JLabel readerSexShow = new JLabel("");
    private JLabel readerPhoneShow = new JLabel("");
    private JLabel readerDeptShow = new JLabel("");
    private JLabel readerDateShow = new JLabel("");
    private JLabel nonReader = new JLabel("该用户没有读者信息");
    private JButton addUserButton;
    public UserPane() throws Exception{
        setLayout(null);
        JPanel borrowTitleBox = new JPanel();
        JLabel borrowTitle = new JLabel("读者借阅信息");
        borrowTitle.setFont(new Font("宋体",Font.BOLD, 40));
        borrowTitle.setForeground(Color.red);
        borrowTitleBox.add(borrowTitle);
        borrowTitleBox.setBounds(0,350,980,50);
        usersScroll.setBounds(0,0, 580, 350);
        bookBorrowScroll.setBounds(0,400,980, 200);
        createAddUserButton();
        createReaderInfoBox();
//        readerBox.setBounds(580,0,400,300);
        reload();
        add(borrowTitleBox);
        add(usersScroll);
        add(bookBorrowScroll);
    }
    public void reload() throws Exception{
        users = UserManager.getData();
        bookBorrow = BorrowManager.getData();
        usersTableReload();
        bookBorrowTableReload();
    }
    private void createAddUserButton(){
        Component window = this;
        addUserButton = new JButton("添加");
        addUserButton.setFont(font);
        add(addUserButton);
        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser(window);
            }
        });
        addUserButton.setBounds(200,100,200,100);
        addUserButton.setVisible(false);
    }
    private void createReaderInfoBox(){
        Component window = this;
        JPanel readerBox = new JPanel();
        readerBox.add(readerInfoBox);
        readerBox.add(nonReader);
        readerBox.setBounds(580,0,400,350);
        readerBox.setBorder(BorderFactory.createTitledBorder("读者信息"));
        add(readerBox);
        JLabel readerTypeLabel = new JLabel("读者类型:");
        JLabel readerNameLabel = new JLabel("姓名:");
        JLabel readerAgeLabel = new JLabel("年龄:");
        JLabel readerSexLabel = new JLabel("性别:");
        JLabel readerPhoneLabel = new JLabel("手机:");
        JLabel readerDeptLabel = new JLabel("院系:");
        JLabel readerDateLabel = new JLabel("注册时间:");
        JButton readerChange = new JButton("修改");
        readerTypeShow.setFont(font);
        readerNameShow.setFont(font);
        readerAgeShow.setFont(font);
        readerSexShow.setFont(font);
        readerPhoneShow.setFont(font);
        readerDeptShow.setFont(font);
        readerDateShow.setFont(font);
        readerTypeLabel.setFont(font);
        readerNameLabel.setFont(font);
        readerAgeLabel.setFont(font);
        readerSexLabel.setFont(font);
        readerPhoneLabel.setFont(font);
        readerDeptLabel.setFont(font);
        readerDateLabel.setFont(font);
        readerChange.setFont(font);
        nonReader.setFont(new Font("微软雅黑",Font.BOLD, 25));
        readerChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    int row = usersTable.getSelectedRow()+1;
                    users.absolute(row);
                    boundReader(users.getString(1));
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
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
        readerInfoBox.setVisible(false);
    }
    private void readerInfo(Integer readerId){
        try{
            if (ReaderManager.getData(readerId, 1) != null){
                ResultSet rs = ReaderManager.getData(readerId);
                rs.last();
                readerTypeShow.setText((String) ReaderTypeManager.getData(rs.getInt(2), 2));
                readerNameShow.setText(rs.getString(3));
                readerAgeShow.setText(rs.getString(4));
                readerSexShow.setText(rs.getString(5));
                readerPhoneShow.setText(rs.getString(6));
                readerDeptShow.setText(rs.getString(7));
                readerDateShow.setText(rs.getString(8));
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
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void boundReader(String userName) throws Exception{
        Component window = this;
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
                    readerInfo(UserManager.getReaderId(userName));
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
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
            usersTable = new JTable(tmp, new String[]{"用户名","密码","读者编号(读者姓名)"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            usersTable.getTableHeader().setPreferredSize(new Dimension(usersTable.getTableHeader().getWidth(), 30));
            usersTable.setRowHeight(30);
            usersTable.getTableHeader().setFont(font);
            usersTable.setFont(font);
            users.last();
            if (users.getRow() == 0){
                addUserButton.setVisible(true);
            }else{
                addUserButton.setVisible(false);
            }
            usersTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK){
                            int row = usersTable.getSelectedRow()+1;
                            users.absolute(row);
                            readerInfo(users.getInt(3));
                            bookBorrow = BorrowManager.getDataUseReaderId(users.getInt(3));
                            bookBorrowTableReload();
                        }else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            usersTableRightClick(e.getComponent(),e.getX(), e.getY(), usersTable.getSelectedRow() + 1, usersTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
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
            ec.printStackTrace();
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookBorrowTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(bookBorrow);
            bookBorrow.last();
            int n = bookBorrow.getRow();
            for(int i = 0; i < n; i ++){
                tmp[i][0] += ("(" + ReaderManager.getReaderName(Integer.valueOf(tmp[i][0].toString())) + ")");
                tmp[i][1] += ("(" + BookManager.getData(Integer.valueOf(tmp[i][1].toString()), 3) + ")");
            }
            bookBorrowTable = new JTable(tmp, new String[]{"读者编号","书本编号","借阅日期","归还日期"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookBorrowTable.getTableHeader().setPreferredSize(new Dimension(bookBorrowTable.getTableHeader().getWidth(), 30));
            bookBorrowTable.setRowHeight(30);
            bookBorrowTable.getTableHeader().setFont(font);
            bookBorrowTable.setFont(font);
            bookBorrowTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            bookBorrowTableRightClick(e.getComponent(),e.getX(), e.getY(), bookBorrowTable.getSelectedRow() + 1, bookBorrowTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            bookBorrowTable .getTableHeader().setReorderingAllowed(false);
            bookBorrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookBorrowScroll.getViewport().add(bookBorrowTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void usersTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem add = new JMenuItem("添加");
        JMenuItem set = new JMenuItem("修改");
        JMenuItem del = new JMenuItem("删除");
        JMenuItem boundReader = new JMenuItem("绑定读者");
        JMenuItem unBound = new JMenuItem("解绑");
        tablerightClickShow.add(add);
        tablerightClickShow.add(set);
        tablerightClickShow.add(del);
        tablerightClickShow.add(boundReader);
        tablerightClickShow.add(unBound);
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
                        usersTableReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    users.absolute(selectRow);
                    UserManager.delData(users.getString(1));
                    users = UserManager.getData();
                    usersTableReload();
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
        unBound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    UserManager.unBoundReader((String) usersTable.getValueAt(selectRow-1, 0));
                    users.absolute(selectRow);
                    readerInfo(users.getInt(3));
                    users = UserManager.getData();
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchUserName = new JMenuItem("搜索用户名");
        JMenuItem searchreaderId = new JMenuItem("搜索读者编号");
        tablerightClickShow.add(search);
        search.add(searchUserName);
        search.add(searchreaderId);
        searchUserName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    users = Database.searchData("users", "userName", JOptionPane.showInputDialog("搜索用户名"));
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchreaderId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    users = Database.getData("users", "readerId",Integer.valueOf(JOptionPane.showInputDialog("搜索读者编号")));
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    users = UserManager.getData();
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add.setFont(font);
        set.setFont(font);
        del.setFont(font);
        boundReader.setFont(font);
        unBound.setFont(font);
        search.setFont(font);
        searchreaderId.setFont(font);
        searchUserName.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    private void bookBorrowTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem del = new JMenuItem("归还");
        tablerightClickShow.add(del);
        Component window = this;
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookBorrow.absolute(selectRow);
                    BorrowManager.delData(bookBorrow.getInt(1), bookBorrow.getInt(2));
                    bookBorrow = BorrowManager.getData();
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchReaderId = new JMenuItem("搜索读者编号");
        JMenuItem searchISBN = new JMenuItem("搜索书本编号");
        tablerightClickShow.add(search);
        search.add(searchISBN);
        search.add(searchReaderId);
        searchReaderId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookBorrow = Database.getData("borrowBook", "readerId",Integer.valueOf(JOptionPane.showInputDialog("搜索读者编号")));
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchISBN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookBorrow = Database.getData("borrowBook", "ISBN",Integer.valueOf(JOptionPane.showInputDialog("搜索书本编号")));
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookBorrow = BorrowManager.getData();
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.setFont(font);
        search.setFont(font);
        searchISBN.setFont(font);
        searchReaderId.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
}
class ReaderPane extends JPanel{
    private Font font = new Font("微软雅黑", Font.PLAIN, 20);
    private JScrollPane readerTypeScroll = new JScrollPane(), readerScroll = new JScrollPane();
    private JTable readerTable, readerTypeTable;
    private ResultSet reader, readerType;
    private JButton addReaderTypeButton;
    public ReaderPane() throws Exception{
        setLayout(null);
        JPanel helpBox = new JPanel();
        JLabel help = new JLabel("点击读者类型会显示所有该类型的读者");
        help.setFont(new Font("宋体", Font.BOLD, 50));
        helpBox.add(help);
        helpBox.setBounds(0,500,980,100);
        readerScroll.setBounds(0,0, 780, 500);
        readerTypeScroll.setBounds(780,0,200, 500);
        createAddReaderTypeButton();
        reload();
        add(helpBox);
        add(readerScroll);
        add(readerTypeScroll);
    }
    public void reload() throws Exception{
        reader = ReaderManager.getData();
        readerType = ReaderTypeManager.getData();
        readerTableReload();
        readerTypeTableReload();
    }
    private void createAddReaderTypeButton(){
        Component window = this;
        addReaderTypeButton = new JButton("添加");
        addReaderTypeButton.setFont(font);
        add(addReaderTypeButton);
        addReaderTypeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    ReaderTypeManager.addData(JOptionPane.showInputDialog(window,"请输入需要添加的读者类型", "添加读者类型", JOptionPane.INFORMATION_MESSAGE));
                    readerType = ReaderTypeManager.getData();
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        addReaderTypeButton.setBounds(820,200,100,100);
        addReaderTypeButton.setVisible(false);
    }
    private void readerTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(reader);
            reader.last();
            int n = reader.getRow();
            for(int i = 0; i < n; i ++){
                String name = (String) ReaderTypeManager.getData((Integer) tmp[i][1], 2);
                if(name == null) tmp[i][1] = "无法识别";
                else tmp[i][1] = name;
            }
            readerTable = new JTable(tmp, new String[]{"读者编号","读者类型","读者姓名","年龄","性别","电话","院系","注册日期"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            readerTable.getTableHeader().setPreferredSize(new Dimension(readerTable.getTableHeader().getWidth(), 30));
            readerTable.setRowHeight(30);
            readerTable.getTableHeader().setFont(font);
            readerTable.setFont(font);
            readerTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            readerTableRightClick(e.getComponent(),e.getX(), e.getY(), readerTable.getSelectedRow() + 1, readerTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            readerTable .getTableHeader().setReorderingAllowed(false);
            readerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            readerScroll.getViewport().add(readerTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void readerTypeTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(readerType);
            readerTypeTable = new JTable(tmp, new String[]{"读者类型编号","读者类型"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            readerTypeTable.getTableHeader().setPreferredSize(new Dimension(readerTypeTable.getTableHeader().getWidth(), 30));
            readerTypeTable.setRowHeight(30);
            readerTypeTable.getTableHeader().setFont(font);
            readerTypeTable.setFont(font);
            readerType.last();
            if (readerType.getRow() == 0){
                addReaderTypeButton.setVisible(true);
            }else{
                addReaderTypeButton.setVisible(false);
            }
            readerTypeTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK){
                            int row = readerTypeTable.getSelectedRow();
                            reader = ReaderManager.getDataUseReaderTypeId((Integer) readerTypeTable.getValueAt(row, 0));
                            readerTableReload();
                        }else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            readerTypeTableRightClick(e.getComponent(),e.getX(), e.getY(), readerTypeTable.getSelectedRow() + 1, readerTypeTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
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
    private void readerTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem set = new JMenuItem("修改");
        JMenuItem del = new JMenuItem("删除");
        tablerightClickShow.add(set);
        tablerightClickShow.add(del);
        Component window = this;
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    if (selectCol == 1) throw new CoustemExecption("不允许修改读者编号");
                    Object change;
                    if(selectCol == 2) {
                        change = JOptionPane.showInputDialog(window, "修改" + readerTable.getValueAt(selectRow-1, 2) + "的（读者类型编号）", "修改", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        change = JOptionPane.showInputDialog(window, "修改" + readerTable.getValueAt(selectRow-1, 2) + "的" + readerTable.getColumnName(selectCol-1), "修改", JOptionPane.INFORMATION_MESSAGE);
                    }
                    reader = Database.setData(reader, selectRow, selectCol, change);
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader.absolute(selectRow);
                    UserManager.unBoundReader((String) UserManager.getData(reader.getInt(1), 1));
                    readerType = ReaderTypeManager.getData();
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchReaderId = new JMenuItem("搜索读者编号");
        JMenuItem searchReaderName = new JMenuItem("搜索读者姓名");
        JMenuItem searchReaderAge = new JMenuItem("搜索读者年龄");
        JMenuItem searchReaderSex = new JMenuItem("搜索读者性别");
        JMenuItem searchReaderPhone = new JMenuItem("搜索读者电话");
        JMenuItem searchReaderDept = new JMenuItem("搜索读者院系");
        tablerightClickShow.add(search);
        search.add(searchReaderId);
        search.add(searchReaderName);
        search.add(searchReaderAge);
        search.add(searchReaderSex);
        search.add(searchReaderPhone);
        search.add(searchReaderDept);
        searchReaderId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.getData("reader", "readerId",Integer.valueOf(JOptionPane.showInputDialog("搜索读者编号")));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.searchData("reader", "readerName", JOptionPane.showInputDialog("搜索读者姓名"));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderAge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.getData("reader", "readerAge", Integer.valueOf(JOptionPane.showInputDialog("搜索读者年龄")));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderSex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.searchData("reader", "readerSex", JOptionPane.showInputDialog("搜索读者性别"));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderPhone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.searchData("reader", "readerPhone", JOptionPane.showInputDialog("搜索读者电话"));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderDept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = Database.searchData("reader", "readerDept", JOptionPane.showInputDialog("搜索读者院系"));
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reader = ReaderManager.getData();
                    readerTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        set.setFont(font);
        del.setFont(font);
        search.setFont(font);
        searchReaderAge.setFont(font);
        searchReaderDept.setFont(font);
        searchReaderId.setFont(font);
        searchReaderName.setFont(font);
        searchReaderPhone.setFont(font);
        searchReaderSex.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    private void readerTypeTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
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
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    if (selectCol == 1) throw new CoustemExecption("不允许修改读者类型编号");
                    Object change = JOptionPane.showInputDialog(window, "修改" + readerTypeTable.getValueAt(selectRow-1, 0) + "的" + readerTypeTable.getColumnName(selectCol-1), "修改", JOptionPane.INFORMATION_MESSAGE);
                    readerType = Database.setData(readerType, selectRow, selectCol, change);
                    readerTypeTableReload();
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
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchReaderTypeId = new JMenuItem("搜索读者类型编号");
        JMenuItem searchReaderTypeName = new JMenuItem("搜索读者类型");
        tablerightClickShow.add(search);
        search.add(searchReaderTypeId);
        search.add(searchReaderTypeName);
        searchReaderTypeId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    readerType = Database.getData("readerType", "readerTypeId",Integer.valueOf(JOptionPane.showInputDialog("搜索读者类型编号")));
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchReaderTypeName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    readerType = Database.searchData("readerType", "readerTypeName", JOptionPane.showInputDialog("搜索读者类型名称"));
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    readerType = ReaderTypeManager.getData();
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add.setFont(font);
        set.setFont(font);
        del.setFont(font);
        search.setFont(font);
        searchReaderTypeId.setFont(font);
        searchReaderTypeName.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
}
class BookPane extends JPanel{
    private Font font = new Font("微软雅黑", Font.PLAIN, 20);
    private JScrollPane bookScroll = new JScrollPane(), bookTypeScroll = new JScrollPane();
    private JTable bookTable, bookTypeTable;
    private ResultSet book, bookType;
    private JButton addBookButton, addBookTypeButton;
    public BookPane()throws Exception{
        setLayout(null);
        JPanel helpBox = new JPanel();
        JLabel help = new JLabel("点击图书类型会显示所有该类型的图书");
        help.setFont(new Font("宋体", Font.BOLD, 50));
        helpBox.add(help);
        helpBox.setBounds(0,500,980,100);
        bookScroll.setBounds(0,0, 780, 500);
        bookTypeScroll.setBounds(780,0,200, 500);
        createAddBookButton();
        createAddBookTypeButton();
        reload();
        add(helpBox);
        add(bookScroll);
        add(bookTypeScroll);
    }
    public void reload() throws Exception{
        book = BookManager.getData();
        bookType = BookTypeManager.getData();
        bookTableReload();
        bookTypeTableReload();
    }
    private void addBook() throws Exception{
        Component window = this;
        new AddBookWindow().addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {
                try{
                    book = BookManager.getData();
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void createAddBookButton(){
        Component window = this;
        addBookButton = new JButton("添加");
        add(addBookButton);
        addBookButton.setFont(font);
        addBookButton.setBounds(380,200,100,100);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    addBook();
                    book = BookManager.getData();
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        addBookButton.setVisible(false);
    }
    private void createAddBookTypeButton(){
        Component window = this;
        addBookTypeButton = new JButton("添加");
        add(addBookTypeButton);
        addBookTypeButton.setFont(font);
        addBookTypeButton.setBounds(820,200,100,100);
        addBookTypeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    BookTypeManager.addData(JOptionPane.showInputDialog(window,"请输入需要添加的书本类型", "添加书本类型", JOptionPane.INFORMATION_MESSAGE));
                    bookType = BookTypeManager.getData();
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void bookTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(book);
            book.last();
            int n = book.getRow();
            for(int i = 0; i < n; i ++){
                tmp[i][1] = BookTypeManager.getData((Integer) tmp[i][1], 2);
                if (BorrowManager.isBorrowed((Integer) tmp[i][0])){
                    tmp[i][0] += "(已借阅)";
                }else{
                    tmp[i][0] += "(未借阅)";
                }
            }
            bookTable = new JTable(tmp, new String[]{"书本编号","书本类型","书本名称","作者","出版社","出版日期","印刷次数","价格"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookTable.getTableHeader().setPreferredSize(new Dimension(bookTable.getTableHeader().getWidth(), 30));
            bookTable.setRowHeight(30);
            bookTable.getTableHeader().setFont(font);
            bookTable.setFont(font);
            book.last();
            if (book.getRow() == 0){
                addBookButton.setVisible(true);
            }else{
                addBookButton.setVisible(false);
            }
            bookTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            bookTableRightClick(e.getComponent(),e.getX(), e.getY(), bookTable.getSelectedRow() + 1, bookTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            bookTable .getTableHeader().setReorderingAllowed(false);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookScroll.getViewport().add(bookTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookTypeTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(bookType);
            bookTypeTable = new JTable(tmp, new String[]{"书本类型编号","书本类型"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookTypeTable.getTableHeader().setPreferredSize(new Dimension(bookTypeTable.getTableHeader().getWidth(), 30));
            bookTypeTable.setRowHeight(30);
            bookTypeTable.getTableHeader().setFont(font);
            bookTypeTable.setFont(font);
            bookType.last();
            if (bookType.getRow() == 0){
                addBookTypeButton.setVisible(true);
            }else{
                addBookTypeButton.setVisible(false);
            }
            bookTypeTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e){ }
                public void mousePressed(MouseEvent e){
                    try{
                        if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK){
                            int row = bookTypeTable.getSelectedRow();
                            book = BookManager.getTypeData((Integer) bookTypeTable.getValueAt(row, 0));
                            bookTableReload();
                        }
                        else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            bookTypeTableRightClick(e.getComponent(),e.getX(), e.getY(), bookTypeTable.getSelectedRow() + 1, bookTypeTable.getSelectedColumn() + 1);
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
                public void mouseReleased(MouseEvent e){}
                public void mouseEntered(MouseEvent e){}
                public void mouseExited(MouseEvent e){}
            });
            bookTypeTable .getTableHeader().setReorderingAllowed(false);
            bookTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookTypeScroll.getViewport().add(bookTypeTable);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
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
                    addBook();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Object change;
                    if (selectCol == 1) throw new CoustemExecption("不允许修改书本编号");
                    if (selectCol == 2){
                        change = JOptionPane.showInputDialog(window, "修改" + bookTable.getValueAt(selectRow-1, 2) + "的书本类型编号", "修改", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        change = JOptionPane.showInputDialog(window, "修改" + bookTable.getValueAt(selectRow-1, 2) + "的" + bookTable.getColumnName(selectCol-1), "修改", JOptionPane.INFORMATION_MESSAGE);
                    }
                    book = Database.setData(book, selectRow, selectCol, change);
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    book.absolute(selectRow);
                    BookManager.delData(book.getInt(1));
                    book = BookManager.getData();
                    bookTableReload();
                }catch (Exception ec){
                    ec.printStackTrace();
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchISBN = new JMenuItem("搜索书本编号");
        JMenuItem searchBookName = new JMenuItem("搜索书本名称");
        JMenuItem searchBookPublish = new JMenuItem("搜索出版社");
        tablerightClickShow.add(search);
        search.add(searchISBN);
        search.add(searchBookName);
        search.add(searchBookPublish);
        searchISBN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    book = Database.getData("book", "ISBN", Integer.valueOf(JOptionPane.showInputDialog("搜索书本编号")));
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchBookName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    book = Database.searchData("book", "bookName", JOptionPane.showInputDialog("搜索书本名称"));
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchBookPublish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    book = Database.searchData("book", "bookPublish", JOptionPane.showInputDialog("搜索出版社"));
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    book = BookManager.getData();
                    bookTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add.setFont(font);
        set.setFont(font);
        del.setFont(font);
        search.setFont(font);
        searchBookName.setFont(font);
        searchBookPublish.setFont(font);
        searchISBN.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    private void bookTypeTableRightClick(Component cp, int x, int y, int selectRow, int selectCol){
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
                    BookTypeManager.addData(JOptionPane.showInputDialog(window,"请输入需要添加的书本类型", "添加书本类型", JOptionPane.INFORMATION_MESSAGE));
                    bookType = BookTypeManager.getData();
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    if (selectCol == 1) throw new CoustemExecption("不允许修改书本类型编号");
                    Object change = JOptionPane.showInputDialog(window, "修改" + bookTypeTable.getValueAt(selectRow-1, 0) + "的" + bookTypeTable.getColumnName(selectCol-1), "修改", JOptionPane.INFORMATION_MESSAGE);
                    bookType = Database.setData(bookType, selectRow, selectCol, change);
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookType.absolute(selectRow);
                    BookTypeManager.delData(bookType.getInt(1));
                    bookType = BookTypeManager.getData();
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchBookTypeId = new JMenuItem("搜索书本类型编号");
        JMenuItem searchBookTypeName = new JMenuItem("搜索书本类型名称");
        tablerightClickShow.add(search);
        search.add(searchBookTypeId);
        search.add(searchBookTypeName);
        searchBookTypeId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookType = Database.getData("bookType", "bookTypeId", Integer.valueOf(JOptionPane.showInputDialog("搜索书本类型编号")));
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        searchBookTypeName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookType = Database.searchData("bookType", "bookTypeName", JOptionPane.showInputDialog("搜索书本类型名称"));
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookType = BookTypeManager.getData();
                    bookTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add.setFont(font);
        set.setFont(font);
        del.setFont(font);
        search.setFont(font);
        searchBookTypeId.setFont(font);
        searchBookTypeName.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
}
public class AdminWindow extends JFrame {
    private Font font = new Font("宋体", Font.BOLD, 40);
    public AdminWindow(String title){
        setLayout(null);
        init();
        setVisible(true);
        setResizable(false);
        setTitle(title);
        setBounds(0,0,1024,768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private void init(){
        try{
            JPanel helpC = new JPanel();
            JLabel help = new JLabel("请选中数据后右击对其进行操作");
            helpC.add(help);
            help.setFont(font);
            helpC.setBounds(10,10,980,50);
            JTabbedPane tabPane = new JTabbedPane();
            UserPane userPane = new UserPane();
            ReaderPane readerPane = new ReaderPane();
            BookPane bookPane = new BookPane();
            tabPane.add("管理用户",userPane);
            tabPane.add("管理读者",readerPane);
            tabPane.add("管理图书",bookPane);
            tabPane.setFont(new Font("微软雅黑", Font.BOLD, 30));
            tabPane.setBounds(10,60,980,650);
            Component window = this;
            tabPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    try{
                        if (((JTabbedPane)e.getSource()).getSelectedIndex() == 0){
                        }else if (((JTabbedPane)e.getSource()).getSelectedIndex() == 1){
                        }else if (((JTabbedPane)e.getSource()).getSelectedIndex() == 2){
                        }
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            add(helpC);
            add(tabPane);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
