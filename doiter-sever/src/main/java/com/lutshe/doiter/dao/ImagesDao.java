package com.lutshe.doiter.dao;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @Author: Art
 */
@Component
public class ImagesDao {
    private static final Logger logger = LoggerFactory.getLogger(ImagesDao.class);

    private File imagesDir;

    @Autowired
    public ImagesDao(@Value("${images.dir}") String imagesRoot) {
        logger.info("using {} as images source root", imagesRoot);
        imagesDir = new File(imagesRoot);
        imagesDir.mkdirs();

        logger.info("images dir contains {} files and folders", imagesDir.list().length);
    }

    public byte[] getImageByName(String name) throws IOException {
        String fileName = imagesDir.getAbsolutePath() + File.separator + name;
        logger.info("{} requested", fileName);
        File file = new File(fileName);

        if (file.exists()) {
            return toBytes(file);
        }

        logger.warn("{} not found in {}", name, fileName);
        throw new FileNotFoundException();
    }

    public void addImage(String imageName, byte[] imageBites) throws IOException {
        String fileName = imagesDir.getAbsolutePath() + File.separator + imageName;
        logger.info("uploading {}", fileName);
        File file = new File(fileName);

        if (file.exists()) {
            logger.warn("{} already exists. discarding");
            throw new RuntimeException(fileName + " already exists");
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        IOUtils.write(imageBites, outputStream);
        IOUtils.closeQuietly(outputStream);
        logger.info("{} has been stored", fileName);
    }

    private byte[] toBytes(File file) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return ByteStreams.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
