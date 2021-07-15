
package com.zxkj.task.support.jobinit;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.zxkj.task.support.ElasticJobProperties;

import java.util.Iterator;
import java.util.Map;

public class SimpleJobInitialization extends AbstractJobInitialization {
    private Map<String, ElasticJobProperties.SimpleConfiguration> simpleConfigurationMap;

    public SimpleJobInitialization(Map<String, ElasticJobProperties.SimpleConfiguration> simpleConfigurationMap) {
        this.simpleConfigurationMap = simpleConfigurationMap;
    }

    public void init() {
        Iterator var1 = this.simpleConfigurationMap.keySet().iterator();

        while (var1.hasNext()) {
            String jobName = (String) var1.next();
            ElasticJobProperties.SimpleConfiguration configuration = (ElasticJobProperties.SimpleConfiguration) this.simpleConfigurationMap.get(jobName);
            this.initJob(jobName, configuration.getJobType(), configuration);
        }

    }

    public JobTypeConfiguration getJobTypeConfiguration(String jobName, JobType jobType, ElasticJobProperties.JobConfiguration jobConfiguration, JobCoreConfiguration jobCoreConfiguration) {
        ElasticJobProperties.SimpleConfiguration configuration = (ElasticJobProperties.SimpleConfiguration) this.simpleConfigurationMap.get(jobName);
        return new SimpleJobConfiguration(jobCoreConfiguration, configuration.getJobClass());
    }
}
