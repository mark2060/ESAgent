package cn.sephora.agent.elasticsearch;

import java.lang.instrument.Instrumentation;

/**
 * Created by Mark Wang on 2017/1/24.
 */

public class EsAgent {
    public static void premain(String options, Instrumentation ins) {
        ins.addTransformer(new EsClassFileTransformer());
    }
}
