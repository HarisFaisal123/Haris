package com.oracle.tutorial.Students;
import com.oracle.tutorial.jdbc.JDBCTutorialUtilities;

import java.io.*;
import java.util.*;
import java.sql.*;
public class createsubjecttable {
    private Connection con;
    private String dbms;
    private String dbname;

    public createsubjecttable(Connection con, String dbname, String dbms){
        this.con = con;
        this.dbname = dbname;
        this.dbms = dbms;
    }
    public void createTable() throws SQLException{
        String create = "create Table SUBJECTS" + "(CODE varchar(255) NOT NULL, " + "SESSION varchar(255) NOT NULL, " +
                "CREDITS Decimal(2,1) NOT NULL, " + "STUDENT_NUMBER int NOT NULL, "+ "PRIMARY KEY (CODE), " +
                "FOREIGN KEY (STUDENT_NUMBER) REFERENCES STUDENTS(STUDENT_NUMBER))";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(create);
        } catch (SQLException e){
            if (e.getSQLState().equals("42S01")){
                try{
                    System.out.println(e.getSQLState());
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("drop table SUBJECTS");
                    System.out.println("dropping table");
                    stmt.executeUpdate(create);
                }
                catch (SQLException ex){
                    JDBCTutorialUtilities.printSQLException(ex);
                }
            }
            else{
                JDBCTutorialUtilities.printSQLException(e);
            }
        }

    }
    public void populate_table() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String code;
        String session;
        float credits;
        int StudentNumber;
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO SUBJECTS(CODE, SESSION, CREDITS, STUDENT_NUMBER) VALUES (?, ?, ?, ?)");
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                System.out.println("input subject code:");
                code = sc.next();
                System.out.println("input the session fall/winter");
                session = sc.next();
                System.out.println("input credits");
                credits = sc.nextFloat();
                System.out.println("input Student Number");
                StudentNumber = sc.nextInt();
                stmt.setString(1,code);
                stmt.setString(2, session);
                stmt.setFloat(3, credits);
                stmt.setInt(4, StudentNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        }
    }

    public static void main(String[] args) {
        JDBCTutorialUtilities myjdbc;
        Connection myconnection = null;
        if (args[0] == null) {
            System.out.println("properties file not specified");
        }
        try {
            myjdbc = new JDBCTutorialUtilities(args[0]);
        } catch (Exception e) {
            System.err.println("Problem reading properties file");
            e.printStackTrace();
            return;
        }
        try {
            myconnection = myjdbc.getConnection();
            createsubjecttable table = new createsubjecttable(myconnection, myjdbc.dbms, myjdbc.dbName);
            table.createTable();
            table.populate_table();
        } catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        } finally {
            JDBCTutorialUtilities.closeConnection(myconnection);
        }
    }
}
