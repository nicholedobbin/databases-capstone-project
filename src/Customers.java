/*===================================================================================================================
    The "Customers" class uses Java and MySQL to connect to the database 'PoisePMS', and contains the
    following methods to read, write and print data about the customers in the database:

    ●   Constructor for customer instances:
            - Unused at present but available if Poise wants to add new customers to the DB at a later stage.
    ●   Get Customers List:
            - gets and prints all details for all customers in the database.
    ●   Choose Customer:
            - shows all available customers, gets user input for the customer they want to select and
              returns the chosen customer's ID number.
    ●   Add Customer:
            - gets customer information input from user and adds a new customer to the database.
 ===================================================================================================================*/
import java.sql.*;
import java.util.Scanner;

/**
 * This is the customers class which reads, writes and prints data about the customers in the database.
 * <p>
 * The customers class uses Java and MySQL to connect to the database 'PoisePMS', and contains methods to
 * read and print data, and to add new customers to the database.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Customers {
    // Initialise variables to store customers' information.
    /**
     * Integer value for customer id number
     */
    int customerID = 0;
    /**
     * String value for customer name
     */
    String customerName = "";
    /**
     * Integer value for customer phone number
     */
    int customerPhone;
    /**
     * String value for customer email address
     */
    String customerEmail = "";
    /**
     * Integer value for customer physical address
     */
    String customerAddress = "";

    // ----------------------------------- CONSTRUCTOR METHOD ------------------------------------------------------
    /**
     * Constructor method for customers instances
     *
     * @param customerID the customer's id number
     * @param customerName the customer's name
     * @param customerPhone the customer's phone number
     * @param customerEmail the customer's email address
     * @param customerAddress the customer's physical address
     */
    public Customers(int customerID, String customerName, int customerPhone, String customerEmail,
                     String customerAddress)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
    }

    // ----------------------------- METHOD: GET CUSTOMERS LIST -----------------------------------------------------
    /**
     * This method gets and prints all details for all customers in the database.
     * <p>
     * It is called in the {@link #chooseCustomer()} method.
     */
    public static void getCustomersList() {
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
            ResultSet results = statement.executeQuery("SELECT * FROM customers");

            System.out.println("\n========== LIST OF CUSTOMERS ==========\n\n"
                    + "[ ID    NAME             PHONE        EMAIL                  ADDRESS ]");

            // Print all customers' details.
            while (results.next()) {
                System.out.println(
                        "[ " + results.getInt("customer_id")
                                + "     " + results.getString("customer_name")
                                + "     " + results.getInt("customer_phone")
                                + "     " + results.getString("customer_email")
                                + "     " + results.getString("customer_address")
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

    // --------------------------------- METHOD: CHOOSE CUSTOMER ---------------------------------------------------
    /**
     * This method gets the user's selection of a customer in the database.
     * <p>
     * It shows all available customers by calling the {@link #getCustomersList()} method, gets user input
     * for the customer they want to select and returns the chosen customer's ID number.
     *
     * @return returns the chosen customer's ID number
     */
    public static int chooseCustomer(){
        int inputtedID = 0;
        String selectedCustomer;

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

            // Create Scanner object to get user input for choice of customer.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("\nEnter the ID of the Customer you want assigned to this project:");
            inputtedID = Integer.parseInt(inputScanner.nextLine());
            results = statement.executeQuery("SELECT * FROM customers WHERE customer_id = " + inputtedID + ";");

            // If the inputtedID matches a customerID in the table, print out the customer name and ID.
            // Else, set the inputtedID to 0.
            if (results.next()) {
                selectedCustomer = results.getString("customer_name");
                System.out.println("Customer " + selectedCustomer + " with the ID number " + inputtedID
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

    // ---------------------------------- METHOD: ADD CUSTOMER -----------------------------------------------------
    // This method
    /**
     * This method adds a new customer to the database.
     * <p>
     * It gets user input for new customer details, adds the new customer to the DB and prints updated
     * customers list by calling the {@link #getCustomersList()} method.
     */

    public static void addCustomer(){
        // Declare variables.
        String newCustomerName;
        int newCustomerPhone;
        String newCustomerEmail;
        String newCustomerAddress;
        int rowsAffected;
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

            // Create Scanner object to get user input for new customer details.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("\nEnter the new customer's Name:");
            newCustomerName = inputScanner.nextLine();
            System.out.println("\nEnter the new customer's Email Address:");
            newCustomerEmail = inputScanner.nextLine();
            System.out.println("\nEnter the new customer's Physical Address:");
            newCustomerAddress = inputScanner.nextLine();
            System.out.println("\nEnter the new customer's Phone Number:");
            newCustomerPhone = inputScanner.nextInt();

            // Insert new customer values into table.
            rowsAffected = statement.executeUpdate("INSERT INTO customers " +
                    "(customer_name, customer_phone, customer_email, customer_address) VALUES " +
                    "('" + newCustomerName
                    + "', " + newCustomerPhone
                    + ", '" + newCustomerEmail
                    + "', '" + newCustomerAddress + "')");

            //  Print updated list of customers.
            getCustomersList();
            System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected + " customers added. <---");

            // Close connections.
            statement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
