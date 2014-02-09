package com.lutshe.doiter.services.assets;

import com.lutshe.doiter.dao.ImagesDao;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: Art
 */
@Path("/image")
@Component
public class ImagesService {

    @Autowired
    private ImagesDao imagesDao;

    @GET
    @Path("/{imageName}")
    @Produces("image/png")
    public byte[] getImage(@PathParam("imageName") String imageName) throws IOException {
        return imagesDao.getImageByName(imageName);
    }
}
