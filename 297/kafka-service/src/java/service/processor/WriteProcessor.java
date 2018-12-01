package service.processor;

import kafka.KafkaHelper;

/***
 *Created by Lin Cheng
 */
public class WriteProcessor extends PostProcessor {
    @Override
    String handle() {
        return KafkaHelper.write(paramMap.get("topic"), body);
    }
}
