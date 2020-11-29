public class Process {
  public int cputime;
  public int delay;
  public int cpudone;
  public int ionext;
  public int numblocked;

  public Process(int cputime, int delay, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.delay = delay;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  } 	
}
