package com.showmiso.swipecontacts

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.core.content.ContextCompat
import kotlin.random.Random

class DrawableManager {


    companion object {
        fun changeDrawableSolidColor(context: Context, drawableId: Int): Drawable {
            val target = context.getDrawable(drawableId)
            val colors = randomColor()

//            if (target is ShapeDrawable) {
                val shapeDrawable: ShapeDrawable = target as ShapeDrawable
                shapeDrawable.paint.color = ContextCompat.getColor(context, colors[0])
                return shapeDrawable
//            } else if (target is ColorDrawable) {
//                val colorDrawable: ColorDrawable = target
//                colorDrawable.color = ContextCompat.getColor(context, colors[0])
//                return colorDrawable
//            }
        }

        fun randomColor(): Array<Int> {
            val randomNumber = Random.nextInt(colorSet.size / 2) * 2
            return arrayOf(colorSet[randomNumber], colorSet[randomNumber + 1])
        }

        private val colorSet = arrayOf(
            R.color.colorGreen,
            R.color.colorGreenDeep,
            R.color.colorBlue,
            R.color.colorBlueDeep,
            R.color.colorPurple,
            R.color.colorPurpleDeep,
            R.color.colorYellow,
            R.color.colorYellowDeep,
            R.color.colorOrange,
            R.color.colorOrangeDeep,
            R.color.colorRed,
            R.color.colorRedDeep
        )
    }

}