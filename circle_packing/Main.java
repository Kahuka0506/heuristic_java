import java.awt.*;
import javax.swing.*;
import java.util.*;





public class Main extends JPanel{
    int N = 50;
    int H = 700;
    int W = 700;
    int Rmax = 50;
    int Rmin = 10;
    int finish = 0;
    State state = new State(N,H,W,Rmax,Rmin,42);
    SA optimizer = new SA();

    
    public Main() {
        setPreferredSize(new Dimension(H,W));
        Thread th = new AnimeThread();
        th.start();
    }

    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawString("cost="+state.cost, W*2/3, 30);
        g.drawString("loop="+optimizer.loop, W*2/3, 60);
        //System.out.println("cost : " + state.cost);
        
        for(int i = 0; i < N; i++){
            if(i < N/4) g.setColor(new Color(240,(240*i)/N*4,0));
            else if(i < N*2/4) g.setColor(new Color(240-(240*(i-N/4))/N*4,240,0));
            else if(i < N*3/4) g.setColor(new Color(0,240-(240*(i-N*2/4))/N*4,(240*(i-N*2/4))/N*4));
            else g.setColor(new Color((240*(i-N*3/4))/N*4,0,240-(240*(i-N*3/4))/N*4));
            
            g.fillOval((int)(state.nodes.get(i).x)-(int)(state.nodes.get(i).r), W-(int)(state.nodes.get(i).y)-(int)(state.nodes.get(i).r), (int)(state.nodes.get(i).r)*2, (int)(state.nodes.get(i).r)*2);
            
            //g.fillOval((int)(state.nodes.get(i).x)-(int)(state.nodes.get(i).r), W-(int)(state.nodes.get(i).y)-(int)(state.nodes.get(i).r), (int)(state.nodes.get(i).r)*2, (int)(state.nodes.get(i).r)*2);
        }
        g.setColor(Color.red);
        g.drawLine((int)(state.x_max), W-(int)(state.y_max), -10, W-(int)(state.y_max));
        g.drawLine((int)(state.x_max), W+10, (int)(state.x_max), W-(int)(state.y_max));
        
        
        for(int i = 0; i < 100; i++){
            state = optimizer.update(state);
        }
        if(optimizer.loop%1 == 0) System.out.println(optimizer.loop + " " + state.computeCost() + " " + optimizer.t);
        
    }

    class AnimeThread extends Thread {
        public void run() {
            while(finish == 0) {
                repaint();
                if(optimizer.loop >= 200000) {
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
        f.setTitle("Circle Packing Problem");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}



