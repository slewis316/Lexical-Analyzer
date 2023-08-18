// Sean Lewis
// Lexeme Data Object


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class lexemeData {
    
    public String lexemeKind = "";
    public int lineNum = 0;
    public int colNum = 0;
    public String lexText = "";

    public lexemeData() {}
    public lexemeData(String lexemeKind, int lineNum, int colNum, String lexText) {
        this.lexemeKind = lexemeKind;
        this.lineNum = lineNum;
        this.colNum = colNum;
        this.lexText = lexText;
    }
    public lexemeData(int lineNum, int colNum, String lexText) {
        this.lineNum = lineNum;
        this.colNum = colNum;
        this.lexText = lexText;
    }
     
    public String getLexemeKind() {
        return lexemeKind;
    }
    public void setLexemeKind(String lexemeKind) {
        this.lexemeKind = lexemeKind;
    }
    public int getLineNum() {
        return lineNum;
    }
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
    public int getColNum() {
        return colNum;
    }
    public void setColNum(int colNum) {
        this.colNum = colNum;
    }
    public String getLexText() {
        return lexText;
    }
    public void setLexText(String lexText) {
        this.lexText = lexText;
    }   

   

}
