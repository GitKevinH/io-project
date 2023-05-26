package org.ctac.java103;


/*Design a simple file manager application that allows users to perform the following tasks:

        1. Display the contents of a specified directory, including file names, file sizes, and last modified dates
        2. Copy, move, and delete files within the specified directory
        3. Create and delete directories within the specified directory
        4. Search for files within the specified directory based on file name or extension
        5. Implement a user interface (CLI or GUI) to allow users to select options and display the results
        6. Implement proper exception handling and logging
        7. Organize the project using best practices, such as proper code organization, modularity, and documentation*/

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            startManager();
        } catch (InputMismatchException ime){
            System.out.println("You've got to enter a number! Start over!");
        }
    }

    // main base of the code, this initiates all over methods based on user input.
    public static void startManager() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Kevin's file manager!");
        System.out.println("First, what directory do you want to point to? Enter in the path below (be careful with incorrect filepaths i.e no whitespaces or illegal chars)");
        String directory = scanner.nextLine();
        while(!checkDir(directory)){
            System.out.println("Please try again, what directory do you want to point to?");
            directory = scanner.nextLine();
        }
        Path directPath = Path.of(directory);
        //getNames(directory);



        while (true && !directory.equals("")) {

            System.out.println("What would you like to do?");
            System.out.println("1. See details and contents of your directory.");
            System.out.println("2. Copy a file");
            System.out.println("3. Move a file");
            System.out.println("4. Delete a file");
            System.out.println("5. Create a directory");
            System.out.println("6. Delete a directory");
            System.out.println("7. Search for a file in the directory");
            System.out.println("8. Exit");

            int input = scanner.nextInt();

            switch (input) {
                case 1:
                    getContents(directory);
                    break;
                case 2:
                    getNames(directory);
                    copyFile(directPath);
                    break;
                case 3:
                    getNames(directory);
                    moveFile(directPath);
                    break;
                case 4:
                    getNames(directory);
                    deleteFile(directPath);
                    break;
                case 5:
                    getNames(directory);
                    createDir(directPath);
                    break;
                case 6:
                    getNames(directory);
                    deleteFile(directPath);
                    break;
                case 7:
                    getNames(directory);
                    searchFile(directPath);
                    break;

                case 8:
                    directory = "";
                    break;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }


        }

        System.out.println("bye-bye!");

    }


    //This method calls a directory stream from the filePath string, and loops through each file in the directory.
    // Then it prints out the required file attributes for each file by calling the printFileAttribute method.
    public static void getContents(String filePath){
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(filePath))) {
            for (Path path : directoryStream) {
                printFileAttributes(path);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to display names (helps user keep track of whats in the current directory)
    public static void getNames(String filePath){
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(filePath))) {
            System.out.println("Files in directory: ");
            for (Path path : directoryStream) {
                printFileName(path);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFileName(Path file) {

            System.out.println(file.getFileName());

    }


    //Prints out file attributes from passed Path parameter
    public static void printFileAttributes(Path file) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.println("File: " + file.getFileName());
            System.out.println("Size: " + attributes.size());
            System.out.println("Last Modified Date/Time: " + attributes.lastModifiedTime());
            System.out.println("----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Copy file method
    public static void copyFile(Path directory){
        System.out.println("What file do you want to copy? Enter the file name.");

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        Path newFilePath = directory.resolve(fileName);

        if (checkFile(newFilePath.toString())) {
            System.out.println("Where do you want to save it to?");
            Path saveTo = Path.of(scanner.nextLine());
            if (checkDir(saveTo.toString())){
                try {
                    saveTo = saveTo.resolve(fileName);
                    Files.copy(newFilePath, saveTo, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied file!");
                } catch (IOException e) {
                    System.out.println("File not copied. Something went wrong or it may already exist!");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Invalid file! Try again.");
        }

    }

    //Move file method
    public static void moveFile(Path directory){
        System.out.println("What file do you want to move? Enter the file name.");

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        Path newFilePath = directory.resolve(fileName);

        if (checkFile(newFilePath.toString())) {
            System.out.println("Where do you want to move it to?");
            Path saveTo = Path.of(scanner.nextLine());
            if (checkDir(saveTo.toString())){
                try {
                    saveTo = saveTo.resolve(fileName);
                    Files.move(newFilePath, saveTo, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved file!");
                } catch (IOException e) {
                    System.out.println("File not moved. Something went wrong!");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Invalid file! Try again.");
        }

    }

    //Delete file method
    public static void deleteFile(Path directory){
        System.out.println("What file do you want to delete? Enter the file name.");

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        Path newFilePath = directory.resolve(fileName);

        if (checkFile(newFilePath.toString())) {
            try {
                Files.delete(newFilePath);
                System.out.println("File or Directory deleted!");
            } catch (IOException e) {
                System.out.println("File or Directory not deleted. This may be because the path was not entered in correctly, or there are contents inside the directory.");
                //e.printStackTrace();
            }

        } else {
            System.out.println("Invalid file! Try again.");
        }

    }

    //Method to create dir
    public static void createDir(Path directory){
        System.out.println("Enter the name of the directory you want to create.");

        Scanner scanner = new Scanner(System.in);
        String directoryName = scanner.nextLine();


        Path directoryPath = directory.resolve(directoryName);

        try {
            Files.createDirectories(directoryPath);
            System.out.println("Created directory.");
        } catch (IOException e) {
            System.out.println("Directory not created.");
            //e.printStackTrace();
        }
    }


    //This method calls a directory stream from the filePath string, and loops through each file in the directory.
    // Then it checks to see if the file name contains the string.
    public static void searchFile(Path directory){
        System.out.println("Enter the file name or extension you want to search.");

        Scanner scanner = new Scanner(System.in);
        boolean found = false;
        String strSearch = scanner.nextLine();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path path : directoryStream) {
                String filename = path.getFileName().toString();
                if (filename.contains(strSearch)) {
                    System.out.println("Files: " + filename);
                    found = true;
                }
            }
            if (!found){
                System.out.println("Nothing matched your search!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to check if directory exists
    public static boolean checkDir(String directory) {
        Path path = Paths.get(directory);

        if (Files.isDirectory(path)) {
            System.out.println("Great!");
            return true;
        } else {
            System.out.println("Directory does not exist!");
            return false;
        }
    }

    //Method to check if file exists
    public static boolean checkFile(String fileName) {
        Path path = Paths.get(fileName);

        if (Files.exists(path)) {
            System.out.println("Great!");
            return true;
        } else {
            System.out.println("File does not exist!");
            return false;
        }
    }
}


