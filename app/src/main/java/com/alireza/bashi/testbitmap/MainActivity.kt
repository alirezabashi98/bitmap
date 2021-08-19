package com.alireza.bashi.testbitmap

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import kotlin.math.min


class MainActivity : AppCompatActivity() {

    lateinit var bmp: Bitmap
    lateinit var operation: Bitmap

    lateinit var mCanvas: Canvas


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bmp = mainActivity_imageView_testBitmap.drawable.toBitmap()

        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)

        setOnClick()


    }

    private fun setOnClick() {

        mainActivity_button_drawText.setOnClickListener {

            mainActivity_imageView_testBitmap.setImageBitmap(bmp.drawText())

        }
        mainActivity_button_drawLine.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.drawLine())
        }
        mainActivity_button_drawCircle.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.drawCircle())
        }
        mainActivity_button_drawRectangle.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.drawRectangle())
        }
        mainActivity_button_drawShadow.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.drawShadow())
        }
        mainActivity_button_addPadding.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.addPadding())
        }
        mainActivity_button_borderedCircularBitmap.setOnClickListener {
            mainActivity_imageView_testBitmap.setImageBitmap(bmp.borderedCircularBitmap())
        }
        mainActivity_button_blue.setOnClickListener { gray() }
        mainActivity_button_bright.setOnClickListener { bright() }
        mainActivity_button_dark.setOnClickListener { dark() }
        mainActivity_button_gama.setOnClickListener { gama() }
        mainActivity_button_green.setOnClickListener { green() }
        mainActivity_button_blue.setOnClickListener { blue() }
    }

    // utility bitmap


    // extension function to draw line on bitmap
    private fun Bitmap.drawLine(x: Float = 0f, y: Float = 555f): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            color = Color.RED
            strokeWidth = 10f

            // draw line on canvas
            canvas.drawLine(
                x, // The x-coordinate of the start point of the line
                x, // The y-coordinate of the start point of the line
                y, // stopX
                y, // stopY
                this // paint
            )
        }

        return bitmap
    }


    // extension function to draw circle on bitmap
    private fun Bitmap.drawCircle(
        radius: Float = 100f,
        color: Int = Color.YELLOW
    ): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            this.color = color

            // draw circle on left bottom of bitmap
            canvas.drawCircle(
                width / 2f, // The x-coordinate of the start point of the line
                height / 2f, // The y-coordinate of the start point of the line
                (width + height) / 4f, // stopX
                this // paint
            )
        }

        return bitmap
    }

    // extension function to draw rectangle on bitmap
    private fun Bitmap.drawRectangle(): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            color = Color.RED
            isAntiAlias = true

            // draw rectangle on canvas
            canvas.drawRect(
                20f, // left side of the rectangle to be drawn
                20f, // top side
                width / 3 - 20f, // right side
                height - 20f, // bottom side
                this
            )
        }

        return bitmap
    }

    // extension function to draw shadow on bitmap
    private fun Bitmap.drawShadow(): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width + 10,
            height + 10,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            isAntiAlias = true

            // draw shadow right and bottom side of bitmap
            /*
                This draws a shadow layer below the main layer,
                with the specified offset and color, and blur radius.
            */
            setShadowLayer(
                10f, // radius
                6f, // dx
                6f, // dy
                Color.BLACK // color
            )
        }

        canvas.drawBitmap(this, 0f, 0f, paint)
        return bitmap
    }

    // extension function to draw text on bitmap
    private fun Bitmap.drawText(
        text: String = "Red Lily",
        textSize: Float = 60f,
        color: Int = Color.RED
    ): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            this.color = color
            this.textSize = textSize
            typeface = Typeface.SERIF
            setShadowLayer(1f, 0f, 1f, Color.WHITE)
            canvas.drawText(text, width  / 4f , height - 20f, this)
        }

        return bitmap
    }

    // extension function to add padding to bitmap
    private fun Bitmap.addPadding(
        color: Int = Color.GRAY,
        left: Int = 0,
        top: Int = 0,
        right: Int = 0,
        bottom: Int = 0
    ): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width + left + right, // width in pixels
            height + top + bottom, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // draw solid color on full canvas area
        canvas.drawColor(color)

        // clear color from bitmap drawing area
        // this is very important for transparent bitmap borders
        // this will keep bitmap transparency
        Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawRect(
                Rect(left, top, bitmap.width - right, bitmap.height - bottom),
                this
            )
        }

        // finally, draw bitmap on canvas
        Paint().apply {
            canvas.drawBitmap(
                this@addPadding, // bitmap
                0f + left, // left
                0f + top, // top
                this // paint
            )
        }

        return bitmap
    }

    // extension function to add border to bitmap
    private fun Bitmap.addBorder(
        color: Int = Color.DKGRAY,
        borderWidth: Int = 10
    ): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width + borderWidth * 2, // width in pixels
            height + borderWidth * 2, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // draw solid color on full canvas area
        canvas.drawColor(color)

        // clear color from bitmap drawing area
        // this is very important for transparent bitmap border
        // this will keep bitmap transparency
        Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawRect(
                Rect(
                    borderWidth, // left
                    borderWidth, // top
                    bitmap.width - borderWidth, // right
                    bitmap.height - borderWidth // bottom
                ),
                this
            )
        }

        // finally, draw bitmap on canvas
        Paint().apply {
            canvas.drawBitmap(
                this@addBorder, // bitmap
                0f + borderWidth, // left
                0f + borderWidth, // top
                this // paint
            )
        }

        return bitmap
    }


    // extension function to get bitmap from assets
    private fun Context.assetsToBitmap(fileName: String): Bitmap? {
        return try {
            val stream = assets.open(fileName)
            BitmapFactory.decodeStream(stream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    // extension function to crop circular area from bitmap
    private fun Bitmap.cropCircularArea(): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width, // width in pixels
            height, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // create a circular path
        val path = Path()
        val radius = min(width / 2f, height / 2f)
        path.apply {
            addCircle(
                width / 2f,
                height / 2f,
                radius,
                Path.Direction.CCW
            )
        }

        // draw circular bitmap on canvas
        canvas.clipPath(path)
        canvas.drawBitmap(this, 0f, 0f, null)

        val diameter = (radius * 2).toInt()
        val x = (width - diameter) / 2
        val y = (height - diameter) / 2

        // return cropped circular bitmap
        return Bitmap.createBitmap(
            bitmap, // source bitmap
            x, // x coordinate of the first pixel in source
            y, // y coordinate of the first pixel in source
            diameter, // width
            diameter // height
        )
    }

    // extension function to create circular bitmap with border
    private fun Bitmap.borderedCircularBitmap(
        borderColor: Int = Color.LTGRAY,
        borderWidth: Int = 10
    ): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width, // width in pixels
            height, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        // canvas to draw circular bitmap
        val canvas = Canvas(bitmap)

        // get the maximum radius
        val radius = min(width / 2f, height / 2f)

        // create a path to draw circular bitmap border
        val borderPath = Path().apply {
            addCircle(
                width / 2f,
                height / 2f,
                radius,
                Path.Direction.CCW
            )
        }

        // draw border on circular bitmap
        canvas.clipPath(borderPath)
        canvas.drawColor(borderColor)


        // create a path for circular bitmap
        val bitmapPath = Path().apply {
            addCircle(
                width / 2f,
                height / 2f,
                radius - borderWidth,
                Path.Direction.CCW
            )
        }

        canvas.clipPath(bitmapPath)
        val paint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = true
        }

        // clear the circular bitmap drawing area
        // it will keep bitmap transparency
        canvas.drawBitmap(this, 0f, 0f, paint)

        // now draw the circular bitmap
        canvas.drawBitmap(this, 0f, 0f, null)


        val diameter = (radius * 2).toInt()
        val x = (width - diameter) / 2
        val y = (height - diameter) / 2

        // return cropped circular bitmap with border
        return Bitmap.createBitmap(
            bitmap, // source bitmap
            x, // x coordinate of the first pixel in source
            y, // y coordinate of the first pixel in source
            diameter, // width
            diameter // height
        )
    }


    // extension function to create rounded corners bitmap
    private fun Bitmap.toRoundedCorners(
        cornerRadius: Float = 25F
    ): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            width, // width in pixels
            height, // height in pixels
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        // path to draw rounded corners bitmap
        val path = Path().apply {
            addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                cornerRadius,
                cornerRadius,
                Path.Direction.CCW
            )
        }
        canvas.clipPath(path)

        // draw the rounded corners bitmap on canvas
        canvas.drawBitmap(this, 0f, 0f, null)
        return bitmap
    }

    // extension function to crop bitmap to square
    private fun Bitmap.toSquare(): Bitmap? {
        // get the small side of bitmap
        val side = min(width, height)

        // calculate the x and y offset
        val xOffset = (width - side) / 2
        val yOffset = (height - side) / 2

        // create a square bitmap
        // a square is closed, two dimensional shape with 4 equal sides
        return Bitmap.createBitmap(
            this, // source bitmap
            xOffset, // x coordinate of the first pixel in source
            yOffset, // y coordinate of the first pixel in source
            side, // width
            side // height
        )
    }

    // extension function to crop bitmap rectangle area
    private fun Bitmap.cropRectangle(
        xOffset: Int = 0,
        yOffset: Int = 0,
        newWidth: Int = this.width,
        newHeight: Int = this.height
    ): Bitmap? {
        return try {
            Bitmap.createBitmap(
                this, // source bitmap
                xOffset, // x coordinate of the first pixel in source
                yOffset, // y coordinate of the first pixel in source
                newWidth, // width in pixels
                newHeight // height in pixels
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    // extension function to crop bitmap center
// it return square bitmap when no parameters pass
    private fun Bitmap.cropCenter(
        newWidth: Int = min(width, height),
        newHeight: Int = min(width, height)
    ): Bitmap? {
        // calculate x and y offset
        val xOffset = (width - newWidth) / 2
        val yOffset = (height - newHeight) / 2

        return try {
            Bitmap.createBitmap(
                this, // source bitmap
                xOffset, // x coordinate of the first pixel in source
                yOffset, // y coordinate of the first pixel in source
                newWidth, // new width
                newHeight // new height
            )

        } catch (e: IllegalArgumentException) {
            null
        }
    }

    // extension function to remove bitmap transparency
    private fun Bitmap.removeTransparency(backgroundColor: Int = Color.WHITE): Bitmap? {
        val bitmap = copy(config, true)
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixel = getPixel(x, y)
                alpha = Color.alpha(pixel)
                red = Color.red(pixel)
                green = Color.green(pixel)
                blue = Color.blue(pixel)

                if (alpha == 0) {
                    // if pixel is full transparent then
                    // replace it by solid background color
                    bitmap.setPixel(x, y, backgroundColor)
                } else {
                    // if pixel is partially transparent then
                    // set pixel full opaque
                    val color = Color.argb(
                        255,
                        red,
                        green,
                        blue
                    )
                    bitmap.setPixel(x, y, color)
                }
            }
        }

        return bitmap
    }

    // extension function to mask bitmap
    private fun Bitmap.mask(mask: Bitmap): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            mask.width, mask.height, Bitmap.Config.ARGB_8888
        )

        // paint to mask
        val paint = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        Canvas(bitmap).apply {
            // draw source bitmap on canvas
            drawBitmap(this@mask, 0f, 0f, null)
            // mask bitmap
            drawBitmap(mask, 0f, 0f, paint)
        }

        return bitmap
    }


    // filter bitmap

    private fun gray() {

        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        val red = 0.33
        val green = 0.59
        val blue = 0.11

        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r: Int = Color.red(p)
                var g: Int = Color.green(p)
                var b: Int = Color.blue(p)
                r *= red.toInt()
                g *= green.toInt()
                b *= blue.toInt()
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)

    }

    private fun bright() {
        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                r += 100
                g += 100
                b += 100
                alpha += 100
                operation.setPixel(i, j, Color.argb(alpha, r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)
    }

    private fun dark() {
        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                r -= 50
                g -= 50
                b -= 50
                alpha -= 50
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)
    }

    private fun gama() {
        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                r += 150
                g = 0
                b = 0
                alpha = 0
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)
    }

    private fun green() {
        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                r = 0
                g += 150
                b = 0
                alpha = 0
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)
    }

    private fun blue() {
        operation = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                val p = bmp.getPixel(i, j)
                var r = Color.red(p)
                var g = Color.green(p)
                var b = Color.blue(p)
                var alpha = Color.alpha(p)
                r = 0
                g = 0
                b = b + 150
                alpha = 0
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b))
            }
        }
        mainActivity_imageView_testBitmap.setImageBitmap(operation)
    }

}