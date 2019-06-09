package windows;

import customException.CoustemExecption;
import database.Database;
import manager.*;

import javax.swing.*;
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
public class UserWindow extends JFrame {
    private User user;
    private JLabel userShow = new JLabel();
    public UserWindow(String title, String userName) {
        try {
            userShow.setText(userName + " 欢迎您!");
            userShow.setBounds(0, 0, 100, 20);
            add(userShow);
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
    private JScrollPane bookScroll = new JScrollPane(), bookTypeScroll = new JScrollPane(), bookBorrowScroll = new JScrollPane();
    private JTable bookTable, bookTypeTable, bookBorrowTable;
    private ResultSet book, bookType, bookBorrow;
    private Box readerInfoBox = Box.createHorizontalBox(), nonReaderBox = Box.createHorizontalBox();
    private void init() {
        try {
            createReaderInfo();
            readerInfoReload();
            bookBorrowScroll.setBounds(0, 50, 1024, 200);
            add(bookBorrowScroll);
            bookScroll.setBounds(0, 300, 824, 350);
            add(bookScroll);
            bookTypeScroll.setBounds(824, 300, 200, 350);
            add(bookTypeScroll);
            book = BookManager.getData();
            bookType = BookTypeManager.getData();
            bookBorrow = BorrowManager.getDataUseReaderId(user.getReaderId());
            bookTableReload();
            bookTypeTableReload();
            bookBorrowTableReload();
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);

        }
    }
    private void boundReader(String userName) throws Exception {
        Component window = this;
        new BoundReaderWindow(userName).addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
                try {
                    user.setReaderId(UserManager.getReaderId(userName));
                    readerInfoReload();
                } catch (Exception ec) {
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    JLabel readerType = new JLabel("读者类型:");
    JLabel readerName = new JLabel("姓名:");
    JLabel readerAge = new JLabel("年龄:");
    JLabel readerSex = new JLabel("性别:");
    JLabel readerPhone = new JLabel("手机:");
    JLabel readerDept = new JLabel("院系:");
    JLabel readerDate = new JLabel("注册时间:");
    private void readerInfoReload() {
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
    private void createReaderInfo() {
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
            readerInfoBox.setBounds(200, 0, 824, 20);
            nonReaderBox.setBounds(200, 0, 824, 20);
            add(readerInfoBox);
            add(nonReaderBox);
            bound.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        boundReader(user.getUserName());
                    } catch (Exception ec) {
                        JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        } catch (Exception ec) {
            JOptionPane.showMessageDialog(this, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void bookTypeTableReload() {
        try {
            Component window = this;
            Object[][] tmp = Database.getObject(bookType);
            bookTypeTable = new JTable(tmp, new String[]{"书本类型编号", "书本类型"}) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
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
                    bookBorrow = BorrowManager.getDataUseReaderId(user.getReaderId());
                    bookTableReload();
                    bookBorrowTableReload();
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
        tablerightClickShow.show(cp, x, y);
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
                    bookTableReload();
                    bookBorrowTableReload();
                } catch (Exception ec) {
                    JOptionPane.showMessageDialog(window, ec.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JMenu search = new JMenu("搜索");
        JMenuItem searchISBN = new JMenuItem("搜索书本编号");
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
        tablerightClickShow.show(cp, x, y);
    }
    private void bookTypeTableRightClick(Component cp, int x, int y, int selectRow, int selectCol) {
        JPopupMenu tablerightClickShow = new JPopupMenu();
        Component window = this;
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
        tablerightClickShow.show(cp, x, y);
    }
}