package dev.traydr.geef.domain.repository

import io.minio.*
import java.io.InputStream
import java.time.ZonedDateTime


class FileRepository(minioClient: MinioClient) {
    private val bucketName: String = "geef"
    private val client: MinioClient = minioClient

    init {
        try {
            val found = client.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build())
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build())
            } else {
                println("Bucket 'geef' already exists.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uploadFile(file: ByteArray, name: String, type: String) {
        client.putObject(PutObjectArgs.builder()
            .bucket(bucketName)
            .`object`(name)
            .stream(file.inputStream(), file.size.toLong(), -1)
            .contentType("image/$type")
            .build())
    }

    fun downloadFile(name: String): ByteArray {
        var foundFile: ByteArray = ByteArray(0)
        try {
            val stream: InputStream = client.getObject(GetObjectArgs.builder().
                bucket(bucketName).
                `object`(name)
                .build())
            foundFile = stream.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return foundFile
    }
}