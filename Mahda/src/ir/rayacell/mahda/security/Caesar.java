package ir.rayacell.mahda.security;
/**
 * 
 */


/**
 * @author rayacell
 *Apr 26, 2015
 */
public class Caesar {
	public static void main(String[] args) {
		System.out.println("*138*2*38*32,21,45.34*");
		String s = encryptText("*138*2*38*32,21,45.34*");
		System.out.println(s);
		System.out.println(decryptText(s));
	}
	  /*
     * This encrypts a text with the caesar encryption
     * 
     * @access public
     * @param String inputText - The text for the encryption
     * @return String
     */
    public static String encryptText(String inputText) {
        if(inputText.length() > 0) {
            //Replace special chars
            inputText = replaceSpecialChars(inputText);  
            //Length of text 
            int textChars = inputText.length();
            //Encryption
            String resultText = "";
            for(int i = 0; i < textChars; i++) {
              resultText = resultText + String.valueOf(encryptSingleChar(inputText.charAt(i)));
            }
           return resultText;
        }
        else return null;
//    	return inputText;
    }
    
    /*
     * Encryption of an single char 
     * 
     * @access private
     * @param char textChar - The char for encryption
     * @return char
     */
    private static char encryptSingleChar(char textChar) {
       char newChar = textChar;
       //Replcae characters
       if(textChar == '1') newChar = 'q';
       else if(textChar == '2') newChar = 'a';
       else if(textChar == '3') newChar = 'z';
       else if(textChar == '4') newChar = 's';
       else if(textChar == '5') newChar = 'd';
       else if(textChar == '6') newChar = 'g';
       else if(textChar == '7') newChar = 'o';
       else if(textChar == '8') newChar = 'r';
       else if(textChar == '9') newChar = 'b';
       else if(textChar == '0') newChar = 'Y';
       else if(textChar == '*') newChar = '8';

       return newChar;
    }
    
    /*
     * Decryption of a text
     * 
     * @access public
     * @param String inputText - The text for decryption (This should be a already encrypted text)
     * @return String
     */
    public static String decryptText(String inputText) {
        if(inputText.length() > 0) {
            //Replace special chars
            inputText = replaceSpecialChars(inputText);  
            //Length of text 
            int textChars = inputText.length();
            //Encryption
            String resultText = "";
            for(int i = 0; i < textChars; i++) {
              resultText = resultText + String.valueOf(decryptSingleChar(inputText.charAt(i)));
            }
            return resultText;
        }
        else return null;
    }
    
    /*
     * Decryption of an single char 
     * 
     * @access private
     * @param char textChar - The char for decryption
     * @return char
     */
    private static char decryptSingleChar(char textChar) {
       char newChar = textChar;
       //Replcae characters
       if(textChar == 'q') newChar = '1';
       else if(textChar == 'a') newChar = '2';
       else if(textChar == 'z') newChar = '3';
       else if(textChar == 's') newChar = '4';
       else if(textChar == 'd') newChar = '5';
       else if(textChar == 'g') newChar = '6';
       else if(textChar == 'o') newChar = '7';
       else if(textChar == 'r') newChar = '8';
       else if(textChar == 'b') newChar = '9';
       else if(textChar == 'y') newChar = '0';
       else if(textChar == '8') newChar = '*';
   
       return newChar;        
    }
    
    
    /*
     * Replacement for all german special characters
     * 
     * @access private
     * @param String inputText - The where the speical characters should be replaced
     * @return String
     */
    private static String replaceSpecialChars(String inputText) {
        //Replacement
//        inputText = inputText.replaceAll(String.valueOf("*"), "!");
//        inputText = inputText.replaceAll(String.valueOf("&"), "%");
//        inputText = inputText.replaceAll(String.valueOf("."), "@");
//        inputText = inputText.replaceAll(String.valueOf(","), "$");
        //Return new text 
        return inputText;
    }
}