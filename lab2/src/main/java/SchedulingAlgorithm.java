// Run() is called from scheduling.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
// ;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.io.*;


public class SchedulingAlgorithm {

  private enum ProcessState {
    REGISTERED,
    COMPLETED,
    JOINED,
    FORCEDOUT
  }

  private static void print(int time, PrintStream out, ProcessState state, sProcess process, Vector<sProcess> processVector) {
    print(time, out, state, process, processVector, false);
  }

  private static void print(int time, PrintStream out, ProcessState state, sProcess process, Vector<sProcess> processVector, boolean quantumExpired) {
    out.println(time + "ms ");
    switch (state) {
      case REGISTERED:
        out.println("Process: " + processVector.indexOf(process) + " registered... (" + process.cpuTime + " " + process.delay + " " + process.cpuDone + ")");
        break;
      case COMPLETED:
        if (quantumExpired) {
          out.println("Quantum time expired");
        } else {
          out.println("Quantum time reset");
        }
        out.println("Process: " + processVector.indexOf(process) + " completed... (" + process.cpuTime + " " + process.delay + " " + process.cpuDone + ")");
        break;
      case JOINED:
        out.println("Process joined queue");
        out.println("Process: " + processVector.indexOf(process) + " (" + process.cpuTime + " " + process.delay + " " + process.cpuDone + ")");
        break;
      case FORCEDOUT:
        out.println("Quantum time expired");
        out.println("Process: " + processVector.indexOf(process) + " forced out... (" + process.cpuTime + " " + process.delay + " " + process.cpuDone + ")");
        break;
    }

  }

  public static Results Run(int quantum, int runtime, Vector<sProcess> processVector, Results result) {
    int comptime = 0;
    int size = processVector.size();
    int completed = 0;

    String resultsFile = "Summary-Processes";

    Vector<sProcess> sortedProcessVector = (Vector<sProcess>) processVector.clone();
    sortedProcessVector.sort((o1, o2) -> {
      if (o1.delay == o2.delay) {
        return 0;
      } else if (o1.delay > o2.delay) {
        return 1;
      } else {
        return -1;
      }
    });

    Queue<sProcess> queue = new LinkedList<>();

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "Round Robin scheduling";

    int lastProcessIndex = 0;
    sProcess process = null;
    int quantumCounter = 0;

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      while (comptime < runtime) {
        //add entered processes
        if (lastProcessIndex < size) {
          while (sortedProcessVector.elementAt(lastProcessIndex).delay == comptime) {
            sProcess processCopy = sortedProcessVector.elementAt(lastProcessIndex);
            queue.add(processCopy);
            print(comptime, out, ProcessState.JOINED, processCopy, processVector);
            ++lastProcessIndex;
            if (lastProcessIndex >= size) {
              break;
            }
          }
        }

        if (process == null && !queue.isEmpty()) {
          process = queue.peek();
          print(comptime, out, ProcessState.REGISTERED, process, processVector);
        }

        if (process != null && process.cpuDone == process.cpuTime) {
          completed++;

          print(comptime, out, ProcessState.COMPLETED, process, processVector, quantumCounter == quantum);

          quantumCounter = 0;

          if (completed == size) {
            result.compuTime = comptime;
            return result;
          }

          queue.remove();

          if (!queue.isEmpty()) {
            process = queue.peek();
            print(comptime, out, ProcessState.REGISTERED, process, processVector);
          } else {
            process = null;
          }
        }

        //if quantum time expired
        if (quantumCounter == quantum) {
          quantumCounter = 0;
          process.numBlocked++;

          print(comptime, out, ProcessState.FORCEDOUT, process, processVector, true);

          queue.add(queue.poll());
          process = queue.peek();

          print(comptime, out, ProcessState.REGISTERED, process, processVector);
        }

        if (process != null) {
          quantumCounter++;
          process.cpuDone++;
        }

        comptime++;
      }

      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    result.compuTime = comptime;
    return result;
  }
}