import edu.duke.FileResource;


public class tester {

    public String readFile() {
        FileResource fr = new FileResource();
        String message = fr.asString();
        return message;
    }

    public void testCaesarCipher() {
        CaesarCipher cc = new CaesarCipher(15);
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println("The Encrypted message is " + cc.encrypt(message));
        System.out.println("The Decrypted message is " + cc.decrypt(cc.encrypt(message)));
        for (char c : message.toCharArray()) {
            System.out.println(cc.encryptLetter(c));
            System.out.println(cc.decryptLetter(cc.encryptLetter(c)));
        }
    }

    public void testCaesarCracker() {
        CaesarCracker cr = new CaesarCracker('a');
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println("The key to the encrypted message is " + cr.getKey(message));
        System.out.println("The decrypted message is " + cr.decrypt(message));
    }

    public void testVigenereCipher() {
        int[] key = {17, 14, 12, 4};
        VigenereCipher vc = new VigenereCipher(key);
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println("The encrypted message is " + vc.encrypt(message));
        System.out.println("The decrypted message is " + vc.decrypt(vc.encrypt(message)));
    }

    public void testVigenereBreaker() {
        VigenereBreaker vb = new VigenereBreaker();
        //FileResource fr = new FileResource();
        //String message = fr.asString();
        //System.out.println(vb.sliceString("abcdefghijklm", 0, 4));
        //int[] key = vb.tryKeyLength(message, 38, 'e');
        //System.out.println("The key is " + Arrays.toString(key));
        vb.breakVigenere();
    }

    public static void main(String[] args) {
        tester t = new tester();
        //t.testCaesarCipher();
        //t.testCaesarCracker();
        //t.testVigenereCipher();
        t.testVigenereBreaker();

    }
}
