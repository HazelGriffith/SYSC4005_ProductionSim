package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * The FileEditor class facilitates easy creation and writing to new .txt files
 *
 */
public class FileEditor {
    private String filename;

    /**
     * constructor function of FileEditor objects with the given filename
     * @param filename
     */
    public FileEditor(String filename){
        this.filename = filename+".txt";
    }

    /**
     * createNewFile is the function that creates a new file
     * @return true if successful, false otherwise
     */
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

    /**
     * writeToFile is the function writes a String to the file
     * @param String toWrite
     */
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
