package com.example.h2withjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class H2WithJavaApplication {
    private static String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:mem:testdb";
    private static String USER = "sa";
    private static String PASSWORD = "";

    private static Connection conn = null;
    private static Statement stmt = null;

    public static void main(String[] args) {
        SpringApplication.run(H2WithJavaApplication.class, args);

        //Step 1:  Set up the driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        createRegistrationTable();

        addDataToRegistrationTable();

        retrieveDataFromRegistrationTable();

        updateAndRetrieveDataFromRegistration();

        retrieveDataFromEmployees();

        retrieveDataFromEmployeesWithTableJoin();
    }

    private static void createRegistrationTable() {
        try {
            //Step 2:  Open a connection to the database
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            //Step 3 Prepare a SQL query
            stmt = conn.createStatement();
            String sql = "CREATE TABLE   REGISTRATION " +
                    "(id INTEGER AUTO_INCREMENT  PRIMARY KEY, " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER)";

            //Step 4:  Execute the SQL statement
            stmt.executeUpdate(sql);

            // STEP 6 and 7: Clean-up environment

            //Step 5:  Handle the response.  We don't have to do this for a CREATE statement

            //Steps 6 and 7:  Close open things
            stmt.close();
            conn.close();
        } catch (
                SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

        public static void addDataToRegistrationTable()  {
            try {
                //Step 2:  Open a connection to the database
                conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

                //Step 3 Prepare a SQL query
                stmt = conn.createStatement();
                String sql = "insert into REGISTRATION(first, last, age)  VALUES" +
                        "('Bill', 'Fairfield', 71)";

                //Step 4:  Execute the SQL statement
                stmt.executeUpdate(sql);

                // STEP 6 and 7: Clean-up environment

                //Step 5:  Handle the response.  We don't have to do this for a CREATE statement

                //Steps 6 and 7:  Close open things
                stmt.close();
                conn.close();
            } catch (
                    SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException se2) {
                } // nothing we can do
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

        public static void retrieveDataFromRegistrationTable()  {
            try {
                //Step 2:  Open a connection to the database
                conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

                //Step 3 Prepare a SQL query
                stmt = conn.createStatement();
                String sql = "SELECT * FROM REGISTRATION";

                //Step 4:  Execute the SQL statement
                ResultSet rs = stmt.executeQuery(sql);

                //Step 5:  Handle the response.
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String first = rs.getString("first");
                    String last = rs.getString("last");
                    int age = rs.getInt("age");

                    System.out.print("ID: " + id);
                    System.out.print(", First: " + first);
                    System.out.print(", Last: " + last);
                    System.out.println(", Age: " + age);
                }

                //Steps 6 and 7:  Close open things
                stmt.close();
                rs.close();
                conn.close();
            } catch (
                    SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException se2) {
                } // nothing we can do
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }

    public static void updateAndRetrieveDataFromRegistration() {
        try {
            // STEP 2: Open a connection

            conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);

            // STEP# 3 and 4: Prepare and Execute two queries.  Note that we only need one statement and one sql declaration
            stmt = conn.createStatement();
            String sql = "UPDATE Registration " + "SET age = 30 WHERE first='Bill'";
            stmt.executeUpdate(sql);

            sql = "SELECT id, first, last, age FROM REGISTRATION";

            ResultSet rs = stmt.executeQuery(sql);

            // STEP 5: Extract data from result set
            while(rs.next()) {
                int id  = rs.getInt("id");
                String first = rs.getString("first");
                String last = rs.getString("last");
                int age = rs.getInt("age");

                System.out.print("ID: " + id);
                System.out.print(", First: " + first);
                System.out.print(", Last: " + last);
                System.out.println(", Age: " + age);
            }

            // STEP 6 and 7: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void retrieveDataFromEmployees()  {
        try {
            //Step 2:  Open a connection to the database
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            //Step 3 Prepare a SQL query
            stmt = conn.createStatement();
            String sql = "SELECT * FROM EMPLOYEES";

            //Step 4:  Execute the SQL statement
            ResultSet rs = stmt.executeQuery(sql);

            //Step 5:  Handle the response.
            while(rs.next()) {
                int id = rs.getInt("employee_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                int company_id = rs.getInt("company_id");

                System.out.print("ID: " + id);
                System.out.print(", First: " + first_name);
                System.out.print(", Last: " + last_name);
                System.out.print(", Email: " + email);
                System.out.println(", Company ID: " + company_id);
            }

            //Steps 6 and 7:  Close open things
            stmt.close();
            conn.close();
        } catch (
                SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void retrieveDataFromEmployeesWithTableJoin()  {
        try {
            //Step 2:  Open a connection to the database
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            //Step 3 Prepare a SQL query
            stmt = conn.createStatement();
            String sql = ("SELECT first_name, last_name, email, company_name FROM EMPLOYEES" +
                    " JOIN COMPANIES ON COMPANIES.company_id = EMPLOYEES.company_id");

            //Step 4:  Execute the SQL statement
            ResultSet rs = stmt.executeQuery(sql);

            //Step 5:  Handle the response.
            while(rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String company_name = rs.getString("company_name");

                System.out.print("First: " + first_name);
                System.out.print(", Last: " + last_name);
                System.out.print(", Email: " + email);
                System.out.println(", Company Name: " + company_name);
            }

            //Steps 6 and 7:  Close open things
            stmt.close();
            conn.close();
        } catch (
                SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}

