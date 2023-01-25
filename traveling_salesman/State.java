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
    
    
    
    
    
    double distance(int a, int b){
        a %= N;
        b %= N;
        return nodes.get(idx.get(a)).computeDistance(nodes.get(idx.get(b)));
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
    
    void getNeigborhood_1opt_all(){
        for(int a = 0; a < N; a++){
            for(int b = 0; b < N; b++){
                if(a == b || Math.abs(a-b) == 1 ||  Math.abs(a-b) == N-1) continue;
                double d0 = nodes.get(idx.get(a)).computeDistance(nodes.get(idx.get((a+1)%N)))
                + nodes.get(idx.get((a+1)%N)).computeDistance(nodes.get(idx.get((a+2)%N)))
                + nodes.get(idx.get(b)).computeDistance(nodes.get(idx.get((b+1)%N)));
                double d1 = nodes.get(idx.get(a)).computeDistance(nodes.get(idx.get((a+2)%N)))
                + nodes.get(idx.get(b)).computeDistance(nodes.get(idx.get((a+1)%N)))
                + nodes.get(idx.get((a+1)%N)).computeDistance(nodes.get(idx.get((b+1)%N)));
                if(d0 < d1) continue;
                
                ArrayList<Integer> idx1 = new ArrayList<>();
                for(int i = 0; i < N; i++){
                    if(i == (a+1)%N) continue;
                    else if(i == b){
                        idx1.add(idx.get(i));
                        idx1.add(idx.get((a+1)%N));
                    }else idx1.add(idx.get(i));
                }
                idx = idx1;
            }
        }
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
    
    void getNeigborhood_3opt_all(){
        for(int a = 0; a < N; a++){
            for(int c = a+2; c < N; c++){
                for(int e = c+2; e < N; e++){
                    int b = a+1;
                    int d = c+1;
                    int f = e+1;
                    //[...A-B...C-D...E-F...]
                    double d0 = distance(a, b) + distance(c, d) + distance(e, f);
                    double d1 = distance(a, c) + distance(b, d) + distance(e, f);
                    if(d0 > d1){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = c; i >= b; i--) idx1.add(idx.get(i));
                        for(int i = d; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    d1 = distance(a, c) + distance(b, e) + distance(d, f);
                    if(d0 > d1){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = c; i >= b; i--) idx1.add(idx.get(i));
                        for(int i = e; i >= d; i--) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    double d2 = distance(a, b) + distance(c, e) + distance(d, f);
                    if(d0 > d2){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= c; i++) idx1.add(idx.get(i));
                        for(int i = e; i >= d; i--) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    d2 = distance(a, d) + distance(c, e) + distance(b, f);
                    if(d0 > d2){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = d; i <= e; i++) idx1.add(idx.get(i));
                        for(int i = c; i >= b; i--) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    double d3 = distance(a, d) + distance(e, b) + distance(c, f);
                    if(d0 > d3){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = d; i <= e; i++) idx1.add(idx.get(i));
                        for(int i = b; i <= c; i++) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    //[...A-B...C-D...E-F...]
                    double d4 = distance(f, b) + distance(c, d) + distance(e, a);
                    if(d0 > d3){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = e; i >= b; i--) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                    d4 = distance(f, c) + distance(b, d) + distance(e, a);
                    if(d0 > d3){
                        ArrayList<Integer> idx1 = new ArrayList<>();
                        for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                        for(int i = e; i >= d; i--) idx1.add(idx.get(i));
                        for(int i = b; i <= c; i++) idx1.add(idx.get(i));
                        for(int i = f; i < N; i++) idx1.add(idx.get(i));
                        idx = idx1;
                        continue;
                    }
                }
            }
        }
    }
    
    
    void getNeigborhood_orOpt_all(){
        for(int a = 0; a < N; a++){
            for(int b = a+2; b < N; b++){
                for(int c = b+2; c < N; c++){
                    double d0 = nodes.get(idx.get(a)).computeDistance(nodes.get(idx.get((a+1)%N)));
                    d0 += nodes.get(idx.get(b)).computeDistance(nodes.get(idx.get((b+1)%N)));
                    d0 += nodes.get(idx.get(c)).computeDistance(nodes.get(idx.get((c+1)%N)));
                    double d1 = nodes.get(idx.get(a)).computeDistance(nodes.get(idx.get((b+1)%N)));
                    d1 += nodes.get(idx.get(c)).computeDistance(nodes.get(idx.get((b)%N)));
                    d1 += nodes.get(idx.get((a+1)%N)).computeDistance(nodes.get(idx.get((c+1)%N)));
                    if(d0 < d1) continue;
                    ArrayList<Integer> idx1 = new ArrayList<>();
                    for(int i = 0; i <= a; i++) idx1.add(idx.get(i));
                    for(int i = b+1; i <= c; i++) idx1.add(idx.get(i));
                    for(int i = b; i > a; i--) idx1.add(idx.get(i));
                    for(int i = c+1; i < N; i++) idx1.add(idx.get(i));
                    idx = idx1;
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
        kick_DoubleBridge();
        //if(rand.nextInt(15) == 0) kick_DoubleBridge();
         
        getNeigborhood_2opt_all();
        //if(rand.nextInt(30) == 0)getNeigborhood_3opt_all();
        if(rand.nextInt(10) == 0) getNeigborhood_1opt_all();
        if(rand.nextInt(20) == 0) getNeigborhood_orOpt_all();
        getNeigborhood_2opt_all();
        
        
        
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







