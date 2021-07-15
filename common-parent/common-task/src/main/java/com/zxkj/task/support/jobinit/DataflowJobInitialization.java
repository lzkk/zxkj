
package com.zxkj.task.support.jobinit;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.zxkj.task.support.ElasticJobProperties;

import java.util.Iterator;
import java.util.Map;

public class DataflowJobInitialization extends AbstractJobInitialization {
    private Map<String, ElasticJobProperties.DataflowConfiguration> dataflowConfigurationMap;

    public DataflowJobInitialization(Map<String, ElasticJobProperties.DataflowConfiguration> dataflowConfigurationMap) {
        this.dataflowConfigurationMap = dataflowConfigurationMap;
    }

    public void init() {
        Iterator var1 = this.dataflowConfigurationMap.keySet().iterator();

        while (var1.hasNext()) {
            String jobName = (String) var1.next();
            ElasticJobProperties.DataflowConfiguration configuration = (ElasticJobProperties.DataflowConfiguration) this.dataflowConfigurationMap.get(jobName);
            this.initJob(jobName, configuration.getJobType(), configuration);
        }

    }

    public JobTypeConfiguration getJobTypeConfiguration(String jobName, JobType jobType, ElasticJobProperties.JobConfiguration jobConfiguration, JobCoreConfiguration jobCoreConfiguration) {
        ElasticJobProperties.DataflowConfiguration configuration = (ElasticJobProperties.DataflowConfiguration) this.dataflowConfigurationMap.get(jobName);
        return new DataflowJobConfiguration(jobCoreConfiguration, configuration.getJobClass(), configuration.isStreamingProcess());
    }
}
