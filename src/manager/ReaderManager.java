package manager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;
import java.sql.ResultSet;

public class ReaderManager {
    public static Integer getFreeReaderId() throws Exception{
        ResultSet rs = Database.getData("select top 1 * from freeReader");
        rs.last();
        if(rs.getRow() == 1) {
            Integer res = rs.getInt(1);
            rs.deleteRow();
            return res;
        }
        rs = Database.getData("select top 1 readerId from reader order by readerId desc");
        rs.last();
        if(rs.getRow() == 1) return rs.getInt(1) + 1;
        return 1;
    }
    public static void addFreeReaderId(Integer readerId) throws Exception{
        Database.runSqlCommand("insert into freeReader values ("+ DataProcess.processData(readerId) +")");
    }
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from reader");
    }
    public static ResultSet getData(Integer readerId) throws Exception{
        return Database.getData("select top 1 * from reader where readerId = " + DataProcess.processData(readerId));
    }
    public static Object getData(Integer readerId, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from reader where readerId = " + DataProcess.processData(readerId));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static ResultSet getDataUseReaderTypeId(Integer readerTypeId) throws Exception{
        return Database.getData("select * from reader where readerTypeId = " + DataProcess.processData(readerTypeId));
    }
    public static void addData(Integer readerId, Integer readerTypeId, String readerName, Integer readerAge, String readerSex, String readerPhone, String readerDept) throws Exception{
        Database.runSqlCommand("insert into reader values ("+ DataProcess.processData(readerId) + "," + DataProcess.processData(readerTypeId) + "," + DataProcess.processData(readerName) + "," + DataProcess.processData(readerAge) + "," +  DataProcess.processData(readerSex)  + "," +  DataProcess.processData(readerPhone)  + "," + DataProcess.processData(readerDept) + ",getdate())");
    }
    public static Integer addData(String userName, Integer readerTypeId, String readerName, Integer readerAge, String readerSex, String readerPhone, String readerDept) throws Exception{
        Integer readerId = getFreeReaderId();
        addData(readerId, readerTypeId, readerName, readerAge, readerSex, readerPhone, readerDept);
        return readerId;
    }
    public static void setData(Integer readerId, int col, Object data) throws Exception{
        if(col == 1) throw new CoustemExecption("不允许修改读者编号");
        ResultSet rs = getData(readerId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该读者不存在");
        rs.updateObject(col, data);
        rs.updateRow();
    }
    public static void delData(Integer readerId) throws Exception{
        if (!BorrowManager.isAllReturn(readerId)) throw new CoustemExecption("该读者还有书未还，不可修改");
        ResultSet rs = getData(readerId);
        rs.last();
        if(rs.getRow() == 0) return;
        UserManager.delReaderId(readerId);
        rs.deleteRow();
        addFreeReaderId(readerId);
    }
    public static Integer getTypeData(Integer readerTypeId) throws Exception{
        ResultSet rs = Database.getData("select * from reader where readerTypeId = " + DataProcess.processData(readerTypeId));
        rs.last();
        return rs.getRow();
    }
    public static String getReaderName(Integer readerId) throws Exception{
        return (String) getData(readerId, 3);
    }
}
