import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ATMMachine {
	static Scanner scanner=new Scanner(System.in);

	static String accountPass=null;
	static int option;
	static AccountManager m=new AccountManager();
    static Validator valid=new Validator();

	public static void main(String[] args){
		System.out.println("Welcome to my ATM");
		System.out.println("Enter Account number");
		int account_number=scanner.nextInt();
		System.out.println("Enter Account password");
		
		accountPass=new String(scanner.next());
		
		try {
			Connection myConn=null;
						String dbURL="jdbc:mysql://localhost:3306/atm?useSSL=false";
			String user="jai";
			String pass="1234";
			try {
				myConn=DriverManager.getConnection(dbURL,user,pass);
			} catch (SQLException e) {

				e.printStackTrace();
			}
			Statement mySttmt=myConn.createStatement();
			ResultSet res=mySttmt.executeQuery("select account_number from customer where account_number="+account_number+" and isActive='N'");
			if(res.next())
			System.out.println("Your account is blocked. Please contact your nearest branch!");
			else {
			
			ResultSet results=mySttmt.executeQuery("select account_number,password,atm_pin,name from customer where account_number= "+account_number + " and isActive='Y'");
			
			if(results.next())
			{
				if(results.getString("password").equalsIgnoreCase(accountPass))
				{	   
					System.out.println("Login Successful!");
					
					System.out.println("Welcome "+results.getString("name"));
					
					System.out.println("Choose account type: \n1.Saving \n2.Current ");
					int acc_type=scanner.nextInt();
					while(true) {
						System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
						System.out.println("What do you want to do?");
						System.out.println("1. Withdraw");
						System.out.println("2. Deposit");
						System.out.println("3. Transfer");
						System.out.println("4. Block Atm card");
						System.out.println("5. Display Mini Statement");
						System.out.println("6. Display Balance");
						System.out.println("7. Change Atm PIN");
						System.out.println("8. Change Account Password");
						System.out.println("9. Exit");
						System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");


						System.out.println("Enter an option from above");
						Scanner sc = new Scanner(System.in);
						option = sc.nextInt();

						switch(option) {
						case 1:
							System.out.println( m.Withdraw(account_number,myConn));
							
							break;
							
						case 2:
							System.out.println( m.deposit(account_number, myConn));
							break;
							
						case 3:
						System.out.println(m.balanceTransfer(account_number, myConn));
							break;
							
						case 4:
							System.out.println( m.blockAtm(account_number, myConn));
							break;
							
						case 5:
							System.out.println(	m.displayMiniStatement(account_number, myConn));
							break;
							
						case 6:
							System.out.println(m.displayBalance(account_number, myConn));
							break;
							
						case 7:
							System.out.println( m.changeAtmPin(account_number, myConn));
							break;

						case 8:
							try {
								System.out.println( m.changeAccountPassword(account_number, myConn));
							} catch (Exception e) {
								// TODO: handle exception
							}
							break;
						case 9:
							sc.close();
							System.exit(0);
						default :
							System.out.println("Wrong input!");
						}
					}      
				
				}  
				else
				{
					System.out.println("Wrong Password!");
				}
			}
			else {
				System.out.println("Account does not exist!");
			}
			}
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
		