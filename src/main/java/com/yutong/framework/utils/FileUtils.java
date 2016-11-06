package com.yutong.framework.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;


public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);


    /**
     * 读取classpath下路径下文件的String内容
     * 
     * @param filePath
     * @return
     */
    public static String contentToString(String filePath) {
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = getFileInputStream(filePath);
            bis = new BufferedInputStream(is);
            byte[] b = new byte[bis.available()];
            bis.read(b);
            return new String(b, "UTF-8");
        }
        catch (IOException e) {
            logger.error(e);
        }
        finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(bis);
        }
        return null;
    }


    public static InputStream getFileInputStream(String filePath)
        throws IOException {
        Resource resource = getResource(filePath);
        return resource.getInputStream();
    }


    public static Resource getResource(String filePath) throws IOException {
        ResourcePatternResolver resourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        return resourcePatternResolver
                .getResource(ResourceUtils.CLASSPATH_URL_PREFIX + filePath);
    }
}
