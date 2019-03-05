package frcpspf.geneticAlgorithm;

/**
 *  @author    Robert Brachmann
 *  @version   1.0
 */
public class Gene{

    // Activity id
    private Integer activityId;

    // Activity mode
    private int mode;

    // Number of successors
    private int numberSuccessors;

    // List of successors
    private Integer[] successors;

    // List of predecessors
    private Integer[] predecessors;

    // Activity duration
    private int duration;

    // Activity consumptions
    private Integer[] consumptions;

    // Activity's start
    private Integer start;

    // Activity's earliest finish
    private Integer finish;

    // Activity's earliest start
    private Integer earliestStart;

    // Activity's earliest finish
    private Integer earliestFinish;

    // Activity's latest start
    private Integer latestStart;

    // Activity's latest finish
    private Integer latestFinish;

    // Activity scheduled
    private boolean scheduled;

    // Activity eligible
    private boolean eligible;

    // Activity list position
    private Integer activityListPosition;

    // Constructor
    public Gene(Integer activityId){
        super();
        this.activityId = activityId;
    }

    // toString override
    @Override
    public String toString(){
        return "Gene id:        " + this.activityId;
    }

}
