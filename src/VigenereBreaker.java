import edu.duke.FileResource;

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
        int[] key = tryKeyLength(message, 4, 'e');
        VigenereCipher vc = new VigenereCipher(key);
        String decrypted = vc.decrypt(message);
        System.out.println("The decrypted message is \n" + decrypted);
    }

}
