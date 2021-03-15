package com.karimsinouh.youtixv2.data.items

import com.karimsinouh.youtixv2.data.ResourceId
import com.karimsinouh.youtixv2.data.Snippet

data class SearchItem (
    val kind:String,
    val etag:String,
    val snippet:Snippet,
    val id:ResourceId,
)