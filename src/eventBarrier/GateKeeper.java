package eventBarrier;

public class GateKeeper implements Runnable{

    private int raiseTime = 0;
    @Override
    public void run () {
        // TODO Auto-generated method stub
        
    }
    public int getRaiseTime () {
        return raiseTime;
    }
    public void setRaiseTime (int raiseTime) {
        this.raiseTime = raiseTime;
    }
    
}