package presentation;

import java.util.Scanner;

public class Console {
    private final Scanner scanner;

    public Console() {
        this.scanner = new Scanner(System.in);
    }

    public void spacing() {
        System.out.println();
    }

    public void showMessage(String message, boolean newLine) {
        if (newLine) {
            System.out.println(message);
        } else {
            System.out.print(message);
        }
    }

    public void showError(String error, boolean newLine) {
        this.showMessage("ERROR: " + error, newLine);
    }

    private String getText() {
        return this.scanner.nextLine();
    }

    private int getInteger() {
        try {
            return Integer.parseInt(this.getText());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public int askForInteger(String message, int min, int max) {
        int inputInteger;

        while (true) {
            this.showMessage(message, false);

            inputInteger = this.getInteger();
            this.spacing();

            if (inputInteger >= min && inputInteger <= max && !String.valueOf(inputInteger).isEmpty()) {
                return inputInteger;
            } else {
                this.showError("Invalid number.", true);
            }
        }
    }

    public String askDatasetSize() {
        System.out.println("What dataset size do you want to use?");
        System.out.println("\t1 - L");
        System.out.println("\t2 - M");
        System.out.println("\t3 - S");
        System.out.println("\t4 - XS");
        this.spacing();


        int option = askForInteger("Enter an option: ", 1, 4);

        switch (option) {
            case 1 -> {
                return "L";
            }
            case 2 -> {
                return "M";
            }
            case 3 -> {
                return "S";
            }
            case 4 -> {
                return "XS";
            }
            default -> {
                return null;
            }
        }
    }

    public int askForProblem() {
        System.out.println("Choose a problem to solve:");
        System.out.println("\t1 - High Speed Navigation");
        System.out.println("\t2 - Complete Fleet");
        System.out.println("\t3 - Exit");
        this.spacing();

        return askForInteger("Enter an option: ", 1, 3);
    }

    public int askForMethod() {
        System.out.println("Choose a method to solve the selected problem:");
        System.out.println("\t1 - Backtracking");
        System.out.println("\t2 - Branch & Bound");
        System.out.println("\t3 - Back");
        this.spacing();

        return askForInteger("Enter an option: ", 1, 3);
    }

    public int askForImprovement() {
        System.out.println("Choose a method to solve the selected problem:");
        System.out.println("\t1 - No improvements");
        System.out.println("\t2 - With improvements");
        System.out.println("\t3 - Back");
        this.spacing();

        return askForInteger("Enter an option: ", 1, 3);
    }

    public void showChronometer(long millis) {
        long milliseconds = (millis % 1000) ;
        long seconds =  ( millis / 1000 ) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) ((millis / (1000 * 60 * 60)) % 24);

        this.showMessage("Time: " + hours + "h " + minutes + "min " + seconds + "s "+  milliseconds + "ms" , true);
        this.spacing();
    }
}