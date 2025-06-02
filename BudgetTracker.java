import java.util.*;
import java.io.*;

class Transaction {
    String type; // income or expense
    String category;
    double amount;
    String date;

    public Transaction(String type, String category, double amount, String date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String toString() {
        return type + "," + category + "," + amount + "," + date;
    }

    public static Transaction fromString(String line) {
        String[] parts = line.split(",");
        return new Transaction(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
    }
}

public class BudgetTracker {
    static final String FILE_NAME = "transactions.txt";
    static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        loadTransactions();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Personal Budget Tracker ===");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. View Summary");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addTransaction(scanner, "Income");
                case 2 -> addTransaction(scanner, "Expense");
                case 3 -> viewTransactions();
                case 4 -> viewSummary();
                case 5 -> {
                    saveTransactions();
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void addTransaction(Scanner scanner, String type) {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Transaction t = new Transaction(type, category, amount, date);
        transactions.add(t);
        System.out.println(type + " added.");
    }

    static void viewTransactions() {
        System.out.println("\n=== All Transactions ===");
        for (Transaction t : transactions) {
            System.out.println(t.type + " | " + t.category + " | " + t.amount + " | " + t.date);
        }
    }

    static void viewSummary() {
        double income = 0, expense = 0;
        for (Transaction t : transactions) {
            if (t.type.equals("Income")) income += t.amount;
            else expense += t.amount;
        }
        System.out.println("\n=== Summary ===");
        System.out.println("Total Income: Rs." + income);
        System.out.println("Total Expense: Rs." + expense);
        System.out.println("Balance: Rs." + (income - expense));
    }

    static void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                transactions.add(Transaction.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No existing data found. Starting fresh.");
        }
    }

    static void saveTransactions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}

