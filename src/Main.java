/*===================================================================================================================
    The 'Main' class contains the following methods:
    ●   Get User Action:
            - prints a selection of actions the user can take, gets user selection of the action to take,
              and returns the selection (i.e. action number).
    ●   Main Method:
            - calls the getUserAction() method to get the user's choice of action to take,
              then calls the related class' methods depending on the action selected.
            - Please see the Java Classes 'Projects.java', 'PrintProjects.java', 'Architects.java',
              'Contractors.java', 'Managers.java' and 'Customers.java' for descriptions of the methods
              in each related class.
===================================================================================================================*/

import java.util.Scanner;

/**
 * This is the main class which reads, writes, updates and deletes data about projects and people in the database.
 * <p>
 *  The program forms part of the project management system for a small structural engineering firm called
 *  “Poised”, and uses Java and MySQL to connect to the database 'PoisePMS'.
 *
 * @author Nichole Dobbin
 * @version JDK 18.0.1.1
 */
public class Main {
    // ---------------------------------------- GET USER ACTION METHOD -----------------------------------------------
    /**
     * This method prints an action menu for the user and returns the user's selection.
     * <p>
     * It prints a selection of actions the user can take to read/write/update the database,
     * gets the user's selection of the action to take, and returns the selection (i.e. action number).
     * The method is called in the main method of this class.
     *
     * @return returns user selection of action to take.
     */
    public static int getUserAction() {

        // Initialise variable with text block to store Action Menu for user.
        String actionMenu = """
                1. Add a new project
                2. Update an existing project
                3. Delete an existing project
                4. Finalize an existing project
                5. Find all incomplete projects
                6. Find all overdue projects
                7. Find a project by project name or project number
                0. Exit""";

        // Initialise variable to store user input.
        int userSelection;

        // Create Scanner object to get user input and print action menu.
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("\n----- ACTION MENU -----\n" + actionMenu + "\n");
        System.out.println("Enter the number of the action you want to take:");
        userSelection = inputScanner.nextInt();

        // Return user selection of action to take.
        return userSelection;
    }

    // -------------------------------------- MAIN METHOD ----------------------------------------------------------
    // This is the main method for this project. It calls the getUserAction() method to get the user's choice of
    // action to take, then calls the related class' methods depending on the action selected.
    /**
     * This is the main method which uses the {@link #getUserAction()} method to perform various operations on
     * the database, based on the selected action.
     * <p>
     * The various actions are performed by calling the methods listed in the See Also section.
     *
     * @param args Unused.
     * @see Projects#addNewProject()
     * @see Projects#updateProject()
     * @see Projects#deleteProject()
     * @see Projects#finalizeProject()
     * @see Projects#getIncompleteProjects()
     * @see Projects#getOverdueProjects()
     * @see Projects#getProjectByNameOrNumber()
     */

    public static void main(String[] args) {
        // Create variable to store user selection.
        int userSelection;

        // Do-while loop gets user choice of action to take, calling methods from related classes.
        do {
            // Print action menu and get user selection.
            userSelection = getUserAction();

            // OPTION 1 - Add a new project.
            if (userSelection == 1) {
                Projects.addNewProject();
            }

            // OPTION 2 - Update an existing project.
            else if (userSelection == 2) {
                Projects.updateProject();
            }

            // OPTION 3 - Delete an existing project.
            else if (userSelection == 3) {
                Projects.deleteProject();
            }

            // OPTION 4 - Finalize an existing project.
            else if (userSelection == 4) {
                Projects.finalizeProject();
            }

            // OPTION 5 - Find all incomplete projects.
            else if (userSelection == 5) {
                Projects.getIncompleteProjects();
            }

            // OPTION 6 - Find all overdue projects.
            else if (userSelection == 6) {
                Projects.getOverdueProjects();
            }

            // OPTION 7 - Find a project by project name or project number.
            else if (userSelection == 7) {
                Projects.getProjectByNameOrNumber();
            }

            // Error message if user inputs invalid action number.
            else if (userSelection == 8 || userSelection == 9){
                System.out.println("Invalid Entry! Please enter the number of the action you want to take.");
            }

        // Stop Condition: When user selects option 0, exit do-while loop.
        } while (userSelection != 0);
    }
}
