package ru.profsoft.addressbook.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import ru.profsoft.addressbook.R
import kotlin.math.min

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CircleImageView(context, attrs, defStyleAttr) {
    private lateinit var textPaint: Paint
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas

    fun setInitials(initials: String) {
        val textBounds = Rect()
        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat())

        textPaint = Paint().apply {
            isAntiAlias = true
            this.textSize = (min(layoutParams.width, layoutParams.height) / 2.33f)
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
        }
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)

        bitmap =
            Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(getInitialsBackgroundColor(initials))
        canvas = Canvas(bitmap)
        canvas.drawText(
            initials,
            backgroundBounds.centerX(),
            backgroundBounds.centerY() - textBounds.exactCenterY(),
            textPaint
        )
        setImageBitmap(bitmap)
        invalidate()
    }

    private fun getInitialsBackgroundColor(initials: String): Int {
        return when (initials.hashCode() % 6) {
            0 -> resources.getColor(R.color.color_avatar_1, context.theme)
            1 -> resources.getColor(R.color.color_avatar_2, context.theme)
            2 -> resources.getColor(R.color.color_avatar_3, context.theme)
            3 -> resources.getColor(R.color.color_avatar_4, context.theme)
            4 -> resources.getColor(R.color.color_avatar_5, context.theme)
            else -> resources.getColor(R.color.color_avatar_6, context.theme)
        }
    }
}