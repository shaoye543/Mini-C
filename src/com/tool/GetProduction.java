package com.tool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: GetProduction
 **/

public class GetProduction {
    private static List<String> list = new ArrayList<>();
    private static Map<String, String> result = new HashMap<>();

    /**
     * 将产生式拆分  一行一个产生式
     *
     * @throws IOException
     */
    private static void writeAllProduction() throws IOException {
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(new File("src/com/directory/grammar.txt")));
        String line;
        reader.readLine();      //第一行为注释 没必要读取

        //存放方便程序读取的文法
        File file = new File("src/com/directory/grammer1.txt");
        if (!file.exists())
            file.createNewFile();
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write("左部(只有一个)   右部(以空格分隔)");

        while ((line = reader.readLine()) != null) {
            String[] nonTerminal = line.split("\\s+");   //以空格分割
            writer.write("\n" + nonTerminal[1]);       //第一个为产生式的左部
            list.add(nonTerminal[1]);      //所有的非终结符
            for (int i = 2; i < nonTerminal.length; i++) {       //每行的第一个字符串皆是序号
                if (!nonTerminal[i].equals("|"))     //产生式右部 以空格分隔
                    writer.write(" " + nonTerminal[i]);
                else
                    writer.write("\n" + nonTerminal[1]);
//                System.out.println(nonTerminal[i]);
            }
        }
        writer.close();
    }

    /**
     * 将起始符和非终结符简化 如A B C AA, etc
     *
     * @throws IOException
     */
    private static void SimplifyProduction() throws IOException {
        File file = new File("src/com/directory/grammer2.txt");
        if (!file.exists())
            file.createNewFile();
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fileWriter);

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            char c = (char) ('A' + i);
            if (i < 26)
                result.put(list.get(i), String.valueOf(c));
            else
                result.put(list.get(i), "A" + String.valueOf((char) ('A' + i % 26)));
            writer.write(s);
            for (int k = 0; k < 30 - s.length(); k++)
                writer.write(" ");
            writer.write(result.get(s) + "\n");
        }
        writer.close();
    }

    /**
     * 将所有产生式替换为简化符
     */
    private static void writeAllSimplifiedProduction() throws IOException {
        File file = new File("src/com/directory/grammer3.txt");
        BufferedReader reader = new BufferedReader(new FileReader(new File("src/com/directory/grammer1.txt")));
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fileWriter);
        if (!file.exists())
            file.createNewFile();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] pros = line.split("\\s+");
            for (String s : pros) {
                String pro = result.get(s);
                if (pro != null)
                    writer.write(pro + " ");
                else
                    writer.write(s + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    private static void writeAllSimplifiedProduction1() throws IOException {
        File file = new File("src/com/directory/simplify_grammar.txt");
        BufferedReader reader = new BufferedReader(new FileReader(new File("src/com/directory/grammar.txt")));
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fileWriter);
        if (!file.exists())
            file.createNewFile();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] pros = line.split("\\s+");
            writer.write(result.get(pros[1]));
            for (int i = 2; i < pros.length; i++) {
                String s = result.get(pros[i]);
                if (s != null)
                    writer.write(" " + s);
                else
                    writer.write(" " + pros[i]);
            }
            writer.write("\n");
        }
        writer.close();
    }


    public static void main(String[] args) throws IOException {
        writeAllProduction();
        SimplifyProduction();
        writeAllSimplifiedProduction();
        writeAllSimplifiedProduction1();


//        for (String value : list) {
//            String[] fragment = value.split("-");
//            StringBuilder temp = new StringBuilder();
//            for (String s : fragment)
//                temp.append(s.charAt(0));
//
//            boolean flag = false;
//            String t = String.valueOf(temp).toUpperCase();
//            for (String key : result.keySet()) {   //是否有重复的缩写
//                if (t.equals(result.get(key))) {
//                    flag = true;
//                    System.out.println("重复:" + key + " : " + value + " : " + t);
//                    break;
//                }
//            }
//            if (!flag)
//                result.put(value, t);
//        }
//
//        for (String key : result.keySet())
//            System.out.println(key + " : " + result.get(key));
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < list.size(); i++)
//            result.put(list.get(i), String.valueOf(simple.get(i)).toUpperCase());


    }
}
