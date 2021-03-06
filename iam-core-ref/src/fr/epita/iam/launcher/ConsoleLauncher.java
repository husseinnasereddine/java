/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;

/**
 * @author tbrou
 *
 */
public class ConsoleLauncher {
	
	private static JDBCIdentityDAO dao;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		
		
		System.out.println("Hello, welcome to the IAM application");
		Scanner scanner = new Scanner(System.in);
		dao = new JDBCIdentityDAO();
		String answer = null;
		
		
		//authentication
		System.out.println("Login Please:");
		String login = scanner.nextLine();
		System.out.println("Password Please:");
		String password = scanner.nextLine();
		
		if(!authenticate(login, password)){
			System.out.println("wrong credentials, not allowed to enter.");
			scanner.close();
			
			return;
		}
		System.out.println("You're authenticated");
		do{
		// menu
		 answer = menu(scanner);
		
		switch (answer) {
		case "a":
			// creation
			createIdentity(scanner);
			break;
		case "b":
			updateIdentity(scanner); 
			break;
		case "c":
			deleteIdentity(scanner);
			break;
		case "d":
			listIdentities();
			break;
		case "e":
			break;
		default:
			System.out.println("choose well, the problem is: ("+ answer +" )");
			break;
		}
		
	} while(!answer.equals("e"));
	

		scanner.close();
		System.out.println("Thank you! Have a nice day sir!");
	}

	/**
	 * @param login
	 * @param password
	 * @return
	 */
private static boolean authenticate(String login, String password) {
		
		
		try {
			if (dao.authenticate(login, password)){
				System.out.println("Athentication was successful");
				
				return true;
			}else{
				System.out.println("Athentication failed");
				
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong and we could authenticate you try restart the application");
			return false;
			}
	}


	/**
	 * @throws SQLException 
	 * 
	 */
	private static void listIdentities() throws SQLException {
		System.out.println("Those are all info available:");
		List<Identity> list = dao.readAll();
		int size = list.size();
		for(int i = 0; i < size; i++){
			System.out.println( i+ "." + list.get(i));
		}
		
	}

	/**
	 * @param scanner
	 * @throws SQLException 
	 */
	private static void createIdentity(Scanner scanner) throws SQLException {
		System.out.println("Item Selected : Identity Creation");
		System.out.println("Please enter the Identity display name");
		String displayName = scanner.nextLine();
		System.out.println("Please enter the Identity email");
		String email = scanner.nextLine();
		Identity newIdentity = new Identity(null, displayName, email);
		dao.writeIdentity(newIdentity);
		System.out.println("Identity created successfully :" + newIdentity);
	}

	
	private static void updateIdentity(Scanner scanner) throws SQLException
	{
		Identity identity = null;
		String choice = null;
		String email = null;	
		
		
		
		System.out.print("Update: please enter your email");
			if ((email = scanner.nextLine()).equals("e"))
			{
				return;
			}
				
			if (null == (identity = dao.fetch(email))) 
			{
			
				System.out.println("No such Email, please try again");
			return;
			}
			do
			{
		System.out.println("Current Record: "+identity.toString()+"\nSelect a field to update\na) Email \nb) Name\n");
        choice = scanner.nextLine();
		
		switch (choice)
		{
		case "a":
			System.out.println("Enter new email");
			identity.setEmail(scanner.nextLine()); 
			break;
		case "b":
			System.out.println("Enter new Name");
			identity.setDisplayName(scanner.nextLine()); 
		    break;

		 default:
			 System.out.println("enter a correct option to update");
		}
		}while (!choice.equals("b")&& !choice.equals("a"));
		    try {
				dao.update(identity);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to update (Database contectivity issue, possibly) Exiting...");

				return;				
			}		System.out.println("UPdated record as:"+identity.toString());
		
		
		
		
	}
	
	private static void deleteIdentity(Scanner scanner) throws SQLException {
	
		Identity identity = null;
		String choice = null;
		String email = null;
		
		
		System.out.print("Welcome to the Deletion tool");
		
		System.out.println("Please enter the mail\n<type \"e\" to quite update procedure>");
	    if ((email = scanner.nextLine()).equals("e"))
	    {
	
	    	return;
	    }
	    try {
					dao.delete(email);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Failed to Delete record. Exiting...");
					return;
				}
			
	    System.out.print("deleted");
		
	
	}
	
	
	private static String menu(Scanner scanner) {
		
		System.out.println("Here are the actions you can perform :");
		System.out.println("a. Create an Identity");
		System.out.println("b. Modify an Identity");
		System.out.println("c. Delete an Identity");
		System.out.println("d. List Identities");
		System.out.println("e. quit");
		System.out.println("your choice (a|b|c|d|e) ? : ");
		String answer = scanner.nextLine();
		return answer;
	}

	/**
	 * @param login
	 * @param password
	 */
	

	
	
	
}
