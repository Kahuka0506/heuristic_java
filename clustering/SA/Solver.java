
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
        cluster = update_cluster();
    }
    
    long compute_d(int ni, int ki){
        long a = (long)(x.get(ni)-Cx.get(ki));
        long b = (long)(y.get(ni)-Cy.get(ki));
        return (long)(Math.sqrt(a*a + b*b));
    }
    ArrayList<Integer> update_cluster(){
        score = 0;
        ArrayList<Integer> res = new ArrayList<>();
        for(int i = 0; i < N; i++){
            long d = 100000000;
            int id = -1;
            for(int k = 0; k < K; k++){
                long dd = compute_d(i,k);
                if(d > dd) {
                    d = dd;
                    id = k;
                }
            }
            score += d;
            res.add(id);
        }
        return res;
    }
    
    int xy_log, n_log, d_log;
    ArrayList<Integer> cluster_log;
    long score_log;
    
    long neigbor(){
        cluster_log = cluster;
        score_log = score;
        
        int n = rand.nextInt(K);
        int d = rand.nextInt(30);
        if(rand.nextInt(2) == 0) d *= -1;
        
        if(rand.nextInt(2) == 0) {
            int a = Cx.get(n)+d;
            Cx.set(n, a);
            xy_log = 0;
            d_log = d;
            n_log = n;
        }else{
            int a = Cy.get(n)+d;
            Cy.set(n, a);
            xy_log = 1;
            d_log = d;
            n_log = n;
        }
        
        cluster = update_cluster();
        return score;
    }
    
    void rollback(){
        cluster = cluster_log;
        score = score_log;
        if(xy_log == 0) {
            int a =  Cx.get(n_log)-d_log;
            Cx.set(n_log, a);
        }else{
            int a = Cy.get(n_log)-d_log;
            Cy.set(n_log, a);
        }
    }
    
    
    
    double t = 100.0;
    void SA(){
        loop++;
        
        long score0 = score;
        long score1 = neigbor();

        if(score1 <= score0){
            
        }else{
            if(rand.nextDouble() <= Math.exp((score0-score1)/t)) {
                
            }else rollback();
        }
        
        t *= 0.999;
        if(loop%5000 == 0) t = 10.0;
        if(loop%100 == 0) System.out.println(loop + " " + score + " " + t);
    }
}







