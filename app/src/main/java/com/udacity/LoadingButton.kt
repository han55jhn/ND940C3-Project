package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // udacity
    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()

    //sizes for button
    private var measureTextButton = 0f
    private var buttonTextSize: Float = resources.getDimension(R.dimen.default_text_size)

    private var buttonText = "Download"

    private var displacement = 10

    //small circle
    private var smallCircleSize = 0f
    private var smallCircle = 0f

    //init the color
    private var normalColorButton = getColor(context, R.color.colorPrimary)
    private var buttonWhenLoading = getColor(context, R.color.colorPrimaryDark)
    private var smallCircleColor = getColor(context, R.color.colorAccent)


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {
                //nothing to show
            }
            ButtonState.Loading -> {
                setText("We are loading")
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
                valueAnimator.apply {
                    duration = 6000
                    addUpdateListener { animation ->
                        smallCircleSize = animation.animatedValue as Float
                        smallCircle = (widthSize.toFloat().div(356)).times(smallCircleSize)
                        invalidate()
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            smallCircleSize = 0f
                            when (buttonState) {
                                ButtonState.Loading -> {
                                    buttonState = ButtonState.Loading
                                }
                                else -> {}
                            }
                        }
                    })
                    start()

                }
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                resetValues()
                invalidate()
            }
        }

    }

    private fun resetValues() {
        smallCircleSize = 0f
        smallCircle = 0f
        setText("Download")
    }


    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            normalColorButton = getColor(R.styleable.LoadingButton_normalButtonColor, 0)
            buttonWhenLoading = getColor(R.styleable.LoadingButton_loadingButtonColor, 0)
            smallCircleColor = getColor(R.styleable.LoadingButton_smallCircleColor, 0)
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.default_text_size)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawButtonBackgroundColor(canvas)
        drawButtonBackgroundWhenLoading(canvas)
        drawTextButton(canvas)
        drawSmallYellowCircle(canvas)
    }

    private fun drawButtonBackgroundColor(canvas: Canvas?) {
        paint.color = normalColorButton
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawButtonBackgroundWhenLoading(canvas: Canvas?) {
        paint.color = buttonWhenLoading
        canvas?.drawRect(0f, 0f, smallCircleSize, heightSize.toFloat(), paint)
    }

    private fun drawSmallYellowCircle(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(
            widthSize.div(2).plus(measureTextButton.div(2)).plus(displacement),
            heightSize.div(3).minus(buttonTextSize.div(3))
        )
        paint.color = smallCircleColor
        canvas?.drawArc(
            RectF(0f, 0f, buttonTextSize, buttonTextSize),
            0f,
            smallCircle * 0.400f,
            true,
            paint
        )
        canvas?.restore()
    }

    private fun drawTextButton(canvas: Canvas?) {
        paint.color = Color.WHITE
        measureTextButton = paint.measureText(buttonText)
        canvas?.drawText(
            buttonText,
            widthSize.div(2) - measureTextButton.div(2),
            heightSize.div(2) - (paint.descent() + paint.ascent()).div(2),
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun setText(title: String) {
        this.buttonText = title

    }


}

