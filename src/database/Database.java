package database;

import java.sql.*;

public class Database {
    private static Connection cn;
    static {
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try{
            Class.forName(driverName);
            cn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;integratedSecurity=true;DatabaseName=bookManager");
            System.out.println("连接成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ResultSet getData(String cmd) throws Exception{
        Statement statement = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        return statement.executeQuery(cmd);
    }
    public static int runSqlCommand(String cmd) throws Exception{
        PreparedStatement pstmt = cn.prepareStatement(cmd);
        return pstmt.executeUpdate();
    }
    public static void delData(ResultSet old, Integer row) throws Exception{
        old.absolute(row);
        old.deleteRow();
    }
    public static ResultSet setData(ResultSet old, Integer row, Integer col, Object data) throws Exception{
        old.absolute(row);
        old.updateObject(col, data);
        old.updateRow();
        return old;
    }
    public static Object[][] getObject(ResultSet rs) throws Exception{
        Object[][] obj = null;
        rs.last();
        int m = rs.getMetaData().getColumnCount();
        obj = new Object[rs.getRow()][m];
        rs.beforeFirst();
        int i = 0;
        while (rs.next()){
            for(int j = 1; j <= m; j ++){
                obj[i][j-1] = rs.getObject(j);
            }
            i ++;
        }
        return obj;
    }
}