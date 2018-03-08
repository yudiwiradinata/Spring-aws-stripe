package com.yudi.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yudi.exceptions.S3Exception;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by Yudi on 31/01/2018.
 */
@Service
public class S3Service {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final String PROFILE_PICTURE_FILENAME = "profilePicture";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3 s3Client;


    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param uploadedFile The multipart file uploaed by the user
     * @param username The username for which to upload this file
     * @return The URL of the uploaded image
     * @throws S3Exception If something goes wrong
     */
    public String storeProfileName(MultipartFile uploadedFile, String username) {

        String profileImageUrl = null;
        try {
            if (uploadedFile != null &&!uploadedFile.isEmpty()){
                byte[] bytes = uploadedFile.getBytes();

                //the root of our temporary assets. will create if doesn't exist
                File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);
                if(!tmpImageStoredFolder.exists()){
                    LOG.info("Creating temp folder");
                    tmpImageStoredFolder.mkdirs();
                }

                File tmpProfileImgFile = new File(tmpImageStoredFolder.getAbsolutePath()
                        + File.separatorChar
                        + PROFILE_PICTURE_FILENAME
                        + "."
                        + FilenameUtils.getExtension(uploadedFile.getOriginalFilename())
                );

                LOG.info("Temporary file will be saved to {}",tmpProfileImgFile.getAbsolutePath());

                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                        new File(tmpProfileImgFile.getAbsolutePath())))){
                    bos.write(bytes);
                }

                profileImageUrl = this.storeProfileNameToS3(tmpProfileImgFile, username);

                tmpProfileImgFile.delete();

            }
        }catch (IOException e){
            throw new S3Exception(e);
        }

        return profileImageUrl;
    }

    /**
     * Return the url where the bucket name is located
     * url doesn't contain bucketname
     * @param bucketName
     * @return the root URL where bucket name is located
     * @throws S3Exception If something goes wrong
     */
    private String ensureBucketExists(String bucketName){
        String bucketUrl = null;
        try{
            if(!s3Client.doesBucketExistV2(bucketName)){
                LOG.info("Bucket doesn't exist.... creating one");
                s3Client.createBucket(bucketName);
                LOG.info("Created bucket "+ bucketName);
            }
            bucketUrl =s3Client.getUrl(bucketName, null) + bucketName;
        }catch (AmazonClientException e){
            LOG.error("Error {}",bucketName,e);
            throw new S3Exception(e);
        }
        return bucketUrl;
    }


    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     * @param resource the file resource to upload S3
     * @param username
     * @return THE URL of the uploaded resource or null if a problem occured
     * @throws S3Exception If something goes wrong
     */
    private String storeProfileNameToS3(File resource, String username){
        String resourceUrl = null;
        if(!resource.exists()){
            LOG.error("file doesn't exist {}", resource.getAbsolutePath());
            throw  new S3Exception("The file "+resource.getAbsolutePath()+" doesn't exist");
        }
        String rootBucketUrl = this.ensureBucketExists(bucketName);
        if(null == rootBucketUrl){
            LOG.error("The bucket doesn't exist {}", rootBucketUrl);
        }else{
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username + "/" + PROFILE_PICTURE_FILENAME + "." + FilenameUtils.getExtension(resource.getName());
            try{
                s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
                resourceUrl = s3Client.getUrl(bucketName, key).toString();
            }catch (AmazonClientException e){
                LOG.error("Error {}",bucketName,e);
                throw new S3Exception(e);
            }
        }

        return resourceUrl;
    }

}
