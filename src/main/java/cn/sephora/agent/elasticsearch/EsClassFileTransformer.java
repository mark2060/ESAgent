package cn.sephora.agent.elasticsearch;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by Mark Wang on 2017/1/24.
 */
public class EsClassFileTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        //如果加载Business类才拦截
        if (className.equals("org/springframework/data/elasticsearch/core/ElasticsearchTemplate")) {
            try {
                //通过包名获取类文件, javassist的包名是用点分割的，需要转换下
                CtClass ctClass = ClassPool.getDefault().get(className.replaceAll("/", "."));
                //获得指定方法名的方法
                CtMethod method = ctClass.getDeclaredMethod("doSearch");
                //在方法执行后插入代码
                method.insertAfter("System.out.println(\"========================ElasticSearch DSL Start==================================\");" +
                        "System.out.println(searchRequest.internalBuilder().toString());" +
                        "System.out.println(\"========================ElasticSearch DSL End==================================\");");
                byte[] byteCode = ctClass.toBytecode();
                ctClass.detach();
                return byteCode;
            } catch (Exception e) {
                System.out.println("Elastic Search Agent Error!");
                System.out.println(e.toString());
            }
        }
        return null;
    }
}