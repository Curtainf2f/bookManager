package manager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;

import java.sql.Date;
import java.sql.ResultSet;

public class BorrowManager {
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from borrowBook");
    }
    public static ResultSet getDataUseReaderId(Integer readerId) throws Exception{
        return Database.getData("select * from borrowBook where readerId = " + DataProcess.process(readerId));
    }
    public static ResultSet getDataUseISBN(Integer ISBN) throws Exception{
        return Database.getData("select * from borrowBook where ISBN = " + DataProcess.process(ISBN));
    }
    public static ResultSet getData(Integer readerId, Integer ISBN) throws Exception{
        return Database.getData("select * from borrowBook where readerId = " + DataProcess.process(readerId) + " and ISBN = " + DataProcess.process(ISBN));
    }
    public static ResultSet getNotReturnData(Integer readerId, Integer ISBN) throws Exception{
        return Database.getData("select * from borrowBook where readerId = " + DataProcess.process(readerId) + " and ISBN = " + DataProcess.process(ISBN) + " and returnDate is null");
    }
    public static ResultSet getNotReturnDataUserReaderId(Integer readerId) throws Exception {
        return Database.getData("select * from borrowBook where readerId = " + DataProcess.process(readerId) + "and returnDate is null");
    }
    public static ResultSet getNotReturnDataUseISBN(Integer ISBN) throws Exception {
        return Database.getData("select * from borrowBook where ISBN = " + DataProcess.process(ISBN) + "and returnDate is null");
    }
    public static boolean isBorrowed(Integer ISBN) throws Exception{
        ResultSet rs = getNotReturnDataUseISBN(ISBN);
        rs.last();
        return rs.getRow() > 0;
    }
    public static boolean isAllReturn(Integer readerId) throws Exception{
        ResultSet rs = getNotReturnDataUserReaderId(readerId);
        rs.last();
        return rs.getRow() == 0;
    }
    public static void addData(Integer bookTypeId, Integer readerId) throws Exception{
        Database.runSqlCommand("insert into borrowBook values ("+ DataProcess.process(bookTypeId) + "," + DataProcess.process(readerId) + ",getdate(),null)");
    }
    public static void delData(Integer bookTypeId, Integer readerId) throws Exception{
        ResultSet rs = getNotReturnData(bookTypeId, readerId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("不存在该借阅信息");
        rs.updateDate(4, Database.getDate());
    }
    public static void delBookData(Integer bookTypeId) throws Exception{
        Database.runSqlCommand("delete from borrowBook where bookTypeId = " + DataProcess.process(bookTypeId));
    }
    public static void delReaderData(Integer readerId) throws Exception{
        Database.runSqlCommand("delete from borrowBook where readerId = " + DataProcess.process(readerId));
    }
}
