import java.awt.*;
import javax.swing.*;
import java.util.*;

class Graph{
    int N;
    int X,Y;
    final int inf = 1000000000;
    Random rand = new Random();
    int[][] G;
    int[] x;
    int[] y;
    
    public Graph(int N, int X, int Y){
        this.N = N;
        this.X = X;
        this.Y = Y;
        G = new int[N][N];
        x = new int[N];
        y = new int[N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++) G[i][j] = inf;
            x[i] = 0;
            y[i] = 0;
        }
        random_layout();
    }
    public void graph_generator(){
        Random r = new Random();
        int e = r.nextInt(N)+N;
        for(int i = 0; i < e; i++){
            int a = r.nextInt(N);
            int b = r.nextInt(N);
            while(a == b) b = r.nextInt(N);
            G[a][b] = 1;
            G[b][a] = 1;
        }
    }
    public void add_edge(int u, int v){
        G[u][v] = 1;
    }
    
    public void random_layout(){
        for(int i = 0; i < N; i++){
            x[i] = rand.nextInt((int)(X*0.9)) + (int)(X*0.05);
            y[i] = rand.nextInt((int)(Y*0.9)) + (int)(Y*0.05);
        }
    }
    
    
    public void fruchterman_reingold_algorithm(){
        
    }
    int loop = 0;
    double T = 10.0;
    public void fruchterman_reingold_algorithm_update(){
        double[] Fx = new double[N];
        double[] Fy = new double[N];
        for(int i = 0; i < N; i++){
            Fx[i] = 0;
            Fy[i] = 0;
        }
        double k = 1.0*(Math.sqrt(((double)(X*Y)/10/(double)N)));
        
        for(int i = 0; i < N; i++){
            for(int j = i+1; j < N; j++){
                if(G[i][j] == 1 || G[j][i] == 1){
                    int dx = x[j]-x[i];
                    int dy = y[j]-y[i];
                    double d = Math.max((Math.sqrt(dx*dx + dy*dy)),0.01);
                    double f = d*d/k;
                    Fx[i] += f*dx/d;
                    Fy[i] += f*dy/d;
                    Fx[j] -= f*dx/d;
                    Fy[j] -= f*dy/d;
                }else{
                    int dx = -x[j]+x[i];
                    int dy = -y[j]+y[i];
                    double d = Math.max((Math.sqrt(dx*dx + dy*dy)),0.01);
                    double f = k*k/d;
                    Fx[i] += f*dx/d;
                    Fy[i] += f*dy/d;
                    Fx[j] -= f*dx/d;
                    Fy[j] -= f*dy/d;
                }
            }
        }
        for(int i = 0; i < N; i++){
            double F = Math.max(Math.sqrt(Fx[i]*Fx[i] + Fy[i]*Fy[i]),0.00001);
            //System.out.println(i + " " + Fx[i] + " " + Fy[i]);
            
            x[i] += (int)(Fx[i]/F*Math.min(F,T));
            y[i] += (int)(Fy[i]/F*Math.min(F,T));
            if(x[i] < 0) x[i] = 0;
            else if(x[i] > X) x[i] = X;
            if(y[i] < 0) y[i] = 0;
            else if(y[i] > Y) y[i] = Y;
        }
        T *= 0.99;
        loop++;
    }
}



public class Main extends JPanel{
    int N = 200;
    int X = 700;
    int Y = 700;
    Graph graph = new Graph(N,X,Y);
    int finish = 0;
    int R = 10;
    
    
    public Main() {
        setPreferredSize(new Dimension(X,Y));
        graph.graph_generator();
        
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
        //g.drawString("N="+problem.N+"  K="+problem.K + "  loop="+solver.loop + "  cost="+solver.score, X/2, 10);
        
        
        g.setColor(Color.black);
        for(int i = 0; i < N; i++){
            for(int j = i; j < N; j++){
                if(graph.G[i][j] == 1 || graph.G[j][i] == 1){
                    g.drawLine(graph.x[i], graph.y[i], graph.x[j], graph.y[j]);
                }
            }
        }
        
        for(int i = 0; i < N; i++){
            g.setColor(Color.black);
            g.fillOval(graph.x[i]-R/2, graph.y[i]-R/2, R, R);
        }
        
        for(int i = 0; i < 3; i++) graph.fruchterman_reingold_algorithm_update();
        System.out.println(graph.loop  + " " + graph.T);
        /*
        for(int i = 0; i < 100; i++){
            solver.SA();
        }
         */
    }

    class AnimeThread extends Thread {
        public void run() {
            while(finish == 0) {
                repaint();
                if(graph.loop > 700) {
                    finish = 1;
                    break;
                }
                try {
                    Thread.sleep(50);
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



