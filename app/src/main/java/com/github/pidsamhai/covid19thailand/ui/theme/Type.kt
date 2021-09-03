package com.github.pidsamhai.covid19thailand.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.pidsamhai.covid19thailand.R


val IBMPlexLooped = FontFamily(
        Font(R.font.ibm_plex_sans_thai_looped_bold, FontWeight.Bold),
        Font(R.font.ibm_plex_sans_thai_looped_bold, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.ibm_plex_sans_thai_looped_medium, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.ibm_plex_sans_thai_looped_regular, FontWeight.Normal),
        Font(R.font.ibm_plex_sans_thai_looped_semi_bold, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
        defaultFontFamily = IBMPlexLooped,
        body1 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        )
        /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)