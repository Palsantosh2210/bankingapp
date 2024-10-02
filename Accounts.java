import java.sql.*;
import java.util.Scanner;

public class Accounts {
	private Connection connection ; 
	private Scanner scanner ;
	
	public Accounts(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	} 
	
	public long openAccount(String email) {
		
		if(!accountExist(email)) {
			String openAcQuery = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?)";
			
			scanner.nextLine();
			System.out.println("Enter full name");
			String fullName = scanner.nextLine();
			System.out.println("Enter Initial Amount");
			double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter your Security pin");
			String securityPin = scanner.nextLine();
			
			try {
				long accountNumber = generateAccountNumber();
				PreparedStatement preparedStatement = connection.prepareStatement(openAcQuery);
				preparedStatement.setLong(1, accountNumber);
				preparedStatement.setString(2,fullName);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4, balance);
				preparedStatement.setString(5,securityPin);
				
				int rowsAffected = preparedStatement.executeUpdate();
				if(rowsAffected > 0) {
					return accountNumber;
				}
				else{
					System.out.println("Account Creation Failed !!");
				}
			} catch (SQLException e) {
				System.out.println("Error: "+e.getMessage());
			}
			 
		}
		throw new RuntimeException("Account Already Exist");
		
	}
	
	 public long getAccount_number(String email) {
	        String query = "SELECT account_number from accounts WHERE email = ?";
	        try{
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, email);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if(resultSet.next()){
	                return resultSet.getLong("account_number");
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        throw new RuntimeException("Account Number Doesn't Exist!");
	    }
	
	
	private long generateAccountNumber() {
		String query= "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1 ";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet.next()) {
				long lastAccountNumber = resultSet.getLong("account_number");
				return lastAccountNumber +1 ; 
			}
			else {
				return 10000100;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return 1000100;
	}

	public boolean accountExist(String email) {
		String query = "SELECT account_number FROM accounts WHERE email = ? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,email);
			ResultSet resultSet = preparedStatement.executeQuery(); 
			
			if(resultSet.next()) {
				return true ; 
			}
			else {
				return false; 
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false ; 
	}

}