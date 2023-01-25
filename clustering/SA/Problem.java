import java.awt.*;
import javax.swing.*;
import java.util.*;




public class Problem{
    int N,K,X,Y;
    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();
    
    Problem(int N, int K, int X, int Y, int seed){
        this.N = N;
        this.K = K;
        this.X = X;
        this.Y = Y;
        Random r = new Random((long)42);
        for(int i = 0; i < N; i++){
            x.add(r.nextInt((int)(X*0.9))+(int)(X*0.05));
            y.add(r.nextInt((int)(Y*0.9))+(int)(Y*0.05));
        }
    }
}






