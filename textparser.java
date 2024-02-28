import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class textparser {

    public static void main(String[] args) {
        String filePath = "/Users/evill052/Downloads/data/tiny1.txt";
        try {
            // READ THE TEXT FILE AND PROCESS ITS CONTENT
            String[] words = processTextFile(filePath);

            // FIND THE MOST FREQUENT WORDS IN THE ENTIRE TEXT
            String[][] mostFrequentWords = findMostFrequentWords(words);
            System.out.println("Most frequent word: " + mostFrequentWords[0][0] + ", Count: " + mostFrequentWords[0][1]);
            System.out.println("Third most frequent word: " + mostFrequentWords[2][0] + ", Count: " + mostFrequentWords[2][1]);

            // FIND THE WORD WITH THE HIGHEST FREQUENCY IN EACH SENTENCE
            findSentenceWithMostRepetitions(filePath);

            // LIST SENTENCES WITH MAXIMUM NO. OF OCCURRENCES OF SPECIFIC WORDS OR PHRASES
            findSentenceWithMostWordOccurrences(filePath, "the");
            findSentenceWithMostWordOccurrences(filePath, "of");
            findSentenceWithMostWordOccurrences(filePath, "was");
            findSentenceWithMostPhraseOccurrences(filePath, "but the");
            findSentenceWithMostPhraseOccurrences(filePath, "it was");
            findSentenceWithMostPhraseOccurrences(filePath, "in my");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // METHOD TO READ THE TEXT FILE AND PROCESS ITS CONTENT
    public static String[] processTextFile(String filePath) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // READ EACH LINE FROM THE FILE
            while ((line = reader.readLine()) != null) {
                // APPEND THE LINE CONTENT TO THE TEXT BUILDER
                textBuilder.append(line).append(" ");
            }
        }
        // CONVERT THE ENTIRE TEXT TO LOWERCASE AND SPLIT IT INTO WORDS
        String text = textBuilder.toString().toLowerCase();
        return text.split("\\s+");
    }

    // METHOD TO FIND THE MOST FREQUENT WORDS IN THE ENTIRE TEXT
    public static String[][] findMostFrequentWords(String[] words) {
        String mostCommonWord = null;
        String secondMostCommonWord = null;
        String thirdMostCommonWord = null;
        int maxCount = 0;
        int secondMaxCount = 0;
        int thirdMaxCount = 0;

        // ITERATE OVER EACH WORD IN THE TEXT
        for (int i = 0; i < words.length; i++) {
            int count = 1; // START COUNTING FROM 1 FOR THE CURRENT WORD
            if (words[i] == null) continue; // SKIP NULL WORDS
            // ITERATE OVER THE REMAINING WORDS TO COUNT OCCURRENCES OF THE CURRENT WORD
            for (int j = i + 1; j < words.length; j++) {
                if (words[j] != null && words[i].equals(words[j])) {
                    count++;
                    words[j] = null; // MARK THE WORD AS VISITED TO AVOID COUNTING IT AGAIN
                }
            }
            // UPDATE THE MOST COMMON WORDS BASED ON COUNTS
            if (count > maxCount) {
                thirdMostCommonWord = secondMostCommonWord;
                thirdMaxCount = secondMaxCount;
                secondMostCommonWord = mostCommonWord;
                secondMaxCount = maxCount;
                mostCommonWord = words[i];
                maxCount = count;
            } else if (count > secondMaxCount && !words[i].equals(mostCommonWord)) {
                thirdMostCommonWord = secondMostCommonWord;
                thirdMaxCount = secondMaxCount;
                secondMostCommonWord = words[i];
                secondMaxCount = count;
            } else if (count > thirdMaxCount && !words[i].equals(mostCommonWord) && !words[i].equals(secondMostCommonWord)) {
                thirdMostCommonWord = words[i];
                thirdMaxCount = count;
            }
        }

        // STORE THE MOST FREQUENT WORDS AND THEIR COUNTS IN A 2D ARRAY
        return new String[][]{
                {mostCommonWord, String.valueOf(maxCount)},
                {secondMostCommonWord, String.valueOf(secondMaxCount)},
                {thirdMostCommonWord, String.valueOf(thirdMaxCount)}
        };
    }

    // METHOD TO FIND THE SENTENCE WITH THE MOST REPETITIONS OF A WORD
    public static void findSentenceWithMostRepetitions(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String wordWithMostRepetitions = "";
            int maxRepetitions = 0;
            String sentenceWithMostRepetitions = "";

            while ((line = reader.readLine()) != null) {
                // SPLIT THE SENTENCE INTO WORDS
                String[] wordsInSentence = line.toLowerCase().split("\\s+");
                for (int i = 0; i < wordsInSentence.length; i++) {
                    int count = 1;
                    // COUNT REPETITIONS OF THE CURRENT WORD
                    for (int j = i + 1; j < wordsInSentence.length; j++) {
                        if (wordsInSentence[i].equals(wordsInSentence[j])) {
                            count++;
                        }
                    }
                    // IF CURRENT WORD HAS MORE REPETITIONS, UPDATE MAXIMUM REPETITIONS AND STORE THE SENTENCE
                    if (count > maxRepetitions) {
                        maxRepetitions = count;
                        sentenceWithMostRepetitions = line;
                        wordWithMostRepetitions = wordsInSentence[i];
                    }
                }
            }

            // PRINT THE SENTENCE WITH THE MOST REPETITIONS OF A WORD
            System.out.println(maxRepetitions + "|" + wordWithMostRepetitions + "|" + sentenceWithMostRepetitions + "|"  );
        }
    }

    // METHOD TO FIND SENTENCE(S) WITH THE MAXIMUM NO. OF OCCURRENCES OF A SPECIFIC WORD IN THE ENTIRE FILE
    public static void findSentenceWithMostWordOccurrences(String filePath, String word) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int maxOccurrences = 0;
            List<String> sentencesWithMaxOccurrences = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // SPLIT THE SENTENCE INTO WORDS
                String[] wordsInSentence = line.toLowerCase().split("\\s+");
                int wordCount = 0;
                // COUNT OCCURRENCES OF THE SPECIFIC WORD IN THE SENTENCE
                for (String w : wordsInSentence) {
                    if (w.equals(word.toLowerCase())) {
                        wordCount++;
                    }
                }
                // IF CURRENT SENTENCE HAS MORE OCCURRENCES, UPDATE MAXIMUM OCCURRENCES AND STORE THE SENTENCE
                if (wordCount > maxOccurrences) {
                    maxOccurrences = wordCount;
                    sentencesWithMaxOccurrences.clear();
                    sentencesWithMaxOccurrences.add(line);
                } else if (wordCount == maxOccurrences) {
                    // IF CURRENT SENTENCE HAS SAME OCCURRENCES AS MAXIMUM, ADD TO THE LIST
                    sentencesWithMaxOccurrences.add(line);
                }
            }

            // PRINT SENTENCE(S) WITH THE MAXIMUM NO. OF OCCURRENCES OF THE SPECIFIC WORD
            System.out.println("\nSentences with maximum occurrences of the word \"" + word + "\":");
            for (String sentence : sentencesWithMaxOccurrences) {
                System.out.println("Sentence: " + sentence);
            }
            System.out.println("Frequency: " + maxOccurrences);
        }
    }


    // METHOD TO FIND SENTENCE(S) WITH THE MAXIMUM NO. OF OCCURRENCES OF A SPECIFIC PHRASE IN THE ENTIRE FILE
    public static void findSentenceWithMostPhraseOccurrences(String filePath, String phrase) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int maxOccurrences = 0;
            List<String> sentencesWithMaxOccurrences = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // COUNT OCCURRENCES OF THE SPECIFIC PHRASE IN THE SENTENCE
                int phraseCount = countPhraseOccurrences(line.toLowerCase(), phrase.toLowerCase());
                // IF CURRENT SENTENCE HAS MORE OCCURRENCES, UPDATE MAXIMUM OCCURRENCES AND STORE THE SENTENCE
                if (phraseCount > maxOccurrences) {
                    maxOccurrences = phraseCount;
                    sentencesWithMaxOccurrences.clear();
                    sentencesWithMaxOccurrences.add(line);
                } else if (phraseCount == maxOccurrences && phraseCount != 0) {
                    // IF CURRENT SENTENCE HAS SAME OCCURRENCES AS MAXIMUM AND NOT ZERO, ADD TO THE LIST
                    sentencesWithMaxOccurrences.add(line);
                }
            }

            // PRINT SENTENCE(S) WITH THE MAXIMUM NO. OF OCCURRENCES OF THE SPECIFIC PHRASE
            System.out.println("\nSentences with maximum occurrences of the phrase \"" + phrase + "\":");
            if (maxOccurrences == 0) {
                System.out.println("No sentence contains the phrase \"" + phrase + "\".");
            } else {
                for (String sentence : sentencesWithMaxOccurrences) {
                    System.out.println("Sentence: " + sentence);
                }
                System.out.println("Frequency: " + maxOccurrences);
            }
        }
    }


    // METHOD TO COUNT OCCURRENCES OF A SPECIFIC PHRASE IN A SENTENCE
    public static int countPhraseOccurrences(String sentence, String phrase) {
        int count = 0;
        int index = sentence.indexOf(phrase);
        while (index != -1) {
            count++;
            index = sentence.indexOf(phrase, index + 1);
        }
        return count;
    }
}
