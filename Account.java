/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firstbankug;


import java.time.LocalDate;
import java.time.Period;

public abstract class Account {
    private final String firstName;
    private final String lastName;
    private final String nin;
    private final String email;
    private final String phoneNumber;
    private final String pin;
    private final String dob;
    private final String branch;

    public Account(String firstName, String lastName, String nin, String email, 
                   String phoneNumber, String pin, String dob, String branch) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.dob = dob;
        this.branch = branch;
    }

    
    public abstract double minimumDeposit();
    
    
    public abstract String getAccountType();

    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPin() { return pin; }
    public String getDob() { return dob; }
    public String getBranch() { return branch; }
}
