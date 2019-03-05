package projectObjects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 Project class

 @author    Robert Brachmann
 @version   1.0

 The project object/class is a superclass to inherent subclasses
 for different scheduling problems and solving algorithms
 */
public class Project {


    public int projectId;
    public ArrayList<Activity> listOfActivities;
    public ArrayList<Resource> listOfResources;
    protected int dueDate;
    public int completion;
    private int planningHorizon;
    public ArrayList<Integer> activityList;
    public  ArrayList<Integer> modeList;
    public ArrayList<Integer> randomKey;
    private Integer[] activitySequence;

    public Project(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return  "Project:       " + projectId + "\n" +
                "Activities:    " + listOfActivities.size() + "\n" +
                "Resources:     " + listOfResources.size() + "\n" +
                "Dead line:     " + dueDate + "\n" +
                "Completion:    " + completion + "\n" +
                "Horizon:       " + planningHorizon + "\n\n";
    }

    /**
     * Generates full project report
     * @return String that contains project information
     */
    public String projectToString(int resourceDisplayFrom, int resourceDisplayTo){

        // Use StringBuilder
        StringBuilder resultString = new StringBuilder();

        for (Activity activity : this.listOfActivities){
            resultString.append(activity.activityToString());
        }

        resultString.append(
                "\n" +
                "Project:           " + this.projectId + "\n" +
                "Due date:          " + this.dueDate + "\n" +
                "Completion:        " + this.completion + "\n" +
                "Activity list:     " + this.activityList + "\n\n");

        for (Resource resource:this.listOfResources) {
            resultString.append("Resource " + resource.getResourceId() + " limit: " + " " + resource.resourceUpperBound + "\n");
        }

        resultString.append("\n");

        for (Resource resource:this.listOfResources) {
            resultString.append("Resource " + resource.toStringResourceProfile(50) +  "\n");
        }

        return resultString.toString();
    }

    /**
     * Read instance of PSPLib
     * and pass data to project object
     * @param filePath PspLib file
     * @throws Exception just for debugging
     */
    public void readPspLibInstance(String filePath) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        skip(br, 5);
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(br.readLine());

        // Number of activities
        int numberActivities = 0;
        if (m.find()) {
            numberActivities = Integer.parseInt(m.group());
        }

        // Planning horizon
        skip(br, 0);
        m = p.matcher(br.readLine());
        if (m.find()) {
            this.planningHorizon = Integer.parseInt(m.group());
        }

        // Number of resources
        skip(br, 1);
        m = p.matcher(br.readLine());
        int numberResources = 0;
        if (m.find()) {
            numberResources = Integer.parseInt(m.group());
        }

        // Due date
        skip(br, 5);
        String[] line = br.readLine().split("\\s+");
        this.dueDate = Integer.parseInt(line[4]);

        // Lists for activity objects
        skip(br, 3);
        List<Integer> activityId = new ArrayList<>();
        List<Integer> activityMode = new ArrayList<>();
        List<Integer> numberSuccessors = new ArrayList<>();
        ArrayList<Integer[]> successors = new ArrayList<>();
        ArrayList<Integer[]> predecessors;
        List<Integer> duration = new ArrayList<>();
        List<Integer[]> consumption = new ArrayList<>();
        List<Integer> tmpSuccessor = new ArrayList<>();

        //tmp var
        int tmpNumSuccessors;

        for (int i = 0; i < numberActivities; i++) {
            line = br.readLine().split("\\s+");
            activityId.add(Integer.parseInt(line[1]));
            activityMode.add(Integer.parseInt(line[2]));
            tmpNumSuccessors = Integer.parseInt(line[3]);
            numberSuccessors.add(tmpNumSuccessors);

            if (tmpNumSuccessors == 0) {
                successors.add(new Integer[0]);
            } else {
                tmpSuccessor.clear();
                for (int k = 0; k < tmpNumSuccessors; k++) {
                    tmpSuccessor.add(Integer.parseInt(line[4 + k]));
                }
                Integer tmpSuccessorArr[] = tmpSuccessor.toArray(new Integer[tmpSuccessor.size()]);
                successors.add(tmpSuccessorArr);
            }
        }

        // Set duration and consumption
        skip(br, 4);
        for (int i = 0; i < numberActivities; i++) {
            line = br.readLine().split("\\s+");
            duration.add(Integer.parseInt(line[3]));
            Integer[] res = {Integer.parseInt(line[4]), Integer.parseInt(line[5]),
                    Integer.parseInt(line[6]), Integer.parseInt(line[7])};
            consumption.add(res);
        }

        // Set predecessors
       predecessors = successorToPredecessor(successors);

        // Create list of activity objects
        ArrayList<Activity> tmpActivityList = new ArrayList<>();
        for (int l = 0; l < numberActivities; l++) {
            Activity tmpActivity = new Activity(activityId.get(l),
                    activityMode.get(l),
                    successors.get(l),
                    predecessors.get(l),
                    duration.get(l),
                    consumption.get(l),
                    numberSuccessors.get(l));
            tmpActivityList.add(tmpActivity);
        }
        this.listOfActivities = tmpActivityList;

        // Set resources
        skip(br, 3);
        line = br.readLine().split("\\s+");
        ArrayList tmpResourceList = new ArrayList();
        for (int i = 0; i < numberResources; i++) {
            tmpResourceList.add(new Resource(i + 1, Integer.parseInt(line[i + 1]), this.planningHorizon));
        }
        this.listOfResources = tmpResourceList;

        // set random precedence feasible activity list
        this.randomActivityList();
        this.setActivityListPosByActivityList();

        // Set empty activity sequence
        this.activitySequence = new Integer[this.listOfActivities.size()];
        Arrays.fill(this.activitySequence, 0);

        br.close();
    }

    /**
     * Derive predecessors from successors
     * @param successors
     * @return
     */
    private ArrayList<Integer[]> successorToPredecessor(ArrayList<Integer[]> successors){
        ArrayList<Integer[]> predecessors = new ArrayList<>();
        for (Integer i = 0; i < successors.size(); i++){
            ArrayList<Integer> tmpPredecessors = new ArrayList();
            for (int j = 0; j < successors.size(); j++){
                if (i != j){
                    if (Arrays.asList(successors.get(j)).contains(i + 1)){
                        tmpPredecessors.add(j + 1);
                    }
                }
            }
            Integer[] tmpArray = new Integer[tmpPredecessors.size()];
            tmpArray = tmpPredecessors.toArray(tmpArray);
            predecessors.add(tmpArray);
        }
        return predecessors;
    }

    /**
     * Serial schedule generator scheme with activity list representation
     */
    public void serialScheduleGeneratorSchemeActivityList(){
        // Reset all necessary values
        this.resetSchedule();

        // Loop through all activities
        for (int i = 0; i < this.listOfActivities.size(); i++){

            // Select eligible activity by activity list position
            Integer selectedActivityId = this.selectEligibleActivityByActivityList();
            Integer selectedActivityIndex = selectedActivityId - 1;

            // Set activity sequence
            this.setActivitySequence(selectedActivityIndex, i);

            // Calculate and set earliest start
            this.setEarliestPrecedenceFeasibleStart(selectedActivityIndex);

            // Calculate and set earliest resource feasible start
            this.setEarliestResourceFeasibleStart(selectedActivityIndex);

            // Derive finish
            this.listOfActivities.get(selectedActivityIndex).finish =
                    this.listOfActivities.get(selectedActivityIndex).start +
                            this.listOfActivities.get(selectedActivityIndex).duration;
            this.listOfActivities.get(selectedActivityIndex).scheduled = true;

            // Set resource
            this.setResourceConsumption(selectedActivityIndex);
        }

        // Set completion
        this.completion = this.listOfActivities.get(this.activityList.size() - 1).finish;
    }

    /**
     * Serial schedule generator scheme with random key representation
     */
    private void serialScheduleGeneratorSchemeRandomKey(){

    }

    /**
     * Sets instance variables and returns array list of activity indices
     * @return ArrayList of eligible activities / activity indices
     */
    private ArrayList<Integer> eligibleActivitiesIndices(){
        ArrayList<Integer> eligibleActivities = new ArrayList<>();
        for(int i = 0; i < this.listOfActivities.size(); i++){
            Activity currentActivity = this.listOfActivities.get(i);
            if (!currentActivity.scheduled){
                currentActivity.eligible = true;
                if (currentActivity.predecessors.length < 1){
                    eligibleActivities.add(i);
                }else{
                    for(int j = 0; j < currentActivity.predecessors.length; j++){
                        if (!this.listOfActivities.get(currentActivity.predecessors[j] - 1).scheduled){
                            currentActivity.eligible = false;
                        }
                    }
                    if (currentActivity.eligible == true){
                        eligibleActivities.add(i);
                    }
                }
            }
        }
        return eligibleActivities;
    }

    /**
     * Set earliest precedence feasible start
     */
    private void setEarliestPrecedenceFeasibleStart(Integer activityIndex){
        Integer earliestStart = 0;
        Integer[] predecessors = this.listOfActivities.get(activityIndex).predecessors;
        for (Integer predecessor : predecessors){
            if (this.listOfActivities.get(predecessor - 1).finish > earliestStart){
                earliestStart = this.listOfActivities.get(predecessor - 1).finish;
            }
        }
        this.listOfActivities.get(activityIndex).earliestStart = earliestStart;
    }

    /**
     * Set earliest resource feasible start
     * between earliestStart and planningHorizon
     * @param activityIndex
     */
    private void setEarliestResourceFeasibleStart(Integer activityIndex){
        // Boolean resource feasible
        Boolean feasible = false;

        // Duration of current activity
        Integer duration = this.listOfActivities.get(activityIndex).duration;

        // For each period i from earliestStart to planningHorizon
        for (int i = this.listOfActivities.get(activityIndex).earliestStart; i < this.planningHorizon; i++){

            // Break if dummy activity
            if (this.listOfActivities.get(activityIndex).duration == 0){
                this.listOfActivities.get(activityIndex).start = i;
                break;
            }

            // Check for each resource, feasibility from period i to period i + duration of activity
            innerLoop:{
                for (int r = 0; r < this.listOfResources.size(); r++) {
                    for (int j = i; j <= i + duration; j++) {
                        if(this.listOfResources.get(r).resourceProfile[j] +
                                this.listOfActivities.get(activityIndex).consumptions[r] <=
                                this.listOfResources.get(r).resourceUpperBound){
                            feasible = true;
                        }else{
                            // If one period-resource-combination is not feasible, break innerLoop
                            feasible = false;
                            break innerLoop;
                        }
                    }
                }
            }

            // Check if current period i is resource feasible
            if (feasible){
                this.listOfActivities.get(activityIndex).start = i;
                break;
            }
        }
    }

    /**
     * Set resource consumption of activity according to start period
     * @param activityIndex
     */
    private void setResourceConsumption(Integer activityIndex){

        // For each resource
        for (int r = 0; r < this.listOfResources.size(); r++){
            if (this.listOfActivities.get(activityIndex).consumptions[r] != 0){
                // For periods i between start and finish of activity
                for (int i = this.listOfActivities.get(activityIndex).start;
                     i < this.listOfActivities.get(activityIndex).finish; i++){
                    this.listOfResources.get(r).resourceProfile[i] =
                            this.listOfResources.get(r).resourceProfile[i] +
                                    this.listOfActivities.get(activityIndex).consumptions[r];
                }
            }
        }
    }

    /**
     * Set activity sequence
     * @param activityIndex
     * @param sequencePosition
     */
    private void setActivitySequence(Integer activityIndex,Integer sequencePosition){
        this.activitySequence[sequencePosition]=this.listOfActivities.get(activityIndex).getActivityId();
    }

    /**
     * Select eligible activity by activity list
     * @return activityId
     */
    private Integer selectEligibleActivityByActivityList(){

        // Get eligible activity
        ArrayList<Activity> eligibleActivities = new ArrayList<>();
        for (Activity currentActivity : this.listOfActivities) {
            if (!currentActivity.scheduled) {
                currentActivity.eligible = true;
                if (currentActivity.predecessors.length < 1) {
                    eligibleActivities.add(currentActivity);
                } else {
                    for (int j = 0; j < currentActivity.predecessors.length; j++) {
                        if (!this.listOfActivities.get(currentActivity.predecessors[j] - 1).scheduled) {
                            currentActivity.eligible = false;
                        }
                    }
                    if (currentActivity.eligible) {
                        eligibleActivities.add(currentActivity);
                    }
                }
            }
        }

        // Select from eligible activity list by activity list position
        Activity selectedActivity =  eligibleActivities.stream().
                min(Comparator.comparing(Activity::getActivityListPosition)).get();
        return selectedActivity.getActivityId();
    }

    /**
     * Generates a precedence feasible activity list
     * and sets activity object variable: activityListPosition
     */
    public void randomActivityList(){
        // Reset all necessary variables
        this.resetSchedule();

        // Create new random activity list
        ArrayList activityList = new ArrayList();
        for(int i = 0; i < this.listOfActivities.size(); i++) {
            ArrayList eligibleActivityIndices = this.eligibleActivitiesIndices();
            Integer choice = eligibleActivityIndices.get(new Random().nextInt(eligibleActivityIndices.size())).hashCode();
            this.listOfActivities.get(choice).scheduled = true;
            this.listOfActivities.get(choice).eligible = false;
            activityList.add(choice + 1);
        }

        this.resetSchedule();
        this.activityList = activityList;
    }

    public void setActivityListPosByActivityList(){
        for (int i = 0; i < this.activityList.size(); i++){
            this.listOfActivities.get(this.activityList.get(i).hashCode() - 1).setActivityListPosition(i);
        }
    }

    /**
     * Resets instance variables
     */
    private void resetSchedule(){
        for(int i = 0; i < this.listOfActivities.size(); i++) {
            Activity currentActivity = this.listOfActivities.get(i);
            currentActivity.eligible = false;
            currentActivity.scheduled = false;
            currentActivity.start = 0;
            currentActivity.finish = 0;
            currentActivity.earliestStart = 0;
            currentActivity.earliestFinish = 0;
            currentActivity.latestStart = 0;
            currentActivity.latestFinish = 0;
        }
        for (Resource resource:this.listOfResources) {
            Arrays.fill(resource.resourceProfile, 0);
        }
    }

    /**
     * Skip n line in BufferedReader
     * @param br
     * @param lines
     * @throws IOException
     */
    private static void skip(BufferedReader br, int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            br.readLine(); //skip
        }
    }




}
