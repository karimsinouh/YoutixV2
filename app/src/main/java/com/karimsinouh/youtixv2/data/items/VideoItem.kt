package com.karimsinouh.youtixv2.data.items

import com.karimsinouh.youtixv2.data.ContentDetails
import com.karimsinouh.youtixv2.data.Snippet
import com.karimsinouh.youtixv2.data.Statistics

data class VideoItem (
    val kind:String,
    val etag:String,
    val snippet: Snippet,
    val id:String?=null,
    val contentDetails: ContentDetails?=null,
    val statistics:Statistics?=null
)
