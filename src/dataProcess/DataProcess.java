package dataProcess;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.sql.Date;

public class DataProcess {
    public static String processData(String a){
        if(a == null) return "null";
        else return "'" + a + "'";
    }
    public static String processData(Integer a){
        if(a == null) return "null";
        else return a.toString();
    }
    public static String processData(Date a){
        if (a == null) return "null";
        else return "'" + a.toString() + "'";
    }
    public static void hideColumn(JTable table, int index){

        TableColumn tc= table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setMinWidth(0);
        tc.setWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }
}
