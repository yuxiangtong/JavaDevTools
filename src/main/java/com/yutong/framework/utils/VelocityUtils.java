package com.yutong.framework.utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


public class VelocityUtils {

    private static Logger logger = LogManager.getLogger(VelocityUtils.class);

    private static VelocityEngine velocityEngine = null;


    public static VelocityEngine getVelocityEngine() {
        if (velocityEngine == null) {
            logger.trace("开始初始化 VelocityEngine ");
            velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,
                    "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class",
                    ClasspathResourceLoader.class.getName());

            Properties properties = new Properties();
            try {
                InputStream in = Object.class
                        .getResourceAsStream("/velocity/volecity.properties");
                properties.load(in);
            }
            catch (Exception e) {
                logger.error("初始化velocityEngine时异常:", e.getMessage());
            }
            velocityEngine.init(properties);
        }
        return velocityEngine;
    }


    public static String contentToString(String templateName,
        Map<String, Object> map) {
        if (StringUtils.isEmpty(templateName)) {
            return null;
        }
        VelocityEngine velocityEngine = VelocityUtils.getVelocityEngine();
        VelocityContext velocityContext = new VelocityContext();

        if (map != null) {
            Iterator<Entry<String, Object>> iterator =
                    map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }

        StringWriter stringWriter = new StringWriter();

        Template template = velocityEngine.getTemplate(templateName);
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();
    }

}
