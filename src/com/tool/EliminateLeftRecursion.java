package com.tool;


import com.pojo.Grammar;
import com.pojo.Production;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: EliminateLeftRecursion
 * 消除左递归
 **/

public class EliminateLeftRecursion {

    private static final Grammar grammar = new Grammar("src/com/directory/消除左递归.txt");

    private static void display(Production production) {
        for (int i = 0; i < production.value.length; i++) {
            if (production.value[i][0] != null) {
                System.out.print(production.key);
                for (String str : production.value[i]) {
                    if (str == null) break;
                    else System.out.print(" " + str);
                }
                System.out.println();
            }
        }
    }

    /**
     * 从文件读取简化后的产生式集合
     *
     * @param filepath
     * @return
     */
    private static void getProductions(String filepath) {
        BufferedReader reader = null;
        String line;
        int num = 0;     //记录非终结符的数量
        try {
            reader = new BufferedReader(new FileReader(new File(filepath)));
            while ((line = reader.readLine()) != null) {
                String[] terminal = line.split("\\s+");          //获取到所有非终结符或终结符存入数组
                Production production = new Production();
                production.key = terminal[0];             //第一个字符是产生式左端
                int row = 0, col = 0;                     //当前产生式的子产生式、子产生式的第col个字符
                for (int i = 1; i < terminal.length; i++) {
                    if ("|".equals(terminal[i])) {
                        row++;
                        col = 0;
                    } else
                        production.value[row][col++] = terminal[i];
                }
                grammar.production.add(production);          //加入文法集合中
                num++;
            }
            for (int i = 0; i < num; i++)      //所有非终结符集合
                grammar.vn.add(grammar.production.get(i).key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static {
        getProductions("src/com/directory/simplify_grammar.txt");
    }

    /**
     * 检验是否存在间接左递归
     */
    private static class Pro {
        String key;
        String[] value = new String[20];
        private static boolean isExist = false;         //记录是否存在间接左递归
        private static final List<Pro> pros = new ArrayList<>();

        static void change() {
            for (Production production : grammar.production) {
                for (String[] strings : production.value) {
                    if (strings[0] != null) {
                        Pro pro = new Pro();
                        pro.key = production.key;
                        pro.value = strings;
                        pros.add(pro);
                    } else break;
                }
            }
        }

        private static void isExistDirLeftRecursion(List<String> list, boolean[] visit, String key, String check) {
            if (list.size() > 0 && key.equals(check)) {    //初始时check和key一定相同 所以需要加限制条件list的集合内需要存在元素
                System.out.println(list);
                System.out.println(check + ":YesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes\nYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYesYes");
                isExist = true;
            } else {
                for (int i = 0; i < pros.size(); i++) {
                    Pro pro = pros.get(i);
                    if (pro.key.equals(key) && !pro.value[0].equals(key) && !visit[i]) {
                        List<String> list1 = list;
                        if (list.size() > 0) {
                            List<String> sub = list.subList(1, list.size());   //将首部替换为相应的产生式右部
                            list = new ArrayList<>();       //清空
                            for (String string : pro.value) {
                                if (string != null) list.add(string);
                                else break;
                            }
                            list.addAll(sub);
                        } else {
                            for (String string : pro.value) {
                                if (string != null) list.add(string);
                                else break;
                            }
                        }

                        visit[i] = true;
                        System.out.println(list);
                        for (int k = 0; k < list.size(); k++) {
                            if (!"ε".equals(list.get(k))) break;
                            else {
                                list.remove(k);
                                k--;
                            }
                        }

                        if (list.size() > 0)             //上面消去ε（空）后可能导致list集合清空
                            isExistDirLeftRecursion(list, visit, list.get(0), check);
                        visit[i] = false;
                        list = list1;
                        System.out.println("--------------------------回溯-------------------------");
                    }
                }
            }
        }

        public static void main(String[] args) {
            change();
            boolean[] visit = new boolean[78];
            for (String string : grammar.vn) {
                List<String> list = new ArrayList<>();
                System.out.println("*******************************************************");
                System.out.println(string);
                System.out.println("*******************************************************");
                isExistDirLeftRecursion(list, visit, string, string);
            }
            System.out.println(isExist);
        }
    }

    /**
     * 消除直接左递归
     */
    private static void EliDirLeftRecursion() {
        List<Production> productions = new ArrayList<>();
        for (Production production : grammar.production) {
            Production pro1 = new Production();         //存储原左部的产生式
            Production pro2 = new Production();         //存储变换后的产生式
            int num1 = 0, num2 = 0;
            pro1.key = production.key;
            if (production.value[0][0].equals(production.key)) {         //存在直接左递归
                for (String[] strings : production.value) {
                    if (strings[0] != null) {
                        if (strings[0].equals(production.key)) {
                            pro2.key = pro1.key + "’";        //变换后的关键字
                            for (int k = 1; k < strings.length; k++) {      //存放新的产生式
                                if (strings[k] != null) pro2.value[num2][k - 1] = strings[k];
                                else {
                                    pro2.value[num1][k - 1] = pro1.key + "’";
                                    break;
                                }
                            }
                            num2++;
                        } else {
                            for (int k = 0; k < strings.length; k++) {      //存放新的产生式
                                if (strings[k] != null) pro1.value[num1][k] = strings[k];
                                else {
                                    pro1.value[num1][k] = pro1.key + "’";
                                    break;
                                }
                            }
                            num1++;
                        }
                    }
                    pro2.value[num2][0] = "ε";
                }
                productions.add(pro1);
                productions.add(pro2);
            } else productions.add(production);
        }
        grammar.production = productions;
    }

    public static void main(String[] args) throws IOException {
        EliDirLeftRecursion();

        for (Production production : grammar.production)
            display(production);

        File file = new File("src/com/directory/消除左递归.txt");
        if (!file.exists())
            file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Production production : grammar.production) {
            for (String[] strings : production.value) {
                if (strings[0] != null) {
                    writer.write(production.key);
                    for (String str : strings)
                        if (str != null)
                            writer.write(" " + str);
                    writer.write("\n");
                }
            }
        }
        writer.close();

    }

}
