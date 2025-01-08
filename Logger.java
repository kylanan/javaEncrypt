import java.util.Calendar;
import java.util.Scanner;
import java.io.*;

public class Logger {
    public static void main(String[] args) throws Exception {
        //System.out.println("hey");
        Scanner scnr = new Scanner(System.in);	    
        
        //get output file name and open in append mode
        System.out.print("Enter file name to write to: ");
        String fileName = scnr.next();

        FileOutputStream outStream = new FileOutputStream(fileName, true);      //open in append mode
        PrintWriter writer = new PrintWriter(outStream);

        //continue reading from stdin until "quit" is recieved
        String inputString = "";
        boolean quitRecieved = false;
        while (!quitRecieved) {
            inputString = scnr.next();
            if (inputString.equalsIgnoreCase("QUIT")) quitRecieved = true;      //quit

            Calendar currentTime = Calendar.getInstance();      //get time
            //YYYY-MM-DD HH:MM [ACTION] MESSAGE
            //if <10, a zero should be put in the front
            writer.print(currentTime.get(Calendar.YEAR) + "-");
            if (currentTime.get(Calendar.MONTH) + 1 < 10) writer.print("0");
            writer.print((currentTime.get(Calendar.MONTH) + 1) + "-");
            if (currentTime.get(Calendar.DAY_OF_MONTH) < 10) writer.print("0");
            writer.print(currentTime.get(Calendar.DAY_OF_MONTH) + " ");
            if (currentTime.get(Calendar.HOUR_OF_DAY) < 10) writer.print("0");
            writer.print(currentTime.get(Calendar.HOUR_OF_DAY) + ":");
            if (currentTime.get(Calendar.MINUTE) < 10) writer.print("0");
            writer.print(currentTime.get(Calendar.MINUTE) + " [");
            //ACTION
            writer.print(inputString + "]");
            //MESSAGE
            inputString = scnr.nextLine();
            writer.println(inputString);
        }

        scnr.close();
        //outStream.close();        //shouldn't close this
        writer.close();
    }
}
