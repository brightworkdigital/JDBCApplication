package com.example.h2withjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SuppressWarnings("SqlResolve")
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

        // In older versions of JDBC, before obtaining a connection, we first had to
        // initialize the JDBC driver by calling the Class.forName method.
        // As of JDBC 4.0, all drivers that are found in the classpath are automatically loaded.
//        Step 1:  Set up the driver
//        try {
//            Class.forName(JDBC_DRIVER);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        createRegistrationTable();
        addDataToRegistrationTable();
        retrieveDataFromRegistrationTable();
        updateAndRetrieveDataFromRegistration();
        retrieveDataFromEmployees();
        retrieveDataFromEmployeesWithTableJoin();
        demoLittleBillyTables();
    }

    private static void demoLittleBillyTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            System.out.println("\nLittle Billy Tables:");

//            String name = "Bill";

            // SQL injection attack - Note SQL statement to handle trailing single-quote
            String name = "Bill'; drop table REGISTRATION; set @tmp='gotcha";

//            String sql = "select * from REGISTRATION where first='%s'".formatted(name);
//            try (ResultSet rs = stmt.executeQuery(sql)) {
//                printResultSet(rs);
//            }

            String psSql = "select * from REGISTRATION where first=?";
            try (PreparedStatement ps = conn.prepareStatement(psSql)) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                printResultSet(rs);
            } catch (SQLException ex) {
                System.err.println("There was a problem running the prepared statement.");
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }



    }

    private static void createRegistrationTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = """
                    CREATE TABLE REGISTRATION
                    (id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    first VARCHAR(255),
                    last VARCHAR(255),
                    age INTEGER);""";

            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

        public static void addDataToRegistrationTable()  {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                String sql = "INSERT INTO REGISTRATION (first, last, age) VALUES ('Bill', 'Fairfield', 71)";

                stmt.executeUpdate(sql);

            } catch (SQLException ex) {
                throw new RuntimeException("Error while adding data to registration table", ex);
            }
        }

        public static void retrieveDataFromRegistrationTable()  {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                try (ResultSet rs = stmt.executeQuery("SELECT * FROM REGISTRATION")) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String first = rs.getString("first");
                        String last = rs.getString("last");
                        int age = rs.getInt("age");

                        System.out.println("ID: " + id + ", First: " + first + ", Last: " +
                                last + ", Age: " + age);
                    }
                }

            } catch (SQLException ex) {
                throw new RuntimeException("Error while retrieving data from registration table", ex);
            }
        }

    public static void updateAndRetrieveDataFromRegistration() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Update data
            String updateSql = "UPDATE REGISTRATION SET age = 30 WHERE first='Bill'";
            stmt.executeUpdate(updateSql);

            // Retrieve and display data
            String selectSql = "SELECT first, last, age FROM REGISTRATION";
            try (ResultSet rs = stmt.executeQuery(selectSql)) {
                while (rs.next()) {
                    String first = rs.getString("first");
                    String last = rs.getString("last");
                    int age = rs.getInt("age");

                    System.out.print(", First: " + first);
                    System.out.print(", Last: " + last);
                    System.out.println(", Age: " + age);
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException("Error while updating and retrieving data from registration table", se);
        }
    }

    public static void retrieveDataFromEmployees()  {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM EMPLOYEES")) {

            while (rs.next()) {
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

        } catch (SQLException ex) {
            throw new RuntimeException("Error while retrieving data from employees table", ex);
        }
    }

    public static void retrieveDataFromEmployeesWithTableJoin()  {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("""
                    SELECT E.first_name, E.last_name, E.email, C.company_name
                    FROM EMPLOYEES E
                    JOIN COMPANIES C ON C.company_id = E.company_id""");

            while (rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String company_name = rs.getString("company_name");

                System.out.print("First: " + first_name);
                System.out.print(", Last: " + last_name);
                System.out.print(", Email: " + email);
                System.out.println(", Company Name: " + company_name);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error while retrieving data from employees table with table join", ex);
        }
    }

    private static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.print((metadata.getColumnName(i) + "\t"));
        }
        System.out.println();
        while(rs.next()) {
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                row += rs.getString(i) + "\t";
            }
            System.out.println(row);
        }
    }
}

