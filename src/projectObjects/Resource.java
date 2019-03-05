/*
Resource class

Author: Robert Brachmann
Date:   29.09.2017

The resource object/class is a superclass to inherent subclasses
for different scheduling problems and solving algorithms

Instance variables:
    resourceId
    resourceProfile
    resourceUpperBound
    resourceLowerBound
 */

package projectObjects;

import java.util.Arrays;

public class Resource {

    // Resource id
    private int resourceId;
    // Resource profile list with variable length / periods
    public Integer[] resourceProfile;
    // Resource upper bound
    public int resourceUpperBound;
    // Resource lower bound
    public int resourceLowerBound;
    // Planning horizon
    private int planningHorizon;

    // Constructor
    public Resource(int resourceId,
                    int resourceUpperBound,
                    int planningHorizon){
        this.resourceId = resourceId;
        this.resourceUpperBound = resourceUpperBound;
        this.resourceLowerBound = 0;
        this.planningHorizon = planningHorizon;
        this.resourceProfile = new Integer[planningHorizon];
        Arrays.fill(this.resourceProfile, 0);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", resourceProfile=" + resourceProfile.length +
                ", resourceUpperBound=" + resourceUpperBound +
                ", resourceLowerBound=" + resourceLowerBound +
                ", planningHorizon=" + planningHorizon +
                '}';
    }

    public String toStringResourceProfile(Integer toPeriod){
        StringBuilder resultString =
                new StringBuilder();
        resultString.append("[");
        for (int i = 0; i <= toPeriod; i++){
            if (this.resourceProfile[i].toString().length()==1){
                resultString.append("  " + this.resourceProfile[i].toString()+ " ");
            }
            if (this.resourceProfile[i].toString().length()==2){
                resultString.append(" " + this.resourceProfile[i].toString()+ " ");
            }
            if (this.resourceProfile[i].toString().length()==3){
                resultString.append("" + this.resourceProfile[i].toString()+ " ");
            }

        }
        resultString.append("]");
        return resultString.toString();
    }

    public int getResourceId() {
        return resourceId;
    }
}

