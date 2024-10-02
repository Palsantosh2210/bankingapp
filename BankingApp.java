import java.sql.*;
import java.util.Scanner;

public class BankingApp {

	private static final String url = "jdbc:postgresql://localhost:5432/demo";
	private static final String uname = "postgres";
	private static final String pswd = "root";

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = DriverManager.getConnection(url, uname, pswd);
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);
			Accounts accounts = new Accounts(connection, scanner);

			String email;
			long accountNumber;

			while (true) {
				System.out.println("==== Welcome to Banking System ===");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login ");
				System.out.println("3. Exit");
				System.out.println("Enter your choice ");
				int choice = scanner.nextInt();

				switch (choice) {
					case 1:
						user.register();
						break;
					case 2:
						email = user.login();
						if (email != null) {
							System.out.println();
							System.out.println("User Logged In!!");

							if (!accounts.accountExist(email)) {
								System.out.println();
								System.out.println("1. Open new Account");
								System.out.println("2. Return to Main Menu");
								int newAccChoice = scanner.nextInt();
								if (newAccChoice == 1) {
									accountNumber = accounts.openAccount(email);
									System.out.println("Your Account has been Successfully Created");
									System.out.println("Account Number is: " + accountNumber);
								} else {
									break;
								}
							} else {
								accountNumber = accounts.getAccount_number(email);
								int choice1 = 0;
								while (choice1 != 5) {
									System.out.println();
									System.out.println("1. Debit Money");
									System.out.println("2. Credit Money");
									System.out.println("3. Transfer Money");
									System.out.println("4. Check Balance");
									System.out.println("5. Log out");

									choice1 = scanner.nextInt();
									switch (choice1) {
										case 1:
											accountManager.debitMoney(accountNumber);
											break;
										case 2:
											accountManager.creditMoney(accountNumber);
											break;
										case 3:
											accountManager.transferMoney(accountNumber);
											break;
										case 4:
											accountManager.getBalance(accountNumber);
											break;
										case 5:
											System.out.println("Logging out...");
											break;
										default:
											System.out.println("Enter valid choice");
											break;
									}
								}
							}
						} else {
							System.out.println("Incorrect Email or Password");
						}
						break;
					case 3:
						System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
						System.out.println("Exiting System!");
						return;
					default:
						System.out.println("Enter Valid Choice");
						break;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}