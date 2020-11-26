package ru.profsoft.addressbook.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.extensions.dpToPixels

open class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr){

    companion object {
        private val DEFAULT_BORDER_WIDTH = 2.dpToPixels
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BACKGROUND_COLOR = Color.BLACK
    }

    private var borderWidth: Int = DEFAULT_BORDER_WIDTH
    private var borderColor = DEFAULT_BORDER_COLOR
    private var backgroundColor = DEFAULT_BACKGROUND_COLOR

    private var civImage: Bitmap? = null
    private var civDrawable: Drawable? = null
    private var paintBorder: Paint = Paint().apply { isAntiAlias = true }
    private var paintBackground: Paint = Paint().apply { isAntiAlias = true }
    private var paint: Paint = Paint().apply { isAntiAlias = true }

    private var circleCenter: Int = 0
    private var heightCircle: Int = 0

    init {
        if(attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)

            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            a.recycle()
        }
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        backgroundColor = color
        background = ColorDrawable(Color.TRANSPARENT)
    }

    override fun getScaleType(): ScaleType =
        super.getScaleType().let { if (it == null || it != ScaleType.CENTER_INSIDE) ScaleType.CENTER_CROP else it }


    override fun onDraw(canvas: Canvas) {
        loadBitmap()

        val resultPaint = if(civImage == null)
            paintBackground.apply { color = backgroundColor } else paint

        val circleCenterWithBorder = circleCenter + borderWidth
        canvas.drawCircle(circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), paintBorder)
        canvas.drawCircle(circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), circleCenter.toFloat(), resultPaint)
    }

    private fun loadBitmap() {
        civDrawable = drawable
        civImage = drawableToBitmap(civDrawable)
        updateShader()
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        return when(drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> {
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0,0,canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
    }

    private fun updateShader() {
        civImage?.also {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val scale: Float
            val dx: Float
            val dy: Float

            when (scaleType) {
                ScaleType.CENTER_CROP -> if (it.width * height > width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                ScaleType.CENTER_INSIDE -> if (it.width * height < width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                else -> {
                    scale = 0f
                    dx = 0f
                    dy = 0f
                }
            }

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })

            paint.shader = shader
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    private fun update() {
        if(civImage != null) updateShader()

        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)

        heightCircle = Math.min(usableWidth, usableHeight)

        circleCenter = ((heightCircle - borderWidth * 2) / 2)
        paintBorder.color = borderColor
        invalidate()
    }
}