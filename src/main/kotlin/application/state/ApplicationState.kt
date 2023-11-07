package application.state

import application.textStructure.GapBuffer
import application.textStructure.TextStructureInterface

class ApplicationState(var bufferObject: TextStructureInterface = GapBuffer(gapBufferSize = 4, text = "  frr  ")) {
    val printableCharacters =
        "`~ёЁ!1@2\"3№#4;$5%6:^7&?8*9(0)-_+=qwertyuiop[{]}asdfghjkl;':zxcvbnm,<.>/?йцукенгшщзхъ/\\|фывапролджэячсмитьбю., QWERTYUIOPLKJHGFDSAZXCVBNMЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ\n\t".toCharArray()

    fun showText(): String = bufferObject.showText()
}