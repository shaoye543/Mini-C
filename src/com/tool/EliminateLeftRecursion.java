package com.tool;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: EliminateLeftRecursion
 **/

public class EliminateLeftRecursion {

    /**
     * 产生式类
     */
    private static class Production {
        private String key;          //左
        private String[] value;

        Production(String key, String[] value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 文法类
     */
    private static class Grammer{
        private String[] vn;      //非终结符集合
        private String[] vt;      //终结符集合
        private Production[] production;  //产生式
        private String start;             //起始符
        public Grammer(String[] vn, String[] vt, Production[] production, String start) {
            this.vn = vn;
            this.vt = vt;
            this.production = production;
            this.start = start;
        }
    }



}
