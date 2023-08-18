// Sean Lewis
// Lexical Analyzer

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// user can enter a file to analyze or to "quit" 
// when analyzed, all lexeme tokens are shown
// if a lexically-invalid token is found, the analysis terminates and reports an error
// the user is infinitely queried for new files until they "quit" the program 

public class lexicalAnalyzer {

  static int currentLine = 0;

  // Establish range of valid Capital letters (ASCII)
  static Integer validBigAlphaBegin = new Integer(65);
  static Integer validBigAlphaEnd = new Integer(90);

  // Establish range of valid lower letters (ASCII)
  static Integer validLilAlphaBegin = new Integer(97);
  static Integer validLilAlphaEnd = new Integer(122);

  // Establish valid digit range (ASCII)
  static Integer validDigitBegin = new Integer(48);
  static Integer validDigitEnd = new Integer(57);

  // arrays for valid symbols, kewords, and ops

  static char[] validChars = { 32, 36, 40, 41, 
                               58, 42, 43, 45, 47, 
                               59, 60, 61, 62, 95 };

  static String[] validOperators = { "<", "=<", "=", ">=", ">",
                                     "+", "-", "*", "/", "(",
                                     //")", "_", ";", ":=", ":",
                                     ")", "_", ";", ":=", 
                                     "!=", "$", ":" };

  static String[] validKeyWords = { "or", "and", "not", "program", "end",
                                    "bool", "int", "if", "then", "else",
                                    "fi", "while", "do", "od", "print", "false",
                                    "true", "String" };

  static Integer eofChar = new Integer(36);

  static ArrayList < lexemeData > lexDataArray = new ArrayList < lexemeData > ();

  public static void main(String[] args) throws IOException {
    String fileNameToRead = null;
    ArrayList < Integer > intArray;

    lexemeData ld = new lexemeData();

    char[] charArray;

    while (true) {

      System.out.println("Please enter a file path/name to analyze or quit.");

      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      fileNameToRead = reader.readLine();

      if (fileNameToRead.equalsIgnoreCase("quit")) {
        System.exit(0);
      }
      try {

        // clear array in case second file read in
        lexDataArray.clear();
        currentLine = 0;
        // input file (must be user input thru command line)
        File f = new File(fileNameToRead);
        // file reader
        FileReader fr = new FileReader(f);
        // bufferedReader
        BufferedReader br = new BufferedReader(fr);

        do {
          intArray = reader(br);
          currentLine++;
          int ret = 0;

          ret = validateLine(intArray);
          next(intArray);
        }
        while (!intArray.get(0).equals(eofChar));

        // first pass
        tokenSplit();

        // second pass
        getKind();

        // where log gets built
        for (int i = 0; i < lexDataArray.size(); i++) {
          lexemeData lData = lexDataArray.get(i);

          if (lData.getLexemeKind().equals("")) {
            System.out.println(lData.getLineNum() + ":" + lData.getColNum() + ": " +
              "'" + lData.getLexText() + "'");
          } else if (lData.getLexText().equals("end")) {
            System.out.println(lData.getLineNum() + ":" + lData.getColNum() + ": " + lData.getLexemeKind());
            System.out.println("Concluded analysis on " + fileNameToRead);
          } else {
            System.out.println(lData.getLineNum() + ":" + lData.getColNum() + ": " + lData.getLexemeKind() +
              lData.getLexText());
          }
        }

      } catch (IOException e) {
        System.out.println(e.getMessage());
        System.exit(1);
      }
    } // end outer while 
  }

  // iterate over line chars (ints) read from file and validate each char
  public static int validateLine(ArrayList < Integer > intArray) {
    int c = 0;
    boolean found = false;

    if (intArray.size() > 0) {
      int i = 0;
      for (; i < intArray.size(); i++) {
        found = false;

        // needed this extra check for comments
        if ((i + 1) <= intArray.size()) {
          if (47 == intArray.get(i).intValue() &&
              47 == intArray.get(i + 1).intValue()) {
            return (0);
          }
        }
        // check for invalid operators
        // look for '=:'
        if ((i + 1) <= intArray.size()) {
            if (61 == intArray.get(i).intValue()) {
                if (58 == intArray.get(i + 1).intValue()) {
                    System.out.println("currentLine = " + currentLine + " operator not allowed: '=:' at position " + currentLine + ":" + i);
                    System.exit(1);
                }
            }
        }

        c = intArray.get(i).intValue();
        //System.out.println("currentLine = " + currentLine + " c = " + (char)c);

        // check for uppercase letter
        if (c >= validBigAlphaBegin.intValue() && c <= validBigAlphaEnd.intValue()) {
          found = true;
          //System.out.println("currentLine = " + currentLine + " validBigAlpha found. Char = " + (char)c);
        }
        // check for lower case letter
        else if (c >= validLilAlphaBegin.intValue() && c <= validLilAlphaEnd.intValue()) {
          found = true;
          //System.out.println("currentLine = " + currentLine + " validLilAlpha found. Char = " + (char)c);
        }
        // check for digit
        else if (c >= validDigitBegin.intValue() && c <= validDigitEnd.intValue()) {
            //System.out.println("currentLine = " + currentLine + " validDigit found. Char = " + (char)c);
          found = true;
        } else {
          // search allowed symbol array
          for (int j = 0; j < validChars.length; j++) {
            if (c == validChars[j]) {
              found = true;
              //System.out.println("currentLine = " + currentLine + " validChars found. Char = " + (char)c);
              break;
            }
          }
        }

        // check for '!=', which is allowed
        if (!found && c == 33) {
            if ((i + 1) <= intArray.size()) {
                if (61 == intArray.get(i + 1).intValue()) {
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Character not allowed: " + (char) c + " at position " +
              currentLine + ":" + i);
            System.exit(1);
        }

    
      } // end for

      /* 
      if (!found) {
        System.out.println("Character not allowed: " + (char) c + " at position " +
          currentLine + ":" + i);
        System.exit(1);
      } else {
        return 0;
      }
*/
    }
    
    return 0;
    
  }

  // read a line from the file and return in ArrayList of Integers
  public static ArrayList < Integer > reader(BufferedReader br) throws IOException {
    //int array for storing
    ArrayList < Integer > intArray = new ArrayList < Integer > ();
    int c = 0;

    // read char by char
    while ((c = br.read()) != -1) {

      // 36 is $ 26 is EOF
      if (c == 26 || c == 36) {
        break;
      } else if (c == 13 || c == 10) {
        break;
      } else {
        intArray.add(new Integer(c));
      }
    }

    if (c == -1) {
      intArray.add(new Integer(36));
    }
    return intArray;
  }

  // parse the line (ArrayList of Integers). tokenize on space. tokens are stored in lexemaData objects with
  // other needed data
  public static void next(ArrayList < Integer > intArray) {
    ArrayList < Integer > tokenIntegerArray = new ArrayList < Integer > ();
    boolean moreToParse = true;
    int currCol = 1;
    boolean isAlphaNum;
    int tokenColBegin = 0;
    String kind = "";

    while (moreToParse || currCol < intArray.size()) {

      isAlphaNum = false;

      // look for spaces to pass over
      if (32 == intArray.get(currCol - 1).intValue() && isAlphaNum == false) {
        currCol++;
        continue;
      } else if (47 == intArray.get(currCol - 1).intValue() &&
        47 == intArray.get(currCol).intValue()) {
        // Two slashes found together. Stop processing
        break;
      } else {
        isAlphaNum = true;
      }

      // begin tokenizing
      // Preserve starting point of token
      tokenColBegin = currCol;

      // Past any spaces, clear temp array for population
      tokenIntegerArray.clear();

      while (32 != intArray.get(currCol - 1).intValue()) {
        // Check that we're not overrunning buffer
        if (currCol == intArray.size()) {
          moreToParse = false;
          tokenIntegerArray.add(intArray.get(currCol - 1)); //new
          break;
        }

        tokenIntegerArray.add(intArray.get(currCol - 1));
        currCol++;
      }

      // what is the token
      String tokenString = "";
      char[] tokenCharArray = new char[tokenIntegerArray.size()];

      for (int i = 0; i < tokenIntegerArray.size(); i++) {
        tokenCharArray[i] = (char) tokenIntegerArray.get(i).intValue();
      }
      tokenString = new String(tokenCharArray);

      // Adjust column count
      int charCnt = (currCol - tokenString.length());

      lexemeData lexData = new lexemeData(null, currentLine, tokenColBegin, tokenString);
      lexDataArray.add(lexData);
    } // end outer while
  }

  // iterate over lexemeData object array and determine kind
  static void getKind() {
    boolean found = false;
    int keywordNeedsIDPOS = -1;
    String kind = "";
    String noKind = "";
    String[] IDKeywords = {
      "program",
      "bool",
      "int",
      "String",
      "long"
    };
    ArrayList < String > IDVarArray = new ArrayList < String > ();

    // Detect kind. Labeled kind possibilities = ID, NUM
    for (int i = 0; i < lexDataArray.size(); i++) {
      found = false;
      lexemeData lexDObj = lexDataArray.get(i);

      // set kind default 
      lexDObj.setLexemeKind(noKind);

      // check if element indicates next token is ID
      for (int j = 0; j < IDKeywords.length; j++) {
        if (lexDObj.getLexText().equals(IDKeywords[j])) {
          found = true;
          kind = noKind;
          keywordNeedsIDPOS = i;
          break;
        }
      }
      // needs ID handling
      // check if preceeding element needs ID flag
      if (keywordNeedsIDPOS > -1 && i == keywordNeedsIDPOS + 1) {
        found = true;
        lexDObj = lexDataArray.get(i);
        lexDObj.setLexemeKind("'ID' ");
        keywordNeedsIDPOS = -1;

        // store this idea for further use
        IDVarArray.add(lexDObj.getLexText());
      }

      // detect operators
      if (!found) {
        for (int j = 0; j < validOperators.length; j++) {
          if (lexDObj.getLexText().equals(validOperators)) {
            found = true;
            kind = noKind;
            break;
          }
        }
      }

      // detect numbers
      if (!found) {
        String val = lexDObj.getLexText();
        StringBuffer sb = new StringBuffer(val);
        boolean digit = true;

        for (int j = 0; j < sb.length(); j++) {

          int dig = (int) sb.charAt(j);

          if (dig >= validDigitBegin.intValue() && dig <= validDigitEnd.intValue()) {
            digit = true;
          } else {
            digit = false;
            break;
          }
        }
        if (digit == true) {
          lexDObj.setLexemeKind("'NUM' ");
          found = false;
        }
      }
      if (!found) {
        if (lexDObj.getLexText().equals("end")) {
          lexDObj.setLexemeKind("'end-of-text' ");
        }
      }

      // check for vars past declaration (ID)
      if (!found) {
        for (int j = 0; j < IDVarArray.size(); j++) {
          if (lexDObj.getLexText().equals(IDVarArray.get(j))) {
            lexDObj.setLexemeKind("'ID' ");
          }
        }
      }
      // check end for $
      if (lexDObj.getLexText().equals("end$")) {
        lexDObj.setLexText("end");
        lexDObj.setLexemeKind("'end-of-text' ");
      } else if (lexDObj.getLexText().endsWith("$")) {
        lexDataArray.remove(i);

      }

    } // end outer for 
    return;
  }

  // iterates through lexemeData objects in ArrayList and handles the complicated tokens. creates and inserts
  // new lexemeData object as needed 
  public static void tokenSplit() {
    int oldColCount;
    int newColCount;

    StringBuffer sb;
    String tokenText;
    String newTokenText;

    for (int i = 0; i < lexDataArray.size(); i++) {
      lexemeData lexD = lexDataArray.get(i);
      oldColCount = lexD.getColNum();
      tokenText = lexD.getLexText();;

      if (tokenText.length() == 1) {
        // only 1 char, no parsing needed
        continue;
      }

      if (tokenText.equals(":=")) {
        continue;
      }

      sb = new StringBuffer("");
      sb.append(tokenText);
      int k = 0;
      newColCount = oldColCount;

      while (sb.charAt(k) == ' ') {
        // remove more whitespaces
        sb.deleteCharAt(k);
        k++;
        newColCount++;
      }

      // Handle begin paren
      if (sb.charAt(0) == '(') {

        // handle new lexData object
        lexemeData newlData = new lexemeData();
        newlData.setLineNum(lexD.getLineNum());
        newlData.setColNum(oldColCount);
        newlData.setLexText("(");

        // handle old lexData object
        lexD.setColNum(oldColCount + 1);
        sb.deleteCharAt(0);
        lexD.setLexText(sb.toString());

        // insert new
        lexDataArray.add(i, newlData);
        continue;
      }
      // Handle end paren
      if (tokenText.endsWith(")")) {
        int len = sb.length();

        // handle new lexData object
        lexemeData newlData = new lexemeData();
        newlData.setLineNum(lexD.getLineNum());
        newlData.setColNum(oldColCount + (len - 1));
        newlData.setLexText(")");

        // handle old lexData object
        sb.deleteCharAt(len - 1);
        lexD.setLexText(sb.toString());

        // insert new
        lexDataArray.add(i + 1, newlData);
      }

      // handle end w/ colon
      if (tokenText.endsWith(":")) {
        int len = sb.length();

        // handle new lexData object
        lexemeData newlData = new lexemeData();
        newlData.setLineNum(lexD.getLineNum());
        newlData.setColNum(oldColCount + (len - 1));
        newlData.setLexText(":");

        // handle old lexData object
        sb.deleteCharAt(len - 1);
        lexD.setLexText(sb.toString());

        // insert new
        lexDataArray.add(i + 1, newlData);
      }

      // handle end w/ ;
      if (tokenText.endsWith(";")) {
        int len = sb.length();

        // handle new lexData object
        lexemeData newlData = new lexemeData();
        newlData.setLineNum(lexD.getLineNum());
        newlData.setColNum(oldColCount + (len - 1));
        newlData.setLexText(";");

        // handle old lexData object
        sb.deleteCharAt(len - 1);
        lexD.setLexText(sb.toString());

        // insert new
        lexDataArray.add(i + 1, newlData);
      }
    }
  }
}