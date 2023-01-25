import java.awt.*;
import javax.swing.*;
import java.util.*;




class Node{
    double h;
    double w;
    Node(double h, double w){
        this.h = h;
        this.w = w;
    }
    
    double computeDistance(Node nextNode){
        double dh = this.h-nextNode.h;
        double dw = this.w-nextNode.w;
        return Math.sqrt(dh*dh + dw*dw);
    }
}



class State{
    int N;
    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Integer> idx = new ArrayList<>();
    Random rand = new Random();
    double cost = 0;
    
    State(int N, int H, int W, int R, long seed){
        this.N = N;
        Random r = new Random(seed);
        for(int i = 0; i < N; i++){
            double h = r.nextDouble()*(H-R*2);
            double w = r.nextDouble()*(W-R*2);
            nodes.add(new Node(h,w));
        }
        init();
        cost = computeCost();
    }
    State(State state){
        this.N = state.N;
        for(int i = 0; i < this.N; i++){
            nodes.add(state.nodes.get(i));
            idx.add(state.idx.get(i));
        }
        this.cost = state.cost;
    }
    
    void init(){
        //for(int i = 0; i < N; i++) idx.add(i);
        
        ArrayList<Integer> used = new ArrayList<>();
        for(int i = 0; i < N; i++) used.add(0);
        idx.add(0);
        
        for(int i = 0; i < N-1; i++){
            int u = idx.get(i);
            used.set(u,1);
            
            int nextId = -1;
            double d_max = 100000000;
            for(int j = 0; j < N; j++) if(used.get(j) == 0) {
                double d = nodes.get(u).computeDistance(nodes.get(j));
                if(d < d_max){
                    d_max = d;
                    nextId = j;
                }
            }
            idx.add(nextId);
        }
    }
    
    
    
    
    
    
    void swap(int a, int b){
        Integer a0 = idx.get(a);
        Integer b0 = idx.get(b);
        idx.set(a,b0);
        idx.set(b,a0);
    }
    void getNeigborhood_insert(){
        int x = rand.nextInt(N);
        int a = idx.get(x);
        idx.remove(x);
        idx.add(a);
        x = rand.nextInt(N);
        swap(x,N-1);
    }
    void getNeigborhood_randomSwap(){
        int x = rand.nextInt(N-1);
        swap(x,x+1);
    }
    void getNeigborhood_randomSwap1(){
        int x = rand.nextInt(N);
        int y = rand.nextInt(N);
        while(x == y) y = rand.nextInt(N);
        swap(x,y);
    }
    void getNeigborhood_2opt(){
        int x = rand.nextInt(N-1);
        int y = x+1+rand.nextInt(N-x-1);
        x++;
        while(x < y){
            swap(x,y);
            x++;
            y--;
        }
    }
    void getNeigborhood_2opt_fast(){
        int x = rand.nextInt(N);
        int y = rand.nextInt(N);
        for(int a = 0; a < N; a++){
            y = (y+1)%N;//rand.nextInt(N);
            if(x == y) continue;
            double d0 = nodes.get(idx.get(x)).computeDistance(nodes.get(idx.get((x+1)%N)))
            + nodes.get(idx.get(y)).computeDistance(nodes.get(idx.get((y+1)%N)));
            double d1 = nodes.get(idx.get(x)).computeDistance(nodes.get(idx.get(y)))
            + nodes.get(idx.get((y+1)%N)).computeDistance(nodes.get(idx.get((x+1)%N)));
            
            if(d0 > d1) break;
        }
        if(x > y) {
            int xx = x;
            x = y;
            y = x;
        }
        x++;
        while(x < y){
            swap(x,y);
            x++;
            y--;
        }
    }
    void getNeigborhood_orOpt(){
        int x = rand.nextInt(N-3);
        int y = x+1+rand.nextInt(N-2-x);
        int z = y+1+rand.nextInt(N-1-y);
        ArrayList<Integer> idx1 = new ArrayList<>();
        for(int i = 0; i <= x; i++) idx1.add(idx.get(i));
        for(int i = y+1; i <= z; i++) idx1.add(idx.get(i));
        for(int i = y; i > x; i--) idx1.add(idx.get(i));
        for(int i = z+1; i < N; i++) idx1.add(idx.get(i));
        idx = idx1;
    }
    
    void getNeigborhood_2opt_all(){
        for(int a = 0; a < N; a++){
            for(int b = a+1; b < N; b++){
                int x = a;
                int y = b;
                double d0 = nodes.get(idx.get(x)).computeDistance(nodes.get(idx.get((x+1)%N)))
                + nodes.get(idx.get(y)).computeDistance(nodes.get(idx.get((y+1)%N)));
                double d1 = nodes.get(idx.get(x)).computeDistance(nodes.get(idx.get(y)))
                + nodes.get(idx.get((y+1)%N)).computeDistance(nodes.get(idx.get((x+1)%N)));
                if(d0 < d1) continue;
                x++;
                while(x < y){
                    swap(x,y);
                    x++;
                    y--;
                }
            }
        }
    }
    
    
    void kick_DoubleBridge(){
        int a = rand.nextInt(N-4);
        int b = a+1+rand.nextInt(N-3-a);
        int c = b+1+rand.nextInt(N-2-b);
        int d = c+1+rand.nextInt(N-1-c);
        ArrayList<Integer> idx1 = new ArrayList<>();
        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
        for(int i = c+1; i <= d; i++) idx1.add(idx.get(i));
        for(int i = b+1; i <= c; i++) idx1.add(idx.get(i));
        for(int i = a+1; i <= b; i++) idx1.add(idx.get(i));
        for(int i = d+1; i < N; i++) idx1.add(idx.get(i));
        idx = idx1;
    }
    
    
    void getNeigborhood(){
        //kick_DoubleBridge();
        //getNeigborhood_2opt_all();
        
        if(rand.nextInt(13) == 0){
            kick_DoubleBridge();
        }else{
            int n = rand.nextInt(10);
            if(n == 0) getNeigborhood_randomSwap();
            else if(n == 1) getNeigborhood_randomSwap1();
            else if(n == 2) getNeigborhood_insert();
            else if(n <= 4) getNeigborhood_orOpt();
            else getNeigborhood_2opt_fast();
        }
    }
    
    double computeCost(){
        double res = 0.0;
        for(int i = 0; i < N; i++){
            res += nodes.get(idx.get(i)).computeDistance(nodes.get(idx.get((i+1)%N)));
        }
        return cost = res;
    }
    
    State createNewState(){
        State newState = new State(this);
        return newState;
    }
}







class SA{
    double T = 0;
    double t = 20.0;
    double c = 0.9999;
    Random rand = new Random();
    int loop = 0;
    
    
    State update(State state0){
        loop++;
        State state1 = state0.createNewState();
        state1.getNeigborhood();
        
        
        double cost0 = state0.computeCost();
        double cost1 = state1.computeCost();
        double d = cost1 - cost0;
        //T += 0.01;
        //t = 12.0/(Math.exp((T-700)*0.03)+1.0);
        t *= c;
        
        if(d <= 0){
            state0 = state1;
        }else{
            if(rand.nextDouble() <= Math.exp(-d/t)) state0 = state1;
        }
        if(loop % 100000 == 0) t = 10;
        return state0;
    }
    
}



















public class Visualizer extends JPanel{
    int N = 300;
    int H = 700;
    int W = 700;
    int R = 5;
    int finish = 0;
    State state = new State(N,H,W,R,42);
    SA optimizer = new SA();

    
    public Visualizer() {
        setPreferredSize(new Dimension(H,W));
        Thread th = new AnimeThread();
        th.start();
    }

    
    public void paintComponent(Graphics g) {
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
        g.setColor(Color.black);
        //g.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,20));
        //g.drawString("cost="+state.cost, W*2/3, 30);
        //g.drawString("loop="+optimizer.loop, W*2/3, 60);
    }

    class AnimeThread extends Thread {
        public void run() {
            while(finish == 0) {
                for(int i = 0; i < 1000; i++){
                    state = optimizer.update(state);
                }
                if(optimizer.loop%100 == 0) System.out.println(optimizer.loop + " " + state.computeCost() + " " + optimizer.t);
                
                repaint();
                if(optimizer.loop >= 500000-1) {
                    finish = 1;
                    repaint();
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
        f.getContentPane().add(new Visualizer());
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}
