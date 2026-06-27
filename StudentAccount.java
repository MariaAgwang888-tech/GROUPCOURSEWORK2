/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firstbankug;

import java.time.LocalDate;
import java.time.Period;


public class StudentAccount extends Account {
    public StudentAccount(String firstName, String lastName, String nin, String email, 
                          String phoneNumber, String pin, String dob, String branch) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dob, branch);
    }

    @Override
    public double minimumDeposit() {
        return 10000; // e.g., UGX 10,000 (accessible tier)
    }

    @Override
    public String getAccountType() {
        return "Student Account";
    }
}