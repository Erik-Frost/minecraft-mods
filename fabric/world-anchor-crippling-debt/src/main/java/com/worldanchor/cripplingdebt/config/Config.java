package com.worldanchor.cripplingdebt.config;

import java.util.List;

public class Config {
    public Integer dayDurationDivider;
    public Integer nightDurationDivider;
    public Double multiplyEnemyHealthEachDayBy;
    public Double multiplyEnemyDamageEachDayBy;
    public List<Enemy> enemies = null;

    public double getDayCycleDuration() {
        return (14000D/ dayDurationDivider) + (10000D/ nightDurationDivider);
    }
    public double getDayDuration() {
        return (14000D/ dayDurationDivider);
    }
    public double getNightDuration() {
        return (10000D/ nightDurationDivider);
    }
}
