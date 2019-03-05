package projectObjects;

import java.util.Arrays;

/**
Activity class

 @author    Robert Brachmann
 @version   1.0

The activity object/class is a superclass to inherent subclasses
for different scheduling problems and solving algorithms
 */
public class Activity {

    // Activity id
    public Integer activityId;
    // Activity mode
    public int mode;
    // Number of successors
    public int numberSuccessors;
    // List of successors
    public Integer[] successors;
    // List of predecessors
    public Integer[] predecessors;
    // Activity duration
    public int duration;
    // Activity consumptions
    public Integer[] consumptions;
    // Activity's start
    public Integer start;
    // Activity's earliest finish
    public Integer finish;
    // Activity's earliest start
    public Integer earliestStart;
    // Activity's earliest finish
    public Integer earliestFinish;
    // Activity's latest start
    public Integer latestStart;
    // Activity's latest finish
    public Integer latestFinish;
    // Activity scheduled
    public boolean scheduled;
    // Activity eligible
    public boolean eligible;
    // Activity list position
    private Integer activityListPosition;

    // Constructor
    public Activity(Integer activityId, int mode, Integer[] successors, Integer[] predecessors,
                    int duration, Integer[] consumptions, int numberSuccessors) {
        this.activityId = activityId;
        this.mode = mode;
        this.successors = successors;
        this.predecessors = predecessors;
        this.duration = duration;
        this.consumptions = consumptions;
        this.numberSuccessors = numberSuccessors;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", activityDuration=" + duration +
                ", successors=" + Arrays.toString(successors) +
                ", predecessors=" + predecessors +
                ", consumptions=" + Arrays.toString(consumptions) +
                ", earliestStart=" + earliestStart +
                ", earliestFinish=" + earliestFinish +
                ", latestStart=" + latestStart +
                ", latestFinish=" + latestFinish +
                ", scheduled=" + scheduled +
                ", eligible=" + eligible +
                '}';
    }

    public  String activityToString(){
        return  "Activity:          " + this.activityId + "\n" +
                "Activity list pos: " + this.activityListPosition + "\n" +
                "Duration:          " + this.duration + "\n" +
                "Consumption:       " + Arrays.toString(this.consumptions) + "\n" +
                "Predecessors:      " + Arrays.toString(this.predecessors) + "\n" +
                "Start:             " + this.start +  "\n" +
                "Finish:            " + this.finish +  "\n" +
                "Earliest start:    " + this.earliestStart +  "\n" +
                "Earliest finish:   " + this.earliestFinish + "\n" +
                "Latest start:      " + this.latestStart + "\n" +
                "Latest finish:     " + this.latestFinish + "\n" +
                "Scheduled:         " + this.scheduled + "\n" +
                "Eligible:          " + this.eligible + "\n\n";
    }

    public int getActivityId() {
        return activityId;
    }

    public Integer getActivityListPosition() {
        return activityListPosition;
    }

    public void setActivityListPosition(Integer activityListPosition) {
        this.activityListPosition = activityListPosition;
    }
}

