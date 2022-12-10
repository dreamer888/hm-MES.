package com.dream.iot.client.mqtt.impl;

import java.util.List;
import java.util.stream.Collectors;

public class MqttSubscribeListenerManager {

    private List<MqttSubscribeListener> listeners;
    private static final String PatternOFPlus = "+";
    private static final String PatternOFShape = "#";

    public MqttSubscribeListenerManager(List<MqttSubscribeListener> listeners) {
        this.listeners = listeners;
    }

    public List<MqttSubscribeListener> matcher(String topic) {
        String[] topicSplit = topic.split("/");
        return listeners.stream().filter(listener -> {
            String topicName = listener.topic().topicName();
            if(topicName.equals(PatternOFShape) || topicName.equals("/#")) {
                return true;
            }

            String[] subscribeTopicSplit = topicName.split("/");
            for (int i=0; i<subscribeTopicSplit.length; i++) {
                String part = subscribeTopicSplit[i];
                if(part.equals(PatternOFPlus)) {
                    // 订阅子路径包含'+' 则发布路径在此子路径下必须有值
                    // e.g：test/+/topic  不匹配 test
                    if(i + 1 > topicSplit.length) {
                        return false;
                    } else {
                        continue;
                    }
                } else if(part.equals(PatternOFShape)) { // 说明订阅的topic已经是结尾了
                    // #模式必须再topic的结尾 比如：test/#/topic不合法   test/#合法
                    if(!topicName.endsWith(PatternOFShape)) {
                        return false;
                    } else if(i + 1 > topicSplit.length) {
                        // 订阅子路径包含'#' 则发布路径在此子路径下必须有值
                        // e.g：test/#  不匹配 test   匹配test/1
                        return false;
                    } else {
                        return true; // 其他情况均匹配
                    }
                } else if(i + 1 > topicSplit.length) {
                    return false;
                } else if(!(topicSplit[i].equals(subscribeTopicSplit[i]))){
                    return false;
                } else {
                    continue;
                }
            }

            if(topicSplit.length != subscribeTopicSplit.length) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    public List<MqttSubscribeListener> getListeners() {
        return listeners;
    }
}
