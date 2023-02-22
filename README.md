# **MySQL and Java: Database Project Management System**

## **IFS L3T08 - Capstone Project: Databases**

## **Description**
This is a database Project Management System (PMS) for a hypothetical company called 'Poised', 
which keeps track of the projects they work on. The program uses Java and MySQL to connect to 
a database called 'PoisePMS', and to read, write, update and delete data about projects and people
associated with projects in the database.

### **Database Details (MySQL)**
The 'PoisePMS' database was designed with MySQL and contains the following 5 tables:
1. Projects Table 
2. Architects Table
3. Contractors Table
4. Customers Table
5. Managers Table
To view the database and tables Entity Relationship Diagram (ERD), see the [L3T7_ERD.pdf](extras/L3T7_ERD.pdf) file.

### **Program Details (Java)**
The program contains 7 Classes with the following methods and functionality:
1. **Main Class:**
   * reads, writes, updates and deletes data about projects and people in the database.
   * getUserAction() method: prints an action menu for the user and returns the user's selection.
   * main() method: calls getUserAction() method to get user's choice of action to take, then calls
     the related class' methods depending on the action selected.

2. **Projects Class:**
   * reads, writes, updates and deletes data about projects and people in the database.
   * constructor method for projects instances.
   * assignProjectName() method: assigns a project name as (Building Type) + (Customer Surname) when
     the name is unknown.
   * addNewProject() method: retrieves all the information required to create and add a new project to the database.
   * updateProject() method: allows the user to update fields in any of the existing projects.
   * deleteProject() method: deletes a project and its associated people if all people assigned to the project
     are not assigned to any other projects. If it's people are assigned elsewhere, only the project's data is deleted.
   * finalizeProject() method: finalizes a project.
   * getIncompleteProjects() method: finds and prints all incomplete projects.
   * getOverdueProjects() method: finds and prints all overdue projects.
   * getProjectByNameOrNumber() method: finds a project by getting either the project name or the project number
     from the user, and prints the project's full details.

3. **PrintProjects Class:**
   * reads and prints data about projects and associated people in the database.
   * getCurrentProjectConcise() method: gets the main details for the project being searched and prints a
     concise list of the project's details.
   * getCurrentProjectFull() method: This method gets all details for the project being searched and prints a
     full list of the project's details.
   * getAllProjectsConcise() method: gets the main details for all projects in the database and prints a concise
     list of all projects' details.
   * getAllProjectsFull() method: gets all details for all projects in the database and prints a full list of all
     projects' details.

4. **Architects Class:**
   * reads and prints data about the architects in the database.
   * constructor method for architects instances.
   * getArchitectsList() method: gets and prints all details for all architects in the database.
   * chooseArchitect() method: gets the user's selection of an architect in the database.

5. **Contractors Class:**
   * reads and prints data about the contractors in the database.
   * contractor method for contractors instances.
   * getContractorsList() method: gets and prints all details for all contractors in the database.
   * chooseContractor() method: gets the user's selection of a contractor in the database.

6. **Managers Class:**
   * reads and prints data about the project managers in the database.
   * contractor method for project managers instances.
   * getManagersList() method: gets and prints all details for all project managers in the database.
   * chooseManager() method: gets the user's selection of a project manager in the database.

7. **Customers Class:**
   * reads, writes and prints data about the customers in the database.
   * contractor method for customers instances.
   * getCustomersList() method: gets and prints all details for all customers in the database.
   * chooseCustomer() method: gets the user's selection of a customer in the database.
   * addCustomer() method: gets user input for customer details and adds a new customer to the database.

### **JavaDocs**
To view the methods called from the main class, you can view the [JavaDocs API documentation](L3T7_JavaDocs), 
or you can navigate to each class/file in the [src](src) folder.

<hr> 

## **Installation and Setup**
1. You need to set up your own MySQL server and database for this project. See [dev.mysql.com](https://dev.mysql.com/doc/mysql-getting-started/en/) for instructions on how to do this.
2. To recreate the database that I used, you can use the [L3T7-SQL-Code.txt](extras/L3T7-SQL-Code.txt) 
file as a guide, or the [L3T7_ERD.pdf](extras/L3T7_ERD.pdf) file for this project's ERD.
3. This program is run using the JDBC. To set up the environment for running this program:
    * Download and install the JDK. You can download it from [here](https://www.oracle.com/java/technologies/downloads/).
    * Download and install the MySQL JDBC driver to access the MySQL database. You can download it from
      [here](https://dev.mysql.com/downloads/mysql/).
    * Optional: use an IDE of your choice to run the program (e.g. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac)).

<hr> 

## **How to Use**
1. Start your MySQL server.
2. Clone the repo and open with your preferred IDE (e.g. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac)).
3. Navigate to the Main class and run the main() method. This will print a selection of actions you can take 
to search, update, add or delete projects and their associated people.

<hr> 

## **Credits and References**
Made by [Nichole Dobbin](https://github.com/nicholedobbin), for my [HyperionDev](https://www.hyperiondev.com/) course.
Please see [L3T7_References.txt](extras/L3T7_References.txt) for the list of references I used in this task.
