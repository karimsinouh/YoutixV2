package com.karimsinouh.youtixv2.data.items

import com.karimsinouh.youtixv2.data.ResourceId

data class SearchItem <SnippetType> (
    val kind:String,
    val etag:String,
    val snippet:SnippetType,
    val id:ResourceId,
)