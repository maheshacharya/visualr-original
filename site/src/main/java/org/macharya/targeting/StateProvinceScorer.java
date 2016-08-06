package org.macharya.targeting;

import com.onehippo.cms7.targeting.Scorer;
import com.onehippo.cms7.targeting.model.TargetGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by maheshacharya on 8/4/16.
 */
public class StateProvinceScorer implements Scorer<StateProvinceTargetingData> {
    private static final Logger log = LoggerFactory.getLogger(StateProvinceScorer.class);
    private Map<String, TargetGroup> targetGroups;

    public StateProvinceScorer() {
    }

    public void init(Map<String, TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public double evaluate(String targetGroupId, StateProvinceTargetingData targetingData) {
        if (targetingData == null) {
            return 0.0D;
        } else if (!this.targetGroups.containsKey(targetGroupId)) {
            log.warn("Cannot find target group with id \'{}\'. Return a score of 0.0", targetGroupId);
            return 0.0D;
        } else {
            TargetGroup targetGroup = (TargetGroup) this.targetGroups.get(targetGroupId);
            Iterator var4 = targetGroup.getProperties().keySet().iterator();

            String state;
            do {
                if (!var4.hasNext()) {
                    return 0.0D;
                }

                state = (String) var4.next();
            } while (!state.equalsIgnoreCase(targetingData.getState()));

            return 1.0D;
        }
    }
}


