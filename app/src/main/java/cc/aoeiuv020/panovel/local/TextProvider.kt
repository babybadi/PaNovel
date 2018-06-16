package cc.aoeiuv020.panovel.local

import cc.aoeiuv020.panovel.api.NovelChapter
import cc.aoeiuv020.panovel.data.entity.Novel
import cc.aoeiuv020.panovel.local.text.TextContext
import java.nio.charset.Charset
import java.util.*

/**
 * Created by AoEiuV020 on 2018.06.13-15:36:18.
 */
class TextProvider(
        novel: Novel
) : LocalNovelProvider(novel) {
    // novel.chapters存文件编码，导入时就要判断过确实存在并合法，
    private val charset: Charset = Charset.forName(novel.nChapters)
    override val context: LocalNovelContext = TextContext(file, charset)

    // 虽然如果小说文件是复制到app内部后再解析阅读的，
    // 再怎么刷新也不会变，
    // 但还是统一留着准备直接读写可能改变的外部小说，
    override fun requestNovelChapters(): List<NovelChapter> {
        // 章节列表需要事先准备，
        // 不重复解析，
        context.prepare()
        val parser = context.parser
        // 每次刷新章节列表都重新解析一遍，
        parser.parse()

        // 解析章节列表后更新小说对象中的数据，
        update(novel, parser)

        return parser.chapters
                // 统一转成api模块里的章节类型，顺便map后的是随机访问的ArrayList,
                .map { NovelChapter(name = it.name, extra = it.extra) }
    }

    override fun clean() {
        file.delete()
    }

    companion object {
        // 因为要在导入时和刷新章节列表时调用，所以写在伴生对象里，
        fun update(novel: Novel, parser: LocalNovelParser) {
            novel.apply {
                introduction = parser.introduction ?: "(null)"
                checkUpdateTime = Date()
            }
            val list = parser.chapters
            novel.apply {
                chaptersCount = list.size
                if (readAtChapterIndex == 0) {
                    // 阅读至第一章代表没阅读过，保存第一章的章节名，
                    readAtChapterName = list.firstOrNull()?.name ?: "(null)"
                }
                lastChapterName = list.lastOrNull()?.name ?: "(null)"
            }
        }
    }
}