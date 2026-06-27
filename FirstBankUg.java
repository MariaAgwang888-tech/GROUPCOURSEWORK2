/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package firstbankug;


import firstbankug.databasemanager.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class FirstBankUg extends Application {
    
    private TextField txtFirstName, txtLastName, txtNIN, txtSecondaryNIN, txtEmail, txtConfirmEmail, txtPhone, txtDeposit;
    private PasswordField txtPIN, txtConfirmPIN;
    private ComboBox<Integer> cmbYear, cmbDay;
    private ComboBox<String> cmbMonth, cmbAccountType, cmbBranch;
    private TextArea areaSummary;

    private Label errFN, errLN, errNIN, errSecNIN, errEmail, errCEmail, errPhone, errPIN, errCPIN, errDOB, errDeposit;
    private Label lblSecondaryNIN;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("First Bank Uganda - New Account Registration Portal [JavaFX]");

        // Main layout containing Title Header, Center Form, and Bottom Output Section
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #F4F6F9;");

        // Header Section
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: #14325A; -fx-background-radius: 5;");
        Label lblTitle = new Label("FIRST BANK UGANDA");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblTitle.setTextFill(Color.WHITE);
        Label lblSubTitle = new Label("New Bank Account Opening Form");
        lblSubTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        lblSubTitle.setTextFill(Color.web("#C8DCF0"));
        headerBox.getChildren().addAll(lblTitle, lblSubTitle);
        root.setTop(headerBox);

        // Core Form Grid Structure
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #DCDCDC; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtFirstName = new TextField(); txtLastName = new TextField();
        txtNIN = new TextField(); txtSecondaryNIN = new TextField();
        txtEmail = new TextField(); txtConfirmEmail = new TextField();
        txtPhone = new TextField(); txtPIN = new PasswordField();
        txtConfirmPIN = new PasswordField(); txtDeposit = new TextField();

        cmbAccountType = new ComboBox<>();
        cmbAccountType.getItems().addAll("Savings", "Current", "Fixed Deposit", "Student", "Joint");
        cmbAccountType.getSelectionModel().selectFirst();

        cmbBranch = new ComboBox<>();
        cmbBranch.getItems().addAll("Kampala", "Gulu", "Mbarara", "Jinja", "Mbale");
        cmbBranch.getSelectionModel().selectFirst();

        cmbYear = new ComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 75; y <= currentYear - 17; y++) cmbYear.getItems().add(y);
        cmbYear.getSelectionModel().select((int) (cmbYear.getItems().size() * 0.7)); 

        cmbMonth = new ComboBox<>();
        cmbMonth.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        cmbMonth.getSelectionModel().selectFirst();

        cmbDay = new ComboBox<>();
        updateDaysInMonth(); // Population logic trigger Execution

        cmbYear.setOnAction(e -> updateDaysInMonth());
        cmbMonth.setOnAction(e -> updateDaysInMonth());

        errFN = createErrorLabel(); errLN = createErrorLabel(); errNIN = createErrorLabel();
        errSecNIN = createErrorLabel(); errEmail = createErrorLabel(); errCEmail = createErrorLabel();
        errPhone = createErrorLabel(); errPIN = createErrorLabel(); errCPIN = createErrorLabel();
        errDOB = createErrorLabel(); errDeposit = createErrorLabel();

        // Map Form Nodes to Grid Coordinates
        int r = 0;
        addFormRow(grid, "First Name:", txtFirstName, errFN, r++);
        addFormRow(grid, "Last Name:", txtLastName, errLN, r++);
        addFormRow(grid, "National ID (NIN):", txtNIN, errNIN, r++);

        lblSecondaryNIN = new Label("Secondary NIN:");
        lblSecondaryNIN.setVisible(false); txtSecondaryNIN.setVisible(false);
        grid.add(lblSecondaryNIN, 0, r); grid.add(txtSecondaryNIN, 1, r); grid.add(errSecNIN, 2, r++);

        addFormRow(grid, "Email Address:", txtEmail, errEmail, r++);
        addFormRow(grid, "Confirm Email:", txtConfirmEmail, errCEmail, r++);
        addFormRow(grid, "Phone Number:", txtPhone, errPhone, r++);
        addFormRow(grid, "Security PIN:", txtPIN, errPIN, r++);
        addFormRow(grid, "Confirm PIN:", txtConfirmPIN, errCPIN, r++);

        // Date layout row composite mapping assembly
        HBox dobBox = new HBox(5);
        dobBox.getChildren().addAll(new Label("Y:"), cmbYear, new Label("M:"), cmbMonth, new Label("D:"), cmbDay);
        grid.add(new Label("Date of Birth:"), 0, r); grid.add(dobBox, 1, r); grid.add(errDOB, 2, r++);

        addFormRow(grid, "Account Type:", cmbAccountType, new Label(""), r++);
        addFormRow(grid, "Preferred Branch:", cmbBranch, new Label(""), r++);
        addFormRow(grid, "Opening Deposit (UGX):", txtDeposit, errDeposit, r++);

        cmbAccountType.setOnAction(e -> {
            boolean isJoint = "Joint".equals(cmbAccountType.getValue());
            lblSecondaryNIN.setVisible(isJoint);
            txtSecondaryNIN.setVisible(isJoint);
            if (!isJoint) { txtSecondaryNIN.clear(); errSecNIN.setText(""); }
        });

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Control Panel and Summary Interface Box Setup
        VBox southBox = new VBox(10);
        southBox.setPadding(new Insets(10, 0, 0, 0));

        HBox actionButtonsBox = new HBox(20);
        actionButtonsBox.setAlignment(Pos.CENTER);
        Button btnSubmit = new Button("Submit Registration");
        btnSubmit.setStyle("-fx-background-color: #14325A; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnReset = new Button("Reset Form");
        btnReset.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-weight: bold;");
        actionButtonsBox.getChildren().addAll(btnSubmit, btnReset);

        VBox summaryBox = new VBox(5);
        Label lblSummary = new Label("Account Summary is Below:");
        lblSummary.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        areaSummary = new TextArea();
        areaSummary.setPrefRowCount(4);
        areaSummary.setEditable(false);
        areaSummary.setStyle("-fx-font-family: 'Courier New'; -fx-control-inner-background: #F0F2F5;");
        summaryBox.getChildren().addAll(lblSummary, areaSummary);

        southBox.getChildren().addAll(actionButtonsBox, summaryBox);
        root.setBottom(southBox);

        // Map Button Actions
        btnSubmit.setOnAction(e -> handleFormSubmission());
        btnReset.setOnAction(e -> clearFormFields());

        primaryStage.setScene(new Scene(root, 950, 820));
        primaryStage.show();
    }

    private Label createErrorLabel() {
        Label l = new Label("");
        l.setTextFill(Color.RED);
        l.setFont(Font.font("Arial", 11));
        return l;
    }

    private void addFormRow(GridPane grid, String labelText, Control control, Label errLabel, int row) {
        grid.add(new Label(labelText), 0, row);
        grid.add(control, 1, row);
        grid.add(errLabel, 2, row);
    }

    private void updateDaysInMonth() {
        if (cmbYear.getValue() == null || cmbMonth.getValue() == null) return;
        int year = cmbYear.getValue();
        int monthIdx = cmbMonth.getSelectionModel().getSelectedIndex();

        LocalDate date = LocalDate.of(year, monthIdx + 1, 1);
        int totalDays = date.lengthOfMonth(); 

        Integer selectedDay = cmbDay.getValue();
        cmbDay.getItems().clear();
        for (int d = 1; d <= totalDays; d++) cmbDay.getItems().add(d);

        if (selectedDay != null && selectedDay <= totalDays) {
            cmbDay.setValue(selectedDay);
        } else {
            cmbDay.getSelectionModel().selectFirst();
        }
    }

    private void handleFormSubmission() {
        clearErrorLabels();
        boolean failed = false;
        StringBuilder dialogErrors = new StringBuilder("Please fix the matching input violations:\n");

        String fn = txtFirstName.getText().trim();
        if (!fn.matches("[a-zA-Z]{2,30}")) {
            errFN.setText("Invalid (Letters only, 2-30 chars)");
            dialogErrors.append("- First Name structure format is invalid.\n");
            failed = true;
        }
        String ln = txtLastName.getText().trim();
        if (!ln.matches("[a-zA-Z]{2,30}")) {
            errLN.setText("Invalid (Letters only, 2-30 chars)");
            dialogErrors.append("- Last Name structure format is invalid.\n");
            failed = true;
        }

        String nin = txtNIN.getText().trim().toUpperCase();
        txtNIN.setText(nin);
        if (!nin.matches("[A-Z0-9]{14}")) {
            errNIN.setText("Required, exactly 14 uppercase alphanumeric chars");
            dialogErrors.append("- Primary NIN must contain exactly 14 uppercase alphanumeric characters.\n");
            failed = true;
        }

        String accType = cmbAccountType.getValue();
        String secNin = txtSecondaryNIN.getText().trim().toUpperCase();
        if ("Joint".equals(accType)) {
            txtSecondaryNIN.setText(secNin);
            if (!secNin.matches("[A-Z0-9]{14}")) {
                errSecNIN.setText("Required for Joint, 14 uppercase alphanumeric chars");
                dialogErrors.append("- Joint Accounts require a valid secondary NIN configuration entry.\n");
                failed = true;
            }
        }

        String email = txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errEmail.setText("Invalid email structural format pattern");
            dialogErrors.append("- Email address is invalid.\n");
            failed = true;
        } else if (!email.equalsIgnoreCase(confirmEmail)) {
            errCEmail.setText("Email matching verification failed");
            dialogErrors.append("- Email confirmation fields do not match.\n");
            failed = true;
        }

        String phone = txtPhone.getText().trim();
        if (!phone.matches("\\+256\\d{9}")) {
            errPhone.setText("Required structure format: +256XXXXXXXXX");
            dialogErrors.append("- Phone number must conform to the 12-digit Ugandan structure format (+256...).\n");
            failed = true;
        }

        String pin = txtPIN.getText().trim();
        String confirmPin = txtConfirmPIN.getText().trim();
        if (!pin.matches("\\d{4,6}")) {
            errPIN.setText("Must be a 4-6 digit numeric code");
            dialogErrors.append("- PIN configuration must contain between 4 and 6 numeric digits.\n");
            failed = true;
        } else if (pin.matches("^(\\d)\\1+$")) {
            errPIN.setText("Identical repeated digits are blocked");
            dialogErrors.append("- Security Check Failed: PIN cannot consist of entirely identical values.\n");
            failed = true;
        } else if (!pin.equals(confirmPin)) {
            errCPIN.setText("PIN matching verification failed");
            dialogErrors.append("- Security PIN confirmation fields do not match.\n");
            failed = true;
        }

        int birthY = cmbYear.getValue();
        int birthM = cmbMonth.getSelectionModel().getSelectedIndex() + 1;
        int birthD = cmbDay.getValue();
        LocalDate bdate = LocalDate.of(birthY, birthM, birthD);
        int age = Period.between(bdate, LocalDate.now()).getYears();

        if (age < 18 || age > 75) {
            errDOB.setText("Applicant must be 18-75 years of age");
            dialogErrors.append("- Onboarding is restricted to applicants between 18 and 75 years of age.\n");
            failed = true;
        } else if ("Student".equals(accType) && age > 25) {
            errDOB.setText("Student Account boundary limit is 18-25");
            dialogErrors.append("- Student accounts are restricted to applicants aged 25 and under.\n");
            failed = true;
        }

        double deposit = 0;
        boolean depositParsed = true;
        try {
            deposit = Double.parseDouble(txtDeposit.getText().trim());
        } catch (NumberFormatException nex) {
            errDeposit.setText("Must be a valid numeric calculation entry");
            dialogErrors.append("- Opening deposit value must be numeric.\n");
            depositParsed = false;
            failed = true;
        }

        Account model = null;
        String branch = cmbBranch.getValue();
        String dobString = bdate.toString();

        if (depositParsed) {
            model = switch (accType) {
                case "Savings" -> new SavingsAccount(fn, ln, nin, email, phone, pin, dobString, branch);
                case "Current" -> new CurrentAccount(fn, ln, nin, email, phone, pin, dobString, branch);
                case "Fixed Deposit" -> new FixedDeposit(fn, ln, nin, email, phone, pin, dobString, branch);
                case "Student" -> new StudentAccount(fn, ln, nin, email, phone, pin, dobString, branch);
                case "Joint" -> new JointAccount(fn, ln, nin, secNin, email, phone, pin, dobString, branch);
                default -> null;
            };

            if (model != null && deposit < model.minimumDeposit()) {
                errDeposit.setText("Minimum required: UGX " + String.format("%,.0f", model.minimumDeposit()));
                dialogErrors.append("- Entered deposit is lower than the required minimum tier for ").append(accType).append(" accounts.\n");
                failed = true;
            }
        }

        if (failed) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Validation Failure");
            alert.setHeaderText("Invalid Fields Detected");
            alert.setContentText(dialogErrors.toString());
            alert.showAndWait();
            areaSummary.clear();
            return;
        }

        // Database Generation and Insertion Block
        try {
            // 1. Generate the account number sequentially via DB lookup
            String accountNo = DatabaseManager.generateAccountNumber(branch); 

            // 2. Generate and update text output record summary window
            String outRecord;
            outRecord = String.format("ACC: %s | %s %s |\n%s | %s | DOB %s |\n%s | Deposit %,.0f | %s", 
                    accountNo, model.getFirstName(), model.getLastName(), model.getAccountType(),
                    model.getBranch(), model.getDob(), model.getPhoneNumber(), deposit, model.getEmail());
            areaSummary.setText(outRecord); 

            // 3. Persist the data directly into MS Access via dynamic parameters
            DatabaseManager.saveAccount(model, accountNo, deposit); 

            // 4. Trigger Success Notification Dialogue box
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Account Generated");
            successAlert.setHeaderText("Registration Processed Successfully");
            successAlert.setContentText("Account Number: " + accountNo + "\nData has been written to the database storage layer.");
            successAlert.showAndWait();
            
        } catch (SQLException sqlEx) {
            // Error fallbacks if database tracking driver throws anomalies
            Alert dbErrorAlert = new Alert(Alert.AlertType.ERROR);
            dbErrorAlert.setTitle("Database Engine Failure");
            dbErrorAlert.setHeaderText("SQL Write Execution Blocked");
            dbErrorAlert.setContentText("Reason: " + sqlEx.getMessage());
            dbErrorAlert.showAndWait();
            areaSummary.clear();
        }
    }

    private void clearErrorLabels() {
        errFN.setText(""); errLN.setText(""); errNIN.setText(""); errSecNIN.setText("");
        errEmail.setText(""); errCEmail.setText(""); errPhone.setText("");
        errPIN.setText(""); errCPIN.setText(""); errDOB.setText(""); errDeposit.setText("");
    }

    private void clearFormFields() {
        txtFirstName.clear(); txtLastName.clear(); txtNIN.clear(); txtSecondaryNIN.clear();
        txtEmail.clear(); txtConfirmEmail.clear(); txtPhone.clear(); txtDeposit.clear();
        txtPIN.clear(); txtConfirmPIN.clear();
        cmbAccountType.getSelectionModel().selectFirst();
        cmbBranch.getSelectionModel().selectFirst();
        cmbYear.getSelectionModel().selectFirst();
        cmbMonth.getSelectionModel().selectFirst();
        updateDaysInMonth();
        clearErrorLabels();
        areaSummary.clear(); 
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    } 
}