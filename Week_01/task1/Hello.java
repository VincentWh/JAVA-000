package sky.week01;

public class Hello {
    public static void main(String[] args) {
        int a = 1;
        int b = 3;
        for (int i = 0; i < b; i++) {
            a += i;
        }
        if (4 == a) {
            a = a + 6;
        }
        System.out.println(a);
    }
}
