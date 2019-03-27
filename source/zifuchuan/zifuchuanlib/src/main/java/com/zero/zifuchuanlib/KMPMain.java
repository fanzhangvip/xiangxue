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

    public static String SPACE = " ";

    public static String getSpaceByNumber(int number) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < number; i++) {
            sb.append(SPACE);
        }
        return sb.toString();
    }



    /**
     * @param p 要生成next[]数组的字符串，在KMP算法中是子串（模式串）
     * @return 给定字符串的next[]数组
     */
    public static int[] next(String p) {
        int[] next = new int[p.length()];
        next[0] = -1;// 这里规定next[]第1个元素值为-1
        next[1] = 0;
        int j = 2;// 从给定的字符串的第3个字符(下标从0开始)开始计算next[j]数值
        while (j < p.length()) {// 从第三个字符开始逐次求解对应的next[]值
            if (next[j - 1] != 0 && p.charAt(next[j - 1]) == p.charAt(j - 1)) {// 判断Y与X是否相等（X,Y对应上图，下同）
                next[j] = next[j - 1] + 1;// 若Y与X相等，当前next[j]为上一个next[]数组值加1
                j++;// 开始自增一个字符，准备下一次求解
            } else if (p.charAt(0) == p.charAt(j - 1)) {// 若不相等，判断X与子串（模式串）第一个字符是否相同
                next[j] = 1;// 若相同，找到一个长度为1的相同前后缀，即next[j]为1
                j++;
            } else {// 若上述两个条件都不满足，即意味着没有相等的前后缀，即next[j]为0
                next[j] = 0;
                j++;
            }
        System.out.println(Arrays.toString(next));
        }
        return next;
    }

    public static int[] buildNext(String p){
        int[] N = new int[p.length()];
        int m = p.length(),j = 0;//主串位置
        int t = N[0] = -1;//字串位置
        while(j < m -1){
            if(t <0 || p.charAt(j) == p.charAt(t)){
                j++;t++;
                N[j] = t;
            }else{//失配
                t = N[t];
            }
        }
        return N;
    }

    public static int[] buildNext1(String p){
        int[] N = new int[p.length()];
        int m = p.length(),j = 0;//主串位置
        int t = N[0] = -1;//字串位置
        while(j < m -1){
            if(t <0 || p.charAt(j) == p.charAt(t)){
                j++;t++;
                N[j] = t;
            }else{//失配
                t = N[t];
            }
        }
        return N;
    }

    public static String getNextPre(String p,int preNumber){
        StringBuffer sb = new StringBuffer("");
        for(int i = 0; i < preNumber;i++){
            sb.append(p.charAt(i));
        }
        return sb.toString();
    }

    public static void kmp(String s, String p) {
        int[] next = next(p);// 调用next（String p）方法
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


}
