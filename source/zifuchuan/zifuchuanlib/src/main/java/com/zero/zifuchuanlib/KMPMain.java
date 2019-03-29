package com.zero.zifuchuanlib;

import java.util.Arrays;

public class KMPMain {

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

        long start = System.nanoTime();
        kmp(mainString, subString);
        System.out.println("KMP diff: " + (System.nanoTime() - start));
    }

    public static int[] buildNext(String p) {//求子串的真前缀 = 真后缀的最大长度t
        int[] N = new int[p.length()];
        for (int k = 0; k < N.length; k++) {
            N[k] = -2;
        }
        int m = p.length(), j = 0;//主串位置
        int t = N[0] = -1;//字串位置
        System.out.println(Arrays.toString(N));
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        System.out.println("t = " + t);
        System.out.println("m -1 = " + (m - 1));
        int count = 0;
        while (j < m - 1) {
            System.out.println("=============第[" + count + "]回合================");
            count++;
            System.out.println(p);
            System.out.println("主串位置:j = " + j +" < " +( m-1));
            System.out.println("p_t:" + p.substring(0, j+1));
            System.out.println("子串位置:t = " + t);
            if (t < 0 || p.charAt(j) == p.charAt(t)) {
                if (t < 0) {
                    System.out.println("因为t = " + t + " < 0");
                } else {
                    System.out.println("P[" + j + "]{" + p.charAt(j) + "} == P[" + t + "]{" + p.charAt(t) + "}");
                }
                j++;
                t++;
                System.out.println("j++ = " + j);
                System.out.println("t++ = " + t);
                N[j] = t;
                System.out.println("N[" + j + "] = " + t);
                System.out.println(Arrays.toString(N));
            } else {//失配
                System.out.println("P[" + j + "]{" + p.charAt(j) + "} != P[" + t + "]{" + p.charAt(t) + "}");
                int pret = t;
                t = N[t];
                System.out.println("t = N[" + pret + "] = " + t);
                System.out.println(Arrays.toString(N));
            }
        }
        System.out.println("1: " + Arrays.toString(N));
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
        System.out.println("sLength: " + sLength + " ,pLength:" + pLength);
        int i = 0;
        int j = 0;
        int count = 0;
        StringBuffer equalschars = new StringBuffer();
        while (i < sLength && j < pLength) {
            /*
             * 如果j = -1， 或者当前字符匹配成功（即s.charAt(i) == p.charAt(j)）， 都令i++，j++
             */
            System.out.println("=============第[" + count + "]回合================");
            count++;
            System.out.println(getSpaceByNumber(i)+"| => " + i);
            System.out.println(s);
            System.out.println(getSpaceByNumber(i-j) + p);
            if (j == -1 || s.charAt(i) == p.charAt(j)) {
                if(j == -1){
                    System.out.println("因为j = -1");
                }else{
                    equalschars.append(p.charAt(j));
                    System.out.println(getSpaceByNumber(i-j) + equalschars);
                    System.out.println("S[" + i + "]{" + s.charAt(i) + "} == P[" + j + "]{" + p.charAt(j) + "}");
                }
                i++;
                j++;
                System.out.println("因此");
                System.out.println("i++ =" + i);
                System.out.println("j++ =" + j);
            } else {
                equalschars = new StringBuffer("");
                /*
                 * 如果j != -1，且当前字符匹配失败， 则令 i 不变，j = next[j], next[j]即为j所对应的next值
                 */
                System.out.println("S[" + i + "]{" + s.charAt(i) + "} != P[" + j + "]{" + p.charAt(j) + "}");
                int prej = j;
                j = next[j];
                equalschars.append(getNextPre(p,j));
                System.out.println("因此");
                System.out.println("i不变 =" + i);
                System.out.println("j = next["+prej+"] = " + j);
            }
        }
        if (j >= pLength) {// 匹配成功
            index = i - j;
            System.out.println("Successful match,index is:" + index);
        } else {// 匹配失败
            System.out.println("Match failed.");
        }
    }


    public static String SPACE = " ";

    public static String getSpaceByNumber(int number) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < number; i++) {
            sb.append(SPACE);
        }
        return sb.toString();
    }

    public static String getNextPre(String p,int preNumber){
        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < preNumber;i++){
            sb.append(p.charAt(i));
        }
        return sb.toString();
    }

}
