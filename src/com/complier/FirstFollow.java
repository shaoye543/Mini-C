package com.complier;

import java.io.*;

/**
 * @Author: Shaoye
 * @Date: 2020/8/26
 * @ClassName: FirstFollow
 **/

public class FirstFollow {

    /**
     * ����ʽ
     */
    private class Production {
        String left;       //����ս��
        String[] right;    //��

        Production(String left, String[] right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {

        BufferedReader reader = null;
        try {
            String line;
            reader = new BufferedReader(new FileReader(new File("E:\\Code\\Java\\����ԭ��\\src\\com\\shaoye\\grammer.txt")));
            while ((line=reader.readLine())!=null)
                System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
