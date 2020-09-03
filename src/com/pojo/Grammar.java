package com.pojo;


import java.io.*;
import java.util.*;

public class Grammar {

    public Set<String> vn;      //非终结符集合
    public Set<String> vt;      //终结符集合
    public List<Production> production = new ArrayList<>();  //产生式
    public String start;             //起始符

    /**
     * 是否为非终结符
     *
     * @param str
     * @return
     */
    public boolean isNonTerminal(String str) {
        for (String string : this.vn)
            if (str.equals(string))
                return true;
        return false;
    }

    public Grammar(String filepath) {
        BufferedReader reader = null;
        this.vt = new HashSet<>();         //存放终结符
        try {
            reader = new BufferedReader(new FileReader(new File(filepath)));
            String line;
            int num = 0;
            Production production = new Production();
            this.vn = new HashSet<>();               //存放非终结符
            while ((line = reader.readLine()) != null) {
                String[] symbol = line.split("\\s+");
                if (production.key == null || !production.key.equals(symbol[0])) {
                    num = 0;
                    this.production.add(production);
                    production = new Production();
                    production.key = symbol[0];
                    this.vn.add(symbol[0]);        //非终结符集合
                }
                production.key = symbol[0];
                production.value[num] = new String[symbol.length - 1];    //初始化 避免多余的无用空间
                System.arraycopy(symbol, 1, production.value[num++], 0, symbol.length - 1);   //数组元素复制 产生式右部
                this.vt.addAll(Arrays.asList(symbol));             //将所有字符放入一并处理
            }
            this.production.add(production);
            this.start = this.production.get(0).key;        //第一个作为起始符
            this.production.remove(0);        //第一个添加了多余的空
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
        this.vt.removeIf(this::isNonTerminal);              //将非终结符剔除
    }


    public void display() {
        for (Production production : this.production) {
            for (int i = 0; production.value[i][0] != null; i++) {
                System.out.print(production.key);
                for (String string : production.value[i])
                    System.out.print(" " + string);
                System.out.println();
            }

        }
    }
}
