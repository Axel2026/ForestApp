package com.agh.forest.Pojo;

import lombok.Data;
import com.agh.forest.Model.PollutionGases;

@Data
public class GroupingPollutionRatePojo {

    private PollutionGases components;
    private QualityRatePollutionPojo main;
}
