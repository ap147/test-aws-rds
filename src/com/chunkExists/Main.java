package com.chunkExists;

import java.sql.*;

public class Main {

    static Connection con = null;

    public static void main(String[] args) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            // Make sure your Amazon rds is public available and you have added ur ip to a security group or create one.
            // More info : https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithSecurityGroups.html
            con = DriverManager.getConnection("jdbc:mysql://endpoint","usr","password");

            if (!hashExists("bike")) {

                writeHash("bike");
            }
        }

        catch (SQLException ex) {

            // String describing the error
            System.out.println("SQLException: " + ex.getMessage());

            // Vendor specific error code
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        catch (ClassNotFoundException e) {
            // Executes if the driver can't be found
            e.printStackTrace();
        }
    }

    // Check if hash exists in database
    public static boolean hashExists(String hash) throws SQLException {

        System.out.println("- Checking if '" + hash + "' exists in database.");

        Statement sqlState = con.createStatement();
        String pickDB = "use peanut";
        String selectStuff = "SELECT * from hash_tbl WHERE hash = '"+ hash +"'";

        // Pick the database then run the query
        ResultSet rows = sqlState.executeQuery(pickDB);
        rows = sqlState.executeQuery(selectStuff);

        // if hash exists, print it and return true
        if(rows.next()) {

            System.out.println(rows.getString("hash"));
            System.out.println("- hash exists.");
            return true;
        }

        System.out.println("- hash does not exist.");
        return false;
    }

    public static void writeHash (String hash) throws SQLException {

        System.out.println("- Inserting '" + hash + "' into database." );
        Statement sqlState = con.createStatement();
        String pickDB = "use peanut";
        String selectStuff = "INSERT INTO hash_tbl (hash) VALUES ('"+ hash + "')";

        sqlState.executeQuery(pickDB);
        sqlState.executeUpdate(selectStuff);

        System.out.println("- '" + hash + "' successfully inserted.");
    }
}

//mysql> select * from hash_tbl;
//+---------+-----------------+
//| hash_id | hash            |
//+---------+-----------------+
//|       1 | DJFLKDJFKJDLKFJ |
//|       2 | dfdfdf          |
//|       3 | aaaas           |
//|       4 | pizza           |
//|       5 | bike            |
//+---------+-----------------+
//5 rows in set (0.19 sec)
//
//mysql>
