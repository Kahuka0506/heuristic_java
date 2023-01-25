
import java.awt.*;
import javax.swing.*;
import java.util.*;





public class Solver{
    int N,K;
    Random rand = new Random();
    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();
    ArrayList<Integer> Cx = new ArrayList<>();
    ArrayList<Integer> Cy = new ArrayList<>();
    ArrayList<Integer> cluster = new ArrayList<>();
    long score = 1000000;
    int loop = 0;
    
    
    public Solver(Problem problem){
        this.N = problem.N;
        this.K = problem.K;
        this.x = problem.x;
        this.y = problem.y;
        for(int i = 0; i < K; i++){
            Cx.add(rand.nextInt(problem.X));
            Cy.add(rand.nextInt(problem.Y));
        }
        for(int i = 0; i < N; i++) cluster.add(0);
        update_cluster();
    }
    
    long compute_d(int ni, int ki){
        long a = (long)(x.get(ni)-Cx.get(ki));
        long b = (long)(y.get(ni)-Cy.get(ki));
        return (long)(Math.sqrt(a*a + b*b));
    }
    void update_cluster(){
        score = 0;
        for(int i = 0; i < N; i++){
            long d = 100000000;
            int id = 0;
            for(int k = 0; k < K; k++){
                long dd = compute_d(i,k);
                if(d > dd) {
                    d = dd;
                    id = k;
                }
            }
            score += d;
            cluster.set(i,id);
        }
    }
    
    
    
    void k_mean(){
        loop++;
        ArrayList<Integer> ave_x = new ArrayList<>();
        ArrayList<Integer> ave_y = new ArrayList<>();
        ArrayList<Integer> cnt = new ArrayList<>();
        for(int i = 0; i < K; i++){
            ave_x.add(0);
            ave_y.add(0);
            cnt.add(0);
        }
        for(int i = 0; i < N; i++){
            int n = cluster.get(i);
            int xx = ave_x.get(n) + x.get(i);
            ave_x.set(n,xx);
            int yy = ave_y.get(n) + y.get(i);
            ave_y.set(n,yy);
            int cc = cnt.get(n) + 1;
            cnt.set(n,cc);
        }
        for(int i = 0; i < K; i++){
            if(cnt.get(i) == 0) continue;
            Cx.set(i,(int)(ave_x.get(i)/cnt.get(i)));
            Cy.set(i,(int)(ave_y.get(i)/cnt.get(i)));
        }
         
        
        update_cluster();
        
        System.out.println(loop + " " + score);
    }
    
}







