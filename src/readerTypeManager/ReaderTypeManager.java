package readerTypeManager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;
import readerManager.ReaderManager;

import java.sql.ResultSet;

public class ReaderTypeManager {
    public static Integer getFreeReaderTypeId() throws Exception{
        ResultSet rs = Database.getData("select top 1 * from freeReaderType");
        rs.last();
        if(rs.getRow() == 1){
            Integer res = rs.getInt(1);
            rs.deleteRow();
            return res;
        }
        rs = Database.getData("select top 1 readerTypeId from readerType order by readerTypeId desc");
        rs.last();
        if(rs.getRow() == 1) return rs.getInt(1) + 1;
        return 1;
    }
    public static void addFreeReaderTypeId(Integer readerTypeId) throws Exception{
        Database.runSqlCommand("insert into freeReaderType values ("+ DataProcess.process(readerTypeId) +")");
    }
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from readerType");
    }
    public static ResultSet getData(Integer readerTypeId) throws Exception{
        return Database.getData("select top 1 * from readerType where readerTypeId = " + DataProcess.process(readerTypeId));
    }
    public static ResultSet getData(String readerTypeName) throws Exception{
        return Database.getData("select * from readerType where readerTypeName = " + DataProcess.process(readerTypeName));
    }
    public static Object getData(String readerTypeName, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from readerType where readerTypeName = " + DataProcess.process(readerTypeName));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static Object getData(Integer readerTypeId, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from readerType where readerTypeId = " + DataProcess.process(readerTypeId));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static void addData(Integer readerTypeId, String readerTypeName) throws Exception{
        Database.runSqlCommand("insert into readerType values (" + DataProcess.process(readerTypeId) + "," + DataProcess.process(readerTypeName) + ")");
    }
    public static void addData(String readerTypeName) throws Exception{
        Integer id = (Integer) getData(readerTypeName, 1);
        if(id == null){
            id = getFreeReaderTypeId();
            addData(id, readerTypeName);
        }
    }
    public static void setData(Integer readerTypeId, int col, Object data) throws Exception{
        ResultSet rs = getData(readerTypeId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该读者类型不存在");
        rs.updateObject(col, data);
        rs.updateRow();
    }
    public static void delData(Integer readerTypeId) throws Exception{
        if(ReaderManager.getTypeData(readerTypeId) > 0) throw new CoustemExecption("还存在有该类型的书");
        ResultSet rs = getData(readerTypeId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该读者类型不存在");
        rs.deleteRow();
        addFreeReaderTypeId(readerTypeId);
    }
    public static Integer getReaderTypeId(String readerTypeName) throws Exception{
        return (Integer) getData(readerTypeName, 1);
    }
}
