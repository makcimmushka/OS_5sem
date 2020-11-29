public class sProcess {
    public int cpuTime;
    public int delay;
    public int cpuDone;
    public int ionext;
    public int numBlocked;

    public sProcess(int cpuTime, int delay, int cpuDone, int ionext, int numBlocked) {
        this.cpuTime = cpuTime;
        this.delay = delay;
        this.cpuDone = cpuDone;
        this.ionext = ionext;
        this.numBlocked = numBlocked;
    }
}
