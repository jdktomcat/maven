package com.jdktomcat.pack.instrument;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * 类描述：性能监视器代理
 *
 * @author 汤旗
 * @date 2019-11-07 15:23
 */
public class ProfilerMonitorAgent {

    static private Instrumentation inst = null;

    /**
     * This method is called before the application’s main-method is called,
     * when this agent is specified to the Java VM.
     **/
    public static void premain(String agentArgs, Instrumentation _inst) {
        // Initialize the static variables we use to track information.
        inst = _inst;
        // Set up the class-file transformer.
        ClassFileTransformer trans = new ProfilerMonitorFormer();
        inst.addTransformer(trans);
    }
}
