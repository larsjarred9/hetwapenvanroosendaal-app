package com.example.hetwapenvanroosendaal.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.hetwapenvanroosendaal.ui.card.CardFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.EAN13Writer
import java.util.*

object EAN13Generator {
    fun generateEAN13Code(context: CardFragment, barcode: String): Drawable? {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 0 // Set the barcode margin to 0

        try {
            val writer = EAN13Writer()
            val bitMatrix: BitMatrix = writer.encode(barcode, BarcodeFormat.EAN_13, 600, 300, hints)

            // Convert the BitMatrix to a Bitmap
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            // Create a drawable from the bitmap
            return BitmapDrawable(context.resources, bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }
}