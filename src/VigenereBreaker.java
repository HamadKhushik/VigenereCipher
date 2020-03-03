import edu.duke.DirectoryResource;
import edu.duke.FileResource;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class VigenereBreaker {

    public String sliceString(String message, int whichSlice, int totalSlices) {
        //has three parameters—a String message, representing the encrypted message, an integer whichSlice,
        //indicating the index the slice should start from, and an integer totalSlices, indicating the
        //length of the key. This method returns a String consisting of every totalSlices-th character
        //from message, starting at the whichSlice-th character.

        StringBuilder result = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            result.append(message.charAt(i));
        }
        return result.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {

        // takes three parameters—a String encrypted that represents the encrypted message, an integer klength
        // that represents the key length, and a character mostCommon that indicates the most common character
        // in the language of the message. This method should make use of the CaesarCracker class, as well as
        // the sliceString method, to find the shift for each index in the key. You should fill in the
        // key (which is an array of integers) and return it

        int[] key = new int[klength];
        for (int i = 0; i < klength; i++) {
            String sliced = sliceString(encrypted, i, klength);
            CaesarCracker ccr = new CaesarCracker(mostCommon);
            int currKey = ccr.getKey(sliced);
            key[i] = currKey;
        }
        return key;
    }

    public void breakVigenere() {

        //This void method puts everything together, so that you can create a new VigenereBreaker,
        // call this method on it, and crack the cipher used on a message.

        FileResource fr = new FileResource();
        String message = fr.asString();
        HashMap<String, HashSet<String>> dictionaries = new HashMap<String, HashSet<String>>();
        DirectoryResource dict = new DirectoryResource();
        for (File f : dict.selectedFiles()) {
            FileResource frDict = new FileResource(f);
            HashSet<String> dictionary = readDictionary(frDict);
            dictionaries.put(f.getName(), dictionary);
            System.out.println(f.getName());
        }
        breakForAllLangs(message, dictionaries);
    }

    public HashSet<String> readDictionary(FileResource fr) {
        HashSet<String> dictionary = new HashSet<String>();
        for (String line : fr.lines()) {
            dictionary.add(line.toLowerCase());
        }
        return dictionary;
    }

    public int countWords(String message, HashSet<String> dictionary) {
        String[] words = message.split("\\W");
        int count = 0;
        for (String word : words) {
            if (dictionary.contains(word.toLowerCase())) {
                count += 1;
            }
        }
        return count;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {

        //This method tries all the keyLengths from 1 to 100 and decrypts the message with every keyLength
        // returns the decrypted message with most valid words by using countWords()

        int count = 0;
        int keyLength = 0;
        String result = "";
        StringBuilder sb = new StringBuilder();
        char c = mostCommonCharIn(dictionary);
        for (int i = 1; i < 101; i++) {
            int[] key = tryKeyLength(encrypted, i, c);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int currCount = countWords(decrypted, dictionary);
            if (currCount > count) {
                count = currCount;
                result = decrypted;
                keyLength = i;
                sb.setLength(0);
                sb.append(Arrays.toString(key));
            }
        }
        System.out.println("The number of valid words found is " + count);
        System.out.println("The key Length is " + keyLength);
        System.out.println("The key is " + sb);
        return result;
    }

    public char mostCommonCharIn(HashSet<String> dictionary) {

        //which has one parameter—a HashSet of Strings dictionary. This method should find out
        // which character, of the letters in the English alphabet, appears most often in
        // the words in dictionary

        HashMap<Character, Integer> alphabetCount = new HashMap<Character, Integer>();
        for (String word : dictionary) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (!alphabetCount.containsKey(c)) {
                    alphabetCount.put(c, 1);
                } else {
                    alphabetCount.put(c, alphabetCount.get(c) + 1);
                }
            }
        }
        char mostCommon = 0;
        int currCount = 0;
        for (char c : alphabetCount.keySet()) {
            if (alphabetCount.get(c) > currCount) {
                mostCommon = c;
                currCount = alphabetCount.get(c);
            }
        }
        return mostCommon;
    }

    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages) {

        //has two parameters—a String encrypted, and a HashMap, called languages, mapping a String
        // representing the name of a language to a HashSet of Strings containing the words in that language.
        // Tries breaking the encryption for each language, and see which gives the best results!

        String language = "";
        String result = "";
        int maxWords = 0;
        for (String lang : languages.keySet()) {
            HashSet<String> dictionary = languages.get(lang);
            System.out.println("\n\n" + lang);
            String currResult = breakForLanguage(encrypted, dictionary);
            int currCount = countWords(currResult, dictionary);
            if (currCount > maxWords) {
                maxWords = currCount;
                language = lang;
                result = currResult;
            }
        }
        System.out.println("The decryption language is " + language);
        System.out.println("The decrypted String is " + result);
    }
}
