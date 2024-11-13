package com.nezuko.data.di

import android.content.Context
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.nezuko.data.source.amazon.ApiRoute.ENDPOINT
import com.nezuko.data.source.amazon.ApiRoute.KEY_ID
import com.nezuko.data.source.amazon.ApiRoute.REGION
import com.nezuko.data.source.amazon.ApiRoute.SECRET
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object {
        @Provides
        @Singleton
        fun providesAmazonS3Client(): AmazonS3Client {
            Log.i("NetworkModuleTAG", "providesAmazonS3Client: ${Region.getRegion(REGION)}")
            Log.i("NetworkModuleTAG", "providesAmazonS3Client: ${REGION}")
            return AmazonS3Client(
                BasicAWSCredentials(
                    KEY_ID, SECRET
                )
            ).apply {
                endpoint = ENDPOINT
            }
        }

        @Provides
        @Singleton
        fun providesTransferUtility(
            client: AmazonS3Client,
            @ApplicationContext context: Context
        ): TransferUtility {
            return TransferUtility.builder()
                .context(context)
                .s3Client(client)
                .defaultBucket("profile-photo-history")
                .build()
        }
    }
}