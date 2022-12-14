package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.util.BasicLogger;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final int APPROVED = 2;
    private final int REJECTED = 3;
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private AuthenticatedUser currentUser;
    private Account currentUserAccount;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println((char) 27 + "[31m" + "Invalid Selection" + (char) 27 + "[0m");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        try {
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());

        } catch (NullPointerException e) {
            consoleService.printErrorMessage();
        }

    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println((char) 27 + "[31m" + "Invalid Selection" + (char) 27 + "[0m");
            }
            consoleService.pause();
        }
    }

    /**
     * Function prints a formatted balance of the current user to the screen.
     *
     * @return Balance of current user.
     */
    private BigDecimal viewCurrentBalance() {
        currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
        System.out.println("Current balance is: " + NumberFormat.getCurrencyInstance().format(currentUserAccount.getBalance()));
        return currentUserAccount.getBalance();
    }

    private void viewTransferHistory() {
        currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
        List<TransferDTO> currentUserTransferHistory = transferService.getTransferRecord((long) currentUserAccount.getAccountid());

        System.out.println("__________________________________________________");
        System.out.println("Transfers: ");
        System.out.printf("%-5s%-8s%-10s%-10s%-8s%s", "ID", "TYPE", "TO", "FROM", "AMOUNT", "STATUS\n");

        for (TransferDTO t : currentUserTransferHistory) {

            User from = accountService.getUserByAccountId(t.getAccountfrom());
            User to = accountService.getUserByAccountId(t.getAccountto());

            if (t.getTransferType().getTransfertypedesc().equals("Send")) {
                System.out.printf("%-5s%-8s%-10s%-10s%-8s(%s)\n", t.getId(), t.getTransferType().getTransfertypedesc(), to.getUsername(), from.getUsername(), t.getAmount(), t.getTransferStatus().getTransferstatusdesc());
            } else {
                System.out.printf("%-5s%-8s%-10s%-10s%-8s(%s)\n", t.getId(), t.getTransferType().getTransfertypedesc(), from.getUsername(), to.getUsername(), t.getAmount(), t.getTransferStatus().getTransferstatusdesc());
            }
        }
        System.out.println("__________________________________________________");
        int userInput = consoleService.promptForMenuSelection("Select a transaction id for more details, or [0] to exit: ");
        try {
            if (userInput != 0) {
                if (transferService.getTransferById(userInput).getId() == userInput) {
                    displaySingleTransfer(userInput);
                }
            } else {
                System.out.println("Returning to main menu.");
            }
        } catch (NullPointerException e) {
            BasicLogger.log(e.getMessage());
            System.err.println("No transfer id by that number");
        }
    }

    private List<Transfer> viewPendingRequests() {
        currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
        List<Transfer> pendingTransfers = transferService.pendingTransfers((long) currentUserAccount.getAccountid());
        List<Integer> transactionIds = new ArrayList<>();

        try {
            if (pendingTransfers.size() > 0) {

                for (Transfer transfer : pendingTransfers) {
                    transactionIds.add(transfer.getId());
                    User user = accountService.getUserByAccountId(transfer.getAccountto());
                    System.out.println("Transfer Id: " + "[" + transfer.getId() + "]: Account holder: " + user.getUsername() + " is requesting the amount of " + NumberFormat.getCurrencyInstance().format(transfer.getAmount()));
                }
                String message = approveOrReject(transactionIds);
                System.out.println(message);
            } else {
                System.out.println("You have no pending transactions at this time.");
            }
        } catch (NullPointerException e) {
            System.out.println((char) 27 + "[33m" + "Please choose a valid id." + (char) 27 + "[0m");
        }

        return pendingTransfers;
    }

    public String approveOrReject(List<Integer> transactionIds) {
        String success = "";
        try {
            int input = consoleService.promptForMenuSelection("Select transaction by id or enter 0 to exit: ");
            if (input != 0) {
                if (transactionIds.contains(input)) {
                    Transfer transfer = transferService.singlePendingTransfer((long) input);
                    System.out.println("Id: " + transfer.getId() + " $" + transfer.getAmount() + " " + transfer.getAccountto());
                    int choice = 0;
                    while (true) {
                        choice = consoleService.promptForMenuSelection("Approve [1] | Reject [2] | EXIT [0]: ");
                        if (choice == 1) {
                            currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
                            if (currentUserAccount.getBalance().compareTo(transfer.getAmount()) > 0) {
                                transfer.setTransferstatusid(APPROVED);
                                transferService.transferMoney(transfer.getTransfertypeid(), transfer.getAccountfrom(), transfer.getAccountto(), transfer.getAmount());
                                transferService.update(transfer);
                                success = "Transfer Approved";
                            } else {
                                success = (char) 27 + "[31m" + "Insufficient funds" + (char) 27 + "[0m";
                            }
                            break;
                        }
                        if (choice == 2) {
                            transfer.setTransferstatusid(REJECTED);
                            boolean response = transferService.update(transfer);
                            success = (char) 27 + "[31m" + "Transfer Rejected" + (char) 27 + "[0m";
                            break;
                        }
                        if (choice == 0) {
                            break;
                        }
                    }
                } else {
                    success = (char) 27 + "[31m" + "Invalid Transfer Id" + (char) 27 + "[0m";
                    throw new RuntimeException(success);
                }
            }
        } catch (RuntimeException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private void sendBucks() {
        //List of Available recipients
        List<User> userList = getAllUsers();
        try {
            int userSelection = consoleService.promptForInt("Select user to receive funds or [0] to exit: ");
            while (true) {
                if (userSelection == 0) {
                    break;
                }
                if (userSelection <= userList.size()) {
                    //Getting Id of Recipient
                    long sendMoneyToUser = userList.get(userSelection - 1).getId();
                    currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
                    int accountofTargetUser = accountService.getAccountIdByUserId((int) sendMoneyToUser);

                    System.out.println("Account balance: " + NumberFormat.getCurrencyInstance().format(currentUserAccount.getBalance()));
                    BigDecimal moneyToSend = consoleService.promptForBigDecimal("Enter Your Funds: ");

                    if (moneyToSend.compareTo(currentUserAccount.getBalance()) > 0) {
                        System.out.println((char) 27 + "[31m" + "Insufficient funds" + (char) 27 + "[0m");

                    } else if (moneyToSend.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println((char) 27 + "[33m" + "Must send a positive number more than $0.00" + (char) 27 + "[0m");
                    } else {
                        //Sends money from current user to selected user.
                        transferService.transferMoney(APPROVED, currentUserAccount.getAccountid(), accountofTargetUser, moneyToSend);
                        System.out.println("You sent: " + moneyToSend + " TE bucks to " + userList.get(userSelection - 1).getUsername());
                    }
                }
                break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println((char) 27 + "[33m" + "Please select a valid user" + (char) 27 + "[0m");
        }
    }

    private void requestBucks() {
        boolean isValid;

        do {
            isValid = true;
            List<User> userList = getAllUsers();
            try {
                int userSelection = consoleService.promptForInt("Select user to request TE bucks from or [0] to exit: ");
                if (userSelection == 0) {
                    break;
                }

                long sendMoneyToUser = userList.get(userSelection - 1).getId();
                int accountFromUser = accountService.getAccountIdByUserId((int) sendMoneyToUser);
                currentUserAccount = accountService.getAccount(currentUser.getUser().getId());

                BigDecimal amountToRequest = consoleService.promptForBigDecimal("Enter request amount: ");
                //Creating a transfer request from current user - to receive funds from selected user.
                if (amountToRequest.compareTo(BigDecimal.ZERO) > 0) {
                    transferService.requestMoney(amountToRequest, currentUserAccount.getAccountid(), accountFromUser);
                } else {
                    System.out.println((char) 27 + "[33m" + "Please enter a positive amount." + (char) 27 + "[0m");
                    isValid = false;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println((char) 27 + "[33m" + "Please select a valid user." + (char) 27 + "[0m");
                isValid = false;
            }

        } while (!isValid);

    }

    public List<User> getAllUsers() {
        List<User> userList = accountService.getListOfUsers();
        int counter = 0;
        for (User user : userList) {
            counter++;
            System.out.println(counter + " : " + user.getUsername());
        }
        return userList;
    }

    public void displaySingleTransfer(int id) {
        TransferDTO singleTransfer = transferService.getTransferById(id);
        User from = accountService.getUserByAccountId(singleTransfer.getAccountfrom());
        User to = accountService.getUserByAccountId(singleTransfer.getAccountto());
        System.out.println("Id: " + singleTransfer.getId());
        System.out.println("Status: " + singleTransfer.getTransferStatus().getTransferstatusdesc());
        System.out.println("Type: " + singleTransfer.getTransferType().getTransfertypedesc());
        System.out.println("From: " + from.getUsername());
        System.out.println("To: " + to.getUsername());
        System.out.println("Amount: $" + singleTransfer.getAmount());
    }
}
