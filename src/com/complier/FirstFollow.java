package com.complier;


import com.pojo.Grammar;
import com.pojo.Production;

import java.util.*;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: FirstFollow
 **/

public class FirstFollow {

    static Map<String, Set<String>> first = new HashMap<>();
    static Map<String, Set<String>> firstTerminal = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();
    static Grammar grammar = new Grammar("src/com/directory/消除左递归.txt");

    static {    //初始化
        Set<String> set = new HashSet<>();
        for (Production production : grammar.production)
            first.put(production.key, set);
        for (String string : grammar.vt)
            first.put(string, new HashSet<>(Collections.singletonList(string)));   //非终结符的first集合将其本身置入
    }

    static void createFirst() {
        while (true) {
            boolean flag = false;
            for (Production production : grammar.production) {
                Set<String> set = new HashSet<>(first.get(production.key));
                int length = set.size();        //当前first集合的元素数量
                for (String[] strings : production.value) {
                    if (strings[0] == null) break;
                    if (!grammar.isNonTerminal(strings[0]))     //第一个为终结符 加入first集合
                        set.add(strings[0]);
                    else {                                          //第一个是非终结符的情况
                        List<String> temp = new ArrayList<>(first.get(strings[0]));
                        temp.removeIf(s -> s.equals("ε"));          //去除first集合中的ε
                        set.addAll(temp);                           //第一个是非终结符时 将first(strings[0])的非ε元素放入
                        if (first.get(strings[0]).contains("ε")) {
                            int num = 0;                  //记录产生式右部各元素的first集合中有ε的数量
                            for (String string : strings) {
                                if (!first.get(string).contains("ε")) {
                                    //前面的first都有ε 则将该元素的first中非ε元素置入
                                    temp = new ArrayList<>(first.get(strings[0]));
                                    temp.removeIf(s -> s.equals("ε"));
                                    set.addAll(temp);
                                    break;
                                }
                                num++;
                            }
                            if (num == strings.length) set.add("ε");
                        }
                    }
                }


                first.put(production.key, set);
                if (first.get(production.key).size() > length)
                    flag = true;
//                System.out.println("===================================================================");
//                System.out.println(set.size() + " " + length);
//                for (String key : first.keySet()) {
//                    if (grammar.vn.contains(key)) {
//                        System.out.print(key + " ");
//                        System.out.println(first.get(key));
//                    }
//                }
//                System.out.println(flag);
//                System.out.println("===================================================================");
            }
            if (!flag) break;
        }
    }

    void createFollow(){

    }


    public static void main(String[] args) {

        createFirst();
    }

}
