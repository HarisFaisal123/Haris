package com.oracle.tutorial.Students;
import com.oracle.tutorial.jdbc.JDBCTutorialUtilities;

import javax.swing.plaf.nimbus.State;
import java.util.*;
import java.io.*;
import java.sql.*;

public class createStudentTable{
    private Connection con;
    private String dbname;
    private String dbms;
    public createStudentTable(Connection con, String dbname, String dbms){
        this.con = con;
        this.dbms = dbms;
        this.dbname = dbname;
    }
    public void CreateTable() throws SQLException{
        String create = "create Table STUDENTS " + "(FIRST_NAME varchar(255) NOT NULL, " + "LAST_NAME varchar(255) NOT NULL, " +
                "AGE int NOT NULL, "+"STUDENT_NUMBER int NOT NULL, "+"PRIMARY KEY (STUDENT_NUMBER))";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(create);
        } catch (SQLException e){
            if (e.getSQLState().equals("42S01")){
                try{
                    System.out.println(e.getSQLState());
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("drop table STUDENTS");
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
        String name;
        String lastname;
        int age;
        int StudentNumber;
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO STUDENTS (FIRST_NAME, LAST_NAME, AGE, STUDENT_NUMBER) VALUES (?, ?, ?, ?)");
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                System.out.println("input first name:");
                name = sc.next();
                System.out.println("input last name:");
                lastname = sc.next();
                System.out.println("input age");
                age = sc.nextInt();
                System.out.println("input Student Number");
                StudentNumber = sc.nextInt();
                stmt.setString(1,name);
                stmt.setString(2, lastname);
                stmt.setInt(3, age);
                stmt.setInt(4, StudentNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        }
    }
    public void readvalues() throws SQLException{
        String statement = "select FIRST_NAME, LAST_NAME, AGE, STUDENT_NUMBER from STUDENTS";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            while (rs.next()) {
                String fn = rs.getString("FIRST_NAME");
                System.out.println("firstname:" + "     " + fn);
                String ln = rs.getString("LAST_NAME");
                System.out.println("lastname:" + "     " + ln);
                int age = rs.getInt("AGE");
                System.out.println("age:" + "     " + age);
                int sn = rs.getInt("STUDENT_NUMBER");
                System.out.println("student number" + "     " + sn);
            }
        }
        catch (SQLException e){
            JDBCTutorialUtilities.printSQLException(e);
        }
    }

    public void deletevalues() throws SQLException{
        int StudentNumber;
        Scanner sc = new Scanner(System.in);
        try {
            Statement stmt = con.createStatement();
            System.out.println("input student number to delete");
            StudentNumber = sc.nextInt();
            stmt.executeUpdate("delete from SUBJECTS where STUDENT_NUMBER = " + StudentNumber);
            stmt.executeUpdate("delete from STUDENTS where STUDENT_NUMBER = " + StudentNumber);
        }
        catch (SQLException e){
            JDBCTutorialUtilities.printSQLException(e);
        }
    }
    public void updatevalues() throws SQLException{
        int StudentNumber;
        String FirstName,LastName;
        Scanner sc = new Scanner(System.in);
        String st;
        st = "UPDATE STUDENTS SET FIRST_NAME = ?, LAST_NAME = ? WHERE STUDENT_NUMBER = ?";

        try{
            PreparedStatement stmt = con.prepareStatement(st);
            System.out.println("Enter student id");
            StudentNumber = sc.nextInt();
            System.out.println("enter First Name");
            FirstName = sc.next();
            System.out.println("enter the last name");
            LastName = sc.next();
            stmt.setString(1, FirstName);
            stmt.setString(2,LastName);
            stmt.setInt(3,StudentNumber);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            JDBCTutorialUtilities.printSQLException(e);
        }
    }
    public static void main(String[] args){
        JDBCTutorialUtilities myjdbc;
        Connection myconnection = null;
        if (args[0] == null) {
            System.out.println("properties file not specified");
        }
        try {
            myjdbc = new JDBCTutorialUtilities(args[0]);
        }
        catch (Exception e) {
            System.err.println("Problem reading properties file");
            e.printStackTrace();
            return;
        }
        try{
            myconnection = myjdbc.getConnection();
            createStudentTable table = new createStudentTable(myconnection, myjdbc.dbms, myjdbc.dbName);
            // table.CreateTable();
            table.readvalues();
            table.deletevalues();
            table.updatevalues();
        }
        catch (SQLException e){
            JDBCTutorialUtilities.printSQLException(e);
        }
        finally {
            JDBCTutorialUtilities.closeConnection(myconnection);
        }
    }


}
