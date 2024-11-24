package com.scm.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.exceptionHelpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements  ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile picture, String filenameId) {
        
        // code likna hai jo image ko upload kr raha ho server pe .., it may be aws S3 or cloudinary...
        // here we are going to use Cloudinary ... to upload the image


        try {
            byte[] data = new byte[picture.getInputStream().available()];
            picture.getInputStream().read(data);

            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", filenameId
            ));

        // and return kr raha hoga URL 
        return this.getUrlFromPublicId(filenameId);
            
        } catch (Exception e) {
           e.printStackTrace();

           return null;
        }

    
    }


    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP)
        ).generate(publicId);

    }

}
