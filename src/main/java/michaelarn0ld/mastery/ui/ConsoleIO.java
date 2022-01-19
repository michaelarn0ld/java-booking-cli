package michaelarn0ld.mastery.ui;

import michaelarn0ld.mastery.models.State;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleIO {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Specifies behavior for System.out.println() in the View
     */
    public void println(Object value) {
        System.out.println(value);
    }

    /**
     * Specifies behavior for System.out.print() in the View
     */
    public void print(Object value) {
        System.out.print(value);
    }

    /**
     * Specifies behavior for System.out.printf() in the View
     */
    public void printf (String format, Object... values) {
        System.out.printf(format, values);
    }

    /**
     * Return a String based on a defined prompt; remove leading and trailing whitespace
     *
     * @param prompt - tell the user the type of information to input
     * @return - a String matching the user input
     */
    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (result.isBlank()) {
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            try {
                return LocalDate.parse(result, formatter);
            } catch (DateTimeParseException ex) {
                printf("[ERR]\nvalue must be a date (MM/dd/yyyy)\n");
            }
        }
    }

    /**
     * Return a State based on a defined prompt; return null if the user gives an empty input.
     * Indicate valid inputs upon invalid user input.
     *
     * @param prompt - tell the user to enter a string matching one a State
     * @return a possible State, or null if the user does not enter input
     */
    public State readState(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (result.isBlank()) {
                return null;
            }
            for (State s : State.values()) {
                if (s.name().equals(result.toUpperCase())) {
                    return State.valueOf(result.toUpperCase());
                }
            }
            printf("[ERR]\nvalue must be a State\n");
        }
    }

    /**
     * Return a boolean based on a defined prompt; can also return null if the user gives an empty input. Indicate valid
     * inputs to the user upon an invalid input
     *
     * @param prompt - tell the user what kind of boolean to input
     * @return a possible boolean value, or null if the user does not enter input
     */
    public boolean readBoolean(String prompt) {
        while (true) {
            String result = readString(prompt).toLowerCase();
            if (result.equals("y") || result.equals("n")) {
                return result.equalsIgnoreCase("y");
            }
            printf("[ERR]\nvalue must be [y/n]\n");
        }
    }

    /**
     * Return an int based on a defined prompt; can also return null if the user omits input. Indicates valid inputs
     * upon invalid user input
     *
     * @param prompt - tell the user to enter a number that can be parsed into an integer
     * @return a possible int if the user enters parsable valid input; return null if input is empty.
     */
    public Integer readInt(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (result.isBlank()) {
                return null;
            }
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException ex) {
                printf("[ERR]\n'%s' is not a valid number\n", result);
            }
        }
    }

    /**
     * Return an int inclusively between a min and a max boundary
     *
     * @param prompt - tell the user to enter a number that can be parsed into an integer
     * @param min - the parsed number must be greater than or equal to min
     * @param max - the parsed number must be less than or equal to max
     * @return - an int between min and max if the input is not empty, if it is empty return null
     */
    public Integer readInt(String prompt, int min, int max) {
        while (true) {
            Integer result = readInt(prompt);
            if (result == null) {
                return null;
            } else if (result >= min && result <= max) {
                return result;
            }
            printf("[ERR]\nvalue must be between %d and %d\n", min, max);
        }
    }
}
