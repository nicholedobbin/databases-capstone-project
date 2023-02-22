/*===================================================================================================================
    The "Managers" class uses Java and MySQL to connect to the database 'PoisePMS', and contains the
    following methods to read and print data about the project managers in the database:

    ●   Constructor for project manager instances:
            - Unused at present but available if Poise wants to add new project managers to the DB at a later stage.
    ●   Get Managers List:
            - gets and prints all details for all project managers in the database.
    ●   Choose Manager:
            - shows all available project managers, gets user input for the project manager they want to select and
              returns the chosen project manager's ID number.
 ===================================================================================================================*/

import java.sql.*;
import java.util.Scanner;

/**
 * This is the managers class which reads and prints data about the project managers in the database.
 * <p>
 * The managers class uses Java and MySQL to connect to the database 'PoisePMS', and contains methods to
 * read and print data.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Managers {
    // Initialise variables to store managers' information.
    /**
     * Integer value for project manager id number
     */
    int projManagerID = 0;
    /**
     * String value for project manager name
     */
    String projManagerName = "";
    /**
     * Integer value for project manager phone number
     */
    int projManagerPhone;
    /**
     * String value for project manager email address
     */
    String projManagerEmail = "";
    /**
     * Integer value for project manager physical address
     */
    String projManagerAddress = "";


    // ----------------------------------- CONSTRUCTOR METHOD ------------------------------------------------------
    /**
     * Constructor method for managers instances
     *
     * @param projManagerID the project manager's id number
     * @param projManagerName the project manager's name
     * @param projManagerPhone the project manager's phone number
     * @param projManagerEmail the project manager's email address
     * @param projManagerAddress the project manager's physical address
     */
    public Managers(int projManagerID, String projManagerName, int projManagerPhone, String projManagerEmail,
                    String projManagerAddress)
    {
        this.projManagerID = projManagerID;
        this.projManagerName = projManagerName;
        this.projManagerPhone = projManagerPhone;
        this.projManagerEmail = projManagerEmail;
        this.projManagerAddress = projManagerAddress;
    }

    // ----------------------------- METHOD: GET MANAGERS LIST -----------------------------------------------------
    /**
     * This method gets and prints all details for all project managers in the database.
     * <p>
     * It is called in the {@link #chooseManager()} method.
     */
    public static void getManagersList() {
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
            ResultSet results = statement.executeQuery("SELECT * FROM managers");

            System.out.println("\n========== LIST OF PROJECT MANAGERS ==========\n\n"
                    + "[ ID    NAME            PHONE        EMAIL                  ADDRESS ]");

            // Print all managers' details.
            while (results.next()) {
                System.out.println(
                        "[ " + results.getInt("proj_man_id")
                                + "     " + results.getString("proj_man_name")
                                + "     " + results.getInt("proj_man_phone")
                                + "     " + results.getString("proj_man_email")
                                + "     " + results.getString("proj_man_address")
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

    // ----------------------------- METHOD: CHOOSE PROJECT MANAGER -----------------------------------------------
    // This method shows all available managers, gets user input for the manager they want to select
    // and returns the chosen manager's ID number.
    /**
     * This method gets the user's selection of a project manager in the database.
     * <p>
     * It shows all available project managers by calling the {@link #getManagersList()} method, gets user input
     * for the project manager they want to select and returns the chosen project manager's ID number.
     *
     * @return returns the chosen project manager's ID number
     */
    public static int chooseManager(){
        int inputtedID = 0;
        String selectedManager;

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

            // Print list of project managers.
            getManagersList();

            // Create Scanner object to get user input for choice of project manager.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("\nEnter the ID of the Project Manager you want assigned to this project:");
            inputtedID = Integer.parseInt(inputScanner.nextLine());
            results = statement.executeQuery("SELECT * FROM managers WHERE proj_man_id = " + inputtedID + ";");

            // If the inputtedID matches a managerID in the table, print out the manager name and ID.
            // Else, set the inputtedID to 0.
            if (results.next()) {
                selectedManager = results.getString("proj_man_name");
                System.out.println("Project Manager " + selectedManager + " with the ID number " + inputtedID
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
