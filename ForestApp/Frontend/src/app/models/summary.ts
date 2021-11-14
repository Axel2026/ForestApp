export interface ISummary{

    numberOfDestroyedFields: Number;
    startingDate: Date;
    endingDate: Date;
    allDamagedFields: Map<string, number>;
    overallPercentageDestroyed: Number;
}
