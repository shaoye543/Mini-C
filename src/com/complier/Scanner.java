package com.complier;

import java.io.*;

/**
 * @Author: Shaoye
 * @Date: 2020/8/19
 * @ClassName: Scanner
 * 词法分析器
 * 文件读取尚有小bug亟需改正
 **/

public class Scanner {

    private static final int Start = 0;   //开始状态
    private static final int Num = 1;     //读到数字
    private static final int ID = 2;      //读到字母
    private static final int EQ = 3;      //读到等号
    private static final int GL = 4;      //读到大于或小于
    private static final int NE = 5;      //读到感叹号
    //    private static final int MU = 7;      //读到乘号
    //    private static final int DI = 8;      //读到除号
    //    private static final int SU = 9;      //读到减号
    //    private static final int AD = 10;     //读到加号
    private static final int Special = 11;
    private static final int Error = 12;
    private static final int Done = -1;   //结束符

    //关键字 种别码为其索引+1
    private static String[] keywords = {"else", "if", "switch", "case", "default", "int", "void", "struct", "return", "break", "goto", "while", "for"};
    //运算符和分隔符  种别码为其索引+14
    private static String[] symbols = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "[", "]", "{", "}"};

    private static BufferedReader file;
    private static String line;    //当前行内容
    private static int position = 0;   //当前字符位置
    private static int buffSize = 0;   //
    private static boolean isEOF = false;

    private Scanner(String filePath) throws Exception {
        file = new BufferedReader(new FileReader(filePath));
    }

    private static char getNextChar() throws IOException {
        char c = '\0';
        if (position >= buffSize) {
            if ((line = file.readLine()) != null) {
                buffSize = line.length();
                position = 0;
                c = line.charAt(position++);
            } else isEOF = true;
        } else c = line.charAt(position++);
        return c;
    }

    private static void scan() throws Exception {
        while (!isEOF) {
            getToken();
        }
    }


    /**
     * 判断是否当前字符为数字
     *
     * @param c
     * @return
     */
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * 判断当前字符是否为字母
     *
     * @param c
     * @return
     */
    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 获取种别码
     *
     * @param str
     * @return
     */
    private static int getCode(String str) {
        for (int i = 0; i < 13; i++) {
            if (str.equals(keywords[i]))
                return i + 1;
        }
        for (int i = 0; i < 19; i++) {
            if (str.equals(symbols[i]))
                return i + 14;
        }

        boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                flag = false;
                break;
            }
        }
        if (flag) return 33;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 'A' || (c > 'Z' && c < 'a') || c > 'z')
                return -1;
        }
        return 0;
    }


    private static void getToken() throws Exception {

        File file = new File("E:\\Code\\Java\\编译原理\\src\\com\\directory\\token.txt");
        if (!file.exists())
            file.createNewFile();
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write("字符\t\t种别码");

        StringBuilder token = new StringBuilder();
        int currentState = Start;
        char c = '\0';
        boolean isSave = true;

        while (!isEOF) {
            c = getNextChar();
            switch (currentState) {
                case Start:
                    if (isDigit(c))
                        currentState = Num;
                    else if (isLetter(c))
                        currentState = ID;
                    else if (c == '=')
                        currentState = EQ;
                    else if (c == '>' || c == '<')
                        currentState = GL;
                    else if (c == '!')
                        currentState = NE;
                    else
                        currentState = Done;
                    isSave = true;
                    break;
                case Num:
                    if (!isDigit(c)) {
                        position--;
                        currentState = Done;
                        isSave = false;
                    } else isSave = true;
                    break;
                case ID:
                    if (!isLetter(c) && !isDigit(c)) {
                        currentState = Done;
                        position--;
                        isSave = false;
                    } else isSave = true;
                    break;
                case EQ:
                    if (c == '=') {
                        currentState = Done;
                        isSave = true;
                    } else {
                        currentState = Done;
                        position--;
                        isSave = false;
                    }
                    break;
                case GL:
                    if (c == '=') {
                        currentState = Done;
                        isSave = true;
                    } else {
                        currentState = Start;
                        position--;
                        isSave = false;
                    }
                    break;
                case NE:
                    if (c == '=') {
                        currentState = Done;
                        isSave = true;
                    } else {
                        isSave = false;
                        position--;
                        currentState = Error;
                    }
                    break;
                case Error:
                    position--;
                    currentState = Start;
                    token = new StringBuilder();
                default:
                    break;
            }
            if (isSave) {
                token.append(c);
            }
            if (currentState == Done) {
                isSave = true;
                currentState = Start;
                int code = getCode(token.toString());
                if (code != -1) {
                    System.out.println("token:" + token + "---种别码:" + code);
                    bw.write("\n" + String.valueOf(token));
                    bw.write("\t\t" + String.valueOf(code));
                }
                token = new StringBuilder();
            }
        }
        bw.close();
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner("E:\\Code\\Java\\编译原理\\src\\com\\directory\\test.txt");
        scan();
    }

}
