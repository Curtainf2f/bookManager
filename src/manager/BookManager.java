package manager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;

import java.sql.Date;
import java.sql.ResultSet;

public class BookManager {
    public static Integer getFreeISBN() throws Exception{
        ResultSet rs = Database.getData("select top 1 * from freeBook");
        rs.last();
        if(rs.getRow() == 1) {
            Integer res = rs.getInt(1);
            rs.deleteRow();
            return res;
        }
        rs = Database.getData("select top 1 ISBN from book order by ISBN desc");
        rs.last();
        if(rs.getRow() == 1) return rs.getInt(1) + 1;
        return 1;
    }
    public static void addFreeISBN(Integer ISBN) throws Exception{
        Database.runSqlCommand("insert into freeBook values ("+ DataProcess.processData(ISBN) +")");
    }
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from book");
    }
    public static ResultSet getData(Integer ISBN) throws Exception{
        return Database.getData("select top 1 * from book where ISBN = " + DataProcess.processData(ISBN));
    }
    public static ResultSet getTypeData(Integer bookTypeId) throws Exception{
        return Database.getData("select top 1 * from book where bookTypeId = " + DataProcess.processData(bookTypeId));
    }
    public static Integer haveTypeBook(Integer bookTypeId) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from book where bookTypeId = " + DataProcess.processData(bookTypeId));
        rs.last();
        return rs.getRow();
    }
    public static Object getData(Integer ISBN, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from book where ISBN = " + DataProcess.processData(ISBN));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static void addData(Integer ISBN, Integer bookTypeId, String bookName, String bookAuthor, String bookPubilish, Date bookPublishDate, Integer bookPublishTimes, Integer bookPrice) throws Exception{
        Database.runSqlCommand("insert into book values ("+ DataProcess.processData(ISBN) + "," + DataProcess.processData(bookTypeId) + "," + DataProcess.processData(bookName) + "," + DataProcess.processData(bookAuthor) + "," + DataProcess.processData(bookPubilish) + "," + DataProcess.processData(bookPublishDate) + "," + DataProcess.processData(bookPublishTimes) + "," + DataProcess.processData(bookPrice) +")");
    }
    public static void addData(Integer bookTypeId, String bookName, String bookAuthor, String bookPubilish, Date bookPublishDate, Integer bookPublishTimes, Integer bookPrice) throws Exception{
        addData(getFreeISBN(), bookTypeId, bookName, bookAuthor, bookPubilish, bookPublishDate, bookPublishTimes, bookPrice);
    }
    public static void setData(Integer ISBN, int col, Object data) throws Exception{
        ResultSet rs = getData(ISBN);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该图书编码不存在");
        rs.updateObject(col, data);
        rs.updateRow();
    }
    public static void delData(Integer ISBN) throws Exception{
        if(BorrowManager.isBorrowed(ISBN)) throw new CoustemExecption("该书已被借走，不可删除");
        ResultSet rs = getData(ISBN);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该图书编码不存在");
        addFreeISBN(ISBN);
        rs.deleteRow();
    }
}
