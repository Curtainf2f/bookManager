package manager;

import customException.CoustemExecption;
import dataProcess.DataProcess;
import database.Database;

import java.sql.ResultSet;

public class AdminManager {
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
    public static ResultSet getData(String userName) throws Exception{
        return Database.getData("select top 1 * from adminUsers where adminUserName = " + DataProcess.processData(userName));
    }
    public static Object getData(String userName, int col) throws Exception{
        ResultSet rs = Database.getData("select top 1 * from adminUsers where adminUserName = " + DataProcess.processData(userName));
        rs.last();
        if(rs.getRow() == 1) return rs.getObject(col);
        return null;
    }
    public static void addData(String userName, String password) throws Exception{
        if(checkUesrName(userName) != null)
            throw new CoustemExecption(checkUesrName(userName));
        if(checkPassword(password) != null)
            throw new CoustemExecption(checkPassword(password));
        Database.runSqlCommand("insert into adminUsers values ("+ DataProcess.processData(userName) + "," + DataProcess.processData(password) + ")");
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
        rs.deleteRow();
    }
    public static String getPassword(String userName)throws Exception{
        return (String) getData(userName, 2);
    }
}