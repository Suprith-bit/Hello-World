import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

abstract class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New Balance: $" + balance);
        } else {
            System.out.println("Invalid amount for deposit.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds!");
            return false;
        }
        balance -= amount;
        return true;
    }

    abstract double calculateInterest();
}

class Ticket {
    private static int nextTicketId = 1;
    private int ticketId;
    private String destination;
    private double price;
    
    public Ticket(String destination, double price) {
        this.ticketId = nextTicketId++;
        this.destination = destination;
        this.price = price;
    }
    
    public int getTicketId() { return ticketId; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    
    @Override
    public String toString() {
        return "Ticket ID: " + ticketId + ", Destination: " + destination + ", Price: $" + price;
    }
}

class SavingsAccount extends BankAccount {
    private int withdrawals = 0;

    public SavingsAccount(double balance) {
        super(balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (withdrawals >= 3) {
            System.out.println("Withdrawal limit exceeded!");
            return false;
        }
        if (super.withdraw(amount)) {
            withdrawals++;
            System.out.println("Withdrawal successful. New Balance: $" + getBalance());
            return true;
        }
        return false;
    }

    @Override
    double calculateInterest() {
        double interest = getBalance() * 0.04;
        deposit(interest);
        return interest;
    }
}

class CurrentAccount extends BankAccount {
    public CurrentAccount(double balance) {
        super(balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (super.withdraw(amount)) {
            if (getBalance() < 500) {
                System.out.println("Below minimum balance. $50 penalty applied.");
                deposit(-50);
            }
            System.out.println("Withdrawal successful. New Balance: $" + getBalance());
            return true;
        }
        return false;
    }

    @Override
    double calculateInterest() {
        return 0;
    }
}

public class Bank {
    private static Scanner scanner = new Scanner(System.in);
    private static BankAccount account;
    private static List<Ticket> bookedTickets = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the Banking System!");
        createAccount();
        showMenu();
    }

    private static void createAccount() {
        while (true) {
            System.out.println("\nSelect Account Type:");
            System.out.println("1. Savings Account");
            System.out.println("2. Current Account");
            System.out.print(">> ");

            int choice = scanner.nextInt();

            System.out.print("Enter initial deposit" +
                    (choice == 1 ? " (minimum $1000)" : " (minimum $500)") +
                    ":\n>> ");
            double initialDeposit = scanner.nextDouble();

            if (choice == 1) {
                if (initialDeposit < 1000) {
                    System.out.println("Minimum deposit of $1000 required for Savings Account.");
                    continue;
                }
                account = new SavingsAccount(initialDeposit);
                break;
            } else if (choice == 2) {
                if (initialDeposit < 500) {
                    System.out.println("Minimum deposit of $500 required for Current Account.");
                    continue;
                }
                account = new CurrentAccount(initialDeposit);
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nMENU:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Apply Interest");
            System.out.println("5. Book Ticket");
            System.out.println("6. View Booked Tickets");
            System.out.println("7. Exit");
            System.out.print("Enter your choice:\n>> ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount:\n>> ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;

                case 2:
                    System.out.print("Enter withdrawal amount:\n>> ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;

                case 3:
                    System.out.println("Current Balance: $" + account.getBalance());
                    break;

                case 4:
                    if (account instanceof SavingsAccount) {
                        double interest = account.calculateInterest();
                        System.out.println("Interest applied: $" + interest);
                    } else {
                        System.out.println("Current Account does not earn interest.");
                    }
                    break;

                case 5:
                    bookTicket();
                    break;

                case 6:
                    viewBookedTickets();
                    break;

                case 7:
                    System.out.println("Thank you for using our Banking System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void bookTicket() {
        System.out.println("\n=== TICKET BOOKING ===");
        System.out.println("Available Destinations:");
        System.out.println("1. New York - $250");
        System.out.println("2. Los Angeles - $300");
        System.out.println("3. Chicago - $200");
        System.out.println("4. Miami - $280");
        System.out.println("5. Seattle - $320");
        System.out.print("Select destination (1-5):\n>> ");
        
        int choice = scanner.nextInt();
        String destination;
        double price;
        
        switch (choice) {
            case 1:
                destination = "New York";
                price = 250;
                break;
            case 2:
                destination = "Los Angeles";
                price = 300;
                break;
            case 3:
                destination = "Chicago";
                price = 200;
                break;
            case 4:
                destination = "Miami";
                price = 280;
                break;
            case 5:
                destination = "Seattle";
                price = 320;
                break;
            default:
                System.out.println("Invalid destination choice.");
                return;
        }
        
        System.out.println("Selected: " + destination + " - $" + price);
        System.out.print("Confirm booking? (y/n):\n>> ");
        scanner.nextLine(); // consume newline
        String confirm = scanner.nextLine().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            if (account.getBalance() >= price) {
                if (account.withdraw(price)) {
                    Ticket ticket = new Ticket(destination, price);
                    bookedTickets.add(ticket);
                    System.out.println("Ticket booked successfully!");
                    System.out.println(ticket);
                    System.out.println("Amount deducted: $" + price);
                    System.out.println("Remaining Balance: $" + account.getBalance());
                }
            } else {
                System.out.println("Insufficient funds! Your balance: $" + account.getBalance());
                System.out.println("Ticket price: $" + price);
            }
        } else {
            System.out.println("Ticket booking cancelled.");
        }
    }
    
    private static void viewBookedTickets() {
        System.out.println("\n=== YOUR BOOKED TICKETS ===");
        if (bookedTickets.isEmpty()) {
            System.out.println("No tickets booked yet.");
        } else {
            System.out.println("Total tickets booked: " + bookedTickets.size());
            for (Ticket ticket : bookedTickets) {
                System.out.println(ticket);
            }
        }
    }
}