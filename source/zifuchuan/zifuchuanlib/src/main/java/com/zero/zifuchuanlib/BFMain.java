package com.zero.zifuchuanlib;

public class BFMain {

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
        bruteForce(mainString, subString);
        System.out.println("BF diff: " + (System.nanoTime() - start));
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
     * @param s 主串
     * @param p 子串
     */
    public static void bruteForce(String s, String p) {
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
            System.out.println("=============第[" + count + "]回合================");
            System.out.println(getSpaceByNumber(i)+"| => " + i);
            count++;
            System.out.println(s);
            System.out.println(getSpaceByNumber(i-j) + p);
            if (s.charAt(i) == p.charAt(j)) {// 判断对应位置的字符是否相等
                equalschars.append(p.charAt(j));
                System.out.println(getSpaceByNumber(i-j) + equalschars);
                System.out.println("S[" + i + "]{" + s.charAt(i) + "} == P[" + j + "]{" + p.charAt(j) + "}");
                i++;// 若相等，主串、子串继续依次比较
                j++;
                System.out.println("因此");
                System.out.println("i++ =" + i);
                System.out.println("j++ =" + j);
            } else {// 若不相等
                equalschars = new StringBuffer("");
                System.out.println("S[" + i + "]{" + s.charAt(i) + "} != P[" + j + "]{" + p.charAt(j) + "}");
                System.out.println("因此");
                //如果不回溯
                i = i + 1;
                System.out.println("i =" + i + " + 1 = " + (i + 1));
//                int prei = i;
//                i = i - j + 1;// 主串回溯到上次开始匹配的下一个字符
//                System.out.println("i =" + prei + " - " + j + " + 1 = " + (prei -j + 1));
                j = 0;// 子串从头开始重新匹配
                System.out.println("j = 0");
            }
            System.out.println();
        }
        if (j >= pLength) {// 匹配成功
            System.out.println("j = " + j + " >= tLength =" + pLength);
            index = i - j;
            System.out.println("index = i - j =" + index);
            System.out.println("Successful match,index is:" + index);
        } else {// 匹配失败
            System.out.println("i = " + i + " ,j = " + j + ", pLength: " + pLength);
            System.out.println("Match failed.");
        }
    }




}
