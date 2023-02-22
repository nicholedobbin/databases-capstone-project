/*===================================================================================================================
    The "Architects" class uses Java and MySQL to connect to the database 'PoisePMS', and contains the
    following methods to read and print data about the architects in the database:

    ●   Constructor for architect instances:
            - Unused at present but available if Poise wants to add new architects to the DB at a later stage.
    ●   Get Architects List:
            - gets and prints all details for all architects in the database.
    ●   Choose Architect:
            - shows all available architects, gets user input for the architect they want to select and
              returns the chosen architect's ID number.
 ===================================================================================================================*/
import java.sql.*;
import java.util.Scanner;

/**
 * This is the architects class which reads and prints data about the architects in the database.
 * <p>
 * The architects class uses Java and MySQL to connect to the database 'PoisePMS', and contains methods to
 * read and print data.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Architects {
    // Initialise variables to store architects' information.
    /**
     * Integer value for architect id number
     */
    int architectID = 0;
    /**
     * String value for architect name
     */
    String architectName = "";
    /**
     * Integer value for architect phone number
     */
    int architectPhone;
    /**
     * String value for architect email address
     */
    String architectEmail = "";
    /**
     * Integer value for architect physical address
     */
    String architectAddress = "";


    // ----------------------------------- CONSTRUCTOR METHOD ------------------------------------------------------
    /**
     * Constructor method for architects instances
     *
     * @param architectID the architect's id number
     * @param architectName the architect's name
     * @param architectPhone the architect's phone number
     * @param architectEmail the architect's email address
     * @param architectAddress the architect's physical address
     */
    public Architects(int architectID, String architectName, int architectPhone, String architectEmail,
                String architectAddress)
    {
        this.architectID = architectID;
        this.architectName = architectName;
        this.architectPhone = architectPhone;
        this.architectEmail = architectEmail;
        this.architectAddress = architectAddress;
    }

    // ------------------------------- METHOD: GET ARCHITECTS LIST -------------------------------------------------
    /**
     * This method gets and prints all details for all architects in the database.
     * <p>
     * It is called in the {@link #chooseArchitect()} method.
     */
    public static void getArchitectsList() {
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
            ResultSet results = statement.executeQuery("SELECT * FROM architects");

            System.out.println("\n========== LIST OF ARCHITECTS ==========\n\n"
                    + "[ ID    NAME             PHONE        EMAIL                  ADDRESS ]");

            // Print all architects' details.
            while (results.next()) {
                System.out.println(
                        "[ " + results.getInt("architect_id")
                                + "     " + results.getString("architect_name")
                                + "     " + results.getInt("architect_phone")
                                + "     " + results.getString("architect_email")
                                + "     " + results.getString("architect_address")
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

    // -------------------------------- METHOD: CHOOSE ARCHITECT ---------------------------------------------------
    /**
     * This method gets the user's selection of an architect in the database.
     * <p>
     * It shows all available architects by calling the {@link #getArchitectsList()} method, gets user input
     * for the architect they want to select and returns the chosen architect's ID number.
     *
     * @return returns the chosen architect's ID number
     */
    public static int chooseArchitect(){
        int inputtedID = 0;
        String selectedArchitect;

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

            // Print list of architects.
            getArchitectsList();

            // Create Scanner object to get user input for choice of architect.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("\nEnter the ID of the Architect you want assigned to this project:");
            inputtedID = Integer.parseInt(inputScanner.nextLine());

            // Get all architects' details from DB.
            results = statement.executeQuery("SELECT * FROM architects WHERE architect_id = " + inputtedID + ";");

            // If the inputtedID matches an architectID in the table, print out the architect name and ID.
            // Else, set the inputtedID to 0.
            if (results.next()) {
                selectedArchitect = results.getString("architect_name");
                System.out.println("Architect " + selectedArchitect + " with the ID number " + inputtedID
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





