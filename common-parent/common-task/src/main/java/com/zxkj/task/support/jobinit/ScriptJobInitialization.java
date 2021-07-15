
package com.zxkj.task.support.jobinit;

import com.dangdang.ddframe.job.api.JobType;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.zxkj.task.support.ElasticJobProperties;

import java.util.Iterator;
import java.util.Map;

public class ScriptJobInitialization extends AbstractJobInitialization {
    private Map<String, ElasticJobProperties.ScriptConfiguration> scriptConfigurationMap;

    public ScriptJobInitialization(Map<String, ElasticJobProperties.ScriptConfiguration> scriptConfigurationMap) {
        this.scriptConfigurationMap = scriptConfigurationMap;
    }

    public void init() {
        Iterator var1 = this.scriptConfigurationMap.keySet().iterator();

        while (var1.hasNext()) {
            String jobName = (String) var1.next();
            ElasticJobProperties.ScriptConfiguration configuration = (ElasticJobProperties.ScriptConfiguration) this.scriptConfigurationMap.get(jobName);
            this.initJob(jobName, configuration.getJobType(), configuration);
        }

    }

    public JobTypeConfiguration getJobTypeConfiguration(String jobName, JobType jobType, ElasticJobProperties.JobConfiguration jobConfiguration, JobCoreConfiguration jobCoreConfiguration) {
        ElasticJobProperties.ScriptConfiguration configuration = (ElasticJobProperties.ScriptConfiguration) this.scriptConfigurationMap.get(jobName);
        return new ScriptJobConfiguration(jobCoreConfiguration, configuration.getScriptCommandLine());
    }
}
