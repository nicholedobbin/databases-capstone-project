/*===================================================================================================================
    The "PrintProjects" class uses Java and MySQL to connect to the database 'PoisePMS', and contains the
    following methods to read and print data about projects and their associated people:

    ●   Get Current Project (Concise List):
            - gets and prints the current project's main details.
    ●   Get Current Project (Full List):
            - gets and prints all details of the current project.
    ●   Get All Projects (Concise List):
            - gets and prints main details of all projects.
    ●   Get All Projects (Full List):
            - gets and prints all details of all projects.
 ===================================================================================================================*/

import java.sql.*;

/**
 * This is the PrintProjects class which reads and prints data about projects and associated people in the database.
 * <p>
 *  The program forms part of the project management system for a small structural engineering firm called
 *  “Poised”, and uses Java and MySQL to connect to the database 'PoisePMS'.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class PrintProjects {
    // ------------------------ GET CURRENT PROJECTS (CONCISE LIST) METHOD  ----------------------------------------
    /**
     * This method gets the main details for the project being searched and prints a concise list of the project's
     * details.
     * <p>
     * This method is called in the {@link Projects#updateProject()} method.
     *
     * @param currentProjectNumber the project number of the project to be printed
     */
    public static void getCurrentProjectConcise(int currentProjectNumber) {
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

            // Get all project and related people's data for currentProjectNumber.
            ResultSet results = statement.executeQuery("SELECT \n"
                    + "projects.proj_number, \n"
                    + "projects.proj_name, \n"
                    + "projects.building_type, \n"
                    + "projects.total_proj_fee, \n"
                    + "projects.total_paid, \n"
                    + "projects.proj_deadline, \n"
                    + "projects.architect_id, \n"
                    + "architects.architect_id, \n"
                    + "architects.architect_name, \n"
                    + "projects.contractor_id, \n"
                    + "contractors.contractor_id, \n"
                    + "contractors.contractor_name, \n"
                    + "projects.proj_man_id, \n"
                    + "managers.proj_man_id, \n"
                    + "managers.proj_man_name, \n"
                    + "projects.customer_id, \n"
                    + "customers.customer_id, \n"
                    + "customers.customer_name, \n"
                    + "proj_finalized, \n"
                    + "date_completed \n"
                    + "FROM projects \n"
                    + "INNER JOIN architects ON projects.architect_id = architects.architect_id \n"
                    + "INNER JOIN contractors ON projects.contractor_id = contractors.contractor_id  \n"
                    + "INNER JOIN managers ON projects.proj_man_id = managers.proj_man_id \n"
                    + "INNER JOIN customers ON projects.customer_id = customers.customer_id \n"
                    + "WHERE proj_number = " + currentProjectNumber + ";");

            // Print main details for current project.
            System.out.println("\n========== CURRENT PROJECT (MAIN DETAILS) ==========");

            while (results.next()) {
                System.out.println("\nProject Number: " + results.getInt("proj_number")
                        + "\nProject Name: " + results.getString("proj_name")
                        + "\nBuilding Type: " + results.getString("building_type")
                        + "\nTotal Project Fee: R" + results.getDouble("total_proj_fee")
                        + "\nTotal Amount Paid: R" + results.getDouble("total_paid")
                        + "\nProject Deadline: " + results.getDate("proj_deadline")
                        + "\nArchitect Name: " + results.getString("architect_name")
                        + " (ID Number: " + results.getInt("architect_id") + ")"
                        + "\nContractor Name: " + results.getString("contractor_name")
                        + " (ID Number: " + results.getInt("contractor_id") + ")"
                        + "\nProject Manager Name: " + results.getString("proj_man_name")
                        + " (ID Number: " + results.getInt("proj_man_id") + ")"
                        + "\nCustomer Name: " + results.getString("customer_name")
                        + " (ID Number: " + results.getInt("customer_id") + ")"
                        + "\nProject Finalized: " + results.getString("proj_finalized")
                        + "\nProject Completion Date: " + results.getDate("date_completed"));
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // -------------------------- GET CURRENT PROJECT (FULL LIST) METHOD  ------------------------------------------
    /**
     * This method gets all details for the project being searched and prints a full list of the project's details.
     * <p>
     * This method is called in the {@link Projects#updateProject()} and
     * {@link Projects#getProjectByNameOrNumber()} methods.
     *
     * @param currentProjectNumber the project number of the project to be printed
     */
    public static void getCurrentProjectFull(int currentProjectNumber) {
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

            // Get all project and related people's data for currentProjectNumber.
            ResultSet results = statement.executeQuery("SELECT \n"
                    + "projects.proj_number, \n"
                    + "projects.proj_name, \n"
                    + "projects.building_type, \n"
                    + "projects.proj_address, \n"
                    + "projects.erf_number, \n"
                    + "projects.total_proj_fee, \n"
                    + "projects.total_paid, \n"
                    + "projects.proj_deadline, \n"
                    + "projects.architect_id, \n"
                    + "architects.architect_id, \n"
                    + "architects.architect_name, \n"
                    + "architects.architect_phone, \n"
                    + "architects.architect_email, \n"
                    + "architects.architect_address, \n"
                    + "projects.contractor_id, \n"
                    + "contractors.contractor_id, \n"
                    + "contractors.contractor_name, \n"
                    + "contractors.contractor_phone, \n"
                    + "contractors.contractor_email, \n"
                    + "contractors.contractor_address, \n"
                    + "projects.proj_man_id, \n"
                    + "managers.proj_man_id, \n"
                    + "managers.proj_man_name, \n"
                    + "managers.proj_man_phone, \n"
                    + "managers.proj_man_email, \n"
                    + "managers.proj_man_address,\n"
                    + "projects.customer_id, \n"
                    + "customers.customer_id, \n"
                    + "customers.customer_name, \n"
                    + "customers.customer_phone, \n"
                    + "customers.customer_email, \n"
                    + "customers.customer_address, \n"
                    + "proj_finalized, \n"
                    + "date_completed \n"
                    + "FROM projects \n"
                    + "INNER JOIN architects ON projects.architect_id = architects.architect_id \n"
                    + "INNER JOIN contractors ON projects.contractor_id = contractors.contractor_id  \n"
                    + "INNER JOIN managers ON projects.proj_man_id = managers.proj_man_id \n"
                    + "INNER JOIN customers ON projects.customer_id = customers.customer_id \n"
                    + "WHERE proj_number = " + currentProjectNumber + ";");

            // Print all values for each project in the 'projects' table.
            System.out.println("\n========== CURRENT PROJECT (ALL DETAILS) ==========");

            while (results.next()) {
                System.out.println("\nProject Number: " + results.getInt("proj_number")
                        + "\nProject Name: " + results.getString("proj_name")
                        + "\nBuilding Type: " + results.getString("building_type")
                        + "\nProject Address: " + results.getString("proj_address")
                        + "\nProject ERF Number: " + results.getInt("erf_number")
                        + "\nTotal Project Fee: R" + results.getDouble("total_proj_fee")
                        + "\nTotal Amount Paid: R" + results.getDouble("total_paid")
                        + "\nProject Deadline: " + results.getDate("proj_deadline")
                        + "\nArchitect Name: " + results.getString("architect_name")
                        + " (ID Number: " + results.getInt("architect_id") + ")"
                        + "\nArchitect Phone: " + results.getInt("architect_phone")
                        + "\nArchitect Email: " + results.getString("architect_email")
                        + "\nArchitect Address: " + results.getString("architect_address")
                        + "\nContractor Name: " + results.getString("contractor_name")
                        + " (ID Number: " + results.getInt("contractor_id") + ")"
                        + "\nContractor Phone: " + results.getInt("contractor_phone")
                        + "\nContractor Email: " + results.getString("contractor_email")
                        + "\nContractor Address: " + results.getString("contractor_address")
                        + "\nProject Manager Name: " + results.getString("proj_man_name")
                        + " (ID Number: " + results.getInt("proj_man_id") + ")"
                        + "\nProject Manager Phone: " + results.getInt("proj_man_phone")
                        + "\nProject Manager Email: " + results.getString("proj_man_email")
                        + "\nProject Manager Address: " + results.getString("proj_man_address")
                        + "\nCustomer Name: " + results.getString("customer_name")
                        + " (ID Number: " + results.getInt("customer_id") + ")"
                        + "\nCustomer Phone: " + results.getInt("customer_phone")
                        + "\nCustomer Email: " + results.getString("customer_email")
                        + "\nCustomer Address: " + results.getString("customer_address")
                        + "\nProject Finalized: " + results.getString("proj_finalized")
                        + "\nProject Completion Date: " + results.getDate("date_completed"));
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // -------------------------- GET ALL PROJECTS (CONCISE LIST) METHOD  ------------------------------------------
    /**
     * This method gets the main details for all projects in the database and prints a concise list of
     * all projects' details.
     * <p>
     * This method is called in the methods listed in the See Also section.
     *
     * @see Projects#assignProjectName()
     * @see Projects#updateProject()
     * @see Projects#deleteProject()
     */
    public static void getAllProjectsConcise() {
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
            ResultSet results = statement.executeQuery("SELECT * FROM projects");

            // Print the main information for each project in the 'projects' table.
            System.out.println("\n========== LIST OF ALL PROJECTS (MAIN DETAILS) ==========");

            while (results.next()) {
                System.out.println("\nProject Number: " + results.getInt("proj_number")
                        + "\nProject Name: " + results.getString("proj_name")
                        + "\nBuilding Type: " + results.getString("building_type")
                        + "\nProject Deadline: " + results.getDate("proj_deadline")
                        + "\nProject Finalized: " + results.getString("proj_finalized")
                        + "\nProject Completion Date: " + results.getDate("date_completed")
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

    // -------------------------- METHOD: GET ALL PROJECTS (FULL LIST) ---------------------------------------------
    /**
     * This method gets all details for all projects in the database and prints a full list of all
     * projects' details.
     * <p>
     * This method is called in the {@link Projects#addNewProject()} and {@link Projects#finalizeProject()}
     * methods.
     */
    public static void getAllProjectsFull() {
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
            ResultSet results = statement.executeQuery("SELECT * FROM projects");

            // Print all values for each project in the 'projects' table.
            System.out.println("\n========== LIST OF ALL PROJECTS IN PROJECTS TABLE ==========");

            while (results.next()) {
                System.out.println("\nProject Number: " + results.getInt("proj_number")
                        + "\nProject Name: " + results.getString("proj_name")
                        + "\nBuilding Type: " + results.getString("building_type")
                        + "\nProject Address: " + results.getString("proj_address")
                        + "\nProject ERF Number: " + results.getInt("erf_number")
                        + "\nTotal Project Fee: R" + results.getDouble("total_proj_fee")
                        + "\nTotal Amount Paid: R" + results.getDouble("total_paid")
                        + "\nProject Deadline: " + results.getDate("proj_deadline")
                        + "\nArchitect ID Number: " + results.getInt("architect_id")
                        + "\nContractor ID Number: " + results.getInt("contractor_id")
                        + "\nProject Manager ID Number: " + results.getInt("proj_man_id")
                        + "\nCustomer ID Number: " + results.getInt("customer_id")
                        + "\nProject Finalized: " + results.getString("proj_finalized")
                        + "\nProject Completion Date: " + results.getDate("date_completed")
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
}


