//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.springframework.cloud.client.ServiceInstance;

import java.util.*;

public class NacosServiceDiscovery {
    private NacosDiscoveryProperties discoveryProperties;

    public NacosServiceDiscovery(NacosDiscoveryProperties discoveryProperties) {
        this.discoveryProperties = discoveryProperties;
    }

    public List<ServiceInstance> getInstances(String serviceId) throws NacosException {
        String group = this.discoveryProperties.getGroup();
        List<Instance> instances = this.discoveryProperties.namingServiceInstance().selectInstances(serviceId, group, true);
        return hostToServiceInstanceList(instances, serviceId, this.discoveryProperties.getMetadata());
    }

    public List<String> getServices() throws NacosException {
        String group = this.discoveryProperties.getGroup();
        ListView<String> services = this.discoveryProperties.namingServiceInstance().getServicesOfServer(1, 2147483647, group);
        return services.getData();
    }

    public static List<ServiceInstance> hostToServiceInstanceList(List<Instance> instances, String serviceId, Map<String, String> metadataMap) {
        List<ServiceInstance> result = new ArrayList(instances.size());
        Iterator var3 = instances.iterator();

        while (var3.hasNext()) {
            Instance instance = (Instance) var3.next();
            ServiceInstance serviceInstance = hostToServiceInstance(instance, serviceId, metadataMap);
            if (serviceInstance != null) {
                result.add(serviceInstance);
            }
        }

        return result;
    }

    public static ServiceInstance hostToServiceInstance(Instance instance, String serviceId, Map<String, String> metadataMap) {
        if (instance != null && instance.isEnabled() && instance.isHealthy()) {
            NacosServiceInstance nacosServiceInstance = new NacosServiceInstance();
            nacosServiceInstance.setHost(instance.getIp());
            nacosServiceInstance.setPort(instance.getPort());
            nacosServiceInstance.setServiceId(serviceId);
            Map<String, String> metadata = new HashMap();
            metadata.put("nacos.instanceId", instance.getInstanceId());
            metadata.put("nacos.weight", instance.getWeight() + "");
            metadata.put("nacos.healthy", instance.isHealthy() + "");
            metadata.put("nacos.cluster", instance.getClusterName() + "");
            metadata.putAll(instance.getMetadata());
            metadata.putAll(metadataMap);
            nacosServiceInstance.setMetadata(metadata);
            if (metadata.containsKey("secure")) {
                boolean secure = Boolean.parseBoolean((String) metadata.get("secure"));
                nacosServiceInstance.setSecure(secure);
            }

            return nacosServiceInstance;
        } else {
            return null;
        }
    }
}
