package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    lateinit var txtInput: TextView //для вывода и ввода
    var lastNumeric: Boolean = false //если последняя кнопка, которая была нажата, была числом
    var stateError: Boolean = false //если произошла ошибка
    var lastDot: Boolean = false //если уже точка нажата, то нельзя второй раз нажать точку

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtInput = findViewById(R.id.txtInput)
    }

    fun onDigit(view: View) { //функция, которая добавляет текст кнопки(численной) в ввод
        if (stateError) { //если на выводе сейчас ошибка, то заменяем ее
            txtInput.text = (view as Button).text
            stateError = false //и обнуляем ошибку
        } else {
            txtInput.append((view as Button).text)
        }
        lastNumeric = true //ставим флаг на то, что последней нажатой кнопкой была численная
    }

    fun onDecimalPoint(view: View) { //функция для точки, если последняя нажатая была численная кнопка, нет точки и нет ошибки
        if (lastNumeric && !stateError && !lastDot) {
            txtInput.append(".") //добавляем на экран точку
            lastNumeric = false //нажата была не кнопка теперь
            lastDot = true //теперь точка одна есть
        }
    }

    fun onOperator(view: View) { //функция для добавления операции
        if (lastNumeric && !stateError) { //если последняя была нажата численная кнопка и нет ошибки
            txtInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onClear(view: View) {//функция для очищения экрана
        this.txtInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }


    fun onEqual(view: View) { //функция для вычисления выражения
        if (lastNumeric && !stateError) { //если последняя нажатая кнопка была численной и нет ошибки
            val txt = txtInput.text.toString()  //считываем выражение с ввода
            val expression = ExpressionBuilder(txt).build() //создаем выражение
            try {
                val result = expression.evaluate() //вычисляем выражение
                txtInput.text = result.toString() //выводим результат
                lastDot = true
            } catch (ex: ArithmeticException) {
                txtInput.text = "Error" //при ошибке выводим сообщение
                stateError = true
                lastNumeric = false
            }
        }
    }
}