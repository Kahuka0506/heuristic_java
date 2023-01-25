import java.awt.*;
import javax.swing.*;
import java.util.*;



class SA{
    double t = 733.0;
    double c = 0.99;
    Random rand = new Random();
    int loop = 0;
    
    
    State update(State state0){
        loop++;
        State state1 = state0.createNewState();
        state1.getNeigborhood();
        double cost0 = state0.computeCost();
        double cost1 = state1.computeCost();
        
        //System.out.println(state0 + " " + cost0 + " | " + state1 + " " + cost1);
        //System.out.println(cost0 + " " + cost1);
        double d = cost1 - cost0;
        t *= c;
        if(loop%5000 == 0) t = 350;
        if(d <= 0){
            state0 = state1;
        }else{
            if(rand.nextDouble() <= Math.exp(-d/t)) state0 = state1;
        }
        
        //if(loop%10000 == 0){
        //    state0.shake();
        //}
        return state0;
    }
    
}


















