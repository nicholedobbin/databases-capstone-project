/*===================================================================================================================
    The "Contractors" class uses Java and MySQL to connect to the database 'PoisePMS', and contains the
    following methods to read and print data about the contractors in the database:

    ●   Constructor for contractor instances:
            - Unused at present but available if Poise wants to add new contractors to the DB at a later stage.
    ●   Get Contractors List:
            - gets and prints all details for all contractors in the database.
    ●   Choose Contractor:
            - shows all available contractors, gets user input for the contractor they want to select and
              returns the chosen contractor's ID number.
 ===================================================================================================================*/

import java.sql.*;
import java.util.Scanner;

/**
 * This is the contractors class which reads and prints data about the contractors in the database.
 * <p>
 * The contractors class uses Java and MySQL to connect to the database 'PoisePMS', and contains methods to
 * read and print data.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Contractors {
    // Initialise variables to store contractors' information.
    /**
     * Integer value for contractor id number
     */
    int contractorID = 0;
    /**
     * String value for contractor name
     */
    String contractorName = "";
    /**
     * Integer value for contractor phone number
     */
    int contractorPhone;
    /**
     * String value for contractor email address
     */
    String contractorEmail = "";
    /**
     * Integer value for contractor physical address
     */
    String contractorAddress = "";


    // ----------------------------------- CONSTRUCTOR METHOD ------------------------------------------------------
    /**
     * Constructor method for contractors instances
     *
     * @param contractorID the contractor's id number
     * @param contractorName the contractor's name
     * @param contractorPhone the contractor's phone number
     * @param contractorEmail the contractor's email address
     * @param contractorAddress the contractor's physical address
     */
    public Contractors(int contractorID, String contractorName, int contractorPhone, String contractorEmail,
                       String contractorAddress)
    {
        this.contractorID = contractorID;
        this.contractorName = contractorName;
        this.contractorPhone = contractorPhone;
        this.contractorEmail = contractorEmail;
        this.contractorAddress = contractorAddress;
    }

    // ----------------------------- METHOD: GET CONTRACTORS LIST --------------------------------------------------
    /**
     * This method gets and prints all details for all contractors in the database.
     * <p>
     * It is called in the {@link #chooseContractor()} method.
     */
    public static void getContractorsList() {
        try {
            // Add your url, user and password for your database in the empty strings below.
            String connectionURL = "";
            String connectionUser = "";
            String connectionPassword = "";

            // Connect to 'PoisePMS' database and set up statements and queries.
            Connection connection = DriverManager.getConnection(
                    connectionURL,
                    connectionUser,
                    connectionPassword);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM contractors");


            System.out.println("\n========== LIST OF CONTRACTORS ==========\n\n"
                    + "[ ID    NAME             PHONE        EMAIL                  ADDRESS ]");

            // Print all architects' details.
            while (results.next()) {
                System.out.println(
                        "[ " + results.getInt("contractor_id")
                                + "     " + results.getString("contractor_name")
                                + "     " + results.getInt("contractor_phone")
                                + "     " + results.getString("contractor_email")
                                + "     " + results.getString("contractor_address")
                                + " ]"
                );
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // -------------------------------- METHOD: CHOOSE CONTRACTOR ---------------------------------------------------
    /**
     * This method gets the user's selection of a contractor in the database.
     * <p>
     * It shows all available contractors by calling the {@link #getContractorsList()} method, gets user input
     * for the contractor they want to select and returns the chosen contractor's ID number.
     *
     * @return returns the chosen contractor's ID number
     */
    public static int chooseContractor(){
        int inputtedID = 0;
        String selectedContractor;

        try {
            // Add your url, user and password for your database in the empty strings below.
            String connectionURL = "";
            String connectionUser = "";
            String connectionPassword = "";

            // Connect to 'PoisePMS' database and set up statements and queries.
            Connection connection = DriverManager.getConnection(
                    connectionURL,
                    connectionUser,
                    connectionPassword);

            Statement statement = connection.createStatement();
            ResultSet results;

            // Print list of contractors.
            getContractorsList();

            // Create Scanner object to get user input for choice of contractor.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("\nEnter the ID of the Contractor you want assigned to this project:");
            inputtedID = Integer.parseInt(inputScanner.nextLine());
            results = statement.executeQuery
                    ("SELECT * FROM contractors WHERE contractor_id = " + inputtedID + ";");

            // If the inputtedID matches a contractorID in the table, print out the contractor name and ID.
            // Else, set the inputtedID to 0.
            if (results.next()) {
                selectedContractor = results.getString("contractor_name");
                System.out.println("Contractor " + selectedContractor + " with the ID number " + inputtedID
                        + " has been assigned to this project.");
            } else {
                inputtedID = 0;
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }

        // Return the selected ID.
        return inputtedID;
    }
}
