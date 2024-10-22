package com.example.shifr

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val substitutionMap = mapOf(
        "A" to "xxx", "B" to "xxy", "C" to "xxz", "D" to "xyx", "E" to "xyy", "F" to "xyz",
        "G" to "xzx", "H" to "xzy", "I" to "xzz", "J" to "yxx", "K" to "yxy", "L" to "yxz",
        "M" to "yyx", "N" to "yyy", "O" to "yyz", "P" to "yzx", "Q" to "yxy", "R" to "yzz",
        "S" to "zxx", "T" to "zxy", "U" to "zxz", "V" to "zyx", "W" to "zyy", "X" to "zyz",
        "Y" to "zzx", "Z" to "zzy", "a" to "xxx", "b" to "xxy", "c" to "xxz", "d" to "xyx",
        "e" to "xyy", "f" to "xyz", "g" to "xzx", "h" to "xzy", "i" to "xzz", "j" to "yxx",
        "k" to "yxy", "l" to "yxz", "m" to "yyx", "n" to "yyy", "o" to "yyz", "p" to "yzx",
        "q" to "yxy", "r" to "yzz", "s" to "zxx", "t" to "zxy", "u" to "zxz", "v" to "zyx",
        "w" to "zyy", "x" to "zyz", "y" to "zzx", "z" to "zzy", "." to "zzy", "," to "zzy",
        "!" to "zzy", "?" to "zzy", " " to "zzy",

        // Таблица подстановки
    )

    private val reverseMap = substitutionMap.entries.associate { (key, value) -> value to key }
    private lateinit var clipboard: ClipboardManager

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val editText: EditText = findViewById(R.id.editTextText)
        val encryptButton: Button = findViewById(R.id.button)
        val decryptButton: Button = findViewById(R.id.button2)
        val copyButton: TextView = findViewById(R.id.textViews3)
        val resultTextView: TextView = findViewById(R.id.textViews2)
        

        encryptButton.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                val encryptedText = encrypt(inputText)
                resultTextView.text = encryptedText
                visibleToggle()
            } else {
                Toast.makeText(this, "Введите текст для шифрования", Toast.LENGTH_SHORT).show()
            }
        }

        decryptButton.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                val decryptedText = decrypt(inputText)
                resultTextView.text = decryptedText
                visibleToggle()
            } else {
                Toast.makeText(this, "Введите текст для расшифровки", Toast.LENGTH_SHORT).show()
            }
        }

        copyButton.setOnClickListener {
            val textToCopy = resultTextView.text.toString()
            if (textToCopy.isNotEmpty()) {
                copyToClipboard(textToCopy)
            } else {
                Toast.makeText(this, "Нет текста для копирования", Toast.LENGTH_SHORT).show()
            }
        }


    }

    // Функция для шифрования входной строки
    private fun encrypt(input: String): String {
        // Применяем функцию map к каждому символу входной строки
        return input.map { char ->
            // Ищем соответствие символа в substitutionMap
            // Если соответствие найдено, возвращаем его, иначе возвращаем оригинальный символ
            substitutionMap[char.toString()] ?: char.toString()
        }.joinToString("") // Объединяем все символы обратно в строку
    }

    // Функция для расшифровки входной строки
    private fun decrypt(input: String): String {
        // Разбиваем входную строку на блоки по 3 символа
        return input.chunked(3).joinToString("") { block ->
            // Ищем соответствие блока в reverseMap
            // Если соответствие найдено, возвращаем его, иначе возвращаем оригинальный блок
            reverseMap[block] ?: block
        }
    }

    private fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("label", text) // Создаем ClipData с текстом
        clipboard.setPrimaryClip(clip) // Устанавливаем ClipData в буфер обмена
        Toast.makeText(this, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show() // Уведомляем пользователя
    }

    private fun visibleToggle() {
        val copyText: TextView = findViewById(R.id.textViews3)
        if (copyText.visibility == View.GONE) {
            copyText.visibility = View.VISIBLE // Показываем TextView
        }
    }

}
