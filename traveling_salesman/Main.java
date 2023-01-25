import java.awt.*;
import javax.swing.*;
import java.util.*;




public class Main extends JPanel{
    int N = 300;
    int H = 700;
    int W = 700;
    int R = 5;
    int finish = 0;
    State state = new State(N,H,W,R,42);
    SA optimizer = new SA();

    
    public Main() {
        setPreferredSize(new Dimension(H,W));
        Thread th = new AnimeThread();
        th.start();
    }

    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        //g.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,20));
        g.drawString("cost="+state.computeCost(), W*2/3, 30);
        g.drawString("loop="+optimizer.loop, W*2/3, 50);
        
        for(int i = 0; i < N; i++){
            if(i < N/4) g.setColor(new Color(240,(240*i)/N*4,0));
            else if(i < N*2/4) g.setColor(new Color(240-(240*(i-N/4))/N*4,240,0));
            else if(i < N*3/4) g.setColor(new Color(0,240-(240*(i-N*2/4))/N*4,(240*(i-N*2/4))/N*4));
            else g.setColor(new Color((240*(i-N*3/4))/N*4,0,240-(240*(i-N*3/4))/N*4));
            
            int id = state.idx.get(i);
            int id1 = state.idx.get((i+1)%N);
            g.fillOval((int)(state.nodes.get(id).h), (int)(state.nodes.get(id).w), R*2, R*2);
            g.drawLine((int)(state.nodes.get(id).h)+R, (int)(state.nodes.get(id).w)+R, (int)(state.nodes.get(id1).h)+R, (int)(state.nodes.get(id1).w)+R);
        }
        //g.setColor(Color.black);
        //g.fillOval((int)(state.nodes.get(0).h), (int)(state.nodes.get(0).w), R*3, R*3);
        
        
        for(int i = 0; i < 2; i++){
            state = optimizer.update(state);
        }
        if(optimizer.loop%1 == 0) System.out.println(optimizer.loop + " " + state.computeCost() + " " + optimizer.t);
        
    }

    class AnimeThread extends Thread {
        public void run() {
            while(finish == 0) {
                repaint();
                if(optimizer.loop >= 3000) {
                    finish = 1;
                    break;
                }
                try {
                    Thread.sleep(10);
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
        f.setTitle("Traveling Salesman Problem");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}
