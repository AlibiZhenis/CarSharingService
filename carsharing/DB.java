package carsharing;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    private Connection connection;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL;

    public DB(String dbName) throws ClassNotFoundException, SQLException {
        if(dbName==null || dbName.isEmpty()){
            dbName="carsharing";
        }
        DB_URL = "jdbc:h2:./src/carsharing/db/" + dbName;

        Class.forName(JDBC_DRIVER);
        System.out.println("Connecting to database...");
        connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(true);
//        dropTables();
        createCompanyTable();
        createCarTable();
        createCustomerTable();
    }

    public void dropTables() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DROP TABLE CUSTOMER";
        statement.executeUpdate(query);
        query = "DROP TABLE CAR";
        statement.executeUpdate(query);
        query = "DROP TABLE COMPANY";
        statement.executeUpdate(query);
        statement.close();
    }

    public void createCustomerTable() throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INTEGER," +
                "FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID)" +
                ")";
        statement.executeUpdate(query);
        statement.close();
    }

    public void createCompanyTable() throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                " NAME VARCHAR(255) UNIQUE NOT NULL)";
        statement.executeUpdate(query);
        statement.close();
    }

    public void createCarTable() throws SQLException{
        Statement statement = connection.createStatement();
        String query =  "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                " NAME VARCHAR(255) UNIQUE NOT NULL," +
                "COMPANY_ID INTEGER NOT NULL," +
                "FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID)" +
                ")";
        statement.executeUpdate(query);
        statement.close();
    }

    public void returnCar(int customerID) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "UPDATE CUSTOMER SET RENTED_CAR_ID=NULL WHERE ID=%d".formatted(customerID);
        statement.executeUpdate(query);
    }
    public void rentCar(int customerID, int carID) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "UPDATE CUSTOMER SET RENTED_CAR_ID=%d WHERE ID=%d".formatted(carID, customerID);
        statement.executeUpdate(query);
    }

    public ArrayList getCompanies() throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT * FROM COMPANY";
        ResultSet rs = statement.executeQuery(query);

        ArrayList ans = new ArrayList<Company>();
        while(rs.next()){
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            ans.add(new Company(id, name));
        }


        statement.close();
        return ans;
    }

    public boolean isRented(Car car) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT RENTED_CAR_ID FROM CUSTOMER";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            int id = rs.getInt("RENTED_CAR_ID");
            if(id==car.getId()){
                return true;
            }
        }
        return false;
    }

    public Company getCompany(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT * FROM COMPANY WHERE ID=%s".formatted(id);
        ResultSet rs = statement.executeQuery(query);

        rs.next();
        String name = rs.getString("NAME");

        return new Company(id, name);
    }

    public Car getCar(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT * FROM CAR WHERE ID=%s".formatted(id);
        ResultSet rs = statement.executeQuery(query);

        rs.next();
        int companyID = rs.getInt("COMPANY_ID");
        String name = rs.getString("NAME");

        return new Car(id, companyID, name);
    }

    public void addCompany(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "INSERT INTO COMPANY(NAME) " +
                "VALUES('%s')".formatted(name);
        statement.executeUpdate(query);

        statement.close();
    }

    public void addCar(String name, int companyID) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "INSERT INTO CAR(NAME, COMPANY_ID) " +
                "VALUES('%s', %d)".formatted(name, companyID);
        statement.executeUpdate(query);

        statement.close();
    }

    public void addCustomer(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO CUSTOMER(NAME, RENTED_CAR_ID) " +
                "VALUES('%s', NULL)".formatted(name);
        statement.executeUpdate(query);

        statement.close();
    }

    public ArrayList<Car> getCars(int companyID) throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT * FROM CAR WHERE COMPANY_ID=%d".formatted(companyID);
        ResultSet rs = statement.executeQuery(query);

        ArrayList ans = new ArrayList<Car>();
        while(rs.next()){
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            ans.add(new Car(id, companyID, name));
        }


        statement.close();
        return ans;
    }

    public ArrayList<Customer> getCustomers() throws SQLException {
        Statement statement = connection.createStatement();
        String query =  "SELECT * FROM CUSTOMER";
        ResultSet rs = statement.executeQuery(query);

        ArrayList ans = new ArrayList<Customer>();
        while(rs.next()){
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            int rented_car_id = rs.getInt("RENTED_CAR_ID");
            ans.add(new Customer(id, rented_car_id, name));
        }


        statement.close();
        return ans;
    }
}
