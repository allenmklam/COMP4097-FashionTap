package hk.edu.hkbu.comp.groupd.fashiontap.json

data class YoutubeChannelResponse(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)