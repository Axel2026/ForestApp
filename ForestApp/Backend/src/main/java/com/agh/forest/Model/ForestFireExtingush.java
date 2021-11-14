package com.agh.forest.Model;

public enum ForestFireExtingush {
    LITTLE, TINY, VERY_SMALL, SMALL, MEDIUM, BIG, VERY_BIG, EXTREME, FULL;
        public ForestFireExtingush getNext() {
            return this.ordinal() < ForestFireExtingush.values().length - 1
                    ? ForestFireExtingush.values()[this.ordinal() + 1]
                    : null;
        }
    }

