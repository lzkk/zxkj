package com.zxkj.task.dataFlow;


public class DataProcessFactory {
    private static DataProcess dataProcess = new DataProcess();

    public static DataProcess getDataProcess() {
        return dataProcess;
    }

}