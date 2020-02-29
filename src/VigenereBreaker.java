import edu.duke.FileResource;

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
        FileResource dict = new FileResource();
        HashSet<String> dictionary = readDictionary(dict);
        String decrypted = breakForLanguage(message, dictionary);
        System.out.println("The decrypted message is \n" + decrypted);
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

    public String breakForLanguage(String encrypted, HashSet dictionary) {

        //This method tries all the keyLengths from 1 to 100 and decrypts the message with every keyLength
        // returns the decrypted message with most valid words by using countWords()

        int count = 0;
        int keyLength = 0;
        String result = "";
        for (int i = 1; i < 101; i++) {
            int[] key = tryKeyLength(encrypted, i, 'e');
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int currCount = countWords(decrypted, dictionary);
            if (currCount > count) {
                count = currCount;
                result = decrypted;
                keyLength = i;
            }
        }
        System.out.println("The number of valid words found is " + count);
        System.out.println("The key Length is " + keyLength);

        return result;
    }

}
