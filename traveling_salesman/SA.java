import java.awt.*;
import javax.swing.*;
import java.util.*;







class SA{
    double t = 20.0;
    double c = 0.99;
    Random rand = new Random();
    int loop = 0;
    
    
    State update(State state0){
        loop++;
        State state1 = state0.createNewState();
        state1.getNeigborhood();
        
        
        double cost0 = state0.computeCost();
        double cost1 = state1.computeCost();
        double d = cost1 - cost0;
        t *= c;
        
        if(d <= 0){
            state0 = state1;
        }else{
            if(rand.nextDouble() <= Math.exp(-d/t)) state0 = state1;
        }
        
        return state0;
    }
    
}


