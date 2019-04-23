package dev.doubledot.doki.api.models

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import dev.doubledot.doki.api.extensions.hasContent
import dev.doubledot.doki.api.extensions.round

data class DokiResponse(
    val name: String,
    val manufacturers: String,
    val url: String,
    val award: Int,
    val position: Int,
    val explanation: String,
    val userSolution: String,
    val devSolution: String?
) : Parcelable {

    private fun Int.toRgbaString(): String =
        "rgba(${Color.red(this)}, ${Color.green(this)}, ${Color.blue(this)}, ${(Color.alpha(this) / 255F).round(3)})"

    fun getHTMLContent(
        explanationTitle: String = "",
        solutionTitle: String = "",
        lineHeight: Float = 1.8F,
        maxImgWidth: Float = .75F,
        imgBorderColor: Int = Color.BLACK,
        textColor: Int = Color.BLACK,
        linksColor: Int = Color.BLUE,
        marginTopPx: Int = 0,
        marginRightPx: Int = 0,
        marginBottomPx: Int = marginTopPx,
        marginLeftPx: Int = marginRightPx
    ): String {
        val actualExplanationTitle = if (explanationTitle.hasContent()) "<h2>$explanationTitle</h2>" else ""
        val actualSolutionTitle = if (solutionTitle.hasContent()) "<h2>$solutionTitle</h2>" else ""
        return "<html><head>" +
                "<style>html{margin: ${marginTopPx}px ${marginRightPx}px ${marginBottomPx}px ${marginLeftPx}px;" +
                "color: ${textColor.toRgbaString()}; line-height: ${lineHeight}em;} a{color: ${linksColor.toRgbaString()}}" +
                " img{display: block;height: auto;max-width: ${maxImgWidth * 100}%;" +
                "margin-left: auto;margin-right: auto;border: 1px solid ${imgBorderColor.toRgbaString()}}</style>" +
                "</head><body>$actualExplanationTitle$explanation<br>$actualSolutionTitle$userSolution</body></html>"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DokiResponse) return false
        return other.name == name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + manufacturers.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + award.hashCode()
        return result
    }

    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(manufacturers)
        parcel.writeString(url)
        parcel.writeInt(award)
        parcel.writeInt(position)
        parcel.writeString(explanation)
        parcel.writeString(userSolution)
        parcel.writeString(devSolution)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DokiResponse> {
        override fun createFromParcel(parcel: Parcel): DokiResponse = DokiResponse(parcel)
        override fun newArray(size: Int): Array<DokiResponse?> = arrayOfNulls(size)
    }
}
