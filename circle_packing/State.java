
import java.awt.*;
import javax.swing.*;
import java.util.*;



class Node implements Cloneable{
    double r;
    double x;
    double y;
    Node(double r, double x, double y){
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    @Override
    public Node clone() {
        Node node = new Node(this.r, this.x, this.y);
        return node;
    }
}

class State{
    int N;
    ArrayList<Node> nodes = new ArrayList<>();
    Random rand = new Random();
    double cost = 0;
    double x_max;
    double y_max;
    
    State(int N, int X, int Y, int Rmax, int Rmin, long seed){
        this.N = N;
        this.x_max = X;
        this.y_max = Y;
        Random r = new Random(seed);
        for(int i = 0; i < N; i++){
            double ra = r.nextDouble()*(Rmax-Rmin) + Rmin;
            nodes.add(new Node(ra,-1,-1));
        }
        //nodes.sort((a,b)-> {
        //    if(a.r < b.r) return -1;
        //    else if(a.r > b.r) return 1;
        //    else return 0;
        //} );
        
        
        init(X,Y);
        cost = computeCost();
    }
    State(State state){
        this.N = state.N;
        for(int i = 0; i < this.N; i++){
            nodes.add(state.nodes.get(i).clone());
            //nodes.add(state.nodes.get(i));
        }
        //System.out.println(nodes + " " + state.nodes);
        this.cost = state.cost;
    }
    void init(int X, int Y){
        double r_max = 0;
        for(int i = 0; i < N; i++){
            if(r_max < nodes.get(i).r) r_max = nodes.get(i).r;
        }
        double x = 0, y = r_max;
        for(int i = 0; i < N; i++){
            if(x+nodes.get(i).r*2 > X){
                x = 0;
                y += r_max*2;
            }
            x += nodes.get(i).r;
            nodes.get(i).x = x;
            nodes.get(i).y = y;
            x += nodes.get(i).r+0.1;
        }
    }
    
    double distance(int i, int j){
        return Math.sqrt((nodes.get(i).x-nodes.get(j).x)*(nodes.get(i).x-nodes.get(j).x) + (nodes.get(i).y-nodes.get(j).y)*(nodes.get(i).y-nodes.get(j).y));
    }
    boolean isVaild(){
        for(int i = 0; i < N; i++){
            if(nodes.get(i).x-nodes.get(i).r < 0) return false;
            if(nodes.get(i).y-nodes.get(i).r < 0) return false;
            for(int j = i+1; j < N; j++){
                if(distance(i,j) < nodes.get(i).r+nodes.get(j).r){
                    return false;
                }
            }
        }
        return true;
    }
    
    double computeCost(){
        x_max = 0.0;
        y_max = 0.0;
        for(int i = 0; i < N; i++){
            if(x_max < nodes.get(i).x+nodes.get(i).r) x_max = nodes.get(i).x+nodes.get(i).r;
            if(y_max < nodes.get(i).y+nodes.get(i).r) y_max = nodes.get(i).y+nodes.get(i).r;
        }
        return cost = x_max*y_max;
    }
    
    
    
    void getNeigborhood(){
        for(int aa = 0; aa < 10; aa++){
            
            int n = rand.nextInt(N);
            int xy = rand.nextInt(2);
            double d = rand.nextDouble()*30.0;
            int pm = rand.nextInt(2);
            if(pm == 0) pm = -1;
            
            for(int ii = 0; ii < 300; ii++){
                if(xy == 0) this.nodes.get(n).x += d*pm;
                else this.nodes.get(n).y += d*pm;
                
                if(isVaild()) break;
                else {
                    if(xy == 0) this.nodes.get(n).x -= d*pm;
                    else this.nodes.get(n).y -= d*pm;
                    d *= 0.5;
                }
            }
            
            
        }
    }
    void shake(){
        for(int i = 0; i < N; i++){
            this.nodes.get(i).x += 40;
            this.nodes.get(i).y += 40;
        }
    }
    
    State createNewState(){
        State newState = new State(this);
        return newState;
    }
}







