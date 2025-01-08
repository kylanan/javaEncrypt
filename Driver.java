import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Driver {
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);	    
        
        //get output file name and open in append mode
        System.out.print("Enter name of log file: ");
        String fileName = scnr.nextLine();

        try
		{
            //set up logger
            Process loggerProcess = Runtime.getRuntime().exec("java Logger.java");
            OutputStream outLoggerStream = loggerProcess.getOutputStream();
            PrintStream toLogger = new PrintStream(outLoggerStream);

            toLogger.println(fileName);
            toLogger.flush();

            toLogger.println("START Logging started.");
            toLogger.flush();

            try
            {
                //set up ecryption program
                Process encryptProcess = Runtime.getRuntime().exec("java Encrypt.java");
                InputStream inEncryptStream = encryptProcess.getInputStream();
                OutputStream outEncryptStream = encryptProcess.getOutputStream();
                Scanner fromEncrypt = new Scanner(inEncryptStream);
                PrintStream toEncrypt = new PrintStream(outEncryptStream);

                int userInput = 0;
                String userString = "";
                String resultString = "";

                //create history
                ArrayList<String> history = new ArrayList<>();

                while(userInput != 5) {
                    System.out.print("[1]Password, [2]Encrypt, [3]Decrypt, [4]History, [5]Quit\n>");
                    userInput = scnr.nextInt();     //should error check
                    //toLogger.println(userInput);      //write to logger

                    switch(userInput) {
                        case 1:
                            //password
                            do {
                                System.out.print("[1]Enter new password or [2]enter from history?\n>");
                                userInput = scnr.nextInt();     //should error check
                                if (userInput == 1) {
                                    userString = GetFromUser(scnr);
                                }
                                else if (userInput == 2) {
                                    userString = GetFromHistory(scnr, history, userInput);
                                }
                            } while(userString.isEmpty());
                            toLogger.println("PASSWORD Password changed.");
                            toEncrypt.println("PASSKEY " + userString);
                            toEncrypt.flush();
                            System.out.println(fromEncrypt.nextLine());
                            //log result
                            toLogger.println("RESULT Password change successful.");
                        break;

                        case 2:
                            //encrypt
                            do {
                                System.out.print("[1]Enter new string or [2]enter from history?\n>");
                                userInput = scnr.nextInt();     //should error check
                                if (userInput == 1) {
                                    userString = GetFromUser(scnr);
                                }
                                else if (userInput == 2) {
                                    userString = GetFromHistory(scnr, history, userInput);
                                }
                            } while(userString.isEmpty());
                            //put into history
                            history.add(userString);
                            //pass to logger
                            toLogger.println("ENCRYPT Input: " + userString);
                            toEncrypt.println("ENCRYPT " + userString);
                            toEncrypt.flush();
                            
                            resultString = fromEncrypt.nextLine();
                            history.add(resultString.substring(7));
                            System.out.println(resultString);
                            //log result
                            toLogger.println(resultString);
                        break;

                        case 3:
                            //decrypt
                            do {
                                System.out.print("[1]Enter new string or [2]enter from history?\n>");
                                userInput = scnr.nextInt();     //should error check
                                if (userInput == 1) {
                                    userString = GetFromUser(scnr);
                                }
                                else if (userInput == 2) {
                                    userString = GetFromHistory(scnr, history, userInput);
                                }
                            } while (userString.isEmpty());
                            //put into history
                            history.add(userString);
                            //pass to logger
                            toLogger.println("DECRYPT Input: " + userString);
                            toEncrypt.println("DECRYPT " + userString);
                            toEncrypt.flush();

                            resultString = fromEncrypt.nextLine();
                            history.add(resultString.substring(7));
                            System.out.println(resultString);
                            //log result
                            toLogger.println(resultString);
                        break;

                        case 4:
                            //history
                            for(int i = 0; i < history.size(); i++) {
                                System.out.println((i+1) + ": " + history.get(i));
                            }
                            toLogger.println("HISTORY History accessed.");
                        break;

                        case 5:
                            //quit
                        break;

                        default:
                            //error
                            System.out.println("Invalid command.");
                            //log result
                            toLogger.println("ERROR Command error.");
                        break;
                    }
                }
                toEncrypt.close();
                fromEncrypt.close();
                encryptProcess.waitFor();
            }
            catch(IOException ex)
            {
                System.out.println("Unable to run Encryption.");
            }

            //close the logger
            toLogger.println("QUIT Logging ended.");
            toLogger.flush();
            loggerProcess.waitFor();
            outLoggerStream.close();
            toLogger.close();
		}
		catch(IOException ex)
		{
			System.out.println("Unable to run Logger.");
		}
		catch(InterruptedException ex)
		{
			System.out.println("Unexpected termination of Logger.");
		}

        scnr.close();
    }

    static String GetFromUser(Scanner scnr) {
        scnr.nextLine();
        System.out.print("Enter string: ");
        String userString = scnr.nextLine();
        return userString.toUpperCase();
    }

    static String GetFromHistory(Scanner scnr, ArrayList<String> history, int userInput) {
        scnr.nextLine();
        System.out.println("[0] Exit history and enter new string.");
        for(int i = 0; i < history.size(); i++) {
            System.out.println("[" + (i+1) + "] " + history.get(i));
        }
        System.out.println("Choose an index, or 0 to go back: ");
        userInput = scnr.nextInt();
        if (userInput > 0) return history.get(userInput - 1);
        else return "";
    }
}
