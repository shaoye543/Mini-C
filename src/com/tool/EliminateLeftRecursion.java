package com.tool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: EliminateLeftRecursion
 **/

public class EliminateLeftRecursion {

    private static class Production {
        String key;          //左
        List<List<String>> value = new ArrayList<>();

        boolean equal(Production production) {
            List<String> list = this.value.get(0);
            List<String> list1 = production.value.get(0);
            if (production.key.equals(this.key))
                return list.equals(list1);
            return false;
        }
    }


    /**
     * 其实这个类最终也没啥用 反而还弄麻烦了 让我放弃了
     */
    private static class Grammer {
        String[] vn;      //非终结符集合
        String[] vt;      //终结符集合
        List<Production> production = new ArrayList<>();  //产生式
        String start;             //起始符
    }

    private static void display(Grammer grammer) {
        int i = 0;
        for (Production production : grammer.production) {
            for (List<String> list : production.value) {
                System.out.print(++i + ". " + production.key + " ");
                for (String s : list)
                    System.out.print(s + " ");
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Grammer grammer = new Grammer();

        BufferedReader reader = new BufferedReader(new FileReader(new File("src/com/directory/grammer4.txt")));
        String line;

        while ((line = reader.readLine()) != null) {
            Production production = new Production();
            String[] str = line.split("\\s+");

            production.key = str[0];    //第一个是左部的key
            List<String> temp = new ArrayList<>();
            for (int i = 1; i < str.length; i++) {
                if (!"|".equals(str[i]))
                    temp.add(str[i]);
                else {
                    production.value.add(temp);
                    temp = new ArrayList<>();
                }
            }
            production.value.add(temp);
            grammer.production.add(production);
        }
        reader.close();


        Grammer result = new Grammer();          //最终产生式结果
        for (int i = 0; i < grammer.production.size(); i++) {
//            for (int j = 0; j < i; j++) {                 //间接左递归变为直接左递归
//                for (int k = 0; k < grammer.production.get(i).value.size(); k++) {
//                    List<String> list = grammer.production.get(i).value.get(k);
//                    String iStart = list.get(0);
//                    if (iStart.equals(grammer.production.get(j).key)) {
//                        List<String> list1 = new ArrayList<>();
//                        list1.addAll(grammer.production.get(j).value.get(0));
//                        list1.addAll(list.subList(1, list.size()));          //重组后的产生式右部
//
//                        replaceAll(grammer.production.get(i).value, list, list1);
//
//                        for (int m = 1; m < grammer.production.get(j).value.size(); m++) {
//                            list1 = new ArrayList<>();
//                            list1.addAll(grammer.production.get(j).value.get(m));
//                            list1.addAll(list.subList(1, list.size()));
//                            grammer.production.get(i).value.add(list1);
//                        }
//                    }
//                }
//            }

            Production production = grammer.production.get(i);
            boolean flag = false;
            for (int j = 0; j < production.value.size(); j++) {    //消除左递归
                String left = production.value.get(j).get(0);    //右部的首符
                Production production1 = new Production();
                List<String> temp = new ArrayList<>();
                if (left.equals(production.key)) {                      //存在直接左递归
                    flag = true;    //存在左递归
                    production1.key = production.key + "‘";
                    temp.addAll(production.value.get(j).subList(1, production.value.get(j).size()));   //P‘的产生式 消除左递归
                    temp.add(production1.key);
                    production1.value.add(temp);          //加入产生式右部集合
                    grammer.production.add(production1);
                    temp = new ArrayList<>();
                    result.production.add(production1);
                } else if (flag) {
                    production1.key = production.key + "‘";
                    temp.addAll(production.value.get(j).subList(0, production.value.get(j).size()));
                    temp.add(production1.key);
                    production1.value.add(temp);
                    grammer.production.add(production1);
                    temp = new ArrayList<>();
                    result.production.add(production1);
                }
                if (!flag) {
                    for (List<String> list : production.value) {
                        production1.key = production.key;
                        production1.value.add(list);
                        result.production.add(production1);
                        production1 = new Production();
                    }
                }
            }
        }


        //去除重复的产生式
        int sum = 0;
        Grammer g = new Grammer();
        for (int i = 0; i < result.production.size(); i++) {
            boolean flag = false;
            for (int k = 0; k < i; k++) {
                if (result.production.get(i).equal(result.production.get(k))) {
                    flag = true;
                    sum++;
                    break;
                }
            }
            if (!flag)
                g.production.add(result.production.get(i));
        }

        File file = new File("src/com/directory/grammer5.txt");
        if (!file.exists())
            file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Production production : g.production) {
            writer.write(production.key);
            for (String str : production.value.get(0))
                writer.write(" " + str);
            writer.write("\n");
        }
        writer.close();

//        if (result.production.size() - sum == g.production.size())
//            System.out.println("OK");
//        System.out.println(sum);
//        display(result);
//        display(g);


    }


}
