import java.lang.*;
import java.util.*;

class Keyboard{
    public static void main(String[] args) {
        System.out.println("Enter any number");
        Scanner s=new Scanner(System.in);
        int a,b,c;
        a = s.nextInt();
        b = s.nextInt();
        c= a+b;
        System.out.println("The some is "+ c);
    }
}