package database;

import java.security.AccessControlContext;
import java.sql.*;

class Database {
    private static Database database;
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306";
    private static String account;
    private static String password;
    private String sqlUrl;
    private Connection connection;

    private String tableName;

    public static void main(String[] args) {
        account="root";
        password="2972358040WCJwcj";
        Database database = Database.newInstance(account, password);
        System.out.println("初始化完成");
        database.createSchema("user_dates");
        System.out.println("数据库创建完成");
        database.useSchema("user_dates");
        System.out.println("目标数据库已改变");
        database.createTable("user","account");
        System.out.println("数据表创建完成");
//        database.insert();
    }

    private Database(String account, String password) {
        this.account = account;
        this.password = password;
        init();
    }

    public final static Database newInstance(String account, String password) {
        if (database == null) {
            database = new Database(account, password);
        }
        return database;
    }

    private void init() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, account, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    void createSchema(String schemaName) {
        String CTEDB = "CREATE DATABASE IF NOT EXIST"+schemaName;
        try {
            PreparedStatement statement = connection.prepareStatement(CTEDB);
//            statement.setString(1,schemaName);
//            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void crateTableNonExist(String tableName,String account)
    {
        String cmd= "CREATE TABLE IF NOT EXIST ?(? VARCHAR NOT NULL)";

    }

    void createTable(String tableName,String account) {
        this.tableName = tableName;
        String CTB = "CREATE TABLE "+tableName+"("+account+" VARCHAR(50) NOT NULL,"
                +password+" VARCHAR(50) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=utf8";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CTB);
//            preparedStatement.setString(1,tableName);
//            preparedStatement.setString(2,account);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void useSchema(String schemaName) {
        String s = "USE " + schemaName;
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(s);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insert(String account,String accountId) {
        String INSTER_INTO= """
                INSERT INTO ?
                """;
    }

    String select(String account) {
        // if判断item是否符合规范
        // 生成报文发送浏览器，告知account不符合规范
        String searchCmd = "SELECT ? FROM " + tableName + "WHERE ACCOUNT = " + account;
        return searchCmd;
    }

    void register(String account, String password) {
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
