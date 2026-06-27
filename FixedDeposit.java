/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firstbankug;



import java.time.LocalDate;


public class FixedDeposit extends Account {
    public FixedDeposit(String firstName, String lastName, String nin, String email, 
                        String phoneNumber, String pin, String dob, String branch) {
        super(firstName, lastName, nin, email, phoneNumber, pin, dob, branch);
    }

    @Override
    public double minimumDeposit() {
        return 1000000; 
    }

    @Override
    public String getAccountType() {
        return "Fixed Deposit Account";
    }
}