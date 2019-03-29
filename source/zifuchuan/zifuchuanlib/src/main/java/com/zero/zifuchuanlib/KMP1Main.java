package com.zero.zifuchuanlib;

import java.util.Arrays;

public class KMP1Main {

    public static void main(String... args) {
        System.out.println("Hello 字符串匹配算法...");

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please input main string:");
//        String mainString = scanner.nextLine();// 从标准输入读取主串
//        System.out.println("Please input sub string:");
//        String subString = scanner.nextLine();// 从标准输入读取子串
//        scanner.close();
        String mainString = "BBC ABCDAB ABCDABCDABDE";
        String subString = "ABCDABD";
        System.out.println("主串");
        System.out.println(mainString);
        System.out.println("子串");
        System.out.println(subString);

        kmp(mainString, subString);
    }

    public static int[] buildNext(String p) {//求子串的真前缀 = 真后缀的最大长度t
        int[] N = new int[p.length()];
        for (int k = 0; k < N.length; k++) {
            N[k] = -2;
        }
        int m = p.length(), j = 0;//主串位置
        int t = N[0] = -1;//字串位置
        while (j < m - 1) {
            if (t < 0 || p.charAt(j) == p.charAt(t)) {
                j++;
                t++;
                N[j] = t;
            } else {//失配
                t = N[t];
            }
        }
        return N;
    }


    public static void kmp(String s, String p) {
        int[] next = buildNext(p);// 调用next（String p）方法
        int index = -1;// 成功匹配的位置
        int sLength = s.length();// 主串长度
        int pLength = p.length();// 子串长度
        if (sLength < pLength) {
            System.out.println("Error.The main string is greater than the sub string length.");
            return;
        }
        int i = 0;
        int j = 0;
        while (i < sLength && j < pLength) {
            /*
             * 如果j = -1， 或者当前字符匹配成功（即s.charAt(i) == p.charAt(j)）， 都令i++，j++
             */
            if (j == -1 || s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                /*
                 * 如果j != -1，且当前字符匹配失败， 则令 i 不变，j = next[j], next[j]即为j所对应的next值
                 */
                j = next[j];
            }
        }
        if (j >= pLength) {// 匹配成功
            index = i - j;
            System.out.println("Successful match,index is:" + index);
        } else {// 匹配失败
            System.out.println("Match failed.");
        }
    }

}
