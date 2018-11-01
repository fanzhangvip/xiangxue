public class KotlinJava {

    public static void vid() {

        for (int i = 2; i >= 0; --i) {
            System.out.println("i= " + i);
        }

    }

    public static void main(String... args) {

//        ByteArrayPool pool = new ByteArrayPool(10000);
//        byte[] lasts = null;
//        for (int i = 1; i <= 100; i++) {
//            byte[] bytes = pool.getBuf(i * 10);
//            fillData(bytes, Integer.valueOf(i).byteValue());
//            lasts = bytes;
//            pool.returnBuf(lasts);
//        }

        String str2 = new String("str")+new String("01");
        str2.intern();
        String str1 = "str01";
        System.out.println(str2==str1);

    }

    public static void fillData(byte[] data, byte b) {
        System.out.println("data: " + data + " b: " + b);
        for (int i = 0; i < data.length; i++) {
            data[i] = b;
        }
    }
}
