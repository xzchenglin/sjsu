package service.processor;

import kafka.KafkaHelper;

/***
 *Created by Lin Cheng
 */
public class ReadProcessor extends GetProcessor {
    @Override
    String handle() {
        return KafkaHelper.read(paramMap.get("topic"));
    }
}
