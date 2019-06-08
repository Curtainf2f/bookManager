package userManager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;
import readerManager.ReaderManager;

import java.sql.ResultSet;

public class UserManager {
    public static String checkUesrName(String userName) throws Exception{
        if(repeatUserName(userName))
            return "该用户名已存在";
        if (userName == null || userName.isEmpty())
            return "用户名不能为空";
        if (userName.length()> 16)
            return "用户名长度不能超过16个字符";
        return null;
    }
    public static String checkPassword(String password){
        if(password == null){
            return "密码不能为空";
        }
        if(password.length() < 6)
            return "密码长度不能少于6个字符";
        if (password.length() > 16)
            return "密码长度不能超过16个字符";
        return null;
    }
    public static boolean repeatUserName(String userName) throws Exception{
        return getData(userName, 1) != null;
    }
    public static ResultSet getData() throws Exception{
        return Database.getData("select * from users");
    }
    public static ResultSet getData(Integer readerId) throws Exception{
        return Database.getData("select top 1 * from users where readerId = " + DataProcess.process(readerId));
    }
    public static ResultSet getData(String userName) throws Exception{
        return Database.getData("select top 1 * from users where userName = " + DataProcess.process(userName));
    }
    public static Object getData(String userName, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from users where userName = " + DataProcess.process(userName));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static void addData(String userName, String password, Integer readerId) throws Exception{
        if(checkUesrName(userName) != null)
            throw new CoustemExecption(checkUesrName(userName));
        if(checkPassword(password) != null)
            throw new CoustemExecption(checkPassword(password));
        Database.runSqlCommand("insert into users values ("+ DataProcess.process(userName) + "," + DataProcess.process(password) + "," + DataProcess.process(readerId) +")");
    }
    public static void setData(String userName, int col, Object data) throws Exception{
        ResultSet rs = getData(userName);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该用户名不存在");
        rs.updateObject(col, data);
        rs.updateRow();
    }
    public static void delData(String userName) throws Exception{
        ResultSet rs = getData(userName);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该用户名不存在");
        ReaderManager.delData((Integer) getData(userName, 3));
        rs.deleteRow();
    }
    public static String getPassword(String userName)throws Exception{
        return (String) getData(userName, 2);
    }
    public static Integer getReaderId(String userName) throws Exception{
        return (Integer)getData(userName, 3);
    }
    public static void setPassword(String userName, String password) throws Exception{
        if(checkPassword(password) == null)
            throw new CoustemExecption(checkPassword(password));
        setData(userName, 2, password);
    }
    public static void setReaderId(String userName, Integer readerId) throws Exception{
        setData(userName, 3, readerId);
    }
    public static void delReaderId(Integer readerId) throws Exception{
        ResultSet rs = getData(readerId);
        rs.last();
        if(rs.getRow() == 0) throw new CoustemExecption("该读者不存在");
        rs.updateObject(3, null);
        rs.updateRow();
    }
    public static void boundReader(String userName, Integer readerTypeId, String readerName, Integer readerAge, String readerSex, String readerPhone, String readerDept) throws Exception {
        setData(userName, 3, ReaderManager.addData(userName, readerTypeId, readerName, readerAge, readerSex, readerPhone, readerDept));
    }
}
