package com.doiter.services.images;

import com.doiter.services.common.Accept;
import com.doiter.services.common.Path;
import com.doiter.services.common.Rest;
import com.doiter.services.common.Service;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

/**
 * User: Artur
 * TODO move logic to other layer
 * TODO add proper error handling throughout the app
 * TODO anyone can upload & download??
 */
@Rest("/image")
public class ImagesService extends Service {
    private static final Logger logger = LoggerFactory.getLogger(ImagesService.class);

    private File imagesDir;

    @Autowired
    public void setupImagesDir(@Value("${images.dir}") String imagesRoot) throws IOException {
        logger.info("using {} as images source root", imagesRoot);
        imagesDir = new File(imagesRoot);
        imagesDir.mkdirs();

        logger.info("images dir contains {} files and folders", imagesDir.list().length);
    }

    @Path("/{imageName}")
    public byte[] getImage(String imageName) throws IOException {
        String fileName = imagesDir.getAbsolutePath() + File.separator + imageName;
        logger.info("{} requested", fileName);
        File file = new File(fileName);

        if (file.exists()) {
            return toBytes(file);
        }

        logger.warn("{} not found in {}", imageName, fileName);
        throw new FileNotFoundException();
    }

    @Path("/add/{imageName}")
    @Accept(byte[].class)
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
