package org.example.expert.domain.common.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class S3Uploader (
    private val amazonS3: AmazonS3
) {

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null

    fun uploadImageFromUrl(imageUrl: String): String {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            val inputStream = connection.getInputStream()

            val metadata = ObjectMetadata()
            metadata.contentLength = connection.contentLengthLong
            metadata.contentType = connection.contentType

            val fileName = UUID.randomUUID().toString() + extractExtension(imageUrl)

            amazonS3.putObject(bucket, fileName, inputStream, metadata)

            return amazonS3.getUrl(bucket, fileName).toString()
        } catch (e: Exception) {
            throw RuntimeException("이미지 업로드 실패")
        }
    }

    private fun extractExtension(imageUrl: String): String {
        try {
            val path = URL(imageUrl).path
            return path.substring(path.lastIndexOf("."))
        } catch (e: Exception) {
            return ".jpg"
        }
    }
}
