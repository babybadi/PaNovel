package cc.aoeiuv020.panovel.refresher

import cc.aoeiuv020.panovel.api.NovelItem
import cc.aoeiuv020.panovel.server.ServerAddress
import cc.aoeiuv020.panovel.server.dal.model.autogen.Novel
import cc.aoeiuv020.panovel.server.service.NovelService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory

class Refresher {
    private val logger = LoggerFactory.getLogger(Refresher::class.java.simpleName)
    private lateinit var service: NovelService
    private var isRunning = false
    private val vipNovelList = mutableSetOf<Novel>()
    fun start(address: ServerAddress, config: Config = Config(), bookshelfList: MutableSet<String>) {
/*
        logger.info {
            "start address: ${address.data}"
        }
        logger.info {
            config
        }
        logger.info {
            "bookshelfList: $bookshelfList"
        }
        getBookshelf(bookshelfList, config.requireBookshelf)
        isRunning = true
        service = NovelServiceImpl(address)
        var lastTime = 0L
        var lastCount = 0
        while (isRunning) {
            try {
                // 当前时间，
                val currentTime = System.currentTimeMillis()
                // 本轮耗时，
                val roundTime = currentTime - lastTime
                if (roundTime < TimeUnit.SECONDS.toMillis(1)) {
                    // 无论如何，一轮耗时小于一秒的话，就休息一秒，
                    TimeUnit.SECONDS.sleep(1)
                }
                // 更新记录的时间，
                lastTime = currentTime
                var targetCount = (lastCount * (config.targetTime.toFloat() / roundTime)).toInt().let {
                    if (it <= 0 || it >= config.maxSize) {
                        config.maxSize
                    } else {
                        it
                    }
                }
                logger.info {
                    "roundTime: $roundTime, targetCount: $targetCount, lastCount: $lastCount"
                }
                lastCount = 0
                if (roundTime > config.minTime) {
                    targetCount -= vipNovelList.size
                    // vip每轮都刷新，以防万一太频繁，
                    vipNovelList.also {
                        lastCount += it.size
                    }.forEach { novel ->
                        refresh(novel)
                    }
                }
                service.needRefreshNovelList(targetCount)
                        .also {
                            if (it.isEmpty()) {
                                throw IllegalStateException("没有需要刷新的了，")
                            }
                        }
                        .also { lastCount += it.size }
                        .forEach { novel ->
                            refresh(novel)
                        }
            } catch (e: Exception) {
                logger.error(e) {
                    "请求需要刷新的小说列表失败，"
                }
                isRunning = false
            }
        }
*/
    }

    /**
     * 用于解析书架，要支持新版的requester,
     */
    private val gson: Gson = GsonBuilder()
            .create()

    private fun getBookshelf(bookshelfList: MutableSet<String>, requireBookshelf: Boolean) {
/*
        val paste = PasteUbuntu()
        bookshelfList.forEach { url ->
            logger.debug {
                "正在获取书架 $url"
            }
            try {
                if (!paste.check(url)) {
                    return@forEach
                }
                paste.download(url).toBean<BookListData>(gson).list.forEach {
                    logger.debug {
                        "获取到书架小说 $it"
                    }
                    val novel = Novel().apply {
                        requesterType = it.requester.type
                        requesterExtra = it.requester.extra
                        chaptersCount = 0
                    }
                    vipNovelList.add(novel)
                }
            } catch (e: Exception) {
                logger.error(e) {
                    "获取书架失败 $url"
                }
                if (requireBookshelf) {
                    throw e
                }
            }
        }
*/
    }

    private fun refresh(novel: Novel) {
/*
        logger.info {
            "refresh ${novel.requesterExtra}"
        }
        try {
            val detailRequester = Requester.deserialization(
                    novel.requesterType,
                    novel.requesterExtra
            )
            val context = NovelContext.getNovelContextByUrl(detailRequester.url)
            val detail = context.getNovelDetail(detailRequester)
            val chapters = context.getNovelChaptersAsc(detail.requester)
            val updateTime = chapters.last().update?.let {
                if (it > detail.update) {
                    it
                } else {
                    detail.update
                }
            } ?: detail.update.takeIf { it.time != 0L }
            val hasUpdate = chapters.size > novel.chaptersCount
                    || (updateTime != null && updateTime > novel.updateTime)
            novel.updateTime = updateTime
            novel.chaptersCount = chapters.size
            novel.modifyTime = Date()
            if (hasUpdate) {
                service.uploadUpdate(novel)
            } else {
                service.touch(novel)
            }
        } catch (e: Exception) {
            logger.error(e) {
                "刷新失败，${novel.requesterExtra}"
            }
        }
*/
    }

    fun stop() {
        isRunning = false
    }

    data class BookListData(val name: String, val list: MutableSet<NovelItem> = mutableSetOf())
}