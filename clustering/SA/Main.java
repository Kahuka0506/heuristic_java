import java.awt.*;
import javax.swing.*;
import java.util.*;





public class Main extends JPanel{
    int N = 500;
    int X = 700;
    int Y = 700;
    int K = 20;
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



