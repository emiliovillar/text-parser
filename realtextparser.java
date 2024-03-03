import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class realtextparser {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please copy paste the file path of your text file for analysis :)");

        String filePath = scanner.nextLine();

        String finishedFile = fileToString(filePath);

        String[] sentencesArray = finishedFile.split("\n");

        String[] wordArray = finishedFile.replaceAll("\n", "").split(" ");

        // store the answers
        String[] answers1And2 = highestWordCount(wordArray);
        String answers3 = wordsInASentence(sentencesArray);
        String[] answers456789 = sentenceSearch(sentencesArray);

        // now they can be put into the answers array
        String[] answers = new String[]{
                answers1And2[0],
                answers1And2[1],
                answers3,
                answers456789[0],
                answers456789[1],
                answers456789[2],
                answers456789[3],
                answers456789[4],
                answers456789[5]
        };
      // directory to save output files
        System.out.println("Please copy paste the file path of your text file for analysis :)");

        System.out.println("Please copy paste the file path of your output :)");
        String outputDirectory = scanner.nextLine();

        //  write to output files d
        for (int i = 0; i < answers.length; i++) {
            String outputFilePath = outputDirectory + "assignment1_" + (i + 1) + ".txt";
            File outputFile = new File(outputFilePath);
            outputFile.createNewFile(); // Create the output file

            try (PrintWriter writer = new PrintWriter(outputFile)) {
                writer.println(answers[i]); // write the answer to the file
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the output file: " + e.getMessage());
            }
        }

        scanner.close(); // Close the scanner

    }


// instead of trying to 'fix' the text file as we process it, just make a method to do it automatically
    public static String fixString(String string) {
        string = string.toLowerCase();
        string = string.replaceAll("no\\.", "no");
        string = string.replaceAll("mr\\.", "mr");
        string = string.replaceAll("dr\\.", "dr");
        return string;
    }

    public static String fileToString(String filepath) throws IOException {
        StringBuilder sentenceString = new StringBuilder();
        File file = new File(filepath); // reads from file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { //store file here
            String nextLine;
            while ((nextLine = reader.readLine()) != null) { //until we finish
                nextLine = fixString(nextLine);
                while (nextLine.contains(".")) {
                    int period = nextLine.indexOf(".");
                    String holder = nextLine.substring(0, period + 1).trim(); //store sentence
                    sentenceString.append(holder).append(" \n ");
                    nextLine = nextLine.substring(period + 1).trim();//cut newly added part from line
                }
                sentenceString.append(nextLine).append(" \n "); //add on whatever is left
            }
            return sentenceString.toString();
        }
    }

    public static String[] highestWordCount(String[] words) {
        int frequency1 = 0; //highest word count
        int frequency2 = 0; //second highest word count
        int frequency3 = 0; // third highest word count

        ArrayList<String> frequencyWords1 = new ArrayList<>(); //list of words that appear most
        ArrayList<String> frequencyWords2 = new ArrayList<>(); //second
        ArrayList<String> frequencyWords3 = new ArrayList<>(); //third

        for (int i = 0; i < words.length; i++) {
            String currentWord = words[i];
            int currentWordCount = 1;

            if (!currentWord.equals("")) { // if we already counted the word
                for (int j = i + 1; j < words.length; j++) {
                    if (currentWord.equals(words[j])) {
                        currentWordCount++;
                        words[j] = "";
                    }
                }

                if (currentWordCount > frequency1) { //repeat update process
                    frequency3 = frequency2;
                    frequency2 = frequency1;
                    frequency1 = currentWordCount;

                    frequencyWords3.clear();
                    frequencyWords3.addAll(frequencyWords2);

                    frequencyWords2.clear();
                    frequencyWords2.addAll(frequencyWords1);

                    frequencyWords1.clear();
                    frequencyWords1.add(currentWord);
                } else if (currentWordCount > frequency2 && currentWordCount != frequency1) {
                    frequency3 = frequency2;
                    frequency2 = currentWordCount;

                    frequencyWords3.clear();
                    frequencyWords3.addAll(frequencyWords2);

                    frequencyWords2.clear();
                    frequencyWords2.add(currentWord);
                } else if (currentWordCount > frequency3 && currentWordCount != frequency1 && currentWordCount != frequency2) {
                    frequency3 = currentWordCount;

                    frequencyWords3.clear();
                    frequencyWords3.add(currentWord);
                }
            }
        }
        // format and store answers
        StringBuilder answer1 = new StringBuilder();
        StringBuilder answer2 = new StringBuilder();

        for (String word : frequencyWords1) {
            answer1.append(word).append(":").append(frequency1).append("\n");
        }

        for (String word : frequencyWords3) {
            answer2.append(word).append(":").append(frequency3).append("\n");
        }

        String[] output = new String[]{answer1.toString(), answer2.toString()};

        return output;
    }

    public static String wordsInASentence(String[] sentenceArray) {
        int highestFrequency = 0;
        StringBuilder sentences = new StringBuilder();

        for (int i = 0; i < sentenceArray.length; i++) {
            String sentence = sentenceArray[i].trim();
            String[] words = sentence.split(" ");

            Arrays.sort(words);

            String currentWord = "";
            int currentWordCount = 0;

            for (int j = 0; j < words.length; j++) {
                if (words[j].equals(currentWord)) {
                    currentWordCount++;
                } else {
                    currentWordCount = 1;
                    currentWord = words[j];
                }

                if (currentWordCount >= highestFrequency) {
                    if (currentWordCount > highestFrequency) {
                        highestFrequency = currentWordCount;
                        sentences = new StringBuilder();
                    }
                    sentences.append(currentWord).append(":").append(currentWordCount).append(":").append(sentence).append("\n");
                }
            }
        }

        return sentences.toString();
    }

    public static String[] sentenceSearch(String[] sentenceArray) {
        //  arrays to store counts and sentences for each word or phrase
        int[] highestCounts = new int[6]; // array to store the highest counts
        StringBuilder[] highestCountSentences = new StringBuilder[6]; // array to store sentences with highest counts
        String[] keywords = {" the ", " of ", " was ", "but the", "it was", "in my"}; // array of keywords to search for


        for (int i = 0; i < sentenceArray.length; i++) { // loop through each sentence
            for (int j = 0; j < 6; j++) { // loop through each keyword
                int count = sentenceArray[i].split(keywords[j]).length - 1; //
                highestCounts[j] = Math.max(highestCounts[j], count); // update highest count
            }
        }

        // collect sentences matching the highest counts for each word or phrase
        for (int i = 0; i < sentenceArray.length; i++) { // loop through each sentence
            for (int j = 0; j < 6; j++) { //
                int count = sentenceArray[i].split(keywords[j]).length - 1; // count occurrences of keyword in sentence
                if (count == highestCounts[j]) { // if count matches highest count
                    if (highestCountSentences[j] == null) { // if StringBuilder not initialized
                        highestCountSentences[j] = new StringBuilder();
                    }
                    highestCountSentences[j].append(keywords[j] + ":" + count + ":" + sentenceArray[i] + "\n"); // add sentence to StringBuilder
                }
            }
        }

        // store everything in an array
        String[] output = new String[6]; // array to store output sentences
        for (int i = 0; i < 6; i++) { // loop through each keyword
            if (highestCountSentences[i] != null) { // if sentences found for keyword
                output[i] = highestCountSentences[i].toString(); // convert StringBuilder to String and store
            } else {
                output[i] = ""; // if no sentences found for keyword
            }
        }


        return output; // return array of sentences with highest counts for each keyword
    }

    }

