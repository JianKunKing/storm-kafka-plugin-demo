package com.jiankunking.stormkafka.topologies;


import com.jiankunking.stormkafka.bolts.CustomBolt;
import com.jiankunking.stormkafka.schemes.MessageScheme;
import com.jiankunking.stormkafka.util.PropertiesUtil;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by jiankunking on 2017/4/19 16:27.
 */
public class CustomCounterTopology {

    /**
     * 入口类，即提交任务的类
     *
     * @throws InterruptedException
     * @throws AlreadyAliveException
     * @throws InvalidTopologyException
     */
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        System.out.println("11111");
        PropertiesUtil propertiesUtil = new PropertiesUtil("/application.properties", false);
        Map propsMap = propertiesUtil.getAllProperty();
        String zks = propsMap.get("zk_hosts").toString();
        String topic = propsMap.get("kafka.topic").toString();
        String zkRoot = propsMap.get("zk_root").toString();
        String zkPort = propsMap.get("zk_port").toString();
        String zkId = propsMap.get("zk_id").toString();
        BrokerHosts brokerHosts = new ZkHosts(zks);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, zkRoot, zkId);
        spoutConfig.zkServers = Arrays.asList(zks.split(","));
        if (zkPort != null && zkPort.length() > 0) {
            spoutConfig.zkPort = Integer.parseInt(zkPort);
        } else {
            spoutConfig.zkPort = 2181;
        }
        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", new KafkaSpout(spoutConfig));
        builder.setBolt("customCounterBolt", new CustomBolt(), 1).shuffleGrouping("kafkaSpout");
        //Configuration
        Config conf = new Config();
        conf.setDebug(false);
        if (args != null && args.length > 0) {
            //提交到集群运行
            try {
                StormSubmitter.submitTopologyWithProgressBar("customCounterTopology", conf, builder.createTopology());
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        } else {
            conf.setMaxTaskParallelism(3);
            //本地模式运行
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("CustomCounterTopology", conf, builder.createTopology());
        }
    }
}
