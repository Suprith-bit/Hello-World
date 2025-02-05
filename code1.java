import java.util.Scanner;

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
            System.out.println("5. Exit");
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
                    System.out.println("Thank you for using our Banking System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}