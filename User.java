import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	private Connection connection ; 
	private Scanner scanner ; 
	
	public User(Connection connection, Scanner scanner) {
		this.connection = connection; 
		this.scanner = scanner; 
	}
	
	public void register() {
	    scanner.nextLine();  // Clear any previous input
	    System.out.println("Enter Your Full Name ");
	    String fullName = scanner.nextLine();
	    System.out.println("Enter Your Email");
	    String email = scanner.nextLine();
	    System.out.println("Password:");
	    String password = scanner.nextLine(); 

	    if (userExits(email)) {
	        System.out.println("User Already Exists for this Email Address");
	        return;
	    }

	    String registerQuery = "INSERT INTO users(full_name, email, password) VALUES(?, ?, ?);";
	    
	    try {
	        PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
	        preparedStatement.setString(1, fullName);
	        preparedStatement.setString(2, email);
	        preparedStatement.setString(3, password);
	        
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Registration Successful!");
	        } else {
	            System.out.println("Registration Failed!");
	        }
	    } catch (SQLException e) {  // Properly formatted catch block
	        System.out.println("Error: " + e.getMessage());  
	    }
	}

	public String login() {
		scanner.nextLine(); 
		System.out.println("Enter your Email id ");
		String email = scanner.nextLine();
		System.out.println("Enter your password");
		String password = scanner.nextLine();
		
		String loginQuery = "SELECT * FROM users WHERE email = ? AND password = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return email;
			}
			else {
				return null; 
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public boolean userExits(String email) {
		String query = "SELECT * FROM users WHERE email = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true; 
			}
			else {
				return false; 
			}
		 } catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}