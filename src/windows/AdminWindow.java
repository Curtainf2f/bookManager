package windows;

import manager.*;
import customException.CoustemExecption;
import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class AdminWindow extends JFrame {
    private JScrollPane usersScroll = new JScrollPane(), readerTypeScroll = new JScrollPane(), readerScroll = new JScrollPane();
    private JScrollPane bookScroll = new JScrollPane(), bookTypeScroll = new JScrollPane(), bookBorrowScroll = new JScrollPane();
    private JTable usersTable, readerTable, readerTypeTable, bookTable, bookTypeTable, bookBorrowTable;
    private ResultSet users, reader, readerType, book, bookType, bookBorrow;
    private JButton addUserButton, addReaderTypeButton, addBookButton, addBookTypeButton;
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
                        readerTypeTableReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            addReaderTypeButton.setBounds(846,50,140,100);
            addReaderTypeButton.setVisible(false);

            addBookTypeButton = new JButton("添加");
            add(addBookTypeButton);
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
            addBookTypeButton.setBounds(846,550,140,100);
            addBookTypeButton.setVisible(false);

            addBookButton = new JButton("添加");
            add(addBookButton);
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
            addBookButton.setBounds(400,550,140,100);
            addBookButton.setVisible(false);

            createReaderInfoBox();
            usersScroll.setBounds(0, 0, 300, 200);
            readerScroll.setBounds(624,0,200,200);
            readerTypeScroll.setBounds(824,0, 200, 200);
            bookTypeScroll.setBounds(824, 500, 200, 200);
            bookScroll.setBounds(0,500,824,200);
            bookBorrowScroll.setBounds(0, 250, 1024, 200);
            users = UserManager.getData();
            readerType = ReaderTypeManager.getData();
            reader = ReaderManager.getData();
            bookType = BookTypeManager.getData();
            book = BookManager.getData();
            bookBorrow = BorrowManager.getData();
            usersTableReload();
            readerTableReload();
            readerTypeTableReload();
            bookTypeTableReload();
            bookTableReload();
            bookBorrowTableReload();
            add(bookTypeScroll);
            add(usersScroll);
            add(readerTypeScroll);
            add(readerScroll);
            add(bookScroll);
            add(bookBorrowScroll);
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
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
            readerTable = new JTable(tmp, new String[]{"读者编号","读者类型","读者姓名"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
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
    private void bookTypeTableReload(){
        try{
            Component window = this;
            Object[][] tmp = Database.getObject(bookType);
            bookTypeTable = new JTable(tmp, new String[]{"书本类型编号","书本类型"}){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
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
    private Box readerInfoBox=Box.createVerticalBox();
    private JLabel readerTypeShow = new JLabel("");
    private JLabel readerNameShow = new JLabel("");
    private JLabel readerAgeShow = new JLabel("");
    private JLabel readerSexShow = new JLabel("");
    private JLabel readerPhoneShow = new JLabel("");
    private JLabel readerDeptShow = new JLabel("");
    private JLabel readerDateShow = new JLabel("");
    private JLabel nonReader = new JLabel("没有读者信息");
    private void createReaderInfoBox(){
        Component window = this;
        JLabel readerTypeLabel = new JLabel("读者类型:");
        JLabel readerNameLabel = new JLabel("姓名:");
        JLabel readerAgeLabel = new JLabel("年龄:");
        JLabel readerSexLabel = new JLabel("性别:");
        JLabel readerPhoneLabel = new JLabel("手机:");
        JLabel readerDeptLabel = new JLabel("院系:");
        JLabel readerDateLabel = new JLabel("注册时间:");
        JButton readerChange = new JButton("修改");
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
        readerInfoBox.setBounds(300,0,300,200);
        add(readerInfoBox);
        readerInfoBox.setVisible(false);
        add(nonReader);
        nonReader.setBounds(400,100,300,50);
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
                        reader = ReaderManager.getData();
                        readerInfo(UserManager.getReaderId(userName));
                        usersTableReload();
                        readerTableReload();
                    }catch (Exception ec){
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
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
                    reader = ReaderManager.getData();
                    readerTableReload();
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
                    reader = ReaderManager.getData();
                    usersTableReload();
                    readerTypeTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
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
        tablerightClickShow.show(cp, x, y);
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
                    users = UserManager.getData();
                    usersTableReload();
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
                    users = UserManager.getData();
                    readerType = ReaderTypeManager.getData();
                    readerTypeTableReload();
                    usersTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
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
        tablerightClickShow.show(cp, x, y);
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
                    bookTableReload();
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        tablerightClickShow.show(cp, x, y);
    }
}
