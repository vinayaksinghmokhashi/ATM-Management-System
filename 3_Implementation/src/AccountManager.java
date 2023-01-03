import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AccountManager {
	ArrayList <Object> arr=new ArrayList<Object>();
	Scanner sc=new Scanner(System.in);
	 static Validator valid=new Validator();



	//1
	public String Withdraw(int account_number,Connection con) {
		try {
			System.out.println("Please enter the amount to be withdrawn:");
			double amount = sc.nextDouble();
			if(amount%100==0)
			{
				String validateBalance = "SELECT balance from customer where account_number= "+account_number;
				PreparedStatement miniStatementPst = con.prepareStatement(validateBalance);
				ResultSet customer_current_balance = miniStatementPst.executeQuery(); 
				double amount1=0;
				while(customer_current_balance.next()) amount1=customer_current_balance.getDouble("balance");
				if(amount1<amount)
					return "Insufficient balance!";
			String withdrawQuery = "UPDATE customer SET balance = balance - ? WHERE account_number = ?";
			PreparedStatement withdrawPst = con.prepareStatement(withdrawQuery);
			withdrawPst.setDouble(1, amount);
			withdrawPst.setInt(2, account_number);
			int withdrawRowsUpdated = withdrawPst.executeUpdate();

			if (withdrawRowsUpdated > 0) {
				String transactionQuery = "INSERT INTO transactions (account_number,amount,type) VALUES (?,?,?)";
				PreparedStatement transactionPst = con.prepareStatement(transactionQuery);
				transactionPst.setInt(1, account_number);
				transactionPst.setDouble(2, amount);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				transactionPst.setString(3,"on "+formatter.format(date)+ " Withdraw -");
				int rowsInserted = transactionPst.executeUpdate();
				if (rowsInserted > 0) {
					return ("Withdrawal Successful!"+"\n Please collect your cash!");
				} else {
					return ("Withdrawal Unsuccessful!");
				}
			} else {
				return ("Withdrawal Unsuccessful!");
			}
			}
			else
			{
				return "Amount should be in multiples of 100 only!";
			}
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}

	//2
	public String deposit(int account_number,Connection con) {
		try {
			System.out.println("Please enter the amount to be deposited:");
			double depositAmount = sc.nextDouble();
			if(depositAmount%100!=0)
				return "Amount should be in multiples of 100 only!";
			String depositQuery = "UPDATE customer SET balance = balance + ? WHERE account_number = ?";
			PreparedStatement depositPst = con.prepareStatement(depositQuery);
			depositPst.setDouble(1, depositAmount);
			depositPst.setInt(2, account_number);
			int depositRowsUpdated = depositPst.executeUpdate();

			if (depositRowsUpdated > 0) {
				//save the transaction to the transactions table
				String transactionQuery = "INSERT INTO transactions (account_number,amount,type) VALUES (?,?,?)";
				PreparedStatement transactionPst = con.prepareStatement(transactionQuery);
				transactionPst.setInt(1, account_number);
				transactionPst.setDouble(2, depositAmount);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				transactionPst.setString(3,"on "+formatter.format(date)+ " Depositted +");
				int rowsInserted = transactionPst.executeUpdate();
				if (rowsInserted > 0) {
					return ("Deposit Successful!");
				} else {
					return ("Deposit Unsuccessful!");
				}
			} else {
				return ("Deposit Unsuccessful!");
			}
		}
		
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
		
	}

	//3
	public String balanceTransfer(int account_number,Connection con) {
		try {
			System.out.println("Please enter the amount to be transferred:");
			double transferAmount = sc.nextDouble();
			String validateBalance = "SELECT balance from customer where account_number= "+account_number;
			PreparedStatement miniStatementPst = con.prepareStatement(validateBalance);
			ResultSet customer_current_balance = miniStatementPst.executeQuery(); 
			double amount1=0;
			while(customer_current_balance.next()) amount1=customer_current_balance.getDouble("balance");
			if(amount1<transferAmount)
				return "Insufficient balance to initiate transfer!";
			
			System.out.println("Please enter the recipient account number:");
			int recipientAccountNumber = sc.nextInt();
             
			
			String transferQuery = "UPDATE customer SET balance = balance - ? WHERE account_number = ? and isactive ='Y'";
			PreparedStatement transferPst = con.prepareStatement(transferQuery);
			transferPst.setDouble(1, transferAmount);
			transferPst.setInt(2, account_number);
			int transferRowsUpdated = transferPst.executeUpdate();

			if (transferRowsUpdated > 0) {
				//updating the recipient's balance
				String recipientTransferQuery = "UPDATE customer SET balance = balance + ? WHERE account_number = ? and isactive ='Y'";
				PreparedStatement recipientTransferPst = con.prepareStatement(recipientTransferQuery);
				recipientTransferPst.setDouble(1, transferAmount);
				recipientTransferPst.setInt(2, recipientAccountNumber);
				int recipientTransferRowsUpdated = recipientTransferPst.executeUpdate();

				if (recipientTransferRowsUpdated > 0) {
					//saving the transaction to the transactions table
					String transactionQuery = "INSERT INTO transactions (account_number,amount,type,recipient_account_number) VALUES (?,?,?,?)";
					PreparedStatement transactionPst = con.prepareStatement(transactionQuery);
					transactionPst.setInt(1, account_number);
					transactionPst.setDouble(2, transferAmount);
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = new Date();
					transactionPst.setString(3,"on "+formatter.format(date)+ " Transfered -");
					transactionPst.setInt(4, recipientAccountNumber);
					int rowsInserted = transactionPst.executeUpdate();
					if (rowsInserted > 0) {
						return ("Balance Transfer Successful!");
					} else {
						return ("Balance Transfer Unsuccessful!");
					}
				} else {
					return ("Balance Transfer Unsuccessful!");
				}
			} else {
				return ("Balance Transfer Unsuccessful!");
			}
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
      return null;
	}
	//4
	public String blockAtm(int account_number,Connection con){
		try {
			String blockAtmCardQuery = "UPDATE customer SET isactive = 'N' WHERE account_number = ?";
			PreparedStatement blockAtmCardPst = con.prepareStatement(blockAtmCardQuery);
			blockAtmCardPst.setInt(1, account_number);
			int blockAtmCardRowsUpdated = blockAtmCardPst.executeUpdate();

			if (blockAtmCardRowsUpdated > 0) {
				return ("ATM Card Blocked Successfully!");
			} else {
				return ("ATM Card Blocking Unsuccessful!");
			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	//5
	public String displayMiniStatement(int account_number,Connection con) {
		
		String statement ="";
		
		try {
			String miniStatementQuery = "SELECT * from transactions WHERE account_number = ? ORDER BY transaction_id DESC LIMIT 5";
			PreparedStatement miniStatementPst = con.prepareStatement(miniStatementQuery);
			miniStatementPst.setInt(1, account_number);
			ResultSet miniStatementRs = miniStatementPst.executeQuery();

			System.out.println("Mini Statement:");
			

			while (miniStatementRs.next()) {
				
				String type = miniStatementRs.getString("type");
				double amount = miniStatementRs.getDouble("amount");
				statement+= type + " " + amount+"\n";
	
				
			}
			
			File f=new File("mini.txt");
	          FileWriter fw=new FileWriter(f);
	          fw.write(statement);
	            fw.close();          			
			FileReader fr=new FileReader(f);
			BufferedReader br1=new BufferedReader(fr);
			String line;
			
			while((line=br1.readLine())!=null) {
				System.out.println(line);
			}
			br1.close();fr.close();
			f.delete();


			
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return "";
	}

	//6
	public String displayBalance(int account_number,Connection con) {
		try {
			String balanceQuery = "SELECT balance from customer WHERE account_number = ?";
			PreparedStatement balancePst = con.prepareStatement(balanceQuery);
			balancePst.setInt(1, account_number);
			ResultSet balanceRs = balancePst.executeQuery();
			if (balanceRs.next()) {
				double balance = balanceRs.getDouble("balance");
				return ("Your balance is: " + balance);
			} else {
				return ("Unable to fetch balance!");
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	//7
	public String changeAtmPin(int account_number,Connection con) {
		try {
			System.out.println("Please enter your new ATM PIN:");
			int newAtmPin = sc.nextInt();
			String changeAtmPinQuery = "UPDATE customer SET atm_pin = ? WHERE account_number = ?";
			PreparedStatement changeAtmPinPst = con.prepareStatement(changeAtmPinQuery);
			changeAtmPinPst.setInt(1, newAtmPin);
			changeAtmPinPst.setInt(2, account_number);
			int changeAtmPinRowsUpdated = changeAtmPinPst.executeUpdate();
			if (changeAtmPinRowsUpdated > 0) {
				return ("ATM PIN changed successfully!");
			} else {
				return ("Unable to change ATM PIN!");
			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	//8
	public String changeAccountPassword(int account_number,Connection con) {
		try {
			System.out.println("Please enter your new account password:");
			String newAccountPassword = sc.next();
			try {
				if(valid.validatePassword(newAccountPassword))
				{
					String changeAccountPasswordQuery = "UPDATE customer SET password = ? WHERE account_number = ?";
					PreparedStatement changeAccountPasswordPst = con.prepareStatement(changeAccountPasswordQuery);
					changeAccountPasswordPst.setString(1, newAccountPassword);
					changeAccountPasswordPst.setInt(2, account_number);
					int changeAccountPasswordRowsUpdated = changeAccountPasswordPst.executeUpdate();
					if (changeAccountPasswordRowsUpdated > 0) {
						return ("Account password changed successfully!");
					} else {
						return ("Unable to change account password!");
					}
				}
				else
				{
					return "Password should meet the standard specifications!";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void createCustomer(Connection conn)  { 
		Statement stmt = null;
		try {
			stmt = conn.createStatement(); 
			Scanner scanner = new Scanner(System.in); 
			System.out.print("Enter name: "); 
			String name = scanner.next(); 
			System.out.print("Enter email: "); 
			String email = scanner.next(); 
			System.out.print("Enter ATM PIN: "); 
			int atmPin = scanner.nextInt(); 
			System.out.print("Enter account password: "); 
			String accountPassword = scanner.next(); 
			System.out.println("Enter phone number");
			int phone_number=scanner.nextInt();
			String sqlQuery = "INSERT INTO customer (name, email_id, atm_pin, password,phone_number) VALUES('" + name + "', '" + email + "', " + atmPin + ", '" + accountPassword + "', "+   phone_number +")"; 
			int rowsAffected = stmt.executeUpdate(sqlQuery); 
			if (rowsAffected == 1) { 
				System.out.println("Customer created successfully."); 
			} else { 
				System.out.println("Error creating customer."); 
			} 
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	} 
}
