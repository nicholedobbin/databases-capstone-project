/*===================================================================================================================
    The 'Projects' class contains the following methods:
    ●   Constructor for project instances.
    ●   Assign Project Name:
            - assigns project names as [(Building Type) (Customer Surname)] where the name is unknown.
    ●   Add New Project:
            - gets project information input from user and adds a new project to the database.
    ●   Update Existing Project:
            - allows user to update fields of an existing project.
    ●   Delete Existing Project (*** SEE NOTE (1) BELOW ***):
            - allows user to delete data about projects and people associated with them.
    ●   Finalize Project:
            - allows user to finalize a project by marking the proj_finalized field as 'finalized'.
            - When a project is finalized, the completion date is added as the current date.
    ●   Find All Incomplete Projects
            - allows user to find all projects with the value NULL in the date_completed field.
    ●   Find All Overdue Projects
            - allows user to find all projects where the deadline has already passed.
    ●   Find Project By Project Name Or Project Number:
            - allows user to find a project by the project number, which can only return up to one result
              because the project number is the primary key.
            - allows user to find a project by the project name (not a primary key), which can return multiple
              results because the project name is not unique and not a primary key.
 ===================================================================================================================*/

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the projects class which reads, writes, updates and deletes data about projects and people in the database.
 * <p>
 *  The program forms part of the project management system for a small structural engineering firm called
 *  “Poised”, and uses Java and MySQL to connect to the database 'PoisePMS'.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Projects {
    /**
     * String value for project name
     */
    String projectName;
    /**
     * String value for building type
     */
    String buildingType;
    /**
     * String value for project address
     */
    String projectAddress;
    /**
     * Integer value for erf number
     */
    int erfNumber;
    /**
     * Double value for total project fee
     */
    Double totalProjectFee;
    /**
     * Double value for total paid by customer
     */
    Double totalPaid;
    /**
     * Date value for project deadline
     */
    Date projectDeadline;
    /**
     * Integer value for architect id
     */
    int architectID;
    /**
     * Integer value for contractor id
     */
    int contractorID;
    /**
     * Integer value for project manger id
     */
    int managerID;
    /**
     * Integer value for customer id
     */
    int customerID;
    /**
     * String value for project finalization
     */
    String projectFinalized;

    // ------------------------------------ CONSTRUCTOR METHOD -----------------------------------------------------

    /**
     * Constructor method for projects instances
     *
     * @param projectName the project's name
     * @param buildingType the project's building type
     * @param projectAddress the project's address
     * @param erfNumber the project's erf number
     * @param totalProjectFee the project's total fee
     * @param totalPaid the total amount paid by the project's customer
     * @param projectDeadline the date by which the project must be completed
     * @param architectID the id of the architect assigned to the project
     * @param contractorID the id of the contractor assigned to the project
     * @param managerID the id of the project manager assigned to the project
     * @param customerID the id of the customer assigned to the project
     * @param projectFinalized the string identifying project finalization
     */
    public Projects(String projectName, String buildingType, String projectAddress, int erfNumber,
                    Double totalProjectFee, Double totalPaid, Date projectDeadline, int architectID,
                    int contractorID, int managerID, int customerID, String projectFinalized)
    {
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.projectAddress = projectAddress;
        this.erfNumber = erfNumber;
        this.totalProjectFee = totalProjectFee;
        this.totalPaid = totalPaid;
        this.projectDeadline = projectDeadline;
        this.architectID = architectID;
        this.contractorID = contractorID;
        this.managerID = managerID;
        this.customerID = customerID;
        this.projectFinalized = projectFinalized;
    }

    //------------------------------- METHOD: ASSIGN PROJECT NAME --------------------------------------------------

    /**
     * This method assigns a project name as [(Building Type) (Customer Surname)] when the name is unknown
     * <p>
     * This method finds projects in the database without a project name value and assigns the project name as that
     * project's building type and customer surname, e.g., 'House Tyson', and it is called in the
     * {@link #addNewProject()} method to ensure all inputted projects have a name.
     */
    public static void assignProjectName() {
        int projectNumber = 0;
        String buildingType;
        String[] customerNameArray;
        int surnameIndex;
        String customerSurname;
        String updatedProjectName = null;

        try{
            // Add your url, user and password for your database in the empty strings below.
            String connectionURL = "";
            String connectionUser = "";
            String connectionPassword = "";

            // Connect to 'PoisePMS' database, set up statements and queries using 'inner join' to get
            // customer_name from the customer_id foreign key.
            Connection connection = DriverManager.getConnection(
                    connectionURL,
                    connectionUser,
                    connectionPassword);

            Statement statement = connection.createStatement();
            ResultSet results;
            int rowsAffected;

            results = statement.executeQuery
                    ("SELECT projects.proj_number, projects.proj_name, projects.building_type, "
                            + "projects.customer_id, customers.customer_name "
                            + "FROM projects INNER JOIN customers ON projects.customer_id = customers.customer_id "
                            + "WHERE proj_name = '';");

            // Loop through results and store DB values in variables.
            while (results.next()) {
                projectNumber = results.getInt("proj_number");
                buildingType = results.getString("building_type");

                // Get customerSurname by storing customer_name in string array, then get last index in
                // the array's value (i.e. the surname) and store it in 'customerSurname'.
                customerNameArray = results.getString("customer_name").split(" ");
                surnameIndex = customerNameArray.length-1;
                customerSurname = customerNameArray[surnameIndex];

                // Set updatedProjectName to value of buildingType and customerSurname (e.g. 'House Tyson').
                updatedProjectName = buildingType + " " + customerSurname;
            }
            
            // Execute update to assign the project name and print updated list of all projects (main details only).
            rowsAffected = statement.executeUpdate("UPDATE projects SET proj_name = '"
                    + updatedProjectName +"' WHERE proj_number = " + projectNumber);

            PrintProjects.getAllProjectsConcise();
            System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected + " project names assigned. <---");

            // Close connections.
            results.close();
            statement.close();
            connection.close();
            
        }  catch(SQLException e){
            e.printStackTrace();
        }
    }

    // --------------------------------- METHOD: ADD NEW PROJECT ---------------------------------------------------
    /**
     * This method retrieves all the information required to create and add a new project to the database.
     * <p>
     * This method retrieves user input for project details and calls the methods listed in the See Also section.
     * It creates a new project instance with retrieved input and adds the new project to the database,
     * and calls the {@link #assignProjectName()} method to assign a project name and
     * the {@link PrintProjects#getAllProjectsFull()} method to print the updated projects list.
     *
     * @see Architects#chooseArchitect()
     * @see Contractors#chooseContractor()
     * @see Managers#chooseManager()
     * @see Customers#chooseCustomer()
     * @see Customers#addCustomer()
     */
     public static void addNewProject() {
        // Declare and initialize variables.
        int rowsAffected;
        String userInput;
        String inputtedName;
        String inputtedBuildingType;
        String inputtedAddress;
        int inputtedErfNumber;
        Double inputtedTotalFee;
        Double inputtedTotalPaid;
        Date inputtedDeadline;
        int inputtedArchitectID;
        int inputtedContractorID;
        int inputtedManagerID;
        int inputtedCustomerID = 0;
        String inputtedFinalized = "Not Finalized";

        try {
            // Add your url, user and password for your database in the empty strings below.
            String connectionURL = "";
            String connectionUser = "";
            String connectionPassword = "";

            // Connect to 'PoisePMS' database and set up statement.
            Connection connection = DriverManager.getConnection(
                    connectionURL,
                    connectionUser,
                    connectionPassword);

            Statement statement = connection.createStatement();

            // Create Scanner object to get user input for new project details.
            Scanner inputScanner = new Scanner(System.in);

            // Check if new project's name is known and get input, or set inputtedName to null if it's unknown.
            System.out.println("You chose: Option 1 - Add a new project."
                    + "\nDo you know the Project Name? Enter Y/N: ");
            userInput = inputScanner.next();

            if (userInput.equalsIgnoreCase("Y")) {
                System.out.println("Enter the Project Name: ");
                inputtedName = inputScanner.nextLine();
            } else {
                inputtedName = null;
            }
            inputtedName = inputScanner.nextLine();

            // Get additional project details.
            System.out.println("Enter the Building Type: ");
            inputtedBuildingType = inputScanner.nextLine();
            System.out.println("Enter the Project's Address: ");
            inputtedAddress = inputScanner.nextLine();
            System.out.println("Enter the Project's ERF Number: ");
            inputtedErfNumber = inputScanner.nextInt();
            System.out.println("Enter the Project's Total Fee: ");
            inputtedTotalFee = inputScanner.nextDouble();
            System.out.println("Enter the Project's Deadline (YYYY-MM-DD): ");
            inputtedDeadline = Date.valueOf(inputScanner.next());

            // Check if an amount has already been paid and get name input, or set inputtedTotalPaid to 00.00 if not.
            System.out.println("Has the customer already paid an amount? Enter Y/N: ");
            userInput = inputScanner.next();
            if (userInput.equalsIgnoreCase("Y")) {
                System.out.println("Enter the total amount the customer has already paid: ");
                inputtedTotalPaid = inputScanner.nextDouble();
            } else {
                inputtedTotalPaid = 00.00;
            }

            // Call chooseArchitect() method to get user choice of architect, with exception for invalid ID entry.
            inputtedArchitectID = Architects.chooseArchitect();
            if (inputtedArchitectID == 0) {
                System.out.println("There are no architects with that ID in our list. Please enter a valid ID.");
                inputtedArchitectID = Architects.chooseArchitect();
            }
            
            // Call chooseContractor() method to get user choice of contractor, with exception for invalid ID entry.
            inputtedContractorID = Contractors.chooseContractor();
            if (inputtedContractorID == 0) {
                System.out.println("There are no contractors with that ID in our list. Please enter a valid ID.");
                inputtedContractorID = Contractors.chooseContractor();
            }
            
            // Call chooseManager() method to get user choice of manager, with exception for invalid ID entry.
            inputtedManagerID = Managers.chooseManager();
            if (inputtedManagerID == 0) {
                System.out.println("There are no project managers with that ID in our list. Please enter a valid ID.");
                inputtedManagerID = Managers.chooseManager();
            }
            
            // Print 'customers' table and get user choice to add new customer or assign existing customer.
            Customers.getCustomersList();
            System.out.println("\nDo you want to assign a new customer to this project? "
                    + "(i.e. a customer not on this list) \nEnter Y/N:");
            userInput = inputScanner.next();
            
            // If user wants to add a new customer, call addCustomer() method, then initialise inputtedCustomerID
            // by calling chooseCustomer() method.
            if (userInput.equalsIgnoreCase("Y")) {
                Customers.addCustomer();
                inputtedCustomerID = Customers.chooseCustomer();
            }
            // Else, initialise inputtedCustomerID by calling chooseCustomer() method, with exception for
            // invalid ID entry.
            else {
                inputtedCustomerID = Customers.chooseCustomer();
                if (inputtedCustomerID == 0) {
                    System.out.println("There are no customers with that ID in our list. Please enter a valid ID.");
                    inputtedCustomerID = Customers.chooseCustomer();
                }
            }

            // Create new project instance.
            Projects newProject = new Projects(inputtedName, inputtedBuildingType, inputtedAddress,
                    inputtedErfNumber, inputtedTotalFee, inputtedTotalPaid, inputtedDeadline, inputtedArchitectID,
                    inputtedContractorID, inputtedManagerID, inputtedCustomerID, inputtedFinalized);

            // Add newProject details to 'projects' table.
            rowsAffected = statement.executeUpdate("INSERT INTO projects "
                    + "(proj_name, "
                    + "building_type, "
                    + "proj_address, "
                    + "erf_number, "
                    + "total_proj_fee, "
                    + "total_paid, "
                    + "proj_deadline, "
                    + "architect_id, "
                    + "contractor_id, "
                    + "proj_man_id, "
                    + "customer_id, "
                    + "proj_finalized) "
                    + "VALUES "
                    + "('" + newProject.projectName + "', "
                    + "'" + newProject.buildingType + "', "
                    + "'" + newProject.projectAddress + "', "
                    + newProject.erfNumber + ", "
                    + newProject.totalProjectFee + ", "
                    + newProject.totalPaid + ", "
                    + "'" + newProject.projectDeadline + "', "
                    + newProject.architectID + ", "
                    + newProject.contractorID + ", "
                    + newProject.managerID + ", "
                    + newProject.customerID + ", "
                    + "'" + newProject.projectFinalized + "') "
                );
            
            // Call assignProjectName() to assign a project name if the name is unknown.
            assignProjectName();
            
            // Print updated list of all projects with new project added.
            PrintProjects.getAllProjectsFull();
            System.out.println("\n---> UPDATE COMPLETE: "+ rowsAffected + " projects added. <---");

            // Close connections.
            statement.close();
            connection.close();
            
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // ---------------------------- METHOD: UPDATE EXISTING PROJECT ------------------------------------------------
    /**
     * This method allows the user to update fields in any of the existing projects.
     * <p>
     * Update options do not include updating the project number (because it is the primary key and needs to
     * be unique) or updating the completion date (because that is updated when the project is finalized).
     * The method prints a concise list of all projects in the 'projects' table, gets the user selection of the
     * project and field they want to update, and updates the selected fields in the database. It uses the
     * methods listed in the See Also section below to print project lists and update the architect, contractor,
     * project manager and customer fields and recursively calls itself if invalid actions are entered.
     *
     * @see PrintProjects#getAllProjectsConcise()
     * @see PrintProjects#getCurrentProjectConcise(int inputtedProjectNumber)
     * @see PrintProjects#getCurrentProjectFull(int inputtedProjectNumber)
     * @see Architects#chooseArchitect()
     * @see Contractors#chooseContractor()
     * @see Managers#chooseManager()
     * @see Customers#chooseCustomer()
     * @see Customers#addCustomer()
     */
    public static void updateProject() {
        int inputtedProjectNumber;
        int userSelection;
        String userInput;

        try {
            // Create Scanner object to get user input.
            Scanner inputScanner = new Scanner(System.in);

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
            ResultSet results = statement.executeQuery("SELECT * FROM projects;");
            int rowsAffected;
            int rowsFound = 0;

            // Print all existing projects' main information.
            System.out.println("\n---> USER SELECTION: Option 2 - Update existing project. <---");
            PrintProjects.getAllProjectsConcise();

            // Get user selection of project to update.
            System.out.println("\n---> Enter the project number of the project you want to update: ");
            inputtedProjectNumber = Integer.parseInt(inputScanner.nextLine());
            System.out.println("\n---> You are updating Project Number: " + inputtedProjectNumber + " <---\n");

            // Get user selection of field to update.
            System.out.println("Which field do you want to update? "
                    + "\n  1 - Project Name"
                    + "\n  2 - Building Type"
                    + "\n  3 - Project Address"
                    + "\n  4 - Project ERF Number"
                    + "\n  5 - Total Project Fee"
                    + "\n  6 - Total Paid By Customer"
                    + "\n  7 - Project Deadline"
                    + "\n  8 - Architect Assigned To This Project"
                    + "\n  9 - Contractor Assigned To This Project"
                    + "\n  10 - Project Manager Assigned To This Project"
                    + "\n  11 - Customer Assigned To This Project"
                    + "\n\n---> Enter the number of the field you want to update:");

            userSelection = Integer.parseInt(inputScanner.nextLine());

            // Use switch statement if userSelection matches field/action numbers.
            if (userSelection == 1 || userSelection == 2 || userSelection == 3 || userSelection == 4
                    || userSelection == 5 || userSelection == 6 || userSelection == 7 ||userSelection == 8
                    || userSelection == 9 || userSelection == 10 || userSelection == 11 || userSelection == 12) {

                switch(userSelection) {

                    // 1 - Update Project Name.
                    case 1:
                        // Get user input for updated project name and update in DB.
                        System.out.println("\nYou chose: Option 1 - Update Project Name."
                                + "\n---> Enter the new project name: ");

                        String updatedProjectName = inputScanner.nextLine();

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET proj_name = '" + updatedProjectName +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 2 - Update Building Type.
                    case 2:
                        // Get user input for updated building type and update in DB.
                        System.out.println("\nYou chose: Option 2 - Update Building Type."
                                + "\n---> Enter the new building type: ");

                        String updatedBuildingType = inputScanner.nextLine();

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET building_type = '" + updatedBuildingType +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 3- Update Project Address.
                    case 3:
                        // Get user input for updated address and update in DB.
                        System.out.println("\nYou chose: Option 3 - Update Project Address."
                                + "\n---> Enter the project's new address: ");
                        String updatedAddress = inputScanner.nextLine();

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET proj_address = '" + updatedAddress +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print all details of updated project.
                        PrintProjects.getCurrentProjectFull(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 4- Update Project ERF Number.
                    case 4:
                        // Get user input for updated ERF number and update in DB.
                        System.out.println("\nYou chose: Option 4 - Update Project ERF Number."
                                + "\n---> Enter the project's new ERF number: ");
                        int updatedErfNumber = Integer.parseInt(inputScanner.nextLine());

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET erf_number = '" + updatedErfNumber +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print all details of updated project.
                        PrintProjects.getCurrentProjectFull(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 5 - Update Total Project Fee.
                    case 5:
                        // Get user input for updated total fee and update in DB.
                        System.out.println("\nYou chose: Option 5 - Update Total Project Fee."
                                + "\n---> Enter the project's new total fee: ");
                        Double updatedTotalFee = Double.valueOf(inputScanner.nextLine());

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET total_proj_fee = '" + updatedTotalFee +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 6 - Update Total Paid.
                    case 6:
                        // Get user input for updated total paid and update in DB.
                        System.out.println("\nYou chose: Option 6 - Update Total Paid By Customer."
                                + "\n---> Enter the new total amount paid by the customer: ");
                        Double updatedTotalPaid = Double.valueOf(inputScanner.nextLine());

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET total_paid = '" + updatedTotalPaid +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 7 - Update Project Deadline.
                    case 7:
                        // Get user input for updated deadline and update in DB.
                        System.out.println("\nYou chose: Option 7 - Update Project Deadline."
                                + "\n---> Enter the new project deadline (YYYY-MM-DD): ");
                        Date updatedDeadline = Date.valueOf(inputScanner.nextLine());

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET proj_deadline = '" + updatedDeadline +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    // 8 - Update Architect.
                    case 8:
                        // Get user input for updated choice of architect and update in DB.
                        System.out.println("\nYou chose: Option 8 - Update Architect Assigned To This Project.");
                        int updatedArchitectID = Architects.chooseArchitect();
                        
                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET architect_id = '" + updatedArchitectID +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n----> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated -----");
                        break;

                    // 9 - Update Contractor.
                    case 9:
                        // Get user input for updated choice of contractor and update in DB.
                        System.out.println("\nYou chose: Option 9 - Update Contractor Assigned To This Project.");
                        int updatedContractorID = Contractors.chooseContractor();

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET contractor_id = '" + updatedContractorID +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n----> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated -----");
                        break;
                    
                    // 10 - Update Project Manager.
                    case 10:
                        // Get user input for updated choice of manager and update in DB.
                        System.out.println
                                ("\nYou chose: Option 10 - Update Project Manager Assigned To This Project.");
                        int updatedManagerID = Managers.chooseManager();

                        rowsAffected = statement.executeUpdate
                                ("UPDATE projects SET proj_man_id = '" + updatedManagerID +
                                        "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n----> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated -----");
                        break;
                    
                    // 11 - Update Customer.
                    case 11:
                        // Print customers list and get user choice to add new or assign existing customer.
                        System.out.println("\nYou chose: Option 11 - Update Customer Assigned To This Project.");
                        int updatedCustomerID = 0;
                        Customers.getCustomersList();
                        System.out.println("\nDo you want to assign a new customer to this project? "
                                + "(i.e. a customer not on this list) \nEnter Y/N:");
                        userInput = inputScanner.next();

                        // If user wants to add new customer, call addCustomer() and chooseCustomer() methods.
                        if (userInput.equalsIgnoreCase("Y")) {
                            Customers.addCustomer();
                            updatedCustomerID = Customers.chooseCustomer();
                        }
                        // Else, if user wants to select a customer from the list, call chooseCustomer() method,
                        // with exceptions for invalid customer id entry.
                        else if (userInput.equalsIgnoreCase("N")) {
                            updatedCustomerID = Customers.chooseCustomer();
                            if (updatedCustomerID == 0) {
                                System.out.println("There are no customers with that ID in our list. "
                                        + "Please enter a valid ID.");
                                updatedCustomerID = Customers.chooseCustomer();
                            }
                        }
                        // Else, print invalid entry message and call updateProject() method.
                        else {
                            System.out.println("Invalid entry! "
                                    + "Please enter the number of the field you want to update.");
                            updateProject();
                        }

                        // Update customer in DB.
                        rowsAffected = statement.executeUpdate("UPDATE projects SET customer_id = '"
                                + updatedCustomerID + "' WHERE proj_number = " + inputtedProjectNumber + ";");

                        // Print main details of updated project.
                        PrintProjects.getCurrentProjectConcise(inputtedProjectNumber);
                        System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected
                                + " project (Project Number: " + inputtedProjectNumber + ") updated <---");
                        break;

                    default:
                        // Default: print invalid entry message and call updateProject() method.
                        System.out.println("Invalid Entry! Please enter the number of the field you want to update.");
                        updateProject();
                        break;
                }
            } else {
                // Print invalid entry message and call updateProject() method.
                System.out.println("Invalid Entry! Please enter the number of the field you want to update.");
                updateProject();
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------- METHOD: DELETE EXISTING PROJECT ------------------------------------------------
    // *** SEE NOTE FOR REVIEWER (1)  ***
    /**
     * This method deletes a project and its associated people if all people assigned to the project
     * are not assigned to any other projects. If it's people are assigned elsewhere, only the project's
     * data is deleted.
     * <p>
     * If an architect, contractor, customer or project manager assigned to the project is also assigned to
     * other projects, only the project's data will be deleted from the 'projects' table and its assigned people's
     * data will not be deleted from the other tables or projects in the database. The method prints the updated
     * projects lists by calling the {@link PrintProjects#getAllProjectsConcise()} method.
     */
    public static void deleteProject() {
        // Initialize variables to store query statements.
        String architectQuery = "SELECT proj_number FROM projects\n" +
                "WHERE architect_id IN \n" +
                "(SELECT architects.architect_id\n" +
                "FROM (projects\n" +
                "INNER JOIN architects ON projects.architect_id = architects.architect_id)\n" +
                "GROUP BY architect_id\n" +
                "HAVING COUNT(projects.proj_number) > 1);";

        String contractorQuery = "SELECT proj_number FROM projects\n" +
                "WHERE contractor_id IN \n" +
                "(SELECT contractors.contractor_id\n" +
                "FROM (projects\n" +
                "INNER JOIN contractors ON projects.contractor_id = contractors.contractor_id)\n" +
                "GROUP BY contractor_id\n" +
                "HAVING COUNT(projects.proj_number) > 1);";

        String managerQuery = "SELECT proj_number FROM projects\n" +
                "WHERE proj_man_id IN \n" +
                "(SELECT managers.proj_man_id\n" +
                "FROM (projects\n" +
                "INNER JOIN managers ON projects.proj_man_id = managers.proj_man_id)\n" +
                "GROUP BY proj_man_id\n" +
                "HAVING COUNT(projects.proj_number) > 1);";

        String customerQuery = "SELECT proj_number FROM projects\n" +
                "WHERE customer_id IN \n" +
                "(SELECT customers.customer_id\n" +
                "FROM (projects\n" +
                "INNER JOIN customers ON projects.customer_id = customers.customer_id)\n" +
                "GROUP BY customer_id\n" +
                "HAVING COUNT(projects.proj_number) > 1);";

        // Initialise variables to store user input and ID numbers of people associated to inputted project number.
        String userInput;
        int inputtedProjectNumber = 0;
        int architectID = 0;
        int contractorID = 0;
        int managerID = 0;
        int customerID = 0;

        // Create Boolean ArrayList to store true values if an inputted project number's assigned people
        // are associated with other projects.
        ArrayList<Boolean> associatedToOtherProjectsList = new ArrayList<Boolean>();

        try {
            // Create Scanner object to get user input.
            Scanner inputScanner = new Scanner(System.in);

            // Add your url, user and password for your database in the empty strings below.
            String connectionURL = "";
            String connectionUser = "";
            String connectionPassword = "";

            // Connect to 'PoisePMS' database and set up statements & queries.
            Connection connection = DriverManager.getConnection(
                    connectionURL,
                    connectionUser,
                    connectionPassword);

            Statement statement = connection.createStatement();
            ResultSet results;
            int rowsAffected = 0;

            // Show all projects.
            System.out.println("\nYou chose: Option 3 - Delete an existing project.");
            PrintProjects.getAllProjectsConcise();

            // Get user selection for proj_number of the project they want to delete.
            System.out.println("\n---> Enter the Project Number of the project you want to delete: ");
            inputtedProjectNumber = Integer.parseInt(inputScanner.nextLine());

            // Get ID numbers for people associated with this project and store in variables declared above.
            results = statement.executeQuery("SELECT \n"
                    + "proj_number, \n"
                    + "proj_name, \n"
                    + "architect_id, \n"
                    + "contractor_id, \n"
                    + "proj_man_id, \n"
                    + "customer_id \n"
                    + "FROM projects \n"
                    +  "WHERE proj_number = "+ inputtedProjectNumber +";");

            while (results.next()) {
                architectID = results.getInt("projects.architect_id");
                contractorID = results.getInt("projects.contractor_id");
                managerID = results.getInt("projects.proj_man_id");
                customerID = results.getInt("projects.customer_id");
            }

            // Get count of projects assigned to each architect.
            results = statement.executeQuery(architectQuery);

            // If this project's architect is assigned to other projects, add 'true' statement to arraylist.
            while (results.next()) {
                if (inputtedProjectNumber == results.getInt("proj_number")) {
                    associatedToOtherProjectsList.add(true);
                }
            }

            // Get count of projects assigned to each contractor.
            results = statement.executeQuery(contractorQuery);

            // If this project's contractor is assigned to other projects, add 'true' statement to arraylist.
            while (results.next()) {
                if (inputtedProjectNumber == results.getInt("proj_number")) {
                    associatedToOtherProjectsList.add(true);
                }
            }

            // Get count of projects assigned to each manager.
            results = statement.executeQuery(managerQuery);

            // If this project's manager is assigned to other projects, add 'true' statement to arraylist.
            while (results.next()) {
                if (inputtedProjectNumber == results.getInt("proj_number")) {
                    associatedToOtherProjectsList.add(true);
                }
            }

            // Get count of projects assigned to each customer.
            results = statement.executeQuery(customerQuery);

            // If this project's customer is assigned to other projects, add 'true' statement to arraylist.
            while (results.next()) {
                if (inputtedProjectNumber == results.getInt("proj_number")) {
                    associatedToOtherProjectsList.add(true);
                }
            }

            // If at least one of the values in the associatedToOtherProjectsList is true (i.e. if one or more
            // people assigned to this project are also assigned to other projects), print warning that associated
            // people's data and other tables will not be deleted, then get user to choose if they still want
            // to delete this project.
            if (associatedToOtherProjectsList.contains(true)){
                System.out.println("\n---> WARNING! <---"
                        + "\nThis project's people are assigned to other projects."
                        + "\nOnly this project's data will be deleted."
                        + "\nAll associated people and their other projects' data will not be affected."
                        + "\n\nAre you sure you want to delete this project? Enter Y/N: " );
                userInput = inputScanner.nextLine();

                // If user enters Y, delete the project from 'projects' table only, and print updated
                // list of projects (main details).
                if (userInput.equalsIgnoreCase("y")) {
                    rowsAffected = statement.executeUpdate("DELETE FROM projects WHERE proj_number = "
                            + inputtedProjectNumber + ";");
                    PrintProjects.getAllProjectsConcise();
                    System.out.println("\n---> UPDATE COMPLETE: " + rowsAffected + " projects deleted. <---");
                }

                // Else if user enters N, print updated list of projects (main details) and 'update aborted' message.
                else if (userInput.equalsIgnoreCase("n")) {
                    PrintProjects.getAllProjectsConcise();
                    System.out.println("\n---> UPDATE ABORTED: "+ rowsAffected + " projects deleted. <---");
                }

            // If none of the values in the associatedToOtherProjectsList are true (i.e. none of the people
            // assigned to this project are also assigned to other projects), print warning that assigned
            // people's data in all tables will be deleted, then get user to choose if they still want
            // to delete this project.
            } else {
                System.out.println("\n---> WARNING! <---"
                        + "\nThis project's people are not assigned to other projects."
                        + "\nAll associated people's data will be deleted when you delete this project."
                        + "\n\nAre you sure you want to delete this project and all associated people's data? "
                        + "Enter Y/N: " );
                userInput = inputScanner.nextLine();

                // If user enters Y, delete that project and all its assigned people's data in other tables.
                if (userInput.equalsIgnoreCase("Y")) {
                    rowsAffected = statement.executeUpdate
                            ("DELETE FROM projects WHERE proj_number = " + inputtedProjectNumber + ";");
                    int architectRowsAffected = statement.executeUpdate
                            ("DELETE FROM architects WHERE architect_id = " + architectID + ";");
                    int contractorRowsAffected = statement.executeUpdate
                            ("DELETE FROM contractors WHERE contractor_id = " + contractorID + ";");
                    int managerRowsAffected = statement.executeUpdate
                            ("DELETE FROM managers WHERE proj_man_id = " + managerID + ";");
                    int customerRowsAffected = statement.executeUpdate
                            ("DELETE FROM customers WHERE customer_id = " + customerID + ";");

                    // Print updated list of projects (main details).
                    PrintProjects.getAllProjectsConcise();
                    System.out.println("\n---> UPDATE COMPLETE: \n"
                            + rowsAffected + " projects deleted.\n"
                            + architectRowsAffected + " architects deleted.\n"
                            + contractorRowsAffected + " contractors deleted.\n"
                            + managerRowsAffected + " project managers deleted.\n"
                            + customerRowsAffected + " customers deleted. <---");
                }

                // Else if user enters N, print updated list of projects (main details) and 'update aborted' message.
                else if (userInput.equalsIgnoreCase("N")) {
                    PrintProjects.getAllProjectsConcise();
                    System.out.println("\n---> UPDATE ABORTED: "+ rowsAffected + " projects deleted. <---");
                }
            }

            // Close connections.
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------- METHOD: FINALIZE EXISTING PROJECT ---------------------------------------------
    /**
     * This method finalizes a project.
     * <p>
     * This method finds and prints all projects with the value 'not finalized' in the proj_finalized field,
     * and gets user selection for the project number they want finalized. It then updates the database's
     * proj_finalized field to 'finalized' and sets the date_completed filed to the current date, and prints
     * the updated list of all projects by calling the {@link PrintProjects#getAllProjectsFull()} method.
     */

    public static void finalizeProject() {
        int inputtedProjectNumber;

        try {
            // Create Scanner object to get user input.
            Scanner inputScanner = new Scanner(System.in);

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
            int rowsAffected;
            results = statement.executeQuery
                    ("SELECT proj_number, proj_name, building_type, proj_finalized, date_completed "
                            + "FROM projects WHERE proj_finalized = 'Not Finalized';");

            // Show all 'not finalized' records in 'projects' table.
            System.out.println("You chose: Option 4 - Finalize a project.");
            System.out.println("\n========== LIST OF NOT FINALIZED PROJECTS ==========");
            while (results.next()) {
                System.out.println("Project Number: " + results.getInt("proj_number")
                                + "\nProject Name: " + results.getString("proj_name")
                                + "\nBuilding Type:" + results.getString("building_type")
                                + "\nProject Finalized: " + results.getString("proj_finalized")
                                + "\nDate Completed: " + results.getDate("date_completed") + "\n");
            }

            // Get user selection for proj_number of the project they want to finalize.
            System.out.println("Enter the Project Number of the project you want to finalize: ");
            inputtedProjectNumber = inputScanner.nextInt();

            // Update 'Not Finalized' field to 'Finalized' and set completion date to current date.
            rowsAffected = statement.executeUpdate("UPDATE projects "
                    + "SET proj_finalized = 'Finalized', date_completed = current_date "
                    + "WHERE proj_number = " + inputtedProjectNumber +  " AND proj_finalized = 'Not Finalized';");

            // Print updated projects list.
            PrintProjects.getAllProjectsFull();
            System.out.println("\n---> UPDATE COMPLETE: "+ rowsAffected + " projects finalized. <---");

            // Close connections.
            results.close();
            statement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------- METHOD: FIND INCOMPLETE PROJECTS -------------------------------------------------
    /**
     * This method finds and prints all incomplete projects.
     * <p>
     * It finds and prints all projects with the value NULL in the date_completed field (i.e. projects which
     * do not have a completion date).
     */
    public static void getIncompleteProjects() {
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
            ResultSet results = statement.executeQuery
                    ("SELECT * FROM projects WHERE date_completed IS NULL;");
            int rowsFound = 0;

            // Print all ongoing projects (i.e. projects without a completion date).
            System.out.println("\n---> USER SELECTION: Option 5 - Find all incomplete projects. <---\n");
            System.out.println("========== LIST OF INCOMPLETE PROJECTS ==========");

            while (results.next()) {
                rowsFound++;
                System.out.println(
                        "\nProject Number: " + results.getInt("proj_number")
                                + "\nProject Name: " + results.getString("proj_name")
                                + "\nProject Deadline: " + results.getDate("proj_deadline")
                                + "\nProject Finalized: " + results.getString("proj_finalized")
                                + "\nProject Completion Date: " + results.getDate("date_completed")
                );
            }

            System.out.println("\n---> QUERY COMPLETE: "+ rowsFound + " incomplete projects found. <---");

            // Close connections.
            results.close();
            statement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------- METHOD: FIND OVERDUE PROJECTS --------------------------------------------------
    /**
     * This method finds and prints all overdue projects.
     * <p>
     * It finds and prints all projects where the proj_deadline field values are less than the current date
     * (i.e. projects whose deadline has already passed).
     */
    public static void getOverdueProjects() {
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
            ResultSet results = statement.executeQuery
                    ("SELECT * FROM projects WHERE proj_deadline < current_date;");
            int rowsFound = 0;

            // Loop through results and print all overdue projects (i.e. projects with a deadline
            // less than the current date).
            System.out.println("\n---> USER SELECTION: Option 6 - Find all overdue projects. <---\n");
            System.out.println("========== LIST OF OVERDUE PROJECTS ==========");

            while (results.next()) {
                rowsFound++;
                System.out.println("\nProject Number: " + results.getInt("proj_number")
                                + "\nProject Name: " + results.getString("proj_name")
                                + "\nProject Deadline: " + results.getDate("proj_deadline")
                                + "\nProject Finalized: " + results.getString("proj_finalized")
                                + "\nProject Completion Date: " + results.getDate("date_completed"));
            }

            System.out.println("\n---> QUERY COMPLETE: "+ rowsFound + " overdue projects found. <---");

            // Close connections.
            results.close();
            statement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------------- METHOD: FIND A PROJECT BY NAME OR NUMBER -----------------------------------------
    /**
     * This method finds a project by getting either the project name or the project number from the user, and
     * prints the project's full details.
     * <p>
     * If the user chooses to find a project by project number, only one project will be found because the
     * project number is a unique primary key. If the user choose to find a project by name, multiple projects
     * could be found because the project name is not unique. It prints the found projects by calling the method
     * {@link PrintProjects#getCurrentProjectFull(int inputtedProjectNumber)}.
     */
    public static void getProjectByNameOrNumber() {
        int userSelection;
        String inputtedProjectName;
        int inputtedProjectNumber;
        
        try {
            // Initialise Scanner object to get user input.
            Scanner inputScanner = new Scanner(System.in);

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
            ResultSet results = null;
            int rowsFound = 0;

            // Check if user wants to search by project number or project name.
            System.out.println("\n---> USER SELECTION: Option 7 - "
                    + "Find a project (by project name or project number). <---\n");

            System.out.println("Actions:"
                    + "\n1 - Find a project by project number"
                    + "\n2 - Find projects by project name"
                    + "\n\nEnter  the number of the action you want to take:");

            userSelection = Integer.parseInt(inputScanner.nextLine());

            if (userSelection == 1) {
                // Get inputtedProjectNumber and call method to get all details for the project with this number.
                // NOTE: since proj_number is the Primary Key, it will always only return one project.
                System.out.println("Enter the project number of the project you want to find: ");
                inputtedProjectNumber = inputScanner.nextInt();

                // Print all details of the inputted project number.
                PrintProjects.getCurrentProjectFull(inputtedProjectNumber);
                System.out.println("\n---> QUERY COMPLETE: " + rowsFound + " projects found. <---");
            }
            else if (userSelection == 2) {
                // Get inputtedProjectName and ResultSet of all projects with that name.
                System.out.println("Enter the project name of the project you want to find: ");
                inputtedProjectName = inputScanner.nextLine();
                results = statement.executeQuery
                        ("SELECT * FROM projects WHERE proj_name = '" + inputtedProjectName + "';");

                // Loop through results and increment rowsFound, get project number for each row found and
                // print all details of projects with this project name.
                // NOTE: since proj_name is not a Primary Key,it could return multiple projects.
                while (results.next()) {
                    rowsFound++;
                    inputtedProjectNumber = results.getInt("proj_number");
                    PrintProjects.getCurrentProjectFull(inputtedProjectNumber);
                }

                System.out.println("\n---> QUERY COMPLETE: " + rowsFound + " projects found. <---");
                results.close();

            } else {
                System.out.println("Invalid Entry! Please enter either 1 or 2.");
                getProjectByNameOrNumber();
            }

            // Close connections.
            statement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
