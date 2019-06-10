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
        return Database.getData("select * from borrowBook where readerId = " + DataProcess.processData(readerId));
    }
    public static ResultSet getDataUseISBN(Integer ISBN) throws Exception{
        return Database.getData("select * from borrowBook where ISBN = " + DataProcess.processData(ISBN));
    }
    public static ResultSet getData(Integer readerId, Integer ISBN) throws Exception{
        ResultSet rs = Database.getData("select * from borrowBook where readerId = " + DataProcess.processData(readerId) + " and ISBN = " + DataProcess.processData(ISBN));
        rs.last();
        if (rs.getRow() == 0) throw new Exception("无结果");
        return rs;
    }
    public static boolean isBorrowed(Integer ISBN) throws Exception{
        ResultSet rs = getDataUseISBN(ISBN);
        rs.last();
        return rs.getRow() > 0;
    }
    public static boolean isAllReturn(Integer readerId) throws Exception{
        ResultSet rs = getDataUseReaderId(readerId);
        rs.last();
        return rs.getRow() == 0;
    }
    public static void addData(Integer readerId, Integer ISBN, Date returnDate) throws Exception{
        if (isBorrowed(ISBN)) throw new CoustemExecption("该书已被借阅");
        Database.runSqlCommand("insert into borrowBook values ("+ DataProcess.processData(readerId) + "," + DataProcess.processData(ISBN) + ",getdate(),"+ DataProcess.processData(returnDate) +")");
    }
    public static void delData(Integer readerId, Integer ISBN) throws Exception{
        ResultSet rs = getData(readerId, ISBN);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("不存在该借阅信息");
        rs.deleteRow();
    }
    public static void delBookData(Integer readerId) throws Exception{
        Database.runSqlCommand("delete from borrowBook where readerId = " + DataProcess.processData(readerId));
    }
    public static void delReaderData(Integer readerId) throws Exception{
        Database.runSqlCommand("delete from borrowBook where readerId = " + DataProcess.processData(readerId));
    }
}
