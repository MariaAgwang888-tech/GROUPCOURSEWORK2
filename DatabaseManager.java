/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firstbankug.databasemanager;

import firstbankug.Account;
import firstbankug.JointAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class DatabaseManager {
    
    
    private static final String DB_URL = "jdbc:ucanaccess://C:\\Users\\USER\\Documents\\NetBeansProjects\\firstbank_form.accdb";

   
    public static synchronized String generateAccountNumber(String branchName) throws SQLException {
        String branchCode = switch (branchName.trim().toLowerCase()) {
            case "kampala" -> "KLA";
            case "gulu" -> "GULU";
            case "mbarara" -> "MBRA";
            case "jinja" -> "JNJ";
            case "mbale" -> "MBL";
            default -> "GEN";
        }; 

        int currentYear = LocalDate.now().getYear();
        int sequentialCounter = 1;
        
        String query = "SELECT account_number FROM Accounts WHERE account_number LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, branchCode + "-" + currentYear + "-%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sequentialCounter++; 
                }
            }
        }
        
        return String.format("%s-%d-%06d", branchCode, currentYear, sequentialCounter);
    }

   
    public static void saveAccount(Account account, String accountNumber, double openingDeposit) throws SQLException {
        
        String sql = "INSERT INTO Accounts (account_number, first_name, last_name, nin, secondary_nin, email, "
                   + "phone_number, pin, dob, account_type, branch, opening_deposit) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            stmt.setString(2, account.getFirstName());
            stmt.setString(3, account.getLastName());
            stmt.setString(4, account.getNin());
            
            // Check if it's a Joint Account to grab the secondary NIN, otherwise insert null/empty
            if (account instanceof JointAccount jointAccount) {
                stmt.setString(5, jointAccount.getSecondaryNin());
            } else {
                stmt.setString(5, null);
            }
            
            stmt.setString(6, account.getEmail());
            stmt.setString(7, account.getPhoneNumber());
            stmt.setString(8, account.getPin());
            
            // account.getDob() returns a String ("YYYY-MM-DD") which easily parses into java.sql.Date
            stmt.setDate(9, java.sql.Date.valueOf(account.getDob()));
            stmt.setString(10, account.getAccountType());
            stmt.setString(11, account.getBranch());
            stmt.setDouble(12, openingDeposit);
            
            stmt.executeUpdate();
        }
    }
}
