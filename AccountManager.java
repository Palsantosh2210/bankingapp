import java.sql.*;
import java.util.Currency;
import java.util.Scanner;

public class AccountManager {
	private Connection connection ; 
	private Scanner scanner ;
	public AccountManager(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	} 
	
	public void creditMoney(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount");
		double amount = scanner.nextDouble(); 
		scanner.nextLine();
		System.out.println("Enter Security pin");
		String securityPin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			
			if(account_number != 0) {
				String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, securityPin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					String credit_query ="UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
					PreparedStatement preparedStatement2 = connection.prepareStatement(credit_query);
					preparedStatement2.setDouble(1, amount);
					preparedStatement2.setLong(2, account_number);
					int rowsAffected = preparedStatement2.executeUpdate(); 
					
					if(rowsAffected > 0) {
						System.out.println("Rs "+amount+" credited Sucessfully !");
						connection.commit();
						connection.setAutoCommit(true);
					}
					else {
						System.out.println("Transaction Failed ");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}
				else {
					System.out.println("Envalid Security Pin");
				}
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			connection.setAutoCommit(true);
			
			
		}
	}
	
	public void debitMoney(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount");
		double amount = scanner.nextDouble(); 
		scanner.nextLine();
		System.out.println("Enter Security pin");
		String securityPin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			
			if(account_number != 0) {
				String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, securityPin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					double currentBalance = resultSet.getDouble("balance");
					if(currentBalance >= amount) {
						
						String debit_query ="UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement preparedStatement2 = connection.prepareStatement(debit_query);
						preparedStatement2.setDouble(1, amount);
						preparedStatement2.setLong(2, account_number);
						int rowsAffected = preparedStatement2.executeUpdate(); 
						
						if(rowsAffected > 0) {
							System.out.println("Rs "+amount+" Debited Sucessfully !");
							connection.commit();
							connection.setAutoCommit(true);
						}
						else {
							System.out.println("Transaction Failed ");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					}
					else {
						System.out.println("Insufficient Balance!!");
					}
				}
				else {
					System.out.println("Invalid Security Pin");
				}
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			connection.setAutoCommit(true);
		}
	}
	
	public void transferMoney(long senderAccountNumber) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Receiver Account Number");
		long receiverAccountNumber = scanner.nextLong(); 
		System.out.println("Enter Amount to Send");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security Pin");
		String securityPin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if (senderAccountNumber != 0 && receiverAccountNumber != 0) {
				String query = "SELECT * FROM accounts WHERE account_number = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setLong(1, senderAccountNumber);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					long currentAccountBlc = resultSet.getLong("balance");
					if ( amount <= currentAccountBlc) {
						String credit_query ="UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
						String debit_query ="UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						
						PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
						PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
						
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setLong(2, senderAccountNumber);
						
						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setLong(2,receiverAccountNumber);
						
						int debitRows = debitPreparedStatement.executeUpdate();
						int creditRows = creditPreparedStatement.executeUpdate();
						
						if (debitRows > 0 && creditRows >0) {
							System.out.println("Transaction Successfull!");
							System.out.println("Rs "+amount+" Transferred Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return ; 
						} else {
							System.out.println("Transaction Failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance");
					}
				}
				else {
					System.out.println("Invalid Security pin or Account Number ");
				}
				
			} else {
				System.out.println("Invalid Account Number");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			connection.setAutoCommit(true);
		}
	}
	
	public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
