public class ReferenceTest {

    public static void main(String[] args) {
        ReferenceTest r = new ReferenceTest();
        // reset integer
        r.i = 0;
        System.out.println("Before changeInteger:" + r.i);
        changeInteger(r);
        System.out.println("After changeInteger:" + r.i);
        // just for format
        System.out.println();
        // reset integer
        r.i = 0;
        System.out.println("Before changeReference:" + r.i);
        changeReference(r);
        System.out.println("After changeReference:" + r.i);
    }

    private static void changeReference(ReferenceTest r) {
        r = new ReferenceTest();
        r.i = 5;
        System.out.println("In changeReference: " + r.i);
    }

    private static void changeInteger(ReferenceTest r) {
        r.i = 5;
        System.out.println("In changeInteger:" + r.i);
    }

    public int i;
}
