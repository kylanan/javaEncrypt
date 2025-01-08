import java.util.Scanner;
import java.lang.String;
//import java.io.*;

public class Encrypt {
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);

        String passkey = "";

        //continue reading from stdin until "quit" is recieved
        String inputCommand = "";
        String inputArgument = "";
        boolean quitRecieved = false;

        while (!quitRecieved) {
            //System.out.print(">");
            inputCommand = scnr.next();     //first word is the command
            inputCommand = inputCommand.toUpperCase();
            inputArgument = scnr.nextLine();       //rest of the line is the argument
            if (!inputArgument.isEmpty()) {
                inputArgument = inputArgument.substring(1);     //cut out space
                inputArgument = inputArgument.toUpperCase();
            }
            switch(inputCommand) {
                case "PASSKEY":
                    //change passkey to the user's argument
                    passkey = inputArgument;
                    System.out.println("RESULT Successful.");
                break;

                case "ENCRYPT":
                    EncryptAlgorithm(passkey, inputArgument, true);
                break;

                case "DECRYPT":
                    EncryptAlgorithm(passkey, inputArgument, false);
                break;

                case "QUIT":
                    quitRecieved = true;
                break;

                default:
                    System.out.println("ERROR Invalid command.");
                break;
            }
        }
        scnr.close();
    }
    static void EncryptAlgorithm (String passkey, String inputArgument, boolean encryptMode) {
        int passkeyIndex = -1;
        if (passkey.isEmpty()) System.out.println("ERROR Passkey not set.");
        else {
            System.out.print("RESULT ");
            for(int i = 0; i < inputArgument.length(); i++) {
                if (inputArgument.charAt(i) != ' ') {
                    passkeyIndex++;
                    if (passkeyIndex == passkey.length()) passkeyIndex = 0;

                    int asciiVal;   //'A' is 65 in ASCII
                    if (encryptMode) {
                        //encrypt
                        asciiVal = ((inputArgument.charAt(i)) + (passkey.charAt(passkeyIndex) - 'A'));
                    }
                    else {
                        //decrypt
                        asciiVal = ((inputArgument.charAt(i)) - (passkey.charAt(passkeyIndex) - 'A'));
                    }

                    //wrap around
                    if (asciiVal > 'Z') asciiVal -= 26;
                    if (asciiVal < 'A') asciiVal += 26;
                    char currentChar = (char)asciiVal;
                    System.out.print(currentChar);
                }
                else System.out.print(' ');
            }
            System.out.println("");
        }
        return;
    }
}
