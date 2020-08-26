package com.complier;

import java.io.*;

/**
 * @Author: Shaoye
 * @Date: 2020/8/19
 * @ClassName: Scanner
 * �ʷ�������
 * �ļ���ȡ����Сbugؽ�����
 **/

public class Scanner {

    private static final int Start = 0;   //��ʼ״̬
    private static final int Num = 1;     //��������
    private static final int ID = 2;      //������ĸ
    private static final int EQ = 3;      //�����Ⱥ�
    private static final int GL = 4;      //�������ڻ�С��
    private static final int NE = 5;      //������̾��
    //    private static final int MU = 7;      //�����˺�
    //    private static final int DI = 8;      //��������
    //    private static final int SU = 9;      //��������
    //    private static final int AD = 10;     //�����Ӻ�
    private static final int Special = 11;
    private static final int Error = 12;
    private static final int Done = -1;   //������

    //�ؼ��� �ֱ���Ϊ������+1
    private static String[] keywords = {"else", "if", "switch", "case", "default", "int", "void", "struct", "return", "break", "goto", "while", "for"};
    //������ͷָ���  �ֱ���Ϊ������+14
    private static String[] symbols = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "[", "]", "{", "}"};

    private static BufferedReader file;
    private static String line;    //��ǰ������
    private static int position = 0;   //��ǰ�ַ�λ��
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
     * �ж��Ƿ�ǰ�ַ�Ϊ����
     *
     * @param c
     * @return
     */
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * �жϵ�ǰ�ַ��Ƿ�Ϊ��ĸ
     *
     * @param c
     * @return
     */
    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * ��ȡ�ֱ���
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

        File file = new File("E:\\Code\\Java\\����ԭ��\\src\\com\\directory\\token.txt");
        if (!file.exists())
            file.createNewFile();
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write("�ַ�\t\t�ֱ���");

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
                    System.out.println("token:" + token + "---�ֱ���:" + code);
                    bw.write("\n" + String.valueOf(token));
                    bw.write("\t\t" + String.valueOf(code));
                }
                token = new StringBuilder();
            }
        }
        bw.close();
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner("E:\\Code\\Java\\����ԭ��\\src\\com\\directory\\test.txt");
        scan();
    }

}
