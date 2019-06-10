package windows;

import dataProcess.DataProcess;
import database.Database;
import manager.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;

class User{
    private String userName;
    private Integer readerId;
    public User(String userName, Integer readerId){
        this.userName = userName;
        this.readerId = readerId;
    }
    public String getUserName() {
        return userName;
    }
    public Integer getReaderId() {
        return readerId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }
}
class TopBar extends JPanel{
    Font lableFont = new Font("微软雅黑",Font.PLAIN, 15);
    private Box readerInfoBox = Box.createHorizontalBox(), nonReaderBox = Box.createHorizontalBox();
    private JLabel readerType = new JLabel("读者类型:");
    private JLabel readerName = new JLabel("姓名:");
    private JLabel readerAge = new JLabel("年龄:");
    private JLabel readerSex = new JLabel("性别:");
    private JLabel readerPhone = new JLabel("手机:");
    private JLabel readerDept = new JLabel("院系:");
    private JLabel readerDate = new JLabel("注册时间:");
    private JLabel userShow = new JLabel();
    User user;
    public TopBar(User user) throws Exception{
        setLayout(new FlowLayout());
        this.user = user;
        readerType.setFont(lableFont);
        readerName.setFont(lableFont);
        readerAge.setFont(lableFont);
        readerSex.setFont(lableFont);
        readerPhone.setFont(lableFont);
        readerDept.setFont(lableFont);
        readerDate.setFont(lableFont);
        userShow.setFont(lableFont);
        createUserInfo();
        createReaderInfo();
        readerInfoReload();
    }
    private void boundReader() throws Exception {
        Component window = this;
        new BoundReaderWindow(user.getUserName()).addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {
                try {
                    user.setReaderId(UserManager.getReaderId(user.getUserName()));
                    readerInfoReload();
                } catch (Exception ec) {
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void readerInfoReload(){
        try {
            if (user.getReaderId() == null) {
                readerInfoBox.setVisible(false);
                nonReaderBox.setVisible(true);
            } else {
                ResultSet rs = ReaderManager.getData(user.getReaderId());
                rs.last();
                readerType.setText(readerType.getText() + (String) ReaderTypeManager.getData(rs.getInt(2), 2));
                readerName.setText(readerName.getText() + rs.getString(3));
                readerAge.setText(readerAge.getText() + rs.getString(4));
                readerSex.setText(readerSex.getText() + rs.getString(5));
                readerPhone.setText(readerPhone.getText() + rs.getString(6));
                readerDept.setText(readerDept.getText() + rs.getString(7));
                readerDate.setText(readerDate.getText() + rs.getString(8));
                readerInfoBox.setVisible(true);
                nonReaderBox.setVisible(false);
            }
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);

        }
    }
    private void createUserInfo(){
        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChangePasswordWindow(user.getUserName());
            }
        });
        changePasswordButton.setFont(lableFont);
        Box userInfoBox = Box.createHorizontalBox();
        userInfoBox.add(userShow);
        userInfoBox.add(Box.createHorizontalStrut(8));
        userInfoBox.add(changePasswordButton);
        userShow.setText("用户名:" + user.getUserName() + " 欢迎您!");
        userInfoBox.setBounds(0, 0, 200, 20);
        add(userInfoBox);
    }
    private void createReaderInfo(){
        try {
            Component window = this;
            JButton bound = new JButton("绑定");
            readerInfoBox.add(readerType);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerName);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerAge);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerSex);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerPhone);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerDept);
            readerInfoBox.add(Box.createHorizontalStrut(8));
            readerInfoBox.add(readerDate);
            nonReaderBox.add(new JLabel("没有读者信息"));
            nonReaderBox.add(Box.createHorizontalStrut(8));
            nonReaderBox.add(bound);
            readerInfoBox.setBounds(250, 0, 774, 20);
            nonReaderBox.setBounds(250, 0, 774, 20);
            add(readerInfoBox);
            add(nonReaderBox);
            bound.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        boundReader();
                    } catch (Exception ec) {
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
class BorrowPane extends JPanel implements ActionListener{
    Font font = new Font("微软雅黑",Font.PLAIN, 20);
    private User user;
    private ResultSet book, bookType;
    private JTable bookTable, bookTypeTable;
    private JScrollPane bookScroll = new JScrollPane(), bookTypeScroll = new JScrollPane();
    private JPanel searchBookBox = new JPanel();
    JComboBox searchType;
    JTextField searchText;
    JButton searchButton;
    public BorrowPane(User user) throws Exception{
        setLayout(null);
        this.user = user;
        bookScroll.setBounds(0, 0, 880, 500);
        bookTypeScroll.setBounds(880, 0, 100, 500);
        searchBookBox.setBounds(0,500,980,50);
        createSearchBookBox();
        add(bookScroll);
        add(bookTypeScroll);
        add(searchBookBox);
        reload();
    }
    public void reload() throws Exception{
        book = BookManager.getData();
        bookType = BookTypeManager.getData();
        bookTableReload();
        bookTypeTableReload();
    }
    private void createSearchBookBox(){
        Object[] tmp = {"搜索图书编号","搜索书名","搜索出版社","搜索图书类型(右边栏显示)"};
        JLabel help1 = new JLabel("<html>借阅请选中<br>右击选择借阅</html>");
        searchType = new JComboBox(tmp);
        searchText = new JTextField(15);
        searchButton = new JButton("搜索");
        JButton showAllButton = new JButton("显示全部");
        JLabel help2 = new JLabel("<html>点击书本类型即可显示<br>所有该书本类型的书</html>");
        showAllButton.setFont(font);
        searchButton.setFont(font);
        searchText.setFont(font);
        searchType.setFont(font);
        searchBookBox.add(help1);
        searchBookBox.add(showAllButton);
        searchBookBox.add(searchType);
        searchBookBox.add(searchText);
        searchBookBox.add(searchButton);
        searchBookBox.add(help2);
        searchText.addActionListener(this);
        searchButton.addActionListener(this);
        Component window = this;
        showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    private void bookTypeTableReload() {
        try {
            Component window = this;
            Object[][] tmp = Database.getObject(bookType);
            bookTypeTable = new JTable(tmp, new String[]{"书本类型编号","书本类型"}) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookTypeTable.getTableHeader().setPreferredSize(new Dimension(bookTypeTable.getTableHeader().getWidth(), 30));
            bookTypeTable.setRowHeight(30);
            bookTypeTable.getTableHeader().setFont(font);
            bookTypeTable.setFont(font);
            DataProcess.hideColumn(bookTypeTable, 0);
            bookTypeTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                            int row = bookTypeTable.getSelectedRow();
                            book = BookManager.getTypeData((Integer) bookTypeTable.getValueAt(row, 0));
                            bookTableReload();
                        }else if(e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK){
                            bookTypeTableRightClick(e.getComponent(), e.getX(), e.getY(), bookTable.getSelectedRow() + 1, bookTable.getSelectedColumn() + 1);
                        }
                    } catch (Exception ec) {
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            bookTypeTable.getTableHeader().setReorderingAllowed(false);
            bookTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookTypeScroll.getViewport().add(bookTypeTable);
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookTableReload() {
        try {
            Component window = this;
            Object[][] tmp = Database.getObject(book);
            book.last();
            int n = book.getRow();
            for (int i = 0; i < n; i++) {
                tmp[i][1] = BookTypeManager.getData((Integer) tmp[i][1], 2);
                if (BorrowManager.isBorrowed((Integer) tmp[i][0])) {
                    tmp[i][0] += "(已借阅)";
                } else {
                    tmp[i][0] += "(未借阅)";
                }
            }
            bookTable = new JTable(tmp, new String[]{"书本编号", "书本类型", "书本名称", "作者", "出版社", "出版日期", "印刷次数", "价格"}) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookTable.getTableHeader().setPreferredSize(new Dimension(bookTable.getTableHeader().getWidth(), 30));
            bookTable.setRowHeight(30);
            bookTable.getTableHeader().setFont(font);
            bookTable.setFont(font);
            bookTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                            bookTableRightClick(e.getComponent(), e.getX(), e.getY(), bookTable.getSelectedRow() + 1, bookTable.getSelectedColumn() + 1);
                        }
                    } catch (Exception ec) {
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            bookTable.getTableHeader().setReorderingAllowed(false);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookScroll.getViewport().add(bookTable);
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookTableRightClick(Component cp, int x, int y, int selectRow, int selectCol) {
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem borrow = new JMenuItem("借阅");
        tablerightClickShow.add(borrow);
        Component window = this;
        borrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    book.absolute(selectRow);
                    BorrowManager.addData(user.getReaderId(), book.getInt(1), Date.valueOf(JOptionPane.showInputDialog(window, "请输入归还日期", "请输入归还日期", JOptionPane.INFORMATION_MESSAGE)));
                    bookTableReload();
                } catch (Exception ec) {
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
        borrow.setFont(font);
        search.setFont(font);
        searchBookName.setFont(font);
        searchBookPublish.setFont(font);
        searchISBN.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    private void bookTypeTableRightClick(Component cp, int x, int y, int selectRow, int selectCol) {
        JPopupMenu tablerightClickShow = new JPopupMenu();
        Component window = this;
        JMenu search = new JMenu("搜索");
        JMenuItem searchBookTypeName = new JMenuItem("搜索书本类型");
        tablerightClickShow.add(search);
        search.add(searchBookTypeName);
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
        search.setFont(font);
        showAll.setFont(font);
        searchBookTypeName.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    public void actionPerformed(ActionEvent e) {
        try{
            if (searchType.getSelectedIndex() == 0){
                book = Database.getData("book", "ISBN", Integer.valueOf(searchText.getText()));
                bookTableReload();
            }else if(searchType.getSelectedIndex() == 1){
                book = Database.searchData("book", "bookName", searchText.getText());
                bookTableReload();
            }else if(searchType.getSelectedIndex() == 2){
                book = Database.searchData("book", "bookPublish", searchText.getText());
                bookTableReload();
            }else if(searchType.getSelectedIndex() == 3){
                bookType = Database.searchData("bookType", "bookTypeName", searchText.getText());
                bookTypeTableReload();
            }
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
class ReturnPane extends JPanel implements ActionListener{
    Font font = new Font("微软雅黑",Font.PLAIN, 20);
    private User user;
    private JScrollPane bookBorrowScroll = new JScrollPane();
    private JTable bookBorrowTable;
    private ResultSet bookBorrow;
    private JPanel searchBox = new JPanel();
    JComboBox searchType;
    JTextField searchText;
    JButton searchButton;
    public ReturnPane(User user) throws Exception{
        this.user = user;
        setLayout(null);
        bookBorrowScroll.setBounds(0, 0, 980, 500);
        searchBox.setBounds(0,500,980,50);
        createSearchBookBox();
        add(searchBox);
        add(bookBorrowScroll);
        reload();
    }
    public void reload() throws Exception{
        bookBorrow = BorrowManager.getDataUseReaderId(user.getReaderId());
        bookBorrowTableReload();
    }
    private void bookBorrowTableReload() {
        try {
            Component window = this;
            Object[][] tmp = Database.getObject(bookBorrow);
            bookBorrow.last();
            int n = bookBorrow.getRow();
            for (int i = 0; i < n; i++) {
                tmp[i][0] += ("(" + ReaderManager.getReaderName(Integer.valueOf(tmp[i][0].toString())) + ")");
                tmp[i][1] += ("(" + BookManager.getData(Integer.valueOf(tmp[i][1].toString()), 3) + ")");
            }
            bookBorrowTable = new JTable(tmp, new String[]{"读者编号", "书本编号", "借阅日期", "归还日期"}) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            bookBorrowTable.getTableHeader().setPreferredSize(new Dimension(bookBorrowTable.getTableHeader().getWidth(), 30));
            bookBorrowTable.setRowHeight(30);
            bookBorrowTable.getTableHeader().setFont(font);
            bookBorrowTable.setFont(font);
            bookBorrowTable.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                            bookBorrowTableRightClick(e.getComponent(), e.getX(), e.getY(), bookBorrowTable.getSelectedRow() + 1, bookBorrowTable.getSelectedColumn() + 1);
                        }
                    } catch (Exception ec) {
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            bookBorrowTable.getTableHeader().setReorderingAllowed(false);
            bookBorrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookBorrowScroll.getViewport().add(bookBorrowTable);
        } catch (Exception ec) {
            ec.printStackTrace();
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookBorrowTableRightClick(Component cp, int x, int y, int selectRow, int selectCol) {
        JPopupMenu tablerightClickShow = new JPopupMenu();
        JMenuItem del = new JMenuItem("归还");
        tablerightClickShow.add(del);
        Component window = this;
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bookBorrow.absolute(selectRow);
                    BorrowManager.delData(bookBorrow.getInt(1), bookBorrow.getInt(2));
                    bookBorrow = BorrowManager.getDataUseReaderId(user.getReaderId());
                    bookBorrowTableReload();
                } catch (Exception ec) {
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchISBN = new JMenuItem("搜索书本编号");
        JMenuItem searchBookName = new JMenuItem("搜索书名");
        tablerightClickShow.add(search);
        search.add(searchISBN);
        searchISBN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bookBorrow = BorrowManager.getData(user.getReaderId(), Integer.valueOf(JOptionPane.showInputDialog("搜索书本编号")));
                    bookBorrowTableReload();
                } catch (Exception ec) {
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenuItem showAll = new JMenuItem("显示所有");
        tablerightClickShow.add(showAll);
        showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    bookBorrow = BorrowManager.getDataUseReaderId(user.getReaderId());
                    bookBorrowTableReload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        del.setFont(font);
        search.setFont(font);
        searchISBN.setFont(font);
        showAll.setFont(font);
        tablerightClickShow.show(cp, x, y);
    }
    private void createSearchBookBox(){
        Object[] tmp = {"搜索图书编号"};
        JLabel help1 = new JLabel("<html>归还请选中右击选择归还</html>");
        searchType = new JComboBox(tmp);
        searchText = new JTextField(15);
        searchButton = new JButton("搜索");
        JButton showAllButton = new JButton("显示全部");
        help1.setFont(font);
        showAllButton.setFont(font);
        searchButton.setFont(font);
        searchText.setFont(font);
        searchType.setFont(font);
        searchBox.add(help1);
        searchBox.add(showAllButton);
        searchBox.add(searchType);
        searchBox.add(searchText);
        searchBox.add(searchButton);
        searchText.addActionListener(this);
        searchButton.addActionListener(this);
        Component window = this;
        showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    reload();
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    public void actionPerformed(ActionEvent e) {
        try{
            bookBorrow = BorrowManager.getData(user.getReaderId(), Integer.valueOf(JOptionPane.showInputDialog("搜索书本编号")));
            bookBorrowTableReload();
        }catch (Exception ec){
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
public class UserWindow extends JFrame {
    private User user;
    public UserWindow(String title, String userName) {
        try {
            user = new User(userName, UserManager.getReaderId(userName));
            setLayout(null);
            init();
            setVisible(true);
            setResizable(false);
            setTitle(title);
            setBounds(0, 0, 1024, 768);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void init() throws Exception{
        JTabbedPane tabPane = new JTabbedPane();
        TopBar topBar = new TopBar(user);
        BorrowPane borrowPane = new BorrowPane(user);
        ReturnPane returnPane = new ReturnPane(user);
        tabPane.add("借阅",borrowPane);
        tabPane.add("归还",returnPane);
        Component window = this;
        tabPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                try{
                    if (((JTabbedPane)e.getSource()).getSelectedIndex() == 0){
                        borrowPane.reload();
                    }else if (((JTabbedPane)e.getSource()).getSelectedIndex() == 1){
                        returnPane.reload();
                    }
                }catch (Exception ec){
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        tabPane.setFont(new Font("微软雅黑", Font.BOLD, 30));
        topBar.setBounds(10,10,1000,100);
        tabPane.setBounds(10,110,980,600);
        add(topBar);
        add(tabPane);
    }
}