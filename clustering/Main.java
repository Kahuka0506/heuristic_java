import java.awt.*;
import javax.swing.*;
import java.util.*;


class Problem{
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


class Solver{
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



public class Main extends JPanel{
    int N = 200;
    int X = 700;
    int Y = 700;
    int K = 12;
    Problem problem = new Problem(N,K,X,Y,42);
    Solver solver = new Solver(problem);
    
    int finish = 0;
    int R = 10;
    
    
    public Main() {
        setPreferredSize(new Dimension(X,Y));
        Thread th = new AnimeThread();
        th.start();
    }

    
    void set_color(Graphics g, int i, int N){
        if(i < N/4) g.setColor(new Color(240,(240*i)/N*4,0));
        else if(i < N*2/4) g.setColor(new Color(240-(240*(i-N/4))/N*4,240,0));
        else if(i < N*3/4) g.setColor(new Color(0,240-(240*(i-N*2/4))/N*4,(240*(i-N*2/4))/N*4));
        else g.setColor(new Color((240*(i-N*3/4))/N*4,0,240-(240*(i-N*3/4))/N*4));
        
    }
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawString("N="+problem.N+"  K="+problem.K + "  loop="+solver.loop + "  cost="+solver.score, X/2, 10);
        
        
        
        for(int i = 0; i < N; i++){
            set_color(g,solver.cluster.get(i), K);
            g.fillOval(problem.x.get(i)-R,problem.y.get(i)-R,R,R);
        }
        
        for(int i = 0; i < K; i++){
            g.setColor(Color.black);
            g.fillOval(solver.Cx.get(i)-(int)(R*1.3), solver.Cy.get(i)-(int)(R*1.3),(int)(R*1.3),(int)(R*1.3));
        }
        
        for(int i = 0; i < 100; i++){
            solver.SA();
        }
    }

    class AnimeThread extends Thread {
        public void run() {
            while(finish == 0) {
                repaint();
                if(solver.loop >= 30000) {
                    finish = 1;
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch(Exception e) {
                }
            }
        }
    }

    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Main());
        f.pack();
        f.setTitle("Clustering");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}



