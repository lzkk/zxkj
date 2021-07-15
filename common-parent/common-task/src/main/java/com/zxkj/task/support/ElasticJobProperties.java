
package com.zxkj.task.support;

import com.dangdang.ddframe.job.api.JobType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(
    prefix = "spring.elasticjob"
)
public class ElasticJobProperties {
    private ElasticJobProperties.ZkConfiguration zookeeper;
    private Map<String, ElasticJobProperties.SimpleConfiguration> simples = new LinkedHashMap();
    private Map<String, ElasticJobProperties.DataflowConfiguration> dataflows = new LinkedHashMap();
    private Map<String, ElasticJobProperties.ScriptConfiguration> scripts = new LinkedHashMap();

    public ElasticJobProperties() {
    }

    public ElasticJobProperties.ZkConfiguration getZookeeper() {
        return this.zookeeper;
    }

    public Map<String, ElasticJobProperties.SimpleConfiguration> getSimples() {
        return this.simples;
    }

    public Map<String, ElasticJobProperties.DataflowConfiguration> getDataflows() {
        return this.dataflows;
    }

    public Map<String, ElasticJobProperties.ScriptConfiguration> getScripts() {
        return this.scripts;
    }

    public void setZookeeper(ElasticJobProperties.ZkConfiguration zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setSimples(Map<String, ElasticJobProperties.SimpleConfiguration> simples) {
        this.simples = simples;
    }

    public void setDataflows(Map<String, ElasticJobProperties.DataflowConfiguration> dataflows) {
        this.dataflows = dataflows;
    }

    public void setScripts(Map<String, ElasticJobProperties.ScriptConfiguration> scripts) {
        this.scripts = scripts;
    }

    public static class JobConfiguration {
        private String jobClass;
        private String registryCenterRef = "elasticJobRegistryCenter";
        private String cron;
        private int shardingTotalCount = 1;
        private String shardingItemParameters = "0=A";
        private String jobInstanceId;
        private String jobParameter;
        private boolean monitorExecution = true;
        private int monitorPort = -1;
        private int maxTimeDiffSeconds = -1;
        private boolean failover = false;
        private boolean misfire = true;
        private String jobShardingStrategyClass;
        private String description;
        private boolean disabled = false;
        private boolean overwrite = true;
        private String jobExceptionHandler;
        private String executorServiceHandler;
        private int reconcileIntervalMinutes = 10;
        private String eventTraceRdbDataSource;
        private ElasticJobProperties.JobConfiguration.Listener listener;

        public JobConfiguration() {
        }

        public String getJobClass() {
            return this.jobClass;
        }

        public String getRegistryCenterRef() {
            return this.registryCenterRef;
        }

        public String getCron() {
            return this.cron;
        }

        public int getShardingTotalCount() {
            return this.shardingTotalCount;
        }

        public String getShardingItemParameters() {
            return this.shardingItemParameters;
        }

        public String getJobInstanceId() {
            return this.jobInstanceId;
        }

        public String getJobParameter() {
            return this.jobParameter;
        }

        public boolean isMonitorExecution() {
            return this.monitorExecution;
        }

        public int getMonitorPort() {
            return this.monitorPort;
        }

        public int getMaxTimeDiffSeconds() {
            return this.maxTimeDiffSeconds;
        }

        public boolean isFailover() {
            return this.failover;
        }

        public boolean isMisfire() {
            return this.misfire;
        }

        public String getJobShardingStrategyClass() {
            return this.jobShardingStrategyClass;
        }

        public String getDescription() {
            return this.description;
        }

        public boolean isDisabled() {
            return this.disabled;
        }

        public boolean isOverwrite() {
            return this.overwrite;
        }

        public String getJobExceptionHandler() {
            return this.jobExceptionHandler;
        }

        public String getExecutorServiceHandler() {
            return this.executorServiceHandler;
        }

        public int getReconcileIntervalMinutes() {
            return this.reconcileIntervalMinutes;
        }

        public String getEventTraceRdbDataSource() {
            return this.eventTraceRdbDataSource;
        }

        public ElasticJobProperties.JobConfiguration.Listener getListener() {
            return this.listener;
        }

        public void setJobClass(String jobClass) {
            this.jobClass = jobClass;
        }

        public void setRegistryCenterRef(String registryCenterRef) {
            this.registryCenterRef = registryCenterRef;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public void setShardingTotalCount(int shardingTotalCount) {
            this.shardingTotalCount = shardingTotalCount;
        }

        public void setShardingItemParameters(String shardingItemParameters) {
            this.shardingItemParameters = shardingItemParameters;
        }

        public void setJobInstanceId(String jobInstanceId) {
            this.jobInstanceId = jobInstanceId;
        }

        public void setJobParameter(String jobParameter) {
            this.jobParameter = jobParameter;
        }

        public void setMonitorExecution(boolean monitorExecution) {
            this.monitorExecution = monitorExecution;
        }

        public void setMonitorPort(int monitorPort) {
            this.monitorPort = monitorPort;
        }

        public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
            this.maxTimeDiffSeconds = maxTimeDiffSeconds;
        }

        public void setFailover(boolean failover) {
            this.failover = failover;
        }

        public void setMisfire(boolean misfire) {
            this.misfire = misfire;
        }

        public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
            this.jobShardingStrategyClass = jobShardingStrategyClass;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }

        public void setJobExceptionHandler(String jobExceptionHandler) {
            this.jobExceptionHandler = jobExceptionHandler;
        }

        public void setExecutorServiceHandler(String executorServiceHandler) {
            this.executorServiceHandler = executorServiceHandler;
        }

        public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
            this.reconcileIntervalMinutes = reconcileIntervalMinutes;
        }

        public void setEventTraceRdbDataSource(String eventTraceRdbDataSource) {
            this.eventTraceRdbDataSource = eventTraceRdbDataSource;
        }

        public void setListener(ElasticJobProperties.JobConfiguration.Listener listener) {
            this.listener = listener;
        }

        public static class Listener {
            String listenerClass;
            String distributedListenerClass;
            Long startedTimeoutMilliseconds = 9223372036854775807L;
            Long completedTimeoutMilliseconds = 9223372036854775807L;

            public Listener() {
            }

            public String getListenerClass() {
                return this.listenerClass;
            }

            public String getDistributedListenerClass() {
                return this.distributedListenerClass;
            }

            public Long getStartedTimeoutMilliseconds() {
                return this.startedTimeoutMilliseconds;
            }

            public Long getCompletedTimeoutMilliseconds() {
                return this.completedTimeoutMilliseconds;
            }

            public void setListenerClass(String listenerClass) {
                this.listenerClass = listenerClass;
            }

            public void setDistributedListenerClass(String distributedListenerClass) {
                this.distributedListenerClass = distributedListenerClass;
            }

            public void setStartedTimeoutMilliseconds(Long startedTimeoutMilliseconds) {
                this.startedTimeoutMilliseconds = startedTimeoutMilliseconds;
            }

            public void setCompletedTimeoutMilliseconds(Long completedTimeoutMilliseconds) {
                this.completedTimeoutMilliseconds = completedTimeoutMilliseconds;
            }
        }
    }

    public static class ScriptConfiguration extends ElasticJobProperties.JobConfiguration {
        private final JobType jobType;
        private String scriptCommandLine;

        public ScriptConfiguration() {
            this.jobType = JobType.SCRIPT;
        }

        public JobType getJobType() {
            return this.jobType;
        }

        public String getScriptCommandLine() {
            return this.scriptCommandLine;
        }

        public void setScriptCommandLine(String scriptCommandLine) {
            this.scriptCommandLine = scriptCommandLine;
        }
    }

    public static class DataflowConfiguration extends ElasticJobProperties.JobConfiguration {
        private final JobType jobType;
        private boolean streamingProcess;

        public DataflowConfiguration() {
            this.jobType = JobType.DATAFLOW;
            this.streamingProcess = false;
        }

        public JobType getJobType() {
            return this.jobType;
        }

        public boolean isStreamingProcess() {
            return this.streamingProcess;
        }

        public void setStreamingProcess(boolean streamingProcess) {
            this.streamingProcess = streamingProcess;
        }
    }

    public static class SimpleConfiguration extends ElasticJobProperties.JobConfiguration {
        private final JobType jobType;

        public SimpleConfiguration() {
            this.jobType = JobType.SIMPLE;
        }

        public JobType getJobType() {
            return this.jobType;
        }
    }

    public static class ZkConfiguration {
        private String serverLists;
        private String namespace;
        private int baseSleepTimeMilliseconds = 1000;
        private int maxSleepTimeMilliseconds = 3000;
        private int maxRetries = 3;
        private int connectionTimeoutMilliseconds = 15000;
        private int sessionTimeoutMilliseconds = 60000;
        private String digest;

        public ZkConfiguration() {
        }

        public String getServerLists() {
            return this.serverLists;
        }

        public String getNamespace() {
            return this.namespace;
        }

        public int getBaseSleepTimeMilliseconds() {
            return this.baseSleepTimeMilliseconds;
        }

        public int getMaxSleepTimeMilliseconds() {
            return this.maxSleepTimeMilliseconds;
        }

        public int getMaxRetries() {
            return this.maxRetries;
        }

        public int getConnectionTimeoutMilliseconds() {
            return this.connectionTimeoutMilliseconds;
        }

        public int getSessionTimeoutMilliseconds() {
            return this.sessionTimeoutMilliseconds;
        }

        public String getDigest() {
            return this.digest;
        }

        public void setServerLists(String serverLists) {
            this.serverLists = serverLists;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
            this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
        }

        public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
            this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
            this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
        }

        public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
            this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }
    }
}
