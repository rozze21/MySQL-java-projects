package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;


/**
 * This class is a menu-driven application that accepts user input from the console. it performs
 * CRUD operations on the project tables.
 * 
 * @author rocio
 *
 */
public class ProjectsApp {
  // scanner to obtain input from user
  private Scanner scanner = new Scanner(System.in);
  private ProjectService projectService = new ProjectService();
  private Project curProject;

  // @formatter:off
  //holds the list of operations 
  private List<String> operations = List.of(
      "1) Add a project",
      "2) List projects",
      "3) Select project",
      "4) Update project details",
      "5) Delete a project");
  // @formatter:on


  /**
   * Entry point for Java application
   * 
   * @param args Unused.
   */

  public static void main(String[] args) {
    // creating a new ProjectsApp object
    new ProjectsApp().processUserSelections();
  }

  /**
   * Displays the menu selections, gets a selection from the user, and then acts on the selection.
   */
  private void processUserSelections() {
    boolean done = false;

    while (!done) {
      try {
        int selection = getUserSelection();

        switch (selection) {
          case -1:
            done = exitMenu();
            break;

          case 1:
            createProject();
            break;

          case 2:
            listProjects();
            break;
            
          case 3:
            selectProject();
            break;
            
          case 4:
            updateProjectDetails();
            break;

          case 5:
            deleteProject();
            break;

            
          default:
            System.out.println("\n" + selection + " is not a valid selection. Try again.");
            break;
        }
      } catch (Exception e) {
        System.out.println("\nError: " + e + " Try again.");
      }
    }
  }

  private void deleteProject() {
    listProjects();
    Integer projectId = getIntInput("Enter the ID of the project to delete");
     
    
    projectService.deleteProject(projectId);
    System.out.println("Project " + projectId + " was deleted successfully.");
   
    if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {

    curProject = null;
  }
 } 
 

  //method to get project detail changes from the user and call the project service to make the modifications. 
  private void updateProjectDetails() {
    if (Objects.isNull(curProject)) {
      System.out.println("\nPlease select a project.");
      return;
    }
    
  String projectName = 
      getStringInput("Enter the project name [" + curProject.getProjectName()+ "]");
  BigDecimal estimatedHours = 
      getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours()+ "]");
  BigDecimal actualHours = 
      getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
  Integer difficulty = 
      getIntInput("Enter the project difficulty (1-5)[" + curProject.getDifficulty() + "]");
  String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
  
  Project project = new Project();

  
  project.setProjectId(curProject.getProjectId());
  project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName(): projectName);
  project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
  project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours(): actualHours);
  project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty(): difficulty);
  project.setNotes(Objects.isNull(notes) ? curProject.getNotes(): notes);
    
    projectService.modifyProjectDetails(project);
    
    curProject = projectService.fetchProjectById(curProject.getProjectId());
    
  }
  
 
  private void selectProject() {
    
    listProjects();
    Integer projectId = getIntInput("Enter a project ID to select a project");
    
    /* Unselect the current project. */
    curProject = null;
    
    // This will throw an exception if an invalid project ID is entered. 
    // Calling method fetchProjectbyId on projectService object assigned to instance variable
    curProject = projectService.fetchProjectById(projectId);
  }
    
  private void listProjects() {
    List<Project> projects = projectService.fetchAllProjects();
    
    System.out.println("\nProjects:");
    
    projects.forEach(project -> System.out.println("   " + project.getProjectId() 
    + ": " + project.getProjectName())); 
    
  }

  /**
   * Gather user input for a project row then call the project service to create the row.
   */
  private void createProject() {
    String projectName = getStringInput("Enter the project name");
    BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
    BigDecimal actualHours = getDecimalInput("Enter the actual hours");
    Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
    String notes = getStringInput("Enter the project notes");

    Project project = new Project();

    project.setProjectName(projectName);
    project.setEstimatedHours(estimatedHours);
    project.setActualHours(actualHours);
    project.setDifficulty(difficulty);
    project.setNotes(notes);

    Project dbProject = projectService.addProject(project);
    System.out.println("You have sucessfully created project: " + dbProject);

  }

  /**
   * Gets the user's input from the console and converts it to a BigDecimal.
   * 
   * @param prompt The prompt to display on the console.
   * @return A BigDecimal value if successful.
   * @throws DbException Thrown if an error occurs converting the number to a BigDecimal.
   */

  private BigDecimal getDecimalInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }
    try {
      /* Create the BigDecimal object and set it to two decimal places (the scale). */
      return new BigDecimal(input).setScale(2);

    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid decimal number.");
    }
  }


  /**
   * Called when the user wants to exit the application. It prints a message and returns
   * {@code true} to terminate the app.
   * 
   * @return {@code true}
   */
  private boolean exitMenu() {
    System.out.println("Exiting the menu.");
    return true;
  }

  /**
   * This method prints the available menu selections. It then gets the user's menu selection from
   * the console and converts it to an int.
   * 
   * @return The menu selection as an int or - 1 if nothing is selected.
   * 
   */
  private int getUserSelection() {
    printOperations();

    Integer input = getIntInput("Enter a menu selection");

    return Objects.isNull(input) ? -1 : input;
  }

  /**
   * Prints a prompt on the console and then gets the user's input from the console. It then
   * converts the input to an Integer.
   * 
   * @param prompt The prompt to print
   * @return If the user enters nothing, {@code null} is returned. Otherwise, the input is converted
   *         to an Integer.
   * @throws DbException Thrown if the input is not a valid Integer
   */
  private Integer getIntInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid number.");
    }
  }

  /**
   * Prints a prompt on the console and then gets the user's input from the console. If the user
   * enters nothing, {@code null} is returned.
   * 
   * @param prompt The prompt to print.
   * @return The user's input or {@code null}.
   * 
   */
  private String getStringInput(String prompt) {
    System.out.print(prompt + ": ");
    String input = scanner.nextLine();

    return input.isBlank() ? null : input.trim();
  }

  /**
   * Print the menu selections, one per line.
   */
  // prints each available selection on a separate line in the console.
  private void printOperations() {
    System.out.println();
    System.out.println("These are the available selections. Press the Enter key to quit:");

    operations.forEach(line -> System.out.println("   " + line));
 
    if (Objects.isNull(curProject)) {
      System.out.println("\nYou are not working with a project.");
    } else {
     System.out.println("\nYou are working with project " + curProject);

    }
  }
}
