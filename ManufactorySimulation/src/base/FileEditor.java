package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditor {
    private String filename;

    public FileEditor(String filename){
        this.filename = filename+".txt";
    }

    public boolean createNewFile(){
        boolean success = false;
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                success = true;
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return success;
    }

    public void writeToFile(String toWrite){
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter(filename, true));
            myWriter.write(toWrite);
            myWriter.newLine();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
