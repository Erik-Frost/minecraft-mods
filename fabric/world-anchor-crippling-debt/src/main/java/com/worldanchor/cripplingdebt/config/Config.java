package com.worldanchor.cripplingdebt.config;

import java.util.List;

public class Config {
    public Integer dayDurationDivider;
    public Integer nightDurationDivider;
    public List<Enemy> enemies = null;

    public double getDayCycleDuration() {
        return (14000D/ dayDurationDivider) + (10000D/ nightDurationDivider);
    }
}
