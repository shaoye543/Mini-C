package com.tool;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: EliminateLeftRecursion
 **/

public class EliminateLeftRecursion {

    /**
     * ����ʽ��
     */
    private static class Production {
        private String key;          //��
        private String[] value;

        Production(String key, String[] value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * �ķ���
     */
    private static class Grammer{
        private String[] vn;      //���ս������
        private String[] vt;      //�ս������
        private Production[] production;  //����ʽ
        private String start;             //��ʼ��
        public Grammer(String[] vn, String[] vt, Production[] production, String start) {
            this.vn = vn;
            this.vt = vt;
            this.production = production;
            this.start = start;
        }
    }



}
