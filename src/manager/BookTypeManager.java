package manager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;

import java.sql.ResultSet;

public class BookTypeManager {
    public static Integer getFreeBookTypeId() throws Exception{
        ResultSet rs = Database.getData("select top 1 * from freeBookType");
        rs.last();
        if(rs.getRow() == 1) {
            Integer res = rs.getInt(1);
            rs.deleteRow();
            return res;
        }
        rs = Database.getData("select top 1 bookTypeId from bookType order by bookTypeId desc");
        rs.last();
        if(rs.getRow() == 1) return rs.getInt(1) + 1;
        return 1;
    }
    public static void addFreeBookTypeId(Integer bookTypeId) throws Exception{
        Database.runSqlCommand("insert into freeBookType values ("+ DataProcess.process(bookTypeId) +")");
    }
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from bookType");
    }
    public static ResultSet getData(Integer bookTypeId) throws Exception{
        return Database.getData("select top 1 * from bookType where bookTypeId = " + DataProcess.process(bookTypeId));
    }
    public static Object getData(String bookTypeName, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from bookType where bookTypeName = " + DataProcess.process(bookTypeName));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static Object getData(Integer bookTypeId, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from bookType where bookTypeId = " + DataProcess.process(bookTypeId));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static void addData(Integer bookTypeId, String bookTypeName) throws Exception{
        Database.runSqlCommand("insert into bookType values ("+ DataProcess.process(bookTypeId) + "," + DataProcess.process(bookTypeName) +")");
    }
    public static void addData(String bookTypeName) throws Exception{
        addData(getFreeBookTypeId(), bookTypeName);
    }
    public static void setData(Integer bookTypeId, int col, Object data) throws Exception{
        ResultSet rs = getData(bookTypeId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该图书类型不存在");
        rs.updateObject(col, data);
        rs.updateRow();
    }
    public static void delData(Integer bookTypeId) throws Exception{
        if(BookManager.haveTypeBook(bookTypeId) > 0) throw new CoustemExecption("还存在有该类型的书");
        ResultSet rs = getData(bookTypeId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该图书类型不存在");
        addFreeBookTypeId(bookTypeId);
        rs.deleteRow();
    }
}
