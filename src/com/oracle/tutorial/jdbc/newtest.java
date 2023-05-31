package com.oracle.tutorial.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;

public class newtest {
    public static void PopulateTable(Connection connection) throws SQLException {
        System.out.println(" populating table now . . . . . .");
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("insert into STUDENTS " + "values('Haris', 101, 'Male', 'A')");
            stmt.executeUpdate("insert into STUDENTS " + "values('Affan', 102, 'Male', 'B')");
            stmt.executeUpdate("insert into STUDENTS " + "values('Rijja', 103, 'Female', 'C')");
            stmt.executeUpdate("insert into STUDENTS " + "values('ali', 104, 'Male', 'A')");
            stmt.executeUpdate("insert into STUDENTS " + "values('Khan', 105, 'Male', 'E')");
            stmt.executeUpdate("insert into STUDENTS " + "values('rukayya', 106, 'Female', 'C')");
            stmt.executeUpdate("insert into STUDENTS " + "values('John', 107, 'Male', 'A+')");
            stmt.executeUpdate("insert into STUDENTS " + "values('Jack', 108, 'Male', 'A')");
        } catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        }
    }
    public static void Printnames(Connection connection){
        String query = "select Student_name, ID, Gender, Grade from STUDENTS";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs= stmt.executeQuery(query);
            while (rs.next()){
                String name = rs.getString("Student_name");
                int number = rs.getInt("ID");
                String gender = rs.getString("Gender");
                String grade = rs.getString("Grade");
                System.out.println(name + ", " + number + "," + gender + "," + grade);
            }
        }
        catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        }
    }
    public static void Print_females(Connection connection){
        String query = "select Student_name from STUDENTS where Gender = 'Female'";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                String name = rs.getString("Student_name");
                System.out.println(name);
            }
        }
        catch (SQLException e){
            JDBCTutorialUtilities.printSQLException(e);
        }
    }


        public static void main (String[]args){
            System.out.println("HI");
            JDBCTutorialUtilities myJDBC;
            Connection conn = null;
            try {
                myJDBC = new JDBCTutorialUtilities(args[0]);
            } catch (Exception e) {
                System.err.println("Error reading properties file" + args[0]);
                e.printStackTrace();
                return;
            }
            try {
                conn = myJDBC.getConnection();
                Statement stmt = conn.createStatement();
                System.out.println(stmt);
                String statement = "create table STUDENTS " + "(Student_name varchar(255) NOT NULL, " + "ID int NOT NULL, "
                        + "GENDER varchar(255) NOT NULL," + "PRIMARY KEY (ID))";
                System.out.println(stmt.executeUpdate(statement));
                System.out.println("Table built");
                stmt.executeUpdate("alter table STUDENTS add GRADE varchar(255);");
                PopulateTable(conn);
                Printnames(conn);
                Print_females(conn);

            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
            }

    }

}
