package com.karimsinouh.youtixv2.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.api.RetrofitAPI
import com.karimsinouh.youtixv2.database.Database
import com.karimsinouh.youtixv2.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ocpsoft.prettytime.PrettyTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonsModule {

    @Provides
    @Singleton
    fun provideGlideInstance(@ApplicationContext c:Context)=Glide.with(c).setDefaultRequestOptions(
        RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
    )

    @Provides
    @Singleton
    fun provideRetrofitInstance()=
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitAPI::class.java)


    @Provides
    @Singleton
    fun provideDatabaseInstance(@ApplicationContext c:Context)=
            Room.databaseBuilder(c,Database::class.java,"database").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePrettyTimeInstance()=PrettyTime()



}