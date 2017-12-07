package cc.aoeiuv020.pager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cc.aoeiuv020.pager.animation.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 *
 * Created by AoEiuV020 on 2017.12.02-17:58:54.
 */
class Pager : View, PageAnimation.OnPageChangeListener, AnkoLogger {
    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var mAnim: PagerAnimation? = null
    private val listener = this
    private var direction = PagerDirection.NONE
    var drawer: IPagerDrawer = BlankPagerDrawer()
        set(value) {
            field = value
            resetDrawer()
        }
    var margins: Margins = Margins()
        set(value) {
            field = value
            resetAnim()
        }
    var animMode: AnimMode = AnimMode.SIMULATION
        set(value) {
            field = value
            resetAnim()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        resetAnim(w, h)
        resetDrawer()
    }

    override fun drawCurrent(backgroundCanvas: Canvas, nextCanvas: Canvas) {
        drawer.drawCurrentPage(backgroundCanvas, nextCanvas)
    }

    override fun hasPrev(): Boolean {
        debug { "prev" }
        direction = PagerDirection.PREV
        return drawer.scrollToPrev()
    }

    override fun hasNext(): Boolean {
        debug { "next" }
        direction = PagerDirection.NEXT
        return drawer.scrollToNext()
    }

    override fun pageCancel() {
        debug { "cancel" }
        when (direction) {
            PagerDirection.NEXT -> drawer.scrollToPrev()
            PagerDirection.PREV -> drawer.scrollToNext()
            PagerDirection.NONE -> {
            }
        }
        refresh()
    }

    fun refresh() {
        mAnim?.refresh()
    }

    /**
     * 如果视图没初始化就不重置动画，
     */
    private fun resetAnim() {
        if (width != 0 && height != 0) {
            resetAnim(width, height)
        }
    }

    private fun resetDrawer() {
        if (width != 0 && height != 0) {
            drawer.attach(this,
                    Size(width, height),
                    Size(width - margins.left - margins.right, height - margins.top - margins.bottom)
            )
        }
    }

    private fun resetAnim(w: Int, h: Int) {
        mAnim = when (animMode) {
            AnimMode.SIMULATION -> SimulationPageAnim(w, h, margins, this, listener)
            AnimMode.COVER -> CoverPageAnim(w, h, margins, this, listener)
            AnimMode.SLIDE -> SlidePageAnim(w, h, margins, this, listener)
            AnimMode.NONE -> NonePageAnim(w, h, margins, this, listener)
            AnimMode.SCROLL -> ScrollPageAnim(w, h, margins, this, listener)
        }
    }

    override fun onDraw(canvas: Canvas) {
        mAnim?.draw(canvas)
    }

    override fun computeScroll() {
        mAnim?.scrollAnim()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mAnim?.onTouchEvent(event) ?: false
    }
}